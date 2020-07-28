/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo;

import static edu.ie3.util.quantities.PowerSystemUnits.*;

import com.google.common.collect.Lists;
import edu.ie3.util.copy.DeepCopy;
import edu.ie3.util.exceptions.GeoPreparationException;
import edu.ie3.util.quantities.PowerSystemUnits;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Area;
import javax.measure.quantity.Length;
import net.morbz.osmonaut.geometry.Bounds;
import net.morbz.osmonaut.geometry.Polygon;
import net.morbz.osmonaut.osm.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tec.uom.se.ComparableQuantity;
import tec.uom.se.quantity.Quantities;

/** Functionality to deal with geographical and geometric information */
public class GeoUtils {
  private static final Logger logger = LoggerFactory.getLogger(GeoUtils.class);

  /** radius of the earth in m */
  public static final ComparableQuantity<Length> EARTH_RADIUS =
      Quantities.getQuantity(6378137.0, METRE);

  /** Offer a default geometry factory for the WGS84 coordinate system */
  public static final GeometryFactory DEFAULT_GEOMETRY_FACTORY =
      new GeometryFactory(new PrecisionModel(), 4326);

  protected GeoUtils() {
    throw new IllegalStateException("Utility classes cannot be instantiated");
  }

  /**
   * Calculates the distance in km between two lat/long points using the haversine formula
   *
   * @param lat1 Latitude value of the first coordinate
   * @param lng1 Longitude value of the first coordinate
   * @param lat2 Latitude value of the second coordinate
   * @param lng2 Longitude value of the second coordinate
   * @return The distance between both coordinates in {@link PowerSystemUnits#KILOMETRE}
   */
  public static ComparableQuantity<Length> haversine(
      double lat1, double lng1, double lat2, double lng2) {
    ComparableQuantity<Length> r = EARTH_RADIUS.to(KILOMETRE); // average radius of the earth in km

    ComparableQuantity<Angle> dLat = Quantities.getQuantity(Math.toRadians(lat2 - lat1), RADIAN);
    ComparableQuantity<Angle> dLon = Quantities.getQuantity(Math.toRadians(lng2 - lng1), RADIAN);
    double a =
        Math.sin(dLat.getValue().doubleValue() / 2) * Math.sin(dLat.getValue().doubleValue() / 2)
            + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon.getValue().doubleValue() / 2)
                * Math.sin(dLon.getValue().doubleValue() / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return r.multiply(c);
  }

  /**
   * Calculates the geo position as a Point from a given Latlon (net.morbz.osmonaut.osm). Uses the
   * WGS84 reference system.
   *
   * @param latLon Latlon from which the geo position shall be calculated
   * @return calculated Point from the given Latlon
   */
  public static org.locationtech.jts.geom.Point latlonToPoint(LatLon latLon) {
    return xyToPoint(latLon.getLon(), latLon.getLat());
  }

  /**
   * Wraps XY values in a JTS geometry point
   *
   * @param x longitude value
   * @param y latitude value
   * @return JTS geometry Point
   */
  public static org.locationtech.jts.geom.Point xyToPoint(double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, 0);
    return DEFAULT_GEOMETRY_FACTORY.createPoint(coordinate);
  }

  /**
   * This method takes all {@link RelationMember}s and joins the given ways to have closed ways.
   * This only works, if the ways are connected all in a line.
   *
   * @param relation {@link Relation} relation to treat
   * @return Deep copy of the {@code relation} with closed ways
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static Relation buildClosedWays(Relation relation) throws GeoPreparationException {
    /* Copy relation and empty the Members */
    Relation closedRelation = DeepCopy.copy(relation);
    closedRelation
        .getMembers()
        .removeAll(
            closedRelation.getMembers().stream()
                .filter(e -> e.getEntity() instanceof Way)
                .collect(Collectors.toList()));

    List<Way> ways =
        relation.getMembers().stream()
            .filter(e -> e.getEntity() instanceof Way)
            .map(e -> (Way) e.getEntity())
            .collect(Collectors.toList());

    /* Get an idea, of which ways do have intersections and where */
    HashMap<Node, Set<Way>> intersections = new HashMap<>();
    List<Way> comparativeWays = new LinkedList<>(ways);
    for (Way way1 : ways) {
      List<Node> nodes1 = way1.getNodes();

      for (Way way2 : comparativeWays) {
        if (way1.equals(way2)) continue;
        List<Node> nodes2 = way2.getNodes();
        Set<Node> sharedNodes =
            nodes1.stream().filter(nodes2::contains).collect(Collectors.toSet());
        for (Node node : sharedNodes) {
          intersections.putIfAbsent(node, new HashSet<>());
          intersections.get(node).add(way1);
          intersections.get(node).add(way2);
        }
      }

      // TODO: When there are no shared nodes, iterate through both ways once again and find
      // matching nodes based on a radius search
      comparativeWays.remove(way1);
    }

    if (intersections.values().stream().anyMatch(s -> s.size() > 2))
      throw new GeoPreparationException(
          "There is at least one intersection apparent with more then two ways, which would result in a concave hull curve. Exiting here.");

    /* As long as there are untreated ways iterate through them an build closed ways */
    while (!intersections.isEmpty()) {
      /* Take the first intersection node and it's adjacent ways. Remove those ways, to mark them as travelled */
      Node firstIntersection = intersections.keySet().iterator().next();
      Way closedWay = null;

      do {
        if (closedWay != null && intersections.get(firstIntersection).isEmpty()) {
          logger.warn(
              "There are no more intersections left, but the way is not closed right now. Adding the first node once again.");
          closedWay.getNodes().add(closedWay.getNodes().get(0));
          continue;
        }
        Way currentWay = intersections.get(firstIntersection).iterator().next();
        intersections.get(firstIntersection).remove(currentWay);

        /* Find the next way in order to get the two intersections on the current way */
        List<Node> candidateNodes =
            intersections.entrySet().stream()
                .filter(e -> e.getValue().contains(currentWay))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        Node secondIntersection;
        if (candidateNodes.size() > 1)
          throw new GeoPreparationException(
              "There is an intersection, where more then two ways meet. This is not supported, yet.");
        else if (candidateNodes.isEmpty()) {
          /* There is no more way. Close the way by adding the start node ones again */
          if (closedWay == null) {
            logger.warn("There is only one way in this relation.");
            closedWay = currentWay;
          } else if (!currentWay.getNodes().contains(closedWay.getNodes().get(0))) {
            throw new GeoPreparationException("Ran into an dead end.");
          }
          secondIntersection = closedWay.getNodes().get(0);
        } else {
          secondIntersection = candidateNodes.get(0);
          intersections.get(secondIntersection).remove(currentWay);
        }

        int[] nodePos =
            new int[] {
              currentWay.getNodes().indexOf(firstIntersection),
              currentWay.getNodes().indexOf(secondIntersection)
            };
        LinkedList<Node> nodesToAdd;
        if (nodePos[0] <= nodePos[1]) {
          /* Next way can be added in ascending order */
          nodesToAdd = new LinkedList<>(currentWay.getNodes().subList(nodePos[0], nodePos[1] + 1));
        } else {
          /* The intersection is at the end of the next way. Next way can be added in descending order */
          nodesToAdd = new LinkedList<>(currentWay.getNodes().subList(nodePos[1], nodePos[0] + 1));
          Collections.reverse(nodesToAdd);
        }

        /* If there is no buffer way, yet. Create a new one based on the part way identified first */
        if (closedWay == null)
          closedWay = new Way(currentWay.getId(), currentWay.getTags(), nodesToAdd);
        else closedWay.getNodes().addAll(nodesToAdd);

        /* Go one step ahead */
        firstIntersection = secondIntersection;
      } while (!closedWay.isClosed());

      /* Remove all travelled intersections */
      intersections.entrySet().removeIf(e -> e.getValue().isEmpty());

      /* Add the closed way to the relation */
      closedRelation.getMembers().add(new RelationMember(closedWay, "outer"));
    }

    return closedRelation;
  }

  /**
   * This method is manly inspired by the following internet sources:<br>
   * https://en.wikipedia.org/wiki/Convex_hull_algorithms <br>
   * https://en.wikipedia.org/wiki/Chan%27s_algorithm
   *
   * @param points The {@link Set} of {@link com.vividsolutions.jts.geom.Point}s that shall be
   *     enclosed by the convex hull
   * @param precision Prescision to use for "dirty casting"
   * @param algorithm {@link ConvexHullAlgorithm} to use. CAUTION {@link ConvexHullAlgorithm#CHAN}
   *     currently not working!
   * @return The {@link Polygon} of the convex hull
   * @throws GeoPreparationException If points got missing, no boundary can be found, too few points
   *     are provided or anything else wents wron
   * @throws NullPointerException If there is null
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static Polygon buildConvexHull(
      Set<com.vividsolutions.jts.geom.Point> points, int precision, ConvexHullAlgorithm algorithm)
      throws GeoPreparationException {
    /* "Cast" the points to java.awt.Point */
    Set<Point> candidatePoints =
        points.stream()
            .map(
                p ->
                    new Point(
                        (int) (p.getX() * Math.pow(10, precision)),
                        (int) (p.getY() * Math.pow(10, precision))))
            .collect(Collectors.toSet());

    if (candidatePoints.size() != points.size())
      throw new GeoPreparationException(
          "Some points got lost while converting to java.awt classes.");

    /*
     * Reduce computational complexity by removing all nodes that certainly do not belong the the convex hull
     * (Akl–Toussaint heuristic). Find the bounding coordinates and those four points having
     *    1) the minimum lat value,
     *    2) the minimum lon value,
     *    3) the maximum lat value,
     *    4) the maximum lon value
     * denoting a rectangle with the nodes certainly not in the convex hull. */
    OptionalDouble possiblyXMin = candidatePoints.stream().mapToDouble(Point::getX).min();
    OptionalDouble possiblyXMax = candidatePoints.stream().mapToDouble(Point::getX).max();
    OptionalDouble possiblyYMin = candidatePoints.stream().mapToDouble(Point::getY).min();
    OptionalDouble possiblyYMax = candidatePoints.stream().mapToDouble(Point::getY).max();
    if (!(possiblyXMax.isPresent()
        && possiblyXMin.isPresent()
        && possiblyYMax.isPresent()
        && possiblyYMin.isPresent()))
      throw new GeoPreparationException("Unable to get boundary rectangle of the node set.");

    double[] xBounds = new double[] {possiblyXMin.getAsDouble(), possiblyXMax.getAsDouble()};
    double[] yBounds = new double[] {possiblyYMin.getAsDouble(), possiblyYMax.getAsDouble()};

    HashSet<Point> edgePoints = new HashSet<>();
    edgePoints.add(candidatePoints.stream().filter(p -> p.getX() == xBounds[0]).findFirst().get());
    edgePoints.add(candidatePoints.stream().filter(p -> p.getX() == xBounds[1]).findFirst().get());
    edgePoints.add(candidatePoints.stream().filter(p -> p.getY() == yBounds[0]).findFirst().get());
    edgePoints.add(candidatePoints.stream().filter(p -> p.getY() == yBounds[1]).findFirst().get());
    int[] x = edgePoints.stream().mapToInt(p -> p.x).toArray();
    int[] y = edgePoints.stream().mapToInt(p -> p.y).toArray();
    java.awt.Polygon filterQuadrilateral = new java.awt.Polygon(x, y, x.length);

    candidatePoints.removeIf(p -> filterQuadrilateral.contains(p) && !edgePoints.contains(p));

    int pointCount = candidatePoints.size();

    LinkedList<Point> hullPoints = new LinkedList<>();

    double maxIterations = Math.log(Math.log(candidatePoints.size())) - 1;
    if (maxIterations < 1 || algorithm.equals(ConvexHullAlgorithm.GRAHAM)) {
      if (candidatePoints.size() < 3)
        throw new GeoPreparationException("Cannot build a convex hull for less than three nodes.");
      hullPoints.addAll(GrahamScan.getConvexHull(new ArrayList<>(candidatePoints)));
    } else {
      /* TODO: Currently not working! */
      /* Chan's algorithm (https://en.wikipedia.org/wiki/Chan%27s_algorithm) */
      Point point0 = new Point(-Integer.MAX_VALUE, 0);
      Point point1 = edgePoints.stream().filter(p -> p.getY() == yBounds[0]).findFirst().get();
      hullPoints.addLast(point1);

      /* Limit the amount of iterations to at most log(log(nodeCount)) */
      outloop:
      for (int iterCount = 0; iterCount < maxIterations; iterCount++) {
        double m = Math.pow(2, Math.pow(2, iterCount + 1));

        /* Split the candidate nodes into floor(pointCount/m) with about m points each subsets and calculate those convex hulls */
        int subsetCount = (int) Math.ceil((double) pointCount / m);
        int pointsPerSubset = (int) Math.ceil((double) pointCount / subsetCount);

        LinkedList<List<Point>> subSets = new LinkedList<>();
        subSets.addLast(new ArrayList<>());
        for (Point point : candidatePoints) {
          if (subSets.getLast().size() >= pointsPerSubset) subSets.addLast(new ArrayList<>());
          subSets.getLast().add(point);
        }

        if (subSets.stream().anyMatch(s -> s.size() < 3)) {
          throw new GeoPreparationException(
              "The algorithm lead to a segmentation with less than three nodes, wherefore no convex hull may be built.");
        }

        /* Build convex hull of each subset using Graham scan */
        LinkedList<List<Point>> subHulls = new LinkedList<>();
        for (List<Point> subPoints : subSets) {
          subHulls.addLast(GrahamScan.getConvexHull(subPoints));
        }

        /* Perform jarvis binary search to build the convex hull of all points based on the sub hulls */
        for (int cntSearch = 0; cntSearch < m; cntSearch++) {
          /* Collect all points beloging to the subhulls */
          List<Point> subHullPoints = new ArrayList<>();

          /* Perform Jarvis binary search:
           * Find the point in each convex hull maximising the angle between the last point of the overall hull
           * and that point */
          Point pA, pB;
          if (hullPoints.size() == 1) {
            pB = hullPoints.getLast();
            pA = point0;
          } else {
            pB = hullPoints.getLast();
            pA = hullPoints.get(hullPoints.size() - 2);
          }

          double previousAngle = Math.atan2(pB.getY() - pA.getY(), pB.getX() - pA.getX());

          for (List<Point> subHull : subHulls) {
            /* Calculate the difference between the two straights */
            Point bestPoint = null;
            double maxAngleDifference = Double.MIN_VALUE;
            for (Point point : subHull) {
              double angleDifference =
                  previousAngle - Math.atan2(pB.getY() - point.getY(), pB.getX() - point.getX());
              if (angleDifference > maxAngleDifference) {
                maxAngleDifference = angleDifference;
                bestPoint = point;
              }
            }
            subHullPoints.add(bestPoint);
          }

          /* Calculate the difference between the two straights */
          Point nextPoint = null;
          double maxAngleDifference = Double.MIN_VALUE;
          for (Point point : subHullPoints) {
            double angleDifference =
                previousAngle - Math.atan2(pB.getY() - point.getY(), pB.getX() - point.getX());
            if (angleDifference > maxAngleDifference) {
              maxAngleDifference = angleDifference;
              nextPoint = point;
            }
          }
          if (nextPoint == null)
            throw new GeoPreparationException("Cannot find a next node to add to the convex hull.");

          hullPoints.addLast(nextPoint);

          /* The hull is closed, therefore exit the search and give back the hull */
          if (hullPoints.getFirst().equals(hullPoints.getLast())) break outloop;
        }
      }

      throw new GeoPreparationException(
          "Maximum amount of iterations reached, but no convex hull found...");
    }

    /* The hull is completed. Cast Point back to Node and build a polygon */
    int castCount = -1;
    List<Node> hullNodes = new LinkedList<>();
    for (Point point : hullPoints) {
      hullNodes.add(
          new Node(
              castCount++,
              null,
              new LatLon(
                  point.getY() / Math.pow(10, precision), point.getX() / Math.pow(10, precision))));
    }
    Way hullWay = new Way(0, null, hullNodes);
    if (!hullWay.isClosed()) {
      logger.warn("Closed way manually.");
      hullWay.getNodes().add(hullWay.getNodes().get(0));
    }
    return new Polygon(hullWay);
  }

  /**
   * Calculates the intersection of two {@link Polygon}s {@code a} and {@code b}.
   *
   * @param a First {@link Polygon}
   * @param b Second {@link Polygon}
   * @return A {@link Polygon} if the intersection exists and null if not
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static Polygon getIntersection(Polygon a, Polygon b) {
    List<LatLon> sharedCoords =
        a.getCoords().stream().filter(b::contains).collect(Collectors.toList());
    List<LatLon> additionalCoords =
        b.getCoords().stream().filter(a::contains).collect(Collectors.toList());

    if (sharedCoords.isEmpty() && additionalCoords.isEmpty()) return null;
    else {
      /*
       * If the first node of the additional nodes has a higher distance to the last one of the shared nodes as to
       * the first one, then inverse the order of the additional nodes
       */
      if (sharedCoords.isEmpty()) sharedCoords.addAll(additionalCoords);
      else if (!additionalCoords.isEmpty()
          && haversine(
                      additionalCoords.get(0).getLat(),
                      additionalCoords.get(0).getLon(),
                      sharedCoords.get(sharedCoords.size() - 1).getLat(),
                      sharedCoords.get(sharedCoords.size() - 1).getLon())
                  .getValue()
                  .doubleValue()
              > haversine(
                      additionalCoords.get(0).getLat(),
                      additionalCoords.get(0).getLon(),
                      sharedCoords.get(0).getLat(),
                      sharedCoords.get(0).getLon())
                  .getValue()
                  .doubleValue()) {
        additionalCoords = Lists.reverse(additionalCoords);
      }

      additionalCoords.removeAll(sharedCoords); // Remove duplicates
      sharedCoords.addAll(additionalCoords);

      return new Polygon(sharedCoords);
    }
  }

  /**
   * Calculates the area, which is surrounded by a closed way by the help of {@link
   * GeoUtils#getArea(Polygon)}
   *
   * @param w Closed way, that surrounds the area
   * @return The covered area in {@link PowerSystemUnits#SQUARE_METRE}
   * @throws GeoPreparationException If some serious shit happens
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static Quantity<Area> getArea(Way w) throws GeoPreparationException {
    if (!w.isClosed())
      throw new GeoPreparationException(
          "Cannot determine the area covered by a way, that is not closed");
    return getArea(new Polygon(w));
  }

  /**
   * Calculate the area of an {@link Polygon} by adding subareas spanned between the longitude axis
   * and the line segments on the polygon
   *
   * @param p {@link Polygon} whos area may be calculated
   * @return The spanned area in {@link PowerSystemUnits#SQUARE_METRE}
   * @throws GeoPreparationException If some serious shit happens
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static Quantity<Area> getArea(Polygon p) throws GeoPreparationException {
    /* Get the boundary of the Polygon */
    Bounds bounds = p.getBounds();
    double latMaxGlobal = bounds.getMaxLat();
    double lonMinGlobal = bounds.getMinLon();

    /* Order the points in clockwise direction starting with the one with the highest y coordinate..
     * The "way" has to be closed (containing the start coordinate twice). But it may not be the "original" start
     * coordinate, but the one with the highest latitutde. Otherwise the partial areas would be added incorrect.
     * Therefore remove the duplicate coordinates (original start coordinate) and add the one with highest latitude
     * once again.*/
    List<LatLon> coords = new LinkedList<>(p.getCoords());
    Map<LatLon, Integer> duplicateCoords =
        coords.stream()
            .filter(coord -> Collections.frequency(coords, coord) > 1)
            .distinct()
            .collect(
                Collectors.toMap(coord -> coord, coord -> Collections.frequency(coords, coord)));
    // Only remove one of the duplicate coords
    Iterator<LatLon> it = coords.iterator();
    while (it.hasNext()) {
      LatLon coord = it.next();
      if (duplicateCoords.containsKey(coord) && duplicateCoords.get(coord) > 1) {
        duplicateCoords.put(coord, duplicateCoords.get(coord) - 1);
        it.remove();
      }
    }
    Optional<LatLon> optStartCoord =
        coords.stream().filter(c -> c.getLat() == latMaxGlobal).findFirst();
    if (!optStartCoord.isPresent())
      throw new GeoPreparationException(
          "Did not find a suitable coordinate although a defined maximum latitude has been found...");

    LinkedList<LatLon> orderedCoords = new LinkedList<>();
    LatLon startCoord = optStartCoord.get();
    int idxStart = coords.indexOf(startCoord);
    int idxNext = idxStart + 1 == coords.size() ? 0 : idxStart + 1;
    if (coords.get(idxNext).getLon() > coords.get(idxStart).getLon()) {
      /* Order is already clockwise */
      orderedCoords.addAll(coords.subList(idxStart, coords.size()));
      orderedCoords.addAll(coords.subList(0, idxStart));
    } else {
      /* Order is counterclockwise */
      orderedCoords.addAll(Lists.reverse(coords.subList(0, idxNext)));
      orderedCoords.addAll(Lists.reverse(coords.subList(idxNext, coords.size())));
    }
    orderedCoords.addLast(orderedCoords.get(0));

    /* Calculate the area by summing up the partial areas.
     * Take two points. The distance between the mean of their longitudes and the polygons global longitude
     * as well as the distance of both latitudes does do form the partial areas.
     * Those of the right hand side (distance of lats > 0) do form positive areas, whereas the left hand side
     * is subtracted. */
    Quantity<Area> area = Quantities.getQuantity(0d, SQUARE_METRE);
    /* Go through the coordinates and calculate the partial areas */
    int idxPrevious = orderedCoords.size() - 1;
    for (int idx = 0; idx < orderedCoords.size(); idx++) {
      LatLon coord = orderedCoords.get(idx);
      LatLon coordPrev = orderedCoords.get(idxPrevious);

      double maxLat = Math.max(coord.getLat(), coordPrev.getLat());
      double minLat = Math.min(coord.getLat(), coordPrev.getLat());
      double maxLon = Math.max(coord.getLon(), coordPrev.getLon());
      double minLon = Math.min(coord.getLon(), coordPrev.getLon());

      /* This partial area obviously would be zero. */
      if (maxLat == minLat || maxLon == minLon) {
        idxPrevious = idx;
        continue;
      }

      double meanLon = (maxLon + minLon) / 2;

      Quantity<Length> dX = haversine(maxLat, meanLon, maxLat, lonMinGlobal);
      Quantity<Length> dY = haversine(maxLat, meanLon, minLat, meanLon);
      Quantity<Area> partialArea = dX.multiply(dY).asType(Area.class).to(SQUARE_METRE);

      if (coord.getLat() < coordPrev.getLat()) {
        /* Right hand side*/
        area = area.add(partialArea);
      } else {
        /* Left hand side */
        area = area.subtract(partialArea);
      }

      /* logger.debug("Partial area: (" + dX + " * " + (coord.getLat() > coordPrev.getLat() ? dY.multiply(-1) : dY) +
      " = " + partialArea + "), current total area: " + area); */

      idxPrevious = idx;
    }

    return area;
  }

  /**
   * Checks if Node c is between Node a and b
   *
   * @param a Node A
   * @param b Node B
   * @param c Node C
   * @return True if node c is between node a and node b
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static boolean isBetween(Node a, Node b, Node c) {
    double crossProduct;
    double dotProduct;
    double squaredLengthBA;
    double epsilon = 0.000000000001; // epsilon to check if a,b and c are aligned
    // TODO: as the size of epsilon is crucial for the functionality, it should be available as an
    // option
    // lon = x
    // lat = y

    crossProduct =
        ((c.getLatlon().getLat() - a.getLatlon().getLat())
                * (b.getLatlon().getLon() - a.getLatlon().getLon())
            - (c.getLatlon().getLon() - a.getLatlon().getLon())
                * (b.getLatlon().getLat() - a.getLatlon().getLat()));
    if (Math.abs(crossProduct) > epsilon) return false;

    dotProduct =
        (c.getLatlon().getLon() - a.getLatlon().getLon())
                * (b.getLatlon().getLon() - a.getLatlon().getLon())
            + (c.getLatlon().getLat() - a.getLatlon().getLat())
                * (b.getLatlon().getLat() - a.getLatlon().getLat());

    if (dotProduct < 0) return false;

    squaredLengthBA =
        (b.getLatlon().getLon() - a.getLatlon().getLon())
                * (b.getLatlon().getLon() - a.getLatlon().getLon())
            + (b.getLatlon().getLat() - a.getLatlon().getLat())
                * (b.getLatlon().getLat() - a.getLatlon().getLat());

    if (dotProduct > squaredLengthBA) return false;

    return !(dotProduct > squaredLengthBA) || !(Math.abs(crossProduct) > epsilon);
  }

  /**
   * Calculates the area of a polygon in geo coordinates to an area in square kilometers NOTE: It
   * may be possible, that (compared to the real building area), the size of the building area may
   * be overestimated. To take this into account an optional correction factor might be used.
   *
   * @param geoArea: the area of the building based on geo coordinates
   * @param cor: the optional correction factor
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static Quantity<Area> calcGeo2qm(double geoArea, Quantity<Area> cor) {
    double width = 51.5;
    double length = 7.401;

    double square = Math.sqrt(geoArea);
    double width2 = (width + square) / 180 * Math.PI;
    double length2 = length / 180 * Math.PI;
    double width3 = width / 180 * Math.PI;
    double length3 = (length + square) / 180 * Math.PI;
    width = width / 180 * Math.PI;
    length = length / 180 * Math.PI;

    double e1 =
        Math.acos(
                Math.sin(width) * Math.sin(width2)
                    + Math.cos(width) * Math.cos(width2) * Math.cos(length2 - length))
            * EARTH_RADIUS.getValue().doubleValue();
    double e2 =
        Math.acos(
                Math.sin(width) * Math.sin(width3)
                    + Math.cos(width) * Math.cos(width3) * Math.cos(length3 - length))
            * EARTH_RADIUS.getValue().doubleValue();

    /* (e1 * e2) - cor */
    Quantity<Area> area =
        Quantities.getQuantity(e1, METRE)
            .multiply(Quantities.getQuantity(e2, METRE))
            .asType(Area.class)
            .subtract(cor);

    return area;
  }

  /** @deprecated This method is currently not under test and has to be revised thoroughly */
  @Deprecated
  public static boolean isInsideLanduse(LatLon node, List<Way> landUses) {
    for (Way landUse : landUses) {
      if (rayCasting(new Polygon(landUse), node)) return true;
    }
    return false;
  }

  /** @deprecated This method is currently not under test and has to be revised thoroughly */
  @Deprecated
  public static boolean rayCasting(Polygon shape, LatLon node) {
    boolean inside = false;

    // get lat lon from shape nodes
    List<LatLon> shapeNodes = shape.getCoords();

    for (int i = 1; i < shapeNodes.size(); i++) {
      if (intersects(shapeNodes.get(i - 1), shapeNodes.get((i)), node)) inside = !inside;
    }
    return inside;
  }

  /** @deprecated This method is currently not under test and has to be revised thoroughly */
  @Deprecated
  private static boolean intersects(LatLon a, LatLon b, LatLon n) {

    // convert LatLons to arrays
    double[] A = {a.getLon(), a.getLat()};
    double[] B = {b.getLon(), b.getLat()};
    double[] P = {n.getLon(), n.getLat()};

    if (A[1] > B[1]) return intersects(b, a, n);

    if (P[1] == A[1] || P[1] == B[1]) P[1] += 0.0001;

    if (P[1] > B[1] || P[1] < A[1] || P[0] > Math.max(A[0], B[0])) return false;

    if (P[0] < Math.min(A[0], B[0])) return true;

    double red = (P[1] - A[1]) / (P[0] - A[0]);
    double blue = (B[1] - A[1]) / (B[0] - A[0]);
    return red >= blue;
  }

  /**
   * The algorithm assumes the usual mathematical convention that positive y points upwards. In
   * computer systems where positive y is downwards (most of them) the easiest thing to do is list
   * the vertices counter-clockwise using the "positive y down" coordinates. The two effects then
   * cancel out to produce a positive area.
   *
   * @param building
   * @return polygon area A
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static double calculateBuildingArea(Way building) {
    double area = 0.0;

    // extract number of polygon points
    int j =
        building.getNodes().size()
            - 1; // -1 because the last vertex is the 'previous' one to the first
    // x = long
    // y = lat
    for (int i = 0; i < building.getNodes().size(); i++) {
      area =
          area
              + ((building.getNodes().get(j).getLatlon().getLon()
                      + building.getNodes().get(i).getLatlon().getLon())
                  * (building.getNodes().get(j).getLatlon().getLat()
                      - building.getNodes().get(i).getLatlon().getLat()));
      j = i;
    }

    area = Math.abs(area);

    return area / 2;
  }

  /**
   * Draws a circle with a radius of the provided distance around the provided center coordinates
   * and returns the result as a drawable polygon (one point per degree)
   *
   * @param center coordinate of the circle's center
   * @param radius radius of the circle
   * @return a polygon without the center, but with all points of the circle
   */
  public static Polygon radiusWithCircleAsPolygon(LatLon center, Quantity<Length> radius) {

    List<LatLon> circlePoints = radiusWithCircle(center, radius);

    List<LatLon> circlePointsLatLon =
        circlePoints.stream()
            .map(point -> new LatLon(point.getLat(), point.getLon()))
            .collect(Collectors.toList());

    Polygon res = new Polygon(circlePointsLatLon);

    return res;
  }

  /**
   * Draws a circle with a radius of the provided distance around the provided center coordinates
   * and returns the result as list of all circle points (one per degree)
   *
   * @param center
   * @param radius
   * @return a list with all coordinates of the circle points
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static List<LatLon> radiusWithCircle(LatLon center, Quantity<Length> radius) {

    double lat1 = Math.toRadians(center.getLat());
    double lon1 = Math.toRadians(center.getLon());
    double d = (radius.divide(EARTH_RADIUS)).getValue().doubleValue();

    List<LatLon> locs = new ArrayList<>();

    for (int x = 0; x <= 360; x++) {

      double brng = Math.toRadians(x);

      double latRad =
          Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1) * Math.sin(d) * Math.cos(brng));
      double lonRad =
          lon1
              + Math.atan2(
                  Math.sin(brng) * Math.sin(d) * Math.cos(lat1),
                  Math.cos(d) - Math.sin(lat1) * Math.sin(latRad));

      locs.add(new LatLon(Math.toDegrees(latRad), Math.toDegrees(lonRad)));
    }

    return locs;
  }

  /**
   * Chains together several ways which are close together (e.g. as part of a relation) to one,
   * correctly ordered way. This is a computational expensive method which might be worth
   * refactoring!
   *
   * @param waysToChain the ways from a relation
   * @param radius the radius that should be considered for search for the next connection point
   *     between two ways
   * @param wayId the id of the newly created way
   * @return
   * @deprecated This method is currently not under test and has to be revised thoroughly
   */
  @Deprecated
  public static Way wayFromWays(List<Way> waysToChain, Quantity<Length> radius, int wayId) {

    LinkedList<Way> waysCopy = new LinkedList<>();
    waysCopy.addAll(waysToChain);

    HashMap<Node, Way> nodeToWayMap = new HashMap<>();
    for (Way way : waysToChain) {
      way.getNodes().stream().forEach(node -> nodeToWayMap.put(node, way));
    }

    LinkedList<Node> nodesList = new LinkedList<>();
    nodesList.addAll(waysToChain.get(0).getNodes());
    waysCopy.remove(waysToChain.get(0));

    while (waysCopy.size() > 0) {
      Node lastNode = nodesList.getLast();

      Polygon circle = radiusWithCircleAsPolygon(lastNode.getLatlon(), radius);

      double distance = Double.POSITIVE_INFINITY;
      Way nextWay = null;
      Node nextLastNode = null;
      Iterator<Node> it = nodeToWayMap.keySet().iterator();
      while (it.hasNext()) {
        Node node = it.next();
        if (rayCasting(circle, node.getLatlon())) {
          double tempDistance =
              haversine(
                      lastNode.getLatlon().getLat(),
                      lastNode.getLatlon().getLon(),
                      node.getLatlon().getLat(),
                      node.getLatlon().getLon())
                  .to(KILOMETRE)
                  .getValue()
                  .doubleValue();
          if (tempDistance < distance) {
            distance = tempDistance;
            nextWay = nodeToWayMap.get(node);
            nextLastNode = node;
          }
        }
      }

      if (nextWay.getNodes().indexOf(nextLastNode) == nextWay.getNodes().size() - 1) {
        Collections.reverse(nextWay.getNodes());
        nodesList.addAll(nextWay.getNodes());
      } else nodesList.addAll(nextWay.getNodes());

      logger.debug("Removing way with id {}", nextWay.getId());

      nodeToWayMap.values().removeAll(Collections.singleton(nextWay));
      waysCopy.remove(nextWay);
    }

    Way result = new Way(wayId, new Tags(), nodesList);

    return result;
  }

  public enum ConvexHullAlgorithm {
    CHAN,
    GRAHAM
  }
}
