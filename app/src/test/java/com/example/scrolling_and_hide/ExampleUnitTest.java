package com.example.scrolling_and_hide;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        int[] nums = {2, 11, 15, 7}; int target = 9;

        int[] ans = twoSum(nums, target);
        System.out.println(ans[0] + " " + ans[1]);
    }


        public int[] twoSum(int[] nums, int target) {
            int [] ans = new int[2];

            for (int i=0; i<nums.length; i++) {
                for (int j=i+1; i<nums.length; j++) {
                    if ((nums[i] + nums[j]) == target) {
                        ans[0] = i;
                        ans[1] = j;
                        return ans;
                    }
                }
            }
            return ans;
        }

}