/**
 * Configuration for Karma test-runner
 */

'use strict';

var config = require('../config');

module.exports = {
	
	options: {
		configFile: config.karma.configFile
	},

	// Testing in all browsers
	test: {
		options: {
			browsers: config.karma.testBrowsers
		}
	},

	// Jenkins
	jenkins: {
		options: {
			browsers: config.karma.jenkinsBrowsers
		}
	},
	
	dev: {
		options: {
			browsers: config.karma.devBrowsers
		}
	}
};
