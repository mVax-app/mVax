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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.views.detail.DateDetail;
import mhealth.mvax.records.views.detail.Detail;
import mhealth.mvax.records.views.detail.SexDetail;
import mhealth.mvax.records.views.detail.StringDetail;
import mhealth.mvax.records.views.detail.StringNumberDetail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Object for storing information about mVax records;
 *         implements Serializable so that it can be bassed as
 *         a Bundle argument to fragments
 */

public class Record implements Serializable {

    //================================================================================
    // Constructors
    //================================================================================

    /**
     * Default Firebase constructor; should not
     * be used internally
     */
    public Record() {
    }

    public Record(String databaseId, ArrayList<Vaccine> vaccines) {
        mDatabaseId = databaseId;
        Collections.sort(vaccines);
        mVaccines = vaccines;
    }


    //================================================================================
    // Properties
    //================================================================================

    /**
     * Unique ID assigned to the object by Firebase
     */
    private String mDatabaseId;

    public String getDatabaseId() {
        return this.mDatabaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.mDatabaseId = databaseId;
    }

    /**
     * 13-digit patient integer ID,
     * given to each registered Honduran citizen
     */
    private String mId;

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    /**
     * Patient's first name
     */
    private String mFirstName;

    public String getFirstName() {
        return this.mFirstName;
    }

    public void setFirstName(String name) {
        this.mFirstName = name;
    }

    /**
     * Patient's middle name
     */
    private String mMiddleName;

    public String getMiddleName() {
        return this.mMiddleName;
    }

    public void setMiddleName(String name) {
        this.mMiddleName = name;
    }

    /**
     * Patient's surname
     */
    private String mLastName;

    public String getLastName() {
        return this.mLastName;
    }

    public void setLastName(String name) {
        this.mLastName = name;
    }

    /**
     * Patient's suffix
     */
    private String mSuffix;

    public String getSuffix() {
        return this.mSuffix;
    }

    public void setSuffix(String suffix) {
        this.mSuffix = suffix;
    }

    /**
     * Patient's biological sex
     */
    private Sex mSex;

    public Sex getSex() {
        return this.mSex;
    }

    public void setSex(Sex sex) {
        this.mSex = sex;
    }

    /**
     * Patient's date of birth, expressed in
     * milliseconds since Unix epoch
     */
    private Long mDOB;

    public Long getDOB() {
        return this.mDOB;
    }

    public void setDOB(Long dateInMillis) {
        this.mDOB = dateInMillis;
    }

    /**
     * Patient's place of birth
     */
    private String mPlaceOfBirth;

    public String getPlaceOfBirth() {
        return this.mPlaceOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.mPlaceOfBirth = placeOfBirth;
    }

    /**
     * Patient's community of residence
     */
    private String mCommunity;

    public String getCommunity() {
        return this.mCommunity;
    }

    public void setCommunity(String community) {
        this.mCommunity = community;
    }


    /**
     * Parent first name
     */
    private String mParentFirstName;

    public String getParentFirstName() {
        return this.mParentFirstName;
    }

    public void setParentFirstName(String name) {
        this.mParentFirstName = name;
    }

    /**
     * Patient's middle name
     */
    private String mParentMiddleName;

    public String getParentMiddleName() {
        return this.mParentMiddleName;
    }

    public void setParentMiddleName(String name) {
        this.mParentMiddleName = name;
    }

    /**
     * Parent surname
     */
    private String mParentLastName;

    public String getParentLastName() {
        return this.mParentLastName;
    }

    public void setParentLastName(String name) {
        this.mParentLastName = name;
    }

    /**
     * Parent suffix
     */
    private String mParentSuffix;

    public String getParentSuffix() {
        return this.mParentSuffix;
    }

    public void setParentSuffix(String suffix) {
        this.mParentSuffix = suffix;
    }

    /**
     * Parent's biological sex
     */
    private Sex mParentSex;

    public Sex getParentSex() {
        return this.mParentSex;
    }

    public void setParentSex(Sex sex) {
        this.mParentSex = sex;
    }

    /**
     * 13-digit patient integer ID,
     * given to each registered Honduran citizen
     */
    private String mParentId;

    public String getParentId() {
        return this.mParentId;
    }

    public void setParentId(String id) {
        this.mParentId = id;
    }

    /**
     * Number of dependents, including patient
     */
    private String mNumDependents;

    public String getNumDependents() {
        return this.mNumDependents;
    }

    public void setNumDependents(String numDependents) {
        this.mNumDependents = numDependents;
    }

    /**
     * Parent residential address
     */
    private String mParentAddress;

    public String getParentAddress() {
        return this.mParentAddress;
    }

    public void setParentAddress(String address) {
        this.mParentAddress = address;
    }

    /**
     * Parent phone number
     */
    private String mParentPhone;

    public String getParentPhone() {
        return this.mParentPhone;
    }

    public void setParentPhone(String phoneNumber) {
        this.mParentPhone = phoneNumber;
    }

    /**
     * Map containing the record's vaccine history
     */
    private ArrayList<Vaccine> mVaccines;

    /**
     * This getter should only be used externally by Firebase;
     * use getVaccineList to get the record's vaccine history
     */
    public ArrayList<Vaccine> getVaccines() {
        return this.mVaccines;
    }

    /**
     * This setter should only be used externally by Firebase;
     * use addVaccine or updateVaccine to modify associated Vaccines
     */
    public void setVaccines(ArrayList<Vaccine> vaccines) {
        Collections.sort(vaccines);
        this.mVaccines = vaccines;
    }


    //================================================================================
    // Computed getters
    //================================================================================

    @Exclude
    public String getFullName() {
        // TODO handle lack of first and last name, and test for it
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(mLastName);
        nameBuilder.append(", ");
        nameBuilder.append(mFirstName);
        if (mMiddleName != null) {
            nameBuilder.append(" ");
            nameBuilder.append(mMiddleName);
        }
        if (mSuffix != null) {
            nameBuilder.append(", ");
            nameBuilder.append(mSuffix);
        }
        return nameBuilder.toString();
    }

    @Exclude
    public String getParentFullName() {
        // TODO reduce DRY
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(mParentLastName);
        nameBuilder.append(", ");
        nameBuilder.append(mParentFirstName);
        if (mParentMiddleName != null) {
            nameBuilder.append(" ");
            nameBuilder.append(mParentMiddleName);
        }
        if (mParentSuffix != null) {
            nameBuilder.append(", ");
            nameBuilder.append(mParentSuffix);
        }
        return nameBuilder.toString();
    }


    /**
     * Gets the attributes of a record, sectioned according ArrayLists
     * within a LinkedHashMap
     *
     * @param context Android context used to access Strings
     * @return LinkedHashMap containing ordered ArrayLists of attributes
     * from the record
     */
    @Exclude
    public LinkedHashMap<String, List<Detail>> getSectionedAttributes(Context context) {

//        // PATIENT SECTION ==================================================================
//
//        ArrayList<Detail> patientAttributes = new ArrayList<>();
//
//        // patient ID
//        final StringNumberDetail id = new StringNumberDetail(
//                context.getResources().getString(R.string.label_medicalID),
//                context.getResources().getString(R.string.hint_medicalID),
//                this.mId,
//                context);
//        id.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setId(id.getValue());
//            }
//        });
//        patientAttributes.add(id);
//
//        // patient first name
//        final StringDetail firstName = new StringDetail(
//                context.getResources().getString(R.string.label_firstname),
//                context.getResources().getString(R.string.hint_firstname),
//                this.mFirstName,
//                context);
//        firstName.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setFirstName(firstName.getValue());
//            }
//        });
//        patientAttributes.add(firstName);
//
//        // patient middle name
//        final StringDetail middleName = new StringDetail(
//                context.getResources().getString(R.string.label_middlename),
//                context.getResources().getString(R.string.hint_middlename),
//                this.mMiddleName,
//                context);
//        middleName.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setMiddleName(middleName.getValue());
//            }
//        });
//        patientAttributes.add(middleName);
//
//        // patient last name
//        final StringDetail lastName = new StringDetail(
//                context.getResources().getString(R.string.label_lastname),
//                context.getResources().getString(R.string.hint_lastname),
//                this.mLastName,
//                context);
//        lastName.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setLastName(lastName.getValue());
//            }
//        });
//        patientAttributes.add(lastName);
//
//        // patient suffix
//        final StringDetail suffix = new StringDetail(
//                context.getResources().getString(R.string.label_suffix),
//                context.getResources().getString(R.string.hint_suffix),
//                this.mSuffix,
//                context);
//        suffix.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setSuffix(suffix.getValue());
//            }
//        });
//        patientAttributes.add(suffix);
//
//        // patient sex
//        final SexDetail sex = new SexDetail(
//                context.getResources().getString(R.string.label_sex),
//                context.getResources().getString(R.string.hint_sex),
//                this.mSex,
//                context);
//        sex.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setSex(sex.getValue());
//            }
//        });
//        patientAttributes.add(sex);
//
//        // patient DOB
//        final DateDetail DOB = new DateDetail(
//                context.getResources().getString(R.string.label_dob),
//                context.getResources().getString(R.string.hint_dob),
//                this.mDOB,
//                context);
//        DOB.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setDOB(DOB.getValue());
//            }
//        });
//        patientAttributes.add(DOB);
//
//        // patient place of birth
//        final StringDetail POB = new StringDetail(
//                context.getResources().getString(R.string.label_pob),
//                context.getResources().getString(R.string.hint_pob),
//                this.mPlaceOfBirth,
//                context);
//        POB.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setPlaceOfBirth(POB.getValue());
//            }
//        });
//        patientAttributes.add(POB);
//
//        // patient community
//        final StringDetail community = new StringDetail(
//                context.getResources().getString(R.string.label_community),
//                context.getResources().getString(R.string.hint_community),
//                this.mCommunity,
//                context);
//        community.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setCommunity(community.getValue());
//            }
//        });
//        patientAttributes.add(community);
//
//
//        // GUARDIAN SECTION =================================================================
//
//        ArrayList<Detail> parentAttributes = new ArrayList<>();
//
//        // guardian ID
//        final StringNumberDetail guardianId = new StringNumberDetail(
//                context.getResources().getString(R.string.label_guardian_id),
//                context.getResources().getString(R.string.hint_medicalID),
//                this.mParentId,
//                context);
//        guardianId.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setParentId(guardianId.getValue());
//            }
//        });
//        parentAttributes.add(guardianId);
//
//        // guardian first name
//        final StringDetail guardianFirst = new StringDetail(
//                context.getResources().getString(R.string.label_guardian_firstname),
//                context.getResources().getString(R.string.hint_firstname),
//                this.mParentFirstName,
//                context);
//        guardianFirst.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setParentFirstName(guardianFirst.getValue());
//            }
//        });
//        parentAttributes.add(guardianFirst);
//
//        // guardian middle name
//        final StringDetail guardianMiddle = new StringDetail(
//                context.getResources().getString(R.string.label_guardian_middlname),
//                context.getResources().getString(R.string.hint_middlename),
//                this.mParentMiddleName,
//                context);
//        guardianMiddle.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setParentMiddleName(guardianMiddle.getValue());
//            }
//        });
//        parentAttributes.add(guardianMiddle);
//
//        // guardian last name
//        final StringDetail guardianLast = new StringDetail(
//                context.getResources().getString(R.string.label_guardian_lastname),
//                context.getResources().getString(R.string.hint_lastname),
//                this.mParentLastName,
//                context);
//        guardianLast.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setParentLastName(guardianLast.getValue());
//            }
//        });
//        parentAttributes.add(guardianLast);
//
//        // guardian suffix
//        final StringDetail guardianSuffix = new StringDetail(
//                context.getResources().getString(R.string.label_guardian_suffix),
//                context.getResources().getString(R.string.hint_suffix),
//                this.mParentSuffix,
//                context);
//        guardianSuffix.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setParentSuffix(guardianSuffix.getValue());
//            }
//        });
//        parentAttributes.add(guardianSuffix);
//
//        // guardian sex
//        final SexDetail guardianSex = new SexDetail(
//                context.getResources().getString(R.string.label_guardian_sex),
//                context.getResources().getString(R.string.hint_sex),
//                this.mParentSex,
//                context);
//        guardianSex.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setParentSex(guardianSex.getValue());
//            }
//        });
//        parentAttributes.add(guardianSex);
//
//        // guardian number dependents
//        final StringNumberDetail numDependents = new StringNumberDetail(
//                context.getResources().getString(R.string.label_numDependents),
//                context.getResources().getString(R.string.hint_numDependents),
//                this.mNumDependents,
//                context);
//        numDependents.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setNumDependents(numDependents.getValue());
//            }
//        });
//        parentAttributes.add(numDependents);
//
//
//
//
//        // guardian address
//        final StringDetail guardianAddress = new StringDetail(
//                context.getResources().getString(R.string.label_address),
//                context.getResources().getString(R.string.hint_address),
//                this.mParentAddress,
//                context);
//        guardianAddress.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setParentAddress(guardianAddress.getValue());
//            }
//        });
//        parentAttributes.add(guardianAddress);
//
//        // guardian phone number
//        final StringNumberDetail phoneNumber = new StringNumberDetail(
//                context.getResources().getString(R.string.label_phone_number),
//                context.getResources().getString(R.string.hint_phone_number),
//                this.mParentPhone,
//                context);
//        phoneNumber.setSetter(new Runnable() {
//            @Override
//            public void run() {
//                setParentPhone(phoneNumber.getValue());
//            }
//        });
//        parentAttributes.add(phoneNumber);
//
//        // add ArrayLists to LinkedHashMap for return
        LinkedHashMap<String, List<Detail>> sectionedAttributes = new LinkedHashMap<>();
//        sectionedAttributes.put(context.getString(R.string.patient_detail_section_title), patientAttributes);
//        sectionedAttributes.put(context.getString(R.string.guardian_detail_section_title), parentAttributes);

        return sectionedAttributes;
    }
}
