var exec = require('cordova/exec');
exports.setLocale = function (name, success, error) {
    exec(success, error, 'SetLocale', 'setLocale', [name]);
};