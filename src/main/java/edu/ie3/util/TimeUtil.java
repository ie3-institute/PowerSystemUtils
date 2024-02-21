/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class that can either be used with default values {@link TimeUtil#withDefaults} or as a
 * self parametrized class.
 */
public class TimeUtil {

  public static final TimeUtil withDefaults = new TimeUtil(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

  public static final String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

  private final DateTimeFormatter dateTimeFormatter;

  public TimeUtil(DateTimeFormatter dtf) {
    this.dateTimeFormatter = dtf;
  }

  /**
   * Converts a given instant to a time string
   *
   * @param instant {@link Instant} to be converted to {@link String}
   * @return A String of the given Instant
   */
  public String toString(Instant instant) {
    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
    return dateTimeFormatter.format(zonedDateTime);
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

  public static String toLocalDateTimeString(ZonedDateTime zonedDateTime) {
    return DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN)
        .format(zonedDateTime.toLocalDateTime());
  }

  /**
   * Returns an ZoneDateTime based on a given String, which can be understood by the class private
   * DateTimeFormatter
   *
   * @param timeString {@link String} to convert to {@link Instant}
   * @return Instant object
   */
  public ZonedDateTime toZonedDateTime(String timeString) {
    return ZonedDateTime.parse(timeString, dateTimeFormatter);
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
   * seconds
   *
   * @param startDateTime the start date time
   * @param endDateTime the end date time
   * @return the difference between the provided start and end date time in seconds
   */
  public long zonedDateTimeDifferenceInSeconds(
      ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
    return zonedDateTimeDifference(startDateTime, endDateTime, ChronoUnit.SECONDS);
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
  public long zonedDateTimeDifference(
      ZonedDateTime startDateTime, ZonedDateTime endDateTime, ChronoUnit unit) {
    return unit.between(startDateTime, endDateTime);
  }

  /**
   * Moves the given zoned date time to the next full unit (e.g. hour)
   *
   * @param zdt Zoned date time to alter
   * @param chronoUnit Next full time unit
   * @return Next full chrono unit, if it does not meet yet
   */
  public static ZonedDateTime toNextFull(ZonedDateTime zdt, ChronoUnit chronoUnit) {
    ZonedDateTime truncatedToFullHour =
        zdt.truncatedTo(chronoUnit); // Truncate all minutes and less
    if (zdt.isAfter(truncatedToFullHour)) return truncatedToFullHour.plus(1L, chronoUnit);
    return zdt;
  }
}
