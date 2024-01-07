### Slide Windows
顾名思义,就是一种可以滑动的窗口,一般来说我们使用2个下标(left, right)就可以实现一个滑动窗口,通过left以及right的加就可以控制窗口的移动、增加、减少等操作.
#### 更新方式
对于窗口内的值的更新,我们一般会使用最大值/最小值排序的方式,比如如果我们需要保证窗口内的值是逆序的,
当一个值从right进入窗口(right++),我们依次将该值与窗口的最后一个值比较,直到找到一个大于该值的位置即可
而将一个值从窗口左侧弹出,则仅仅需要判断需要弹出的这个值与窗口的第一个值是否相同,如果是则弹出,否则不变

#### 举例

##### 1、append
arr=[4,3,5,4,3,3,6,7]
<ol>
    <li>left=-1, right=0,此时第一个值从右边进入窗口,由于窗口为空,W={4}</li>
    <li>left=-1, right=1,此时第二个值从右边进入窗口,由于窗口内最后一个值(4)大于arr[right], W={4,3}</li>
    <li>left=-1, right=2,此时第三个值从右边进入窗口,由于窗口内最后一个值(3)小于arr[right],弹出最后一个值继续比较,
        发现最后一个值(4)依然小于arr[right],继续弹出4,此时W={5}</li>
    <li>...如此依序往后移动即可</li>
</ol>

##### 2、remove
先使用[1、append](#1append)的方式增加值,然后如果发现此时窗口已经满了(right-left>W),则需要将窗口的第一个值弹出去,例如W=3
<ol>
    <li>left=-1,right=3,此时W={5,4}; 我们将left=0,表示该值需要移动出窗口,我们发现arr[left]其实已经弹出窗口了,保持不变即可</li>
    <li>left=0,right=4,此时W={5,4,3}; 我们将left=1,我们发现arr[left]其实已经弹出窗口了,保持不变即可</li>
    <li>left=1,right=5,此时W={5,4,3,3}; 我们将left=2,我们发现窗口内的值刚好是left所对应的,直接弹出,W={4,3,3}</li>
    <li>left=2,right=6,此时W={6}; 我们将left=3,我们发现arr[left]其实已经弹出窗口了,保持不变即可</li>
    <li>依次循环...</li>
</ol>

#### 解决的问题
在我个人看来,滑动窗口解决的就是一类具有单调性问题,你首先需要知道你这个窗口需要的是什么值,求解的是什么问题,
然后根据你所求解的问题的规则来更新你的窗口结构,进入一个新元素时如何扩展你的窗口长度,窗口满的规则是什么,然后窗口
满了之后再如何删除不符合窗口结构的值
##### 例如
<ol>
    <li>如果你求解的是<a href="./SlideWindowsExample1.java" title="SlideWindowsExample1">SlideWindowsExample1</a>中的
    这种问题,当你的队列(窗口)每一次往右滑动时,你都需要将所遇到的值加入你的窗口,而你窗口满的规则就是当前窗口内的值大于3,此时就需要弹出一个元素,
    而弹出一个元素(left++),弹出的规则则是你弹出的这个值是不是当前窗口的最大值,如果是则弹出,否则的话什么都不用做</li>
    <li>而对于<a href="./SlideWindowsExample2.java" title="SlideWindowsExample2">SlideWindowsExample2</a>这种问题,
    当前值进入窗口则是规则则是需要满足当前窗口的最大值-最小值不超过所设定的值,而一旦不满足,则代表此时的窗口满(所以窗口满的规则就是当前
    窗口的最大值-最小值大于设定值),此时就需要从窗口内弹出值,使得窗口满足所规定的</li>
    <li>对于<a href="./MinimumSizeSubarraySum.java" title="MinimumSizeSubarraySum">MinimumSizeSubarraySum</a>这种问题,
    窗口满的规则则是窗口内所有的和大于设定值,此时就需要从窗口内弹出值,使得窗口满足所规定的</li>
    <li>对于<a href="./LongestSubStringWithoutReating.java" title="LongestSubStringWithoutReating">LongestSubStringWithoutReating</a>这种问题,
    你所求解是不重复的最大子数组,不难发现窗口满的规则则是窗口内出现了重复,而如果你要考虑其他未在窗口内的值,就必须将当前这个值弹出去使得你的窗口内没有重复值</li>
    <li>还有<a href="LongestSubStringWithAtLeastKRepeating.java">LongestSubStringWithAtLeastKRepeating</a>这个就比较不一样了😂,
    由于这个问题本身是求取窗口内的所有元素都大于K次,因此我们穷尽窗口内所有字母元素的情况[1-26种],如果遇到一个值进入窗口导致窗口的字母种类超过了当前
    窗口所设定的大小(元素种类),此时就需要从窗口内弹出一个值了,使得你的窗口依然满足才可以;</li>
<p>
而以上所有问题的单调性则在于,如果当前窗口不是最优解,那么他子窗口也一定不是最优解...
</p>
</ol>