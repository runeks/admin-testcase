/**
 * evote - A namespace for all global Javascript functions
 */
var evote = {

    // The date format used in the JQuery datepicker is not compatible with the
    // Java date format, so we need to convert it. See formatDate documentation
    // for more info: htmtp://docs.jquery.com/UI/Datepicker/formatDate
    convertToJQDateFormat: function (dateFormat) {
        dateFormat = dateFormat.toLowerCase();
        dateFormat = dateFormat.replace("yyyy", "yy");

        return dateFormat;
    },

    /**
     * Used by the evote:menu tag to show an area when a menu item is selected.
     */
    selectTarget: function (menuid, id) {
        /* Find currently selected menu item */
        var currentSelection = jQuery('#' + menuid + ' li.selected');
        var currentSelectionId = jQuery('#' + menuid + ' li.selected a').attr('id');
        currentSelection.removeClass('selected');
        /* Deselect */

        if (currentSelectionId) {
            /* Hide the previously selected area, if any */
            jQuery('#' + currentSelectionId.substring(3)).hide();
        }

        var area = jQuery('#' + id);
        if (area) {
            /* Show the selected area */
            area.show();
            /* Mark it as selected in the menu */
            jQuery('#' + menuid + ' li a#mi_' + id).parent().attr('class', function () {
                return 'selected';
            });
        }
    },

    /**
     * This method resolves a PrimeFaces widget based on its ID.
     * @param widgetId
     * @returns {*}
     */
    widget: function (widgetId) {
        var widget = PF(widgetId);
        var methodDefined = typeof method != 'undefined';

        if (!methodDefined) {
            return widget;
        }

        if (Object.prototype.toString.call(args) == '[Object Array]') {
            widget[method].apply(widget, args);
        }
        else {
            widget[method].call(widget, args);
        }

        return widget;
    },

    /**
     * This method is used for resolving parent widget for an element. This means that inside e.g. a dialog that uses a
     * datatable which is supposed to interact with the dialog widget and similar cases, this is NOT the correct method to use.
     * see {@link evote.widget}
     * @param eventElement
     * @returns {*}
     */
    resolveWidget: function (eventElement) {

//        console.log(eventElement);
//        debugger;
        var $eventElement;
        if (eventElement instanceof HTMLElement) {
            $eventElement = $(eventElement);
        } else if (Object.prototype.toString.call(eventElement) == '[object Object]') {
            var sourceId = eventElement.source.replace(/:/g, "\\:");
            $eventElement = $('#' + sourceId);
        }

        var $widgetElement = $eventElement.closest('[data-widgetVar]');

        return this.widget($widgetElement.attr('data-widgetVar'));
    }
}

/*
 * autocomplete="off" must be set, to avoid Firefox filling in old ViewState
 * values.
 */
jQuery(document).ready(function () {

    if (document.getElementsByName) {
        var inputElements = document
            .getElementsByName("javax.faces.ViewState");
        for (i = 0; inputElements[i]; i++) {
            inputElements[i].setAttribute("autocomplete", "off");
        }
    }

    // Bind on ajaxError to be able to redirect failed Ajax requests
    // to error page:
    jQuery('body').bind('ajaxError', function (event, xhr) {
        var status = xhr.status;
        if (status == 500) {
            // Redirect to error page
            window.location = xhr.getResponseHeader('X-ERROR-PAGE');
        } else if (status == 0) {
            // Session most likely timed out, reload entire page to force relog
            window.location.reload();
        }
    });

    // Bind on ajaxSuccess to be able to handle session timeouts, etc.
    jQuery('body').bind('ajaxComplete', function (event, xhr) {
        if (xhr.responseText.match(/Velg bruker/gi)) {
            // Reload page to force relog
            window.location.reload();
        }
    });
});
//Add reset function to PrimeFaces InputMask to fix inputmask after update
if (PrimeFaces.widget.InputMask) {
    PrimeFaces.widget.InputMask.prototype.reset = function () {
        this.jq.unmask();
        this.jq.mask(this.cfg.mask, this.cfg);
    };
}

function focusIfFocusable(elementToFocus) {
    if (elementToFocus.focus != undefined) {
        elementToFocus.focus();
    }
}


