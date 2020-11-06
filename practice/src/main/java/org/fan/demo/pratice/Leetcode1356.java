package org.fan.demo.pratice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 给你一个整数数组arr。请你将数组中的元素按照其二进制表示中数字 1 的数目升序排序。
 * <p>
 * 如果存在多个数字二进制中1的数目相同，则必须将它们按照数值大小升序排列。
 * <p>
 * 请你返回排序后的数组。 示例 1：
 * <p>
 * 输入：arr = [0,1,2,3,4,5,6,7,8] 输出：[0,1,2,4,8,3,5,6,7] 解释：[0] 是唯一一个有 0 个 1 的数。 [1,2,4,8] 都有 1 个 1 。 [3,5,6] 有 2 个 1 。
 * [7] 有 3 个 1 。 按照 1 的个数排序得到的结果数组为 [0,1,2,4,8,3,5,6,7] 示例 2：
 * <p>
 * 输入：arr = [1024,512,256,128,64,32,16,8,4,2,1] 输出：[1,2,4,8,16,32,64,128,256,512,1024] 解释：数组中所有整数二进制下都只有 1 个 1
 * ，所以你需要按照数值大小将它们排序。 示例 3：
 * <p>
 * 输入：arr = [10000,10000] 输出：[10000,10000] 示例 4：
 * <p>
 * 输入：arr = [2,3,5,7,11,13,17,19] 输出：[2,3,5,17,7,11,13,19] 示例 5：
 * <p>
 * 输入：arr = [10,100,1000,10000] 输出：[10,100,10000,1000]
 * <p>
 * 提示：
 * <p>
 * 1 <= arr.length <= 500 0 <= arr[i] <= 10^4
 * <p>
 * 来源：力扣（LeetCode） 链接：https://leetcode-cn.com/problems/sort-integers-by-the-number-of-1-bits
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @version 1.0
 * @author: Fan
 * @date 2020.11.6 16:57
 */
public class Leetcode1356 {

  public static void main(String[] args) {
    int[] arr = {1024,512,256,128,64,32,16,8,4,2,1};
    int[] sort = new Leetcode1356().sortByBits(arr);
    System.out.println(Arrays.toString(sort));
  }

  public int[] sortByBits(int[] arr) {
    Integer[] integers = new Integer[arr.length];
    for (int i = 0; i < arr.length; i++) {
      integers[i] = arr[i];
    }
    Arrays.sort(integers, new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        int count1 = getCount(o1);
        int count2 = getCount(o2);
        if (count1 > count2) {
          return 1;
        } else if (count1 < count2) {
          return -1;
        } else {
          return o1 - o2;
        }
      }
    });
    for (int i = 0; i < arr.length; i++) {
      arr[i] = integers[i];
    }
    return arr;
  }

  private static int getCount(int x) {
    int count = 0;
    while (x != 0) {
      if (x % 2 == 1) {
        count++;
      }
      x = x / 2;
    }
    return count;
  }

}
