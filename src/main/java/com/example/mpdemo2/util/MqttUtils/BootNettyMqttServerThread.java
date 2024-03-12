package com.example.mpdemo2.util.MqttUtils;/**
 * @description:
 * @author: Administrator
 * @time: 2024/3/6 0006 11:15
 */

/**
 * @description:
 * @author: Administrator
 * @time: 2024/3/6 0006 11:15
 */
public class BootNettyMqttServerThread extends Thread{
    private final int port;

    public BootNettyMqttServerThread(int port){
        this.port = port;
    }

    public void run() {
        BootNettyMqttServer bootNettyMqttServer = new BootNettyMqttServer();
        bootNettyMqttServer.startup(this.port);
    }

}
