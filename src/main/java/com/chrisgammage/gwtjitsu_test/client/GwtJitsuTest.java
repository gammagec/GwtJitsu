package com.chrisgammage.gwtjitsu_test.client;

import com.chrisgammage.gwtjitsu.client.Model;
import com.chrisgammage.gwtjitsu.client.events.*;
import com.chrisgammage.gwtjitsu.client.impl.ModelBase;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 10:55 AM
 */
public class GwtJitsuTest implements EntryPoint {

  private boolean flag = false;
  private int cnt = 0;

  static final private Logger LOG = Logger.getLogger(GwtJitsuTest.class.getName());
  public void onModuleLoad() {
    TestInjector inject = GWT.create(TestInjector.class);
    TestModel testModel = inject.testModel();
    assert testModel instanceof ModelBase : "Model didn't come out as base";
    assert testModel.getTestString() != null : "constant binding didn't work";
    assert testModel.getTestString().equals("test") : "constant binding didn't work";

    CommonModel model = inject.commonModel();

    assert "constructed".equals(model.getTestConstruct()) : "@Construct not called";

    assert "123".equals(model.getSomeString()) : "Initial Value String failed";

    assert model.getSomeBool() : "Initial Value Bool failed";

    assert model.getSomeInt() == 4 : "Initial Value int failed";

    model.setText("test");
    assert model.isTextGet() == false;
    assert model.getText().equals("test") : "Standard setter not working";
    assert model.isTextGet() : "@BeforeGetHook not called on getText";
    assert model.isTextSet() : "@AfterSetHook not called on setText";

    flag = false;
    model.addCollectionAddHandler(CommonModel.STRINGS, new CollectionAddHandler<String>() {
      @Override
      public void onCollectionAdd(String string) {
        flag = true;
      }
    });
    model.addString("chris");
    assert model.getCounter() == 1 : "Add Hook not working";
    assert flag == true : "collection add event not working";
    assert model.getString(0).equals("chris") : "addTo not working";

    flag = false;
    model.addCollectionAddHandler(CommonModel.STRINGS2, new CollectionAddHandler<String>() {
      @Override
      public void onCollectionAdd(String string) {
        flag = true;
      }
    });
    model.addToStrings2("chris");
    assert flag == true : "collection add event not working";
    assert model.getStrings2().get(0).equals("chris") : "addTo not working";

    flag = false;
    model.addCollectionRemoveHandler(CommonModel.STRINGS, new CollectionRemoveHandler() {
      @Override
      public void onCollectionRemove(Object object) {
        flag = true;
      }
    });
    model.removeString("chris");
    assert flag = true : "collection remove event not working";
    assert model.getStringsCount() == 0 : "removeFrom not working";

    flag = false;
    model.addCollectionRemoveHandler(CommonModel.STRINGS2, new CollectionRemoveHandler() {
      @Override
      public void onCollectionRemove(Object object) {
        flag = true;
      }
    });
    model.removeFromStrings2("chris");
    assert flag = true : "collection remove event not working";
    assert model.getStrings2().size() == 0 : "removeFrom not working";

    flag = false;
    model.addMapHandler(CommonModel.IDS, new MapHandler<CommonModel, String>() {
      @Override
      public void onMap(CommonModel model, String property, String key) {
        flag = true;
      }
    });
    model.putIntoIDs("chris", 52L);
    assert flag : "map observable not working";
    assert model.getFromIDs("chris") == 52L : "map not working";

    flag = false;
    model.addPropertyChangeHandler(CommonModel.TEXT_OBS, new PropertyChangeHandler() {
      @Override
      public void onPropertyChange(PropertyChangeEvent event) {
        flag = true;
      }
    });
    model.setTextObs("test2");
    assert flag : "Property Change Handler not working on Observable";
    assert model.getTextObs().equals("test2") : "Setter not working on textObs";

    model.setFilterTest(12);
    assert model.getProperty(CommonModel.FILTER_TEST).equals(5) : "Model filter not working";

    model.doSomething();
    assert model.getCounter() == 2 : "Model Helper function not working";

    flag = false;
    model.addHandler(TestEvent.TYPE, new TestHandler() {
      @Override
      public void onTest(TestEvent event) {
        flag = true;
      }
    });
    model.setSomethingFiresEvent(5);
    assert model.getSomethingFiresEvent() == 5 : "Value from Observable event failed";
    assert flag : "Observable Event Failed";

    SubModel subModel = inject.subModel();
    model.addSubProperty(subModel);

    HandlerRegistration hr = model.addCollectionPropertyHandler(CommonModel.SUB_PROPERTIES,
            SubModel.PROP,
            new PropertyChangeHandler() {
              @Override
              public void onPropertyChange(PropertyChangeEvent event) {
                cnt++;
              }
            });

    assert cnt == 0;
    subModel.setProp("test1");
    assert cnt == 1;
    subModel.setProp("test2");
    assert cnt == 2;
    subModel.setProp("test2");
    assert cnt == 2;
    hr.removeHandler();
    subModel.setProp("test3");
    assert cnt == 2;

    LOG.log(Level.INFO, "Tests Passed");

    model.setProperty(CommonModel.SOME_STRING, "blah");
    model.setProperty(CommonModel.SOME_BOOL, true);
    model.setSomeInt(8);
    model.setTestConstruct("hello");
    model.setText("boo!");

    CommonDTO dto = new CommonDTO();
    model.mapTo(dto);

    assert dto.getSomeString().equals("blah");
    assert dto.isSomeBool();
    assert dto.getSomeInt() == 8;
    assert dto.getTestConstruct().equals("hello");
    assert dto.getText().equals("boo!");

    dto.setSomeString("bb");
    dto.setSomeBool(false);
    dto.setSomeInt(32);
    dto.setTestConstruct("cc");
    dto.setText("dd");

    model.mapFrom(dto);

    assert model.getSomeString().equals("bb");
    assert model.getSomeBool() == false;
    assert model.getSomeInt() == 32;
    assert model.getTestConstruct().equals("cc");
    assert model.getText().equals("dd");

    TestPresenter presenter = inject.testPresenter();
    assert presenter != null;
    assert presenter.isTextSet() == true;
    presenter.getModel().setTextSet(false);

    presenter.getModel().setText("hello");
    assert presenter.isTextSet();

    cnt = 0;
    HandlerRegistration subModelHandlerReg = model.addSubModelPropertyChangeHandler(CommonModel.SUB_MODEL,
            SubModel.PROP, new SubModelPropertyChangeHandler() {

      @Override
      public void onPropertyPropertyChange(String modelName, String propertyName) {
        assert modelName != null && modelName.equals(CommonModel.SUB_MODEL);
        assert propertyName != null && propertyName.equals(SubModel.PROP);
        cnt++;
      }
    });
    SubModel testSubModel = inject.subModel();

    model.setSubModel(testSubModel);
    assert cnt == 1;
    testSubModel.setProp("test");
    assert cnt == 2;
    SubModel testSubModel2 = inject.subModel();
    model.setSubModel(testSubModel2);
    assert cnt == 3;
    subModelHandlerReg.removeHandler();
    testSubModel2.setProp("test2");
    assert cnt == 3;

    cnt = 0;
    model.addSubModelCollectionHandler(CommonModel.SUB_MODEL,
            SubModel.STRINGS, new SubModelCollectionHandler<String>() {

      @Override
      public void onSubModelCollection(Model modelProperty,
                                       String collectionProperty,
                                       String Object, boolean removed) {
        if(removed) {
          cnt--;
        } else {
          cnt++;
        }
      }
    });
    presenter.getModel().setSubModel(testSubModel2);
    assert cnt == 0;
    assert presenter.getInternalCount() == 0;
    testSubModel2.addString("hi");
    testSubModel2.addString("hi2");
    testSubModel2.addString("hi3");
    assert cnt == 3;
    assert presenter.getInternalCount() == 3;
    model.setSubModel(testSubModel);
    presenter.getModel().setSubModel(testSubModel);
    assert cnt == 0;
    assert presenter.getInternalCount() == 0;
    model.setSubModel(testSubModel2);
    presenter.getModel().setSubModel(testSubModel2);
    assert cnt == 3;
    assert presenter.getInternalCount() == 3;

    Window.alert("tests passed!");
  }
}
