package org.fan.demo.pratice.simple;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/23 18:34
 */
public class LeetCode7 {

  public static void main(String[] args) {
    LeetCode7 code7 = new LeetCode7();
    System.out.println(code7.reverse(-2147483648));
  }

  public int reverse(int x) {
    int y = 0;
    boolean j = x > 0;
    StringBuilder sb = new StringBuilder();
    do {
      y = x % 10;
      x = x /10;
      sb.append(Math.abs(y));
    } while (x != 0);
    long l = Long.parseLong(sb.toString());
    if (l > Integer.MAX_VALUE) {
      return 0;
    }
    if (j) {
      return (int)l;
    } else {
      return -(int)l;
    }
  }

}
