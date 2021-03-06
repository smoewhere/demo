package org.fan.demo.pratice.middle;

/**
 * 给定一个由若干 0 和 1 组成的数组A，我们最多可以将K个值从 0 变成 1 。
 *
 * 返回仅包含 1 的最长（连续）子数组的长度。
 *
 * 示例 1：
 *
 * 输入：A = [1,1,1,0,0,0,1,1,1,1,0], K = 2
 * 输出：6
 * 解释： 
 * [1,1,1,0,0,1,1,1,1,1,1]
 * 粗体数字从 0 翻转到 1，最长的子数组长度为 6。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/max-consecutive-ones-iii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * 
 * @version 1.0
 * @author: Fan
 * @date 2021/2/19 14:21
 */
public class LeetCode1004 {

  public int longestOnes(int[] A, int K) {
    int n = A.length;
    int left = 0, lsum = 0, rsum = 0;
    int ans = 0;
    for (int right = 0; right < n; ++right) {
      rsum += 1 - A[right];
      while (lsum < rsum - K) {
        lsum += 1 - A[left];
        ++left;
      }
      ans = Math.max(ans, right - left + 1);
    }
    return ans;
  }
}
