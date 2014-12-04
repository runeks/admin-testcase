/**
 * Default configuration for Karma
 */
'use strict';

var projectConfig = require('./config');

module.exports = function (config) {

	config.set({
		basePath: projectConfig.karma.basePath,
		autoWatch: false,
		singleRun: projectConfig.karma.autoClose,
		colors: true,
		captureTimeout: 7000,

		frameworks: [
			'jasmine',
			'jasmine-matchers'
		],

		preprocessors: {
			'src/**/dist/**/*.js': ['coverage']
		},

//		jshint: {
//			options: {
//				curly: true,
//				eqeqeq: false,
//				immed: true,
//				latedef: 'nofunc',
//				undef: true,
//				unused: true,
//				jquery: true,
//				boss: true,
//				devel: true,
//				eqnull: true,
//				browser: true
//			},
//			summary: true
//		},

		reporters: [
			'progress',
			'coverage',
//			'jshint',
			'junit'
		],

		junitReporter: {
			outputFile: 'target/test-results.xml'
		},

//		jshintReporter: {
//			outputFile: 'target/jshint-results.xml'
//		},

		plugins: [
			'karma-jasmine',
			'karma-jasmine-matchers',
			'karma-junit-reporter',
//			'karma-jshint',
			'karma-coverage',
			'karma-chrome-launcher',
			'karma-firefox-launcher',
			'karma-safari-launcher',
			'karma-phantomjs-launcher'
		],

		coverageReporter: {
			reporters: [
				{
					type: 'text-summary',
					dir: 'target/javascript-coverage/'
				},
				{
					type: 'html',
					dir: 'target/javascript-coverage/'
				}
			]
		},

		logLevel: config.LOG_INFO,

		// List of files to load in the browser
		files: projectConfig.karma.files
	});
};

