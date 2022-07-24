package ru.pashtet.lesson6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println("сервер запущен");
            Socket socket = serverSocket.accept();
            System.out.println("клиент зашел на сервер");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
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
        catch (IOException e){
            e.printStackTrace();
        }
    }


}
