# mVax

mVax is an Android mobile application for managing a medical records database that stores immunization records for Clínica Esperanza in Roatán, Honduras.

mVax is configured to use Firebase for storing and retrieving data. The application supports concurrent creation and editing of record data, macro visualization of immunization progress and goals, multiple language support, and full functionality when establishing a connection to the Firebase server is not possible.

Currently, this repository is configured to point to the Firebase instance for Clínica Esperanza.

## Requirements

**Minimum SDK**: Android 7.0 Nougat, Android API Level 24

**Target and Compile SDK**: Android 8.0 Oreo, Android API Level 26

mVax can run on any Android device running at least Android 7.0 Nougat with Android API Level 24 or greater. However, mVax has only been tested on the 8-inch Samsung Galaxy Tab S2 running Android 7.0 Nougat with Android API level 24, since this is the device with which it will be deployed at Clínica Esperanza.

## Installation

1. First, clone the repository:

`git clone git@coursework.cs.duke.edu:CompSci408_2017Fall/app_vaccination.git`

2. Open the project in Android Studio

##### Running the application on an emulator 

Note: mVax is optimized for an 8 inch screen, and was developed specifically for the Galaxy Tab S2.

1. In the menu bar, click `Tools` &rarr; `Android` &rarr; `AVD Manager`
2. Click `Create Virtual Device...` and then `Import Hardware Profiles`
3. Navigate to the `hardware_profiles` folder in the root directory and import `Galaxy Tab S2.xml`
4. Select `Galaxy Tab S2` in the device definition list and click `Next`
5. Select `Nougat` with **API level 24**; download the API if necessary and click `Next`
6. Click `Next`
7. Run the application. Select `Galaxy Tab S2 API 24` under `Available Virtual Devices` and click `OK`

##### Running the application on a device

Note: The Android device API minimum is 24.

1. Enable USB Debugging on the device and connect via USB
2. Run the application and select the device under `Connected Devices`

##### Deploying a signed APK

You should create your own release key and key store. See [here](https://developer.android.com/studio/publish/app-signing.html) for more information. Store your generated keystore in the `keystores` directory located in the root directory. There is currently a development key store in the `keystores` directory that can be used, but it should only be used for development releases. See below for the passwords for the included development key store.
1. In the menu bar, click `Build` &rarr; `Generate Signed APK`
2. Click `Next`
3. Ender the password for your key store and click `Next`
4. Choose the export destination under `APK Destination Folder`. Select `release` as the build type and choose which flavor should be used by the APK; select `V2` as the signature version
5. Click `Finish`

The signed `.apk` file can now be downloaded to any Android device (i.e. via email) and run as an application. It may be necessary to enable "Unknown sources" in the device's security settings so that apps can be installed from sources other than the Play Store.

##### Development Key Store
* Path: `keystores/development_key_store`
* Key Store password: &J9sclinica
* Key Alias: Development Key Store
* Key password: &J9sesperanza


## Dependencies

mVax is dependent on the following external libraries:

* Firebase (external database)
* iText (PDF export utility)
* Joda-Time (enhanced data structure for dates)
* Hamcrest (JUnit testing utilities)
