package com.reinkes.codingchallenge.codingchallenge.filter.vo;

import java.util.LinkedList;

import org.springframework.util.StringUtils;

public class FilteredLinkVO {

	private LinkedList<String> path;
	private String url;
	
	public FilteredLinkVO(LinkedList<String> path, String url) {
		this.url = url;
		this.path = path;
	}
	
	public LinkedList<String> getPath() {
		return path;
	}
	
	public void setPath(LinkedList<String> path) {
		this.path = path;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String joinPath() {
		return StringUtils.collectionToDelimitedString(path, " - ");
	}
}
