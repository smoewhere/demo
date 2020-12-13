package org.fan.demo.pratice.simple;

import java.util.Arrays;

/**
 * 给定一个整数数组，判断是否存在重复元素。
 * <p>
 * 如果任意一值在数组中出现至少两次，函数返回 true 。如果数组中每个元素都不相同，则返回 false 。
 *
 * @author Fan
 * @version 1.0
 * @date 2020.12.13 22:42
 */
public class LeetCode217 {

  public boolean containsDuplicate(int[] nums) {
    Arrays.sort(nums);
    int n = nums.length;
    for (int i = 0; i < n - 1; i++) {
      if (nums[i] == nums[i + 1]) {
        return true;
      }
    }
    return false;
  }

}
