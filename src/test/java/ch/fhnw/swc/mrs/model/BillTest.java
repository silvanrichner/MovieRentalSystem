package ch.fhnw.swc.mrs.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BillTest {

    @Test
    public void testPrint() {
        Bill b = new Bill("Muster", "Hans", createRentalList());
        String s = b.print();
        String[] lines = s.split("\n");
        assertEquals(9, lines.length);
        assertEquals("Statement", lines[0]);
        assertEquals("=========", lines[1]);
        assertEquals("for: Hans Muster", lines[2]);
        assertEquals("", lines[3]);
        assertEquals("Days   Price  Title", lines[4]);
        assertEquals("-------------------", lines[5]);
        assertEquals("   1    8.40  Avatar", lines[6]);
        assertEquals("   2   17.20  Casablanca", lines[7]);
        assertEquals("   3   26.40  Tron", lines[8]);
    }

    private List<Rental> createRentalList() {
        List<Rental> rentals = new ArrayList<>(3);
       
        Movie m1 = mock(Movie.class);
        Movie m2 = mock(Movie.class);
        Movie m3 = mock(Movie.class);
        when(m1.getTitle()).thenReturn("Avatar");
        when(m2.getTitle()).thenReturn("Casablanca");
        when(m3.getTitle()).thenReturn("Tron");
        
        Rental r1 = mock(Rental.class);
        Rental r2 = mock(Rental.class);
        Rental r3 = mock(Rental.class);
        when(r1.getMovie()).thenReturn(m1);
        when(r2.getMovie()).thenReturn(m2);
        when(r3.getMovie()).thenReturn(m3);
        when(r1.getRentalDays()).thenReturn(1L);
        when(r2.getRentalDays()).thenReturn(2L);
        when(r3.getRentalDays()).thenReturn(3L);
        when(r1.getRentalFee()).thenReturn(8.4);
        when(r2.getRentalFee()).thenReturn(17.2);
        when(r3.getRentalFee()).thenReturn(26.4);
        
        rentals.add(r1);
        rentals.add(r2);
        rentals.add(r3);

        return rentals;
    }
    
}
