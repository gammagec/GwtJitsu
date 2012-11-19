package com.chrisgammage.gwtjitsu.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/15/12
 * Time: 9:26 PM
 */
public class CollectionRemoveEvent extends GwtEvent<CollectionRemoveHandler> {

  public static Type<CollectionRemoveHandler> TYPE = new Type<CollectionRemoveHandler>();

  public CollectionRemoveEvent(Object object) {
    this.object = object;
  }

  public Type<CollectionRemoveHandler> getAssociatedType() {
    return TYPE;
  }

  private final Object object;

  protected void dispatch(CollectionRemoveHandler handler) {
    handler.onCollectionRemove(object);
  }
}
