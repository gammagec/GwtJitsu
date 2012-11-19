package com.chrisgammage.gwtjitsu.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/3/12
 * Time: 3:14 PM
 */
public interface PropertyChangeHandler extends EventHandler {
  void onPropertyChange(PropertyChangeEvent event);
}
