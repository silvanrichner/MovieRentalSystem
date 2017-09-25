package ch.fhnw.swc.mrs.model;

/**
 * Gets notified when a movie is low in stock.
 */
public interface LowStockListener {
    /**
     * @return get the threshold for "low in stock"-notification
     */
    int getThreshold();

    /**
     * This method is called when movie m drops below threshold.
     * 
     * @param m the movie dropped below threshold.
     * @param c the actual number of items still in stock.
     */
    void stockLow(Movie m, int c);
}
