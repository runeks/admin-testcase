/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/backbone.d.ts"/>
/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../components/BaseView.ts"/>

module EVA.View {

	'use strict';

	export class ListProposal extends EVA.Components.BaseView {

		public buttons:string[] = [
			'editListProposalForm:tabs:revertToStatusPending',
			'editListProposalForm:tabs:setStatusUnderConstruction',
			'editListProposalForm:tabs:createCandidate',
			'editListProposalForm:tabs:listProposalActionsMenu',
			'editListProposalForm:tabs:deleteList',
			'editListProposalForm:tabs:deleteListProposal',
			'editListProposalForm:tabs:rejectListProposal',
			'editListProposalForm:tabs:approveListProposal',
			'editListProposalForm:tabs:setStatusPending',
			'editListProposalForm:tabs:setStatusWithdraw'
		];
		
		
		constructor() {
			super({el: '#page-edit-list-proposal'});
		}

		public initialize():void {
			super.initialize();

			this.onDocumentReady(()=> {
				this.waitButtonManager.attachButtonsById(this.buttons);
			});
		}
	}
}
