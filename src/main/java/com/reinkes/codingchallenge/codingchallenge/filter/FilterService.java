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
import com.reinkes.codingchallenge.codingchallenge.domain.input.NodeType;
import com.reinkes.codingchallenge.codingchallenge.exception.NoResultException;
import com.reinkes.codingchallenge.codingchallenge.filter.vo.FilteredLinkVO;

@Component
public class FilterService {

	@Autowired
	private ApiService apiService;

	public Optional<ArrayList<FilteredLinkVO>> findLinks(Optional<String> parent) throws NoResultException {
		Optional<Navigation> navigationData = apiService.fetchDataFromAPI();
		if (navigationData.isPresent()) {
			return filterNavigationData(navigationData.get(), parent);
		} else if (parent.isPresent()) {
			throw new NoResultException(parent.get());
		} else {
			throw new NoResultException();
		}
	}

	private Optional<ArrayList<FilteredLinkVO>> filterNavigationData(Navigation navigation, Optional<String> parent)
			throws NoResultException {
		ArrayList<FilteredLinkVO> links = filterForURL(navigation, NodeType.link, parent);
		if (!links.isEmpty()) {
			return Optional.of(filterForURL(navigation, NodeType.link, parent));
		} else {
			throw new NoResultException(parent.get());
		}
	}

	private ArrayList<FilteredLinkVO> filterForURL(Navigation navigation, NodeType link, Optional<String> parent)
			throws NoResultException {
		ArrayList<FilteredLinkVO> results = new ArrayList<>();
		if (!navigation.getNavigationEntries().isEmpty()) {
			for (Node node : navigation.getNavigationEntries()) {
				filterNodes(results, link, node, new LinkedList<String>(), parent);
			}
		} else {
			throw new NoResultException(parent.get());
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private void filterNodes(ArrayList<FilteredLinkVO> results, NodeType link, Node node, LinkedList<String> path,
			Optional<String> parent) {
		if (node != null && !link.equals(node.getType()) && node.getChildren() != null
				&& !node.getChildren().isEmpty()) {

			path.add(node.getLabel());
			for (Node n : node.getChildren()) {
				filterNodes(results, link, n, path, parent);
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
