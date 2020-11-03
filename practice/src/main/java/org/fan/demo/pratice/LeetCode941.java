package org.fan.demo.pratice;

/**
 * 给定一个整数数组A，如果它是有效的山脉数组就返回true，否则返回 false。
 * <p>
 * 让我们回顾一下，如果 A 满足下述条件，那么它是一个山脉数组：
 * <p>
 * A.length >= 3 在0 < i< A.length - 1条件下，存在i使得： A[0] < A[1] < ... A[i-1] < A[i] A[i] > A[i+1] > ... > A[A.length - 1]
 * <p>
 * 来源：力扣（LeetCode） 链接：https://leetcode-cn.com/problems/valid-mountain-array 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @version 1.0
 * @author: Fan
 * @date 2020.11.3 17:28
 */
public class LeetCode941 {

  public static void main(String[] args) {
    int[] A = {2, 5, 47, 8, 5, 2, 69, 7, 45, 2, 3, 58, 1, 6};
    int[] B = {1, 3, 2};
    System.out.println(validMountainArray1(B));
  }

  public static boolean validMountainArray1(int[] A) {
    int N = A.length;
    int i = 0;

    // 递增扫描
    while (i + 1 < N && A[i] < A[i + 1]) {
      i++;
    }

    // 最高点不能是数组的第一个位置或最后一个位置
    if (i == 0 || i == N - 1) {
      return false;
    }

    // 递减扫描
    while (i + 1 < N && A[i] > A[i + 1]) {
      i++;
    }

    return i == N - 1;
  }

}
