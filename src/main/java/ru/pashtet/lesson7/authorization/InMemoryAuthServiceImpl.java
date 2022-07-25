package ru.pashtet.lesson7.authorization;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAuthServiceImpl implements AuthService{
    private final Map<String, UserData> users;

    public InMemoryAuthServiceImpl(){
        users = new HashMap<>();
        users.put("login1",new UserData("login1","password1", "nickName1"));
        users.put("login2",new UserData("login2","password2", "nickName2"));
        users.put("login3",new UserData("login3","password3", "nickName3"));
    }
    @Override
    public void start() {
        System.out.println("Сервис аутентификации инициализирован.");
    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {
        UserData user = users.get(login);
        if (user != null){
            if (user.getPassword().equals(password)){
                return user.getNickName();
            }
        }
        return null;
    }

    @Override
    public void end() {
        System.out.println("Сервис аутентификации завершил работу.");
    }
}
