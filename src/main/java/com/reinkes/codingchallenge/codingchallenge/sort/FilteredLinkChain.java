package com.reinkes.codingchallenge.codingchallenge.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.reinkes.codingchallenge.codingchallenge.filter.vo.FilteredLinkVO;

public class FilteredLinkChain implements Comparator<FilteredLinkVO> {
	 
    private List<Comparator<FilteredLinkVO>> listComparators;
 
    @SafeVarargs
    public FilteredLinkChain(Comparator<FilteredLinkVO>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }
 
    @Override
    public int compare(FilteredLinkVO emp1, FilteredLinkVO emp2) {
        for (Comparator<FilteredLinkVO> comparator : listComparators) {
            int result = comparator.compare(emp1, emp2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
