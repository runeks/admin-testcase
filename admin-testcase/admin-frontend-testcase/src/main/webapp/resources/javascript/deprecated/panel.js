PrimeFaces.widget.Panel.prototype.open = function(callback) {
    this.toggler.removeClass('ui-icon-plusthick').addClass('ui-icon-minusthick');
    this.cfg.collapsed = false;
    this.toggleStateHolder.val(false);
    var _self = this;

    this.content.slideDown(this.cfg.toggleSpeed,
        function(e) {
            if(_self.cfg.behaviors) {
                var toggleBehavior = _self.cfg.behaviors['toggle'];
                if(toggleBehavior) {
                    toggleBehavior.call(_self, e);
                }
            }
            if(callback) {
            	callback();
            }
        });
}

PrimeFaces.widget.Panel.prototype.collapse = function() {
    this.toggler.removeClass('ui-icon-minusthick').addClass('ui-icon-plusthick');
    this.cfg.collapsed = true;
    this.toggleStateHolder.val(true);

    var _self = this;

    this.content.slideUp(this.cfg.toggleSpeed,
        function(e) {
            if(_self.cfg.behaviors) {
                var toggleBehavior = _self.cfg.behaviors['toggle'];
                if(toggleBehavior) {
                    toggleBehavior.call(_self, e);
                }
            }
        });
}
