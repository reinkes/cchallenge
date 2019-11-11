package com.reinkes.codingchallenge.codingchallenge.domain.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum NodeType {
	@JsonProperty("link")
	link,
	@JsonProperty("section")
	section,
	@JsonProperty("node")
	node
}
