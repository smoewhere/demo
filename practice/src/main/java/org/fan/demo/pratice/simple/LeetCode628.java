package org.fan.demo.pratice.simple;

import java.util.Arrays;

/**
 * 给定一个整型数组，在数组中找出由三个数组成的最大乘积，并输出这个乘积。
 * 注意:
 *
 * 1. 给定的整型数组长度范围是[3,104]，数组中所有的元素范围是[-1000, 1000]。
 * 2. 输入的数组中任意三个数的乘积不会超出32位有符号整数的范围。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-product-of-three-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @version 1.0
 * @author: Fan
 * @date 2021.1.20 19:25
 */
public class LeetCode628 {
  // 排序之后，分别求出三个最大正数的乘积，以及两个最小负数与最大正数的乘积，二者之间的最大值即为所求答案。
  public int maximumProduct(int[] nums) {
    Arrays.sort(nums);
    int n = nums.length;
    return Math.max(nums[0] * nums[1] * nums[n - 1], nums[n - 3] * nums[n - 2] * nums[n - 1]);
  }
}
