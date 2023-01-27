/*
 * Dataset.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.dataset;

import envi4j.header.ByteOrder;
import envi4j.header.DataType;
import envi4j.header.Header;
import envi4j.header.Interleave;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Interface for datasets.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public interface Dataset
  extends Serializable {

  /** the maximum characters to output. */
  public int MAX_CHARS = 80;

  /**
   * The data type this dataset handles.
   *
   * @return		the data type
   */
  public DataType expectedDataType();

  /**
   * Returns the meta-data of the dataset.
   *
   * @return		the meta-data
   */
  public Header getHeader();

  /**
   * Returns whether warnings/errors get suppressed.
   *
   * @return		true if suppressed
   */
  public boolean isQuiet();

  /**
   * Returns the underlying data type in the data.
   *
   * @return		the data type
   */
  public DataType getDataType();

  /**
   * Returns the underlying byte order in the data.
   *
   * @return		the byte order
   */
  public ByteOrder getByteOrder();

  /**
   * Returns the underlying interleave type in the data.
   *
   * @return		the interleave type
   */
  public Interleave getInterleave();

  /**
   * Returns the number of cols/samples.
   *
   * @return		the number of samples
   */
  public int getSamples();

  /**
   * Returns the number of rows/lines.
   *
   * @return		the number of lines
   */
  public int getLines();

  /**
   * Returns the number of bands.
   *
   * @return		the number of bands
   */
  public int getBands();

  /**
   * Returns the size of a pixel in bytes.
   *
   * @return		the size
   */
  public int getPixelSize();

  /**
   * Returns the underlying raw bytes.
   *
   * @return		the bytes
   */
  public byte[] toRaw();

  /**
   * Returns the bytes in sequential order.
   *
   * @return		the reordered bytes
   */
  public byte[] toSequential();

  /**
   * Returns the bytes in sequential order for the specified band.
   *
   * @return		the reordered bytes
   */
  public byte[] toBand(int band);

  /**
   * Turns the specified band into a string representation (max of {@link #MAX_CHARS}).
   *
   * @param band  	the band to convert
   * @return		the generated string representation
   */
  public String toString(int band);

  /**
   * Turns the specified band into a string representation.
   *
   * @param band  	the band to convert
   * @return		the generated string representation
   */
  public String toString(int band, int max);

  /**
   * Simply returns the header as string.
   *
   * @return		the generated string
   */
  @Override
  public String toString();

  /**
   * Turns the three bands into an RGB image.
   *
   * @param r		the band to act as red channel
   * @param g		the band to act as green channel
   * @param b		the band to act as blue channel
   * @return		the generated image
   */
  public BufferedImage toRGB(int r, int g, int b);

  /**
   * Turns the specified band into a grayscale image.
   *
   * @param band	the band to use
   * @return		the generated image
   */
  public BufferedImage toGray(int band);
}
