package com.beautyteam.everpay;

import android.net.Uri;

/**
 * Created by Admin on 27.02.2015.
 */
public class Constants {
    public class Result {
        public final static int OK = 1;
        public final static int ERROR = -1;
    }

    public class Action {
        public final static String ADD_CONTACT = "ADD_CONTACT";
        public final static String ANY_ACTION_WITH_POST = "POST_ACTION";
    }

    public class State {
        public final static int IN_PROCESS = 1;
        public final static int ENDS = -1;
    }

    public final static String RECEIVER = "RECEIVER";

    public final static String LOG = "MyProgram";

    final public static Uri CONTACT_URI = Uri.parse("content://com.beautyteam.everpay.AdressBook/contacts");
}
