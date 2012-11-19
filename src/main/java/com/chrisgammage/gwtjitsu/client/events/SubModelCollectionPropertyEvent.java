package com.chrisgammage.gwtjitsu.client.events;

import com.chrisgammage.gwtjitsu.client.Model;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 11/1/12
 * Time: 5:33 PM
 */
public class SubModelCollectionPropertyEvent extends GwtEvent<SubModelCollectionPropertyHandler> {
  public static Type<SubModelCollectionPropertyHandler> TYPE = new Type<SubModelCollectionPropertyHandler>();

  public SubModelCollectionPropertyEvent(Model subModel, String collectionName) {
    this.subModel = subModel;
    this.collectionName = collectionName;
  }

  public Type<SubModelCollectionPropertyHandler> getAssociatedType() {
    return TYPE;
  }

  private final Model subModel;
  private final String collectionName;

  protected void dispatch(SubModelCollectionPropertyHandler handler) {
    handler.onSubModelCollectionProperty(subModel, collectionName);
  }
}
