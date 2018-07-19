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
package com.mvax.main;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Robert Steilberg
 * <p>
 * Custom Timer that has an API for computing if
 * the timer is currently running (i.e. can be cancelled)
 */
public class InactivityTimer extends Timer {

    private boolean mIsRunning;

    @Override
    public void schedule(TimerTask task, long delay) {
        super.schedule(adjustedTask(task), delay);
    }

    @Override
    public void schedule(TimerTask task, Date date) {
        super.schedule(adjustedTask(task), date);
    }

    @Override
    public void schedule(TimerTask task, long delay, long period) {
        super.schedule(adjustedTask(task), delay, period);
    }

    @Override
    public void schedule(TimerTask task, Date firstTime, long period) {
        super.schedule(adjustedTask(task), firstTime, period);
    }

    @Override
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        super.schedule(adjustedTask(task), delay, period);
    }

    @Override
    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        super.schedule(adjustedTask(task), firstTime, period);
    }

    private TimerTask adjustedTask(TimerTask original) {
        mIsRunning = true;
        return new TimerTask() {
            @Override
            public void run() {
                original.run();
                mIsRunning = false; // done running
            }
        };
    }

    public boolean isRunning() {
        return mIsRunning;
    }

}
