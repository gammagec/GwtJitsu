package com.chrisgammage.gwtjitsu.client.events;

import com.chrisgammage.gwtjitsu.client.Model;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 11/1/12
 * Time: 12:18 PM
 */
public class SubModelCollectionEvent <T> extends GwtEvent<SubModelCollectionHandler<?>> {
  public static Type<SubModelCollectionHandler<?>> TYPE = new Type<SubModelCollectionHandler<?>>();

  public SubModelCollectionEvent(Model model, String collectionProperty, T object, boolean removed) {
    this.model = model;
    this.collectionProperty = collectionProperty;
    this.object = object;
    this.removed = removed;
  }

  public Type<SubModelCollectionHandler<?>> getAssociatedType() {
    return TYPE;
  }

  private final Model model;
  private final String collectionProperty;
  private final T object;
  private final boolean removed;

  protected void dispatch(SubModelCollectionHandler handler) {
    handler.onSubModelCollection(model, collectionProperty, object, removed);
  }
}
