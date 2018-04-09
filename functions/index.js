const functions = require('firebase-functions');
const algoliasearch = require('algoliasearch');

const ALGOLIA_ID = functions.config().algolia.app_id;
const ALGOLIA_ADMIN_KEY = functions.config().algolia.admin_key;
const ALGOLIA_SEARCH_KEY = functions.config().algolia.search_key;
const ALGOLIA_INDEX_NAME = 'patients';
const client = algoliasearch(ALGOLIA_ID, ALGOLIA_ADMIN_KEY);

exports.addPatientIndex = functions.database.ref('data/patients/{databaseKey}').onCreate((event) => {
    return indexPatient(event);
});

exports.updatePatientIndex = functions.database.ref('data/patients/{databaseKey}').onUpdate((event) => {
    return indexPatient(event);
});

exports.deletePatientIndex = functions.database.ref('data/patients/{databaseKey}').onDelete((event) => {
    const objectID = event.params.databaseKey;
    const index = client.initIndex(ALGOLIA_INDEX_NAME);
    return index.deleteObject(objectID);
});

function indexPatient(event) {
    const patient = event.data.val();
    patient.objectID = event.params.databaseKey;
    const index = client.initIndex(ALGOLIA_INDEX_NAME);
    return index.saveObject(patient);
}