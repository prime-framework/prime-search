package org.primeframework.search;

import java.io.Serializable;

/**
 * This class is the base criteria for searching.
 *
 * @author James Humphrey
 */
@SuppressWarnings(value = "unchecked")
public abstract class SearchCriteria<T> implements Serializable {
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

  /**
   * Transforms the given string into a search string by lower-casing it and then checking if it contains any stars. If
   * it does contain a star, they are replaced with percent signs. If it doesn't, the string is wrapped by a percent
   * sign on each side.
   *
   * @param str The string.
   * @return The search string.
   */
  public static String toSearchString(String str) {
    if (str.contains("*")) {
      return str.trim().toLowerCase().replace("*", "%");
    } else {
      return "%" + str.trim().toLowerCase() + "%";
    }
  }

  public static String toLower(String str) {
    return "lower(" + str.trim().toLowerCase() + ")";
  }
}