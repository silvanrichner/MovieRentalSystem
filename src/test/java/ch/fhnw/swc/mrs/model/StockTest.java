package ch.fhnw.swc.mrs.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class StockTest {
    private Stock stock; // Class under test

    @Before
    public void setUp() throws Exception {
        stock = new Stock(); // create object of CUT
    }

    /**
     * MP06.c.i
     * If the same Movie object is added or removed twice (or x times) then the counter of copies (getInStock)
     * is adjusted by 2 (or x) as well
     */
    @Test
    public void testAddToStock() {
        Movie m = mock(Movie.class);
        stock.addToStock(m);
        stock.addToStock(m);
        assertEquals(2, stock.getInStock(null));
        
        // verify that getTitle gets called twice for each add
        verify(m, times(4)).getTitle();
    }

    @Test
    public void testRemoveFromStockCallsMovieGetTitleOnce() {
        Movie m = mock(Movie.class);
        stock.addToStock(m);
        stock.addToStock(m);
        stock.removeFromStock(m);
        reset(m); // reset mock to measure only remove behavior
        assertEquals(1, stock.getInStock(null));
        stock.removeFromStock(m);
        assertEquals(0, stock.getInStock(null));
        verify(m, times(1)).getTitle();
    }

    /**
     * MP06.c.ii
     * a MovieRentalException is thrown when attempting to call removeFromStock on a stock that does 
     * not contain a certain movie.
     */
    @Test(expected = MovieRentalException.class)
    public void testRemoveFromStockThrowsOnEmptyStock() {
        Movie m = mock(Movie.class);
        Movie notInStock = mock(Movie.class);
        when(m.getTitle()).thenReturn("Titanic");
        when(notInStock.getTitle()).thenReturn("Avatar");
        stock.addToStock(m);
        stock.removeFromStock(notInStock);
    }

    /**
     * MP06.c.iii, c.iv and c.v
     * c.iii: registered listeners are notified as soon as the current stock for a certain movie falls below 
     * a given thresholds.
     * c.iv: the same listener is notified again, when below the threshold further movies are removed from the stock.
     * c.v: no more listeners are notified (no more calls occur on their stockLow method) once they were removed 
     * calling removeStockListener
     */
    @Test
    public void testListenerOnRemoveFromStock() {
        Movie m = mock(Movie.class);
        when(m.getTitle()).thenReturn("Titanic");

        LowStockListener lsl = mock(LowStockListener.class);
        when(lsl.getThreshold()).thenReturn(2);

        stock.addLowStockListener(lsl);
        stock.addToStock(m);
        stock.addToStock(m);
        stock.addToStock(m);
        stock.addToStock(m);
        assertEquals(4, stock.getInStock("Titanic"));

        // MP06.c.iii
        // remove one from stock above threshold, listener should not be called
        stock.removeFromStock(m);
        verify(lsl, never()).stockLow(eq(m), anyInt());
        // test if listener is called when threshold is reached
        stock.removeFromStock(m);
        verify(lsl).stockLow(m, 2);

        // MP06.c.iv
        // test if listener still being called below threshold
        stock.removeFromStock(m);
        verify(lsl).stockLow(m, 1);

        // MP06.c.v
        // make sure listener is not called anymore after unsubscribe
        stock.removeLowStockListener(lsl);
        stock.removeFromStock(m);

        assertEquals(0, stock.getInStock("Titanic"));
        verify(lsl, times(3)).getThreshold();
        verify(lsl, times(2)).stockLow(eq(m), anyInt());
    }
}
