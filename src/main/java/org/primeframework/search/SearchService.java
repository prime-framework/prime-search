package org.primeframework.search;

/**
 * Marker interface for search services
 *
 * @author James Humphrey
 */
public interface SearchService<T, SC extends SearchCriteria> {

  /**
   * Returns search results meeting the search criteria specified
   *
   * @param searchCriteria the search criteria
   * @return search results
   */
  public SearchResults<T> search(SC searchCriteria);
}
