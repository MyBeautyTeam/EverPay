package com.beautyteam.everpay.REST.Processors;

import android.content.Intent;

import com.beautyteam.everpay.REST.Service;

/**
 * Created by Admin on 29.04.2015.
 */
public abstract class Processor {
    public abstract void request(Intent intent, Service service);
}
