package com.choco.server;

import com.choco.client.ClientController;
import com.choco.client.ClientInfo;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

public class ServerHandler implements HandleMessage {
    ClientController clientController;
    private final static int OPERATION_SUCCESS = 200;
    private final static int OPERATION_FAILED = 201;


    String info;
    InetAddress clientAddress;
    int clientPort;
    public ServerHandler(){
        info = null;
        clientAddress = null;
        clientPort = 0;
        clientController = new ClientController();

        ClientInfo client1 = new ClientInfo("陈律", "192.168.1.102", 102);
        ClientInfo client2 = new ClientInfo("周裔欢", "192.168.1.69", 102);

        clientController.addClient(client1);
        clientController.addClient(client2);
    }



    @Override
    public boolean handle(DatagramPacket datagramPacket) {
        ResponseToClient response;
        try {
            System.out.println(datagramPacket);
            clientAddress = datagramPacket.getAddress();
            clientPort = datagramPacket.getPort();
            info = new String(datagramPacket.getData(),0,datagramPacket.getLength());
            ClientInfo client = ClientInfo.fromJSON(info);
            client.setAddress(clientAddress.getHostAddress());
            client.setPort(clientPort);
            if(clientController.addClient(client)){
                response = new ResponseToClient(OPERATION_SUCCESS,client.getToken(), clientController.getClientInfoList());
            }else {
                response = new ResponseToClient(OPERATION_FAILED,"", new ArrayList<>());
            }


            System.out.println(clientAddress.getHostAddress() + ":" + clientPort);
            System.out.println("receive: " + info);
            UDP.getUDP(12345).sendMsg(clientAddress,clientPort,response.toJson());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
