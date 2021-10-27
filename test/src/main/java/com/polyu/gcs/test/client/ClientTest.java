package com.polyu.gcs.test.client;
import com.polyu.gcs.client.Client;
import com.polyu.gcs.client.netty.BusinessHandler;

import java.util.Scanner;


public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        Client.start("127.0.0.1:2181", "tongyan3");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String s = scanner.nextLine();
            BusinessHandler.sendPackage(s);
        }
    }
}
