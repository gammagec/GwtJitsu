package com.chrisgammage.gwtjitsu.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 11/1/12
 * Time: 9:20 AM
 */
public interface SubModelPropertyChangeHandler extends EventHandler {
  void onPropertyPropertyChange(String modelName, String propertyName);
}
