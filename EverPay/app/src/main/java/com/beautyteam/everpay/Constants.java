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
        public final static String DOWNLOAD_IMG = "DOWNLOAD_IMG";
    }

    public class Titles {
        public final static String ADD_BILL = "Добавление счета";
        public final static String FRIENDS = "Друзья";
        public final static String ADD_GROUP = "Создание группы";
        public final static String CALCULATION = "Расчет";
        public final static String GROUPS = "Группы";


    }

    public class State {
        public final static int IN_PROCESS = 1;
        public final static int ENDS = -1;
    }

    public class IntentParams {
        public final static String URL = "URL";
        public final static String NAME = "NAME";
    }

    public final static String RECEIVER = "RECEIVER";

    public final static String LOG = "MyProgram";



    public final static String FILE_DIRECTORY = "/.EverPay";

    public final static String[] SCREEN_NAMES = {"Я должен", "Мне должны"};
}
