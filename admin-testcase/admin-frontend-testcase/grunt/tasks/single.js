module.exports = function (grunt) {

	grunt.registerTask('single', [
		'typescript:compile',
		'karma:dev'
	]);
};