package io.vakin.algorithm.sort;

import java.util.Arrays;

/**
 * 桶排序
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年7月27日
 */
public class BucketSort {
 
    public int[] doSort(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
			if(arr[i] > max)max = arr[i];
		}
        // 构造辅助数组
        boolean[] bucket = new boolean[max + 1];
        for (int i = 0; i < arr.length; i++) {
        	bucket[arr[i]] = true;
        }
        
        int[] result = new int[arr.length];
        int index = 0;
        for (int i = 0; i < bucket.length; i++) {
			if(bucket[i]){
				result[index++] = i;
			}
		}        
        return result;
    }
    public static void main(String[] args) {
        int[] arr = new int[]{4, 1, 3, 2, 6, 9, 7};
    	BucketSort sort = new BucketSort();
        int[] sortedArr = sort.doSort(arr);
        
        System.out.println(Arrays.toString(sortedArr));
    }
    
}
