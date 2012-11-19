package com.chrisgammage.gwtjitsu.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/15/12
 * Time: 9:26 PM
 */
public class CollectionAddEvent extends GwtEvent<CollectionAddHandler> {

  public static Type<CollectionAddHandler> TYPE = new Type<CollectionAddHandler>();

  public CollectionAddEvent(Object object) {
    this.object = object;
  }

  public Type<CollectionAddHandler> getAssociatedType() {
    return TYPE;
  }

  private final Object object;

  protected void dispatch(CollectionAddHandler handler) {
    handler.onCollectionAdd(object);
  }
}
