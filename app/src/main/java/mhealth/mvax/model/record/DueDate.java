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
 *         DESCRIPTION HERE
 */
public class DueDate {

    private DueDate() {
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

    private String databaseKey;

    public String getDatabaseKey() {
        return this.databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    private String patientDatabaseKey;

    public String getPatientDatabaseKey() {
        return this.patientDatabaseKey;
    }

    public void setPatientDatabaseKey(String patientDatabaseKey) {
        this.patientDatabaseKey = patientDatabaseKey;
    }

    private String vaccineDatabaseKey;

    public String getVaccineDatabaseKey() {
        return this.vaccineDatabaseKey;
    }

    public void setVaccineDatabaseKey(String vaccineDatabaseKey) {
        this.vaccineDatabaseKey = vaccineDatabaseKey;
    }

    private Long dueDate;

    public Long getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }


}
