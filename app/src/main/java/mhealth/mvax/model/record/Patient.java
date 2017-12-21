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

import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.views.detail.DateDetail;
import mhealth.mvax.records.views.detail.Detail;
import mhealth.mvax.records.views.detail.StringDetail;
import mhealth.mvax.records.views.detail.StringNumberDetail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Data structure representing a Patient, for which
 *         immunization and other medical data is recorded
 */
public class Patient extends Person {

    // TODO comment
    private Patient() {
    }

    public Patient(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Unique Firebase key of the patient's primary
     * guardian
     */
    private String guardianDatabaseKey;

    public String getGuardianDatabaseKey() {
        return this.guardianDatabaseKey;
    }

    public void setGuardianDatabaseID(String guardianDatabaseKey) {
        this.guardianDatabaseKey = guardianDatabaseKey;
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
    private String phoneNumber;

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public List<Detail> getDetails(Context context) {
        ArrayList<Detail> details = new ArrayList<>();
        details.addAll(getPersonDetails(context));

        // date of birth
        final DateDetail dobDetail = new DateDetail(
                context.getResources().getString(R.string.label_dob),
                context.getResources().getString(R.string.hint_dob),
                this.DOB,
                context);
        dobDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setDOB(dobDetail.getValue());
            }
        });
        details.add(dobDetail);

        // community
        final StringDetail communityDetail = new StringDetail(
                context.getResources().getString(R.string.label_community),
                context.getResources().getString(R.string.hint_community),
                this.community,
                context);
        communityDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setCommunity(communityDetail.getValue());
            }
        });
        details.add(communityDetail);

        // place of birth
        final StringDetail placeOfBirthDetail = new StringDetail(
                context.getResources().getString(R.string.label_pob),
                context.getResources().getString(R.string.hint_pob),
                this.placeOfBirth,
                context);
        placeOfBirthDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setPlaceOfBirth(placeOfBirthDetail.getValue());
            }
        });
        details.add(placeOfBirthDetail);

        // address
        final StringDetail addressDetail = new StringDetail(
                context.getResources().getString(R.string.label_address),
                context.getResources().getString(R.string.hint_address),
                this.address,
                context);
        addressDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setAddress(addressDetail.getValue());
            }
        });
        details.add(addressDetail);

        // phone number
        final StringNumberDetail phoneNumberDetail = new StringNumberDetail(
                context.getResources().getString(R.string.label_phone_number),
                context.getResources().getString(R.string.hint_phone_number),
                this.phoneNumber,
                context);
        phoneNumberDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setPhoneNumber(phoneNumberDetail.getValue());
            }
        });
        details.add(phoneNumberDetail);

        return details;
    }
}
