package org.fan.demo.pratice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 给你两个数组，arr1 和arr2，
 * <p>
 * arr2中的元素各不相同 arr2 中的每个元素都出现在arr1中 对 arr1中的元素进行排序，使 arr1 中项的相对顺序和arr2中的相对顺序相同。 未在arr2中出现过的元素需要按照升序放在arr1的末尾。
 * <p>
 * <p>
 * 示例：
 * <p>
 * 输入：arr1 = [2,3,1,3,2,4,6,7,9,2,19], arr2 = [2,1,4,3,9,6] 输出：[2,2,2,1,4,3,3,9,6,7,19]
 * <p>
 * 提示：
 * <p>
 * arr1.length, arr2.length <= 1000 0 <= arr1[i], arr2[i] <= 1000 arr2中的元素arr2[i]各不相同 arr2 中的每个元素arr2[i]都出现在arr1中
 * <p>
 * 来源：力扣（LeetCode） 链接：https://leetcode-cn.com/problems/relative-sort-array
 *
 * @author Fan
 * @version 1.0
 * @date 2020.11.14 22:45
 */
public class LeetCode1122 {

  public static void main(String[] args) {
    int[] arr1 = {2,3,1,3,2,4,6,7,9,2,19};
    int[] arr2 = {2,1,4,3,9,6};
    int[] array = new LeetCode1122().relativeSortArray(arr1, arr2);
    System.out.println(Arrays.toString(array));
  }



  public int[] relativeSortArray(int[] arr1, int[] arr2) {
    ArrayList<Integer> equals = new ArrayList<>();
    ArrayList<Integer> remain = new ArrayList<>();
    int k = 0;
    for (int value : arr2) {
      for (int i : arr1) {
        if (i == value) {
          equals.add(i);
        }
      }
    }
    for (int value : arr1) {
      boolean flag = false;
      for (int i : arr2) {
        if (i == value) {
          flag = true;
          break;
        }
      }
      if (!flag) {
        remain.add(value);
      }
    }
    Collections.sort(remain);
    equals.addAll(remain);
    for (Integer equal : equals) {
      arr1[k++] = equal;
    }
    return arr1;
  }

  /**
   * 官方
   */
  public int[] func(int[] arr1, int[] arr2){
    int upper = 0;
    for (int x : arr1) {
      upper = Math.max(upper, x);
    }
    int[] frequency = new int[upper + 1];
    for (int x : arr1) {
      ++frequency[x];
    }
    int[] ans = new int[arr1.length];
    int index = 0;
    for (int x : arr2) {
      for (int i = 0; i < frequency[x]; ++i) {
        ans[index++] = x;
      }
      frequency[x] = 0;
    }
    for (int x = 0; x <= upper; ++x) {
      for (int i = 0; i < frequency[x]; ++i) {
        ans[index++] = x;
      }
    }
    return ans;
  }
}
