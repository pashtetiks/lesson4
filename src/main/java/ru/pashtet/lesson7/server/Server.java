package ru.pashtet.lesson7.server;

import ru.pashtet.lesson7.authorization.AuthService;
import ru.pashtet.lesson7.authorization.InMemoryAuthServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {


    private final AuthService authService;
    private List<ClientHandler> connectedUsers;

    public Server(){
        authService = new InMemoryAuthServiceImpl();
        authService.start();
        connectedUsers = new ArrayList<>();
        try (ServerSocket server = new ServerSocket(CommonConstants.SERVER_PORT)){
            while (true){
                System.out.println("Сервер ждет подключения клиента.");
                Socket socket = server.accept();
                System.out.println("Клиент подключился.");
                new ClientHandler(this,socket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка в работе сервера.");
            e.printStackTrace();
        }finally {
            if (authService != null){
                authService.end();
            }
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNickNameBusy(String nickName){
        for(ClientHandler handler:connectedUsers){
            if (handler.getNickName().equals(nickName)){
                return true;
            }
        }
        return false;
    }

    public void broadcastMessage(String message){
        String[] splitMessage = message.split(" ");
        if (splitMessage.length >= 3){
            if (splitMessage[1].equals("/w")){
                for (ClientHandler handler: connectedUsers){
                    if (handler.getNickName().equals(splitMessage[2])){
                        String[] newMessageArr = new String[splitMessage.length - 3];
                        System.arraycopy(splitMessage, 3, newMessageArr, 0, splitMessage.length - 3);
                        String newMessage = String.join(" ", newMessageArr);
                        handler.sendMessage(splitMessage[0] + newMessage);
                    }
                }
            }else {
                for(ClientHandler handler: connectedUsers){
                    handler.sendMessage(message);
                }
            }
        }else {
            for(ClientHandler handler: connectedUsers){
                handler.sendMessage(message);
            }
        }

    }

    public void subscribe(ClientHandler handler){
        connectedUsers.add(handler);
    }

    public void unsubscribe(ClientHandler handler){
        connectedUsers.remove(handler);
    }
}
