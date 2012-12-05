package org.primeframework.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easymock.Capture;
import org.primeframework.persistence.service.jpa.PersistenceService;
import org.testng.annotations.Test;

import static org.easymock.EasyMock.*;
import static org.testng.Assert.*;

/**
 * @author James Humphrey
 */
@Test(groups = "unit")
public class AbstractSearchServiceTest {

  @Test
  public void test() {

    String searchCriteria = "bar";

    Capture<String> countQuery = new Capture<String>();
    Capture<Map<String, Object>> countQueryParams = new Capture<Map<String, Object>>();
    Capture<Class<MockBean>> type = new Capture<Class<MockBean>>();
    Capture<String> query = new Capture<String>();
    Capture<Integer> offset = new Capture<Integer>();
    Capture<Integer> resultsPerPage = new Capture<Integer>();
    Capture<Map<String, Object>> params = new Capture<Map<String, Object>>();


    List<MockBean> beans = new ArrayList<MockBean>();
    beans.add(new MockBean(searchCriteria));


    PersistenceService ps = createStrictMock(PersistenceService.class);
    expect(ps.queryCountWithNamedParameters(capture(countQuery), capture(countQueryParams))).andReturn(10L);
    expect(ps.queryWithNamedParameters(capture(type), capture(query), captureInt(offset), captureInt(resultsPerPage), capture(params))).andReturn(beans);
    replay(ps);

    MockSearchService service = new MockSearchService(ps);
    MockSearchCriteria c = new MockSearchCriteria();
    c.foo = searchCriteria;
    SearchResults<MockBean> results = service.search(c);

    assertEquals(results.totalResults, 10L);
    assertEquals(results.results.size(), 1);
    assertEquals(results.results.get(0).foo, searchCriteria);

    verify(ps);
  }

  /**
   * Mock out a bean
   */
  public static class MockBean {
    public String foo;

    public MockBean(String foo) {
      this.foo = foo;
    }
  }

  /**
   * Mock out the criteria
   */
  public static class MockSearchCriteria extends SearchCriteria {
    String foo;
  }

  /**
   * Mock out the search service so that we can test the AbstractSearchService
   */
  public static class MockSearchService extends AbstractSearchService<MockBean, MockSearchCriteria> {

    public static final String query = "select * from mock_beans";
    public static final String countQuery = "select count(*) from mock_beans";

    protected MockSearchService(PersistenceService ps) {
      super(ps);
    }

    @Override
    public SearchResults<MockBean> search(MockSearchCriteria searchCriteria) {

      QueryBuilder builder = new QueryBuilder(query, countQuery);
      builder.andWhere("foo = :foo").withParameter("fao", searchCriteria.foo);

      return super.search(MockBean.class, searchCriteria, builder);
    }
  }
}