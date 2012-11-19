package com.chrisgammage.gwtjitsu.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 11/1/12
 * Time: 5:33 PM
 */
public interface SubModelCollectionPropertyHandler <T> extends EventHandler {
  void onSubModelCollectionProperty(T subModel, String collectionName);
}
