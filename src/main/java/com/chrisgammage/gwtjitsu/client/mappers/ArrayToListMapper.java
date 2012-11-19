package com.chrisgammage.gwtjitsu.client.mappers;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/19/12
 * Time: 12:18 AM
 */
public class ArrayToListMapper {
  public static <T> List<T> map(T[] objects) {
    return Arrays.asList(objects);
  }
}
