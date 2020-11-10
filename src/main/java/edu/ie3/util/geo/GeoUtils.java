/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo;

import static edu.ie3.util.quantities.PowerSystemUnits.*;

import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import net.morbz.osmonaut.osm.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

/** Functionality to deal with geographical and geometric information */
public class GeoUtils {
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
   * @return The distance between both coordinates in {@link
   *     edu.ie3.util.quantities.PowerSystemUnits#KILOMETRE}
   */
  public static ComparableQuantity<Length> calcHaversine(
      double lat1, double lng1, double lat2, double lng2) {

    // average radius of the earth in km
    ComparableQuantity<Length> r = EARTH_RADIUS.to(KILOMETRE);
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
}
