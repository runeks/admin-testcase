$.fn.bindFirst = function (name, fn) {
    this.on(name, fn);

    this.each(function () {
        var handlers = $._data(this, 'events')[name.split('.')[0]];

        var handler = handlers.pop();

        handlers.splice(0, 0, handler);
    });
};
