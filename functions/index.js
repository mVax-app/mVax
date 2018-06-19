const functions = require('firebase-functions');

// USER MANAGEMENT

var admin = require('firebase-admin');

const SERVICE_ACCOUNT_CERT = functions.config().service_account;

admin.initializeApp({
  credential: admin.credential.cert({
    projectId: SERVICE_ACCOUNT_CERT.project_id,
    clientEmail: SERVICE_ACCOUNT_CERT.client_email,
    // Firebase env vars append a "\" to newlines, so strip them out
    privateKey: SERVICE_ACCOUNT_CERT.private_key.replace(/\\n/g, '\n')
  }),
  databaseURL: SERVICE_ACCOUNT_CERT.database_url
});

// creates a new account in the auth table that is disabled
exports.createDisabledAccount = functions.https.onCall((data, context) => {
  return admin.auth().createUser({
    email: data.email,
    emailVerified: false,
    password: data.password,
    displayName: data.displayName,
    disabled: true
  })
  .then((userRecord) => {
    console.log("user " + data.uid + " created");
    return userRecord.uid;
  }).catch((error) => {
    throw new functions.https.HttpsError("error creating disabled user ", error);
  })
});

// activates a user account in the auth table
exports.activateAccount = functions.https.onCall((data, context) => {
  return admin.auth().updateUser(data.uid, {
    disabled: false
  })
  .then((user) => {
    console.log("user " + user.uid + " activated");
    return user.uid;
  }).catch((error) => {
    throw new functions.https.HttpsError("error activating user ", error);
  })
});

// disables a user account in the auth table
exports.disableAccount = functions.https.onCall((data, context) => {
  return admin.auth().updateUser(data.uid, {
    disabled: true
  })
  .then((user) => {
    console.log("user " + user.uid + " disabled");
    return user.uid;
  }).catch((error) => {
    throw new functions.https.HttpsError("error disabling user ", error);
  })
});

// deletes a user account from the auth table
exports.deleteAccount = functions.https.onCall((data, context) => {
  return admin.auth().deleteUser(data.uid)
  .then(() => {
    console.log("user " + data.uid + " deleted");
    return data.uid;
  }).catch((error) => {
    throw new functions.https.HttpsError("error deleting user ", error);
  })
});
