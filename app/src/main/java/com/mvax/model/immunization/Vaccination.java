/*
Copyright (C) 2018 Duke University

This file is part of mVax.

mVax is free software: you can redistribute it and/or
modify it under the terms of the GNU Affero General Public License
as published by the Free Software Foundation, either version 3,
or (at your option) any later version.

mVax is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU General Public
License along with mVax; see the file LICENSE. If not, see
<http://www.gnu.org/licenses/>.
*/
package com.mvax.model.immunization;

import java.io.Serializable;

/**
 * @author Robert Steilberg
 * <p>
 * Stores data representing a vaccination
 * associated with a particular patient and dose
 */
public class Vaccination extends Date implements Serializable {

    private Vaccination() {
        // Firebase POJO constructor
    }

    public Vaccination(String databaseKey, String patientKey, String vaccineKey, String doseKey, Long date) {
        super(databaseKey, patientKey, date);
        this.vaccineKey = vaccineKey;
        this.doseKey = doseKey;
    }

    /**
     * Unique Firebase database key representing the
     * Vaccination's associated Vaccine
     */
    private String vaccineKey;

    public String getVaccineKey() {
        return this.vaccineKey;
    }

    public void setVaccineDatabaseKey(String vaccineKey) {
        this.vaccineKey = vaccineKey;
    }

    /**
     * Unique Firebase database key representing the
     * Vaccination's associated Dose
     */
    private String doseKey;

    public String getDoseKey() {
        return this.doseKey;
    }

    public void setDoseKey(String doseKey) {
        this.doseKey = doseKey;
    }

    /**
     * Age of patient in months at time of vaccination
     */
    private String months;

    public String getMonths() {
        return this.months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    /**
     * Age of patient in years at time of vaccination
     */
    private String years;

    public String getYears() {
        return this.years;
    }

    public void setYears(String years) {
        this.years = years;
    }

}
