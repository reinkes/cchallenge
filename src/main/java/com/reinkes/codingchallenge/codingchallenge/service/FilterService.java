package com.reinkes.codingchallenge.codingchallenge.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reinkes.codingchallenge.codingchallenge.domain.Navigation;
import com.reinkes.codingchallenge.codingchallenge.domain.Node;
import com.reinkes.codingchallenge.codingchallenge.domain.result.ResultVO;

@Component
public class FilterService {

	@Autowired
	private ApiService apiService;

	public ArrayList<ResultVO> findLinks() {
		Optional<Navigation> res = apiService.fetchDataFromAPI();
		return filterForURL(res.get(), "link");
	}

	private ArrayList<ResultVO> filterForURL(Navigation navigation, String type) {
		ArrayList<ResultVO> results = new ArrayList<ResultVO>();
		if (!navigation.getNavigationEntries().isEmpty()) {
			for (Node node : navigation.getNavigationEntries()) {
				filterNodes(results, type, node, "");
			}
		}
		return results;
	}

	private void filterNodes(ArrayList<ResultVO> results, String type, Node node, String breadcrumb) {
		if (node != null && !type.equals(node.getType()) && node.getChildren() != null
				&& !node.getChildren().isEmpty()) {
			breadcrumb = !breadcrumb.isEmpty() ? breadcrumb + " - " + node.getLabel() : breadcrumb + node.getLabel();
			for (Node n : node.getChildren()) {
				filterNodes(results, type, n, breadcrumb);
			}
		} else {
			results.add(new ResultVO(breadcrumb, node.getUrl()));
		}
	}

}
