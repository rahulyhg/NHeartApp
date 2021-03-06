package com.cviac.nheart.nheartapp;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.cviac.nheart.nheartapp.datamodel.ConvMessage;
import com.cviac.nheart.nheartapp.datamodel.HugInfo;
import com.cviac.nheart.nheartapp.fragments.ChatFragment;

public class NheartApp extends MultiDexApplication {



    public boolean isNetworkstatus() {
        return networkstatus;
    }

    public void setNetworkstatus(boolean networkstatus) {
        this.networkstatus = networkstatus;
    }

    private ChatFragment chatFrag;

    private boolean networkstatus=true;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate(){
        super.onCreate();

        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses(ConvMessage.class);
        configurationBuilder.addModelClasses(HugInfo.class);
        ActiveAndroid.initialize(configurationBuilder.create());

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    public ChatFragment getChatFrag() {
        return chatFrag;
    }

    public void setChatFrag(ChatFragment chatFrag) {
        this.chatFrag = chatFrag;
    }

    public void notifyCartChange(String action) {
        Intent i = new Intent("notifyCartChange");
        i.putExtra("action", action);
        sendBroadcast(i);
    }

    public void setNetworkStatus(boolean networkstatus){
        this.networkstatus=networkstatus;
    }
}
