package com.reinkes.codingchallenge.codingchallenge.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reinkes.codingchallenge.codingchallenge.domain.api.ApiService;
import com.reinkes.codingchallenge.codingchallenge.domain.input.Navigation;
import com.reinkes.codingchallenge.codingchallenge.domain.input.Node;
import com.reinkes.codingchallenge.codingchallenge.filter.vo.FilteredLinkVO;

@Component
public class FilterService {

	@Autowired
	private ApiService apiService;

	public ArrayList<FilteredLinkVO> findLinks(Optional<String> parent) {
		Optional<Navigation> navigationData = apiService.fetchDataFromAPI();
		// TODO: "link" => Enum?
		return filterForURL(navigationData.get(), "link", parent);
	}
	
	private ArrayList<FilteredLinkVO> filterForURL(Navigation navigation, String type, Optional<String> parent) {
		ArrayList<FilteredLinkVO> results = new ArrayList<>();
		if (!navigation.getNavigationEntries().isEmpty()) {
			for (Node node : navigation.getNavigationEntries()) {
				filterNodes(results, type, node, new LinkedList<String>(), parent);
			}
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private void filterNodes(ArrayList<FilteredLinkVO> results, String type, Node node, LinkedList<String> path,
			Optional<String> parent) {
		if (node != null && !type.equals(node.getType()) && node.getChildren() != null
				&& !node.getChildren().isEmpty()) {

			path.add(node.getLabel());
			for (Node n : node.getChildren()) {
				filterNodes(results, type, n, path, parent);
			}
			path.removeLast();
		} else {
			if (!parent.isPresent() || pathContainsParent(path, parent)) {
				results.add(new FilteredLinkVO((LinkedList<String>) path.clone(), node.getUrl()));
			}
		}
	}

	private boolean pathContainsParent(LinkedList<String> path, Optional<String> parent) {
		return path.stream().filter(Objects::nonNull).filter(p -> p.equals(parent.get())).count() > 0;
	}

}
