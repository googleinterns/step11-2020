package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/**
 * This interface forces implementing classes to support conversion to and from a datastore entity
 * object.
 *
 * @author guptamudit
 * @version 1.0
 */
public interface DatastoreEntity {
  Entity convertToEntity();
}
