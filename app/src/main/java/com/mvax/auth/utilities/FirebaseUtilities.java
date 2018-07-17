/*
Copyright (C) 2018 Duke University

This file is part of mVax.

mVax is free software: you can redistribute it and/or
modify it under the terms of the GNU Affero General Public License
as published by the Free Software Foundation, either version 3,
or (at your option) any later version.

mVax is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU General Public
License along with mVax; see the file LICENSE. If not, see
<http://www.gnu.org/licenses/>.
*/
package com.mvax.auth.utilities;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.HashMap;

import com.mvax.model.user.User;

/**
 * @author Robert Steilberg
 * <p>
 * Static class for encapsulating general Firebase utilities; methods in this class
 * MUST be cross-checked with functions in index.js
 */
public class FirebaseUtilities {

    /**
     * Calls the HTTPS Firebase Cloud Function "createDisabledAccount" which
     * creates a disabled user; an error will be returned if the call is not successful
     *
     * @param email       the new user's email
     * @param password    the new user's password
     * @param displayName the new user's display name
     * @return Task result from calling the Firebase Cloud Function
     */
    public static Task<String> createDisabledUser(String email, String password, String displayName) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("email", email);
        args.put("password", password);
        args.put("displayName", displayName);
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        return functions.getHttpsCallable("createDisabledAccount")
                .call(args)
                .continueWith(task -> (String) task.getResult().getData());
    }

    /**
     * Calls the HTTPS Firebase Cloud Function "activateAccount" which activates
     * an existing, disabled user; an error will be thrown if the user is not found;
     * nothing will happen if the user is already enabled
     *
     * @param user the Firebase-assigned UID of the existing user
     * @return Task result from calling the Firebase Cloud Function
     */
    public static Task<String> activateUser(User user) {
        final HashMap<String, Object> args = new HashMap<>();
        args.put("uid", user.getUID());
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        return functions.getHttpsCallable("activateAccount")
                .call(args)
                .continueWith(task -> task.getResult().getData().toString());
    }

    /**
     * Calls the HTTPS Firebase Cloud Function "disableAccount" which disables
     * an existing, disabled user; an error will be thrown if the user is not found;
     * nothing will happen if the user is already disabled
     *
     * @param user the Firebase-assigned UID of the existing user
     * @return Task result from calling the Firebase Cloud Function
     */
    public static Task<String> disableUser(User user) {
        final HashMap<String, Object> args = new HashMap<>();
        args.put("uid", user.getUID());
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        return functions.getHttpsCallable("disableAccount")
                .call(args)
                .continueWith(task -> task.getResult().getData().toString());
    }

    /**
     * Calls the HTTPS Firebase Cloud Function "deleteAccount" which deletes
     * an existing user; an error will be thrown if the user is not found
     *
     * @param uid the Firebase-assigned UID of the existing user
     * @return Task result from calling the Firebase Cloud Function
     */
    public static Task<String> deleteUser(String uid) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("uid", uid);
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        return functions.getHttpsCallable("deleteAccount")
                .call(args)
                .continueWith(task -> task.getResult().toString());
    }

}
