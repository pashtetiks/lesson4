package ru.pashtet.lesson7.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Scanner scanner;

    public Client(){
        scanner = new Scanner(System.in);
        try{
            openConnection();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void openConnection() throws IOException {
        initializeNetwork();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (true){
                        String messageFromServer = inputStream.readUTF();
                        System.out.println(messageFromServer);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        String text = scanner.nextLine();
                        if (text.equals(ServerCommandConstants.SHUTDOWN)) {
                            sendMessage(text);
                            closeConnection();
                        }else{
                            sendMessage(text);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initializeNetwork() throws IOException {
        socket = new Socket(CommonConstants.SERVER_ADDRESS,CommonConstants.SERVER_PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream((socket.getOutputStream()));
    }

    public void sendMessage(String message){
        try{
            outputStream.writeUTF(message);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void closeConnection(){
        try{
            outputStream.close();
            inputStream.close();
            socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
