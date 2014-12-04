module.exports = function (grunt) {

	grunt.registerTask('dev', [
		'typescript:compile',
		'less:compile',
		'karma:dev',
		'watch:dev'
	]);
};