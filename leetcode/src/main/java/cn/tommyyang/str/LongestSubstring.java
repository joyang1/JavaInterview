package cn.tommyyang.str;

import java.util.HashSet;
import java.util.Set;

/**
 * @author TommyYang on 2019-04-08
 */
public class LongestSubstring {

    public int lengthofLongestSubstring(String s){
        int i = 0, j = 0, maxLen = 0;
        Set<Character>  set = new HashSet<Character>();
        int n = s.length();
        while (i < n && j < n){
            if (!set.contains(s.charAt(j))){
                set.add(s.charAt(j++));
                maxLen = Math.max(maxLen, j - i);
            } else {
                set.remove(s.charAt(i++));
            }
        }
        return maxLen;
    }

}
