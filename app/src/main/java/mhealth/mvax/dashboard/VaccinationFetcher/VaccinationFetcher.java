package mhealth.mvax.dashboard.VaccinationFetcher;

import java.util.List;

/**
 * @author Matthew Tribby
 *         This interface sets forth standard methods for vetching vaccination data from the DB.
 *         The reason for an interface is to make the expectations DB agnostic
 */

public interface VaccinationFetcher {

    public VaccinationBundle getVaccinationsByDay(String day, String month, String year, List<String> possibleVaccines);
    public VaccinationBundle getVaccinationsByMonth(String month, String year, List<String> possibleVaccines);
}
