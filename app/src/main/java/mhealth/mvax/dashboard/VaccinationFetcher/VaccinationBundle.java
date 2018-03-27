package mhealth.mvax.dashboard.VaccinationFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.record.Patient;

/**
 * @author Matthew Tribby
 *         This class acts as an intermediate data carrier to be passed into the PDF Builders to provide a common interface
 */

public class VaccinationBundle {
    //Maps the Patient Object to the Vaccinations they have received
    private Map<Patient, List<Vaccination>> vaccinations;
    private Map<Vaccination, String> vaccinationFormCodes;
    private Map<Patient, List<String>> patientFormCodes;

    VaccinationBundle(Map<Patient, List<Vaccination>> vaccinations, Map<Vaccination, String> vaccFormCodes){
        this.vaccinations = vaccinations;
        this.vaccinationFormCodes = vaccFormCodes;
        combineTwoMaps();
    }

    public Map<Patient, List<Vaccination>> getPatientVaccinations(){
        return vaccinations;
    }

    public Map<Vaccination, String> getDoseFormCodes(){
        return vaccinationFormCodes;
    }

    public Map<Patient,List<String>> getPatientFormCodes(){
        return patientFormCodes;
    }

    private void combineTwoMaps(){
        if(vaccinations != null && vaccinationFormCodes != null){
            patientFormCodes = new HashMap<>();
            for(Patient patient : vaccinations.keySet()) {
                List<String> formCodes = new ArrayList<>();
                List<Vaccination> vaccinationsPerPatient = vaccinations.get(patient);
                for (Vaccination vacc : vaccinationsPerPatient) {
                    formCodes.add(vaccinationFormCodes.get(vacc));
                }

                patientFormCodes.put(patient, formCodes);
            }
        }
    }





}
