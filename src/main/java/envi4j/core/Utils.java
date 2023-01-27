/*
 * Utils.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.core;

import java.lang.reflect.Array;

/**
 * Contains various helper methods.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Utils {

  /**
   * Turns an array into a string representation.
   *
   * @param array	the array the convert
   * @return		the string representation
   */
  public static String arrayToString(Object array) {
    return arrayToString(array, -1);
  }

  /**
   * Turns an array into a string representation.
   *
   * @param array	the array the convert
   * @param max		the maximum length of the string to generate, ignored if < 1
   * @return		the string representation
   */
  public static String arrayToString(Object array, int max) {
    StringBuilder	result;
    int			i;
    Object		element;

    if (!array.getClass().isArray())
      return array.toString();

    result = new StringBuilder();
    result.append("[");
    for (i = 0; i < Array.getLength(array); i++) {
      element = Array.get(array, i);
      if (i > 0)
	result.append(",");
      if (element == null) {
	result.append("null");
      }
      else {
	if (element.getClass().isArray())
	  result.append(arrayToString(element));
	else
	  result.append(element.toString());
      }

      // maximum reached?
      if (max > 0) {
	if (result.length() >= max)
	  break;
      }
    }

    if ((max <= 0) || (result.length() < max))
      result.append("]");

    // trim?
    if ((max > 0) && (result.length() > max)) {
      result.delete(max - 3, result.length());
      result.append("...");
    }

    return result.toString();
  }

  /**
   * Turns the byte array into a Byte array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[] toNumberArray(byte[] array) {
    Byte[]	result;
    int		i;

    if (array == null)
      return null;

    result = new Byte[array.length];
    for (i = 0; i < array.length; i++)
      result[i] = array[i];

    return result;
  }

  /**
   * Turns the byte array into a Byte array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[][] toNumberArray(byte[][] array) {
    Byte[][]	result;
    int		i;

    if (array == null)
      return null;

    result = new Byte[array.length][];
    for (i = 0; i < array.length; i++)
      result[i] = (Byte[]) toNumberArray(array[i]);

    return result;
  }

  /**
   * Turns the byte array into a Byte array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[] toNumberArray(short[] array) {
    Short[]	result;
    int		i;

    if (array == null)
      return null;

    result = new Short[array.length];
    for (i = 0; i < array.length; i++)
      result[i] = array[i];

    return result;
  }

  /**
   * Turns the byte array into a Byte array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[][] toNumberArray(short[][] array) {
    Short[][]	result;
    int		i;

    if (array == null)
      return null;

    result = new Short[array.length][];
    for (i = 0; i < array.length; i++)
      result[i] = (Short[]) toNumberArray(array[i]);

    return result;
  }

  /**
   * Turns the int array into a Integer array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[] toNumberArray(int[] array) {
    Integer[]	result;
    int		i;

    if (array == null)
      return null;

    result = new Integer[array.length];
    for (i = 0; i < array.length; i++)
      result[i] = array[i];

    return result;
  }

  /**
   * Turns the int array into a Integer array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[][] toNumberArray(int[][] array) {
    Integer[][]	result;
    int		i;

    if (array == null)
      return null;

    result = new Integer[array.length][];
    for (i = 0; i < array.length; i++)
      result[i] = (Integer[]) toNumberArray(array[i]);

    return result;
  }

  /**
   * Turns the float array into a Float array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[] toNumberArray(float[] array) {
    Float[]	result;
    int		i;

    if (array == null)
      return null;

    result = new Float[array.length];
    for (i = 0; i < array.length; i++)
      result[i] = array[i];

    return result;
  }

  /**
   * Turns the float array into a Float array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[][] toNumberArray(float[][] array) {
    Float[][]	result;
    int		i;

    if (array == null)
      return null;

    result = new Float[array.length][];
    for (i = 0; i < array.length; i++)
      result[i] = (Float[]) toNumberArray(array[i]);

    return result;
  }

  /**
   * Turns the double array into a Double array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[] toNumberArray(double[] array) {
    Double[]	result;
    int		i;

    if (array == null)
      return null;

    result = new Double[array.length];
    for (i = 0; i < array.length; i++)
      result[i] = array[i];

    return result;
  }

  /**
   * Turns the double array into a Double array.
   *
   * @param array	the array to convert
   * @return		the converted array
   */
  public static Number[][] toNumberArray(double[][] array) {
    Double[][]	result;
    int		i;

    if (array == null)
      return null;

    result = new Double[array.length][];
    for (i = 0; i < array.length; i++)
      result[i] = (Double[]) toNumberArray(array[i]);

    return result;
  }

  /**
   * Determines the minimum and maximum values in the short array.
   *
   * @param array	the array to use
   * @return		the min/max
   */
  public static double[] minAndMax(short[] array) {
    double[]	result;
    int		i;

    result = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    for (i = 0; i < array.length; i++) {
      result[0] = Math.min(result[0], array[i]);
      result[1] = Math.max(result[1], array[i]);
    }

    return result;
  }

  /**
   * Determines the minimum and maximum values in the int array.
   *
   * @param array	the array to use
   * @return		the min/max
   */
  public static double[] minAndMax(int[] array) {
    double[]	result;
    int		i;

    result = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    for (i = 0; i < array.length; i++) {
      result[0] = Math.min(result[0], array[i]);
      result[1] = Math.max(result[1], array[i]);
    }

    return result;
  }

  /**
   * Determines the minimum and maximum values in the float array.
   *
   * @param array	the array to use
   * @return		the min/max
   */
  public static double[] minAndMax(float[] array) {
    double[]	result;
    int		i;

    result = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    for (i = 0; i < array.length; i++) {
      result[0] = Math.min(result[0], array[i]);
      result[1] = Math.max(result[1], array[i]);
    }

    return result;
  }

  /**
   * Determines the minimum and maximum values in the double array.
   *
   * @param array	the array to use
   * @return		the min/max
   */
  public static double[] minAndMax(double[] array) {
    double[]	result;
    int		i;

    result = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    for (i = 0; i < array.length; i++) {
      result[0] = Math.min(result[0], array[i]);
      result[1] = Math.max(result[1], array[i]);
    }

    return result;
  }

  /**
   * Determines the minimum and maximum values in the short matrix.
   *
   * @param matrix	the matrix to use
   * @return		the min/max
   */
  public static double[] minAndMax(short[][] matrix) {
    double[]	result;
    int		i;
    int		n;

    result = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    for (n = 0; n < matrix.length; n++) {
      for (i = 0; i < matrix[n].length; i++) {
	result[0] = Math.min(result[0], matrix[n][i]);
	result[1] = Math.max(result[1], matrix[n][i]);
      }
    }

    return result;
  }

  /**
   * Determines the minimum and maximum values in the int matrix.
   *
   * @param matrix	the matrix to use
   * @return		the min/max
   */
  public static double[] minAndMax(int[][] matrix) {
    double[]	result;
    int		i;
    int		n;

    result = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    for (n = 0; n < matrix.length; n++) {
      for (i = 0; i < matrix[n].length; i++) {
	result[0] = Math.min(result[0], matrix[n][i]);
	result[1] = Math.max(result[1], matrix[n][i]);
      }
    }

    return result;
  }

  /**
   * Determines the minimum and maximum values in the float matrix.
   *
   * @param matrix	the matrix to use
   * @return		the min/max
   */
  public static double[] minAndMax(float[][] matrix) {
    double[]	result;
    int		i;
    int		n;

    result = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    for (n = 0; n < matrix.length; n++) {
      for (i = 0; i < matrix[n].length; i++) {
	result[0] = Math.min(result[0], matrix[n][i]);
	result[1] = Math.max(result[1], matrix[n][i]);
      }
    }

    return result;
  }

  /**
   * Determines the minimum and maximum values in the double matrix.
   *
   * @param matrix	the matrix to use
   * @return		the min/max
   */
  public static double[] minAndMax(double[][] matrix) {
    double[]	result;
    int		i;
    int		n;

    result = new double[]{Double.MAX_VALUE, -Double.MAX_VALUE};
    for (n = 0; n < matrix.length; n++) {
      for (i = 0; i < matrix[n].length; i++) {
	result[0] = Math.min(result[0], matrix[n][i]);
	result[1] = Math.max(result[1], matrix[n][i]);
      }
    }

    return result;
  }
}
