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
 * <p>
 * Object for storing information corresponding to search results
 * returne by the search engine index
 */
public class SearchResult {

    public SearchResult(String databaseKey) {
        this.databaseKey = databaseKey;
    }

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
     * First name
     */
    private String firstName = "";

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Last name
     */
    private String lastName = "";

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Patient date of birth, represented as milliseconds
     * since Unix epoch
     */
    private Long DOB;

    public Long getDOB() {
        return this.DOB;
    }

    public void setDOB(Long DOB) {
        this.DOB = DOB;
    }

    /**
     * Medical ID assigned to the person
     */
    private String medicalId = "";

    public String getMedicalId() {
        return this.medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    /**
     * Computes a String to display the Person's name, in format
     * lastName, firstName
     *
     * @return formatted String representing full name, or no_patient_name
     * if the patient does not have a last name
     */
    public String getName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(lastName);
        if (!firstName.equals("")) sb.append(", ").append(firstName);
        return sb.toString();
    }

}
