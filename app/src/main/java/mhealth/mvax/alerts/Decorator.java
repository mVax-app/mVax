package mhealth.mvax.alerts;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import mhealth.mvax.R;

/**
 * Use a custom selector
 */
public class Decorator implements DayViewDecorator {

//    private final Drawable drawable;

    public Decorator(Activity context) {
//        drawable = context.getResources().getDrawable(R.drawable.date);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.setSelectionDrawable(drawable);
    }
}
