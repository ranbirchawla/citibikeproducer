package io.twdps.starter.kafka.domain;

import java.util.List;


public class Data {

  private List<StationStatus> stations;

  public List<StationStatus> getStations() {
    return stations;
  }

  public void setStations(List<StationStatus> stations) {
    this.stations = stations;
  }
}
