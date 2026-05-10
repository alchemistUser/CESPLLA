/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Pololoers
 */
import java.time.LocalDate;
import java.time.Month;

public final class SchoolYearUtil {

    // Philippine Academic Year: Starts June, Ends March
    public static String getSchoolYear(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }

        int year = date.getYear();
        Month month = date.getMonth();

        // If current month is June (6) or later, School Year starts this year.
        // E.g., June 2025 -> SY 2025-2026
        if (month.getValue() >= 6) {
            return year + "-" + (year + 1);
        } // If current month is Jan-May, School Year started last year.
        // E.g., Jan 2026 -> SY 2025-2026
        else {
            return (year - 1) + "-" + year;
        }
    }

    public static String getCurrentSchoolYear() {
        return getSchoolYear(LocalDate.now());
    }
}
