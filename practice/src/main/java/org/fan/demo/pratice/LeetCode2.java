package org.fan.demo.pratice;

/**
 * 给出两个非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照逆序的方式存储的，并且它们的每个节点只能存储一位数字。
 * <p>
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * <p>
 * 您可以假设除了数字 0 之外，这两个数都不会以 0开头。
 * <p>
 * 示例：
 * <p>
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4) 输出：7 -> 0 -> 8 原因：342 + 465 = 807
 * <p>
 * 来源：力扣（LeetCode） 链接：https://leetcode-cn.com/problems/add-two-numbers
 *
 * @version 1.0
 * @author: Fan
 * @date 2020.11.11 18:25
 */
public class LeetCode2 {

  public static void main(String[] args) {
    ListNode l1 = new ListNode(2, new ListNode(4, new ListNode(3)));
    ListNode l2 = new ListNode(5, new ListNode(6, new ListNode(4)));
    ListNode listNode = new LeetCode2().addTwoNumbers(l1, l2);
    while (listNode != null) {
      System.out.println(listNode.val);
      listNode = listNode.next;
    }
  }


  public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    ListNode head = null, tail = null;
    int carry = 0;
    while (l1 != null || l2 != null) {
      int n1 = l1 != null ? l1.val : 0;
      int n2 = l2 != null ? l2.val : 0;
      int sum = n1 + n2 + carry;
      if (head == null) {
        head = tail = new ListNode(sum % 10);
      } else {
        tail.next = new ListNode(sum % 10);
        tail = tail.next;
      }
      carry = sum / 10;
      if (l1 != null) {
        l1 = l1.next;
      }
      if (l2 != null) {
        l2 = l2.next;
      }
    }
    if (carry > 0) {
      tail.next = new ListNode(carry);
    }
    return head;
  }

}


class ListNode {

  int val;
  ListNode next;

  ListNode() {
  }

  ListNode(int val) {
    this.val = val;
  }

  ListNode(int val, ListNode next) {
    this.val = val;
    this.next = next;
  }
}