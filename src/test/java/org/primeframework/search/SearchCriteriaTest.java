package org.primeframework.search;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author James Humphrey
 */
@Test(groups = "unit")
public class SearchCriteriaTest {

  @Test
  public void calculateOffset() {
    SearchCriteria searchCriteria = new SearchCriteria() {

    };

    // page 1 w/100
    searchCriteria.resultsPerPage = 100;
    searchCriteria.page = 1;
    assertEquals(searchCriteria.calculateOffset(), 0);
    // page 2 w/100
    searchCriteria.page = 2;
    assertEquals(searchCriteria.calculateOffset(), 100);

    // page 1 with 30
    searchCriteria.resultsPerPage = 30;
    searchCriteria.page = 1;
    assertEquals(searchCriteria.calculateOffset(), 0);
    // page 2 w/30
    searchCriteria.page = 2;
    assertEquals(searchCriteria.calculateOffset(), 30);
  }
}