package ru.pashtet.lesson6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    public Client(){
        try{
            openConnection();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void openConnection() throws IOException{
        socket = new Socket(SERVER_ADDRESS,SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (true){
                        Scanner scanner = new Scanner(System.in);
                        String messageOut = scanner.next();
                        out.writeUTF(messageOut);
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    while (true){
                        String message = in.readUTF();
                        System.out.println(message);
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
