/*
 * FileUtils.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.core;

import java.io.File;

/**
 * Helper class for I/O related tasks.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class FileUtils {

  /**
   * Replaces the extension of the given file with the new one. Leave the
   * new extension empty if you want to remove the extension.
   * Always removes ignored extension suffixes first from the filename.
   *
   * @param file	the file to replace the extension for
   * @param newExt	the new extension (incl dot), empty string to remove extension
   * @return		the updated file
   */
  public static File replaceExtension(File file, String newExt) {
    return new File(replaceExtension(file.getAbsolutePath(), newExt));
  }

  /**
   * Replaces the extension of the given file with the new one. Leave the
   * new extension empty if you want to remove the extension.
   * Always removes ignored extension suffixes first from the filename.
   *
   * @param file	the file to replace the extension for
   * @param newExt	the new extension (incl dot), empty string to remove extension
   * @return		the updated file
   */
  public static String replaceExtension(String file, String newExt) {
    String	result;
    int		index;

    result = file;
    index  = file.lastIndexOf('.');
    if (index > -1) {
      if (newExt.length() > 0)
	result = file.substring(0, index) + newExt;
      else
	result = file.substring(0, index);
    }

    return result;
  }

}
