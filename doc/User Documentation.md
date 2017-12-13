# mVax User Documentation

## Contents

1. User approval process
2. Log into mVax
3. Change your password
4. Change your email
5. Search for a patient
6. Create a new patient record
7. Edit an existing patient record
8. Delete an existing patient record
9. Record an immunization
10. Export forms
11. View vaccine statistics
12. Check for overdue patients
13. Change the language

### User approval process

New users must be approved by an administrator before they can log into mVax and view/edit data. There are currently two different levels of account permission:

1. _admin_: admins can write new records AND see old records in the app. They have also have special privileges like approving users / editing special fields.
2. _reader_: readers are able to still log-in to the app and view the data in the app but do not have permission to edit the records.

When it comes to new user registration, we only want people associated with Clínica Esperanza to be able to use the app. There is an approval process to limit who has access to this sensitive data. An _admin_ account is required to approve a user registration request before they have access to the app. The process is described below:

1. A prospective user downloads the app and requests to register for an account. The prospective user navigates taps the `Register` button and navigates to the following form.

    ![Register page](images/register_page.png)

Once this form is submitted, a user request is sent that can be approved by an _admin_.

2. A different user with _admin_ privileges approves the user. After logging in, an administrator navigates to the settings page by tapping the gear icon in the bottom tab bar.

    ![Settings page](images/settings_page.png)

3. The _admin_ then taps on the `Approve Users` button.

    ![Approve users button](images/approve_users_button.png)


4. The list of current user requests is displayed. The _admin_ taps on the user request and choose whether or not to approve it.

    ![Approve user](images/approve_user.png)

### Log into mVax

A registered user can log into mVax and view patient data. A user can log in by inputting their email and password into the `Email` and `Password` fields on the `Sign in` page, shown on app startup.

    ![Sign in](images/sign_in_page.png)

### Change your password

There are two ways to reset your password. You can do so from the login screen for when you've forgotten your password, or you can do so in the settings page.

##### Reset password

1. Open mVax and tap `Forgot Password?`.

    ![Forgot password](images/forgot_password_login.png)


2. Enter the email associated with your account and tap `Reset Password`. An email will be sent to the email address with a link for resetting your password.

    ![Forgot password](images/forgot_password_modal.png)

##### Change password

1. Open mVax and log in. Navigate to the settings page by tapping the gear icon in the bottom tab bar. Tap the `Change Password` button.

    ![Change password](images/change_password.png)

2. A modal appears. Tap `Reset Password`. An email will be sent to the email address associated with the account containing instructions on how to change the password.


### Change your email

1. Open mVax and log in. Navigate to the settings page by tapping the gear icon in the bottom tab bar. Tap the `Update Email` button.

    ![Change email](images/change_email.png)

2. Enter a new email address to use with your account and tap `Update Email`.

    ![Confirm email change](images/confirm_email_change.png)


### Search for a patient

A user can currently use six different filters to search a patient. By default, the search will try to find a patient by first name or last name. The supporting filters are as follows:
* Name
* Patient ID
* Year of Birth
* Community
* Parent ID
* Parent Name

The following steps describe the process for searching for a patient.

1. Log into mVax. The `Patients` tab is displayed and a list of patients shown.

    ![Patients](images/patients.png)


2. Tap the `Filter by...` drop down menu. Select an attribute with which to search for a patient.

    ![Filters](images/filters.png)

3. Begin typing into the search bar and  results will be displayed below. Only patients who meet the filter criteria will be visible.

    ![Search results](images/patient_search_result.png)

### Create a new patient record

A user can create a new patient record to store data about the patient and their immunizations.

1. Log into mVax. The `Patients` tab is displayed and a list of patients shown. Tap the "Add New Patient" to begin the process for adding a new patient.

    ![Add new patient](images/add_new_patient.png)

2. Enter all relevant patient details in the fields on the right side of the screen.

    ![Add new patient](images/new_patient_page.png)

3. Save the record by tapping the "Save Record" button. The patient will permanently appear in the complete list of patients until deleted.

### Edit an existing patient record

A user can edit the details of an existing patient record.

1. Log into mVax. The `Patients` tab is displayed and a list of patients shown. Tap any patient in the list and select it.

2. Tap on the `Edit Record` button.

    ![Edit patient](images/edit_patient.png)

3. A screen appears that allows the user to tap any field and edit its value. Simply tap a field and edit it to change its value.

    ![Edit patient](images/edit_patient_2.png)

4. Tap the `Save Record` button located at the bottom of the page to save any changes made. If you do not wish to save any changes, tap the back button.

    ![Save patient edits](images/save_patient_edits.png)

### Delete an existing patient record

A user can permanently delete an existing patient record.

1. Log in to mVax. The `Patients` tab is displayed and a list of patients shown. Tap any patient in the list and select it.

2. Tap the `Delete Record` button to permanently delete the current record. Tap `Confirm` if you are sure that you want to delete the record permanently. Once deleted, a record cannot be restored.

    ![Delete Patient](images/delete_patient.png)

### Record an immunization

A user can enter dates in which a dose of a particular vaccine was administered to a patient.

1. Log into mVax. The `Patients` tab is displayed and a list of patients shown. Tap any patient in the list and select it.

2. Tap the `Vaccine History` tab at the top of the details page. You can also swipe left on the page.

    ![Select vaccine history](images/select_vaccine_history.png)

3. A page showing the patient's vaccines and their doses is displayed. To enter a date for a particular dose, tap the gray box next to the label for which you want to enter a date.

    ![Select dose](images/select_dose.png)

4. Select the date the represents the date on which the dose was administered to the patient. Tap `Confirm`.

    ![Date picker](images/vaccine_date_picker.png)

### Export forms

This feature allows users to export the data from records and format them for the SINOVA and LINV forms. The user can send the resulting PDF to an email account through the app.

1. Once logged into mVax, navigate to the `Forms` tab.

    ![Forms page](images/forms_page.png)

2. Tap the `Export Forms` button.

    ![Export forms](images/export_forms.png)

3. Tap the button respective to the form that you would like to export.

    ![Form buttons](images/form_buttons.png)

4. Select the date for which you'd like to export the form. Tap `Export`.

    ![Date picker](images/form_date_picker.png)

5. Tap `Gmail`. Another source may work, but the app has only been tested with Gmail. If `Gmail` is not displayed, you will have to allow Gmail as a source in the account settings on your device and register a Gmail account to use as a sender.

    ![Export sources](images/form_export.png)

6. Compose an email to send with the PDF attached. Optionally, add any description you'd like to the body of the email.

    ![Export email](images/form_email.png)

### View vaccine statistics

You can view statistics about each vaccine. Specifically, you can see the total number of times each vaccine has been administered, and visualize progress towards annual immunization goals.

1. Once logged into mVax, navigate to the `Forms` tab.

    ![Forms page](images/forms_page.png)

Progress bars are displayed that visualize progress towards immunization goals.

### Check for overdue patients

This feature allows users to keep track of when a patient is next due for a particular vaccination. When the due date has passed, the patient will appear in the overdue patients tab.

1. After a vaccine dose has been administered, enter a due date for the vaccine, which represents the next time the patient should return for another dose of the vaccine. 

    ![Set due date](images/set_due_date.png)

2. After the due date has passed, the patient will appear in the overdue patients tab of the application. Overdue patients are split by priority according to how many weeks they are late.

    ![Overdue patients](images/overdue_patients.png)

3. Each overdue patient is displayed with their contact information (phone number, community, and address). This allows the user to easily locate or call the patient to remind them they are due to come into the clinic for their next vaccination.

    ![Overdue patient](images/overdue_patient.png)

### Change the language

You can change the language of mVax between English and Spanish.

1. Log into mVax and navigate to the settings page by tapping the gear icon. Tap the slider button next to "Change to Spanish". The app automatically reloads into Spanish.

    ![Language toggle](images/language_toggle.png)

2. To change the app language back to English, repeat these steps.