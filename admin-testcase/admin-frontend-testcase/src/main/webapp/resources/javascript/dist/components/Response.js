var EVA;
(function (EVA) {
    (function (Components) {
        var Response = (function () {
            function Response() {
                this.validationFailed = false;
            }
            Response.prototype.setPrimeFacesArguments = function (args) {
                if (args && args.validationFailed) {
                    this.validationFailed = true;
                }

                this.response = args;
            };

            Response.prototype.isValidationFailed = function () {
                return this.validationFailed === true;
            };

            Response.prototype.setStatus = function (code) {
                this.status = code;
            };

            Response.prototype.getStatus = function () {
                return this.status;
            };

            Response.prototype.setComponentId = function (componentId) {
                this.componentId = componentId;
            };

            Response.prototype.getComponentId = function () {
                return this.componentId;
            };
            return Response;
        })();
        Components.Response = Response;
    })(EVA.Components || (EVA.Components = {}));
    var Components = EVA.Components;
})(EVA || (EVA = {}));
