package io.twdps.starter.kafka.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StationStatus {

  @JsonProperty("station_id")
  private String stationId;

  @JsonProperty("num_bikes_available")
  private int numBikesAvailable;

  @JsonProperty("num_ebikes_available")
  private int numbEbikesAvailable;

  @JsonProperty("last_reported")
  private long lastReported;

  public String getStationId() {
    return stationId;
  }

  public void setStationId(String stationId) {
    this.stationId = stationId;
  }

  public int getNumBikesAvailable() {
    return numBikesAvailable;
  }

  public void setNumBikesAvailable(int numBikesAvailable) {
    this.numBikesAvailable = numBikesAvailable;
  }

  public int getNumbEbikesAvailable() {
    return numbEbikesAvailable;
  }

  public void setNumbEbikesAvailable(int numbEbikesAvailable) {
    this.numbEbikesAvailable = numbEbikesAvailable;
  }

  public long getLastReported() {
    return lastReported;
  }

  public void setLastReported(long lastReported) {
    this.lastReported = lastReported;
  }

  @Override
  public String toString() {
    return "StationStatus{" +
        "stationId='" + stationId + '\'' +
        ", numBikesAvailable=" + numBikesAvailable +
        ", numbEbikesAvailable=" + numbEbikesAvailable +
        ", lastReported=" + lastReported +
        '}';
  }
}
