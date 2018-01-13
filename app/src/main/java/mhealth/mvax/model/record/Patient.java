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
import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.detail.DateDetail;
import mhealth.mvax.records.record.patient.detail.Detail;
import mhealth.mvax.records.record.patient.detail.SexDetail;
import mhealth.mvax.records.record.patient.detail.StringDetail;
import mhealth.mvax.records.utilities.StringFetcher;
import mhealth.mvax.records.record.patient.detail.StringNumberDetail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Abstract data structure representing the basic info for
 *         a person, which is extended to create a specialized
 *         data structure (i.e. Patient, Guardian)
 */
public class Patient implements Serializable {

    //================================================================================
    // Constructors
    //================================================================================

    private Patient() {
        // Firebase POJO constructor
    }

    public Patient(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    //================================================================================
    // Properties
    //================================================================================

    /**
     * Unique Firebase database key of the Person object
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
     * Middle name
     */
    private String middleName = "";

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * First printed surname
     */
    private String firstSurname = "";

    public String getFirstSurname() {
        return this.firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    /**
     * Last printed surname
     */
    private String lastSurname = "";

    public String getLastSurname() {
        return this.lastSurname;
    }

    public void setLastSurname(String lastSurname) {
        this.lastSurname = lastSurname;
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
     */
    private String phoneNumber = "";

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

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
     * List of unique Firebase database keys, each of
     * which represents a Patient who is one of this
     * Patient's dependents
     */
    private List<String> dependents = new ArrayList<>();

    public ArrayList<String> getDependents() {
        return new ArrayList<>(this.dependents);
    }

    public void setDependents(List<String> dependents) {
        this.dependents = dependents;
    }

    /**
     * Adds a Patient to this Patient's list of patients
     *
     * @param dependentDatabaseKey Firebase unique database key
     *                             representing the dependent
     */
    @Exclude
    public void addDependent(String dependentDatabaseKey) {
        dependents.add(dependentDatabaseKey);
    }

    //================================================================================
    // Static methods
    //================================================================================

//    /**
//     * @param people variadic list of Person objects
//     * @return a map, ordered by key, that maps a String resource id,
//     * representing a section title, to a Person object's details
//     */
//    @Exclude
//    public static LinkedHashMap<Integer, List<Detail>> getSectionedDetails(Person... people) {
//        final LinkedHashMap<Integer, List<Detail>> details = new LinkedHashMap<>();
//        for (final Person p : people) {
//            if (p != null) details.put(p.getSectionTitleStringID(), p.getDetails());
//        }
//        return details;
//    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Computes a String to display the Person's name, in format
     * firstSurname lastSurname, firstName middleName
     *
     * @return formatted String representing full name, or no_patient_name
     * if the patient does not have a first surname
     */
    @Exclude
    public String getName() {
        if (firstSurname.equals("")) {
            return StringFetcher.fetchString(R.string.no_patient_name);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(firstSurname);
        if (!lastSurname.equals("")) sb.append(" ").append(lastSurname);
        if (!firstName.equals("")) sb.append(", ").append(firstName);
        if (!middleName.equals("")) sb.append(" ").append(middleName);
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
                R.string.hint_medicalID);
        medicalIdDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setMedicalId(medicalIdDetail.getValue());
            }
        });
        details.add(medicalIdDetail);

        // first name
        final StringDetail firstNameDetail = new StringDetail(
                this.firstName,
                R.string.label_firstname,
                R.string.hint_firstname);
        firstNameDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setFirstName(firstNameDetail.getValue());
            }
        });
        details.add(firstNameDetail);

        // patient middle name
        final StringDetail middleNameDetail = new StringDetail(
                this.middleName,
                R.string.label_middlename,
                R.string.hint_middlename);
        middleNameDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setMiddleName(middleNameDetail.getValue());
            }
        });
        details.add(middleNameDetail);

        // first surname
        final StringDetail firstSurnameDetail = new StringDetail(
                this.firstSurname,
                R.string.label_first_surname,
                R.string.hint_first_surname);
        firstSurnameDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setFirstSurname(firstSurnameDetail.getValue());
            }
        });
        details.add(firstSurnameDetail);

        // last surname
        final StringDetail lastSurnameDetail = new StringDetail(
                this.lastSurname,
                R.string.label_last_surname,
                R.string.hint_last_surname);
        lastSurnameDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setLastSurname(lastSurnameDetail.getValue());
            }
        });
        details.add(lastSurnameDetail);

        // patient sex
        final SexDetail sexDetail = new SexDetail(
                this.sex,
                R.string.label_sex,
                R.string.hint_sex);
        sexDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setSex(sexDetail.getValue());
            }
        });
        details.add(sexDetail);

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

}
