package com.beautyteam.everpay.Utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Admin on 01.05.2015.
 */
public class DateFormetter {
    public static String formatDateTime(Context context, String timeToFormat) {

        String finalDateTime = "";

        String day = timeToFormat.substring(0, timeToFormat.indexOf(" "));

        String dateWithoutDay = timeToFormat.substring(timeToFormat.indexOf(" ")+1);
        String month = dateWithoutDay.substring(0, dateWithoutDay.indexOf(" "));

        String dateWithoutMonth = dateWithoutDay.substring(dateWithoutDay.indexOf(month));
        String year = dateWithoutMonth.substring(dateWithoutMonth.indexOf(" "), dateWithoutMonth.lastIndexOf(" "));

        String time = timeToFormat.substring(timeToFormat.lastIndexOf(" "));

        String monthDigit = "";
        if (month.equals("April")) {
            monthDigit = "04";
        } else
        if (month.equals("May")) {
            monthDigit = "05";
        }
        String result = year + "-" + monthDigit + "-" + day + " " + time;
        return result;
        /*SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
*/
        /*Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                int flags = 0;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = android.text.format.DateUtils.formatDateTime(context,
                        when + TimeZone.getDefault().getOffset(when), flags);
            }
        }*/


        //return finalDateTime;
    }
}
