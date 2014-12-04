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

        var RegisterModifiedBallot = (function (_super) {
            __extends(RegisterModifiedBallot, _super);
            function RegisterModifiedBallot() {
                var _this = this;
                _super.call(this, { el: "#page-register-modified-ballot" });

                this.onDocumentReady(function () {
                    _this.setFocusOnDirectToCandidateField();
                    _this.attachEventHandlers();
                });
            }
            RegisterModifiedBallot.prototype.setFocusOnDirectToCandidateField = function () {
                this.getDirectToCandidateNoTextBox().focus();
            };

            RegisterModifiedBallot.prototype.getDirectToCandidateNoTextBox = function () {
                return this.$('#direct-to-candidate-no');
            };

            RegisterModifiedBallot.prototype.attachEventHandlers = function () {
                var _this = this;
                this.$el.on("keydown", "#direct-to-candidate-no", function (event) {
                    _this.changeCheckBoxValueIfEnterIsPressed(event);
                });

                this.$el.on("keyup", "#direct-to-candidate-no", function (event) {
                    _this.deselectPersonVoteCandidateRow();
                    _this.focusAndSelectIndicatedPersonVoteCandidateRow();
                    _this.setFocusOnDirectToCandidateField();
                });

                this.$el.on("keydown", ".ui-autocomplete-input", function (event) {
                    if (event.keyCode === RegisterModifiedBallot.ENTER) {
                        return _this.stopEventPropagation(event);
                    }
                });
            };

            RegisterModifiedBallot.prototype.changeCheckBoxValueIfEnterIsPressed = function (event) {
                if (event.keyCode === RegisterModifiedBallot.ENTER) {
                    this.changeValueOfCandidateCheckBox();
                    this.clearDirectToCandidateField();
                    return this.stopEventPropagation(event);
                }
            };

            RegisterModifiedBallot.prototype.stopEventPropagation = function (event) {
                event.preventDefault();
                event.stopImmediatePropagation();
                return false;
            };

            RegisterModifiedBallot.prototype.deselectPersonVoteCandidateRow = function () {
                this.$('td').removeClass('selected-candidate-row');
            };

            RegisterModifiedBallot.prototype.focusAndSelectIndicatedPersonVoteCandidateRow = function () {
                this.$('.candidate_' + this.getDirectToCandidateNoTextBox().val()).focus().closest('tr').children('td').addClass('selected-candidate-row');
            };

            RegisterModifiedBallot.prototype.changeValueOfCandidateCheckBox = function () {
                var candidateCheckBox = this.$('.candidate_' + this.getDirectToCandidateNoTextBox().val()).get(0);
                if (candidateCheckBox !== undefined) {
                    candidateCheckBox.checked = !candidateCheckBox.checked;
                }
            };

            RegisterModifiedBallot.prototype.clearDirectToCandidateField = function () {
                this.getDirectToCandidateNoTextBox().val('');
            };
            RegisterModifiedBallot.ENTER = 13;
            return RegisterModifiedBallot;
        })(EVA.Components.BaseView);
        View.RegisterModifiedBallot = RegisterModifiedBallot;
    })(EVA.View || (EVA.View = {}));
    var View = EVA.View;
})(EVA || (EVA = {}));
