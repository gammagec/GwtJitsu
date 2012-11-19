package com.chrisgammage.gwtjitsu.shared;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/15/12
 * Time: 4:59 PM
 */
public @interface ObservableEvent {
  Class<? extends GwtEvent<?>> value();
}
