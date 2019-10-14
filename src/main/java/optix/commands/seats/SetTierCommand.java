package optix.commands.seats;

import optix.commands.Command;
import optix.commons.Model;
import optix.commons.Storage;
import optix.commons.model.ShowMap;
import optix.exceptions.OptixInvalidDateException;
import optix.ui.Ui;
import optix.util.OptixDateFormatter;

import java.time.LocalDate;
import java.util.TreeMap;

public class SetTierCommand extends Command {
    private String showName;
    private String showDate;
    private String[] seatsEachTier;
    private int numberOfTiers;
    private TreeMap<String, Integer> seatTiers = new TreeMap<>();

    private OptixDateFormatter formatter = new OptixDateFormatter();

    private static final String MESSAGE_INVALID_DATE = "☹ OOPS!!! It is not possible to change the seat tiers of " +
            "past shows \n";

    private static final String MESSAGE_SHOW_NOT_FOUND = "☹ OOPS!!! There is no show on this date. \n";

    private static final String MESSAGE_DOES_NOT_MATCH = "☹ OOPS!!! Did you get the wrong date or wrong show. \n"
            + "Try again!\n";

    private static final String MESSAGE_THREE_TIERS = "Here are the current tiers for the seats: \n" +
            "    Tier 1: %1$s \n" + "    Tier 2: %2$s \n" + "    Tier 3: %3$s \n";

    private static final String MESSAGE_FOUR_TIERS = "Here are the current tiers for the seats: \n" +
            "    Tier 1: %1$s \n" + "    Tier 2: %2$s \n" + "    Tier 3: %3$s \n" + "    Tier 4: %4$s \n";

    private static final String MESSAGE_FIVE_TIERS = "Here are the current tiers for the seats: \n" +
            "    Tier 1: %1$s \n" + "    Tier 2: %2$s \n" + "    Tier 3: %3$s \n" + "    Tier 4: %4$s \n" +
            "    Tier 5: %5$s \n";

    private static final String MESSAGE_SIX_TIERS = "Here are the current tiers for the seats: \n" +
            "    Tier 1: %1$s \n" + "    Tier 2: %2$s \n" + "    Tier 3: %3$s \n" + "    Tier 4: %4$s \n" +
            "    Tier 5: %5$s \n" + "    Tier 6: %6$s \n";


    public SetTierCommand(String showName, String showDate, String tierSettings) {
        this.showName = showName;
        this.showDate = showDate;
        this.seatsEachTier = tierSettings.split("\\|");
        this.numberOfTiers = seatsEachTier.length;

        String[] seatsRow;
        for (int i = 0; i < this.numberOfTiers; i ++) {
            seatsRow= seatsEachTier[0].split(" ");
            for (int j = 0; j < seatsRow.length; j++) {
                this.seatTiers.put(seatsRow[0], j+1);
            }
        }
    }

    @Override
    public void execute(Model model, Ui ui, Storage storage) {
        //ToDo set the tolowercase /toUppercase
        String message = "";

        ShowMap shows = model.getShows();
        try {
            if (!formatter.isValidDate(showDate)) {
                throw new OptixInvalidDateException();
            }

            LocalDate localDate = formatter.toLocalDate(showDate);

            if (localDate.compareTo(storage.getToday()) <= 0) {
                message = MESSAGE_INVALID_DATE;
            } else if (!shows.containsKey(localDate)) {
                message = MESSAGE_SHOW_NOT_FOUND;
            } else if (!shows.get(localDate).hasSameName(showName)) {
                message = MESSAGE_DOES_NOT_MATCH;
            } else {
                shows.get(localDate).setTiers(seatTiers, numberOfTiers);
                if (numberOfTiers == 3) {
                    message = String.format(MESSAGE_THREE_TIERS, seatsEachTier[0], seatsEachTier[1], seatsEachTier[2]);
                } else if (numberOfTiers == 4) {
                    message = String.format(MESSAGE_FOUR_TIERS, seatsEachTier[0], seatsEachTier[1], seatsEachTier[2],
                            seatsEachTier[3]);
                } else if (numberOfTiers == 5) {
                    message = String.format(MESSAGE_FIVE_TIERS, seatsEachTier[0], seatsEachTier[1], seatsEachTier[2],
                            seatsEachTier[3], seatsEachTier[4]);
                } else {
                    message = String.format(MESSAGE_SIX_TIERS, seatsEachTier[0], seatsEachTier[1], seatsEachTier[2],
                                seatsEachTier[3], seatsEachTier[4], seatsEachTier[5]);
                }
            }
        } catch (OptixInvalidDateException e) {
            message = e.getMessage();
        } finally {
            ui.setMessage(message);
        }
    }
}
