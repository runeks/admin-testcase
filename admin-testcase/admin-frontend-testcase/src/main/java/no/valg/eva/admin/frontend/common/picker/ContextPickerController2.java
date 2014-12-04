package no.valg.eva.admin.frontend.common.picker;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import no.evote.presentation.BaseController;
import no.evote.presentation.components.Action;
import no.valg.eva.admin.frontend.common.PickerTable;

@Named
@ConversationScoped
public class ContextPickerController2 extends BaseController {
	private Action action;
	private List<PickerPanelProvider> pickerPanelProviders;
	private Map<String, PickerPanelProvider> pickerPanelProviderMap;

	public List<PickerPanelProvider> getPickerPanelProviders() {
		return pickerPanelProviders;
	}

	public void setPickerPanelProviders(PickerPanelProvider... pickerPanelProviders) {
		this.pickerPanelProviders = asList(pickerPanelProviders);
		this.pickerPanelProviderMap = new HashMap<>();
		for (int i = 0; i < pickerPanelProviders.length; i++) {
			String pickerPanelProviderId = pickerPanelProviders[i].getId();
			if (i + 1 < pickerPanelProviders.length) {
				pickerPanelProviders[i].setNextPickerPanelProvider(pickerPanelProviders[i + 1]);
			}
			if (i > 0) {
				pickerPanelProviders[i].setPreviousPickerPanelProvider(pickerPanelProviders[i - 1]);
			}
			pickerPanelProviderMap.put(pickerPanelProviderId, pickerPanelProviders[i]);
		}
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String select(String pickerPanelProviderId) {
		PickerPanelProvider pickerPanelProvider = pickerPanelProviderMap.get(pickerPanelProviderId);
		pickerPanelProvider.selectDataRow();
		pickerPanelProvider.setPickerPanelRendered(false);
		PickerPanelProvider nextPickerPanelProvider = findNextPickerPanelProvider(pickerPanelProvider);
		if (nextPickerPanelProvider != null) {
			nextPickerPanelProvider.setPickerPanelRendered(true);
			return null;
		}
		return action.action();
	}

	private PickerPanelProvider findNextPickerPanelProvider(PickerPanelProvider currentPickerPanelProvider) {
		PickerPanelProvider nextPickerPanelProvider = currentPickerPanelProvider.getNextPickerPanelProvider();
		if (nextPickerPanelProvider == null) {
			return null;
		}
		nextPickerPanelProvider.initPickerTables();
		List<PickerTable> pickerTables = nextPickerPanelProvider.getPickerTables();
		PickerTable pickerTable = pickerTables.get(pickerTables.size() - 1);
		if (pickerTable.getSelectedDataRow() == null) {
			return nextPickerPanelProvider;
		}
		nextPickerPanelProvider.selectDataRow();
		return findNextPickerPanelProvider(nextPickerPanelProvider);
	}

	public String getAjaxUpdateSelector(int hierarchyIndex, final String hierachyDepthInt) {

		int hierachyDepth = Integer.parseInt(hierachyDepthInt);
		StringBuilder ajaxSelector = new StringBuilder();

		// If we already are at the max selection level, just return a blank string. There are no more levels to update
		if (hierarchyIndex == hierachyDepth) {
			ajaxSelector
					.append("@(.selectContext")
					.append(hierarchyIndex)
					.append(")");
		} else {
			// We do not want to update the current level, only the lower levels
			int currentHierarchyIndex = hierarchyIndex + 1;

			while (currentHierarchyIndex <= hierachyDepth) {
				ajaxSelector
						.append("@(.contextLevel")
						.append(currentHierarchyIndex)
						.append(")")
						.append(" ");
				currentHierarchyIndex++;
			}
		}

		return ajaxSelector.toString();
	}
}
