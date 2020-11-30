package org.fan.demo.pratice.middle;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 给定一个字符串S，检查是否能重新排布其中的字母，使得两相邻的字符不同。
 *
 * 若可行，输出任意可行的结果。若不可行，返回空字符串。
 *
 * 示例1:
 *
 * 输入: S = "aab"
 * 输出: "aba"
 * 示例 2:
 *
 * 输入: S = "aaab"
 * 输出: ""
 * 注意:
 *
 * S 只包含小写字母并且长度在[1, 500]区间内。
 *
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * 贪心算法：只要字符串中出现最多的一个字母都可以符合条件，则所有的都可以
 * 当 nn 是偶数时，有 n/2n/2 个偶数下标和 n/2n/2 个奇数下标，因此每个字母的出现次数都不能超过 n/2n/2 次，否则出现次数最多的字母一定会出现相邻。
 *
 * 当 nn 是奇数时，由于共有 (n+1)/2(n+1)/2 个偶数下标，因此每个字母的出现次数都不能超过 (n+1)/2(n+1)/2 次，否则出现次数最多的字母一定会出现相邻。
 *
 * 作者：LeetCode-Solution
 * 链接：https://leetcode-cn.com/problems/reorganize-string/solution/zhong-gou-zi-fu-chuan-by-leetcode-solution/
 * 来源：力扣（LeetCode）
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * @version 1.0
 * @author: Fan
 * @date 2020.11.30 13:02
 */
public class LeetCode767 {

  public String reorganizeString(String S) {
    if (S.length() < 2) {
      return S;
    }
    int[] counts = new int[26];
    int maxCount = 0;
    int length = S.length();
    for (int i = 0; i < length; i++) {
      char c = S.charAt(i);
      counts[c - 'a']++;
      maxCount = Math.max(maxCount, counts[c - 'a']);
    }
    if (maxCount > (length + 1) / 2) {
      return "";
    }
    PriorityQueue<Character> queue = new PriorityQueue<Character>(new Comparator<Character>() {
      public int compare(Character letter1, Character letter2) {
        return counts[letter2 - 'a'] - counts[letter1 - 'a'];
      }
    });
    for (char c = 'a'; c <= 'z'; c++) {
      if (counts[c - 'a'] > 0) {
        queue.offer(c);
      }
    }
    StringBuffer sb = new StringBuffer();
    while (queue.size() > 1) {
      char letter1 = queue.poll();
      char letter2 = queue.poll();
      sb.append(letter1);
      sb.append(letter2);
      int index1 = letter1 - 'a', index2 = letter2 - 'a';
      counts[index1]--;
      counts[index2]--;
      if (counts[index1] > 0) {
        queue.offer(letter1);
      }
      if (counts[index2] > 0) {
        queue.offer(letter2);
      }
    }
    if (queue.size() > 0) {
      sb.append(queue.poll());
    }
    return sb.toString();
  }
}
