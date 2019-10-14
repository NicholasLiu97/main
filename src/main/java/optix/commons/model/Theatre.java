package optix.commons.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Theatre {
    //@SuppressWarnings("checkstyle:membername")
    private final String SPACES = "  "; // CHECKSTYLE IGNORE THIS LINE
    private final String STAGE = "                |STAGE|           \n"; // CHECKSTYLE IGNORE THIS LINE

    private Seat[][] seats = new Seat[6][10];
    private int tierOneSeats;
    private int tierTwoSeats;
    private int tierThreeSeats;
    private int tierFourSeats;
    private int tierFiveSeats;
    private int tierSixSeats;
    private double seatBasePrice;

    private double cost;
    private double revenue;

    private String showName;

    /**
     * instantiates Theatre Object. Used when loading save file data.
     *
     * @param showName      name of show
     * @param cost          cost of show
     * @param revenue       expected revenue, calculated from seat purchases - cost
     * @param seatBasePrice base price of seats
     */
    public Theatre(String showName, double cost, double revenue, double seatBasePrice) {
        this.showName = showName;
        this.cost = cost;
        this.revenue = revenue;
        this.seatBasePrice = seatBasePrice;
        initializeLayout();
    }

    /**
     * Instantiates Theatre Object. Used when there is no revenue yet (fresh instance).
     *
     * @param showName      name of show
     * @param cost          cost of show
     * @param seatBasePrice base price of seats.
     */
    public Theatre(String showName, double cost, double seatBasePrice) {
        this.showName = showName;
        this.cost = cost;
        this.revenue = 0;
        this.seatBasePrice = seatBasePrice;
        initializeLayout();
    }

    // can have multiple layouts to be added for future extensions.

    private void initializeLayout() {
        tierOneSeats = 0;
        tierTwoSeats = 0;
        tierThreeSeats = 0;
        tierFourSeats = 0;
        tierFiveSeats = 0;
        tierSixSeats = 0;
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                switch (i) {
                case 0:
                case 1:
                    seats[i][j] = new Seat("3");
                    tierOneSeats++;
                    break;
                case 2:
                case 3:
                    seats[i][j] = new Seat("2");
                    tierTwoSeats++;
                    break;
                case 4:
                case 5:
                    seats[i][j] = new Seat("1");
                    tierThreeSeats++;
                    break;
                default:
                    assert i > seats.length;
                    break;
                }
            }
        }
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return showName;
    }

    public Seat[][] getSeats() {
        return seats;
    }

    /**
     * function to set the status of a seat (change it to booked when a seat is bought).
     *
     * @param buyerName Name of buyer
     * @param row       desired seat row
     * @param col       desired seat column
     */
    public void setSeat(String buyerName, int row, int col) {
        //ToDo find out a way to get number of seats in each tier
        seats[row][col].setBooked(true);
        seats[row][col].setName(buyerName);
        switch (seats[row][col].getSeatTier()) {
        case "1":
            tierOneSeats--;
            break;
        case "2":
            tierTwoSeats--;
            break;
        case "3":
            tierThreeSeats--;
            break;
        case "4":
            tierFourSeats--;
            break;
        case "5":
            tierFiveSeats--;
            break;
        case "6":
            tierSixSeats--;
            break;
        default:
            System.out.println("Should have a Seat Tier!");
        }
    }

    public void setTiers(TreeMap<String, Integer> seatTiers, int numberOfTiers) {
        for (Map.Entry<String, Integer> entry : seatTiers.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            int row = 0;

            switch (key) {
            case "A":
                row = 0;
            case "B":
                row = 1;
            case "C":
                row = 2;
            case "D":
                row = 3;
            case "E":
                row = 4;
            case "F":
                row = 5;
            }

            for (int j = 0; j < seats[row].length; j++) {
                seats[row][j].setTier(value, numberOfTiers);
            }
        }
    }

                        /**
     * Get the seating arrangement of the Theatre.
     *
     * @return seating arrangement as a String.
     */
    public String getSeatingArrangement() {
        StringBuilder seatingArrangement = new StringBuilder(STAGE);

        for (int i = 0; i < seats.length; i++) {
            seatingArrangement.append(SPACES);
            for (int j = 0; j < seats[i].length; j++) {
                seatingArrangement.append(seats[i][j].getSeat());
            }
            seatingArrangement.append("\n");
        }
        seatingArrangement.append(getSeatsLeft());

        return seatingArrangement.toString();
    }

    private String getSeatsLeft() {
        return "\nTier 1 Seats: " + tierOneSeats + "\n"
                + "Tier 2 Seats: " + tierTwoSeats + "\n"
                + "Tier 3 Seats: " + tierThreeSeats + "\n";
    }

    public String writeToFile() {
        return String.format("%s | %f | %f | %f\n", showName, cost, revenue, seatBasePrice);
    }

    public boolean hasSameName(String checkName) {
        return showName.toLowerCase().equals(checkName.toLowerCase());
    }


    /**
     * Sell seats to customers.
     *
     * @param buyerName name of buyer
     * @param seat      desired seat
     * @return cost of seat.
     */
    public double sellSeats(String buyerName, String seat) {
        int row = getRow(seat.substring(0, 1));
        int col = getCol(seat.substring(1));

        double costOfSeat = 0;

        //This needs to be changed in the event that the theatre dont have fixed seats for each row
        if (row == -1 || col == -1) {
            return costOfSeat;
        }

        if (!seats[row][col].isBooked()) {
            Seat soldSeat = seats[row][col];
            soldSeat.setBooked(true);
            soldSeat.setName(buyerName);
            costOfSeat = soldSeat.getSeatPrice(seatBasePrice);
            revenue += costOfSeat;

            switch (soldSeat.getSeatTier()) {
            case "1":
                tierOneSeats--;
                break;
            case "2":
                tierTwoSeats--;
                break;
            case "3":
                tierThreeSeats--;
                break;
            default:
            }
            seats[row][col] = soldSeat;

        }

        return costOfSeat;
    }

    /**
     * Sell seats to customers. Used when customer wants to buy multiple seats.
     *
     * @param buyerName name of buyer
     * @param seats     String array of desired seats
     * @return Message detailing status of desired seats (sold out or successfully purchased.)
     */
    public String sellSeats(String buyerName, String... seats) {
        double totalCost = 0;
        ArrayList<String> seatsSold = new ArrayList<>();
        ArrayList<String> seatsNotSold = new ArrayList<>();
        String message;
        for (String seatNumber : seats) {
            double costOfSeat = sellSeats(buyerName, seatNumber);

            if (costOfSeat != 0) {
                totalCost += costOfSeat;
                seatsSold.add(seatNumber);
            } else {
                seatsNotSold.add(seatNumber);
            }
        }

        if (seatsSold.isEmpty()) {
            message = String.format("☹ OOPS!!! All of the seats %s are unavailable\n", seatsNotSold);
        } else if (seatsNotSold.isEmpty()) {
            message = "You have successfully purchased the following seats: \n"
                    + seatsSold + "\n"
                    + "The total cost of the ticket is " + new DecimalFormat("$#.00").format(totalCost) + "\n";
        } else {
            message = "You have successfully purchased the following seats: \n"
                    + seatsSold + "\n"
                    + "The total cost of the ticket is " + new DecimalFormat("$#.00").format(totalCost) + "\n"
                    + "The following seats are unavailable: \n"
                    + seatsNotSold + "\n";
        }

        return message;
    }

    private int getRow(String row) {
        switch (row) {
        case "A":
            return 0;
        case "B":
            return 1;
        case "C":
            return 2;
        case "D":
            return 3;
        case "E":
            return 4;
        case "F":
            return 5;
        default:
            return -1;
        }
    }

    private int getCol(String col) {
        switch (col) {
        case "1":
            return 0;
        case "2":
            return 1;
        case "3":
            return 2;
        case "4":
            return 3;
        case "5":
            return 4;
        case "6":
            return 5;
        case "7":
            return 6;
        case "8":
            return 7;
        case "9":
            return 8;
        case "10":
            return 9;
        default:
            return -1;
        }
    }

    public double getProfit() {
        return this.revenue - this.cost;
    }

    public int getTierOneSeats() {
        return tierOneSeats;
    }

    public int getTierTwoSeats() {
        return tierTwoSeats;
    }

    public int getTierThreeSeats() {
        return tierThreeSeats;
    }

    private int decreaseSeats(int numSeats) {
        return numSeats--;
    }
}
