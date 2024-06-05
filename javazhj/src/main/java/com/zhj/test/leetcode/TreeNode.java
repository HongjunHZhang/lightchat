package com.zhj.test.leetcode;


import java.util.*;

public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }
  }

class FindElements {
    HashSet<Integer> set;
    TreeNode root;

    public FindElements(TreeNode root) {
        this.root = root;
        this.set = new HashSet<>();
        createTree(root,-1,false);
    }

    public boolean find(int target) {
      return set.contains(target);
    }


    public void createTree(TreeNode root, int val,boolean isLeft){
        if( isLeft ){
            root.val = 2 * val + 1;
        }else {
            root.val = 2 * val + 2;
        }

        set.add(root.val);

        if (root.left != null){
            createTree(root.left, root.val, true);
        }


        if (root.right != null){
            createTree(root.right, root.val, false);
        }

    }

    public static void main(String[] args) {
        maxProfitAssignment(new int[]{2,17,19,20,24,29,33,43,50,51,57,67,70,72,73,75,80,82,87,90},
                new int[]{6,7,10,17,18,29,30,31,34,39,40,42,48,54,57,78,78,78,83,88},
                new int[]{12,9,11,41,11,87,48,6,48,93,76,73,7,50,55,97,47,33,46,10});
    }


    public static int maxProfitAssignment(int[] difficulty, int[] profit, int[] worker) {
        Map<Integer,Integer> map = new HashMap<>();
        int length = difficulty.length;
        for (int i = 0; i < length; i++) {
            map.put(profit[i],difficulty[i]);
        }

        Arrays.sort(profit);
        Arrays.sort(worker);
        int m = length - 1;
        int n = worker.length - 1;
        int ret = 0;

        while ( m >= 0 && n >= 0 ){
             if ( worker[n] >= map.get(profit[m]) ){
                 ret += profit[m];
                 n--;
             }else {
                 m--;
             }
        }

         return  ret;
    }

}

