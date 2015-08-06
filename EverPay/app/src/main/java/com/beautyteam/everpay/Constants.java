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

    public class URL {
        public final static String BASE_URL = "http://188.166.89.182/";
        public final static String GET_GROUPS = BASE_URL + "groups/";
        public final static String GET_GROUP_MEMBERS = BASE_URL + "groups/members/";
        public final static String GET_DEBTS = BASE_URL + "debts/";
        public final static String GET_BILL = BASE_URL + "bills/details/";
        public final static String GET_HISTORY = BASE_URL + "groups/history/";


        public final static String ADD_BILL = BASE_URL + "bills/add/";
        public final static String ADD_GROUP_MEMBER = BASE_URL + "groups/members/add/";
        public final static String ADD_GROUP = BASE_URL + "groups/add/";
        public final static String CALCULATE = BASE_URL + "debts/add/";

        public final static String EDIT_BILL = BASE_URL + "bills/edit/";
        public final static String EDIT_GROUP = BASE_URL + "groups/edit/";
        public final static String EDIT_CALCULATION = BASE_URL + "debts/edit/";

        public final static String REMOVE_GROUP_MEMBER = BASE_URL + "groups/members/remove/";
        public final static String REMOVE_BILL = BASE_URL + "bills/remove/";

        public final static String SIGNUP = BASE_URL + "login/";
    }

    public class Action {
        //===
        public final static String ADD_CONTACT = "ADD_CONTACT";
        public final static String ANY_ACTION_WITH_POST = "POST_ACTION";
        public final static String DOWNLOAD_IMG = "DOWNLOAD_IMG";
        //===

        public final static String INIT_VK_USERS = "INIT_VK_USERS";
        public final static String GET_GROUPS = "GET_GROUPS";
        public final static String GET_GROUP_MEMBERS = "GET_GROUP_MEMBERS";
        public final static String GET_DEBTS = "GET_DEBTS";
        public final static String GET_BILL = "GET_BILL";
        public final static String GET_HISTORY = "GET_HISTORY";

        public final static String ADD_BILL = "ADD_BILL";
        public final static String ADD_MEMBER_TO_GROUP = "ADD_MEMBER_TO_GROUP";
        public final static String ADD_GROUP = "ADD_GROUP";
        public final static String CALCULATE = "CALCULATE";

        public final static String EDIT_BILL = "EDIT_BILL";
        public final static String EDIT_GROUP = "EDIT_GROUP";
        public final static String EDIT_CALCULATION = "EDIT_CALC";

        public final static String REMOVE_BILL = "REMOVE_BILL";
        public final static String REMOVE_MEMBER_FROM_GROUP = "REMOVE_GROUP_MEMBER";

        public final static String NOTIFICATION = "NOTIFICATION";

        public final static String SEND_MESSAGE_WITH_IMAGE = "IMG_MESSAGE";

        public final static String BUG_REPORT = "BUG_REPORT";
    }

    public class Titles {
        public final static String ADD_BILL = "Добавление счета";
        public final static String EDIT_BILL = "Редактирование счета";
        public final static String FRIENDS = "Добавление участников";
        public final static String ADD_GROUP = "Создание группы";
        public final static String EDIT_GROUP = "Участники группы";
        public final static String CALCULATION = "Расчет";
        public final static String GROUPS = "Группы";
        public final static String MAIN = "Главная";
        public final static String SETTINGS = "Настройки";
    }

    public class Preference {
        public final static String SHARED_PREFERENCES = "SHARED_PREFERENCES";
        public final static String ACCESS_TOKEN = "ACCESS_TOKEN";
        public final static String USER_ID = "USER_ID";
        public final static String USER_ID_VK = "USER_ID_VK";
        public final static String USER_NAME = "USER_NAME";
        public final static String IMG_URL = "IMG_URL";
        public final static String MALE = "MALE";
        public final static String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";
        public final static String REGISTATION_STATUS = "REG_STATUS";
    }

    public class State {
        public final static int READY_TO_SEND = 0;
        public final static int IN_PROCESS = 1;
        public final static int ENDS = -1;
    }

    public class IntentParams {
        public final static String URL = "URL";
        public final static String NAME = "USER_NAME";
        public final static String GROUP_ID = "GROUP_ID";
        public final static String BILL_ID = "BILL_ID";
        public final static String USER_ID = "USER_ID";
        public final static String IS_MORE_LOAD = "IS_MORE_LOAD";
        public final static String IS_ENDS = "IS_ENDS";
        public final static String MALE = "MALE";
        public final static String GROUP_TITLE = "GROUP_TITLE";
        public final static String IMAGE = "IMAGE";

        //==== bugreport
        public final static String THEME = "THEME";
        public final static String EMAIL_MSG = "EMAIL_MSG";
    }

    public final static String RECEIVER = "RECEIVER";

    public final static String LOG = "MyProgram";

    public final static String FILE_DIRECTORY = "/.EverPay";

    public final static String[] SCREEN_NAMES = {"Я должен", "Мне должны"};
    public final static String ACTION = "ACTION";
    public final static String IS_FROM_NOTYFICATION = "NOTIFY";

    public final static String SENDER_ID = "663098152756";
}

