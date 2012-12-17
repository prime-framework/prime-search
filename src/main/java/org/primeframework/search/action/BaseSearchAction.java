/*
 * Copyright (c) 2009, Inversoft, All Rights Reserved.
 */
package org.primeframework.search.action;

import java.util.List;

import org.primeframework.mvc.parameter.annotation.PreParameter;
import org.primeframework.mvc.parameter.annotation.PreParameterMethod;
import org.primeframework.mvc.scope.annotation.ActionSession;
import org.primeframework.search.SearchCriteria;
import org.primeframework.search.SearchResults;
import org.primeframework.search.SearchService;

/**
 * This is a generic base action for performing searches. In order to implement a CRUD search and index page, simply
 * extend this class and provide an implementation of the {@link #getDefaultCriteria()} method.
 * <p/>
 * An implementation might look like this:
 * <p/>
 * <p/>
 * <pre>
 *
 * @author Brian Pontarelli
 * @@Action public class Index extends BaseSearchAction&lt;User, UserSearchCriteria&lt;User>> { protected
 * UserSearchCriteria&lt;User> getDefaultCriteria() { return new UserSearchCriteria&lt;User>(User.class); } } </pre>
 * <p/>
 * The bulk of the search work happens in the FTL file and the <code>UserSearchCriteria</code> class.
 */
public abstract class BaseSearchAction<T, U extends SearchCriteria> {
  protected final SearchService<T, U> searchService;

  /**
   * The results of the search or an empty list.
   */
  public List<T> results;

  /**
   * The search criteria, which is either the default or the current criteria that is stored in the session.
   */
  @ActionSession
  public U s;

  /**
   * The total number of results.
   */
  public long totalCount;

  /**
   * The current clear flag state.
   */
  @PreParameter
  public boolean clear = false;

  /**
   * Determines if the entity can be added via the CRUD interface.
   */
  public boolean addable = true;

  /**
   * Determines if the entity can be edited via the CRUD interface.
   */
  public boolean editable = true;

  /**
   * Determines if the entity details can be viewed via the CRUD interface.
   */
  public boolean detailable = true;

  /**
   * Determines if the entity can be deleted via the CRUD interface.
   */
  public boolean deletable = true;

  /**
   * The maximum number of page links to display.
   */
  public int maximumPageLinks = 6;

  /**
   * Total number of pages to for the search.
   */
  public int numberOfPages;

  /**
   * The start page.
   */
  public int startPage;

  /**
   * The end page.
   */
  public int endPage;

  /**
   * The current page.
   */
  public int currentPage;

  /**
   * The next page.
   */
  public int nextPage;

  /**
   * The previous page.
   */
  public int previousPage;

  /**
   * The number of the first result on this page within the total results.
   */
  public int firstResult;

  /**
   * The number of the last result on this page within the total results.
   */
  public int lastResult;

  protected BaseSearchAction(SearchService<T, U> searchService) {
    this.searchService = searchService;
  }

  /**
   * This method prepares the SearchCriteria instance if it is null by calling the {@link #getDefaultCriteria()}
   * method.
   */
  @PreParameterMethod
  public void prepare() {
    if (s == null || clear) {
      s = getDefaultCriteria();
    }
  }

  /**
   * Performs the search.
   *
   * @return Always success.
   */
  public String execute() {
    SearchResults<T> results = searchService.search(s);
    this.results = results.results;
    this.totalCount = results.totalResults;
    calculatePages();
    calculateResultNumbers();
    return "success";
  }

  /**
   * Calculates the page numbers for the pagination.
   */
  protected void calculatePages() {
    numberOfPages = (int) totalCount / s.resultsPerPage;
    int extra = (int) totalCount % s.resultsPerPage;
    if (extra != 0) {
      numberOfPages++;
    }

    if (numberOfPages <= 0) {
      numberOfPages = 1;
    }

    currentPage = s.page;
    int factor = maximumPageLinks / 2;
    startPage = (currentPage - factor < 1) ? 1 : currentPage - factor;
    endPage = (currentPage + factor > numberOfPages) ? numberOfPages : currentPage + factor;

    if (startPage > numberOfPages) {
      startPage = numberOfPages;
    }

    if (endPage < 1) {
      endPage = 1;
    }

    nextPage = (currentPage >= numberOfPages) ? numberOfPages : currentPage + 1;
    previousPage = (currentPage <= 1) ? 1 : currentPage - 1;
  }

  /**
   * Calculates the result numbers of the current page within all the results.
   */
  protected void calculateResultNumbers() {
    if (results.size() > 0) {
      int numberPerPage = s.resultsPerPage;
      firstResult = (currentPage - 1) * numberPerPage + 1;
      lastResult = firstResult + results.size() - 1;
    } else {
      firstResult = 0;
      lastResult = 0;
    }
  }

  /**
   * @return A new default search criteria. Must be implemented by sub-classes.
   */
  protected abstract U getDefaultCriteria();
}
