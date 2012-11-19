package com.chrisgammage.gwtjitsu.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 11/1/12
 * Time: 9:20 AM
 */
public class SubModelPropertyChangeEvent extends GwtEvent<SubModelPropertyChangeHandler> {
  public static Type<SubModelPropertyChangeHandler> TYPE = new Type<SubModelPropertyChangeHandler>();

  private final String propertyName;
  private final String modelName;

  public SubModelPropertyChangeEvent(String modelName, String propertyName) {
    this.propertyName = propertyName;
    this.modelName = modelName;
  }

  public Type<SubModelPropertyChangeHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(SubModelPropertyChangeHandler handler) {
    handler.onPropertyPropertyChange(modelName, propertyName);
  }
}
