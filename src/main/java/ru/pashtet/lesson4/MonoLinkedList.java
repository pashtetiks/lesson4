package ru.pashtet.lesson4;

public class MonoLinkedList<T> {
    private long size = 0;
    private Node head;
    private Node tile;
    private class Node{
        T data;
        Node next;

        public Node(T data){
            this.data = data;
            this.next = null;
        }
    }

    public void addNode(T data){
        Node newNode = new Node(data);
        if (head == null){
            head = newNode;
            tile = newNode;
        }
        else {
            tile.next = newNode;
        }
        tile = newNode;
        size += 1;
    }
    public void display(){
        Node currently = head;
        if (currently == null){
            System.out.println("Односвязный список пуст");
        }
        else {
            while (currently != null){
                System.out.print(currently.data + " ");
                currently = currently.next;
            }
        }
        System.out.println();
    }
    public T display(int index){
        Node currently = head;
        if (index >= size){
            System.out.println("Индекс за пределами списка");
            return null;
        }
        else{
            for(int i = 0; i < index; i++){
                currently = currently.next;
            }
        }
        return currently.data;
    }
    public void addNode(T data, int index){
        Node currently = head;
        Node currentlyNext = head.next;
        Node newNode = new Node(data);

        if (index > size){
            System.out.println("Индекс за пределами списка");
        }
        else if (index == 0){
            newNode.next = head;
            head = newNode;

        }
        else{
            for(int i = 0; i < index - 1; i++){
                currently = currently.next;
                currentlyNext = currentlyNext.next;
            }
            currently.next = newNode;
            newNode.next = currentlyNext;
        }
    }
    public void delNode(int index){
        Node currently = head;
        Node currentlyNext = head.next;
        if (index >= size){
            System.out.println("Индекс за пределами списка");
        }
        else if(index == 0){
            head = head.next;
        }
        else{
            for(int i = 0; i < index - 1; i++){
                currently = currently.next;
                currentlyNext = currentlyNext.next;
            }
            currently.next = currentlyNext.next;
        }
    }

}
