package org.fan.demo.pratice.simple;

/**
 * 数组的每个索引作为一个阶梯，第i个阶梯对应着一个非负数的体力花费值cost[i](索引从0开始)。
 *
 * 每当你爬上一个阶梯你都要花费对应的体力花费值，然后你可以选择继续爬一个阶梯或者爬两个阶梯。
 *
 * 您需要找到达到楼层顶部的最低花费。在开始时，你可以选择从索引为 0 或 1 的元素作为初始阶梯。
 *
 * 示例1:
 *
 * 输入: cost = [10, 15, 20]
 * 输出: 15
 * 解释: 最低花费是从cost[1]开始，然后走两步即可到阶梯顶，一共花费15。
 * 示例 2:
 *
 * 输入: cost = [1, 100, 1, 1, 1, 100, 1, 1, 100, 1]
 * 输出: 6
 * 解释: 最低花费方式是从cost[0]开始，逐个经过那些1，跳过cost[3]，一共花费6。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/min-cost-climbing-stairs
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * 
 * @version 1.0
 * @author: Fan
 * @date 2020.12.21 19:08
 */
public class LeetCode746 {

  /**
   * 动态规划问题，当走到第i个台阶的时候，看看是前一个台阶省力还是两个省力，
   * 每次都选择最省力的走法，则走完是最省力的。
   */
  public int minCostClimbingStairs(int[] cost) {
    int n = cost.length;
    int prev = 0, curr = 0;
    for (int i = 2; i <= n; i++) {
      int next = Math.min(curr + cost[i - 1], prev + cost[i - 2]);
      prev = curr;
      curr = next;
    }
    return curr;
  }

}
