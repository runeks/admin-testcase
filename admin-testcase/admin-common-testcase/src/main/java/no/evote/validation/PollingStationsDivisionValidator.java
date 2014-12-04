package no.evote.validation;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.evote.constants.EvoteConstants;
import no.evote.dto.PollingStationDivisionDto;

public class PollingStationsDivisionValidator {

	public static final String VALIDATION_FEEDBACK_NOT_WHOLE_ALPHABET = "@config.polling_stations.divisionList.notWholeAlphabet";
	public static final String VALIDATION_FEEDBACK_DOENST_START_WITH_A = "@config.polling_stations.divisionList.doesntStartWithA";
	public static final String VALIDATION_FEEDBACK_RANGES_NOT_FOLLOWING = "@config.polling_stations.divisionList.rangesNotFollowing";
	public static final String VALIDATION_FEEDBACK_ILLEGAL_SIGNS = "@config.polling_stations.divisionList.illegalSigns";
	public static final String VALIDATION_FEEDBACK_ONE_OR_TWO_LETTERS = "@config.polling_stations.divisionList.oneOrTwoLetters";
	public static final String VALIDATION_FEEDBACK_DIVISION_LIST_EMPTY = "@config.polling_stations.divisionList.empty";

	private String validationFeedback;

	public boolean isValid(final List<PollingStationDivisionDto> divisionList) {
		validationFeedback = null;
		if (divisionList == null) {
			validationFeedback = VALIDATION_FEEDBACK_DIVISION_LIST_EMPTY;
			return false;
		} else if (divisionList.isEmpty()) {
			validationFeedback = VALIDATION_FEEDBACK_DIVISION_LIST_EMPTY;
			return false;
		}

		normalizeDivisionList(divisionList);
		if (!checkListDontContainsAnythingButTheAlphabet(divisionList)) {
			validationFeedback = VALIDATION_FEEDBACK_ILLEGAL_SIGNS;
			return false;
		} else if (!"A".equals(divisionList.get(0).getFirst())) {
			validationFeedback = VALIDATION_FEEDBACK_DOENST_START_WITH_A;
			return false;
		} else if (!checkDividorLength(divisionList)) {
			validationFeedback = VALIDATION_FEEDBACK_ONE_OR_TWO_LETTERS;
			return false;
		} else if (!checkNoMixtureOfDividorLength(divisionList)) {
			validationFeedback = VALIDATION_FEEDBACK_ONE_OR_TWO_LETTERS;
			return false;
		} else if (!doesRangesFollowEachother((divisionList))) {
			validationFeedback = VALIDATION_FEEDBACK_RANGES_NOT_FOLLOWING;
			return false;
		} else if (!divisionCoversTheWholeAlphabet(divisionList)) {
			validationFeedback = VALIDATION_FEEDBACK_NOT_WHOLE_ALPHABET;
			return false;
		}

		return true;
	}

	private boolean checkDividorLength(final List<PollingStationDivisionDto> divisionList) {
		for (PollingStationDivisionDto psdd : divisionList) {
			if (psdd.getFirst().length() < 1 || psdd.getFirst().length() > 2 || psdd.getLast().length() < 1 || psdd.getLast().length() > 2) {
				return false;
			}
		}

		return true;
	}

	private boolean checkNoMixtureOfDividorLength(final List<PollingStationDivisionDto> divisionList) {
		int lengthOnFirst = divisionList.get(0).getLast().length();
		for (PollingStationDivisionDto psdd : divisionList) {

			if (!psdd.getFirst().equals("A") && psdd.getFirst().length() != lengthOnFirst) {
				return false;
			}
			if (psdd.getLast().length() != lengthOnFirst) {
				return false;
			}

		}

		return true;
	}

	private void normalizeDivisionList(final List<PollingStationDivisionDto> divisionList) {
		for (PollingStationDivisionDto psdd : divisionList) {
			psdd.setFirst(psdd.getFirst().toUpperCase());
			psdd.setLast(psdd.getLast().toUpperCase());
		}

		Collections.sort(divisionList, new Comparator<PollingStationDivisionDto>() {

			@Override
			public int compare(final PollingStationDivisionDto o1, final PollingStationDivisionDto o2) {
				return o1.getFirst().compareTo(o2.getFirst());
			}
		});
	}

	private boolean checkListDontContainsAnythingButTheAlphabet(final List<PollingStationDivisionDto> divisionList) {
		for (PollingStationDivisionDto psdd : divisionList) {
			for (char c : psdd.getFirst().toCharArray()) {
				if (!EvoteConstants.ALPHABET.contains(Character.toString(c))) {
					return false;
				}

			}
			for (char c : psdd.getLast().toCharArray()) {
				if (!EvoteConstants.ALPHABET.contains(Character.toString(c))) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean divisionCoversTheWholeAlphabet(final List<PollingStationDivisionDto> divisionList) {
		if (divisionList.get(0).getLast().length() == 2) {
			return divisionCoversTheWholeAlphabetTwoLetter(divisionList);
		} else if (divisionList.get(0).getLast().length() == 1) {
			return divisionCoversTheWholeAlphabetOneLetter(divisionList);
		} else {
			return false;
		}

	}

	private boolean divisionCoversTheWholeAlphabetOneLetter(final List<PollingStationDivisionDto> divisionList) {
		// Does the last range end with the last letter of the alphabet?
		boolean endsWithO = Character.toString(EvoteConstants.ALPHABET.charAt(EvoteConstants.ALPHABET.length() - 1)).equals(
				divisionList.get(divisionList.size() - 1).getLast());

		return endsWithO;

	}

	// Test that the ranges are in right order ant the and the every letter is covered
	private boolean doesRangesFollowEachother(final List<PollingStationDivisionDto> divisionList) {
		if (divisionList.get(0).getLast().length() == 2) {
			return doesRangesFollowEachotherTwoLetter(divisionList);
		} else if (divisionList.get(0).getLast().length() == 1) {
			return doesRangesFollowEachotherOneLetter(divisionList);
		} else {
			return false;
		}
	}

	private boolean doesRangesFollowEachotherOneLetter(final List<PollingStationDivisionDto> divisionList) {
		for (int i = 0; i < divisionList.size(); i++) {
			// Check that the next range starts with the letter after with which this one ends
			if ((i < divisionList.size() - 1) && (divisionList.get(i + 1).getFirst().charAt(0) != getNextInAlphabet(divisionList.get(i).getLast().charAt(0)))) {
				return false;
			}
		}
		return true;
	}

	private boolean doesRangesFollowEachotherTwoLetter(final List<PollingStationDivisionDto> divisionList) {
		for (int i = 0; i < divisionList.size(); i++) {
			// Check that the next range starts with the letter after with which this one ends
			if ((i < divisionList.size() - 1) && (!divisionList.get(i + 1).getFirst().equals(getNextAlphabeticCombination(divisionList.get(i).getLast())))) {
				return false;
			}
		}
		return true;
	}

	private boolean divisionCoversTheWholeAlphabetTwoLetter(final List<PollingStationDivisionDto> divisionList) {
		// Does the last range end with the last letter of the alphabet?
		boolean endsWithOO = (Character.toString(EvoteConstants.ALPHABET.charAt(EvoteConstants.ALPHABET.length() - 1)) + Character
				.toString(EvoteConstants.ALPHABET.charAt(EvoteConstants.ALPHABET.length() - 1))).equals(divisionList.get(divisionList.size() - 1).getLast());

		return endsWithOO;

	}

	private String getNextAlphabeticCombination(final String s) {
		if (s.charAt(1) != EvoteConstants.ALPHABET.charAt(EvoteConstants.ALPHABET.length() - 1)) {
			return Character.toString(s.charAt(0)) + getNextInAlphabet(s.charAt(1));
		} else {
			return Character.toString(getNextInAlphabet(s.charAt(0))) + Character.toString(EvoteConstants.ALPHABET.charAt(0));
		}

	}

	private Character getNextInAlphabet(final Character c) {
		return EvoteConstants.ALPHABET.charAt(EvoteConstants.ALPHABET.indexOf(c) + 1);
	}

	public String getValidationFeedback() {
		return validationFeedback;
	}
}
