# mVax Backlog

This file lists all changes and improvements that need to be addressed.

##### General

* Uneven Spanish translation coverage
* Issues with translated strings on modals
* Persistence of language selection (i.e. spanish should persist upon multiple runs of app if selected; needs to be stored locally)

##### Navigation bar

* Users find the navigation bar difficult to use initially without tooltips underneath, though many noted that it's not an issue after several uses
 
##### Login page

* Buttons for sign in and register are identical in size and color, so it's unclear which one is for the text fields above (i.e. user may think that the `Sign In` and `Register` buttons might both work for text field)
* Change language from the log-in page (perhaps get language from the system)

##### Registration

* When registering, not clear if is necessary to choose reader or admin
* Checking for duplication of emails during registration (notifying the user that an account already exists for that email)
* Add information about what the different user roles (_admin_ and _reader_) mean
* Improve the security of passwords (In current version, only requirement is that the password has to be 6 alphanumeric digits)

##### Search

* Translate all of the `Add new patient` form into Spanish strings
* Develop a faster search algorithm (current is O(n))
* Load test--performance bad after 600 records
* Make `filter by` a label, and default spinner to `name`
* Keyboard occasionally does not dismiss when “Submit new record” clicked
* The keyboard stays up unless you press the back button
* If you navigate away from the patient record, when you go back to the search page, it ends up closing the patient record and you have to open it again (this is because clicking the search button in the tab restarts activity--may be a way to fix this but we need to decide if this is the behavior we want)
* Perhaps use icons instead of dropdown for the `filter by` spinner, though text may still be necessary
* Sort the records alphabetically by default, but also provide a way to sort the records by most recently added and other parameters
* Not clear that the filter defaults to search by name
* Check null handling in filter functionality
* Handle extremely long names
* Side margins on ListView
* Enforce height of search result ListView cell (this also refers to the other issue of handling long names)
* Crashes on community search but only occasionally (need to discover reason)

##### Patient record page 

* **High priority**: Form validation--patient must have certain required info entered before can be saved
* **High priority**: Difficult for users to edit text fields because the text is too far to the right compared to the field name; cursor starts at beginning of word
* Data does not save unless navigate to another text field
* Phone number is 8 digits without country code--implement checking for that
* Last name data entry - should have two last names because of Honduran naming conventions
* Validation and constraints on 13-digit ID
* Font of the pop-up modals is too small
* Purpose of the `dependents` field
* Ability to support multiple guardians and see other dependents of a particular guardian
* `Remove sex` instead of `No sex` in modal for choosing patient sex

##### Patient immunization page
* Entire `Due Date` UX needs to be overhauled
* Add ability for an _admin_ (not _reader_) to add new vaccines
* When removing a vaccine record, `No date` option could be renamed to `Remove date`

##### Alerts/Overdue page
* **High priority**: Ability to see which patients are late _and_ the specific vaccines they are late for
* Test if users are actually correctly listed
* If a patient late record is tapped, should be able to pull up the patient record
* Ability to print (as in physically send to a printer) or email list of patients in the alerts tab
* General UI improvements for readability for the row items under each accordion
* When in the Alerts tab then hit back button, the screens overlay occasionally (Fragment transaction manager issue)
* Title “Alerts” does not clearly reflect a page to find overdue patients.
* New patients with no vaccinations do not show up in alerts page (need to decide how to handle this)

##### Forms page

* `Dashboard` tab not intuitive to find on nav bar
* Not intuitive that the export buttons are located on the dashboard page
* Called `Dashboard form` on the tooltip but `Forms` on the actual page 

##### Export forms page

* Users expressed concerns in the lack of instructions on this page. Instructions would be included in user documentation but possibly a simple help button for easy walking through export steps or other instructions

##### Settings page

* Master-detail UI would be ideal
* Admin should be able to delete users / revoke access
* Crashes when you click sign out; sign out not working in general

##### Approve users page

* **High priority**: Denied user is still registered in the Firebase auth page, so they cannot re-apply to register
* When approving users it cuts off the screen on the top occasionally
* The approve users page looks weird when the keyboard is up
* UI error that occurs occasionally with the Approve `Users` page overlapping over another page when the back button is clicked (Fragment transaction manager issue)
* Provide confirmation of approval in some form
* List of all previously approved users for admins to view
* If a user is denied, may want to send back a notification/email describing why they were denied
* The person registering should not choose a role, the admin should assign the role (decision needs to be made regarding how to handle this)
