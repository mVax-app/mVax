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

import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.detail.DateDetail;
import mhealth.mvax.records.record.patient.detail.Detail;
import mhealth.mvax.records.record.patient.detail.StringDetail;
import mhealth.mvax.records.record.patient.detail.StringNumberDetail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Extends Person to store additional information
 *         and define functionality specific to a Patient
 */
public class Patient extends Person {

    //================================================================================
    // Constructors
    //================================================================================

    private Patient() {
        // Firebase POJO constructor
    }

    public Patient(String databaseKey) {
        super(databaseKey);
    }

    //================================================================================
    // Properties
    //================================================================================

    /**
     * Unique Firebase key representing the
     * patient's primary guardian
     */
    private String guardianDatabaseKey;

    public String getGuardianDatabaseKey() {
        return this.guardianDatabaseKey;
    }

    public void setGuardianDatabaseKey(String guardianDatabaseKey) {
        this.guardianDatabaseKey = guardianDatabaseKey;
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
     * Patient address
     */
    private String address = "";

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
    private String phoneNumber = "";

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //================================================================================
    // Public methods
    //================================================================================

    @Override
    @Exclude
    public List<Detail> getDetails() {
        ArrayList<Detail> details = new ArrayList<>(getPersonDetails());

        // date of birth
        final DateDetail dobDetail = new DateDetail(
                this.DOB,
                R.string.label_dob,
                R.string.hint_dob);
        dobDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setDOB(dobDetail.getValue());
            }
        });
        details.add(dobDetail);

        // community
        final StringDetail communityDetail = new StringDetail(
                this.community,
                R.string.label_community,
                R.string.hint_community);
        communityDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setCommunity(communityDetail.getValue());
            }
        });
        details.add(communityDetail);

        // place of birth
        final StringDetail placeOfBirthDetail = new StringDetail(
                this.placeOfBirth,
                R.string.label_pob,
                R.string.hint_pob);
        placeOfBirthDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setPlaceOfBirth(placeOfBirthDetail.getValue());
            }
        });
        details.add(placeOfBirthDetail);

        // address
        final StringDetail addressDetail = new StringDetail(
                this.address,
                R.string.label_address,
                R.string.hint_address);
        addressDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setAddress(addressDetail.getValue());
            }
        });
        details.add(addressDetail);

        // phone number
        final StringNumberDetail phoneNumberDetail = new StringNumberDetail(
                this.phoneNumber,
                R.string.label_phone_number,
                R.string.hint_phone_number);
        phoneNumberDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setPhoneNumber(phoneNumberDetail.getValue());
            }
        });
        details.add(phoneNumberDetail);

        return details;
    }

    @Override
    @Exclude
    public int getSectionTitleStringID() {
        return R.string.patient_detail_section_title;
    }
}