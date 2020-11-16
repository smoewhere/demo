package org.fan.demo.pratice.middle;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 假设有打乱顺序的一群人站成一个队列。 每个人由一个整数对(h, k)表示，其中h是这个人的身高，k是排在这个人前面且身高大于或等于h的人数。
 * <p>
 * 编写一个算法来重建这个队列。
 * <p>
 * 注意： 总人数少于1100人。
 * <p>
 * 示例
 * <p>
 * 输入: [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
 * <p>
 * 输出: [[5,0], [7,0], [5,2], [6,1], [4,4], [7,1]]
 * <p>
 * 来源：力扣（LeetCode） 链接：https://leetcode-cn.com/problems/queue-reconstruction-by-height
 *
 * @version 1.0
 * @author: Fan
 * @date 2020.11.16 15:29
 */
public class LeetCode406 {

  public static void main(String[] args) {
    int[][] people = {{7,0}, {4,4}, {7,1}, {5,0}, {6,1}, {5,2}};
    new LeetCode406().reconstructQueue(people);
  }

  /**
   * 解题思路：先按照身高进行排序，然后按照前面有几个人进行排序。因为k表示前面有几个人大于或者等于身高，先按照身高降序排列，
   * 那么只有前面的人才会对后面有影响，那么只要根据k的大小进行插入队列就行。
   *
   * @param people people
   * @return int[][]
   */
  public int[][] reconstructQueue(int[][] people) {
    Arrays.sort(people, (a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);
    List<int[]> list = new LinkedList<>();
    for (int[] p : people) {
      list.add(p[1], p);
    }
    return list.toArray(new int[0][2]);
  }

}
