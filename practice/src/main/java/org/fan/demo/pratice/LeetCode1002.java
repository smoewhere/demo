package org.fan.demo.pratice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给定仅有小写字母组成的字符串数组 A，返回列表中的每个字符串中都显示的全部字符（包括重复字符）组成的列表。例如，如果一个字符在每个字符串中出现 3 次，但不是 4 次，则需要在最终答案中包含该字符 3 次。
 * <p>
 * 你可以按任意顺序返回答案。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：["bella","label","roller"] 输出：["e","l","l"] 示例 2：
 * <p>
 * 输入：["cool","lock","cook"] 输出：["c","o"]  
 * <p>
 * 提示：
 * <p>
 * 1 <= A.length <= 100 1 <= A[i].length <= 100 A[i][j] 是小写字母
 * <p>
 * 来源：力扣（LeetCode） 链接：https://leetcode-cn.com/problems/find-common-characters 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author Fan
 * @version 1.0
 * @date 2020.10.14 21:49
 */
public class LeetCode1002 {

  public static void main(String[] args) {
    String[] A = {"bella", "label", "roller"};
    List<String> list = commonChars(A);
    System.out.println(list);
  }

  public static List<String> commonChars(String[] A) {
    int[] minfreq = new int[26];
    Arrays.fill(minfreq, Integer.MAX_VALUE);
    for (String word : A) {
      int[] freq = new int[26];
      int length = word.length();
      for (int i = 0; i < length; ++i) {
        char ch = word.charAt(i);
        ++freq[ch - 'a'];
      }
      for (int i = 0; i < 26; ++i) {
        minfreq[i] = Math.min(minfreq[i], freq[i]);
      }
    }

    List<String> ans = new ArrayList<String>();
    for (int i = 0; i < 26; ++i) {
      for (int j = 0; j < minfreq[i]; ++j) {
        ans.add(String.valueOf((char) (i + 'a')));
      }
    }
    return ans;
  }

}
