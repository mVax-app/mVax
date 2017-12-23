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

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Object for storing information about mVax doses;
 *         implements Serializable so that it can be bassed as
 *         a Bundle argument to fragments
 */

public class Dose implements Serializable {

    //================================================================================
    // Constructors
    //================================================================================

    /**
     * Default Firebase constructor; should not
     * be used internally
     */
    private Dose() {
    }

    public Dose (String databaseKey) {
        this.databaseKey = databaseKey;
    }

    private String databaseKey;

    public String getDatabaseKey() {
        return this.databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

//    private String vaccineDatabaseKey;
//
//    public String getVaccineDatabaseKey() {
//        return this.vaccineDatabaseKey;
//    }
//
//    public void setVaccineDatabaseKey(String vaccineDatabaseKey) {
//        this.vaccineDatabaseKey = vaccineDatabaseKey;
//    }

    private String formCode;

    public String getFormCode() {
        return this.formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    /**
     * The first label for the dose
     */
    private String label1;

    public String getLabel1() {
        return this.label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    /**
     * The second label for the dose, if there is one
     */
    private String label2;

    public String getLabel2() {
        return this.label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

//    /**
//     * The amount of time until the next dose in the
//     * vaccine regimen should be administered
//     */
//    private Long mTimeUntilNextDose;
//
//    public Long getTimeUntilNextDose() {
//        return this.mTimeUntilNextDose;
//    }
//
//    public void setTimeUntilNextDose(Long millis) {
//        this.mTimeUntilNextDose = millis;
//    }


    //================================================================================
    // Computed getters
    //================================================================================

    @Exclude
    public String getLabel() {
        StringBuilder sb = new StringBuilder();
        if (label2 != null) {
            sb.append(label1);
            sb.append(" (");
            sb.append(label2);
            sb.append("):");
        } else {
            sb.append(label1);
            sb.append(":");
        }
        return sb.toString();
    }

    //================================================================================
    // Computed setters
    //================================================================================

    @Exclude
    public void setLabels(String label1, String label2) {
        this.label1 = label1;
        this.label2 = label2;
    }

}
