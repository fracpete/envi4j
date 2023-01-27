/*
 * IntDataset.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.dataset;

/**
 * Interface for datasets that return int as type.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public interface IntDataset
  extends Dataset {

  /**
   * Returns the band as matrix.
   *
   * @param band	the band to retrieve
   * @return		the matrix
   */
  public int[][] getBand(int band);
}
