# mVax App Maintenance Documentation

## App Installation Instructions

See the [README](../README.md) and [release notes](../RELEASE_NOTES.md) for full installation instructions and requirements. The most pertinent information is repeated below.
 
 #### Target device requirements
 
 mVax is an Android application only supported by devices that run the Android operating system.
 
 mVax is currently optimized for an 8-inch screen, specifically that of the Samsung Galaxy Tab S2. The application supports both portrait and landscape mode. Running mVax on other Android devices may result in unintended consequences or cause unknown graphical errors.
 
 **Minimum SDK**: Android 7.0 Nougat, Android API Level 24
 
 **Target and Compile SDK**: Android 8.0 Oreo, Android API Level 26
 
 mVax can run on any Android device running at least Android 7.0 Nougat with Android API Level 24 or greater. However, mVax has only been tested on the 8-inch Samsung Galaxy Tab S2 running Android 7.0 Nougat with Android API level 24, since this is the device with which it will be deployed at Clínica Esperanza.

#### Installation instructions (for non-developers)

These instructions assume you have access to a pre-built and pre-signed mVax APK. If not, refer to the "Deploying a signed APK" section in the [README](../README.md).

Ensure that "Unknown sources" are enabled in the target device's security settings so that apps can be installed from sources other than the Play Store. Once this has been checked, the APK need only be downloaded to the Android device via an email or other medium, and installed by opening the APK file.

## Modifying the Firebase instance

To change which Firebase instance mVax points to, one need only generate a new `google-services.json` file from the desired Firebase project and place it in the repository corresponding to the correct environment. For example, supposed you want to change which Firebase instance `dev` points to:

1. On the homepage for the desired Firebase project, click "Add Another App", select Android, and follow the instructions for downloading a `google-services.json` file that corresponds to that Firebase instance.
2. Download the `google-services.json` file and save it to the `app/src/dev` directory.

Similarly, if one wanted to change the Firebase instance for `qa` or `prod`, they need only follow the same instructions but for the `app/src/qa` or `app/src/prod` directory.

## Directly modify the database

One can directly modify the data in the Firebase database through the Firebase console. Choose the desired Firebase project and select "Database" in the sidebar. There, fields can be edited or deleted, the entire datbase can be expored as a `JSON` file, or a new database can be imported as a `JSON` file. For an explanation of the database schema, see the "Database Schema" section in the [technical documentation](Technical%20Documentation.md).

Existing user accounts can be directly administered by selecting the "Authentication" tab in the sidebar in the Firebase console. There, an administrator can reset an account's password, disable an account, or delete an account. To modify a user's name or email, modify the `Users` table in the database.

## Modify strings

To modify a string in the app, open `app/src/main/res/values/strings.xml`. There, search for the string that you would like to modify. After modifying the English string, you must modify that string's Spanish equivalent in `app/src/main/res/values-es/strings.xml`. As more language support is added, strings will need to be updated in the `values` folder for every language. A string that exists in the `values` folder but has no internationalization equivalent will always default to the English version of that string.

## Environment information

* **dev**: Use for development and general messing around with the database
* **QA**: Use for running automated tests
* **prod**: Use for real user data--never develop or test on prod

To access build variants, in the menu bar, select `View` &rarr; `Tool Windows` &rarr; `Build Variants`.

You should use the debug variants of each flavor when testing, but you can test with release as well to make sure minify works. Prod release is currently unsigned and will not compile, since mVax has not been deployed to production yet.

**Sometimes when switching between build variants or product flavors, Android Studio will tell you that the app installation failed when you try to compile and run. This is normal--you only need to compile and run again to overwrite the signature of the existing app and run mVax.**


## Authentication information

### Dev

#### Doris Brown
* **Role: Admin**
* Username: devadmin@mvax.com
* Password: devadmin1

#### Donald Brown
* **Role: Reader**
* Username: devreader@mvax.com
* Password: devreader1


### QA

#### Doris Brown
* **Role: Admin**
* Username: qaadmin@mvax.com
* Password: qaadmin1

#### Donald Brown
* **Role: Reader**
* Username: qareader@mvax.com
* Password: qareader1


### Prod

*No accounts have been set on prod since mVax has not been deployed to a production instance.*


## Key Stores

Key Stores store APK signing keys that are used to digitally sign the app for distribution under a `release` build variant.

### Development Key Store
* Path: `keystores/development_key_store`
* Key Store password: &J9sclinica
* Key Alias: Development Key Store
* Key password: &J9sesperanza
