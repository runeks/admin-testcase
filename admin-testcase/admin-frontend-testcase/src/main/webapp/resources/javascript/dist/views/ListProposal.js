var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
var EVA;
(function (EVA) {
    (function (View) {
        'use strict';

        var ListProposal = (function (_super) {
            __extends(ListProposal, _super);
            function ListProposal() {
                _super.call(this, { el: '#page-edit-list-proposal' });
                this.buttons = [
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
            }
            ListProposal.prototype.initialize = function () {
                var _this = this;
                _super.prototype.initialize.call(this);

                this.onDocumentReady(function () {
                    _this.waitButtonManager.attachButtonsById(_this.buttons);
                });
            };
            return ListProposal;
        })(EVA.Components.BaseView);
        View.ListProposal = ListProposal;
    })(EVA.View || (EVA.View = {}));
    var View = EVA.View;
})(EVA || (EVA = {}));
