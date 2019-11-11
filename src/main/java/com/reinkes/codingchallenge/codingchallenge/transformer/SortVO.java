package com.reinkes.codingchallenge.codingchallenge.transformer;

import com.reinkes.codingchallenge.codingchallenge.sort.SortKey;
import com.reinkes.codingchallenge.codingchallenge.sort.SortingDirection;

public class SortVO {

	private SortKey key;
	private SortingDirection direction;

	public SortVO(SortKey key, SortingDirection direction) {
		this.key = key;
		this.direction = direction;
	}

	public SortKey getKey() {
		return key;
	}
	
	public void setKey(SortKey key) {
		this.key = key;
	}
	
	public SortingDirection getDirection() {
		return direction;
	}
	
	public void setDirection(SortingDirection direction) {
		this.direction = direction;
	}
	
}
