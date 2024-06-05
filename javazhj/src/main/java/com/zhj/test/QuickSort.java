package com.zhj.test;

/**
 * QuickSort
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/25 22:24
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] arr = new int[]{1,7,-5,87,45,112,24,31,14,118,12,34,-14,75};
        quickSort(arr,0, arr.length - 1);
        for (int i : arr) {
            System.out.println(i);
        }

    }

    public static void quickSort(int[] arr, int left, int right){
        if (left >= right){
            return;
        }
        int i = left, j = right;
        int temp = arr[left];
        while (i < j){
            while (j > i && arr[j] >= temp){
                j--;
            }

            while (j > i && arr[i] <= temp){
                i++;
            }

            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }

        arr[left] = arr[j];
        arr[j] = temp;

        quickSort(arr,left,i - 1);
        quickSort(arr,i + 1,right);
    }

}
