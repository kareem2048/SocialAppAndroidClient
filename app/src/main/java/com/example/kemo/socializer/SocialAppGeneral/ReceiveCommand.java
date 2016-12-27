package com.example.kemo.socializer.SocialAppGeneral;

import java.io.DataInputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by kemo on 25/10/2016.
 */
public abstract class ReceiveCommand extends Thread {
    private Socket remote;
    private volatile boolean run;
    public ReceiveCommand(Socket remote)
    {
        this.remote = remote;
        run = true;
    }
    @Override
    public void run() {
        super.run();

            while (run) {
                try {
                    DataInputStream objectInputStream = new DataInputStream(remote.getInputStream()); //open remote stream
                    Command command = Command.fromString(objectInputStream.readUTF()); //generate command from string
                    Analyze(command); //send it to the abstract function Analyze so other team members do there work
                }
                catch (SocketException e)
                {
                    break;
                }
                catch (Exception e) {
                    //Export to log
//                    System.out.println("ReadClientData\t" +
                    e.printStackTrace();
                }
            }
    }
    public void kill()
    {
        run = false;
    }
    public abstract void Analyze(Command command);
}