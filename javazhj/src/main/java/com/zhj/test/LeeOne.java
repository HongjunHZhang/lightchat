package com.zhj.test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * LeeOne
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/9/25 10:31
 */
public class LeeOne {
    static int maxValue;
    static int maxLength;

    public static void main(String[] args) {
//       check(new int[]{3,4,5,1,2});
        System.out.println((int)Math.pow(10,9));
    }

    public static boolean check(int[] nums) {
        int minValue = nums[0],index = 0;
        for(int i = 0; i < nums.length - 1; i++){
            if(nums[i + 1] < nums[i]){
                index = i + 1;
                break;
            }else{
                minValue = Math.min(minValue,nums[i]);
            }
            if (index == nums.length - 2){
                return true;
            }
        }

        for(int i = index; i  < nums.length - 1; i++){
            if(nums[i + 1] < nums[i] || nums[i] > minValue){
                return false;
            }
        }
        return nums[nums.length - 1] <= minValue;
    }

//    public int trapRainWater(int[][] heightMap) {
//        int row = heightMap[0].length;
//        int col = heightMap.length;
//        int[][] leftMax = new int[row][col];
//        int[][] rightMax = new int[row][col];
//        for (int i = 0; i < col; i++) {
//            leftMax[col][0] = heightMap[i][0];
//            rightMax[col][row - 1] = heightMap[i][row - 1];
//        }
//
//        for (int i = 0; i < row; i++) {
//            for (int j = 0; j < col; j++) {
//
//
//            }
//
//        }
//
//
//
//    }

    public int trap(int[] height) {
        Deque<Integer> stack = new LinkedList<>();
        int ret = 0;
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[i] > height[stack.peek()]){
                int top = stack.pop();
                if (stack.isEmpty()){
                    break;
                }
                int leftHeight = stack.peek();
                int width = i - 1 -leftHeight;
                int col = Math.min(height[i],height[leftHeight]) - height[top];
                ret += col * width;
            }
            stack.push(i);
        }
        return ret;

    }

    class Student{
        public String name;
        public int height;
        public Student(String name,int height){
            this.name = name;
            this.height = height;
        }
    }

    public  String[] sortPeople(String[] names, int[] heights) {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
           list.add(new Student(names[i],heights[i]));
        }
        list.sort((t1,t2)->t2.height - t1.height);
        for (int i = 0; i < list.size(); i++) {
            names[i] = list.get(i).name;
        }
        return names;
    }

    public static List<Integer> goodIndices(int[] nums, int k) {
        int length = nums.length;
        List<Integer> ret = new ArrayList<>();
        for (int i = k; i < length - k; i++) {
           if (isGoodArr(nums,i,k)){
               ret.add(i);
           }

        }
          return ret;
    }

    public static boolean isGoodArr(int[] nums,int index,int k){
        for (int i = index - k; i < index - 1; i++) {
            if (nums[i] < nums[i+1]){
                return false;
            }
        }

        for (int i = index + 1; i < index + k ; i++) {
            if (nums[i] > nums[i+1]){
                return false;
            }
        }

        return true;
    }

    public static int longestSubarray(int[] nums) {
        boolean[] signal = new boolean[nums.length];
        for (int i = 1; i <= nums.length; i++) {
            df(nums,new ArrayList<>(),signal,i);
        }
        return maxLength;
    }

    public static void df(int[] nums,List<Integer> list,boolean[] signal,int length){
       if (list.size() == length){
           int temp = list.get(0);
           for (int i = 1; i < list.size(); i++) {
               temp = temp & list.get(i);
           }
           if (temp >= maxValue){
               maxValue = temp;
               maxLength = length;
           }
       }

        for (int i = 0; i < nums.length; i++) {
            if (signal[i]){
                continue;
            }
            signal[i] = true;
            list.add(nums[i]);
            df(nums,list,signal,length);
            signal[i] = false;
            list.remove(list.size()-1);
        }

    }

}
