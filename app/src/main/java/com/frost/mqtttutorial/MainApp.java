package com.frost.mqtttutorial;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.MqttHelper;
import listener.MqttMessageListener;

/**
 * Created by bookhsu on 2018/6/17.
 */

public class MainApp extends Application {

    private MqttHelper mqttHelper;
    private Stack<Activity> activityStack = new Stack<>();

    @Override
    public void onCreate() {
        super.onCreate();

        startMqtt();
        registerApplicationLifeCycle();
    }

    private void startMqtt(){
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug","Connected");
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
                notifyStackActivity(mqttMessage.toString());
                // dataReceived.setText(mqttMessage.toString());
                // mChart.addEntry(Float.valueOf(mqttMessage.toString()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    private void notifyStackActivity(String message) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activityStack.peek() instanceof MqttMessageListener) {
                ((MqttMessageListener)activityStack.peek()).onMessageReceive(message);
            }
        }
    }

    private void registerApplicationLifeCycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityStack.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityStack.remove(activity);
            }
        });
    }
}
