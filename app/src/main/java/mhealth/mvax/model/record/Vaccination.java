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
 *         DESCRIPTION HERE
 */
public class Vaccination implements Serializable {

    //================================================================================
    // Constructors
    //================================================================================

    private Vaccination() {
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

    private String doseDatabaseKey;

    public String getDoseDatabaseKey() {
        return this.doseDatabaseKey;
    }

    public void setDoseDatabaseKey(String doseDatabaseKey) {
        this.doseDatabaseKey = doseDatabaseKey;
    }

    private Long date;

    public Long getDate() {
        return this.date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

}
