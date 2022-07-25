package ru.pashtet.lesson7.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Server server;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private String nickName;

    public String getNickName() {
        return nickName;
    }
    public ClientHandler(Server server, Socket socket){
        try{
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        authentication();
                        readMessage();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    finally {
                        closeConnection();
                    }
                }
            }).start();
        }
        catch (IOException e){
            throw new RuntimeException("ѕроблема при создании обработчика.");
        }
    }
    public void authentication() throws IOException {
        while (true){
            String message = inputStream.readUTF();
            if(message.startsWith(ServerCommandConstants.AUTHORIZATION)){
                String[] authInfo = message.split(" ");
                String nickName = server.getAuthService().getNickNameByLoginAndPassword(authInfo[1],authInfo[2]);
                if (nickName != null){
                    if (!server.isNickNameBusy(nickName)){
                        sendMessage("/authok " + nickName);
                        this.nickName = nickName;
                        server.broadcastMessage(nickName + " зашел в чат.");
                        server.subscribe(this);
                        return;
                    }
                    else{
                        sendMessage("учетна€ запись уже используетс€.");
                    }
                }
                else{
                    sendMessage("Ќеверна€ учетна€ запись или пароль.");
                }
            }
        }
    }

    public void sendMessage(String message){
        try{
            outputStream.writeUTF(message);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readMessage() throws IOException {
        while(true){
            String messageInChat = inputStream.readUTF();
            System.out.println("от " + nickName + ": " + messageInChat);
            if (messageInChat.equals(ServerCommandConstants.SHUTDOWN)){
                return;
            }
            String[] splitMessage = messageInChat.split(" ");
            server.broadcastMessage(nickName + ": " + messageInChat);
        }

    }
    private void closeConnection(){
        server.unsubscribe(this);
        server.broadcastMessage(nickName + "вышел из чата.");
        try{
            outputStream.close();
            inputStream.close();
            socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
