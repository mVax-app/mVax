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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.views.detail.Detail;
import mhealth.mvax.records.views.detail.SexDetail;
import mhealth.mvax.records.views.detail.StringDetail;
import mhealth.mvax.records.views.detail.StringNumberDetail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Data structure representing the basic info for
 *         a person, which can be any person represented
 *         in the database
 */
public abstract class Person implements Serializable {

    /**
     * Unique Firebase key generated through push();
     * should be set in constructor
     */
    String databaseKey = "";

    public String getDatabaseKey() {
        return this.databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Medical ID assigned to the person
     */
    private String medicalID = "";

    public String getMedicalID() {
        return this.medicalID;
    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
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





    public String getName(Context context) {
        if (firstSurname.equals("")) {
            return context.getString(R.string.no_patient_name);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(firstSurname);
        if (!lastSurname.equals("")) sb.append(" ").append(lastSurname);
        if (!firstName.equals("")) sb.append(", ").append(firstName);
        if (!middleName.equals("")) sb.append(" ").append(middleName);
        return sb.toString();
    }

    protected List<Detail> getPersonDetails(Context context) {
        ArrayList<Detail> details = new ArrayList<>();

        // medical ID
        final StringNumberDetail medicalIdDetail = new StringNumberDetail(
                context.getResources().getString(R.string.label_medicalID),
                context.getResources().getString(R.string.hint_medicalID),
                this.medicalID,
                context);
        medicalIdDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setMedicalID(medicalIdDetail.getValue());
            }
        });
        details.add(medicalIdDetail);

        // first name
        final StringDetail firstNameDetail = new StringDetail(
                context.getResources().getString(R.string.label_firstname),
                context.getResources().getString(R.string.hint_firstname),
                this.firstName,
                context);
        firstNameDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setFirstName(firstNameDetail.getValue());
            }
        });
        details.add(firstNameDetail);

        // patient middle name
        final StringDetail middleNameDetail = new StringDetail(
                context.getResources().getString(R.string.label_middlename),
                context.getResources().getString(R.string.hint_middlename),
                this.middleName,
                context);
        middleNameDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setMiddleName(middleNameDetail.getValue());
            }
        });
        details.add(middleNameDetail);

        // first surname
        final StringDetail firstSurnameDetail = new StringDetail(
                context.getResources().getString(R.string.label_first_surname),
                context.getResources().getString(R.string.hint_first_surname),
                this.firstSurname,
                context);
        firstSurnameDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setFirstSurname(firstSurnameDetail.getValue());
            }
        });
        details.add(firstSurnameDetail);

        // last surname
        final StringDetail lastSurnameDetail = new StringDetail(
                context.getResources().getString(R.string.label_last_surname),
                context.getResources().getString(R.string.hint_last_surname),
                this.lastSurname,
                context);
        lastSurnameDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setLastSurname(lastSurnameDetail.getValue());
            }
        });
        details.add(lastSurnameDetail);

        // patient sex
        final SexDetail sexDetail = new SexDetail(
                context.getResources().getString(R.string.label_sex),
                context.getResources().getString(R.string.hint_sex),
                this.sex,
                context);
        sexDetail.setSetter(new Runnable() {
            @Override
            public void run() {
                setSex(sexDetail.getValue());
            }
        });
        details.add(sexDetail);

        return details;
    }

    public abstract List<Detail> getDetails(Context context);

}
