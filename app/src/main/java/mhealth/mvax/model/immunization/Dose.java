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
 *         Object for storing information about mVax doses
 */

public class Dose implements Serializable {

    //================================================================================
    // Constructors
    //================================================================================

    private Dose() {
        // Firebase constructor
    }

    public Dose(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    //================================================================================
    // Properties
    //================================================================================

    /**
     * Unique Firebase database key
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
    private String formCode;

    public String getFormCode() {
        return this.formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    /**
     * First label
     */
    private String label1;

    public String getLabel1() {
        return this.label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    /**
     * Second label
     */
    private String label2;

    public String getLabel2() {
        return this.label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    //================================================================================
    // Computed methods
    //================================================================================

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

}
