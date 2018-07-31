package io.vakin.algorithm.sort;

import java.util.Arrays;

/**
 * 归并排序
 * 
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年7月31日
 */
public class MergeSort {

	static int number = 0;

	public static void main(String[] args) {
		int[] a = { 26, 5, 98, 108, 28, 99, 100, 56, 34, 1 };
		Sort(a, 0, a.length - 1);
		System.out.println(Arrays.toString(a));
	}

	private static void Sort(int[] a, int left, int right) {
		if (left >= right)
			return;

		int mid = (left + right) / 2;
		// 二路归并排序里面有两个Sort，多路归并排序里面写多个Sort就可以了
		Sort(a, left, mid);
		Sort(a, mid + 1, right);
		merge(a, left, mid, right);

	}

	private static void merge(int[] a, int left, int mid, int right) {

		int[] tmp = new int[a.length];
		int r1 = mid + 1;
		int tIndex = left;
		int cIndex = left;
		// 逐个归并
		while (left <= mid && r1 <= right) {
			if (a[left] <= a[r1])
				tmp[tIndex++] = a[left++];
			else
				tmp[tIndex++] = a[r1++];
		}
		// 将左边剩余的归并
		while (left <= mid) {
			tmp[tIndex++] = a[left++];
		}
		// 将右边剩余的归并
		while (r1 <= right) {
			tmp[tIndex++] = a[r1++];
		}

		System.out.println("第" + (++number) + "趟排序:\t");
		// TODO Auto-generated method stub
		// 从临时数组拷贝到原数组
		while (cIndex <= right) {
			a[cIndex] = tmp[cIndex];
			// 输出中间归并排序结果
			System.out.print(a[cIndex] + "\t");
			cIndex++;
		}
		System.out.println();
	}

}
