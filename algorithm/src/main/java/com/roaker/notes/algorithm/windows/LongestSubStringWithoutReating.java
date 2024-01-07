package com.roaker.notes.algorithm.windows;

import org.assertj.core.api.Assertions;

import java.util.HashSet;
import java.util.Set;

/**
 * Example 1:
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 * Example 2:
 * Input: s = "bbbbb"
 * Output: 1
 * Explanation: The answer is "b", with the length of 1.
 * Example 3:
 * Input: s = "pwwkew"
 * Output: 3
 * Explanation: The answer is "wke", with the length of 3.
 * Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
 *
 * @author lei.rao
 * @since 1.0
 */
public class LongestSubStringWithoutReating {
    public static void main(String[] args) {
        Assertions.assertThat(3).isEqualTo(solution("abcabcbb"));
        Assertions.assertThat(1).isEqualTo(solution("bbbbb"));
        Assertions.assertThat(3).isEqualTo(solution("pwwkew"));
    }


    /**
     * 如果arr[left...i...j]中i,j位置的元素重复,则最大为j-1-left+1
     * 如果想要求取数组的最大不重复长度,则必须考虑j以及j之后的元素,而这就必须移除i位置以及其之前的所有元素
     * 如此才可能找到比arr[left...i...j]长度最大的子数组
     *
     */
    public static int solution(String s) {
        int res = 0;
        //记录元素是否存在
        Set<Character> set = new HashSet<>();
        char[] charArr = s.toCharArray();
        int left = 0, right = 0;
        for (left = 0, right = 0; left < charArr.length && right < charArr.length; right++) {
            //1、如果遇见一个重复的,则从左边一直移除,直到没有重复的
            while (set.contains(charArr[right])) {
                set.remove(charArr[left++]);
            }
            set.add(charArr[right]);
            res = Math.max(res, right - left + 1);
        }
        return res;
    }
}
