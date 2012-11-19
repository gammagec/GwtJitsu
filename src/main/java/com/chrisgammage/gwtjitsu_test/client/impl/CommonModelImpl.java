package com.chrisgammage.gwtjitsu_test.client.impl;

import com.chrisgammage.gwtjitsu.client.impl.ModelBase;
import com.chrisgammage.gwtjitsu.shared.*;
import com.chrisgammage.gwtjitsu_test.client.CommonModel;
import com.chrisgammage.gwtjitsu_test.client.SubModel;
import com.chrisgammage.gwtjitsu_test.client.TestEvent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/28/12
 * Time: 10:44 AM
 */
public abstract class CommonModelImpl extends ModelBase implements CommonModel {

  @Property
  protected SubModel subModel;

  @Collection(singular = "subProperty")
  protected ArrayList<SubModel> subProperties = new ArrayList<SubModel>();

  @Collection(singular = "string")
  protected ArrayList<String> strings = new ArrayList<String>();

  @Collection
  protected ArrayList<String> strings2 = new ArrayList<String>();

  @Property
  protected String someString = "123";

  @Collection
  protected HashMap<String, Long> iDs = new HashMap<String, Long>();

  @Property
  protected boolean someBool = true;

  @Property
  protected int someInt = 4;

  @Property
  protected String testConstruct;

  @Property
  protected String text;

  @Property
  protected int filterTest;

  @Property
  protected boolean textSet;

  @Property
  protected boolean textGet;

  @Property
  @ObservableEvent(TestEvent.class)
  protected int somethingFiresEvent;

  @Property
  protected String textObs;

  @Property
  protected int counter;

  public CommonModelImpl() {
    setTestConstruct("constructed");
  }

  @Filter(CommonModel.FILTER_TEST)
  protected int filterFilterTest(int in) {
    return 5;
  }

  @AfterAdd(CommonModel.STRINGS)
  protected void afterAddString(String string) {
    doSomething();
  }

  @AfterSet(CommonModel.TEXT)
  protected void afterSetText() {
    setTextSet(true);
  }

  @BeforeGet(CommonModel.TEXT)
  protected void beforeGetText() {
    setProperty(TEXT_GET, true);
  }

  @Override
  public void doSomething() {
    setProperty(COUNTER, getCounter() + 1);
  }
}
