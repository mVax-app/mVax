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

/**
 * @author Robert Steilberg
 *         <p>
 *         Stores data representing a due date associated
 *         with a particular patient and vaccine
 */
public class DueDate {

    //================================================================================
    // Constructors
    //================================================================================

    private DueDate() {
        // Firebase constructor
    }

    public DueDate(String databaseKey, String patientDatabaseKey, String vaccineDatabaseKey, Long dueDate) {
        this.databaseKey = databaseKey;
        this.patientDatabaseKey = patientDatabaseKey;
        this.vaccineDatabaseKey = vaccineDatabaseKey;
        this.dueDate = dueDate;
    }

    //================================================================================
    // Properties
    //================================================================================

    /**
     * Unique Firebase database key representing the DueDate
     */
    private String databaseKey;

    public String getDatabaseKey() {
        return this.databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Unique Firebase database key representing DueDate's
     * associated Patient
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
     * DueDate's associated Vaccine
     */
    private String vaccineDatabaseKey;

    public String getVaccineDatabaseKey() {
        return this.vaccineDatabaseKey;
    }

    public void setVaccineDatabaseKey(String vaccineDatabaseKey) {
        this.vaccineDatabaseKey = vaccineDatabaseKey;
    }

    /**
     * Due date, represented as milliseconds since Unix epoch
     */
    private Long dueDate;

    public Long getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

}
