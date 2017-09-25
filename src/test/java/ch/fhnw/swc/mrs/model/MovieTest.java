/**
 * 
 */
package ch.fhnw.swc.mrs.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

/**
 * @author christoph.denzler
 * 
 */
public class MovieTest {
  private LocalDate today;

  /**
   * Expected exception messages.
   */
  private static final String PC_MSG = "price category must not be null";
  private static final String TITLE_MSG = "Title must not be null nor emtpy";
  private static final String RD_MSG = "Release date must not be null";

  @Before
  public void setup() {
    today = LocalDate.now();
  }

  /**
   * Hashcode and equals are always overridden and tested together. Test method for
   * {@link ch.fhnw.edu.rental.model.Movie#hashCode()}.
   * 
   * @throws InterruptedException should not be thrown
   */
  @Test
  public void testHashCode() throws InterruptedException {
    PriceCategory pc = RegularPriceCategory.getInstance();
    Movie x = new Movie();
    Movie y = new Movie("A", today, pc, 0);
    Movie z = new Movie("A", today, pc, 0);

    // do we get consistently the same result?
    int h = x.hashCode();
    assertEquals(h, x.hashCode());
    h = y.hashCode();
    assertEquals(h, y.hashCode());

    // do we get the same result from two equal objects?
    h = y.hashCode();
    assertEquals(h, z.hashCode());

    // still the same hashcode after changing rented state?
    z.setRented(true);
    assertEquals(h, z.hashCode());

    z = new Movie("A", today, pc, 0); // get a new Movie
    z.setPriceCategory(ChildrenPriceCategory.getInstance());
    assertEquals(h, z.hashCode());

    z.setId(42);
    assertFalse(h == z.hashCode());
  }

  /**
   * Test method for {@link ch.fhnw.edu.rental.model.Movie#Movie()}.
   */
  @Test
  public void testMovie() {
    Movie m = new Movie();
    assertNotNull(m.getPriceCategory());
    assertNotNull(m.getReleaseDate());
    assertNotNull(m.getTitle());
    assertFalse(m.isRented());
    assertEquals(0, m.getAgeRating());
  }

  /**
   * Test method for
   * {@link ch.fhnw.edu.rental.model.Movie#Movie(java.lang.String, ch.fhnw.edu.rental.model.PriceCategory)}
   * .
   * 
   * @throws InterruptedException must not be thrown
   */
  @Test
  public void testMovieStringPriceCategory() throws InterruptedException {
    Movie m = new Movie("A", today, RegularPriceCategory.getInstance(), 0);
    assertionsForMovieCtorTests(m, today);
  }

  private void assertionsForMovieCtorTests(Movie m, LocalDate rd) {
    assertEquals("A", m.getTitle());
    assertEquals(RegularPriceCategory.class, m.getPriceCategory().getClass());
    assertEquals(rd, m.getReleaseDate());
    assertFalse(m.isRented());
    assertEquals(0, m.getAgeRating());
  }

  /**
   * Test method for {@link ch.fhnw.edu.rental.model.Movie#Movie(java.lang.String, java.util.Date,
   * ch.fhnw.edu.rental.model.PriceCategory, int ageRating)} .
   */
  @Test
  public void testMovieStringDatePriceCategory() throws InterruptedException {
    LocalDate anotherDay = LocalDate.of(1969, 7, 19);
    Movie m = new Movie("A", anotherDay, RegularPriceCategory.getInstance(), 0);
    assertionsForMovieCtorTests(m, anotherDay);
  }

  /**
   * Test method for {@link ch.fhnw.edu.rental.model.Movie#Movie(java.lang.String,
   * ch.fhnw.edu.rental.model.PriceCategory, int ageRating)} .
   */
  @Test
  public void testExceptionOnMissingTitle() {
    try {
      new Movie(null, today, RegularPriceCategory.getInstance(), 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(TITLE_MSG, e.getMessage());
    }
  }
  
  @Test
  public void testExceptionOnMissingPriceCategory() {
    try {
      new Movie("A", today, null, 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(PC_MSG, e.getMessage());
    }
  }

  @Test
  public void testExceptionOnMissingReleaseDate() {
    try {
      new Movie("A", null, RegularPriceCategory.getInstance(), 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(RD_MSG, e.getMessage());
    }
  }

  /**
   * Test method for {@link ch.fhnw.edu.rental.model.Movie#Movie(java.lang.String, java.util.Date,
   * ch.fhnw.edu.rental.model.PriceCategory, int ageRating)} .
   */
  @Test
  public void testExceptionMovieStringDatePriceCategory() {
    try {
      new Movie(null, today, RegularPriceCategory.getInstance(), 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(TITLE_MSG, e.getMessage());
    }
    try {
      new Movie("A", null, RegularPriceCategory.getInstance(), 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(RD_MSG, e.getMessage());
    }
    try {
      new Movie("A", today, null, 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(PC_MSG, e.getMessage());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAgeRatingInCtorLow() {
    new Movie("A", today, RegularPriceCategory.getInstance(), -5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAgeRatingInCtorHigh() {
    new Movie("A", today, RegularPriceCategory.getInstance(), 25);
  }

  /**
   * Test equals on identity.
   */
  @Test
  public void testEqualsIdentity() {
    Movie m = new Movie();
    // 1. test on identity
    assertTrue(m.equals(m));
  }

  /**
   * Test equals with null.
   */
  @Test
  public void testEqualsNull() {
    Movie m = new Movie();
    // 1. test on identity
    assertFalse(m.equals(null));
  }

  /**
   * Test equals with non-Movie object.
   */
  @Test
  public void testEqualsNonMovie() {
    Movie m = new Movie();
    assertFalse(m.equals("Hallo"));
  }

  /**
   * Test equals with different id.
   */
  @Test
  public void testEqualsId() {
    Movie m1 = new Movie();
    Movie m2 = new Movie();
    m1.setId(1);
    m2.setId(2);
    assertFalse(m1.equals(m2));
    assertFalse(m2.equals(m1));
    m2.setId(1);
    assertTrue(m1.equals(m2));
    assertTrue(m2.equals(m1));
  }

  /**
   * Test equals with different id.
   */
  @Test
  public void testEqualsReleaseDate() {
    Movie m1 = new Movie();
    Movie m2 = new Movie();
    m1.setId(1);
    m2.setId(1);
    m2.setReleaseDate(today.minusDays(1));
    assertFalse(m1.equals(m2));
    assertFalse(m2.equals(m1));
    m1.setReleaseDate(today.minusDays(1));
    assertTrue(m1.equals(m2));
    assertTrue(m2.equals(m1));
  }

  /**
   * Test equals with different id.
   */
  @Test
  public void testEqualsTitle() {
    Movie m1 = new Movie();
    Movie m2 = new Movie();
    m1.setId(1);
    m2.setId(1);
    m2.setReleaseDate(today);
    m1.setReleaseDate(today);
    m2.setTitle("Titanic");
    assertFalse(m1.equals(m2));
    assertFalse(m2.equals(m1));
    m1.setTitle("Titanic");
    assertTrue(m1.equals(m2));
    assertTrue(m2.equals(m1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetTitle() {
    Movie m = new Movie();
    m.setTitle("Hallo");
    assertEquals("Hallo", m.getTitle());
    m.setTitle(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetReleaseDate() {
    Movie m = new Movie();
    m.setReleaseDate(today);
    assertEquals(today, m.getReleaseDate());
    m.setReleaseDate(null);
  }
}
