/**
 * This is a hack to ensure that migration from PrimeFaces 4.x.x to 5.x.x does not 
 * break existing functionality. Please refer to Migration Guide 4.0 to 5.0 for PrimeFaces https://code.google.com/p/primefaces/wiki/MigrationGuide
 * --> "Widgets must be referenced via "PF". e.g. PF('widgetVarName').show() instead of widgetVarName.show();"
 */
(function (jQuery, PrimeFaces) {

	$(document).ready(function () {
		for (var widgetName in PrimeFaces.widgets) {
			if(PrimeFaces.widgets.hasOwnProperty(widgetName)){
				window[widgetName] = PrimeFaces.widgets[widgetName];
			}
		}
	});
	
})(jQuery,PrimeFaces);
