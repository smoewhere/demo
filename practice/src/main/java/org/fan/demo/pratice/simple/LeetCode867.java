package org.fan.demo.pratice.simple;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.2.25 22:19
 */
public class LeetCode867 {

  public static void main(String[] args) {
    int[][] ints = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    int[][] transpose = transpose(ints);
    int x = transpose.length;
    int y = transpose[0].length;
    for (int[] value : transpose) {
      for (int j = 0; j < y; j++) {
        System.out.printf("%d \t", value[j]);
      }
      System.out.println("\n");
    }
  }

  public static int[][] transpose(int[][] matrix) {
    int x = matrix.length;
    int y = matrix[0].length;
    int[][] xirtam = new int[y][x];
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {
        xirtam[j][i] = matrix[i][j];
      }
    }
    return xirtam;
  }
}
