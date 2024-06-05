package com.zhj.test.leetcode;

import java.util.Stack;

class MyQueue {

    Stack<Integer> stackFirst;
    Stack<Integer> stackSecond;

    public MyQueue() {
     stackFirst = new Stack<>();
     stackSecond = new Stack<>();
    }

    public void push(int x) {
      stackFirst.push(x);
    }

    public int pop() {


     if ( !stackSecond.isEmpty() ){
       return  stackSecond.pop();
     }

        if( !stackFirst.isEmpty() ){
            while ( !stackFirst.isEmpty() ){
                stackSecond.push(stackFirst.pop());
            }
            return stackSecond.pop();
        }


        return -1;
    }

    public int peek() {
        if ( !stackSecond.isEmpty() ){
            return  stackSecond.peek();
        }

        if( !stackFirst.isEmpty() ){
            while ( !stackFirst.isEmpty() ){
                stackSecond.push(stackFirst.pop());
            }
            return stackSecond.peek();
        }

        return -1;

    }

    public boolean empty() {
        return stackFirst.isEmpty() && stackSecond.isEmpty();
    }
}
