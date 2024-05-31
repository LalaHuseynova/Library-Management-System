package user;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Time {

    public static boolean isValidFormat(int year, int month, int day) {
        // Check if the year has 4 digits
        if (String.valueOf(year).length() != 4) {
            return false;
        }
    
        // Check if the month is between 1 and 12
        if (month < 1 || month > 12) {
            return false;
        }
    
        // Check if the day is appropriate for the selected month
        boolean validDay = false;
        if (month == 2) { // February
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                validDay = day >= 1 && day <= 29; // Leap year
            } else {
                validDay = day >= 1 && day <= 28; // Non-leap year
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            validDay = day >= 1 && day <= 30; // April, June, September, November
        } else {
            validDay = day >= 1 && day <= 31; // All other months
        }
    
        return validDay;
    }
    



     public static long calculateTimeSpent(String startDate, String endDate) {
        // Parse the start and end dates
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Calculate the difference in minutes
        long daysDifference = ChronoUnit.DAYS.between(start, end);
        long minutesDifference = daysDifference * 24 * 60; // Convert days to minutes

        return minutesDifference;
    }
    
}
