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

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * @author Robert Steilberg
 * <p>
 * Stores information about doses
 */
public class Dose implements Serializable {

    private Dose() {
        // Firebase POJO constructor
    }

    public Dose(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Unique Firebase database key of the Dose object
     */
    private String databaseKey;

    public String getDatabaseKey() {
        return this.databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Form code used by iText to populate PDF forms
     */
    // TODO get rid of this
    private String formCode = "";

    public String getFormCode() {
        return this.formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    /**
     * First label
     */
    private String label1 = "";

    public String getLabel1() {
        return this.label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    /**
     * Second label
     */
    private String label2 = "";

    public String getLabel2() {
        return this.label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    /**
     * Vaccine total administered count
     */
    private Integer givenCount = 0;

    public Integer getGivenCount() {
        return this.givenCount;
    }

    public void setGivenCount(Integer givenCount) {
        this.givenCount = givenCount;
    }

    @Exclude
    public void setLabels(String label1, String label2) {
        this.label1 = label1;
        this.label2 = label2;
    }

    /**
     * @return String representing a label for the dose
     * to be used in a UI
     */
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

    /**
     * Increment a dose's given count by one
     */
    @Exclude
    public void incrementGivenCount() {
        givenCount++;
    }

    /**
     * Decrement a dose's given count by one
     */
    @Exclude
    public void decrementGivenCount() {
        givenCount--;
    }

}
