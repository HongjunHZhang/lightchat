package com.zhj.test;

public class Sort {

    public static void main(String[] args) {
        int[] arr = new int[]{-1,8,45,71,13,9,4,32,21,314,-54,72};
        quickSort(arr,0, arr.length - 1 );
        for (int i : arr) {
            System.out.println(i);
        }

    }

    public static void quickSort(int[] arr,int left,int right){
        if(left >= right){
            return;
        }
        int i = left;
        int j = right;
        int temp = arr[left];

        while ( i < j ){
            while ( arr[j] >= temp && i < j ){
                j--;
            }

            while ( arr[i] <= temp && i < j ){
                i++;
            }

            int num = arr[i];
            arr[i] = arr[j];
            arr[j] = num;
        }

        arr[left] = arr[j];
        arr[j] = temp;
        quickSort(arr,left,i);
        quickSort(arr,i+1,right);

    }

}
