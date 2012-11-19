package com.chrisgammage.gwtjitsu.client;

import com.chrisgammage.gwtjitsu.client.events.PropertyChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/3/12
 * Time: 3:14 PM
 */
public interface HasPropertyChangeHandlers {
  HandlerRegistration addPropertyChangeHandler(String propertyName,
                                               PropertyChangeHandler propertyChangeHandler);
}
