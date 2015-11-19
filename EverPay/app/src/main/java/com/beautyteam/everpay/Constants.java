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

    public class NOTIFICATION_ACTION {
        public final static int ADD_GROUPS = 0;
        public final static int EDIT_GROUP = 1;
        public final static int ADD_MEMBERS = 2;
        public final static int REMOVE_MEMBERS = 3;
        public final static int ADD_BILLS = 4;
        public final static int EDIT_BILLS = 5;
        public final static int ADD_DEBTS = 7;
        public final static int EDIT_DEBTS = 8;
        public final static int UNCLOSED_BILL = 10;
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

        public final static String ADD_USER = BASE_URL + "users/add/";
        public final static String CALC_DETAILS = BASE_URL + "debts/details/";

        public final static String REGISTER_GCM = BASE_URL + "gcm/register/";
        public final static String UNREGISTER_GCM = BASE_URL + "gcm/unregister/";

        public final static String PUSH_IN_APP = BASE_URL + "debts/remind/";
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
        public final static String GET_CALC_DETAILS = "GET_CALC_DETAILS";

        public final static String ADD_BILL = "ADD_BILL";
        public final static String ADD_MEMBER_TO_GROUP = "ADD_MEMBER_TO_GROUP";
        public final static String ADD_GROUP = "ADD_GROUP";
        public final static String CALCULATE = "CALCULATE";
        public final static String CREATE_AND_ADD_USER = "CREATE_AND_ADD_USER";
        public final static String CREATE_USER = "CREATE_USER";
        public final static String REGISTER_GCM = "REGISTER_GCM";

        public final static String EDIT_BILL = "EDIT_BILL";
        public final static String EDIT_GROUP = "EDIT_GROUP";
        public final static String EDIT_CALCULATION = "EDIT_CALC";

        public final static String REMOVE_BILL = "REMOVE_BILL";
        public final static String REMOVE_MEMBER_FROM_GROUP = "REMOVE_GROUP_MEMBER";
        public final static String UNREGISTER_GCM = "REGISTER_GCM";

        public final static String NOTIFICATION = "NOTIFICATION";

        public final static String SEND_VK_FOR_ALL = "VK_FOR_ALL";
        public final static String SEND_VK_FOR_MY = "VK_FOR_ME";

        public final static String BUG_REPORT = "BUG_REPORT";

        public final static String PUSH_IN_APP = "PUSH_IN_APP";
    }

    public class Titles {
        public final static String ADD_BILL = "Добавление счета";
        public final static String EDIT_BILL = "Редактирование счета";
        public final static String FRIENDS = "Добавление участников";
        public final static String ADD_GROUP = "Создание группы";
        public final static String EDIT_GROUP = "Участники группы";
        public final static String CALCULATION = "Расчет";
        public final static String GROUPS = "Группы";
        public final static String MAIN = "Обо мне";
        public final static String SETTINGS = "Настройки";
        public final static String CREATE_USER = "Создание участника";
        public final static String CALC_DETAILS = "Детализация";
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

        public final static String SETTING_PUSH = "SETTING_PUSH";
        public final static String SETTING_ADVICE = "SETTING_ADVICE";
        public final static String GCM_REGID = "GCM_REGID";

        public final static String IS_DEMO_REVIEWED = "IS_DEMO_REVIEWED";


        public final static String WAS_ADD_BILL_ADVICE_REVIEWED = "WAS_ADD_BILL_ADVICE_REVIEWED";
        public final static String WAS_CALCULATION_ADVICE_REVIEWED = "WAS_CALCULATION_ADVICE_REVIEWED";
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
        public final static String IS_DELETED = "IS_DELETED";

        //==== bugreport
        public final static String THEME = "THEME";
        public final static String EMAIL_MSG = "EMAIL_MSG";

        //==== User
        public final static String NEW_USER_NAME = "NEW_USER_NAME";
        public final static String NEW_USER_LASTNAME = "NEW_USER_LASTNAME";
        public final static String SEX = "SEX";

        // ==
        public final static String GCM_ID = "GCM_ID";

        //== NOTIFITACION
        public final static String ACTION_NOTIF = "ACTION_NOTIF";

        public static final String IS_FOR_ALL = "IS_FOR_ALL";
    }

    public final static String RECEIVER = "RECEIVER";

    public final static String LOG = "MyProgram";

    public final static String FILE_DIRECTORY = "/.EverPay";

    public final static String[] SCREEN_NAMES = {"Я должен", "Мне должны"};
    public final static String ACTION = "ACTION";
    public final static String IS_FROM_NOTYFICATION = "NOTIFY";

    public final static String SENDER_ID = "663098152756";
}


