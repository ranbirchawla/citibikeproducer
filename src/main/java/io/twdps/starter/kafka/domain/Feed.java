package io.twdps.starter.kafka.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feed {

  @JsonProperty("last_updated")
  private long lastUpdated;

  @JsonProperty("ttl")
  private int ttl;

  @JsonProperty("data")
  private Data data;

  public long getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(long lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public int getTtl() {
    return ttl;
  }

  public void setTtl(int ttl) {
    this.ttl = ttl;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }
}
