package no.valg.eva.admin.frontend.counting;

import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Sets.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import no.evote.util.Wrapper;
import no.valg.eva.admin.common.counting.model.modifiedballots.Candidate;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 */
public class PersonVotes {
	private final Map<Candidate, Wrapper<Boolean>> personalVoteWrappers = new TreeMap<>();

	public PersonVotes(Collection<Candidate> allCandidates, Set<Candidate> personalVotes) {
		for (Candidate candidate : allCandidates) {
			personalVoteWrappers.put(candidate, new Wrapper<>(false));
		}
		for (Candidate candidate : personalVotes) {
			personalVoteWrappers.put(candidate, new Wrapper<>(true));
		}
	}
	
	public List<Candidate> getCandidatesWithPersonalVotes() {
		return transform(new ArrayList<>(filter(personalVoteWrappers.entrySet(), PERSONAL_VOTE_FILTER)), UN_WRAPPER);
	}

	public List<Map.Entry<Candidate, Wrapper<Boolean>>> getWrappers() {
		return new ArrayList<>(personalVoteWrappers.entrySet());
	}
	
	private static final Predicate<Map.Entry<Candidate, Wrapper<Boolean>>> PERSONAL_VOTE_FILTER = new Predicate<Map.Entry<Candidate, Wrapper<Boolean>>>() {
		@Override
		public boolean apply(Map.Entry<Candidate, Wrapper<Boolean>> candidateWrapperEntry) {
			return candidateWrapperEntry.getValue() != null && candidateWrapperEntry.getValue().getValue();
		}
	};

	private static final Function<Map.Entry<Candidate, Wrapper<Boolean>>, Candidate> UN_WRAPPER = new Function<Map.Entry<Candidate, Wrapper<Boolean>>, Candidate>() {
		@Override
		public Candidate apply(Map.Entry<Candidate, Wrapper<Boolean>> candidateWrapperEntry) {
			return candidateWrapperEntry.getKey();
		}
	};
}
