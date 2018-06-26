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
package mhealth.mvax.model.immunization;

import java.io.Serializable;

/**
 * @author Robert Steilberg
 * <p>
 * Generic data structure for associating a patient
 * with a date
 */
public class Date implements Serializable {

    Date() {
        // Firebase POJO constructor
    }

    public Date(String databaseKey, String patientDatabaseKey, Long date) {
        this.databaseKey = databaseKey;
        this.patientDatabaseKey = patientDatabaseKey;
        this.date = date;
    }

    /**
     * Unique Firebase database key representing the Date object
     */
    protected String databaseKey;

    public String getDatabaseKey() {
        return this.databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Unique Firebase database key representing the
     * Date's associated Patient
     */
    protected String patientDatabaseKey;

    public String getPatientDatabaseKey() {
        return this.patientDatabaseKey;
    }

    public void setPatientDatabaseKey(String patientDatabaseKey) {
        this.patientDatabaseKey = patientDatabaseKey;
    }

    /**
     * Date's date value, represented as milliseconds since
     * Unix epoch
     */
    protected Long date;

    public Long getDate() {
        return this.date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

}
