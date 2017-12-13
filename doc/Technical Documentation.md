# mVax Technical Documentation

## Contents

* Basics
* Java Model Classes
* Database Schema
* Database Rules
* Internationalizatiob
* Current Project Dependencies
* External Tablet Dependencies
* Specific App Technical Processes
    * Log-in Process
    * User Approval Process
    * Record and Immunization Render Process
    * Record Creation Process
    * Record Editing Process
    * Record Deletion Process
    * Immunization Record Process
    * Searching for Patient Records
    * Exporting of PDFs
    * Overdue Patients

### Basics

mVax is an Android mobile application for managing a medical records database that stores immunization records for Clínica Esperanza in Roatán, Honduras.

mVax is configured to use Firebase for storing and retrieving data. The application supports concurrent creation and editing of record data, macro visualization of immunization progress and goals, multiple language support, and full functionality when connection to the Firebase server is not possible.

Currently, this repository is configured to point to the Firebase instance for Clínica Esperanza.


### Java Model Classes

This application has been built with a Firebase backend. Firebase is a NoSQL database and as such we are able to utilize Java classes as our model and then simply push instances of those models to Firebase. Using the built in JSON-to-POJO deserializer, Firebase automatically generates objects from the data downloaded from the database. Our project currently has the following model classes:

* Model classes for patient records
    * Dose: contains labels for describing doses of vaccines
    * Record: represents the personal information (i.e. name, address) of the patients
    * Sex: an enum which limits gender inputs to MALE and FEMALE
    * Vaccine: describes a vaccine and its needed doses. This object is not meant to represent an administration instance of a vaccine but rather the unchanging attribute of a vaccine common to all patients (i.e. number of doses)
    * VaccinationRecord: used to  represent an administration of a vaccine. For example, if a Polio vaccine was given to a child on today’s date, that information would be represented in this model class.
    
* Model classes for mVax user accounts
    * User: This class represents the information about a user that needs to be stored in the database (currently just firstName, lastName, email, and role)
    * UserRequest: When a prospective user makes a request to gain access to the app, a UserRequest object should be filled which contains basic information to display to administrators when they consider approving them. 
    * UserRole: This is an enum class which holds all the possible user roles / access levels a user can have. Currently there are only two roles (_admins_, users that can read/write anywhere and have admin privileges, and _readers_, users that can just see data)


### Database Schema
* mVax
    * records
        * databaseID (String, primary key, used for lookup)
            * databaseID (String, uniquely generated via Firebase `push()`)
            * id (String, 13-digit Honduran ID)
            * firstName (String)
            * middleName (String)
            * lastName (String)
            * suffix (String)
            * sex (Sex enum)
            * DOB (Long, milliseconds since Unix epoch)
            * placeOfBirth (String)
            * community (String)
            * parentFirstName (String)
            * parentMiddleName (String)
            * parentLastName (String)
            * parentSuffix (String)
            * parentSex (Sex enum)
            * parentId (String, 13-digit Honduran ID)
            * numDependents (String)
            * parentAddress (String)
            * phoneNumber (String)
            * vaccines (List<Vaccine>)
                * databaseKey (String, refers to vaccine in master list)
                * name (String)
                * dueDate (Long, milliseconds since Unix epoch)
                * Doses(List<Dose>)
                    * label1 (String)
                    * label2 (String)
                    * dateCompleted (Long, milliseconds since Unix epoch)
* vaccinations (refers to the records for actual vaccines administered)
    * date (String, stored yyyymmdd)
        * ID (20-long alphanumeric string)
            * patientID (String)
            * type (String, refers to which vaccine was administered)
* vaccines (master list of all supported vaccines)
    * databaseID (String, primary key, used for lookup)
        * databaseID (String, uniquely generated via Firebase `push()`)
        * name (String)
        * targetCount (Integer)
        * givenCount (Integer)
        * doses (List<Dose>)
            * label1 (String)
            * label2 (String)
            * timeUntilNextDose (Long, milliseconds since Unix epoch)
* Users (users approved by administrator that have access to app)
    * ID (String, primary key, used for lookup)
        * email (String)
        * firstName (String)
        * lastName (String)
        * role (Role enum)
* userRequests (requests from prospective users for access)
    * ID (String, primary key, used for lookup)
        * email (String)
        * firstName (String)
        * lastName (String)
        * role (Role enum)
        * uid (String)

### Database Rules

##### Patient records (both patient demographic and vaccination history):
* Read: Anyone who can successfully log into app
* Write: Only _admin_ accounts, _readers_ cannot write logically

##### Users:
* Read: _admin_ only because only _admins_ can see who else is a user
* Write: _admin_ only because only _admins_ can change who has access

##### User Requests:
* Read: _admin_ because only _admins_ can evaluate if a user should be approved or not
* Write: Anyone, so users who are not yet registered can request to be registered


### Internationalization
Internationalization of the app currently implemented by utilizing Android locales. Essentially what this means is that the resource files directory changes when the locale is switched. So in the file structure there is a `values` folder and a `values-es` folder in `res`. The `values` folder is the default and also the English version while the `values-es` folder holds Spanish-specific resources (i.e. the `strings.xml` file provides the Spanish translation of the English string with the same resource key being used as in the English `strings.xml`). Important to note that if a resource does not exist in a foreign resource folder (i.e. `values-es`), it default sback to the `regular` values folder.

### Project Dependencies
* Firebase-auth:11.6.0
* Firebase-database:11.6.0 
* Google play-services-auth:11.6.0
* Firebase-invites:11.6.0
* Joda-time:joda-time:2.9.9
* libs/itext5-itextpdf-5.5.12.jar

For testing: 
* Junit:junit:4.12
* org.hamcrest:hamcrest-library:1.3

Additional Android dependencies: 
* Android.support:support-v4:26.1.0
* Android.support:appcompat-v7:26.1.0
* Android.support:design:26.1.0
* Android.support.constraint:constraint-layout:1.02

### External Tablet Dependencies

##### Gmail Email Chooser

In order to export forms via email, the device needs to have an email chooser set up. This can easily be done by setting up Gmail access on your device. The Gmail export option will then automatically be available.


### Specific App Technical Processes

#### 1. Log-in Process

This application leverages the email/password authentication that is made available through Firebase. In addition to having to pass the Firebase Authentication process, users must also be registered as approved users in our database in order to successfully gain access to the application and the data stored in the database. 

##### Technical steps

1. After the fields are validated and inputted, two things have to happen in order for the app to continue into the MainActivity:
    * Firebase Authentication must pass
    * Record must exist in the UserTable
    
#### 2. User Approval Process

The summary of the non-technical process goes as follows: a prospective user applies to gain access and that request is sent out. Then anyone registered as an administrator can go into their own account and under administrator privileges decide whether to accept or deny the request.

##### Technical steps

1. User fills out the registration form and clicks on submit. This triggers a new UserRequest instance to be written into the userRequest table (see data model section above for more information).  
2. Then when an administrator goes to the ApproveUsers page, the page is populated with all the current instances in the UserRequest table (uses the UserRequestAdapter for the ListView).
3. Then when the user makes a decision the request,
    * if accepted, is deleted but before it is the data is collected so that a new User object can be created. This User object is then used to push a new User object to the database.
    * if denied, is simply deleted, no access to log-in
    
#### 3. Record and Immunization Render Process

The demographic data associated with each Record object is rendered in a ListView that displays the label for each field along with its value. Vaccines and their doses are rendered in LinearLayouts according to the `vaccineList` stored within each Record. A `DualTabPager` is used to manage two separate tabs that respectively represent the Record fields and immunization dates, allowing the user to swipe between views or tap each tab.

##### Technical steps

1. The user taps a particular record on the Patients page, triggering a fragment transition to the `DetailFragment`.
2. A `DualTabPager` instantiates and renders a `PatientDataTab` and `VaccineScheduleTab` that respectively represent the record demographics and its vaccine schedule.
    * Record fields and their values are rendered within TextViews in a scrollable ListView that is not editable. An edit button and delete button are included at the head of the ListView to provide editing and deletion functionality with respect to the current record.
    * The vaccine schedule is dynamically generated from the `vaccineList` stored within the current record. A ListView contains a row representing each `Vaccine` object, and that row contains a LinearLayout that holds the vaccines due date and all of its associated doses.
        * Each `Dose` object has a label that is used to differentiate between different doses.
        * Each `Dose` object has a `DoseDateView` that displays the `dateCompleted` field from the `Dose` object, representing when a particular dose was administered.
            * Each `DoseDateView` has an associated listener that triggers the Immunization Recording Process, described below in section 7.

#### 4. Record Creation Process

Record creation is triggered by tapping the “Create New Record” button. This will generate a new Record object, populate it with vaccines, and set it to the database. The app then displays a ListView that allows the user to edit the supported Record fields (such as demographic and parent information). Once done, the user taps the “Save” button, which saves the record and then presents it to the user for viewing.

##### Technical steps

1. The “Create New Record” button is tapped by the user, triggering the new record creation process
2. A new Record object is created that will store all of the information about a patient record, including demographic info, parent info, and vaccines.
    * The Record’s list of vaccines and doses is created by copying the current master vaccine list, which is defined in the `mVax` table as `vaccineMaster`. The `vaccineMaster` table defines all of the vaccines and doses currently supported by that mVax instance.
3. After creating the new Record object, the `EditPatientDataAdapter` is populated and used to generate a ListView that allows editing of the various Record fields. These fields are locally auto-saved to the Record object on each edit completion via listeners that tie back into each setter defined in the Record model.
    * Each field is represented by a `Detail` object that stores a specific data type (for example, a String for `firstName`) and the setter used to set a new value for that datatype in the `Record` object.
4. The user adds the fields they want and taps the save button, which sets the record to the database, making it available for any device running mVax to view. The record is not set to the database unless the save button is tapped in case the user decides not to add the record.
5. mVax then transitions to view mode, allowing the user to view the record fields and also the associated vaccines and doses.

#### 5. Record Editing Process

Record editing is triggered by tapping the “Edit Record” button that is shown when viewing a record. The route taken to edit a record is similar to that of creating a new record in that mVax will display a ListView allowing the user to edit supported Record fields. Any changes made will be set to the database only after the user has tapped the save button.

##### Technical steps
 1. The “Edit Record” button is tapped while viewing an existing mVax Record, triggering the edit record process.
2. mVax populates the `EditPatientDataAdapter` with the values from the chosen Record object. This adapter is used to generate a ListView that allows editing of the various Record fields. These fields are locally auto-saved to the Record object on each edit completion via listeners that to back into each setter defined in the Record model.
    * Each field is represented by a `Detail` object that stores a specific data type (for example, a String for `firstName`) and the setter used to set a new value for that datatype in the `Record` object.
3. The user adds the fields they want and taps teh save button, which sets the record to the database, making it available for any device running mVax to view.  The record is not set to the database unless the save button is tapped in case the user decides not to add the record.
4. mVax then transitions back to the view mode for that record, allowing the user to view the record fields and also the associated vaccines and doses.

#### 6. Record Deletion Process

Record deletion is triggered by tapping the “Delete Record” button that is shown when viewing a record. Once the deletion process has been triggered, mVax simply sets a value of `null` to the `databaseKey` associated with the current record, which is the documented way of deleting data in Firebase.

##### Technical steps
1. User tabs the “Delete Record” button.
2. `null` is set to the `databaseKey` of the current record.

#### 7. Immunization Recording Process

Setting a date for an immunization is triggered by tapping one of the `DoseDateView` objects, which are represented as gray boxes next to the label for each dose. Tapping a `DoseDateView` triggers a calendar modal that allows the user to set a date representing the date on which that dose was administered to the patient represented by the current record. Once the date is confirmed, that date is saved to the associated dose and updated in the view, showing a date of completion.

##### Technical steps

1. User taps a `DoseDateView` field next to the label for a particular dose within a particular vaccine.
2. A modal with a calendar is displayed, allowing the user to choose a date of completion for the dose or to remove an existing date of completion.
3. Upon confirmation, the date selected by the user is stored in the `dateCompleted` field of the dose as milliseconds since Unix epoch. The entire Record object is then set to the database, which includes the Record’s `vaccineList` and thus the newly updated dose.

#### 8. Searching for Patient Records

A simple search algorithm was implemented to support the most basic version of this feature. The big-picture idea of how it works is an O(n) scan through a collection of all patient records, filtering out any records that does not contain some substring (i.e. the string typed into the search box). What is then returned are all records that contain the search query in the appropriate field that the user has decided to filter by.

##### Technical Steps:

1. User can select a filter from the dropdown menu, which contains a number of patient information fields (such as patient ID, patient community, parent name, etc). If the user does not select a filter, the default is “patient name”. 
2. Once a filter is selected and the page is loaded, a full list of the patient records is loaded. A for loop is used to go through this list of patients. 
3. The search bar is where the user would enter his/her query. There is a listener attached to this text box, so that each appended character or deleted character will trigger a re-filtering of the patients. 
4. The filter is used to determine which field in the patient information is used to check for the search query. For each patient, given the filter that picked out a specific patient information field, we check to see if the search query is contained within that string.
		
#### 9. Exporting of PDFs 

In order to auto-fill pdfs, our application utilizes iText, a pdf manipulation software. Below are the technical steps that allows for the exportation of an auto-filled pdf form with a specific date inputted.

##### Technical steps

1. After a date is selected for a specific form, that date is sent to a Builder class (i.e. SinovaBuilder or Sinova2Builder). In the future, these classes will be refactored to utilize the strategy design pattern. It are these builder classes which are responsible for building the pdfs
2. Next, the pdf template is fetched from the assets folder (app_vaccination > app > src > main > assets). The pdf template for the file is a blank pdf form that has fields set in it. All of these fields in the pdf have titles. Using iText, one can fetch the field by its name and place a value inside of it. Our pdf-autofill process leverages that to fill in the Firebase data. 
3. For the day that is selected, the Firebase database is queried for all vaccination records from that day (from the vaccinations table in the data model). 
4. Before patient data, the pdf header is auto-filled in which contains information not-specific to patients (i.e. the date, location). 
5. After that, for each record, a new row is added to the pdf and filled in for each record that was collected from the Firebase query
6. After all the rows have been added, the pdf writing process is closed and the file is stored in external file directory
7. Next the file is fetched from the external file directory and added as an attachment to an email (given that the user tablet has email chooser). This process was leveraged using Android’s built in Intent class. Once the user clicks send, the process is complete.

#### 10. Overdue Patients

In order for the overdue patients tab to detect whether a patient is past their due date, the user needs to first enter a due date for each vaccine after they have a given a dose of that particular vaccine.

##### Technical steps

1. When a user clicks the gray box matching the corresponding due date row, the due date variable for that vaccine is set and the record value is pushed to the Firebase database.
2. In the Alerts fragment, there is an EventListener that pulls the updated due date values for each vaccine.
3. Based on the date and time from the system device, it will compare the number of days between the system date and the vaccine due date to categorize each patient into one of the three categories of urgency (1 week, 1-2 weeks, 2+ weeks overdue)