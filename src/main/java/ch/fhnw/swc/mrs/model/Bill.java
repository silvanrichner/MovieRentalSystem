package ch.fhnw.swc.mrs.model;

import java.util.List;

public class Bill extends Statement {

    /**
     * Creates a bill object for a person (with the given name parameter) and a list of rentals.
     * @param name the family name.
     * @param firstName the first name.
     * @param rentals a list of rentals to be billed.
     */
    public Bill(String name, String firstName, List<Rental> rentals) {
        super(name, firstName, rentals);
    }
    
    @Override
    public String print() {
        StringBuffer sb = new StringBuffer();
        sb.append("Statement\n");
        sb.append("=========\n");
        sb.append(String.format("for: %s %s\n\n", getFirstName(), getLastName()));
        sb.append("Days   Price  Title\n");
        sb.append("-------------------\n");
        for (Rental r: getRentals()) {
            long days = r.getRentalDays();
            double price = r.getRentalFee();
            String title = r.getMovie().getTitle();
            sb.append(String.format("%4d  %6.2f  %s\n", days, price, title));
        }
        return sb.toString();
    }

}
