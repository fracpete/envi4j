/*
 * UInt8Dataset.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.dataset;

import envi4j.core.Utils;
import envi4j.header.DataType;
import envi4j.header.Header;

/**
 * Dataset implementation for {@link envi4j.header.DataType#UINT8}.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class UInt8Dataset
  extends AbstractDataset
  implements ShortDataset {

  /**
   * Initializes the dataset.
   *
   * @param header	the meta-data
   * @param raw 	the raw data
   * @param quiet	whether to suppress warnings/errors
   */
  public UInt8Dataset(Header header, byte[] raw, boolean quiet) {
    super(header, raw, quiet);
  }

  /**
   * The data type this dataset handles.
   *
   * @return		the data type
   */
  @Override
  public DataType expectedDataType() {
    return DataType.UINT8;
  }

  /**
   * Returns the band as matrix.
   *
   * @param band	the band to retrieve
   * @return		the matrix
   */
  @Override
  public short[][] getBand(int band) {
    short[][]	result;
    byte[]	seq;
    int		offset;
    int		s;
    int		i;
    int		n;

    result = new short[m_Lines][m_Samples];
    seq    = toBand(band);
    s      = 0;
    for (n = 0; n < m_Lines; n++) {
      for (i = 0; i < m_Samples; i++) {
        result[n][i] = (short) (seq[s] & 0xFF);
        s++;
      }
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
