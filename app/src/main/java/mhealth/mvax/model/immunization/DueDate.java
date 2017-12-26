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
 *         <p>
 *         Stores data representing a due date associated
 *         with a particular patient and vaccine
 */
public class DueDate extends Date implements Serializable {

    //================================================================================
    // Constructors
    //================================================================================

    private DueDate() {
        // Firebase POJO constructor
    }

    public DueDate(String databaseKey, String patientDatabaseKey, String vaccineDatabaseKey, Long date) {
        super(databaseKey, patientDatabaseKey, date);
        this.vaccineDatabaseKey = vaccineDatabaseKey;
    }

    //================================================================================
    // Extended properties
    //================================================================================

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

}
