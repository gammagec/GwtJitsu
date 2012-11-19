package com.chrisgammage.gwtjitsu.client.events;

import com.chrisgammage.gwtjitsu.client.Model;
import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 11/1/12
 * Time: 12:18 PM
 */
public interface SubModelCollectionHandler <T> extends EventHandler {
  void onSubModelCollection(Model model, String collectionProperty,
                            T Object, boolean removed);
}
