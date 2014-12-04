/**
 * LESS CSS using assemble-less package.
 */

'use strict';

var config = require('../config');

module.exports = {

	options: {
		stripBanners: {
			block: true,
			line: true
		},
		cleancss: true,
		compress: false,
		report: 'min',
		paths: config.less.paths,
		imports: config.less.imports
	},

	compile: {
		files: config.less.files
	}
};
