package me.kernelfreeze.uhc.util;

import java.util.*;

public class ValueComparator implements Comparator<String>
{
    private Map<String, Integer> base;
    
    public ValueComparator(final Map<String, Integer> base) {
        this.base = base;
    }
    
    @Override
    public int compare(final String s, final String s2) {
        if (this.base.get(s) >= this.base.get(s2)) {
            return -1;
        }
        return 1;
    }
}
