package com.chrisgammage.gwtjitsu_test.client.impl;

import com.chrisgammage.gwtjitsu.client.impl.PresenterBase;
import com.chrisgammage.gwtjitsu.shared.AfterModelSet;
import com.chrisgammage.gwtjitsu.shared.AfterSubModelCollectionChanged;
import com.chrisgammage.gwtjitsu.shared.PresenterModel;
import com.chrisgammage.gwtjitsu_test.client.CommonModel;
import com.chrisgammage.gwtjitsu_test.client.SubModel;
import com.chrisgammage.gwtjitsu_test.client.TestPresenter;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/31/12
 * Time: 4:31 PM
 */
public abstract class TestPresenterImpl extends PresenterBase implements TestPresenter {

  @PresenterModel
  @Inject protected CommonModel commonModel;

  private boolean isTextSet;

  private int internalCount;

  @AfterModelSet(CommonModel.TEXT)
  protected void onTextChanged() {
    isTextSet = true;
  }

  @AfterSubModelCollectionChanged(subModel = CommonModel.SUB_MODEL,
          collection = SubModel.STRINGS)
  protected void onSubModelStringsChanged(SubModel model, String str, boolean removed) {
    if(removed) {
      internalCount--;
    } else {
      internalCount++;
    }
  }

  @Override
  public CommonModel getModel() {
    return commonModel;
  }

  @Override
  public boolean isTextSet() {
    return isTextSet;
  }

  @Override
  public int getInternalCount() {
    return internalCount;
  }

  @Override
  public void setInternalCount(int internalCount) {
    this.internalCount = internalCount;
  }
}
