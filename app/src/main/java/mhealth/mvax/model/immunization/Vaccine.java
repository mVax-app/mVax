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
package mhealth.mvax.model.immunization;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Robert Steilberg
 * <p>
 * Stores information about vaccines; sorts by vaccine name
 */
public class Vaccine implements Serializable, Comparable<Vaccine> {

    private Vaccine() {
        // Firebase POJO constructor
    }

    public Vaccine(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    /**
     * Unique Firebase database key of the Vaccine object
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
    private String name = "";

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Vaccine target administered count
     */
    private Integer targetCount = 0;

    public Integer getTargetCount() {
        return this.targetCount;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    /**
     * Vaccine total administered count
     */
    private Integer givenCount = 0;

    public Integer getGivenCount() {
        return this.givenCount;
    }

    public void setGivenCount(Integer givenCount) {
        this.givenCount = givenCount;
    }

    /**
     * Array containing the vaccine's doses, each
     * represented by the dose's unique Firebase database
     * key
     */
    private List<Dose> doses = new ArrayList<>();

    public ArrayList<Dose> getDoses() {
        return new ArrayList<>(this.doses);
    }

    public void setDoses(List<Dose> doses) {
        this.doses = doses;
    }

    /**
     * Adds new doses to the vaccine's associated doses
     *
     * @param doses array of new doses to add
     */
    public void addDoses(Dose... doses) {
        Collections.addAll(this.doses, doses);
    }

    /**
     * Determines how the vaccine is sorted
     */
    private Integer sortOrder;

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Sorts Vaccines by sortOrder
     *
     * @param that Vaccine to sort against
     * @return negative integer, zero, or a positive integer if this
     * Vaccine is less than, equal to, or greater than that Vaccine
     */
    @Override
    public int compareTo(@NonNull Vaccine that) {
        return this.sortOrder.compareTo(that.sortOrder);
    }

    /**
     * Increment a vaccine's given count by one
     */
    @Exclude
    public void incrementGivenCount() {
        givenCount++;
    }

    /**
     * Decrement a vaccine's given count by one
     */
    @Exclude
    public void decrementGivenCount() {
        givenCount--;
    }

}
