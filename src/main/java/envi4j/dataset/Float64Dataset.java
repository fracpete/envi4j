/*
 * Float64Dataset.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.dataset;

import envi4j.core.Utils;
import envi4j.header.ByteOrder;
import envi4j.header.DataType;
import envi4j.header.Header;

import java.nio.ByteBuffer;

/**
 * Dataset implementation for {@link DataType#FLOAT64}.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Float64Dataset
  extends AbstractDataset
  implements DoubleDataset {

  /**
   * Initializes the dataset.
   *
   * @param header	the meta-data
   * @param raw 	the raw data
   * @param quiet	whether to suppress warnings/errors
   */
  public Float64Dataset(Header header, byte[] raw, boolean quiet) {
    super(header, raw, quiet);
  }

  /**
   * The data type this dataset handles.
   *
   * @return		the data type
   */
  @Override
  public DataType expectedDataType() {
    return DataType.FLOAT64;
  }

  /**
   * Returns the band as matrix.
   *
   * @param band	the band to retrieve
   * @return		the matrix
   */
  @Override
  public double[][] getBand(int band) {
    double[][]	result;
    byte[]	seq;
    int		s;
    int		i;
    int		n;
    ByteBuffer  bb;

    result = new double[m_Lines][m_Samples];
    seq    = toBand(band);

    if (getByteOrder() == ByteOrder.LITTLE_ENDIAN)
      bb = ByteBuffer.wrap(seq).order(java.nio.ByteOrder.LITTLE_ENDIAN);
    else
      bb = ByteBuffer.wrap(seq).order(java.nio.ByteOrder.BIG_ENDIAN);

    s = 0;
    for (n = 0; n < m_Lines; n++) {
      for (i = 0; i < m_Samples; i++) {
        result[n][i] = bb.getDouble(s);
        s += 8;
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
