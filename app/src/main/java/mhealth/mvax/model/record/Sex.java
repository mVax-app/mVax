package mhealth.mvax.model.record;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 *         <p>
 *         Enum for storing supported sexes
 */

public enum Sex {
    MALE(R.string.male_enum),
    FEMALE(R.string.female_enum);

    private int mResourceId;

    Sex(int resourceId) {
        this.mResourceId = resourceId;
    }

    /**
     * @return id used to find the string value of the Sex
     */
    public int getResourceId() {
        return this.mResourceId;
    }

}