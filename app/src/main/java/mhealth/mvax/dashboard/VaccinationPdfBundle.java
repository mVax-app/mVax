package mhealth.mvax.dashboard;

import java.util.ArrayList;
import java.util.Map;

import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.record.Patient;

/**
 * @author Matthew Tribby
 *         This class acts as an intermediate data carrier to be passed into the PDF Builders to provide a common interface
 */

public class VaccinationPdfBundle {
    //Maps the Patient Object to the Vaccinations they have received
    private Map<Patient, ArrayList<Vaccination>> vaccinations;
    //Maps each Vaccination Object to the form code associated with it
    private Map<Vaccination, String> doseFormCodes;

    VaccinationPdfBundle(Map<Patient, ArrayList<Vaccination>> vaccinations, Map<Vaccination, String> doseFormCodes){
        this.vaccinations = vaccinations;
        this.doseFormCodes = doseFormCodes;
    }



}
