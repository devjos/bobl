package de.tum.model;

public class Demand {

  private final String title;

  private final String source;
  private final String sourceLatitude;
  private final String sourceLongitude;

  private final String destination;
  private final String destinationLatitude;
  private final String destinationLongitude;

  private final String outboundTime;
  private final String waybackTime; // optional

  private final boolean[] weekdays;

  public Demand(String title, String source, String sourceLatitude, String sourceLongitude,
      String destination, String destinationLatitude, String destinationLongitude,
      String outboundTime, boolean[] weekdays) {
    this(title, source, sourceLatitude, sourceLongitude, destination, destinationLatitude,
        destinationLongitude, outboundTime, null, weekdays);
  }

  public Demand(String title, String source, String sourceLatitude, String sourceLongitude,
      String destination, String destinationLatitude, String destinationLongitude,
      String outboundTime, String waybackTime, boolean[] weekdays) {
    this.title = title;

    this.source = source;
    this.sourceLatitude = sourceLatitude;
    this.sourceLongitude = sourceLongitude;

    this.destination = destination;
    this.destinationLatitude = destinationLatitude;
    this.destinationLongitude = destinationLongitude;

    this.outboundTime = outboundTime;
    this.waybackTime = waybackTime;

    this.weekdays = weekdays;
  }

  public String getTitle() {
    return title;
  }

  public String getSource() {
    return source;
  }

  public String getSourceLatitude() {
    return sourceLatitude;
  }

  public String getSourceLongitude() {
    return sourceLongitude;
  }

  public String getDestination() {
    return destination;
  }

  public String getDestinationLatitude() {
    return destinationLatitude;
  }

  public String getDestinationLongitude() {
    return destinationLongitude;
  }

  public String getOutboundTime() {
    return outboundTime;
  }

  public String getWaybackTime() {
    return waybackTime;
  }

  public boolean[] getWeekdays() {
    return weekdays.clone();
  }

  public void verify() throws IllegalStateException {
    isNotNullAndNotEmpty(title);

    isNotNullAndNotEmpty(source);
    isNotNullAndNotEmpty(sourceLatitude);
    isLatitude(sourceLatitude);
    isNotNullAndNotEmpty(sourceLongitude);
    isLongitude(sourceLongitude);

    isNotNullAndNotEmpty(destination);
    isNotNullAndNotEmpty(destinationLatitude);
    isLatitude(destinationLatitude);
    isNotNullAndNotEmpty(destinationLongitude);
    isLongitude(destinationLongitude);

    isNotNullAndNotEmpty(outboundTime);
    isTime(outboundTime);

    if (waybackTime != null) {
      isTime(waybackTime);
    }

    if (weekdays == null || weekdays.length != 7) {
      throw new IllegalStateException("Weekdays must not be null and must contain 7 elements.");
    }


  }

  private void isTime(String s) {
    if (!s.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
      throw new IllegalStateException("Invalid time parameter.");
    }
  }

  private void isNotNullAndNotEmpty(String s) {
    if (s == null || s.isEmpty()) {
      throw new IllegalStateException("Required parameter is missing or empty.");
    }
  }

  private void isLatitude(String s) {
    if (!s
        .matches("^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$")) {
      throw new IllegalStateException("Coordinate is no latitude.");
    }
  }

  private void isLongitude(String s) {
    if (!s.matches(
        "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$")) {
      throw new IllegalStateException("Coordinate is no longitude.");
    }
  }


}
