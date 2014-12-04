/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/backbone.d.ts"/>
/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../components/BaseView.ts"/>

module EVA.View {
    'use strict';

    export class RegisterModifiedBallot extends EVA.Components.BaseView {

        public static ENTER = 13;

        constructor() {
            super({el: "#page-register-modified-ballot"});

            this.onDocumentReady(()=> {
                this.setFocusOnDirectToCandidateField();
                this.attachEventHandlers();
            });
        }

        public setFocusOnDirectToCandidateField():void {
            this.getDirectToCandidateNoTextBox().focus();
        }

        private getDirectToCandidateNoTextBox():JQuery {
            return this.$('#direct-to-candidate-no');
        }

        private attachEventHandlers():void {
            this.$el.on("keydown", "#direct-to-candidate-no", (event:JQueryEventObject)=> {
                this.changeCheckBoxValueIfEnterIsPressed(event);
            });

            this.$el.on("keyup", "#direct-to-candidate-no", (event:JQueryEventObject)=> {
                this.deselectPersonVoteCandidateRow();
                this.focusAndSelectIndicatedPersonVoteCandidateRow();
                this.setFocusOnDirectToCandidateField();
            });

            this.$el.on("keydown", ".ui-autocomplete-input", (event:JQueryEventObject)=> {
                if (event.keyCode === RegisterModifiedBallot.ENTER) {
                    return this.stopEventPropagation(event);
                }
            });
        }

        public changeCheckBoxValueIfEnterIsPressed(event:JQueryEventObject):any {
            if (event.keyCode === RegisterModifiedBallot.ENTER) {
                this.changeValueOfCandidateCheckBox();
                this.clearDirectToCandidateField();
                return this.stopEventPropagation(event);
            }
        }

        public stopEventPropagation(event):any {
            event.preventDefault();
            event.stopImmediatePropagation();
            return false;
        }

        public deselectPersonVoteCandidateRow():void {
            this.$('td').removeClass('selected-candidate-row');
        }

        public focusAndSelectIndicatedPersonVoteCandidateRow():void {
            this.$('.candidate_' + this.getDirectToCandidateNoTextBox().val()).focus().closest('tr').children('td').addClass('selected-candidate-row');
        }

        public changeValueOfCandidateCheckBox():void {
            var candidateCheckBox:HTMLInputElement = <HTMLInputElement>this.$('.candidate_' + this.getDirectToCandidateNoTextBox().val()).get(0);
            if (candidateCheckBox !== undefined) {
                candidateCheckBox.checked = !candidateCheckBox.checked;
            }
        }

        public clearDirectToCandidateField():void {
            this.getDirectToCandidateNoTextBox().val('');
        }
    }
}

