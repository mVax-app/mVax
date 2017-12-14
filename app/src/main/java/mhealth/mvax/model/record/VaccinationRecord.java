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
 * Created by mtribby on 11/26/17.
 */

public class VaccinationRecord implements Serializable {
    //date should be in yyyymmdd and is used that way internally
   // private String date;
    private String type;
    private String patientUID;

    public VaccinationRecord(){
        //Required empty public constructor, should not be used internally
    }

    public VaccinationRecord(String type, String patientUID){
        this.type = type;
        this.patientUID = patientUID;
    }


    /**
     * Returns the vaccine type as a String.
     * @return vaccine type
     */
    public String getType(){
        return type;
    }

    /**
     * Sets the vaccine type
     * @param type
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Returns the uid of the patient who received the vaccine
     * @return uid
     */
    public String getPatientUID(){
        return patientUID;
    }

    /**
     * Sets the paitent uid instance variable
     * @param uid String
     */
    public void setPatientUID(String uid){
        this.patientUID = uid;
    }

}
