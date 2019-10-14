package optix.commons.model;

public class Seat {
    private double ticketPrice;
    private String buyerName;
    private String seatTier;
    private int numberOfTiers;
    private boolean isBooked;

    /**
     * the seat object.
     *
     * @param seatTier tier of the seat. Higher tier seat is less precious.
     */
    public Seat(String seatTier) {
        this.isBooked = false;
        this.buyerName = null;
        this.numberOfTiers = 3;
        this.seatTier = seatTier;
    }

    public void setName(String name) {
        this.buyerName = name;
    }


    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    //store this in case of refund.
    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setTier(int newTier, int numTiers) {
        this.seatTier = Integer.toString(newTier);
        this.numberOfTiers = numTiers;
    }

    public boolean isBooked() {
        return isBooked;
    }

    private String getStatusIcon() {
        return isBooked ? "✓" : "✘";
    }

    public String getSeat() {
        return "[" + getStatusIcon() + "]";
    }

    public String getName() {
        return buyerName;
    }

    public String getSeatTier() {
        return seatTier;
    }

    /**
     * Get the price of the seat according to its tier.
     * The seat tier cannot be out of bounds.
     *
     * @param basePrice base seat price of a show.
     * @return price seat according to its tier.
     */
    public double getSeatPrice(double basePrice) {
        assert (Integer.parseInt(seatTier) <= 3 && Integer.parseInt(seatTier) > 0);
        if (numberOfTiers == 3) {
            if (seatTier.equals("1")) {
                ticketPrice = basePrice * 1.5;
            }
            if (seatTier.equals("2")) {
                ticketPrice = basePrice * 1.2;
            }
            if (seatTier.equals("3")) {
                ticketPrice = basePrice;
            }
        } else if (numberOfTiers == 4) {
            if (seatTier.equals("1")) {
                ticketPrice = basePrice * 1.5;
            }
            if (seatTier.equals("2")) {
                ticketPrice = basePrice * 1.3;
            }
            if (seatTier.equals("3")) {
                ticketPrice = basePrice * 1.1;
            }
            if (seatTier.equals("4")) {
                ticketPrice = basePrice;
            }
        } else if (numberOfTiers == 5) {
            if (seatTier.equals("1")) {
                ticketPrice = basePrice * 1.8;
            }
            if (seatTier.equals("2")) {
                ticketPrice = basePrice * 1.6;
            }
            if (seatTier.equals("3")) {
                ticketPrice = basePrice * 1.4;
            }
            if (seatTier.equals("4")) {
                ticketPrice = basePrice * 1.2;
            }
            if (seatTier.equals("5")) {
                ticketPrice = basePrice;
            }
        } else {
            if (seatTier.equals("1")) {
                ticketPrice = basePrice * 2.0;
            }
            if (seatTier.equals("2")) {
                ticketPrice = basePrice * 1.8;
            }
            if (seatTier.equals("3")) {
                ticketPrice = basePrice * 1.6;
            }
            if (seatTier.equals("4")) {
                ticketPrice = basePrice * 1.4;
            }
            if (seatTier.equals("5")) {
                ticketPrice = basePrice * 1.2;
            }
            if (seatTier.equals("6")) {
                ticketPrice = basePrice;
            }
        }

        return ticketPrice;

    }
}
