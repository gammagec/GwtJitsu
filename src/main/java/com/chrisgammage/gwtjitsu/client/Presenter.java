package com.chrisgammage.gwtjitsu.client;

import com.chrisgammage.ginjitsu.client.AfterInject;
import com.chrisgammage.gwtjitsu.client.impl.PresenterBase;
import com.google.inject.ImplementedBy;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 10:56 AM
 */
@ImplementedBy(PresenterBase.class)
public interface Presenter {
  @AfterInject
  void setupModelHandlers();
}
