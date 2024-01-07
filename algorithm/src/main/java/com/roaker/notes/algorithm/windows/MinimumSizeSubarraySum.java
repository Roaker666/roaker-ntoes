package com.roaker.notes.algorithm.windows;

import org.assertj.core.api.Assertions;

/**
 * @author lei.rao
 * @since 1.0
 */
public class MinimumSizeSubarraySum {
    public static void main(String[] args) {
        Assertions.assertThat(2).isEqualTo(solution(new int[]{2, 3, 1, 2, 4, 3}, 7));
        Assertions.assertThat(1).isEqualTo(solution(new int[]{1, 4, 4}, 4));
        Assertions.assertThat(0).isEqualTo(solution(new int[]{1, 1, 1, 1, 1, 1, 1, 1}, 11));
    }

    public static int solution(int[] arr, int target) {
        if (arr == null || arr.length ==0) {
            return 0;
        }
        int sum = 0;
        int res = Integer.MAX_VALUE;
        for (int i = 0, j = 0; i <= j && j <= arr.length; ) {
            //窗口和大于target,可以考虑移除左边数子
            if (sum >= target) {
                res = Math.min(res, j - i);
                sum -= arr[i];
                i++;
            } else if (j < arr.length) {
                //小于target,则增加数字到队列中
                sum += arr[j];
                j++;
            } else {
                break;
            }
        }
        return res == Integer.MAX_VALUE ? 0: res;
    }
}
