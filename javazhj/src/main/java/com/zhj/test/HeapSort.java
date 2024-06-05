package com.zhj.test;

/**
 * HeapSort
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/25 22:48
 */
public class HeapSort {

    public static void main(String[] args) {
        int[] arr = new int[]{1,7,-5,87,45,112,24,31,14,118,12,34,-14,75};
        heapSort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }


    public static void heapSort(int[] arr){
        int length = arr.length;
        for (int i = length / 2 - 1; i >= 0; i--) {
            sort(arr,i,length);
        }

        for (int i = length - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[length - 1];
            arr[length - 1] = temp;
            sort(arr,0,--length);
        }

    }

    public static void sort(int[] arr, int index, int length){
        int left = 2 * index;
        int right = 2 * index + 1;
        int maxIndex = index;
        if (left >= length || right >= length){
            return;
        }

        if (arr[maxIndex] < arr[left]){
            maxIndex = left;
        }

        if (arr[maxIndex] < arr[right]){
            maxIndex = right;
        }

        if (maxIndex != index){
            int temp = arr[index];
            arr[index] = arr[maxIndex];
            arr[maxIndex] = temp;
            sort(arr,maxIndex,length);
        }
    }

}
