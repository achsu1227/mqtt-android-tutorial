package listener;

/**
 * Created by bookhsu on 2018/6/17.
 */

public interface MqttMessageListener {
    void onMessageReceive(String msg);
}
