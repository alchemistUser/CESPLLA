/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Pololoers
 */
import dao.SchoolYearDAO;
import utils.SchoolYearUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SchoolYearService {

    private final SchoolYearDAO dao = new SchoolYearDAO();

    public List<Map<String, Object>> getArchiveRecordsForYear(String schoolYear) {
        try {
            // Use the Util to calculate dates from "YYYY-YYYY" string
            // We use the start year to find the start date (June 1st)
            int startYear = Integer.parseInt(schoolYear.split("-")[0]);
            LocalDate startDate = LocalDate.of(startYear, 6, 1);
            LocalDate endDate = LocalDate.of(startYear + 1, 3, 31);

            return dao.findArchiveRecords(startDate, endDate);
        } catch (NumberFormatException e) {
            return List.of();
        }
    }
}
