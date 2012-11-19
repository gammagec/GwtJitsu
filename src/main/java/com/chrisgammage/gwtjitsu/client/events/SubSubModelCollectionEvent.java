package com.chrisgammage.gwtjitsu.client.events;

import com.chrisgammage.gwtjitsu.client.Model;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 11/1/12
 * Time: 3:43 PM
 */
public class SubSubModelCollectionEvent<T> extends GwtEvent<SubSubModelCollectionHandler> {
  public static Type<SubSubModelCollectionHandler> TYPE = new Type<SubSubModelCollectionHandler>();

  public SubSubModelCollectionEvent(Model model1, Model model2,
                                    String collectionProperty, T object, boolean removed) {
    this.model1 = model1;
    this.model2 = model2;
    this.collectionProperty = collectionProperty;
    this.object = object;
    this.removed = removed;
  }

  public Type<SubSubModelCollectionHandler> getAssociatedType() {
    return TYPE;
  }

  private final Model model1;
  private final Model model2;
  private final String collectionProperty;
  private final T object;
  private final boolean removed;

  protected void dispatch(SubSubModelCollectionHandler handler) {
    handler.onSubSubModelCollection(model1, model2, collectionProperty, object, removed);
  }
}
