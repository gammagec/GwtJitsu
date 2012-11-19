package com.chrisgammage.gwtjitsu.client.events;

import com.chrisgammage.gwtjitsu.client.Model;
import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 11/1/12
 * Time: 3:43 PM
 */
public interface SubSubModelCollectionHandler<T> extends EventHandler {
  void onSubSubModelCollection(Model model1, Model model2,
                               String collectionProperty, T object, boolean removed);
}
