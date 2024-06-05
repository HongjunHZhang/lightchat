package com.zhj.test;

import java.util.LinkedList;
import java.util.Stack;

/**
 * ZhanToDui
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/8/29 18:51
 */
public class ZhanToDui {
   private final Stack<Integer> inStack = new Stack<>();
   private final Stack<Integer> outStack = new Stack<>();

    public void push(int num){
        inStack.push(num);
    }

    public int pop(){
        if (outStack.empty()){
            if (inStack.empty()){
                return -1;
            }
            while(!inStack.empty()){
                outStack.push(inStack.pop());
            }
            return outStack.pop();
        }
       return outStack.pop();
    }

    public boolean empty(){
        return inStack.empty() && outStack.empty();
    }

    public static void main(String[] args) {
        ZhanToDui dui = new ZhanToDui();
        dui.push(1);
        dui.push(9);
        dui.push(3);
        dui.push(7);
        dui.push(2);
        dui.push(8);
        dui.push(7);
        while (!dui.empty()){
            System.out.println(dui.pop());
        }


    }


}
