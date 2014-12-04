interface IPrimeFacesWidget {
	_super:any;
	id:string;
	jq:JQuery;
	jqId:string;
	widgetVar:string;
	cfg:Object;
	
	disable():void;
	enable():void;
	init(config:Object):void;
	destroy():void;
	getJQ():JQuery;
	isDetached():boolean;
	refresh(config:Object):void;
	removeScriptElement(a:string):void;
	
}

interface ITabView extends IPrimeFacesWidget {

	navContainer:JQuery;
	onshowHandlers:any;
	panelContainer:JQuery;
	stateHolder:JQuery;

	cfg: {
		id:string;
		scrollable:boolean;
		selected:number;
		widgetVar:string;
		effectDuration:string;
		onTabChange: (index:number)=>boolean;
	}
	
	addOnshowHandler(a:any, b:any):void;
	bindEvents():void;
	constructor():void;
	disable():void;
	enable():void;
	enableScrollerButton(a:any);
	disableScrollerButton(a:any);
	fireTabChangeEvent(a:any);
	fireTabCloseEvent(a:any);
	getActiveIndex():number;
	getLength():number;
	hasBehavior():boolean;
	initScrolling():void;
	isLoaded(a:any):boolean;
	loadDynamicTab():void;
	markAsLoaded(a:any):void;
	postTabShow(a:any):void;
	remove(b:any):void
	restoreScrollState():void;
	saveScrollState(a:any):void;
	scroll(c:any):void;
	select(d:any, c:any):void;
	show(c:any);
}

interface IDialog extends IPrimeFacesWidget {

	show():void;
	hide():void;
	blockEvents:string;
	closeIcon:JQuery;
	content:JQuery;
	footer:JQuery;
	icon:JQuery;
	maximizeIcon:JQuery;
	minimizeIcon:JQuery;
	parent:JQuery;
	positionInitialized:boolean;
	title:JQuery;
	titleBar:JQuery;

}

interface IConfirmDialog extends IDialog {
	message:JQuery;
}

interface ISortOrder {
	ASCENDING: number;
	DESCENDING:number;
	UNSORTED:number;
}

interface IPaginator extends IPrimeFacesWidget {

}

interface ISelectOneMenu extends IPrimeFacesWidget {

	disabled:boolean;
	focusInput:JQuery;
	label:JQuery;
	input:JQuery;
	items:JQuery;
	itemsWrapper:JQuery;
	itemsContainer:JQuery;
	menuIcon:JQuery;
	optGroupsSize:number;
	options:JQuery;
	panel:JQuery;
	panelId:string;
	resizeNS:string;
	triggers:JQuery;
	_render():any;
	alignPanel():any;
	appendPanel():any;
	bindConstantEvents():any;
	bindEvents():any;
	bindFilterEvents():any;
	bindKeyEvents():any;
	bindResize():any;
	blur():any;
	containsFilter():any;
	disable():any;
	enable():any;
	endsWithFilter():any;
	filter(e:string):any;
	focus(a:any):any;
	focusFilter():any;
	getActiveItem():any;
	getSelectedLabel():string;
	getSelectedValue():any;
	handleEnterKey(e:Event):void;
	handleEscapeKey(e:Event):void;
	handleTabKey(e:Event):void;
	highlightItem(e:Event):void;
	highlightNext(e:Event):void;
	highlightPrev(e:Event):void;
	hide():void;
	show():void;
	resolveItemIndex():void;
	revert():void;
	selectItem(f:any, b:any):void;
	selectValue(b:any):void;
	setLabel(a:string):void;
	setupFilterMatcher():void;
	startsWithFilter(b:any, a:any):void;
	triggerChange(a:any):void;
	unbindEvents():void;
	unbindResize():void;
}

interface IDataTable extends IPrimeFacesWidget {
	paginator:IPaginator;
	sortableColumns:JQuery;
	tbody:JQuery;
	thread:JQuery;
	SORT_ORDER:ISortOrder;

	filter():any;
	addResizers():any;
	addSelection():any;
	addSortMeta():any;
	adjustScrollHeight():any;
	adjustScrollWidth():any;
	alignScrollBody():any;
	bindChangeFilter(a:any):any;
	bindCheckboxEvents():any;
	bindEditEvents():any;
	bindEnterKeyFilter():any;
	bindExpansionEvents():any;
	bindFilterEvent(a:any):any;
	bindPaginator():any;
	bindRadioEvents():any;
	bindRowEvents():any;
	bindRowHover():any;
	bindSelectionEvents():any;
	bindTextFilter(a:any):any;
	cancelRowEdit(a:any):any;
	checkHeaderCheckbox():any;
	clearFilters():any;
	clearScrollState():any;
	clearSelection():any;
	collapseAllRows():any;
	collapseRow(a:any):any;
	doCellEditRequest():any;
	findRow(a:any):any;
	fireRowCollapseEvent(c:any):any;
	fireRowSelectEvent(d:any, a:any):any;
	fireRowUnselectEvent(d:any, b:any):any;
	fixColumnWidths():any;
	getExpandedRows():any;
	getPaginator():IPaginator;
	getRowEditors():JQuery;
	getRowMeta():string;
	getScrollbarWidth():number;
	getSelectedRowsCount():number;
	getTbody():JQuery;
	getThead():JQuery;
	hasBehavior(a:any):boolean;
	hasVertialOverflow():boolean;
	highlightRow(a:JQuery):void;
	invalidateRow(a:JQuery):void;
	isSelected(a:JQuery):boolean;
	isCheckboxSelectionEnabled():boolean;
	isSelectionEnabled():boolean;
	isSingleSelection():boolean;
	isEmpty():boolean;
	isMultipleSelection():boolean;
	isRadioSelectionEnabled():boolean;
	joinSortMetaOption(b:any):any;
	loadExpandedRowContent(d:any):any;
	loadLiveRows():any;
	makeRowsDraggable():any;
	onRowClick(e:any, d:any, a:any):any;
	onRowDblclick(a:any, c:any):any;
	onRowRightClick(c:any, d:any):any;
	paginate(d:any):any;
	postUpdateData():any;
	refresh(b:any):any;
	removeSelection(a:JQuery):void;
	resize(a:JQuery, i:any):any;
	restoreScrollState():any;
	saveCell(a:JQuery):any;
	saveColumnOrder():any;
	saveRowEdit(a:JQuery):any;
	saveScrollState(a:JQuery):any;
	selectAllRows():any;
	selectAllRowsOnPage():any;
	selectCheckbox(a:JQuery):any;
	selectRadio(a:JQuery):any;
	selectRow(b:any, a:JQuery):any;
	selectRowWithCheckbox(b:any, a:JQuery):any;
	selectRowWithRadio(b:any, a:JQuery):any;
	selectRowsInRange(f:any):any;
	setOuterWidth(a:any, b:any):any;
	setScrollWidth(a:any):any;
	setupDraggableColumns():any;
	setupFiltering():any;
	setupResizableColumns():any;
	setupScrolling():any;
	setupSelection():any;
	setupStickyHeader():any;
	shouldSort():any;
	showCellEditor():any;
	showRowEditors():any;
	sort(d:any, a:any, f:any):any;
	switchToRowEdit(c:any):any;
	syncRowParity():any;
	tabCell(a:any,d:any):any;
	toggleCheckAll():any;
	toggleExpansion(b:any):any;
	uncheckHeaderCheckbox():any;
	unhighlightRow(a:any):any;
	unselectAllRows():any;
	unselectAllRowsOnPage():any;
	unselectCheckbox(a:JQuery):any;
	unselectRadio(a:JQuery):any;
	unselectRow(b:any, a:JQuery):any;
	unselectRowWithCheckbox(b:any, a:JQuery):any;
	updateData(c:any, a:JQuery):any;
	updateHeaderCheckbox():any;
	updateRow(b:any, a:JQuery):any;
	viewMode(a:JQuery):any;
	writeSelections():any;
}

interface IPrimeFacesResponseArgs {
	validationFailed:boolean;
}

interface IPrimeFacesAjaxConfig {
	ext:any;
	oncomplete:(jqXHR:JQueryAjaxSettings,textStatus:string, primefacesArgs:IPrimeFacesResponseArgs)=>any;
	onstart:(jqXHR:JQueryAjaxSettings,textStatus:string, primefacesArgs:IPrimeFacesResponseArgs)=>any;
	onsuccess:(jqXHR:JQueryAjaxSettings,textStatus:string, primefacesArgs:IPrimeFacesResponseArgs)=>any;
	onerror:(jqXHR:JQueryAjaxSettings,textStatus:string, primefacesArgs:IPrimeFacesResponseArgs)=>any;
	source:string;
	update:string;
}

interface IPrimeFacesRequest {
	handle:(a:any, b:any)=>any;
	resolveComponentsForAjaxCall:(a:any, b:any)=>any;
	send:(config:IPrimeFacesAjaxConfig)=>any;
}