package ch.fhnw.swc.mrs.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ch.fhnw.swc.mrs.model.PriceCategory;

public class PriceCategoryTest {

  private PriceCategory pc;

  private class TestCategory extends PriceCategory {

    @Override
    public double getCharge(long daysRented) {
      return 0L;
    }

  }

  @Before
  public void setUp() throws Exception {
    pc = new TestCategory();
  }

  @Test
  public void testGetFrequentRenterPoints() {
    assertEquals(0, pc.getFrequentRenterPoints(-6));
    assertEquals(0, pc.getFrequentRenterPoints(0));
    assertEquals(1, pc.getFrequentRenterPoints(1));
    assertEquals(1, pc.getFrequentRenterPoints(2));
    assertEquals(1, pc.getFrequentRenterPoints(4000));
  }

}
