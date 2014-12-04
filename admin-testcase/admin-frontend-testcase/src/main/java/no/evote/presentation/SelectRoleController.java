package no.evote.presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.EvoteConstants;
import no.evote.exception.EvoteSecurityException;
import no.evote.model.ElectionEvent;
import no.evote.model.MvArea;
import no.evote.model.Operator;
import no.evote.model.OperatorRole;
import no.evote.model.Role;
import no.evote.security.UserData;
import no.evote.service.ElectionEventService;
import no.evote.service.OperatorRoleService;
import no.evote.service.RoleService;
import no.evote.service.SigningKeyService;
import no.valg.eva.admin.common.rbac.service.NewUserDataService;

import org.jboss.weld.context.http.HttpConversationContext;

@Named
@ConversationScoped
public class SelectRoleController extends BaseController {

	private static final String MENU = "index.xhtml?faces-redirect=true";
	private static final long serialVersionUID = 5547722002660819324L;

	private List<ElectionEvent> electionEvents = null;
	private Map<Long, Integer> accumulatedSecLevelMap;
	private Map<ElectionEvent, List<OperatorRole>> operatorRolesPerElectionEvent = null;
	private final Map<Long, Boolean> electionEventSigningKeySetMap = new HashMap<>();

	@Inject
	private UserData userData;
	@Inject
	private transient HttpConversationContext conversationContext;
	@Inject
	private transient Conversation conversation;
	private boolean dataLoaded = false;

	@PostConstruct
	public void init() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		conversation.setTimeout(EvoteConstants.CONVERSATION_TIMEOUT);
		conversationContext.setDefaultTimeout(EvoteConstants.CONVERSATION_TIMEOUT);

		loadData();
	}

	private void loadData() {
		if (!dataLoaded) {
			dataLoaded = true;

			operatorRolesPerElectionEvent = new HashMap<>();
			ElectionEvent ee1 = new ElectionEvent("1", "valg1", null);
			List<OperatorRole> roleList = new ArrayList<>();
			OperatorRole oo1 = new OperatorRole();
			Operator o1 = new Operator();
			o1.setId(userData.getUid());
			oo1.setOperator(o1);
			
			roleList.add(oo1);
			
			operatorRolesPerElectionEvent.put(ee1, roleList);

			electionEvents = new ArrayList<>(operatorRolesPerElectionEvent.keySet());
			Collections.sort(electionEvents, new Comparator<ElectionEvent>() {

				@Override
				public int compare(final ElectionEvent o1, final ElectionEvent o2) {
					return o1.getId().compareTo(o2.getId());
				}
			});

		}
	}

	public Conversation getConversation() {
		return conversation;
	}

	public Collection<ElectionEvent> getElectionEvents() {
		return electionEvents;
	}

	/**
	 * Returns operator roles for an event, specified by the election event parameter.
	 * 
	 * @param electionEvent
	 * @return List<OperatorRole>
	 */
	public List<OperatorRole> getOperatorRolePerElectionEvent(final ElectionEvent electionEvent) {
		return new ArrayList<>(operatorRolesPerElectionEvent.get(electionEvent));
	}

	public String selectRole(final OperatorRole operatorRole) throws IOException {
//		ElectionEvent electionEvent = electionEventService.findByPk(operatorRole.getOperator().getElectionEvent().getPk());
//		if (!isSigningKeySetForElectionEventInCache(electionEvent) && (!electionEvent.getId().equals("000000"))) {
//			throw new EvoteSecurityException("P12 is not availible for this event");
//		}

//		if (userData.getSecurityLevel() != null && accumulatedSecLevelMap.get(operatorRole.getRole().getPk()) > userData.getSecurityLevel()) {
//			throw new EvoteSecurityException("Your current security level is lower than the requirement for this role");
//		}

//		if (!operatorRole.getRole().isActive()) {
//			throw new EvoteSecurityException("Your role is deactivated for this election event");
//		}
//
//		if (!operatorRole.getOperator().isActive()) {
//			throw new EvoteSecurityException("Your operator is deactivated for this election event");
//		}

//		UserData userDataFromBackend = userDataService.setAccessCacheOnUserData(userData, operatorRole);
//		userData.setOperatorRole(userDataFromBackend.getOperatorRole());
//		userData.setAccessCache(userDataFromBackend.getAccessCache());
//		userData.setClientAddress(userDataFromBackend.getClientAddress());
//		userData.setLocale(userDataFromBackend.getLocale());
//		userData.setSecurityLevel(userDataFromBackend.getSecurityLevel());

		userData.setUid(operatorRole.getOperator().getId());
		
		conversation.end();

		return MENU;
	}

	/*
	 * Instead of looking up the signing key for every operatorRole, we cache it pr. election event
	 */
	private boolean isSigningKeySetForElectionEventInCache(ElectionEvent electionEvent) {
		if (electionEventSigningKeySetMap.containsKey(electionEvent.getPk())) {
			return electionEventSigningKeySetMap.get(electionEvent.getPk());
		}
		return isSigningKeySetForElectionEvent(electionEvent);
	}

	private boolean isSigningKeySetForElectionEvent(final ElectionEvent electionEvent) {
//		if (electionEvent.getId().equals(EvoteConstants.ROOT_ELECTION_EVENT_ID)) {
//			electionEventSigningKeySetMap.put(electionEvent.getPk(), true);
//			return true;
//		}
//
//		if (electionEventSigningKeySetMap.containsKey(electionEvent.getPk())) {
//			return electionEventSigningKeySetMap.get(electionEvent.getPk());
//		} else {
//			boolean isSigningKeySet = keyService.isSigningKeySetForElectionEvent(userData, electionEvent);
//			electionEventSigningKeySetMap.put(electionEvent.getPk(), isSigningKeySet);
//			return isSigningKeySet;
//		}
		return true;
	}

	/*
	 * Checks that the OperatorRole can be choosen
	 * Criterias:
	 * * Signing p12 in place for election event
	 * * Security level for role lower or equal to the users
	 * * Role active
	 * * Operator active
	 */
	public boolean isOperatorRoleEnabled(final OperatorRole operatorRole) {
//		if (!isSigningKeySetForElectionEvent(operatorRole.getOperator().getElectionEvent())) {
//			return false;
//		}

//		if (userData.getSecurityLevel() != null && accumulatedSecLevelMap.get(operatorRole.getRole().getPk()) > userData.getSecurityLevel()) {
//			return false;
//		}

//		if (!operatorRole.getRole().isActive()) {
//			return false;
//		}
//
//		if (!operatorRole.getOperator().isActive()) {
//			return false;
//		}
		return true;
	}

	public Integer getAccumulatedSecLevel(final Role r) {
		return 3;
	}

	public List<OperatorRole> getOperatorRoles(final ElectionEvent electionEvent) {
		loadData();
		return operatorRolesPerElectionEvent.get(electionEvent);
	}

	public String getAreaName(OperatorRole role) {
		return "an area name";
	}

	public String getBaseAreaName(OperatorRole role) {
		return "a base area name";
	}

}
