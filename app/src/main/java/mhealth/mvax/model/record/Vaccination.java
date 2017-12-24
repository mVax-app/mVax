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
package mhealth.mvax.model.record;

import java.io.Serializable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Stores data representing a vaccination
 *         associated with a particular patient and dose
 */
public class Vaccination implements Serializable {

    //================================================================================
    // Constructors
    //================================================================================

    private Vaccination() {
        // Firebase constructor
    }

    public Vaccination(String databaseKey, String patientDatabaseKey, String doseDatabaseKey, Long date) {
        this.databaseKey = databaseKey;
        this.patientDatabaseKey = patientDatabaseKey;
        this.doseDatabaseKey = doseDatabaseKey;
        this.date = date;
    }

    //================================================================================
    // Properties
    //================================================================================

    /**
     * Unique Firebase database key representing the Vaccination
     */
    private String databaseKey;

    public String getDatabaseKey() {
        return this.databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Unique Firebase database key representing the
     * Vaccination's associated Patient
     */
    private String patientDatabaseKey;

    public String getPatientDatabaseKey() {
        return this.patientDatabaseKey;
    }

    public void setPatientDatabaseKey(String patientDatabaseKey) {
        this.patientDatabaseKey = patientDatabaseKey;
    }

    /**
     * Unique Firebase database key representing the
     * Vaccination's associated Dose
     */
    private String doseDatabaseKey;

    public String getDoseDatabaseKey() {
        return this.doseDatabaseKey;
    }

    public void setDoseDatabaseKey(String doseDatabaseKey) {
        this.doseDatabaseKey = doseDatabaseKey;
    }

    /**
     * Vaccination date, represented as milliseconds since
     * Unix epoch
     */
    private Long date;

    public Long getDate() {
        return this.date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

}
