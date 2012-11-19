package com.chrisgammage.gwtjitsu.client.impl;

import com.chrisgammage.gwtjitsu.client.Model;
import com.chrisgammage.gwtjitsu.client.SmartHandler;
import com.chrisgammage.gwtjitsu.client.events.*;
import com.google.gwt.event.shared.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 11:03 AM
 */
public abstract class ModelBase implements Model {

  protected Map<String, EventBus> handlerManagers = new HashMap<String, EventBus>();

  protected HandlerManager handlerManager = new HandlerManager(this);

  protected EventBus getHandler(String propertyName) {
    EventBus ret = handlerManagers.get(propertyName);
    if(ret == null) {
      ret = new SimpleEventBus();
      handlerManagers.put(propertyName, ret);
    }
    return ret;
  }

  public HandlerRegistration addSubSubModelCollectionHandler(final String modelName1,
                                                             final String modelName2,
                                                             final String collectionName,
                                                             final SubSubModelCollectionHandler handler) {
    final SmartHandler model1Handler = new SmartHandler();
    final SmartHandler model2Handler = new SmartHandler();
    model1Handler.set(addPropertyChangeHandler(modelName1, new PropertyChangeHandler() {
      @Override
      public void onPropertyChange(PropertyChangeEvent event) {
        if(event.getPreviousValue() != null) {
          Model prevModel = (Model)event.getPreviousValue();
          if(prevModel.getProperty(modelName2) != null) {
            Model prevModel2 = (Model)prevModel.getProperty(modelName2);
            if(prevModel2.getProperty(collectionName) != null) {
              List<Object> objects = (List<Object>)prevModel2.getProperty(collectionName);
              for(Object object : objects) {
                handler.onSubSubModelCollection(prevModel, prevModel2, collectionName, object, true);
              }
            }
          }
        }
        Object m1 = getProperty(modelName1);
        if(m1 != null) {
          assert m1 instanceof Model;
          final Model model = (Model)m1;
          model2Handler.set(model.addSubModelCollectionHandler(modelName2, collectionName,
                  new SubModelCollectionHandler() {
                    @Override
                    public void onSubModelCollection(Model m2,
                                                     String collectionProperty,
                                                     Object object, boolean removed) {
                      handler.onSubSubModelCollection(model, m2, collectionName, object,
                              removed);
                    }
                  }));
        }
      }
    }, true));
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        model1Handler.release();
        model2Handler.release();
      }
    };
  }

  public HandlerRegistration addSubModelCollectionHandler(final String modelName,
                                                          final String collectionName,
                                                          final SubModelCollectionHandler handler) {
    final SmartHandler modelHandlerReg = new SmartHandler();
    final SmartHandler collectionAddHandlerReg = new SmartHandler();
    final SmartHandler collectionRemoveHandlerReg = new SmartHandler();

    modelHandlerReg.set(
            addPropertyChangeHandler(modelName, new PropertyChangeHandler() {
              @Override
              public void onPropertyChange(PropertyChangeEvent event) {
                if(event.getPreviousValue() != null) {
                  Model prevModel = (Model)event.getPreviousValue();
                  List<Object> objects = (List<Object>)
                          prevModel.getProperty(collectionName);
                  if(objects != null) {
                    for(Object object : objects) {
                      handler.onSubModelCollection(prevModel, collectionName, object, true);
                    }
                  }
                }
                Object obj = getProperty(modelName);
                if(obj != null) {
                  assert obj instanceof Model;
                  final Model model = (Model)obj;
                  collectionAddHandlerReg.set(model.addCollectionAddHandler(collectionName,
                          new CollectionAddHandler() {
                            @Override
                            public void onCollectionAdd(Object object) {
                              handler.onSubModelCollection(model, collectionName,
                                      object, false);
                            }
                          }));
                  collectionRemoveHandlerReg.set(model.addCollectionRemoveHandler(collectionName,
                          new CollectionRemoveHandler() {
                            @Override
                            public void onCollectionRemove(Object object) {
                              handler.onSubModelCollection(model, collectionName,
                                      object, true);
                            }
                          }));
                  List<Object> objects = (List<Object>)model.getProperty(collectionName);
                  if(objects != null) {
                    for(Object object : objects) {
                      handler.onSubModelCollection(model, collectionName, object, false);
                    }
                  }
                }
              }
            }));
    Object prop = getProperty(modelName);
    if(prop != null) {
      assert prop instanceof Model;
      final Model model = (Model)prop;
      collectionAddHandlerReg.set(model.addCollectionAddHandler(collectionName,
              new CollectionAddHandler() {
                @Override
                public void onCollectionAdd(Object object) {
                  handler.onSubModelCollection(model, collectionName,
                          object, false);
                }
              }));
      collectionRemoveHandlerReg.set(model.addCollectionRemoveHandler(collectionName,
              new CollectionRemoveHandler() {
                @Override
                public void onCollectionRemove(Object object) {
                  handler.onSubModelCollection(model, collectionName,
                          object, true);
                }
              }));
      List<Object> objects = (List<Object>)model.getProperty(collectionName);
      if(objects != null) {
        for(Object obj : objects) {
          handler.onSubModelCollection(model, collectionName, obj, false);
        }
      }

    }
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        modelHandlerReg.release();
        collectionAddHandlerReg.release();
        collectionRemoveHandlerReg.release();
      }
    };
  }

  public HandlerRegistration addSubModelCollectionPropertyHandler(final String subModelName,
                                                             final String collectionName,
                                                             final String propertyName,
                                                             final SubModelCollectionPropertyHandler handler) {
    final SmartHandler modelHandlerReg = new SmartHandler();
    final SmartHandler collectionPropertyHandler = new SmartHandler();

    modelHandlerReg.set(addPropertyChangeHandler(subModelName, new PropertyChangeHandler() {
      @Override
      public void onPropertyChange(PropertyChangeEvent event) {
        Object object = getProperty(subModelName);
        if(object != null) {
          final Model model = (Model)object;
          collectionPropertyHandler.set(model.addCollectionPropertyHandler(collectionName, propertyName,
                  new PropertyChangeHandler() {
                    @Override
                    public void onPropertyChange(PropertyChangeEvent event) {
                      handler.onSubModelCollectionProperty(model, collectionName);
                    }
                  }));
        }
      }
    }));

    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        modelHandlerReg.release();
      }
    };
  }

  public HandlerRegistration addSubModelPropertyChangeHandler(final String modelName,
                                                              final String propertyName,
                                                              final SubModelPropertyChangeHandler handler) {
    final SmartHandler modelHandlerReg = new SmartHandler();
    final SmartHandler propertyHandlerReg = new SmartHandler();

    modelHandlerReg.set(
            addPropertyChangeHandler(modelName, new PropertyChangeHandler() {
              @Override
              public void onPropertyChange(PropertyChangeEvent event) {
                Object prop = getProperty(modelName);
                if(prop != null) {
                  assert prop instanceof Model;
                  Model model = (Model)prop;
                  propertyHandlerReg.set(model.addPropertyChangeHandler(propertyName,
                          new PropertyChangeHandler() {
                            @Override
                            public void onPropertyChange(PropertyChangeEvent event) {
                              handler.onPropertyPropertyChange(modelName, propertyName);
                            }
                          }));
                  handler.onPropertyPropertyChange(modelName, propertyName);
                }
              }
            }));
    Object prop = getProperty(modelName);
    if(prop != null) {
      assert prop instanceof  Model;
      Model model = (Model)prop;
      propertyHandlerReg.set(model.addPropertyChangeHandler(propertyName,
              new PropertyChangeHandler() {
                @Override
                public void onPropertyChange(PropertyChangeEvent event) {
                  handler.onPropertyPropertyChange(modelName, propertyName);
                }
              }));
      handler.onPropertyPropertyChange(modelName, propertyName);
    }
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        modelHandlerReg.release();
        propertyHandlerReg.release();
      }
    };
  }

  public <H extends EventHandler> void fireEvent(GwtEvent<H> event) {
    handlerManager.fireEvent(event);
  }

  public <T extends EventHandler>
  HandlerRegistration addHandler(GwtEvent.Type<T> type, T handler) {
    return handlerManager.addHandler(type, handler);
  }

  public void setProperty(String property, Object value) {
    setProperty(property, value, true);
  }

  public HandlerRegistration addCollectionAddHandler(String propertyName,
                                                     CollectionAddHandler collectionHandler) {
    return getHandler(propertyName).addHandler(CollectionAddEvent.TYPE, collectionHandler);
  }

  public HandlerRegistration addCollectionAddHandler(String propertyName,
                                                     CollectionAddHandler collectionHandler,
                                                     boolean initialFire) {
    if(initialFire) {
      for(Object j : (Collection)getProperty(propertyName)) {
        collectionHandler.onCollectionAdd(j);
      }
    }
    return getHandler(propertyName).addHandler(CollectionAddEvent.TYPE, collectionHandler);
  }

  public HandlerRegistration addCollectionRemoveHandler(String propertyName,
                                                        CollectionRemoveHandler collectionHandler) {
    return getHandler(propertyName).addHandler(CollectionRemoveEvent.TYPE, collectionHandler);
  }

  public HandlerRegistration addMapHandler(String propertyName, MapHandler handler) {
    return getHandler(propertyName).addHandler(MapEvent.TYPE, handler);
  }

  public HandlerRegistration addPropertyChangeHandler(String propertyName,
                                                      PropertyChangeHandler handler) {
    return getHandler(propertyName).addHandler(PropertyChangeEvent.TYPE, handler);
  }

  public HandlerRegistration addPropertyChangeHandler(String propertyName,
                                                      PropertyChangeHandler handler,
                                                      boolean initialFire) {
    if(initialFire) {
      handler.onPropertyChange(new PropertyChangeEvent(propertyName, null));
    }
    return getHandler(propertyName).addHandler(PropertyChangeEvent.TYPE, handler);
  }

  public HandlerRegistration addCollectionPropertyHandler(String collectionName,
                                                          final String propertyName,
                                                          final PropertyChangeHandler handler) {
    final Map<ModelBase, HandlerRegistration> handlers = new HashMap<ModelBase, HandlerRegistration>();
    final HandlerRegistration reg1 =
            addCollectionAddHandler(collectionName, new CollectionAddHandler<ModelBase>() {
              @Override
              public void onCollectionAdd(ModelBase m) {
                handlers.put(m, m.addPropertyChangeHandler(propertyName, handler));
              }
            });
    final HandlerRegistration reg2 =
            addCollectionRemoveHandler(collectionName, new CollectionRemoveHandler<ModelBase>() {
              @Override
              public void onCollectionRemove(ModelBase m) {
                if(handlers.containsKey(m)) {
                  handlers.remove(m).removeHandler();
                }
              }
            });

    for(Object o : (List<Object>)getProperty(collectionName)) {
      ModelBase m = (ModelBase)o;
      handlers.put(m, m.addPropertyChangeHandler(propertyName, handler));
    }
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        reg1.removeHandler();
        reg2.removeHandler();
        for(HandlerRegistration hr : handlers.values()) {
          hr.removeHandler();
        }
        handlers.clear();
      }
    };
  }
}
