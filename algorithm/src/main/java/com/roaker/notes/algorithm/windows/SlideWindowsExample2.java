package com.roaker.notes.algorithm.windows;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 给定一个整形数组arr,和一个证书num
 * 某个arr中的子数组sub,如果想达标,必须满足:
 * sub中的最大值-sub中的最小值<=num,返回这样的子数组数量
 *
 * @author lei.rao
 * @since 1.0
 */
public class SlideWindowsExample2 {
    public static void main(String[] args) {
        Integer[] arr = getRandomArray(10).toArray(new Integer[0]);
        Assertions.assertThat(solutionBao(arr, 3))
                .isEqualTo(solution(arr, 3));
    }

    public static int solution(Integer[] arr, int num) {
        //核心思想:
        //1、子数组如何取?固定左下标,然后右下标依次往后移动,就可以得到某一下标开头的所有子数组了
        //2、如果某个子数组有效,则该数组内的所有子数组也是有效的,为啥?
        // 对于sub1[l1,r1], max[sub1] - min[sub1] <= num
        // 对于sub2[l2 <= l1, r2 <= r2], 则max[sub2]<=max[sub1], min[sub2] >= min[sub1], 则max[sub2]-min[sub2]<=max[sub1] - min[sub1] <= num
        // 而对于以l1开头的子数组数量,不就是r1 - l1种吗?而我们要做的就是寻找这样的子数组sub1即可,直到sub1中的l1 >= arr.length()
        int right = 0;
        int left = 0;
        int res = 0;
        LinkedList<Integer> maxWindows = new LinkedList<>();
        LinkedList<Integer> minWindows = new LinkedList<>();
        while (left < arr.length) {
            //走到第一个left开头但是不达标的位置,则arr[left,right - 1]就是最大有效子数组
            while (right < arr.length) {
                //3、最小值
                while (!minWindows.isEmpty() && arr[minWindows.peekLast()] >= arr[right]) {
                    minWindows.pollLast();
                }
                minWindows.addLast(right);
                //4、最大值
                while (!maxWindows.isEmpty() && arr[maxWindows.peekLast()] <= arr[right]) {
                    maxWindows.pollLast();
                }
                maxWindows.addLast(right);
                if (!maxWindows.isEmpty() && !minWindows.isEmpty()) {
                    //5、找到第一个不达标的值
                    if (arr[maxWindows.peekFirst()] - arr[minWindows.peekFirst()] > num) {
                        break;
                    }
                }
                right++;
            }
            //此时换取下一个下标,但是left需要弹出去
            res += right - left;
            if (!maxWindows.isEmpty() && maxWindows.peekFirst() == left) {
                maxWindows.pollFirst();
            }
            if (!minWindows.isEmpty() && minWindows.peekFirst() == left) {
                minWindows.pollFirst();
            }
            left++;
        }
        return res;
    }

    public static int solutionBao(Integer[] arr, int num) {
        int res = 0;
        for (int left = 0; left < arr.length; left++) {
            for (int right = left; right < arr.length; right++) {
                int[] maxAndMin = maxAndMin(arr, left, right);
                if (maxAndMin[0] - maxAndMin[1] <= num) {
                    res++;
                } else {
                    break;
                }
            }
        }
        return res;
    }

    public static List<Integer> getRandomArray(int length) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            res.add(RandomUtils.nextInt(0, 20));
        }
        return res;
    }

    public static int[] maxAndMin(Integer[] arr, int left, int right) {
        int[] res = new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int i = left; i <= right; i++) {
            if (arr[i] >= res[0]) {
                res[0] = arr[i];
            }
            if (arr[i] <= res[1]) {
                res[1] = arr[i];
            }
        }
        return res;
    }


}
