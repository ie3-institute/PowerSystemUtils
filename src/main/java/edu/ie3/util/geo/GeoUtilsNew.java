/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo;

import static edu.ie3.util.quantities.PowerSystemUnits.*;

import com.google.common.collect.Lists;
import edu.ie3.util.exceptions.GeoPreparationException;
import edu.ie3.util.quantities.PowerSystemUnits;
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
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

/** Functionality to deal with geographical and geometric information */
public class GeoUtilsNew {
  private static final Logger logger = LoggerFactory.getLogger(GeoUtilsNew.class);

  /** radius of the earth in m */
  public static final ComparableQuantity<Length> EARTH_RADIUS =
      Quantities.getQuantity(6378137.0, METRE);

  /** Offer a default geometry factory for the WGS84 coordinate system */
  public static final GeometryFactory DEFAULT_GEOMETRY_FACTORY =
      new GeometryFactory(new PrecisionModel(), 4326);

  protected GeoUtilsNew() {
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
        if (GeoUtils.rayCasting(circle, node.getLatlon())) {
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
