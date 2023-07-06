/*
 * ENVI.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j;

import envi4j.core.FileUtils;
import envi4j.dataset.AbstractDataset;
import envi4j.header.Header;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Reads datasets.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class ENVI {

  public final static String[] EXTENSIONS = {".dat", ".DAT", ".raw", ""};

  /**
   * Loads the ENVI dataset from the specified file.
   * Suppresses warnings/errors.
   *
   * @param hdrFile	the .hdr file of the dataset to load
   * @return		the dataset, null if failed to load
   */
  public static AbstractDataset load(File hdrFile) {
    return load(hdrFile, true);
  }

  /**
   * Loads the ENVI dataset from the specified file.
   *
   * @param hdrFile	the .hdr file of the dataset to load
   * @param quiet 	whether to suppress warnings/errors
   * @return		the dataset, null if failed to load
   */
  public static AbstractDataset load(File hdrFile, boolean quiet) {
    return load(hdrFile, quiet, EXTENSIONS);
  }

  /**
   * Loads the ENVI dataset from the specified file.
   *
   * @param hdrFile	the .hdr file of the dataset to load
   * @param quiet 	whether to suppress warnings/errors
   * @param extensions  the extensions of the data files to look for
   * @return		the dataset, null if failed to load
   */
  public static AbstractDataset load(File hdrFile, boolean quiet, String[] extensions) {
    Header	header;
    File	datFile;

    header = Header.read(hdrFile);
    if (header != null) {
      for (String ext: extensions) {
	datFile = FileUtils.replaceExtension(hdrFile, ext);
	if (datFile.exists())
	  return AbstractDataset.read(header, datFile, quiet);
      }
      if (!quiet)
	System.err.println("Failed to locate corresponding data file, looked for: " + Arrays.asList(EXTENSIONS));
    }

    return null;
  }

  /**
   * Determines the image type based on the file's extension.
   *
   * @param output	the file to determine the type for
   * @return		the image type
   * @throws IOException	if failed to determine the image type
   */
  protected static String determineImageType(File output) throws IOException {
    if (output.getName().toLowerCase().endsWith(".jpg") || output.getName().toLowerCase().endsWith(".jpeg"))
      return "jpg";
    else if (output.getName().toLowerCase().endsWith(".png"))
      return "png";
    else
      throw new IOException("Only .jpg, .jpeg and .png supported as file extension!");
  }

  /**
   * Saves the specified bands as RGB image. Uses the file's extension to determine
   * whether to save as JPG (.jpg/.jpeg) or PNG (.png).
   *
   * @param dataset	the dataset to use
   * @param r		the band to act as red channel
   * @param g		the band to act as green channel
   * @param b		the band to act as blue channel
   * @param output	the output file
   * @throws IOException	if unsupported extension or failed to output
   */
  public static void saveRGB(AbstractDataset dataset, int r, int g, int b, File output) throws IOException {
    ImageIO.write(dataset.toRGB(r, g, b), determineImageType(output), output);
  }

  /**
   * Saves the specified bands as grayscale image. Uses the file's extension to determine
   * whether to save as JPG (.jpg/.jpeg) or PNG (.png).
   *
   * @param dataset	the dataset to use
   * @param band	the band to use
   * @param output	the output file
   * @throws IOException	if unsupported extension or failed to output
   */
  public static void saveGray(AbstractDataset dataset, int band, File output) throws IOException {
    ImageIO.write(dataset.toGray(band), determineImageType(output), output);
  }
}
