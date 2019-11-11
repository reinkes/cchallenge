package com.reinkes.codingchallenge.codingchallenge.transformer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.util.StringUtils;

import com.reinkes.codingchallenge.codingchallenge.exception.UnexpectedParserException;
import com.reinkes.codingchallenge.codingchallenge.exception.UnknownSortingKeyException;
import com.reinkes.codingchallenge.codingchallenge.sort.SortKey;
import com.reinkes.codingchallenge.codingchallenge.sort.SortingDirection;

public class SortingTransformer {

	private String defaultSortingDirection;
	private List<String> validKeys;

	public SortingTransformer(String defaultSortingDirection, String[] validKeys) {
		this.defaultSortingDirection = defaultSortingDirection;
		this.validKeys = Arrays.asList(validKeys);
	}

	public Optional<LinkedList<SortVO>> transform(Optional<String> sorting)
			throws UnknownSortingKeyException, UnexpectedParserException {
		try {
			if (sorting.isPresent()) {
				return parseSorting(sorting);
			}
		} catch (UnknownSortingKeyException e) {
			throw e;
		} catch (Exception e) {
			throw new UnexpectedParserException(sorting.get());
		}
		return Optional.empty();
	}

	private Optional<LinkedList<SortVO>> parseSorting(Optional<String> sorting) throws UnknownSortingKeyException {
		String[] allKeys = sorting.orElse("").split(",");
		LinkedList<SortVO> sortVOs = new LinkedList<>();
		for (String singleKeyWithDirection : allKeys) {
			String[] singleKeyAndDirection = singleKeyWithDirection.split(":");
			if (!validKeys.contains(singleKeyAndDirection[0])) {
				throw new UnknownSortingKeyException("Unknown sorting key: " + singleKeyAndDirection[0]);
			}

			SortKey key = SortKey.valueOf(singleKeyAndDirection[0]);
			SortingDirection direction = SortingDirection.valueOf(this.defaultSortingDirection);
			if (singleKeyAndDirection.length > 1 && !StringUtils.isEmpty(singleKeyAndDirection[1])) {
				direction = SortingDirection.valueOf(singleKeyAndDirection[1]);
			}
			sortVOs.add(new SortVO(key, direction));
		}
		return Optional.of(sortVOs);
	}

}
