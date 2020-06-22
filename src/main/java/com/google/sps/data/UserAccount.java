package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public class UserAccount {

  private String nickname;
  private String userID;

  public UserAccount(String nickname, String userID) {
    this.nickname = nickname;
    this.userID = userID;
  }

  public UserAccount(Entity entity) {
    this.nickname = (String) entity.getProperty("nickname");
    this.userID = (String) entity.getProperty("userID");
  }

  public String getNickname() {
    return nickname;
  }

  public String getUserID() {
    return userID;
  }
}
