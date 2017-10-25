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

    private Vaccine _vaccine;

    private Dose _dose;

    //================================================================================
    // Constructors
    //================================================================================

    public DoseDateView(Context context, Vaccine vaccine, Dose dose) {
        super(context);
        _vaccine = vaccine;
        _dose = dose;
    }

    //================================================================================
    // Getters
    //================================================================================

    public Vaccine getVaccine() {
        return _vaccine;
    }

    public Dose getDose() {
        return _dose;
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setVaccine(Vaccine vaccine) {
        _vaccine = vaccine;
    }

    public void setDose(Dose dose) {
        _dose = dose;
    }
}
