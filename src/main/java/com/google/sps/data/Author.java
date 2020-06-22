package com.google.sps.data;

public class Author {

  private String name;
  private String college;
  private String description;
  private String imageURL;

  public Author(String name, String college, String description, String imageURL) {
    this.name = name;
    this.college = college;
    this.description = description;
    this.imageURL = imageURL;
  }

  public String getName() {
    return name;
  }

  public String getCollege() {
    return college;
  }

  public String getDescription() {
    return description;
  }

  public String getImageURL() {
    return imageURL;
  }
}
