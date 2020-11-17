package io.ssbswang.nflrushingserver.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import io.ssbswang.nflrushingserver.utils.Utils;

/**
 * DTO and deserialization class for rushing.json.
 * All data types are casted to String to cover all any mis-entry and reduce parsing issues.
 * The getter methods are used for sanitizing and comparing data values.
 */
public class DataDTO {

  public String player;
  public String team;
  public String pos;
  public String att;
  public String attG;
  public String yds;
  public String avg;
  public String ydsG;
  public String td;
  public String lng;
  public String first;
  public String firstPercent;
  public String twenty;
  public String fourty;
  public String fum;

  @JsonSetter("Player")
  public void setPlayer(String player) {
    this.player = player;
  }

  @JsonSetter("Team")
  public void setTeam(String team) {
    this.team = team;
  }

  @JsonSetter("Pos")
  public void setPos(String pos) {
    this.pos = pos;
  }

  @JsonSetter("Att")
  public void setAtt(String att) {
    this.att = att;
  }

  @JsonSetter("Att/G")
  public void setAttG(String attG) {
    this.attG = attG;
  }

  @JsonSetter("Yds")
  public void setYds(String yds) {
    this.yds = yds;
  }

  @JsonSetter("Avg")
  public void setAvg(String avg) {
    this.avg = avg;
  }

  @JsonSetter("Yds/G")
  public void setYdsG(String ydsG) {
    this.ydsG = ydsG;
  }

  @JsonSetter("TD")
  public void setTd(String td) {
    this.td = td;
  }

  @JsonSetter("Lng")
  public void setLng(String lng) {
    this.lng = lng;
  }

  @JsonSetter("1st")
  public void setFirst(String first) {
    this.first = first;
  }

  @JsonSetter("1st%")
  public void setFirstPercent(String firstPercent) {
    this.firstPercent = firstPercent;
  }

  @JsonSetter("20+")
  public void setTwenty(String twenty) {
    this.twenty = twenty;
  }

  @JsonSetter("40+")
  public void setFourty(String fourty) {
    this.fourty = fourty;
  }

  @JsonSetter("FUM")
  public void setFum(String fum) {
    this.fum = fum;
  }

  public float compareYds() {
    return Utils.strToFloat(this.yds);
  }

  public float compareLng() {
    return Utils.strToFloat(this.lng);
  }

  public float compareTd() {
    return Utils.strToFloat(this.td);
  }

}
