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

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Robert Steilberg
 *         <p>
 *         Object for storing information about mVax vaccines;
 *         sorts by vaccine name, implements Serializable
 *         so that it can be bassed as a Bundle argument to fragments
 */

public class Vaccine implements Serializable, Comparable<Vaccine> {

    //================================================================================
    // Constructors
    //================================================================================

    private Vaccine() {
        // Firebase constructor
    }

    public Vaccine(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    //================================================================================
    // Properties
    //================================================================================

    /**
     * Unique Firebase database key representing the Vaccine
     */
    private String databaseKey;

    public String getDatabaseKey() {
        return this.databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Vaccine name
     */
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Vaccine target administered count
     */
    private Integer targetCount;

    public Integer getTargetCount() {
        return this.targetCount;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    /**
     * Vaccine total administered count
     */
    private Integer givenCount;

    public Integer getGivenCount() {
        return this.givenCount;
    }

    public void setGivenCount(Integer givenCount) {
        this.givenCount = givenCount;
    }

    /**
     * Array containing the vaccine's doses, each
     * represented by its unique Firebase database key
     */
    private List<Dose> doses = new ArrayList<>();

    public ArrayList<Dose> getDoses() {
        return new ArrayList<>(this.doses);
    }

    public void setDoses(List<Dose> doses) {
        this.doses = doses;
    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Adds new doses to the vaccine's associated doses
     *
     * @param doses array of new doses to add
     */
    public void addDoses(Dose... doses) {
        Collections.addAll(this.doses, doses);
    }

    /**
     * Sorts Vaccines by name
     * // TODO implement compareTo for multiple properties
     *
     * @param that Vaccine to sort against
     * @return negative integer, zero, or a positive integer if this
     * Vaccine is less than, equal to, or greater than that Vaccine
     */
    @Override
    public int compareTo(@NonNull Vaccine that) {
        return this.name.compareTo(that.name);
    }

}
