package com.chrisgammage.gwtjitsu.client;

import com.chrisgammage.gwtjitsu.client.events.*;
import com.chrisgammage.gwtjitsu.client.impl.ModelBase;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 10:55 AM
 */
@ImplementedBy(ModelBase.class)
public interface Model extends HasProperties {
  Logger getLogger();
  <H extends EventHandler> void fireEvent(GwtEvent<H> event);
  <T extends EventHandler>
  HandlerRegistration addHandler(GwtEvent.Type<T> type, T handler);
  void setProperty(String property, Object value, boolean fireEvents);
  Object getProperty(String property);
  void setProperty(String property, Object value);
  HandlerRegistration addCollectionAddHandler(String propertyName,
                                              CollectionAddHandler collectionHandler);
  HandlerRegistration addCollectionAddHandler(String propertyName,
                                              CollectionAddHandler collectionHandler,
                                              boolean initialFire);
  HandlerRegistration addCollectionRemoveHandler(String propertyName,
                                                 CollectionRemoveHandler collectionHandler);
  HandlerRegistration addMapHandler(String propertyName, MapHandler handler);
  HandlerRegistration addPropertyChangeHandler(String propertyName,
                                               PropertyChangeHandler handler);
  HandlerRegistration addPropertyChangeHandler(String propertyName,
                                               PropertyChangeHandler handler,
                                               boolean initialFire);
  HandlerRegistration addCollectionPropertyHandler(String collectionName,
                                                   String propertyName,
                                                   PropertyChangeHandler handler);
  HandlerRegistration addSubModelPropertyChangeHandler(String modelName,
                                                       String propertyName,
                                                       SubModelPropertyChangeHandler handler);
  HandlerRegistration addSubModelCollectionHandler(final String modelName,
                                                   final String collectionName,
                                                   final SubModelCollectionHandler handler);
  HandlerRegistration addSubSubModelCollectionHandler(final String modelName1,
                                                      final String modelName2,
                                                      final String collectionName,
                                                      final SubSubModelCollectionHandler handler);
  HandlerRegistration addSubModelCollectionPropertyHandler(final String subModelName,
                                                           final String collectionName,
                                                           final String propertyName,
                                                           final SubModelCollectionPropertyHandler handler);
  List<String> getPropertyNames();
}
