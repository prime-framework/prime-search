package org.primeframework.search;

import java.util.List;

/**
 * This class holds the search results and total count.
 *
 * @author James Humphrey
 */
public class SearchResults<T> {
  public long totalResults;
  public List<T> results;

  public SearchResults(long totalResults, List<T> results) {
    this.totalResults = totalResults;
    this.results = results;
  }
}
