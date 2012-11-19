package com.chrisgammage.gwtjitsu.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/15/12
 * Time: 10:15 PM
 */
public interface MapHandler<M, T> extends EventHandler {
  void onMap(M model, String property, T key);
}
