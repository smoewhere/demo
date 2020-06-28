package org.fan.demo.pratice;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Arrays;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.6.28 18:09
 */
@Slf4j
public class LeetCode209 {

    public static void main(String[] args) {
        int[] ints = LeetCode209.shortest(7, new int[]{7,2, 3, 1, 2, 4, 3});
        System.out.println(Arrays.toString(ints));
    }


    // 复杂度O2,遍历两次
    private static int[] shortest(int s, int[] nums) {
        long l = Instant.now().toEpochMilli();
        int len = nums.length;
        int first = 0;
        int last = 0;
        for (int i = 0; i < len; i++) {
            int rem = s;
            int j = i;
            int currentJ = i;
            do {
                rem = rem - nums[j++];
            } while (rem > 0 && j < len);
            if (rem == 0) {
                currentJ = --j;
                if (first == 0 && nums[0] != s) {
                    first = i;
                }
                if (last == 0 && nums[0] != s) {
                    last = currentJ;
                }
                if ((currentJ - i) < (last - first)) {
                    first = i;
                    last = currentJ;
                }
            }
        }
        if (last == 0) {
            int[] ints = new int[1];
            if (nums[0] == s) {
                System.arraycopy(nums, 0, ints, 0, 1);
            }
            System.out.println(Instant.now().toEpochMilli() - l);
            return ints;
        }
        int[] ints = new int[last - first + 1];
        System.arraycopy(nums, first, ints, 0, last - first + 1);
        System.out.println(Instant.now().toEpochMilli() - l);
        return ints;
    }

    // 使用滑动窗口的方式
    private static int[] shortestTwo(int s, int[] nums) {
        return new int[]{};
    }
}
