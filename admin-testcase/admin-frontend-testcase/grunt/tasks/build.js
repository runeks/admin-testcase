module.exports = function (grunt) {

	grunt.registerTask('build', [
		'typescript:compile',
		'less:compile',
		'karma:test',
		'watch'
	]);
};