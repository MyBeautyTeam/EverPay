package com.beautyteam.everpay.REST;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.beautyteam.everpay.Constants;

/**
 * Created by Admin on 27.02.2015.
 */
public class ServiceHelper implements AppResultsReceiver.Receiver {

    private Activity activity;
    private AppResultsReceiver mReceiver;
    private ActivityCallback activityCallback;

    public ServiceHelper(Activity _activity, ActivityCallback _activityCallback) {
        this.activity = _activity; // Подумать, может быть утечка памяти, если делать serviceHelper Singleton
        this.activityCallback = _activityCallback;
    }

    public void onResume() {
        mReceiver = new AppResultsReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    public void onPause() {
        mReceiver.setReceiver(null);
    }

    public void send(String name, String email) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.ADD_CONTACT);
        intentService.putExtra("name", name);
        intentService.putExtra("email", email);
        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
        Log.d(Constants.LOG, "ServiceHelper, send()");
    }

    // Обработка результата из сервиса
    public void downloadImage(String url, String name) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.DOWNLOAD_IMG);
        intentService.putExtra(Constants.IntentParams.NAME, name);
        intentService.putExtra(Constants.IntentParams.URL, url);
        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
        Log.d(Constants.LOG, "ServiceHelper, send()");
    }

    public void initVKUsers() {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.INIT_VK_USERS);
        intentService.putExtra(Constants.RECEIVER, mReceiver);

        activity.startService(intentService);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        Log.d(Constants.LOG, "ServiceHelper, onReceiveResult()");
        activityCallback.onRequestEnd(resultCode, data);
    }

    public void calculate(int groupID) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.CALCULATE);
        intentService.putExtra(Constants.RECEIVER, mReceiver);

        activity.startService(intentService);
    }

    /*
    Получить список групп
     */
    public void getGroups() {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.GET_GROUPS);

        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
    }

    /*
    Получить список пользователей группы (при заходе в группу)
     */
    public void getGroupMembers(int groupId) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.GET_GROUP_MEMBERS);
        intentService.putExtra(Constants.IntentParams.GROUP_ID, groupId);

        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
    }

    /*
    Получить долги (главный экран)
     */
    public void getDebts() {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.GET_DEBTS);

        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
    }

    /*
    Долучить счет (экран просмотра счета)
     */
    public void getBill(int billId, int groupId) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.GET_BILL);
        intentService.putExtra(Constants.IntentParams.BILL_ID, billId);
        intentService.putExtra(Constants.IntentParams.GROUP_ID, groupId);

        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
    }

    /*
    Добавить счет
     */
    public void addBill(int billId, int groupId) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.ADD_BILL);
        intentService.putExtra(Constants.IntentParams.BILL_ID, billId);
        intentService.putExtra(Constants.IntentParams.GROUP_ID, groupId);

        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
    }

    /*
    НЕ ОТТЕСТИРОВАНО!
    Добавить полькозателя в группу
     */
    public void addMemberToGroup(int userId, int groupId) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.ADD_MEMBER_TO_GROUP);
        intentService.putExtra(Constants.IntentParams.USER_ID, userId);
        intentService.putExtra(Constants.IntentParams.GROUP_ID, groupId);

        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
    }

    /*
    НЕ ОТТЕСТИРОВАНО!
    Удалить пользователя из группы
     */
    public void removeMemberFromGroup(int userId, int groupId) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.REMOVE_MEMBER_FROM_GROUP);
        intentService.putExtra(Constants.IntentParams.USER_ID, userId);
        intentService.putExtra(Constants.IntentParams.GROUP_ID, groupId);

        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
    }

    /*
    Запросить историю группы
     */
    public void getHistory(int groupId) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.GET_HISTORY);
        intentService.putExtra(Constants.IntentParams.GROUP_ID, groupId);

        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
    }

    /*
    Создать группу
     */
    public void addGroup(int groupId) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.ADD_GROUP);
        intentService.putExtra(Constants.IntentParams.GROUP_ID, groupId);

        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
    }

}
