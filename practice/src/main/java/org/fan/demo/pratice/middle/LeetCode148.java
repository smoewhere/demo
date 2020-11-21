package org.fan.demo.pratice.middle;


/**
 *
 * 给你链表的头结点 head ，请将其按 升序 排列并返回 排序后的链表 。
 *
 * 进阶：
 *
 * 你可以在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序吗？
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/sort-list
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author Fan
 * @version 1.0
 * @date 2020.11.21 21:47
 */
public class LeetCode148 {

  public ListNode sortList(ListNode head) {
    return sortList(head, null);
  }

  public ListNode sortList(ListNode head, ListNode tail) {
    if (head == null) {
      return head;
    }
    if (head.next == tail) {
      head.next = null;
      return head;
    }
    ListNode slow = head, fast = head;
    while (fast != tail) {
      slow = slow.next;
      fast = fast.next;
      if (fast != tail) {
        fast = fast.next;
      }
    }
    ListNode mid = slow;
    ListNode list1 = sortList(head, mid);
    ListNode list2 = sortList(mid, tail);
    return merge(list1, list2);
  }

  public ListNode merge(ListNode head1, ListNode head2) {
    ListNode dummyHead = new ListNode(0);
    ListNode temp = dummyHead, temp1 = head1, temp2 = head2;
    while (temp1 != null && temp2 != null) {
      if (temp1.val <= temp2.val) {
        temp.next = temp1;
        temp1 = temp1.next;
      } else {
        temp.next = temp2;
        temp2 = temp2.next;
      }
      temp = temp.next;
    }
    if (temp1 != null) {
      temp.next = temp1;
    } else if (temp2 != null) {
      temp.next = temp2;
    }
    return dummyHead.next;
  }

  public static class ListNode {
     int val;
     ListNode next;
     ListNode() {}
     ListNode(int val) { this.val = val; }
     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 }

}
