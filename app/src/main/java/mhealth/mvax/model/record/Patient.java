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
 *         Data structure representing a Patient, for which
 *         immunization and other medical data is recorded
 */
public class Patient extends Person {

    public Patient() {
    }

    /**
     * Unique Firebase key of the patient's primary
     * guardian
     */
    private String guardianDatabaseID;

    public String getGuardianDatabaseID() {
        return this.guardianDatabaseID;
    }

    public void setGuardianDatabaseID(String guardianDatabaseID) {
        this.guardianDatabaseID = guardianDatabaseID;
    }

    /**
     * Patient date of birth, stored as
     * milliseconds since Unix epoch
     */
    private Long DOB;

    public Long getDOB() {
        return this.DOB;
    }

    public void setDOB(Long DOB) {
        this.DOB = DOB;
    }

    /**
     * Patient's residential community
     */
    private String community;

    public String getCommunity() {
        return this.community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    /**
     * Patient's place of birth
     */
    private String placeOfBirth;

    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    /**
     * Patient's address
     */
    private String address;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Phone number for contacting the patient
     * or their guardian
     */
    private String phone;

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
