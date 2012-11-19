package com.chrisgammage.gwtjitsu_test.client;

import com.chrisgammage.gwtjitsu.client.Presenter;
import com.chrisgammage.gwtjitsu_test.client.impl.TestPresenterImpl;
import com.google.inject.ImplementedBy;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/31/12
 * Time: 4:31 PM
 */
@ImplementedBy(TestPresenterImpl.class)
public interface TestPresenter extends Presenter {
  boolean isTextSet();
  CommonModel getModel();

  int getInternalCount();
  void setInternalCount(int internalCount);
}
