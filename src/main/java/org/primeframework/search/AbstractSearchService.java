package org.primeframework.search;

import org.apache.commons.lang3.StringUtils;
import org.primeframework.persistence.service.jpa.PersistenceService;
import org.primeframework.search.QueryBuilder.JPAQuery;

/**
 * This class is an abstract base class for services that provide search capabilities.
 *
 * @author James Humphrey
 */
public abstract class AbstractSearchService<T,SC extends SearchCriteria> implements SearchService<T, SC>{
  protected final PersistenceService ps;

  protected AbstractSearchService(PersistenceService ps) {
    this.ps = ps;
  }

  /**
   * Executes the search for the given type using the search criteria and the query builder. This executes both the
   * count query and the main query.
   *
   * @param type           The type of objects being searched for.
   * @param searchCriteria The search criteria.
   * @param builder        The query builder.
   * @return The search results.
   */
  protected SearchResults<T> search(Class<T> type, SC searchCriteria, QueryBuilder builder) {
    if (!StringUtils.isBlank(searchCriteria.orderBy)) {
      builder.orderBy(searchCriteria.orderBy + " " + searchCriteria.sort.name());
    }

    JPAQuery countQuery = builder.toJPACountQuery();
    JPAQuery query = builder.toJPAQuery();
    return new SearchResults<T>(
      ps.queryCountWithNamedParameters(countQuery.query, countQuery.params),
      ps.queryWithNamedParameters(type, query.query, searchCriteria.calculateOffset(),
        searchCriteria.resultsPerPage, query.params));
  }
}
