package com.zhj.test;

public class ListNode {
    public String value;
    public ListNode next;

    public ListNode(String value, ListNode next) {
        this.value = value;
        this.next = next;
    }

    public ListNode() {
    }


    public static void main(String[] args) {
      ListNode head = new ListNode("0",null);
      ListNode temp = head;
        for (int i = 1; i < 100; i++) {
            temp.next = new ListNode(String.valueOf(i),null);
            temp = temp.next;
        }

        head = df(head);
        while (head != null){
            System.out.println(head.value);
            head = head.next;

        }


    }

    public static ListNode df(ListNode head){
        if (head == null || head.next == null){
            return head;
        }

        ListNode temp = df(head.next);
        head.next.next = head;
        head.next = null;
        return temp;
    }



}


