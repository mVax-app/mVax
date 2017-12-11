# Release Notes

## Current Functionality

##### General

* Nearly full functionality during periods of network connectivity loss
* Concurrent editing of data
* Multiple environment support for pointing at different Firebase instances for development, testing, and production
* Internationalization support for English and Spanish

##### Authentication

* Request a new account with two types of permissions, _admin_ and _reader_
* Sign into an existing account
* Change the email associated with a mVax _admin_ or _reader_ account
* Change or reset password via email
* Approve requests for new mVax _admin_ or _reader_ accounts

##### Records

* Create a new record representing a patient
* Search existing records by patient ID, name, DOB, community, parent ID, or parent name
* View or delete an existing patient record
* Edit the data fields belonging to an existing patient record
* Add a date to an immunization dose, signifying an immunization event
* Edit or remove an existing immunization dose date
* Select a due date for a vaccine, signifying when the patient is due back at the clinic for the next dose in that vaccine's regimen

##### Alerts

* View patients that are overdue to return to the clinic for an immunization, and the degree to which they are overdue

##### Dashboard

* View progress towards macro-level (i.e. yearly overall) immunization goals for a specific vaccine
* Export a SINOVA, SINOVA 2, or LINV immunization record form that is auto-populated with immunization data specific to a given date

## Supported platforms

mVax is an Android application only supported by devices that run the Android operating system.

mVax is currently optimized for an 8-inch screen, specifically that of the Samsung Galaxy Tab S2. The application supports both portrait and landscape mode. Running mVax on other Android devices may result in unintended consequences or cause unknown graphical errors.

**Minimum SDK**: Android 7.0 Nougat, Android API Level 24

**Target and Compile SDK**: Android 8.0 Oreo, Android API Level 26

mVax can run on any Android device running at least Android 7.0 Nougat with Android API Level 24 or greater. However, mVax has only been tested on the 8-inch Samsung Galaxy Tab S2 running Android 7.0 Nougat with Android API level 24, since this is the device with which it will be deployed at Clínica Esperanza.