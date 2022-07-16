package ru.pashtet.lesson4;

public class MainApp {
    public static void main(String[] args) {
        MonoLinkedList<Integer> list = new MonoLinkedList<>();
        list.addNode(25);
        list.addNode(27);
        list.addNode(32);
        list.addNode(526);
        list.addNode(0);



        list.addNode(5454,3);
        list.delNode(0);
        list.display();

    }
}
