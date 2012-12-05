package org.primeframework.search;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple builder class for building query strings.
 *
 * @author James Humphrey
 */
public class QueryBuilder {
  private final StringBuilder where = new StringBuilder();
  private final Map<String, Object> params = new HashMap<String, Object>();
  private String select;
  private String selectCount;
  private String orderBy;
  private String groupBy;

  public QueryBuilder(String select, String selectCount) {
    this.select = select;
    this.selectCount = selectCount;
  }

  /**
   * Sets the select of the query, such as <strong>select e from User e</strong>. An example usage is:
   * <p/>
   * <pre>
   * select("select e from User e")
   * </pre>
   *
   * @param select The select query.
   * @return This builder.
   */
  public QueryBuilder select(String select) {
    this.select = select;
    return this;
  }

  /**
   * Sets the select count of the query, such as <strong>select count(e.id) from User e</strong>. An example usage is:
   * <p/>
   * <pre>
   * selectCount("select count(e.id) from User e")
   * </pre>
   *
   * @param selectCount The select count query.
   * @return This builder.
   */
  public QueryBuilder selectCount(String selectCount) {
    this.selectCount = selectCount;
    return this;
  }

  /**
   * Adds a where clause to the query. The parameters in the where clause must be named parameters like this:
   * <strong>age &lt; :age</strong>. You can set named parameters into this builder using the
   * <strong>withParameter</strong> methods. This should not include the <strong>where</strong> keyword. An example
   * usage is like this:
   * <p/>
   * <pre>
   * where("e.age &lt; :age")
   * </pre>
   * <p/>
   * If there is already a where clause in the query, this <strong>ands</strong> the given where clause to the already
   * existing clause.
   * <p/>
   * You must always alias the Entity to <strong>e</strong>.
   *
   * @param where The clause.
   * @return This builder.
   */
  public QueryBuilder andWhere(String where) {
    if (this.where.length() > 0) {
      this.where.append(" and ");
    }

    this.where.append(where);
    return this;
  }

  /**
   * Adds a where clause to the query. The parameters in the where clause must be named parameters like this:
   * <strong>age &lt; :age</strong>. You can set named parameters into this builder using the
   * <strong>withParameter</strong> methods. This should not include the <strong>where</strong> keyword. An example
   * usage is like this:
   * <p/>
   * <pre>
   * andWhere("e.age &lt; :age")
   * </pre>
   * <p/>
   * If there is already a where clause in the query, this <strong>ors</strong> the given where clause to the already
   * existing clause.
   * <p/>
   * You must always alias the Entity to <strong>e</strong>.
   *
   * @param where The clause.
   * @return This builder.
   */
  public QueryBuilder orWhere(String where) {
    if (this.where.length() > 0) {
      this.where.append(" or ");
    }

    this.where.append(where);
    return this;
  }

  /**
   * Adds a named parameter to the builder.
   *
   * @param name  The name of the parameter.
   * @param param The parameter.
   * @return This builder.
   */
  public QueryBuilder withParameter(String name, Object param) {
    this.params.put(name, param);
    return this;
  }

  /**
   * Adds a order by clause to the query. This should not include the <strong>order by</strong> keyword, but MUST
   * include the alias for the objects (i.e. 'e'). An example usage is like this:
   * <p/>
   * <pre>
   * orderBy("e.age asc, e.id desc")
   * </pre>
   *
   * @param orderBy The order by clause.
   * @return This builder.
   */
  public QueryBuilder orderBy(String orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  /**
   * Adds a group-by clause to the query.
   *
   * @param groupBy The group-by clause.
   * @return this builder.
   */
  public QueryBuilder groupBy(String groupBy) {
    this.groupBy = groupBy;
    return this;
  }

  public JPAQuery toJPAQuery() {
    StringBuilder build = new StringBuilder();
    build.append(select);

    if (where.length() > 0) {
      build.append(" where ").append(where);
    }

    if (groupBy != null) {
      build.append(" group by ").append(groupBy);
    }

    if (orderBy != null) {
      build.append(" order by ").append(orderBy);
    }

    return new JPAQuery(build.toString(), params);
  }

  public JPAQuery toJPACountQuery() {
    StringBuilder build = new StringBuilder();
    build.append(selectCount);

    if (where.length() > 0) {
      build.append(" where ").append(where);
    }

    return new JPAQuery(build.toString(), params);
  }

  public static class JPAQuery {
    public final String query;
    public final Map<String, Object> params;

    public JPAQuery(String query, Map<String, Object> params) {
      this.query = query;
      this.params = params;
    }
  }
}
