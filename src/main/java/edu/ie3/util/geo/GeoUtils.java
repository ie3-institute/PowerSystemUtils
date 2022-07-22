/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo;

import static edu.ie3.util.quantities.PowerSystemUnits.*;
import static java.lang.Math.*;

import edu.ie3.util.exceptions.GeoException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.math.Vector2D;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

public class GeoUtils {
  public static final GeometryFactory DEFAULT_GEOMETRY_FACTORY =
      new GeometryFactory(new PrecisionModel(), 4326);

  private static final ComparableQuantity<Length> EARTH_RADIUS =
      Quantities.getQuantity(6378137.0, METRE);

  protected GeoUtils() {
    throw new IllegalStateException("Utility classes cannot be instantiated.");
  }

  /**
   * Convert a given {@link LineString} with at least two points into a 'safe to be compared' {@link
   * LineString} This is done by removing duplicates in the points in the provided linestring as
   * well as a small change of the start coordinate if the linestring only consists of two
   * coordinates. For details on the bug inside {@link LineString} that is addressed here, see
   * https://github.com/locationtech/jts/issues/531
   *
   * @param lineString the linestring that should be checked and maybe converted to a 'safe to be
   *     compared' linestring
   * @return a 'safe to be compared' linestring
   */
  public static LineString buildSafeLineString(LineString lineString) {
    if (lineString.getCoordinates().length == 2) {
      return buildSafeLineStringBetweenPoints(lineString.getStartPoint(), lineString.getEndPoint());
    } else {
      // rebuild line with unique points
      /* Please note, that using a simple HashSet here to obtain uniqueness is harmful, as it not necessarily maintains
       * the order of coordinates (but most likely will). Additionally, the behaviour of a HashSet might change with the
       * JVM (version) you use to execute the code. */
      Coordinate[] uniqueCoords =
          Arrays.stream(lineString.getCoordinates()).distinct().toArray(Coordinate[]::new);
      return uniqueCoords.length == 1
          ? buildSafeLineStringBetweenPoints(lineString.getStartPoint(), lineString.getEndPoint())
          : DEFAULT_GEOMETRY_FACTORY.createLineString(uniqueCoords);
    }
  }

  /**
   * Build an instance of {@link LineString} between two points that is safe to be compared even if
   * the provided two points consist of exactly the same coordinates. This is done by increasing the
   * coordinate of the provided Point {@code p1} by a small amount to make it different from Point
   * {@code p2}. For details on the bug inside {@link LineString} that is addressed here, see
   * https://github.com/locationtech/jts/issues/531
   *
   * @param p1 start point of the linestring
   * @param p2 end point of the linestring
   * @return a {@link LineString} between the provided points
   */
  public static LineString buildSafeLineStringBetweenPoints(final Point p1, final Point p2) {
    return buildSafeLineStringBetweenCoords(p1.getCoordinate(), p2.getCoordinate());
  }

  /**
   * Build an instance of {@link LineString} between two coordinates that is safe to be compared
   * even if the provided two coordinates are exactly the same coordinates. This is done by
   * increasing the coordinate of the provided Point {@code c1} by a small amount to make it
   * different from Point {@code c2}. For details on the bug inside {@link LineString} that is
   * addressed here, see https://github.com/locationtech/jts/issues/531
   *
   * @param c1 start coordinate of the linestring
   * @param c2 end coordinate of the linestring
   * @return A safely build line string
   */
  public static LineString buildSafeLineStringBetweenCoords(
      final Coordinate c1, final Coordinate c2) {
    final Coordinate safeCoord1 = c1.equals(c2) ? buildSafeCoord(c1) : c1;
    return DEFAULT_GEOMETRY_FACTORY.createLineString(new Coordinate[] {safeCoord1, c2});
  }

  /**
   * Adapted {@link Coordinate#x}, {@link Coordinate#y} and {@link Coordinate#z} of the provided
   * {@link Coordinate} by 1e-13 and return a new, adapted instance of {@link Coordinate}
   *
   * @param coord the coordinate that should be adapted
   * @return the adapted coordinate with slightly changed x,y,z values
   */
  private static Coordinate buildSafeCoord(Coordinate coord) {

    double modVal = 1e-13;
    double p1X = coord.getX() + modVal;
    double p1Y = coord.getY() + modVal;
    double p1Z = coord.getZ() + modVal;

    return new Coordinate(p1X, p1Y, p1Z);
  }

  /**
   * Calculates and sorts the distances between a base coordinate and other given coordinates using
   * {@link #calcHaversine(double, double, double, double)}
   *
   * @param baseCoordinate the base point
   * @param coordinates the points to calculate the distance from the base point for
   * @return a sorted set of distances between the base and other coordinates
   * @deprecated Use {@link #calcOrderedCoordinateDistances(Point, Collection)} instead.
   */
  @Deprecated(since = "2.0", forRemoval = true)
  public static SortedSet<CoordinateDistance> getCoordinateDistances(
      Point baseCoordinate, Collection<Point> coordinates) {
    return calcOrderedCoordinateDistances(baseCoordinate, coordinates);
  }

  /**
   * Calculates and sorts the distances between a base coordinate and other given coordinates using
   * {@link #calcHaversine(double, double, double, double)}
   *
   * @param baseCoordinate the base point
   * @param coordinates the points to calculate the distance from the base point for
   * @return a sorted set of distances between the base and other coordinates
   */
  public static SortedSet<CoordinateDistance> calcOrderedCoordinateDistances(
      Point baseCoordinate, Collection<Point> coordinates) {
    return coordinates.stream()
        .map(coordinate -> new CoordinateDistance(baseCoordinate, coordinate))
        .collect(Collectors.toCollection(TreeSet::new));
  }

  /**
   * Calculates between two coordinates on earth's surface (great circle distance).
   *
   * @param latA latitude of coordinate a
   * @param lngA longitude of coordinate a
   * @param latB latitude of coordinate b
   * @param lngB longitude of coordinate b
   * @return The distance between both coordinates as a {@link Length}
   */
  public static ComparableQuantity<Length> calcHaversine(
      double latA, double lngA, double latB, double lngB) {

    ComparableQuantity<Angle> dLat = Quantities.getQuantity(toRadians(latB - latA), RADIAN);
    ComparableQuantity<Angle> dLon = Quantities.getQuantity(toRadians(lngB - lngA), RADIAN);
    double a =
        sin(dLat.getValue().doubleValue() / 2) * sin(dLat.getValue().doubleValue() / 2)
            + cos(toRadians(latA))
                * cos(toRadians(latB))
                * sin(dLon.getValue().doubleValue() / 2)
                * sin(dLon.getValue().doubleValue() / 2);
    double c = 2 * atan2(sqrt(a), sqrt(1 - a));
    return EARTH_RADIUS.multiply(c);
  }

  /**
   * Calculates the distance between two coordinates on earth's surface (great circle distance).
   *
   * @param coordinateA coordinate a
   * @param coordinateB coordinate b
   * @return the distance between the coordinates as a quantity
   */
  public static ComparableQuantity<Length> calcHaversine(
      Coordinate coordinateA, Coordinate coordinateB) {
    return calcHaversine(
        coordinateA.getY(), coordinateA.getX(), coordinateB.getY(), coordinateB.getX());
  }

  /**
   * Calculates the total length of a LineString through building the sum of the distances between
   * all points of LineString using {@link #calcHaversine(double, double, double, double)}
   *
   * @param lineString the line string which length shall be calculated
   * @return The total length of the line string
   * @deprecated Use {@link #calcHaversine(LineString)} instead.
   */
  @Deprecated(since = "2.0", forRemoval = true)
  public static ComparableQuantity<Length> totalLengthOfLineString(LineString lineString) {
    return calcHaversine(lineString);
  }

  /**
   * Calculates length of a {@link LineString} on earth's surface.
   *
   * @param lineString the linestring to calculate the length of
   * @return the length of the linestring as a quantity
   */
  public static ComparableQuantity<Length> calcHaversine(LineString lineString) {
    ComparableQuantity<Length> y = Quantities.getQuantity(0, KILOMETRE);
    for (int i = 0; i < lineString.getNumPoints() - 1; i++) {
      y = y.add(calcHaversine(lineString.getCoordinateN(i), lineString.getCoordinateN(i + 1)));
    }
    return y;
  }

  /**
   * Builds a convex hull from a set of latitude/longitude coordinates.
   *
   * @param coordinates the coordinates to consider
   * @return a Try of the resulting polygon
   */
  public static Polygon buildConvexHull(Set<Coordinate> coordinates) throws GeoException {

    Geometry geom =
        new ConvexHull(coordinates.toArray(new Coordinate[0]), DEFAULT_GEOMETRY_FACTORY)
            .getConvexHull();

    if (geom instanceof LineString)
      throw new GeoException(
          "Got a line string as a convex hull. Probable cause: $coordinates only contains two different coordinates.");
    else if (geom instanceof Point)
      throw new GeoException(
          "Got a point as a convex hull. Probably coordinates: $coordinates only contains one coordinate.");
    else if (geom instanceof GeometryCollection)
      throw new GeoException("Got a GeometryCollection. Probably $coordinates was empty.");
    else if (geom instanceof Polygon polygon) return polygon;
    else
      throw new GeoException("Got an unexpected return type: " + geom.getClass().getSimpleName());
  }

  public static Point buildPoint(double lat, double lng) {
    return DEFAULT_GEOMETRY_FACTORY.createPoint(buildCoordinate(lat, lng));
  }

  public static Point buildPoint(Coordinate coordinate) {
    return DEFAULT_GEOMETRY_FACTORY.createPoint(coordinate);
  }

  /**
   * Build a coordinate from latitude and longitude values.
   *
   * @param lat latitude value
   * @param lng longitude value
   * @return the built [[Coordinate]]
   */
  public static Coordinate buildCoordinate(double lat, double lng) {
    return new Coordinate(lng, lat);
  }

  /**
   * Builds a polygon from a List of coordinates. To build a Polygon the coordinates have to form a
   * closed ring which means that the first and last coordinate have to be the same coordinate.
   *
   * @param coordinates the coordinates for building the polygon
   * @return a [[Polygon]]
   */
  public static Polygon buildPolygon(Coordinate[] coordinates) {
    CoordinateArraySequence arrayCoordinates = new CoordinateArraySequence(coordinates);
    LinearRing linearRing = new LinearRing(arrayCoordinates, DEFAULT_GEOMETRY_FACTORY);
    return new Polygon(linearRing, new LinearRing[0], DEFAULT_GEOMETRY_FACTORY);
  }

  /**
   * Sinusoidal equal-area projection of latitude and longitude values to a 2d surface. This is an
   * approximation gets worse when lines become very long.
   *
   * <p>Credits to Joe Kington
   * (https://stackoverflow.com/questions/4681737/how-to-calculate-the-area-of-a-polygon-on-the-earths-surface-using-python#:~:text=Basically%2C%20you%20just%20multiply%20the,the%20cosine%20of%20the%20latitude.)
   *
   * @param coordinate the coordinate to project
   * @return a projected Coordinate with values in metre
   */
  public static Coordinate equalAreaProjection(Coordinate coordinate) {
    double lat = coordinate.getY();
    double lng = coordinate.getX();
    double latDist = PI * (EARTH_RADIUS.to(METRE).getValue().doubleValue() / 180d);
    double y = lat * latDist;
    double x = lng * latDist * cos(toRadians(lat));
    return new Coordinate(x, y);
  }

  /**
   * Reverses the {@link #equalAreaProjection(Coordinate)} and returns a coordinate on earths
   * surface
   *
   * @param coordinate the projected coordinate
   * @return the latitude longitude based coordinate
   */
  public static Coordinate reverseEqualAreaProjection(Coordinate coordinate) {
    double latDist = PI * (EARTH_RADIUS.to(METRE).getValue().doubleValue() / 180d);
    double lat = coordinate.y / latDist;
    double lng = coordinate.x / (latDist * cos(toRadians(lat)));
    return buildCoordinate(lat, lng);
  }

  /**
   * Draws a circle with a radius of the provided distance around the provided center coordinates
   * and returns the result as a drawable polygon (one point per degree)
   *
   * <p>Source: https://www.movable-type.co.uk/scripts/latlong.html "Destination point given
   * distance and bearing from start point"
   *
   * @param center coordinate of the circle's center
   * @param radius radius of the circle
   * @return a polygon without the center, but with all points of the circle
   */
  public static Polygon buildCirclePolygon(Coordinate center, Quantity<Length> radius) {
    double centerLat = toRadians(center.y);
    double centerLon = toRadians(center.x);
    double d = radius.divide(EARTH_RADIUS).getValue().doubleValue();

    Coordinate[] coordinates =
        IntStream.rangeClosed(0, 360)
            .mapToObj(
                angle -> {
                  double bearing = toRadians(angle);
                  double latRad =
                      asin(sin(centerLat) * cos(d) + cos(centerLat) * sin(d) * cos(bearing));
                  double lonRad =
                      centerLon
                          + atan2(
                              sin(bearing) * sin(d) * cos(centerLat),
                              cos(d) - sin(centerLat) * sin(latRad));
                  return new Coordinate(toDegrees(lonRad), toDegrees(latRad));
                })
            .toArray(Coordinate[]::new);

    return buildPolygon(coordinates);
  }

  public static Coordinate orthogonalProjection(
      Coordinate linePtA, Coordinate linePtB, Coordinate pt) {
    return orthogonalProjection(
            Vector2D.create(linePtA), Vector2D.create(linePtB), Vector2D.create(pt))
        .toCoordinate();
  }

  /**
   * Calculate the orthogonal projection of a point onto a line. Credits to Andrey Tyukin. Check out
   * how and why this works here: <a
   * href="https://stackoverflow.com/questions/54009832/scala-orthogonal-projection-of-a-point-onto-a-line">Orthogonal
   * projection of a point onto a line</a>
   *
   * @param linePtA first point of the line
   * @param linePtB second point of the line
   * @param pt the point for which to calculate the projection
   * @return the projected point
   */
  public static Vector2D orthogonalProjection(Vector2D linePtA, Vector2D linePtB, Vector2D pt) {
    Vector2D v = pt.subtract(linePtA);
    Vector2D d = linePtB.subtract(linePtA);
    return linePtA.add(d.multiply(v.dot(d) / d.lengthSquared()));
  }
}
