package mhealth.mvax.auth.modals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 *         <p>
 *         Displays a modal for resetting an mVax user's password
 */
public class PasswordResetModal extends AlertDialog.Builder {

    private Activity mActivity;
    private View mView;
    private Resources mResources;

    public PasswordResetModal(View v) {
        super(v.getContext());
        mActivity = (Activity) v.getContext();
        mView = v;
        mResources = v.getResources();
    }

    /**
     * Build and display the modal to reset user password
     */
    @Override
    public AlertDialog show() {
        initBuilder().show();
        return null;
    }

    private AlertDialog initBuilder() {

        final AlertDialog builder = new AlertDialog.Builder(mActivity)
                .setTitle(mResources.getString(R.string.modal_reset_title))
                .setView(mActivity.getLayoutInflater().inflate(R.layout.modal_reset_password, (ViewGroup) mView.getParent(), false))
                .setPositiveButton(mResources.getString(R.string.button_reset_password_submit), null)
                .setNegativeButton(mResources.getString(R.string.button_reset_password_cancel), null)
                .create();

        // attach listener; ensure text field isn't empty on submit
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                final TextView emailTextView = builder.findViewById(R.id.textview_email_reset);

                // in email EditText, enter on hardware keyboard submits for authentication;
                // "Done" button submits for reset too
                emailTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (event != null
                                && event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (attemptPasswordReset(emailTextView)) builder.dismiss();
                            return true;
                        }
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (attemptPasswordReset(emailTextView)) builder.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

                Button button = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (attemptPasswordReset(emailTextView)) builder.dismiss();
                    }
                });
            }
        });
        return builder;
    }

    private boolean attemptPasswordReset(final TextView emailTextView) {
        String emailAddress = emailTextView.getText().toString();
        if (TextUtils.isEmpty(emailAddress)) { // trying to submit with no email
            emailTextView.setError(mResources.getString(R.string.error_empty_field));
            emailTextView.requestFocus();
            return false;
        }
        sendResetEmail(emailAddress);
        return true;
    }

    private void sendResetEmail(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("attemptedReset", "resetPassword:attempted:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Log.w("successReset", "resetPassword:success", task.getException());
                            Toast.makeText(mActivity, mResources.getString(R.string.reset_email_confirm), Toast.LENGTH_LONG).show();
                        } else {
                            Log.w("failedReset", "resetPassword:failed", task.getException());
                            // see what the error was
                            boolean noInternet = task.getException() instanceof FirebaseNetworkException;
                            if (noInternet) {
                                Toast.makeText(mActivity, R.string.firebase_fail_no_connection,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mActivity, R.string.firebase_fail_unknown,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

}
