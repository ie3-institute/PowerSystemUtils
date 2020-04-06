/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Utility class that can either be used with default values {@link TimeUtil#withDefaults} or as a
 * self parametrized class.
 */
class TimeUtil {

  public static final TimeUtil withDefaults =
      new TimeUtil(ZoneId.of("UTC"), Locale.GERMANY, "yyyy-MM-dd HH:mm:ss");

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
}
