var smartCardInterface = smartCardInterface || {};

smartCardInterface.codeMap = {
    "000" : "#{msgs['@issuing_point.operation.successful']}",
    "001" : "#{msgs['@issuing_point.operation.unknown_error']}",
    "002" : "#{msgs['@issuing_point.operation.no_card_found']}",
    "003" : "#{msgs['@issuing_point.operation.invalid_pin']}",
    "004" : "#{msgs['@issuing_point.operation.token_init']}",
    "005" : "#{msgs['@issuing_point.operation.nonexisting_provider']}",
    "006" : "#{msgs['@issuing_point.operation.unknown_command']}",
    "007" : "#{msgs['@issuing_point.operation.json_error']}",
    "008" : "#{msgs['@issuing_point.operation.verify_signature']}"
};

/**
 * Perform any operation on the smart card, given an assertion provided as JSON. An assertion contains
 * the specific operation to be performed, as well as any parameters.
 * @param json
 */
smartCardInterface.appletOperation = function(json) {
    var jsonResult;
    try {
        jsonResult = document.ipapplet.operation(json);
    } catch (e) {
        eval("appletNotRunning.show();");
        hideWait();
        return;
    }

    var result = jQuery.parseJSON(jsonResult);

    if (result.Code == '000') {
        // Success, register that the credentials have been issued
        registerIssuedCredentials();
    } else {
        console.log(result);
    }

    hideWait();
    /* Show the dialog for the resulting error code */
    eval("resultDialog" + result.Code + ".show();");
};

smartCardInterface.writeToCard = function(assertion) {
     showWait();
    return smartCardInterface.appletOperation(assertion);
};

smartCardInterface.checkStatus = function(assertion) {
    return smartCardInterface.appletOperation(assertion);
};

function showWait() {
    jQuery('.ajax-loader').get(0).style.display = 'block';
    jQuery('.ajax-loader').get(0).style.visibility = 'visible';
}

function hideWait() {
   jQuery('.ajax-loader').get(0).style.display = 'none';
   jQuery('.ajax-loader').get(0).style.visibility = 'hidden';
}