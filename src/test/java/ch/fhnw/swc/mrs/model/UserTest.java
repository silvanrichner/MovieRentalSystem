/**
 * 
 */
package ch.fhnw.swc.mrs.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class UserTest {
  private static final String NAME = "name";
  private static final String FIRSTNAME = "first name";
  private static final String EMPTYSTRING = "";

  @Test
  public void testUser() {
    User u = new User(NAME, FIRSTNAME, LocalDate.now());
    assertNotNull("u should not be null", u);

    // check if name and first name were stored correctly
    String n = u.getName();
    String f = u.getFirstName();
    assertEquals(NAME, n);
    assertEquals(FIRSTNAME, f);

    // check if there exists a rental list
    List<Rental> rentals = u.getRentals();
    assertNotNull("rentals list should be empty, not null", rentals);
    assertEquals(0, rentals.size());
    LocalDate bd = u.getBirthdate();
    assertNotNull(bd);
    assertFalse(LocalDate.now().isBefore(bd));
  }

  /**
   * Test if correct exceptions are thrown in constructor.
   */
  @Test
  public void testUserExceptions() {
    User u = null;
    try {
      u = new User(null, FIRSTNAME, null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("non-existing name", e.getMessage());
    }
    try {
      u = new User(NAME, null, null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("non-existing name", e.getMessage());
    }
    try {
      u = new User(EMPTYSTRING, FIRSTNAME, null);
      fail();
    } catch (MovieRentalException e) {
      assertEquals("invalid name value", e.getMessage());
    }
    try {
      u = new User(NAME, EMPTYSTRING, null);
      fail();
    } catch (MovieRentalException e) {
      assertEquals("invalid name value", e.getMessage());
    }
    assertNull("u must not be assigned", u);
  }

  @Test(expected = MovieRentalException.class)
  public void testSetterGetterId() {
    User u = new User(NAME, FIRSTNAME, LocalDate.now());
    u.setId(42);
    assertEquals(42, u.getId());
    u.setId(0);
  }

  /**
   * Test method for {@link ch.fhnw.edu.rental.model.User#getRentals()} . Test method for
   * {@link ch.fhnw.edu.rental.model.User#setRentals()}.
   */
  @Test
  public void testSetterGetterRentals() {
    List<Rental> list = new LinkedList<Rental>();
    User u = new User(NAME, FIRSTNAME, LocalDate.now());
    u.setRentals(list);
    assertEquals(list, u.getRentals());
    u.setRentals(null);
    assertNull("rental list must be null", u.getRentals());
  }

  /**
   * Test method for {@link ch.fhnw.edu.rental.model.User#getName()}. Test method for
   * {@link ch.fhnw.edu.rental.model.User#setName()}.
   */
  @Test
  public void testSetterGetterName() {
    User u = new User(NAME, FIRSTNAME, LocalDate.now());
    try {
      u.setName(null);
      fail("Missing NullPointerException");
    } catch (IllegalArgumentException npe) {
      assertEquals("non-existing name", npe.getMessage());
    }
    try {
      u.setName("");
      fail("Missing MovieRentalException");
    } catch (MovieRentalException npe) {
      assertEquals("invalid name value", npe.getMessage());
    }
    try {
      u.setName("This is a very long name which is over fourty characters long and thus should result in an exception");
      fail("Missing MovieRentalException");
    } catch (MovieRentalException npe) {
      assertEquals("invalid name value", npe.getMessage());
    }
    u.setName("Bla");
    assertEquals("Bla", u.getName());
  }

  /**
   * Test method for {@link ch.fhnw.edu.rental.model.User#setName(java.lang.String)}. Test method
   * for {@link ch.fhnw.edu.rental.model.User#getFirstName()}.
   */
  @Test
  public void testSetterGetterFirstName() {
    User u = new User(NAME, FIRSTNAME, LocalDate.now());
    try {
      u.setFirstName(null);
      fail("Missing NullPointerException");
    } catch (IllegalArgumentException npe) {
      assertEquals("non-existing name", npe.getMessage());
    }
    try {
      u.setFirstName("");
      fail("Missing MovieRentalException");
    } catch (MovieRentalException npe) {
      assertEquals("invalid name value", npe.getMessage());
    }
    try {
      u.setFirstName(
          "This is a very long name which is over fourty characters long and thus should result in an exception");
      fail("Missing MovieRentalException");
    } catch (MovieRentalException npe) {
      assertEquals("invalid name value", npe.getMessage());
    }
    u.setFirstName("Bla");
    assertEquals("Bla", u.getFirstName());
  }

  /**
   * Test method for {@link ch.fhnw.edu.rental.model.User#getCharge()}.
   */
  @Test
  public void testGetCharge() {
    LocalDate today = LocalDate.now();
    double delta = 1e-6;
    User u = new User(NAME, FIRSTNAME, today);
    // a newly created user has no rentals and no charge.
    double charge = u.getCharge();
    assertEquals(0.0d, charge, delta);

    PriceCategory regular = RegularPriceCategory.getInstance();

    // first check regular movie
    Movie mov = new Movie("A", today, regular, 0);
    Rental r = new Rental(u, mov);
    charge = r.getRentalFee();
    assertEquals(charge, u.getCharge(), delta);

    // now add another two regular movies
    mov = new Movie("B", today, regular, 0);
    r = new Rental(u, mov);
    charge += r.getRentalFee();
    mov = new Movie("C", today, regular, 0);
    r = new Rental(u, mov);
    charge += r.getRentalFee();
    assertEquals(charge, u.getCharge(), delta);
  }

  @Test
  public void testEquals() throws Exception {
    LocalDate today = LocalDate.now();
    User u1 = new User(NAME, FIRSTNAME, today);
    User u2 = new User(NAME, FIRSTNAME, today);
    assertTrue(u1.equals(u1));
    assertFalse(u1.equals(NAME));
    assertTrue(u1.equals(u2));
    assertTrue(u2.equals(u1));
    u1.setId(0);
    assertTrue(u1.equals(u2));
    assertTrue(u2.equals(u1));
    u2.setId(5);
    assertFalse(u1.equals(u2));
    assertFalse(u2.equals(u1));
    u1 = new User(NAME, FIRSTNAME, today);
    u1.setId(5);
    assertTrue(u1.equals(u2));
    u1.setName("Meier");
    assertFalse(u1.equals(u2));
    assertFalse(u2.equals(u1));
    u2.setName("Meier");
    assertTrue(u1.equals(u2));
    u1.setFirstName("Hans");
    assertFalse(u1.equals(u2));
    assertFalse(u2.equals(u1));
    u2.setFirstName("Hans");
    assertTrue(u1.equals(u2));
    u1.setBirthdate(today.minusDays(4));
    assertFalse(u1.equals(u2));
    assertFalse(u2.equals(u1));
  }

  @Test
  public void testHashCode() throws Exception {
    // dummy user objects
    User x = new User(NAME, FIRSTNAME, LocalDate.now());
    User y = new User(NAME, FIRSTNAME, LocalDate.now());

    assertEquals(x.hashCode(), y.hashCode());
    x.setId(42);
    assertTrue(x.hashCode() != y.hashCode());
    y.setId(42);
    assertEquals(x.hashCode(), y.hashCode());
  }

}
