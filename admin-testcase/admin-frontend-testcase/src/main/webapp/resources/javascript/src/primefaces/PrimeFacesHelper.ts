/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/underscore.d.ts"/>

declare var PF:any;

module EVA.Components {

	var componentSeperator:RegExp = /:/g; 
	
	export class PrimeFacesHelper {
		
		/**
		 * This method resolves a PrimeFaces widget based on its ID.
		 * @param widgetId
		 * @returns {*}
		 */
		public static widget(widgetId:string):IPrimeFacesWidget {
			return PF(widgetId);
		}

		/**
		 * This method is used for resolving parent widget for an element. This means that inside e.g. a dialog that uses a
		 * datatable which is supposed to interact with the dialog widget and similar cases, this is NOT the correct method to use.
		 * see {@link Application.widget}
		 * @param eventElement
		 * @returns {*}
		 */
		public static resolveWidget(eventElement:any):IPrimeFacesWidget {

			var $eventElement:JQuery;
			if (eventElement instanceof HTMLElement) {
				$eventElement = $(eventElement);
			} else if (_.isObject(eventElement)) {
				var sourceId = eventElement.source.replace(/:/g, "\\:");
				$eventElement = $('#' + sourceId);
			}

			var $widgetElement = $eventElement.closest('[data-widgetVar]');

			return PrimeFacesHelper.widget($widgetElement.attr('data-widgetVar'));
		}

		/**
		 * Performs escaping of a string componentId. Default JSF uses : as a separator. This is configurable,
		 * in both JSF, and in the PrimeFacesHelper class.
		 * @param componentId {string}
		 * @returns {string}
		 */
		public static escapeClientId(componentId:string):string {
			return componentId.replace(componentSeperator, '\\:');
		}
	}

}