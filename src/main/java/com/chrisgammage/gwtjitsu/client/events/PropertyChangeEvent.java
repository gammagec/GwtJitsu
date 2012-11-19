package com.chrisgammage.gwtjitsu.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/3/12
 * Time: 3:14 PM
 */
public class PropertyChangeEvent extends GwtEvent<PropertyChangeHandler> {
  public static Type<PropertyChangeHandler> TYPE = new Type<PropertyChangeHandler>();

  public PropertyChangeEvent(String propertyName, Object previousValue) {
    this.propertyName = propertyName;
    this.previousValue = previousValue;
  }

  public Type<PropertyChangeHandler> getAssociatedType() {
    return TYPE;
  }

  private final String propertyName;
  private final Object previousValue;

  protected void dispatch(PropertyChangeHandler handler) {
    handler.onPropertyChange(this);
  }

  public String getPropertyName() {
    return propertyName;
  }

  public Object getPreviousValue() {
    return previousValue;
  }
}
