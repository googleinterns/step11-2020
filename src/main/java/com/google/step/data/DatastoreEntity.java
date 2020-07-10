package com.google.step.data;

import com.google.appengine.api.datastore.Entity;

public interface DatastoreEntity {
  Entity convertToEntity();
}
