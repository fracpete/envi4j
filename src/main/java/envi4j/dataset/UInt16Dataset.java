/*
 * UInt16Dataset.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.dataset;

import envi4j.core.Utils;
import envi4j.header.ByteOrder;
import envi4j.header.DataType;
import envi4j.header.Header;

import java.awt.image.BufferedImage;

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

  /**
   * Turns the specified band into a grayscale image.
   *
   * @param band	the band to use
   * @return		the generated image
   */
  @Override
  public BufferedImage toGray(int band) {
    BufferedImage	result;
    int[][]		data;
    double[]		minAndMax;
    double		min;
    double		range;
    int[] 		pixels;
    int			i;
    int			n;
    int			p;

    pixels    = new int[m_Lines * m_Samples];
    data      = getBand(band);
    minAndMax = Utils.minAndMax(data);
    min       = minAndMax[0];
    range     = minAndMax[1] - minAndMax[0];

    if (range > 0) {
      for (n = 0; n < m_Lines; n++) {
        for (i = 0; i < m_Samples; i++) {
          p = (int) ((data[n][i] - min) / range * 255);
          pixels[n*m_Samples + i] = p << 16 + p << 8 + p;
        }
      }
    }

    result = new BufferedImage(m_Samples, m_Lines, BufferedImage.TYPE_BYTE_GRAY);
    result.setRGB(0, 0, m_Samples, m_Lines, pixels, 0, m_Samples);

    return result;
  }

  /**
   * Turns the three bands into an RGB image.
   *
   * @param r		the band to act as red channel
   * @param g		the band to act as green channel
   * @param b		the band to act as blue channel
   * @return		the generated image
   */
  @Override
  public BufferedImage toRGB(int r, int g, int b) {
    BufferedImage	result;
    int[][]		red;
    int[][]		green;
    int[][]		blue;
    double[] 		minAndMaxRed;
    double[] 		minAndMaxGreen;
    double[] 		minAndMaxBlue;
    double 		minRed;
    double 		minGreen;
    double 		minBlue;
    double 		rangeRed;
    double 		rangeGreen;
    double 		rangeBlue;
    int[] 		pixels;
    int			i;
    int			n;
    int 		pRed;
    int 		pGreen;
    int 		pBlue;

    pixels         = new int[m_Lines * m_Samples];
    red            = getBand(r);
    green          = getBand(g);
    blue           = getBand(b);
    minAndMaxRed   = Utils.minAndMax(red);
    minAndMaxGreen = Utils.minAndMax(green);
    minAndMaxBlue  = Utils.minAndMax(blue);
    minRed         = minAndMaxRed[0];
    minGreen       = minAndMaxGreen[0];
    minBlue        = minAndMaxBlue[0];
    rangeRed       = minAndMaxRed[1] - minAndMaxRed[0];
    rangeGreen     = minAndMaxGreen[1] - minAndMaxGreen[0];
    rangeBlue      = minAndMaxBlue[1] - minAndMaxBlue[0];

    System.out.println("R: " + minRed + ", " + rangeRed);
    System.out.println("G: " + minGreen + ", " + rangeGreen);
    System.out.println("B: " + minBlue + ", " + rangeBlue);

    if (rangeRed > 0) {
      for (n = 0; n < m_Lines; n++) {
        for (i = 0; i < m_Samples; i++) {
          pRed   = (int) ((red[n][i] - minRed) / rangeRed * 255);
          pGreen = (int) ((green[n][i] - minGreen) / rangeGreen * 255);
          pBlue  = (int) ((blue[n][i] - minBlue) / rangeBlue * 255);
          pixels[n*m_Samples + i] = pRed << 16 + pGreen << 8 + pBlue;
        }
      }
    }

    result = new BufferedImage(m_Samples, m_Lines, BufferedImage.TYPE_INT_RGB);
    result.setRGB(0, 0, m_Samples, m_Lines, pixels, 0, m_Samples);

    return result;
  }
}
