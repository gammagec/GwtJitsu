package com.chrisgammage.gwtjitsu.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/15/12
 * Time: 9:26 PM
 */
public interface CollectionRemoveHandler<T> extends EventHandler {
  void onCollectionRemove(T object);
}
