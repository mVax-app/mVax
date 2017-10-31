package mhealth.mvax.record.vaccine;

import android.content.Context;

/**
 * @author Robert Steilberg
 *
 * Adds functionality to TextView for storing Vaccine and Dose objects
 */

public class DoseDateView extends android.support.v7.widget.AppCompatTextView {

    //================================================================================
    // Properties
    //================================================================================

    private Vaccine mVaccine;

    private Dose mDose;

    //================================================================================
    // Constructors
    //================================================================================

    public DoseDateView(Context context, Vaccine vaccine, Dose dose) {
        super(context);
        mVaccine = vaccine;
        mDose = dose;
    }

    //================================================================================
    // Getters
    //================================================================================

    public Vaccine getVaccine() {
        return mVaccine;
    }

    public Dose getDose() {
        return mDose;
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setVaccine(Vaccine vaccine) {
        mVaccine = vaccine;
    }

    public void setDose(Dose dose) {
        mDose = dose;
    }
}
