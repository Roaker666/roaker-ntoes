package com.roaker.notes.algorithm.windows;

import org.assertj.core.api.Assertions;

/**
 * @author lei.rao
 * @since 1.0
 */
public class LongestSubStringWithAtLeastKRepeating {
    public static void main(String[] args) {
        Assertions.assertThat(solutionOfDivide("aaabb", 3)).isEqualTo(3);
        Assertions.assertThat(solutionOfSlideWindows("aaabb", 3)).isEqualTo(3);
        Assertions.assertThat(solutionOfDivide("ababbc", 2)).isEqualTo(5);
        Assertions.assertThat(solutionOfSlideWindows("ababbc", 2)).isEqualTo(5);
    }

    public static int solutionOfDivide(String s, int k) {
        int res = 0;
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        //如果存在某个字符的次数不足k次,则最长的子字符串一定不包含该字母
        char split = 0;
        for (int i = 0; i < 26; i++) {
            if (cnt[i] > 0 && cnt[i] < k) {
                split = (char) (i + 'a');
                break;
            }
        }
        //重点: 如果没有这样的字符?那么该字符串就是最长的了,因为他的子字符串一定比他自身小
        if (split == 0){
            return s.length();
        }
        //依次循环遍历该字符串,每一个不包含split的片段,然后取其中最大的子字符串即可
        int i = 0;
        while (i < s.length()) {
            while (i < s.length() && s.charAt(i) == split) {
                i++;
            }
            if (i>= s.length()) {
                break;
            }

            int start = i;
            while (i < s.length() && s.charAt(i) != split) {
                i++;
            }
            res = Math.max(res, solutionOfDivide(s.substring(start, i), k));
        }
        return res;
    }

    public static int solutionOfSlideWindows(String s, int k) {
        int res = 0;
        char[] charArr = s.toCharArray();
        for (int t = 1; t <= 26; t++) {
            //窗口内英文数目的种类t
            int left = 0, right = 0;
            //每个字母出现的次数
            int[] cnt = new int[26];
            //当前窗口内的英文字母种类,最多26,小于等于t
            int tot = 0;
            //当前窗口内出现次数未达到k的字母的数量
            int less = 0;
            while (right < charArr.length) {
                cnt[charArr[right] - 'a']++;
                //新进入的窗口的新类型字母
                if (cnt[charArr[right] - 'a'] == 1) {
                    tot++;
                    less++;
                }
                //如果出现了K次,则less的字母数量-1
                if (cnt[charArr[right] - 'a'] == k) {
                    less--;
                }
                //如果当前窗口内的英文字母种类太多,则需要移动滑动窗口,使得其数量等于t
                while (tot > t) {
                    cnt[charArr[left] - 'a']--;
                    //如果出现次数从k变为k-1,则次数不够的字母需要+1
                    if (cnt[charArr[left] - 'a'] == k - 1) {
                        less++;
                    }
                    //变为0,则总类目-1,次数不够的也-1
                    if (cnt[charArr[left] - 'a'] == 0) {
                        tot--;
                        less--;
                    }
                    left++;
                }
                //如果此时不足的字母数量为0,则代表此时窗口是满足条件的
                if (less == 0) {
                    res = Math.max(res, right - left + 1);
                }
                right++;
            }
        }
        return res;
    }
}
