package com.chrisgammage.gwtjitsu.client.mappers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/25/12
 * Time: 11:36 PM
 */
public class ArrayToArrayListMapper {
  public static <T> ArrayList<T> map(T[] objects) {
    return new ArrayList<T>(Arrays.asList(objects));
  }
}
