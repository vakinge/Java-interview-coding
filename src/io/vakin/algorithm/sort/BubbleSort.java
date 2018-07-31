package io.vakin.algorithm.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年7月31日
 */
public class BubbleSort {

	public static void main(String[] args) {
		int[] arr = { 1, 4, 2, 7, 9, 8, 3, 6 };
		sort(arr);
		System.out.println(Arrays.toString(arr));
	}

	public static void sort(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			boolean flag = true;// 设定一个标记，若为true，则表示此次循环没有进行交换，也就是待排序列已经有序，排序已然完成。
			for (int j = 0; j < arr.length - 1 - i; j++) {
				if (arr[j] > arr[j + 1]) {
					swap(arr, j, j + 1);
					flag = false;
				}
			}
			if (flag) {
				break;
			}
		}
	}

	public static void swap(int[] arr, int a, int b) {
		arr[a] = arr[a] + arr[b];
		arr[b] = arr[a] - arr[b];
		arr[a] = arr[a] - arr[b];
	}
}
