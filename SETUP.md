## App installation instructions

See the [README](README.md) for device installation instructions and requirements. This document provides an overview of establishing an instance of mVax with a new back end.

## Change which Firebase instance mVax points to

To change which Firebase instance mVax points to, it is necessary to create a new `google-services.json` file from the respective Firebase project and place it in the repository corresponding to the correct environment. For example, supposed you want to change which Firebase instance `dev` points to:

1. On the homepage for the desired Firebase project, click "Add Another App", select Android, and follow the instructions for downloading a `google-services.json` file that corresponds to that Firebase instance. In this example, the Android package name would be `com.mvax.dev`, but the suffix will differ according to the targeted environment.
2. Download the `google-services.json` file and save it to the `app/src/dev` directory.
3. Install the Firebase CLI and log in. You will need to do a `firebase deploy` with the targeted environment before the correct Firebase functions and rules are initialized.

Similarly, if one wanted to change the Firebase instance for `qa` or `prod`, they need only follow the same instructions but for the `app/src/qa` or `app/src/prod` directory.

## Modify strings

To modify a string in the app, open `app/src/main/res/values/strings.xml`. There, search for the string that you would like to modify. After modifying the English string, you must modify that string's Spanish equivalent in `app/src/main/res/values-es/strings.xml`. As more language support is added, strings will need to be updated in the `values` folder for each supported language. A string that exists in the `values` folder but has no internationalization equivalent will default to the English version of that string.

## Environment information

* **dev**: Used for development and general testing
* **qa**: Used for running automated tests
* **prod**: Used for real user data (should **never** be used for development or testing)

To access build variants, select `View` &rarr; `Tool Windows` &rarr; `Build Variants` in the Android Studio menu bar.

You should use the debug variants of each flavor when testing, but you can test with the release variant as well.

## Dev and QA ogin information

### Dev

#### Doris Brown
* **Role: Admin**
* Username: devadmin@mvax.com
* Password: devadmin1

#### Donald Brown
* **Role: User**
* Username: devreader@mvax.com
* Password: devreader1


### QA

#### Doris Brown
* **Role: Admin**
* Username: qaadmin@mvax.com
* Password: qaadmin1

#### Donald Brown
* **Role: User**
* Username: qareader@mvax.com
* Password: qareader1