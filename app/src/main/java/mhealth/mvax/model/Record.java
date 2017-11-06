package mhealth.mvax.model;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import mhealth.mvax.R;
import mhealth.mvax.search.RecordDateFormat;
import mhealth.mvax.search.Tuple;

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
    private String mNumDependents;

    public String getNumDependents() {
        return this.mNumDependents;
    }

    public void setNumDependents(String numDependents) {
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


    // TODO probably get this out of this class
    @Exclude
    public LinkedHashMap<String, ArrayList<Detail>> getSectionedAttributes(Context context, LayoutInflater inflater) {

        // PATIENT SECTION ==================================================================

        ArrayList<Detail> childAttributes = new ArrayList<>();

        // patient ID
        final StringNumberDetail id = new StringNumberDetail(
                context.getResources().getString(R.string.label_id),
                context.getResources().getString(R.string.hint_id),
                this.mId,
                context);
        id.setSetter(new Runnable() {
            @Override
            public void run() {
                setId(id.getValue());
            }
        });
        childAttributes.add(id);

        // patient first name
        final StringDetail firstName = new StringDetail(
                context.getResources().getString(R.string.label_firstname),
                context.getResources().getString(R.string.hint_firstname),
                this.mFirstName,
                context);
        firstName.setSetter(new Runnable() {
            @Override
            public void run() {
                setFirstName(firstName.getValue());
            }
        });
        childAttributes.add(firstName);


        // patient middle name
        // patient last name
        // patient suffix
        // patient sex

        final SexDetail sex = new SexDetail(
                context.getResources().getString(R.string.label_sex),
                context.getResources().getString(R.string.hint_sex),
                this.mSex,
                context);
        sex.setSetter(new Runnable() {
            @Override
            public void run() {
                setSex(sex.getValue());
            }
        });
        childAttributes.add(sex);

        // patient DOB
        final DateDetail DOB = new DateDetail(
                context.getResources().getString(R.string.label_dob),
                context.getResources().getString(R.string.hint_dob),
                this.mDOB,
                context);
        DOB.setSetter(new Runnable() {
            @Override
            public void run() {
                setDOB(DOB.getValue());
            }
        });
        childAttributes.add(DOB);

        // patient place of birth
        // patient community


        // GUARDIAN SECTION =================================================================

        ArrayList<Detail> parentAttributes = new ArrayList<>();

        // guardian ID
        // guardian first name
        // guardian middle name
        // guardian last name
        // guardian suffix
        // guardian sex
        // guardian number dependents

        final StringNumberDetail numDependents = new StringNumberDetail(
                context.getResources().getString(R.string.label_numDependents),
                context.getResources().getString(R.string.hint_numDependents),
                this.mNumDependents,
                context);
        numDependents.setSetter(new Runnable() {
            @Override
            public void run() {
                setNumDependents(numDependents.getValue());
            }
        });
        parentAttributes.add(numDependents);

        // guardian address
        // guardian phone number

        LinkedHashMap<String, ArrayList<Detail>> sectionedAttributes = new LinkedHashMap<>();
        sectionedAttributes.put(context.getString(R.string.patient_detail_section_title), childAttributes);
        sectionedAttributes.put(context.getString(R.string.guardian_detail_section_title), parentAttributes);

        return sectionedAttributes;
    }


    //================================================================================
    // Private methods
    //================================================================================

    private void initVaccineHistory() {
        // TODO we don't really want this in this class or take in a context
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        mVaccines = new ArrayList<>();

        db.child("vaccineMaster").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                if (mVaccines.size() < 6) {
                    mVaccines.add(vaccine);
                }
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
