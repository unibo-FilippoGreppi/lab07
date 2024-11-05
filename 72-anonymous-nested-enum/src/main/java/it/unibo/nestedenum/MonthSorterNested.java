package it.unibo.nestedenum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of {@link MonthSorter}.
 */
public final class MonthSorterNested implements MonthSorter {
    private enum Month {
        JANUARY(31),
        FEBRUARY(28),
        MARCH(31),
        APRIL(30),
        MAY(31),
        JUNE(30),
        JULY(31),
        AUGUST(31),
        SEPTEMBER(30),
        OCTOBER(31),
        NOVEMBER(30),
        DECEMBER(31);
    
        private final int days;

        Month(final int days) {
            this.days = days;
        }

        public int getDays() {
            return this.days;
        }

        public static Month fromString(final String actualName) {
            try {
                final List<Month> list = new ArrayList<>(); 
                for (var month : Month.values()) {
                    if(month.toString().toLowerCase().startsWith(actualName.toLowerCase())) {
                        list.add(month);
                    }
                }
                if (list.size() == 0) {
                    throw new IllegalArgumentException("No month with such name");
                }
                
                if (list.size() > 1) {
                    throw new IllegalArgumentException("Ambiguos name");
                } 
                
                return list.getFirst();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public Comparator<String> sortByDays() {
        return new SortByDate();
    }

    @Override
    public Comparator<String> sortByOrder() {
        return new SortByMonthOrder();
    }

    private static class SortByMonthOrder implements Comparator<String> {
        @Override
        public int compare(final String o1, final String o2) {
            return Month.fromString(o1).compareTo(Month.fromString(o2));
        }
    }

    private static class SortByDate implements Comparator<String> {
        @Override
        public int compare(final String o1, final String o2) {
            return Integer.valueOf(Month.fromString(o1).getDays()).compareTo(Integer.valueOf(Month.fromString(o2).getDays()));
        }
    }
}
