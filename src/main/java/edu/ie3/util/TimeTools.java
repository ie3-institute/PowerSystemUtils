/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @deprecated As of release 1.4, replaced by {@link TimeUtil} */
@Deprecated
public class TimeTools {
  private static final Logger logger = LoggerFactory.getLogger(TimeTools.class);

  public static TimeZone DEFAULT_TIME_ZONE;
  public static ZoneId DEFAULT_ZONE_ID;
  public static Locale DEFAULT_LOCALE;
  public static final ZonedDateTime EPOCH =
      ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));

  private static DateTimeFormatter DATE_TIME_FORMATTER;
  private static SimpleDateFormat SIMPLE_DATE_FORMAT;
  public static final double UNDEFINED_TIME = Double.NEGATIVE_INFINITY;

  static {
    TimeTools.initialize(ZoneId.of("UTC"), Locale.GERMANY, "dd/MM/yyyy HH:mm:ss");
  }

  /** Standard method to initialize global values for later proper use of time classes. */
  public static void initialize() {
    TimeTools.initialize(ZoneId.of("UTC"), Locale.GERMANY, "dd/MM/yyyy HH:mm:ss");
  }

  /**
   * Initializes global values for later proper use of time classes.
   *
   * @param defaultZoneId {@link ZoneId} to be used by default
   * @param dtfPattern Pattern to be interpreted by the {@link DateTimeFormatter}
   * @param locale Default {@link Locale}
   */

  public static void initialize(ZoneId defaultZoneId, Locale locale, String dtfPattern) {
    DEFAULT_LOCALE = locale;
    DEFAULT_TIME_ZONE = TimeZone.getTimeZone(defaultZoneId);
    if (!DEFAULT_TIME_ZONE.toZoneId().equals(defaultZoneId))
      logger.warn(
          "The desired time zone \"{}\" cannot be set. Use fall back time zone \"{}\"",
          defaultZoneId.getDisplayName(TextStyle.SHORT, DEFAULT_LOCALE),
          DEFAULT_TIME_ZONE.getID());
    DEFAULT_ZONE_ID = DEFAULT_TIME_ZONE.toZoneId();

    logger.info("Set JVM's default time zone to {}", DEFAULT_TIME_ZONE.getDisplayName());
    TimeZone.setDefault(DEFAULT_TIME_ZONE);

    DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern(dtfPattern)
            .withZone(DEFAULT_ZONE_ID)
            .withLocale(DEFAULT_LOCALE);
    SIMPLE_DATE_FORMAT = new SimpleDateFormat(dtfPattern);
  }

  /**
   * Calculate differences between two {@link ZonedDateTime}s and returns the duration in Seconds.
   * The input {@link String}s have to be formatted as defined during initialization
   *
   * @param startDateTime startDateTime as {@link String}
   * @param endDateTime endDateTime as {@link String}
   * @return
   */
  public static double zonedDateTimeDifferenceInSeconds(
      final String startDateTime, final String endDateTime) {
    if (startDateTime == null || startDateTime.length() == 0 || startDateTime.equals("undefined")) {
      return UNDEFINED_TIME;
    }
    if (endDateTime == null || endDateTime.length() == 0 || endDateTime.equals("undefined")) {
      return UNDEFINED_TIME;
    }
    return zonedDateTimeDifference(
        TimeTools.toZonedDateTime(startDateTime),
        TimeTools.toZonedDateTime(endDateTime),
        ChronoUnit.SECONDS);
  }

  /**
   * Calculate differences between two {@link ZonedDateTime}s and returns the duration in Seconds
   *
   * @param startDateTime
   * @param endDateTime
   * @param unit
   * @return
   */
  public static double zonedDateTimeDifference(
      final ZonedDateTime startDateTime, final ZonedDateTime endDateTime, ChronoUnit unit) {
    return unit.between(startDateTime, endDateTime);
  }

  /**
   * Converts a given instant to a time string
   *
   * @param instant {@link Instant} to be converted to {@link String}
   * @return A String of the given Instant
   */
  public static String toString(Instant instant) {
    return DATE_TIME_FORMATTER.format(instant);
  }

  /**
   * Converts a given ZoneDateTime to a time string
   *
   * @param zonedDateTime {@link Instant} to be converted to {@link String}
   * @return A String of the given Instant
   */
  public static String toString(ZonedDateTime zonedDateTime) {
    return DATE_TIME_FORMATTER.format(zonedDateTime);
  }

  /**
   * Returns the ISO-specified date string of an instant
   *
   * @param time {@link Instant} to be converted to {@link String}
   * @return ISO-specified date string
   */
  public static String toIsoString(TemporalAccessor time) {
    return DateTimeFormatter.ISO_INSTANT.format(time);
  }

  /**
   * Returns an instant based on a given String, which can be understood by the class private
   * DateTimeFormatter
   *
   * @param timeString {@link String} to convert to {@link Instant}
   * @return Instant object
   */
  public static Instant toInstant(String timeString) {
    LocalDateTime localDateTime = LocalDateTime.parse(timeString, DATE_TIME_FORMATTER);
    Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
    return instant;
  }

  /**
   * Returns an ZoneDateTime based on a given String, which can be understood by the class private
   * DateTimeFormatter
   *
   * @param timeString {@link String} to convert to {@link Instant}
   * @return Instant object
   */
  public static ZonedDateTime toZonedDateTime(String timeString) {
    LocalDateTime localDateTime = LocalDateTime.parse(timeString, DATE_TIME_FORMATTER);
    return ZonedDateTime.of(localDateTime, DEFAULT_ZONE_ID);
  }

  /**
   * Returns an Calendar object based on a given instant. <br>
   * ONLY USE FOR LEGACY PURPOSES!
   *
   * @param instant {@link Instant} to be converted to {@link Calendar}
   * @return Calendar object
   */
  @Deprecated
  public static Calendar toCalendar(Instant instant) {
    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(instant.getEpochSecond() * 1000);
    return cal;
  }

  /**
   * Returns an Calendar object based on a given ZonedDateTime. <br>
   * ONLY USE FOR LEGACY PURPOSES!
   *
   * @param zonedDateTime {@link ZonedDateTime} to be converted to {@link Calendar}
   * @return Calendar object
   */
  @Deprecated
  public static Calendar toCalendar(ZonedDateTime zonedDateTime) {
    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(
        ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, DEFAULT_ZONE_ID)
            .until(zonedDateTime, ChronoUnit.MILLIS));
    return cal;
  }

  /**
   * Obtain a specified field from the given instant, for example the hour
   *
   * @param instant {@link Instant} to be investigated
   * @param chronoField {@link ChronoField} definition of what to get
   * @return The value as an int
   * @throws Exception is thrown if the the {@link ChronoField} is not supported
   */
  public static int get(Instant instant, ChronoField chronoField) throws Exception {
    ZonedDateTime zdt = instant.atZone(DEFAULT_ZONE_ID);
    if (zdt.isSupported(chronoField)) {
      return zdt.get(chronoField);
    } else
      throw new Exception(
          "The ChronoField \""
              + chronoField.getDisplayName(DEFAULT_LOCALE)
              + "\" is not yet supported.");
  }

  /**
   * Change the time of a provided ZonedDateTime object
   *
   * @param dateTime {@link ZonedDateTime} to be changed
   * @param hours New value of {@link ChronoUnit#HOURS}
   * @param minutes New value of {@link ChronoUnit#MINUTES}
   * @param seconds New value of {@link ChronoUnit#SECONDS}
   * @return a new, adapted ZonedDateTime object
   */
  public static ZonedDateTime changeTime(
      ZonedDateTime dateTime, int hours, int minutes, int seconds) {

    String[] split = dateTime.toString().split("T");
    String[] date = split[0].split("-");

    return TimeTools.toZonedDateTime(
        date[2]
            + "/"
            + date[1]
            + "/"
            + date[0]
            + " "
            + ((hours >= 10) ? hours : "0" + hours)
            + ":"
            + ((minutes >= 10) ? minutes : "0" + minutes)
            + ":"
            + ((seconds >= 10) ? seconds : "0" + seconds));
  }
}
