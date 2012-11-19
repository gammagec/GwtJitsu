package com.chrisgammage.gwtjitsu.client.events;

import com.chrisgammage.gwtjitsu.client.impl.ModelBase;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/15/12
 * Time: 10:15 PM
 */
public class MapEvent extends GwtEvent<MapHandler> {
  public static Type<MapHandler> TYPE = new Type<MapHandler>();

  public MapEvent(ModelBase model, String property, Object key) {
    this.model = model;
    this.property = property;
    this.key = key;
  }

  public Type<MapHandler> getAssociatedType() {
    return TYPE;
  }

  private final ModelBase model;
  private final String property;
  private final Object key;

  protected void dispatch(MapHandler handler) {
    handler.onMap(model, property, key);
  }
}
