/**
 * Grunt configuration
 */
'use strict';

var pkg = require('../package');
var resourcePath = 'src/main/webapp/resources';
var lessPath = resourcePath + '/less';
var javascriptPath = resourcePath + '/javascript';
var root = '../';

module.exports = {

	// A banner for distributed files (name, version, license, date)
	banner: '/*! ' + pkg.name + ' - v' + pkg.version + '<%= grunt.template.today("yyyy-mm-dd") %> */',

	destDir: '/dist',

	watch: {
		files: [
				lessPath + '/**/*.less',
				javascriptPath + '/src/**/*.ts',
				'src/test/javascript/**/*Test.js'
		]
	},

	typescript: {
		src: [
				javascriptPath + '/src/**/*.ts'
		],
		dest: javascriptPath + '/dist',
		basePath: javascriptPath + '/src',
		targetVersion: 'es5' //or es3
	},

	less: {
		paths: [resourcePath],
		imports: {
			inline: ['bootstrap/css/bootstrap.css']
		},
		files: [
			{
				src: [
						lessPath + '/main.less'
				],
				dest: resourcePath + '/css/all.css',
				ext: '.css'
			}
		]
	},

	karma: {
		basePath: root,
		autoClose: true,
		configFile: 'grunt/karma.conf.js',
		devBrowsers: ['PhantomJS'],
		jenkinsBrowsers: ['PhantomJS'],
		testBrowsers: ['Chrome', 'Firefox', 'Safari', 'PhantomJS'],

		coverageFiles: javascriptPath + '/dist/**/*.js ',

		files: [
				javascriptPath + '/vendor/jquery/*.js',
				javascriptPath + '/vendor/jqueryui/*.js',
				javascriptPath + '/vendor/primefaces/*.js',
				javascriptPath + '/vendor/underscorejs/*.js',
				javascriptPath + '/vendor/backbone/*.js',
				javascriptPath + '/dist/primefaces/*.js',
				javascriptPath + '/dist/components/RequestBroker.js',
				javascriptPath + '/dist/components/*.js',
				javascriptPath + '/dist/views/*.js',
				javascriptPath + '/dist/App.js',
			'src/test/javascript/**/*Test.js'
		]
	}
};
