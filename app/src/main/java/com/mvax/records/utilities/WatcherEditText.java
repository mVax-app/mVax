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
package com.mvax.records.utilities;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Steilberg
 * <p>
 * EditText that allows for clearing TextWatchers
 */
public class WatcherEditText extends android.support.v7.widget.AppCompatEditText {

    private List<TextWatcher> mWatchers;

    public WatcherEditText(Context context) {
        super(context);
        mWatchers = new ArrayList<>();
    }

    public WatcherEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWatchers = new ArrayList<>();
    }

    public WatcherEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWatchers = new ArrayList<>();
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        mWatchers.add(watcher);
        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        int i = mWatchers.indexOf(watcher);
        if (i >= 0) mWatchers.remove(i);
        super.removeTextChangedListener(watcher);
    }

    public void clearTextChangedListeners() {
        for (TextWatcher watcher : mWatchers) {
            super.removeTextChangedListener(watcher);
        }
        mWatchers.clear();
    }

}
