package com.chrisgammage.gwtjitsu_test.client;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/16/12
 * Time: 12:02 PM
 */
public class TestEvent extends GwtEvent<TestHandler> {
  public static Type<TestHandler> TYPE = new Type<TestHandler>();

  public TestEvent(CommonModel model) {
    this.model = model;
  }

  public Type<TestHandler> getAssociatedType() {
    return TYPE;
  }

  private final CommonModel model;

  protected void dispatch(TestHandler handler) {
    handler.onTest(this);
  }
}
