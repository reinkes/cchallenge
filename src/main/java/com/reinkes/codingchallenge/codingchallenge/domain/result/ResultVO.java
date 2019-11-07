package com.reinkes.codingchallenge.codingchallenge.domain.result;

public class ResultVO {

	private String label;
	private String url;
	
	public ResultVO(String label, String url) {
		this.url = url;
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
