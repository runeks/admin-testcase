/**
 * Watch
 */

'use strict';

var config = require('../config');

module.exports = {
	
	dev: {
		options: {
			debounceDelay: 250
		},
		files: config.watch.files,
		tasks: ['less', 'typescript','karma:dev']
	},
	
	watch: {
		options: {
			debounceDelay: 250
		},
		files: [
			config.watch.files,
			config.karma.testFiles
		],
		tasks: ['less', 'typescript','karma:test']
	}
};
