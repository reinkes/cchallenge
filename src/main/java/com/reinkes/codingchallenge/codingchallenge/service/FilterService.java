package com.reinkes.codingchallenge.codingchallenge.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.reinkes.codingchallenge.codingchallenge.domain.input.Navigation;
import com.reinkes.codingchallenge.codingchallenge.domain.input.Node;
import com.reinkes.codingchallenge.codingchallenge.service.vo.FilteredLinkVO;
import com.reinkes.codingchallenge.codingchallenge.transformer.SortVO;

@Component
public class FilterService {

	@Autowired
	private ApiService apiService;

	public ArrayList<FilteredLinkVO> findLinks(Optional<String> parent, Optional<LinkedList<SortVO>> sortingKeys) {
		Optional<Navigation> navigationData = apiService.fetchDataFromAPI();
		// TODO: "link" => Enum?
		ArrayList<FilteredLinkVO> allLinks = filterForURL(navigationData.get(), "link", parent);
		
		return sortLinks(allLinks, sortingKeys);
	}

	private ArrayList<FilteredLinkVO> sortLinks(ArrayList<FilteredLinkVO> allLinks,
			Optional<LinkedList<SortVO>> sortingKeys) {
		// TODO Auto-generated method stub
		return null;
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
