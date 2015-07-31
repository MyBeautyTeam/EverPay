package com.beautyteam.everpay.Utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Admin on 01.05.2015.
 */
public class DateFormatter {
    public static String formatDateTime(String timeToFormat) {

        String day = timeToFormat.substring(0, timeToFormat.indexOf(" "));

        String dateWithoutDay = timeToFormat.substring(timeToFormat.indexOf(" ")+1);
        String month = dateWithoutDay.substring(0, dateWithoutDay.indexOf(" "));

        String dateWithoutMonth = dateWithoutDay.substring(dateWithoutDay.indexOf(month));
        String year = dateWithoutMonth.substring(dateWithoutMonth.indexOf(" "), dateWithoutMonth.lastIndexOf(" "));

        String time = timeToFormat.substring(timeToFormat.lastIndexOf(" "));

        String monthDigit = "";
        if (month.equals("January")) {
            monthDigit = "01";
        } else
        if (month.equals("February")) {
            monthDigit = "02";
        } else
        if (month.equals("March")) {
            monthDigit = "03";
        } else
        if (month.equals("April")) {
            monthDigit = "04";
        } else
        if (month.equals("May")) {
            monthDigit = "05";
        } else
        if (month.equals("June")) {
            monthDigit = "06";
        } else
        if (month.equals("July")) {
            monthDigit = "07";
        } else
        if (month.equals("August")) {
            monthDigit = "08";
        } else
        if (month.equals("September")) {
            monthDigit = "09";
        }
        if (month.equals("October")) {
            monthDigit = "10";
        } else
        if (month.equals("November")) {
            monthDigit = "11";
        } else
        if (month.equals("December")) {
            monthDigit = "12";
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
