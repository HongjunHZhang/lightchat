package com.zhj.test;

import java.util.HashMap;
import java.util.Map;

class Solution {
    Map<Integer,Integer> map = new HashMap<>();
    int p;
    public Solution(int n, int[] blacklist) {
    Map<Integer,Integer> tempMap = new HashMap<>();
        for (int j : blacklist) {
            tempMap.put(j, j);
        }
    p = 0;
    for(int i = 0;i < n;i++){
       if(!tempMap.containsKey(i)){
          map.put(p,i);
          p++;
       }
    }
    }

    public int pick() {
    int key = (int)(Math.random()*p);
    return map.get(key);
    }

    public static void main(String[] args) {
       Solution solution =  new Solution(7,new int[]{2,3,5});
        for (int i = 0; i < 5; i++) {
            int pick = solution.pick();
            System.out.println(pick);

        }

    }

}
