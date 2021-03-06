package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.*;
/**
 * Created by Renato & Vladimir on 21/10/2017.
 */

public class Server extends Connection implements Runnable {
    private RoutingTable routingTable;
    private int port;

    public Server(RoutingTable routingTable, int PORT, Logbook logbook) throws IOException {
        this.port = PORT;
        this.routingTable = routingTable;
        this.connectedAS = "";
        this.logbook = logbook;
    }

    public void startServer() {
        try {
            String writing = "";
            this.initConnection("server", this.port, "localhost");
            writing = "Server " + this.routingTable.getId() + " waiting in the port " + this.port;
            //System.out.println(writing);
            this.logbook.writeInLogbook(writing);

            cs = ss.accept();
            writing = "Client connected to the server " + this.routingTable.getId() + "\r\n";
            this.logbook.writeInLogbook(writing);
            //System.out.println(writing);
            outClient = new PrintWriter(cs.getOutputStream(),true);
            BufferedReader input = new BufferedReader(new InputStreamReader(cs.getInputStream()));

            //System.out.println(serverMessage);
            while (this.active) {
                Thread.sleep(10000);
                if ((serverMessage = input.readLine()) != null) {
                    writing = "Received Message: " + serverMessage + "\r\n";
                    //System.out.println(writing);
                    this.connectedAS = serverMessage.split("\\*")[0];
                    routingTable.receiveUpdate(serverMessage);
                    outClient.println(this.routingTable.getUpdatePackage(this.connectedAS));
                    writing = routingTable.showRoutes();
                    this.logbook.writeInLogbook(writing);
                    //System.out.println(writing);
                } else {
                    writing = "Server " + this.routingTable.getId() + " finished a connection, waiting in the port " + this.port + "\r\n";
                    this.logbook.writeInLogbook(writing);
                    //System.out.println(writing);
                    ss.close();
                    this.routingTable.cleanASRoutes(this.connectedAS);
                    this.connectedAS = "";
                    cs = ss.accept();
                    writing = "Client connected to the server " + this.routingTable.getId() + "\r\n";
                    this.logbook.writeInLogbook(writing);
                    //System.out.println(writing);
                }
            }
        } catch (Exception e) {
            this.logbook.writeInLogbook("Error connecting with " + this.connectedAS);
            this.routingTable.cleanASRoutes(this.connectedAS);
            this.restart();
            this.cs = null;
            this.ss = null;
            this.outClient =null;
            if(!this.turnOff){
                this.startServer();
            }
        }
    }



    public void run() {
        this.startServer();
    }
}