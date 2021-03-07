package org.fan.demo.pratice.simple;

/**
 *
 * 给你一个 m x n 的整数网格 accounts ，其中 accounts[i][j] 是第 i位客户在第 j 家银行托管的资产数量。返回最富有客户所拥有的 资产总量 。
 *
 * 客户的 资产总量 就是他们在各家银行托管的资产数量之和。最富有客户就是 资产总量 最大的客户。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/richest-customer-wealth
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @version 1.0
 * @author: Fan
 * @date 2021.3.7 16:55
 */
public class LeetCode1672 {

  public static void main(String[] args) {
    int[][] account = new int[][]{{1,2,3},{3,2,1}};
    System.out.println(new LeetCode1672().maximumWealth(account));
  }

  public int maximumWealth(int[][] accounts) {
    if (accounts == null) {
      return 0;
    }
    int maxMoney = 0;
    int count = accounts.length;
    int account = accounts[0].length;
    for (int i = 0; i < count; i++) {
      int money = 0;
      for (int j = 0; j < account; j++) {
        money += accounts[i][j];
      }
      maxMoney = Math.max(money, maxMoney);
    }
    return maxMoney;
  }

}
