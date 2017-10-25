//This code is a derivative of google's quickstar cloud functions guide which is under a
// Apache 2.0 license and is meant to be open source.
// Link to github page: https://github.com/firebase/functions-samples/tree/master/quickstarts/email-users

const functions = require('firebase-functions');
const nodemailer = require('nodemailer');
const admin = require('firebase-admin');

//Allow less secure apps turned on.
//Display: Unlock Captcha
const gmailEmail = encodeURI(functions.config().gmail.email);
console.log('email:', gmailEmail);
const gmailPassword = encodeURI(functions.config().gmail.password);
console.log('password:', gmailPassword);
//Stack helped fix the next line
//https://stackoverflow.com/questions/31516821/node-js-email-doesnt-get-sent-with-gmail-smtp
const mailTransport = nodemailer.createTransport({
    service: 'Gmail',
    auth: {
    user: gmailEmail,
    pass: gmailPassword
  }
});

const APP_NAME = 'mVax';

//Start of send approval email:

exports.sendApprovalEmail = functions.auth.user().onCreate(event => {

  const user = event.data; //The user

  const email = user.email; // The email of the user.
  //TODO set a main admin in charge of handling this programmatically
  const adminEmail = 'mvaxapp@gmail.com';

  return sendApprovalEmail(email, adminEmail);
});

function sendApprovalEmail(email, adminEmail) {
  const mailOptions = {
    from: `${APP_NAME} <noreply@firebase.com>`,
    to: adminEmail
  };

  // The user subscribed to the newsletter.
  mailOptions.subject = `Approve ${email}! for ${APP_NAME}!`;
  mailOptions.text = `Please work`;
  return mailTransport.sendMail(mailOptions).then(() => {
    console.log('New approve email sent to:', adminEmail);
  });
}

//End of send approval email