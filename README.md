# mVax

mVax is an Android mobile application for managing a medical records database that stores immunization records for Clínica Esperanza in Roatán, Honduras.

mVax is configured to use Firebase for storing and retrieving data. The application currently supports concurrent creation and editing of patient record and immunization data, notifications for patients with overdue vaccine appointments, data aggregation and export for use with the Honduran SINOVA 1 and 2 forms, multiple language support, and basic user account management. Future plans include support for macro visualization of immunization progress and goals along with full functionality when establishing a connection to the Firebase server is not possible.

Currently, this repository is configured to point to the Firebase instance for Clínica Esperanza. Follow the instructions in [SETUP.md](SETUP.md) for configuring mVax with a new database.

## Requirements

**Minimum SDK**: Android 7.0 Nougat, Android API Level 24

**Target and Compile SDK**: Android 8.0 Oreo, Android API Level 27

mVax can run on any Android device running at least Android 7.0 Nougat with Android API Level 24 or greater. However, mVax has only been extensively tested on the 9.7-inch Samsung Galaxy Tab S3 running Android 8.0 Nougat with Android API level 26, since this is the device with which it will be deployed at Clínica Esperanza.

## Installation

1. First, clone the repository:

`git clone git@coursework.cs.duke.edu:CompSci408_2017Fall/app_vaccination.git`

2. Open the project in Android Studio

##### Running the application on an emulator 

Note: mVax is optimized for an 9.7 inch screen, and was developed specifically for the Galaxy Tab S3.

1. In the menu bar, click `Tools` &rarr; `Android` &rarr; `AVD Manager`
2. Click `Create Virtual Device...` and then `Import Hardware Profiles`
3. Navigate to the `hardware_profiles` folder in the root directory and import `Galaxy Tab S3.xml`
4. Select `Galaxy Tab S3` in the device definition list and click `Next`
5. Select `Oreo` with **API level 26**; download the API if necessary and click `Next`
6. Click `Next`
7. Run the application. Select `Galaxy Tab S3 API 26` under `Available Virtual Devices` and click `OK`

##### Running the application on a device

Note: The Android device API minimum is 24.

1. Enable developer mode *and USB Debugging* on the device and connect via USB
2. Run the application and select the device under `Connected Devices`

##### Deploying a signed APK

A debug key store is included in the repo; the `build.gradle` file is configured to use this key store when running the release build type.

You should create your own release key and key store when publishing your version of mVax. See [this page](https://developer.android.com/studio/publish/app-signing.html) for more information. Store your generated release keystore in a safe location outside of the project repo.

1. In the menu bar, click `Build` &rarr; `Generate Signed APK`
2. Click `Next` and then `Create new...`
3. Fill in the necessary information and create the key store.
4. Choose the export destination under `APK Destination Folder`. Select `release` as the build type and choose which flavor should be used by the APK; select `V2` as the signature version
5. Click `Finish`

The signed `.apk` file can now be downloaded to any Android device (i.e. via email) and run as an application. It may be necessary to enable "Unknown sources" in the device's security settings so that apps can be installed from sources other than the Play Store.

## Dependencies

mVax is dependent on the following external libraries:

* Hamcrest (JUnit testing utilities)
* Firebase (database)
* Joda-Time (enhanced data structure for dates)
* Algolia (search engine)
* GmailBackground (sending auth-related background emails)
* MaterialDateTimePicker (date picker modals)
* MaterialCalendarView (alerts page calendar)
* KeyboardVisibilityEvent (scrolling auth page on input focus events)
* NumberPicker (horizontal number picker for inactivity timeout settings)