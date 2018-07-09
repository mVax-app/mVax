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

import android.content.Context;

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
     * Medical ID assigned to the mother
     */
    private String motherId = "";

    public String getMotherId() {
        return this.motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    @Exclude
    public boolean hasMotherId() {
        return !this.motherId.equals("");
    }

    /**
     * Child number
     */
    private String childNumber = "";

    public String getChildNumber() {
        return this.childNumber;
    }

    public void setChildNumber(String childNumber) {
        this.childNumber = childNumber;
    }

    @Exclude
    public boolean hasChildNumber() {
        return !this.childNumber.equals("");
    }

    /**
     * Medical ID assigned to the patient
     */
    private String medicalId = "";

    public String getMedicalId() {
        return this.medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    @Exclude
    public boolean hasMedicalId() {
        return !this.medicalId.equals("");
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

    @Exclude
    public boolean hasFirstName() {
        return !this.firstName.equals("");
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

    @Exclude
    public boolean hasLastName() {
        return !this.lastName.equals("");
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

    @Exclude
    public boolean hasDOB() {
        return this.DOB != null;
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

    @Exclude
    public boolean hasSex() {
        return this.sex != null;
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

    @Exclude
    public boolean hasPlaceOfBirth() {
        return !this.placeOfBirth.equals("");
    }

    /**
     * Patient residence formatted as
     * municipality, department
     */
    private String residence = "";

    public String getResidence() {
        return this.residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    @Exclude
    public boolean hasResidence() {
        return !this.residence.equals("");
    }
    
    /**
     * Patient residential locality
     */
    private String locality = "";

    public String getLocality() {
        return this.locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Exclude
    public boolean hasLocality() {
        return !this.locality.equals("");
    }

    /**
     * Patient residential address
     */
    private String address = "";

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Exclude
    public boolean hasAddress() {
        return !this.address.equals("");
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

    @Exclude
    public boolean hasPhoneNumber() {
        return !this.phoneNumber.equals("");
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

    @Exclude
    public boolean hasGuardianName() {
        return !this.guardianName.equals("");
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
    public List<Detail> getDetails(Context context) {
        ArrayList<Detail> details = new ArrayList<>();

        // mother ID
        final StringNumberDetail motherIdDetail = new StringNumberDetail(
                this.motherId,
                R.string.label_mother_id,
                R.string.hint_mother_id,
                false,
                context);
        motherIdDetail.setSetter(() -> setMotherId(motherIdDetail.getValue()));
        details.add(motherIdDetail);

        // child number
        final StringNumberDetail childNumberDetail = new StringNumberDetail(
                this.childNumber,
                R.string.label_child_number,
                R.string.hint_child_number,
                false,
                context);
        childNumberDetail.setSetter(() -> setChildNumber(childNumberDetail.getValue()));
        details.add(childNumberDetail);

        // medical ID
        final StringNumberDetail medicalIdDetail = new StringNumberDetail(
                this.medicalId,
                R.string.label_medical_id,
                R.string.hint_medical_id,
                false,
                context);
        medicalIdDetail.setSetter(() -> setMedicalId(medicalIdDetail.getValue()));
        details.add(medicalIdDetail);

        // first name
        final StringDetail firstNameDetail = new StringDetail(
                this.firstName,
                R.string.label_first_name,
                R.string.hint_first_name,
                true,
                context);
        firstNameDetail.setSetter(() -> setFirstName(firstNameDetail.getValue()));
        details.add(firstNameDetail);

        // last name
        final StringDetail lastNameDetail = new StringDetail(
                this.lastName,
                R.string.label_last_name,
                R.string.hint_last_name,
                true,
                context);
        lastNameDetail.setSetter(() -> setLastName(lastNameDetail.getValue()));
        details.add(lastNameDetail);

        // date of birth
        final DateDetail dobDetail = new DateDetail(
                this.DOB,
                R.string.label_dob,
                R.string.hint_dob,
                false,
                context);
        dobDetail.setSetter(() -> setDOB(dobDetail.getValue()));
        details.add(dobDetail);

        // patient sex
        final SexDetail sexDetail = new SexDetail(
                this.sex,
                R.string.label_sex,
                R.string.hint_sex,
                true,
                context);
        sexDetail.setSetter(() -> setSex(sexDetail.getValue()));
        details.add(sexDetail);

        // place of birth
        final StringDetail placeOfBirthDetail = new StringDetail(
                this.placeOfBirth,
                R.string.label_pob,
                R.string.hint_pob,
                false,
                context);
        placeOfBirthDetail.setSetter(() -> setPlaceOfBirth(placeOfBirthDetail.getValue()));
        details.add(placeOfBirthDetail);

        // place of residence (dept, mun)
        final StringDetail residenceDetail = new StringDetail(
                this.residence,
                R.string.label_residence,
                R.string.hint_residence,
                false,
                context);
        residenceDetail.setSetter(() -> setResidence(residenceDetail.getValue()));
        details.add(residenceDetail);

        // locality
        final StringDetail localityDetail = new StringDetail(
                this.locality,
                R.string.label_locality,
                R.string.hint_locality,
                false,
                context);
        localityDetail.setSetter(() -> setLocality(localityDetail.getValue()));
        details.add(localityDetail);

        // address
        final StringDetail addressDetail = new StringDetail(
                this.address,
                R.string.label_address,
                R.string.hint_address,
                true,
                context);
        addressDetail.setSetter(() -> setAddress(addressDetail.getValue()));
        details.add(addressDetail);

        // phone number
        final StringNumberDetail phoneNumberDetail = new StringNumberDetail(
                this.phoneNumber,
                R.string.label_phone_number,
                R.string.hint_phone_number,
                false,
                context);
        phoneNumberDetail.setSetter(() -> setPhoneNumber(phoneNumberDetail.getValue()));
        details.add(phoneNumberDetail);

        // guardian name
        final StringDetail guardianNameDetail = new StringDetail(
                this.guardianName,
                R.string.label_guardian_name,
                R.string.hint_guardian_name,
                false,
                context);
        guardianNameDetail.setSetter(() -> setGuardianName(guardianNameDetail.getValue()));
        details.add(guardianNameDetail);

        return details;
    }

}
