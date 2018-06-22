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
 * Stores data representing a vaccination
 * associated with a particular patient and dose
 */
public class Vaccination extends Date implements Serializable {

    private Vaccination() {
        // Firebase POJO constructor
    }

    public Vaccination(String databaseKey, String patientDatabaseKey, String doseDatabaseKey, Long date) {
        super(databaseKey, patientDatabaseKey, date);
        this.doseDatabaseKey = doseDatabaseKey;
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

}
