package mhealth.mvax.model;

import android.content.Context;
import android.util.Pair;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import mhealth.mvax.search.RecordDateFormat;

/**
 * @author Robert Steilberg
 *         <p>
 *         Object for storing information about mVax records
 *         <p>
 *         PLEASE READ DOCUMENTATION BEFORE ADDING, REMOVING,
 *         OR MODIFYING PROPERTIES
 */

// TODO make vaccine list ordered

public class Record implements Serializable {

    //================================================================================
    // Constructors
    //================================================================================

    /**
     * Default Firebase constructor; should not
     * be used internally
     */
    public Record() {
        initVaccineHistory();
    }

    public Record(String databaseId) {
        mDatabaseId = databaseId;
        initVaccineHistory();
    }


    //================================================================================
    // Properties
    //================================================================================

    /**
     * Unique ID assigned to the object by Firebase
     */
    private String mDatabaseId;

    public String getDatabaseId() {
        return this.mDatabaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.mDatabaseId = databaseId;
    }

    /**
     * 13-digit patient integer ID,
     * given to each registered Honduran citizen
     */
    private String mId;

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    /**
     * Patient's first name
     */
    private String mFirstName;

    public String getFirstName() {
        return this.mFirstName;
    }

    public void setFirstName(String name) {
        this.mFirstName = name;
    }

    /**
     * Patient's middle name
     */
    private String mMiddleName;

    public String getMiddleName() {
        return this.mMiddleName;
    }

    public void setMiddleName(String name) {
        this.mMiddleName = name;
    }

    /**
     * Patient's surname
     */
    private String mLastName;

    public String getLastName() {
        return this.mLastName;
    }

    public void setLastName(String name) {
        this.mLastName = name;
    }

    /**
     * Patient's suffix
     */
    private String mSuffix;

    public String getSuffix() {
        return this.mSuffix;
    }

    public void setSuffix(String suffix) {
        this.mSuffix = suffix;
    }

    /**
     * Patient's biological sex
     */
    private Sex mSex;

    public Sex getSex() {
        return this.mSex;
    }

    public void setSex(Sex sex) {
        this.mSex = sex;
    }

    /**
     * Patient's date of birth, expressed in
     * milliseconds since Unix epoch
     */
    private Long mDOB;

    public Long getDOB() {
        return this.mDOB;
    }

    public void setDOB(Long dateInMillis) {
        this.mDOB = dateInMillis;
    }

    /**
     * Patient's place of birth
     */
    private String mPlaceOfBirth;

    public String getPlaceOfBirth() {
        return this.mPlaceOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.mPlaceOfBirth = placeOfBirth;
    }

    /**
     * Patient's community of residence
     */
    private String mCommunity;

    public String getCommunity() {
        return this.mCommunity;
    }

    public void setCommunity(String community) {
        this.mCommunity = community;
    }


    /**
     * Parent first name
     */
    private String mParentFirstName;

    public String getParentFirstName() {
        return this.mParentFirstName;
    }

    public void setParentFirstName(String name) {
        this.mParentFirstName = name;
    }

    /**
     * Patient's middle name
     */
    private String mParentMiddleName;

    public String getParentMiddleName() {
        return this.mParentMiddleName;
    }

    public void setParentMiddleName(String name) {
        this.mParentMiddleName = name;
    }

    /**
     * Parent surname
     */
    private String mParentLastName;

    public String getParentLastName() {
        return this.mParentLastName;
    }

    public void setParentLastName(String name) {
        this.mParentLastName = name;
    }

    /**
     * Parent suffix
     */
    private String mParentSuffix;

    public String getParentSuffix() {
        return this.mParentSuffix;
    }

    public void setParentSuffix(String suffix) {
        this.mParentSuffix = suffix;
    }

    /**
     * Parent's biological sex
     */
    private Sex mParentSex;

    public Sex getParentSex() {
        return this.mParentSex;
    }

    public void setParentSex(Sex sex) {
        this.mParentSex = sex;
    }

    /**
     * 13-digit patient integer ID,
     * given to each registered Honduran citizen
     */
    private String mParentId;

    public String getParentId() {
        return this.mParentId;
    }

    public void setParentId(String id) {
        this.mParentId = id;
    }

    /**
     * Number of dependents, including patient
     */
    private Integer mNumDependents;

    public Integer getNumDependents() {
        return this.mNumDependents;
    }

    public void setNumDependents(Integer numDependents) {
        this.mNumDependents = numDependents;
    }

    /**
     * Parent residential address
     */
    private String mParentAddress;

    public String getParentAddress() {
        return this.mParentAddress;
    }

    public void setParentAddress(String address) {
        this.mParentAddress = address;
    }

    /**
     * Parent phone number
     */
    private String mParentPhone;

    public String getParentPhone() {
        return this.mParentPhone;
    }

    public void setParentPhone(String phoneNumber) {
        this.mParentPhone = phoneNumber;
    }

    /**
     * Map containing the record's vaccine history
     */
    private ArrayList<Vaccine> mVaccines;

    /**
     * This getter should only be used externally by Firebase;
     * use getVaccineList to get the record's vaccine history
     */
    public ArrayList<Vaccine> getVaccines() {
        return this.mVaccines;
    }

    /**
     * This setter should only be used externally by Firebase;
     * use addVaccine or updateVaccine to modify associated Vaccines
     */
    public void setVaccines(ArrayList<Vaccine> vaccines) {
        this.mVaccines = vaccines;
    }


    //================================================================================
    // Computed getters
    //================================================================================

    @Exclude
    public String getFullName() {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(mLastName);
        nameBuilder.append(", ");
        nameBuilder.append(mFirstName);
        if (mMiddleName != null) {
            nameBuilder.append(" ");
            nameBuilder.append(mMiddleName);
        }
        if (mSuffix != null) {
            nameBuilder.append(", ");
            nameBuilder.append(mSuffix);
        }
        return nameBuilder.toString();
    }

    @Exclude
    public String getParentFullName() {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(mParentLastName);
        nameBuilder.append(", ");
        nameBuilder.append(mParentFirstName);
        if (mParentMiddleName != null) {
            nameBuilder.append(" ");
            nameBuilder.append(mParentMiddleName);
        }
        if (mParentSuffix != null) {
            nameBuilder.append(", ");
            nameBuilder.append(mParentSuffix);
        }
        return nameBuilder.toString();
    }

    @Exclude
    public LinkedHashMap<String, ArrayList<Pair<String, String>>> getSectionedAttributes(Context context) {

        ArrayList<Pair<String, String>> childAttributes = new ArrayList<>();
        ArrayList<Pair<String, String>> parentAttributes = new ArrayList<>();

        // Patient info
        childAttributes.add(new Pair<>("ID", this.mId));
        childAttributes.add(new Pair<>("First name", this.mFirstName));
        childAttributes.add(new Pair<>("Middle name", this.mMiddleName));
        childAttributes.add(new Pair<>("Last name", this.mLastName));
        childAttributes.add(new Pair<>("Suffix", this.mSuffix));

        String sexString = "";
        if (this.mSex != null) sexString = context.getString(this.mSex.getResourceId());
        childAttributes.add(new Pair<>("Sex", sexString));


        RecordDateFormat dateFormat = new RecordDateFormat("MM/dd/yyyy");


        childAttributes.add(new Pair<>("Date of Birth", dateFormat.getString(this.mDOB)));

        childAttributes.add(new Pair<>("Place of Birth", this.mPlaceOfBirth));
        childAttributes.add(new Pair<>("Community", this.mCommunity));

        // Parent info
        parentAttributes.add(new Pair<>("ID", this.mParentId));
        parentAttributes.add(new Pair<>("First name", this.mParentFirstName));
        parentAttributes.add(new Pair<>("Middle name", this.mParentMiddleName));
        parentAttributes.add(new Pair<>("Last name", this.mParentLastName));
        parentAttributes.add(new Pair<>("Suffix", this.mParentSuffix));


        String parentSexString = "";
        if (this.mParentSex != null) parentSexString = context.getString(this.mParentSex.getResourceId());
        childAttributes.add(new Pair<>("Sex", parentSexString));

        String numDependents = this.mNumDependents != null ? this.mNumDependents.toString() : "";
        parentAttributes.add(new Pair<>("Number of dependents", numDependents));
        parentAttributes.add(new Pair<>("Address", this.mParentAddress));
        parentAttributes.add(new Pair<>("Phone number", this.mParentPhone));

        LinkedHashMap<String, ArrayList<Pair<String, String>>> sectionedAttributes = new LinkedHashMap<>();
        sectionedAttributes.put("Patient Information", childAttributes);
        sectionedAttributes.put("Guardian Information", parentAttributes);

        return sectionedAttributes;
    }


    //================================================================================
    // Private methods
    //================================================================================

    private void initVaccineHistory() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        mVaccines = new ArrayList<>();

        db.child("vaccineMaster").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                mVaccines.add(vaccine);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
}
