package optix.commands.shows;

import optix.commands.Command;
import optix.commons.Model;
import optix.commons.Storage;
import optix.commons.model.Show;
import optix.commons.model.ShowHistoryMap;
import optix.commons.model.ShowMap;
import optix.commons.model.Theatre;
import optix.exceptions.OptixInvalidDateException;
import optix.ui.Ui;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;

public class ViewMonthlyCommand extends Command {

    private String month;
    private String year;
    private String[] monthList = {"january", "february", "march", "april", "may", "june", "july", "august", "september",
            "october", "november", "december"};

    private static final String MESSAGE_NO_SHOW_FOUND = "☹ OOPS!!! There are no shows in %1$s %2$s\n";

    private static final String MESSAGE_SUCCESSFUL = "The amount earned in %1$s %2$s is %3$s\n";

    /**
     * Views the profit for a certain month.
     * @param month month to query for.
     * @param year  year to query for.
     */
    public ViewMonthlyCommand(String month, String year) {
        this.month = month.trim().toLowerCase();
        this.year = year.trim().toLowerCase();
        System.out.println("the month is " + month);
        System.out.println("the year is " + year);
    }

    private int parseInt(String month) {
        int mth = 1;

        for (int i = 0; i < monthList.length; i++) {
            if (month.equals(monthList[i])) {
                return mth;
            } else {
                mth++;
            }
        }

        return mth;
    }

    private boolean isValidMonth(String month) {
        for (int i = 0; i < monthList.length; i++) {
            if (month.equals(monthList[i])) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidYear(String year) {
        try {
            Integer.parseInt(year);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    @Override
    public void execute(Model model, Ui ui, Storage storage) {
        String message = "";

        try {
            if (!isValidMonth(month) || !isValidYear(year)) {
                throw new OptixInvalidDateException();
            }

            int mth = parseInt(month);
            int yr = Integer.parseInt(year);

            System.out.println("the mth is " + mth);
            System.out.println("the yr is " + yr);
            double profit = 0.0;

            ShowHistoryMap shows = model.getShowsHistory();
            ShowHistoryMap showsWanted = new ShowHistoryMap();

            LocalDate key;
            Show value;

            for (Map.Entry<LocalDate, Show> entry : shows.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                showsWanted.put(key, value);
            }

            System.out.println(showsWanted.size());

            /*
            Iterator<Map.Entry<LocalDate, Theatre>> iter = shows.entrySet().iterator();
            while (iter.hasNext()) {
                //Map.Entry<LocalDate, Theatre> entry = iter.next();
                //remove show from the show list if year and month does not match
                shows.entrySet().removeIf(entry -> entry.getKey().getMonthValue() != mth
                        || entry.getKey().getYear() != yr);
                System.out.println(shows.size());
            }
             */

            showsWanted.entrySet().removeIf(entry -> entry.getKey().getMonthValue() != mth
                    || entry.getKey().getYear() != yr);

            System.out.println("test line");
            if (showsWanted.isEmpty()) {
                message = String.format(MESSAGE_NO_SHOW_FOUND, month, year);
            } else {
                for (Map.Entry<LocalDate, Show> entry : showsWanted.entrySet()) {
                    profit += entry.getValue().getProfit();
                }
                message = String.format(MESSAGE_SUCCESSFUL, month, year, profit);
            }

        } catch (OptixInvalidDateException e) {
            message = e.getMessage();
        } finally {
            ui.setMessage(message);
        }
    }

    @Override
    public boolean isExit() {
        return super.isExit();
    }
}
