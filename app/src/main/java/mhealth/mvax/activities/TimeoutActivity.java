package mhealth.mvax.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import mhealth.mvax.R;

public class TimeoutActivity extends AppCompatActivity {

    private long timeoutDuration = 60000; // 10 sec = 10 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable logoutCallback = new Runnable() {
        @Override
        public void run() {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    TimeoutActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle(getResources().getString(R.string.timeout_title));
            alertDialog
                    .setMessage(getResources().getString(R.string.timeout_msg));
            alertDialog.setNegativeButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(TimeoutActivity.this,
                                    MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            dialog.cancel();
                        }
                    });

            alertDialog.show();

            // Perform any required operation on disconnect
        }
    };

    public void resetDisconnectTimer() {
        System.out.println("PRINT: resetting timeout");
        disconnectHandler.removeCallbacks(logoutCallback);
        disconnectHandler.postDelayed(logoutCallback, timeoutDuration);
    }

    public void stopDisconnectTimer() {
        System.out.println("PRINT: stop timer");
        disconnectHandler.removeCallbacks(logoutCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    public long getTimeoutDuration() {
        return timeoutDuration;
    }

    public void setTimeoutDuration(long newTimeout) {
        timeoutDuration = newTimeout;
        System.out.println("PRINT: new timeout " + timeoutDuration);
    }

}