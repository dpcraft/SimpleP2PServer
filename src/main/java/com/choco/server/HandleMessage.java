package com.choco.server;

import java.net.DatagramPacket;

public interface HandleMessage {

    boolean handle(DatagramPacket datagramPacket);

}
