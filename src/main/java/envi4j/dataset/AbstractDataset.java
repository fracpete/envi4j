/*
 * AbstractDataset.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.dataset;

import envi4j.header.ByteOrder;
import envi4j.header.DataType;
import envi4j.header.Header;
import envi4j.header.HeaderField;
import envi4j.header.Interleave;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

/**
 * Ancestor for datasets of specific data types.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractDataset
  implements Dataset {

  /** whether to be quiet in the parsing. */
  protected boolean m_Quiet;

  /** the meta-data. */
  protected Header m_Header;

  /** the raw data. */
  protected byte[] m_Raw;

  /** the look up table (band -> line -> sample). */
  protected int[] m_Lookup;

  /** the bytes in sequential order (band -> line -> sample). */
  protected byte[] m_Sequential;

  /** the number of bands. */
  protected int m_Bands;

  /** the number of lines. */
  protected int m_Lines;

  /** the number of samples. */
  protected int m_Samples;

  /** the data type. */
  protected DataType m_DataType;

  /** the byte order. */
  protected ByteOrder m_ByteOrder;

  /** the interleave. */
  protected Interleave m_Interleave;

  /** the pixel size in bytes. */
  protected int m_PixelSize;

  /**
   * Initializes the dataset.
   *
   * @param header	the meta-data
   * @param raw		the raw data
   */
  public AbstractDataset(Header header, byte[] raw, boolean quiet) {
    if (header == null)
      throw new IllegalArgumentException("Header cannot be null!");
    if (raw == null)
      throw new IllegalArgumentException("Raw data cannot be null!");
    if (header.getDataType() != expectedDataType())
      throw new IllegalStateException("Expected data type " + expectedDataType() + " but found " + header.getDataType() + " in header!");

    m_Header     = header;
    m_Raw        = raw;
    m_Quiet      = quiet;
    m_Lookup     = null;
    m_Sequential = null;
    m_Bands      = m_Header.getBands();
    m_Lines      = m_Header.getLines();
    m_Samples    = m_Header.getSamples();
    m_DataType   = m_Header.getDataType();
    m_ByteOrder  = m_Header.getByteOrder();
    m_PixelSize  = m_DataType.getSize();
    m_Interleave = m_Header.getInterleave();

    check();
    initLookup();
  }

  /**
   * Performs some checks.
   */
  protected void check() {
    long 	expected;

    if (m_Quiet)
      return;

    expected = m_Samples * m_Lines * m_Bands * m_PixelSize;
    if (expected != m_Raw.length)
      System.err.println("Data size != expected size: " + m_Raw.length + " != " + expected);
  }

  /**
   * Creates a lookup table for the raw bytes.
   *
   * @see #toSequential()
   */
  protected void initLookup() {
    byte[] 	seq;
    int		bandLen;
    int		lineLen;
    int		i;
    int		l;
    int		b;
    int 	s;

    if (m_Lookup != null)
      return;

    m_Lookup = new int[m_Bands * m_Lines * m_Samples];
    lineLen  = m_Samples;
    bandLen  = lineLen * m_Lines;

    switch (getInterleave()) {
      case BAND_SEQUENTIAL:
	for (i = 0; i < m_Lookup.length; i++)
	  m_Lookup[i] = i * m_PixelSize;
	break;

      case BAND_INTERLEAVED_BY_LINE:
	for (i = 0; i < m_Lookup.length; i++) {
	  b = (i / lineLen) % m_Bands;
	  l = i / (lineLen * m_Bands);
	  s = i % m_Samples;
	  m_Lookup[i] = b * bandLen * m_PixelSize + l * lineLen * m_PixelSize + s * m_PixelSize;
	}
	break;

      case BAD_INTERLEAVED_BY_PIXEL:
	for (i = 0; i < m_Lookup.length; i++) {
	  b  = i % m_Bands;                 // the band
	  s  = (i / m_Bands) % lineLen;     // the sample
	  l  = i / (m_Bands * m_Samples);   // the global line index
	  m_Lookup[i] = b * bandLen * m_PixelSize + l * lineLen * m_PixelSize + s * m_PixelSize;
	}
	break;

      default:
	throw new IllegalStateException("Unhandled interleave: " + m_Interleave);
    }
  }

  /**
   * Returns the meta-data of the dataset.
   *
   * @return		the meta-data
   */
  @Override
  public Header getHeader() {
    return m_Header;
  }

  /**
   * Returns whether warnings/errors get suppressed.
   *
   * @return		true if suppressed
   */
  @Override
  public boolean isQuiet() {
    return m_Quiet;
  }

  /**
   * Returns the underlying data type in the data.
   *
   * @return		the data type
   */
  @Override
  public DataType getDataType() {
    return m_DataType;
  }

  /**
   * Returns the underlying byte order in the data.
   *
   * @return		the byte order
   */
  @Override
  public ByteOrder getByteOrder() {
    return m_ByteOrder;
  }

  /**
   * Returns the underlying interleave type in the data.
   *
   * @return		the interleave type
   */
  @Override
  public Interleave getInterleave() {
    return m_Interleave;
  }

  /**
   * Returns the number of cols/samples.
   *
   * @return		the number of samples
   */
  @Override
  public int getSamples() {
    return m_Samples;
  }

  /**
   * Returns the number of rows/lines.
   *
   * @return		the number of lines
   */
  @Override
  public int getLines() {
    return m_Lines;
  }

  /**
   * Returns the number of bands.
   *
   * @return		the number of bands
   */
  @Override
  public int getBands() {
    return m_Bands;
  }

  /**
   * Returns the size of a pixel in bytes.
   *
   * @return		the size
   */
  @Override
  public int getPixelSize() {
    return m_PixelSize;
  }

  /**
   * Returns the underlying raw bytes.
   *
   * @return		the bytes
   */
  @Override
  public byte[] toRaw() {
    return m_Raw;
  }

  /**
   * Returns the bytes in sequential order.
   *
   * @return		the reordered bytes
   */
  @Override
  public synchronized byte[] toSequential() {
    int		i;
    int		o;
    int		n;

    if (m_Sequential != null)
      return m_Sequential;

    initLookup();

    m_Sequential = new byte[m_Raw.length];
    for (i = 0; i < m_Lookup.length; i++) {
      for (n = 0; n < m_PixelSize; n++)
	m_Sequential[m_Lookup[i] + n] = m_Raw[i*m_PixelSize + n];
    }

    return m_Sequential;
  }

  /**
   * Returns the bytes in sequential order for the specified band.
   *
   * @return		the reordered bytes
   */
  @Override
  public byte[] toBand(int band) {
    byte[]	result;
    byte[]	seq;
    int		i;
    int		offset;

    result = new byte[m_Lines * m_Samples * m_PixelSize];
    offset = m_Lines * m_Samples * m_PixelSize * band;
    seq    = toSequential();
    for (i = 0; i < result.length; i++)
      result[i] = seq[offset + i];

    return result;
  }

  /**
   * Simply returns the header as string.
   *
   * @return		the generated string
   */
  @Override
  public String toString() {
    return m_Header.toString();
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
    throw new IllegalStateException("Not implemented!");
  }

  /**
   * Turns the specified band into a grayscale image.
   *
   * @param band	the band to use
   * @return		the generated image
   */
  @Override
  public BufferedImage toGray(int band) {
    throw new IllegalStateException("Not implemented!");
  }

  /**
   * Turns the specified band into a string representation (max of {@link #MAX_CHARS}).
   *
   * @param band  	the band to convert
   * @return		the generated string representation
   */
  @Override
  public String toString(int band) {
    return toString(band, MAX_CHARS);
  }

  /**
   * Reads the dataset from the specified data file using the meta-data from the header.
   *
   * @param header	the meta-data to use for reading
   * @param input	the binary data file to read
   * @return		the dataset, null if failed to read
   */
  public static AbstractDataset read(Header header, File input) {
    return read(header, input, true);
  }

  /**
   * Reads the dataset from the specified data file using the meta-data from the header.
   *
   * @param header	the meta-data to use for reading
   * @param input	the binary data file to read
   * @param quiet	whether to suppress warnings/errors
   * @return		the dataset, null if failed to read
   */
  public static AbstractDataset read(Header header, File input, boolean quiet) {
    byte[] 	raw;
    DataType	dtype;

    try {
      raw   = Files.readAllBytes(input.toPath());
      dtype = header.get(HeaderField.DATA_TYPE, DataType.class);
      switch (dtype) {
	case UINT8:
	  return new UInt8Dataset(header, raw, quiet);
	case UINT16:
	  return new UInt16Dataset(header, raw, quiet);
	case INT16:
	  return new Int16Dataset(header, raw, quiet);
	case INT32:
	  return new Int32Dataset(header, raw, quiet);
	case INT64:
	  return new Int64Dataset(header, raw, quiet);
	case FLOAT32:
	  return new Float32Dataset(header, raw, quiet);
	case FLOAT64:
	  return new Float64Dataset(header, raw, quiet);
	default:
	  throw new IllegalStateException("Unsupported data type: " + dtype);
      }
    }
    catch (Exception e) {
      if (!quiet) {
	System.err.println("Failed to load the ENVI data from: " + input);
	e.printStackTrace();
      }
      return null;
    }
  }
}
