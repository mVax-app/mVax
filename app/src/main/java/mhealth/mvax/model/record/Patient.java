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
import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.detail.DateDetail;
import mhealth.mvax.records.record.patient.detail.Detail;
import mhealth.mvax.records.record.patient.detail.SexDetail;
import mhealth.mvax.records.record.patient.detail.StringDetail;
import mhealth.mvax.utilities.StringFetcher;
import mhealth.mvax.records.record.patient.detail.StringNumberDetail;

/**
 * @author Robert Steilberg
 * <p>
 * Data structure for representing an mVax Patient
 */
public class Patient implements Serializable {

    private Patient() {
        // Firebase POJO constructor
    }

    public Patient(String databaseKey) {
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
     * Sex (Male or Female)
     */
    private Sex sex;

    public Sex getSex() {
        return this.sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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
     * Patient residential community
     */
    private String community = "";

    public String getCommunity() {
        return this.community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    /**
     * Patient place of birth
     */
    private String placeOfBirth = "";

    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    /**
     * Patient residence
     */
    private String residence = "";

    public String getResidence() {
        return this.residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    /**
     * Phone number for contacting the patient
     */
    private String phoneNumber = "";

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Guardian name
     */
    private String guardianName = "";

    public String getGuardianName() {
        return this.guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    /**
     * Computes a String to display the Person's name, in format
     * lastName, firstName
     *
     * @return formatted String representing full name, or no_patient_name
     * if the patient does not have a last name
     */
    @Exclude
    public String getName() {
        if (lastName.equals("")) {
            return StringFetcher.fetchString(R.string.no_patient_name);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(lastName);
        if (!firstName.equals("")) sb.append(", ").append(firstName);
        return sb.toString();
    }

    /**
     * Computes a List of Detail objects that represent each property that
     * every Person contains
     *
     * @return List of Detail objects, ordered according to how they
     * will be displayed
     */
    @Exclude
    public List<Detail> getDetails() {
        ArrayList<Detail> details = new ArrayList<>();

        // medical ID
        final StringNumberDetail medicalIdDetail = new StringNumberDetail(
                this.medicalId,
                R.string.label_medicalID,
                R.string.hint_medicalID,
                false);
        medicalIdDetail.setSetter(() -> setMedicalId(medicalIdDetail.getValue()));
        details.add(medicalIdDetail);

        // first name
        final StringDetail firstNameDetail = new StringDetail(
                this.firstName,
                R.string.label_firstname,
                R.string.hint_firstname,
                true);
        firstNameDetail.setSetter(() -> setFirstName(firstNameDetail.getValue()));
        details.add(firstNameDetail);

        // last name
        final StringDetail lastNameDetail = new StringDetail(
                this.lastName,
                R.string.label_last_name,
                R.string.hint_last_name,
                true);
        lastNameDetail.setSetter(() -> setLastName(lastNameDetail.getValue()));
        details.add(lastNameDetail);

        // patient sex
        final SexDetail sexDetail = new SexDetail(
                this.sex,
                R.string.label_sex,
                R.string.hint_sex,
                true);
        sexDetail.setSetter(() -> setSex(sexDetail.getValue()));
        details.add(sexDetail);

        // date of birth
        final DateDetail dobDetail = new DateDetail(
                this.DOB,
                R.string.label_dob,
                R.string.hint_dob,
                true);
        dobDetail.setSetter(() -> setDOB(dobDetail.getValue()));
        details.add(dobDetail);

        // place of birth
        final StringDetail placeOfBirthDetail = new StringDetail(
                this.placeOfBirth,
                R.string.label_pob,
                R.string.hint_pob,
                false);
        placeOfBirthDetail.setSetter(() -> setPlaceOfBirth(placeOfBirthDetail.getValue()));
        details.add(placeOfBirthDetail);

        // community
        final StringDetail communityDetail = new StringDetail(
                this.community,
                R.string.label_community,
                R.string.hint_community,
                false);
        communityDetail.setSetter(() -> setCommunity(communityDetail.getValue()));
        details.add(communityDetail);

        // address
        final StringDetail residenceDetail = new StringDetail(
                this.residence,
                R.string.label_residence,
                R.string.hint_residence,
                false);
        residenceDetail.setSetter(() -> setResidence(residenceDetail.getValue()));
        details.add(residenceDetail);

        // guardian name
        final StringDetail guardianNameDetail = new StringDetail(
                this.guardianName,
                R.string.label_guardian_name,
                R.string.hint_guardian_name,
                true);
        guardianNameDetail.setSetter(() -> setGuardianName(guardianNameDetail.getValue()));
        details.add(guardianNameDetail);

        // phone number
        final StringNumberDetail phoneNumberDetail = new StringNumberDetail(
                this.phoneNumber,
                R.string.label_phone_number,
                R.string.hint_phone_number,
                false);
        phoneNumberDetail.setSetter(() -> setPhoneNumber(phoneNumberDetail.getValue()));
        details.add(phoneNumberDetail);

        return details;
    }

    public static int getNumDetails() {
        return 10;
    }

}
