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

exports.createDisabledAccount = functions.https.onCall((data, context) => {
  return admin.auth().createUser({
    email: data.email,
    emailVerified: false,
    password: data.password,
    displayName: data.displayName,
    disabled: true
  })
  .then(userRecord => userRecord.uid)
  .catch((error) => {
    throw new functions.https.HttpsError('error', error);
  })
});

exports.activateAccount = functions.https.onCall((data, context) => {
  return admin.auth().updateUser(data.uid, {
    disabled: false
  })
  .then((userRecord) => {
//    sendAccountActivatedEmail(data.email, data.subject, data.body);
    console.log("user " + data.uid + " activated");
    return userRecord;
  }).catch((error) => {
    throw new functions.https.HttpsError('error', error);
  })
});

exports.deleteAccount = functions.https.onCall((data, context) => {
  return admin.auth().deleteUser(data.uid)
  .then(() => {
    console.log("user " + data.uid + " deleted");
    return data.uid; // is this best?
  }).catch((error) => {git a
    throw new functions.https.HttpsError('error', error);
  })
});


//const nodemailer = require('nodemailer');
//const gmailEmail = functions.config().gmail.email;
//const gmailPassword = functions.config().gmail.password;
//const mailTransport = nodemailer.createTransport({
//  service: 'gmail',
//  auth: {
//    user: gmailEmail,
//    pass: gmailPassword,
//  },
//});
//
//function sendAccountActivatedEmail(email, subject, body) {
//  const mailOptions = {
//    from: `mVax <mvaxapp@gmail.com>`,
//    to: email,
//  };
//
//  mailOptions.subject = subject;
//  mailOptions.text = body;
//  return mailTransport.sendMail(mailOptions).then(() => {
//    return console.log('account activated email sent to ', email);
//  });
//}


// ALGOLIA SEARCH

const algoliasearch = require('algoliasearch');

const ALGOLIA_ID = functions.config().algolia.app_id;
const ALGOLIA_ADMIN_KEY = functions.config().algolia.admin_key;
const ALGOLIA_SEARCH_KEY = functions.config().algolia.search_key;
const ALGOLIA_INDEX_NAME = 'patients';
const client = algoliasearch(ALGOLIA_ID, ALGOLIA_ADMIN_KEY);

exports.addPatientIndex = functions.database.ref('data/patients/{databaseKey}').onCreate((snap, context) => {
  return indexPatient(snap.val());
});

exports.updatePatientIndex = functions.database.ref('data/patients/{databaseKey}').onUpdate((change, context) => {
  return indexPatient(change.after.data());
});

exports.deletePatientIndex = functions.database.ref('data/patients/{databaseKey}').onDelete((snap, context) => {
  const objectID = snap.val().databaseKey;
  const index = client.initIndex(ALGOLIA_INDEX_NAME);
  return index.deleteObject(objectID);
});

function indexPatient(patient) {
  // objectID required for Algolia to parse object
  patient.objectID = patient.databaseKey;
  const index = client.initIndex(ALGOLIA_INDEX_NAME);
  return index.saveObject(patient);
}
