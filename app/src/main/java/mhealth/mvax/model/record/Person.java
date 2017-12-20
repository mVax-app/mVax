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

import java.io.Serializable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Data structure representing the basic info for
 *         a person, which can be any person represented
 *         in the database
 */
public abstract class Person implements Serializable {

    public Person() {
    }

    /**
     * Unique Firebase key generated through push()
     */
    private String databaseKey;

    public String getDatabaseKey() {
        return this.databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Medical ID of the person
     */
    private String medicalID;

    public String getMedicalID() {
        return this.medicalID;
    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
    }

    /**
     * First name
     */
    private String firstName;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Middle name
     */
    private String middleName;

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * First printed surname
     */
    private String firstSurname;

    public String getFirstSurname() {
        return this.firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    /**
     * Last printed surname
     */
    private String lastSurname;

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

    public String getName() {
        StringBuilder sb = new StringBuilder;
        sb.append(firstSurname);
        if (lastSurname.equals(""))
        sb.append(lastSurname);
    }
}
