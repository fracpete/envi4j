/*
 * UInt16Dataset.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.dataset;

import envi4j.core.Utils;
import envi4j.header.ByteOrder;
import envi4j.header.DataType;
import envi4j.header.Header;

/**
 * Dataset implementation for {@link envi4j.header.DataType#UINT16}.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class UInt16Dataset
  extends AbstractDataset
  implements IntDataset {

  /**
   * Initializes the dataset.
   *
   * @param header	the meta-data
   * @param raw 	the raw data
   * @param quiet	whether to suppress warnings/errors
   */
  public UInt16Dataset(Header header, byte[] raw, boolean quiet) {
    super(header, raw, quiet);
  }

  /**
   * The data type this dataset handles.
   *
   * @return		the data type
   */
  @Override
  public DataType expectedDataType() {
    return DataType.UINT16;
  }

  /**
   * Returns the band as matrix.
   *
   * @param band	the band to retrieve
   * @return		the matrix
   */
  @Override
  public int[][] getBand(int band) {
    int[][]	result;
    byte[]	seq;
    int		s;
    int		i;
    int		n;
    int		v;

    result = new int[m_Lines][m_Samples];
    seq    = toBand(band);
    s      = 0;

    if (getByteOrder() == ByteOrder.LITTLE_ENDIAN) {
      for (n = 0; n < m_Lines; n++) {
        for (i = 0; i < m_Samples; i++) {
          v = (seq[s] & 0XFF) << 8
            + (seq[s + 1] & 0XFF);
          result[n][i] = v;
          s += 2;
        }
      }
    }
    else {
      throw new IllegalStateException("Not supported: " + getByteOrder());
    }

    return result;
  }

  /**
   * Turns the specified band into a string representation.
   *
   * @param band  	the band to convert
   * @return		the generated string representation
   */
  @Override
  public String toString(int band, int max) {
    return Utils.arrayToString(getBand(band), max);
  }
}
