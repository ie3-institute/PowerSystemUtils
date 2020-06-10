/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util;

import static edu.ie3.util.TimeTools.UNDEFINED_TIME;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Utility class that can either be used with default values {@link TimeUtil#withDefaults} or as a
 * self parametrized class.
 */
public class TimeUtil {

  public static final TimeUtil withDefaults =
      new TimeUtil(ZoneId.of("UTC"), Locale.GERMANY, "yyyy-MM-dd HH:mm:ss");

  public static final double UNDEFINED_TIME = Double.NEGATIVE_INFINITY;

  private final ZoneId zoneId;
  private final TimeZone timeZone;
  private final Locale locale;
  private final String dtfPattern;

  private final DateTimeFormatter dateTimeFormatter;

  public TimeUtil(ZoneId zoneId, Locale locale, String dtfPattern) {
    this.zoneId = zoneId;
    this.timeZone = TimeZone.getTimeZone(zoneId);
    this.locale = locale;
    this.dtfPattern = dtfPattern;
    this.dateTimeFormatter =
        DateTimeFormatter.ofPattern(dtfPattern).withZone(zoneId).withLocale(locale);
  }

  /**
   * Converts a given instant to a time string
   *
   * @param instant {@link Instant} to be converted to {@link String}
   * @return A String of the given Instant
   */
  public String toString(Instant instant) {
    return dateTimeFormatter.format(instant);
  }

  /**
   * Converts a given ZoneDateTime to a time string
   *
   * @param zonedDateTime {@link Instant} to be converted to {@link String}
   * @return A String of the given Instant
   */
  public String toString(ZonedDateTime zonedDateTime) {
    return dateTimeFormatter.format(zonedDateTime);
  }

  /**
   * Returns an ZoneDateTime based on a given String, which can be understood by the class private
   * DateTimeFormatter
   *
   * @param timeString {@link String} to convert to {@link Instant}
   * @return Instant object
   */
  public ZonedDateTime toZonedDateTime(String timeString) {
    LocalDateTime localDateTime = LocalDateTime.parse(timeString, dateTimeFormatter);
    return ZonedDateTime.of(localDateTime, zoneId);
  }

  public ZoneId getZoneId() {
    return zoneId;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getDtfPattern() {
    return dtfPattern;
  }

  public DateTimeFormatter getDateTimeFormatter() {
    return dateTimeFormatter;
  }

  /**
   * Determines the number of quarter hour of the day (starting with 0). The definition of quarter
   * hour relates to something comparable than a right open interval. Example: 00:00:00 = 0 ...
   * 00:14:59 = 0 00:15:00 = 1
   *
   * @param time Time, from which the number of quarter hour may be determined
   * @return Number of the quarter hour of the day
   */
  public int getQuarterHourOfDay(ZonedDateTime time) {
    return time.get(HOUR_OF_DAY) * 4 + time.get(MINUTE_OF_HOUR) / 15;
  }

  /**
   * Calculate the difference between two given [[ZonedDateTime]]s and return the difference in
   * seconds or {@link this#UNDEFINED_TIME} if the provided string is empty or equals to `undefined`
   *
   * @param startDateTime the start date time
   * @param endDateTime the end date time
   * @return the difference between the provided start and end date time in seconds
   */
  public double zonedDateTimeDifferenceInSeconds(String startDateTime, String endDateTime) {
    if (startDateTime.length() == 0 || "undefined".equals(startDateTime)) return UNDEFINED_TIME;
    if (endDateTime.length() == 0 || "undefined".equals(endDateTime)) return UNDEFINED_TIME;
    return zonedDateTimeDifference(
        TimeUtil.withDefaults.toZonedDateTime(startDateTime),
        TimeUtil.withDefaults.toZonedDateTime(endDateTime),
        ChronoUnit.SECONDS);
  }

  /**
   * Calculate the difference between two [[ZonedDateTime]]s and return the difference in the given
   * [[ChronoUnit]]
   *
   * @param startDateTime the start date time
   * @param endDateTime the end date time
   * @param unit the chrono unit that should be used returned
   * @return the difference between the provided start and end date time in the provided chrono unit
   */
  public double zonedDateTimeDifference(
      ZonedDateTime startDateTime, ZonedDateTime endDateTime, ChronoUnit unit) {
    return unit.between(startDateTime, endDateTime);
  }
}
