package io.vakin.algorithm.sort;

import java.util.Arrays;

/**
 * 直接插入排序
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年7月31日
 */
public class InsertionSort {

	public static void main(String[] args) {
		 int []arr ={1,4,2,7,9,8,3,6};
	        sort(arr);
	        System.out.println(Arrays.toString(arr));
	}
	
	  /**
     * 插入排序
     *
     * @param arr
     */
    public static void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int j = i;
            while (j > 0 && arr[j] < arr[j - 1]) {
                swap(arr,j,j-1);
                j--;
            }
        }
    }
    
    public static void swap(int []arr,int a,int b){
        arr[a] = arr[a]+arr[b];
        arr[b] = arr[a]-arr[b];
        arr[a] = arr[a]-arr[b];
    }
}
