package org.primeframework.search;

import java.io.Serializable;

/**
 * This class is the base criteria for searching.
 *
 * @author James Humphrey
 */
public abstract class SearchCriteria implements Serializable {
  private final static long serialVersionUID = 1;

  public int resultsPerPage = 20;
  public int page = 1;
  public Sort sort = Sort.asc;
  public String orderBy;

  /**
   * Calculates the offset used in sql queries
   *
   * @return the offset
   */
  public int calculateOffset() {
    return (page - 1) * resultsPerPage;
  }

  /**
   * Enum for sorting results
   */
  public static enum Sort {
    asc,
    desc
  }
}