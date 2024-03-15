package com.roaker.notes.algorithm.windows;


import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 假设一个固定大小为W的窗口,依次划过arr,返回每一次窗口经过的最大值
 * 例如, arr=[4,3,5,4,3,3,6,7],W=3
 * 返回:[5,5,5,4,6,7]
 * @author lei.rao
 * @since 1.0
 */
public class SlideWindowsExample1 {
    public static void main(String[] args) {
        //其实这就是一个典型的滑动窗口内的最大值/最小值问题
        //固定left不变,使用right依次往后移动,然后逆序更新我们的窗口W,当窗口满了之后,我们使用left弹出窗口的值,并且记录该返回值即可
        int[] arr = {4,3,5,4,3,3,6,7};
        int w=3;
        List<Integer> res= Arrays.asList(5,5,5,4,6,7);
        Assertions.assertThat(res).isEqualTo(solution(arr, w));
    }

    public static List<Integer> solution(int[] arr, int w) {
        List<Integer> res = new ArrayList<>();
        LinkedList<Integer> windows = new LinkedList<>();
        int right = 0;
        while (right < arr.length) {
            while (!windows.isEmpty() && arr[windows.peekLast()] <= arr[right]) {
                windows.pollLast();
            }
            windows.addLast(right);
            //如果此时窗口已经满了,再加进来right的话,窗口多的第一个值需要弹出去,我们只需要比较left是否与窗口中的一个下标是否相同,相同则更新否则不变(因为可能已经弹出)
            //left = right - w
            if (!windows.isEmpty() && windows.peekFirst() == right - w) {
                windows.pollFirst();
            }
            //对于窗口满的情况,记录最大值即可
            if (right - w + 1>= 0 && !windows.isEmpty()) {
                res.add(arr[windows.peekFirst()]);
            }
            right++;
        }
        return res;
    }
}
