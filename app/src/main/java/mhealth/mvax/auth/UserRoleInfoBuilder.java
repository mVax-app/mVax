package mhealth.mvax.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import mhealth.mvax.R;

/**
 * @author Matthew Tribby
 *         A class that creates and holds a builder for ht User Role Info Modal
 *         The reason for this being a separate class is that it is used several times in the app so
 *         making it its own class to avoid code duplication.
 *         However, cannot extend AlertDialog.Builder so just holding a copy.
 */

public class UserRoleInfoBuilder {
    private AlertDialog.Builder builder;
    private Activity activity;

    public UserRoleInfoBuilder(Activity activity){
        this.activity = activity;
        createBuilder();
    }

    private void createBuilder(){
        builder = new AlertDialog.Builder(activity);

        builder.setTitle(activity.getResources().getString(R.string.modal_current_user_title));

        LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.modal_user_role_info, null);
        builder.setView(dialogView);

    }

    public AlertDialog.Builder getBuilder(){
        return builder;
    }


}
