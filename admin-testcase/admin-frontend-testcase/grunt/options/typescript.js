/**
 * Typescript
 */

'use strict';

var config = require('../config');

module.exports = {

	compile: {
		src: config.typescript.src,
		dest: config.typescript.dest,
		options: {
			target: config.typescript.targetVersion,
			basePath: config.typescript.basePath,
			sourceMap: false,
			declaration: false,
			comments: false
		}
	}
};