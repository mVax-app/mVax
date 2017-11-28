package mhealth.mvax.records.views;

import android.content.Context;

import mhealth.mvax.model.record.Dose;
import mhealth.mvax.model.record.Vaccine;

/**
 * @author Robert Steilberg
 *
 * Adds functionality to TextView for storing Vaccine and Dose objects
 */

public class VaccineDateView extends android.support.v7.widget.AppCompatTextView {

    //================================================================================
    // Properties
    //================================================================================

    private Vaccine mVaccine;

    //================================================================================
    // Constructors
    //================================================================================

    public VaccineDateView(Context context, Vaccine vaccine) {
        super(context);
        mVaccine = vaccine;
    }

    //================================================================================
    // Getters
    //================================================================================

    public Vaccine getVaccine() {
        return mVaccine;
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setVaccine(Vaccine vaccine) {
        mVaccine = vaccine;
    }

}
