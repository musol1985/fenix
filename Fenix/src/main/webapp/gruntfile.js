module.exports = function(grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        less: {
            development: {
                options: {
                    paths: ["resources/css"]
                },
                files: {
                    "resources/css/app.css": "resources/less/app.less",
                },
                cleancss: true
            }
        },
        ngtemplates: {
          materialAdmin: {
            src: ['resources/template/**.html', 'resources/template/**/**.html', 'uib/template/**.html'],
            dest: 'resources/js/templates.js',
            options: {
              htmlmin: {
                    collapseWhitespace: true,
                    collapseBooleanAttributes: true
              }
            }
          }
        },
        watch: {
            a: {
                files: ['resources/less/**/*.less'], // which files to watch
                tasks: ['less', 'csssplit'],
                options: {
                    nospawn: true
                }
            },
            b: {
                files: ['resources/template/**/*.html'], // which files to watch
                tasks: ['ngtemplates'],
                options: {
                    nospawn: true
                }
            },
            controllers: {
            	files: ['resources/js/controllers/mantenimientos/**/*.js'], // which files to watch
                tasks: ['concat:controllers','uglify:controllers']
            },
            services: {
            	files: ['resources/js/services/rest/*.js'], // which files to watch
                tasks: ['concat:services','uglify:services']
            },
            directives: {
            	files: ['resources/js/directives/**/*.js'], // which files to watch
                tasks: ['concat:directives','uglify:directives']
            }
        },
        uglify:{
        	libs: {
        	      files: {
        	        'resources/vendors/bower_components/ng-table/dist/ng-table.min.js': ['resources/vendors/bower_components/ng-table/dist/ng-table.js']
        	      }
        	},
        	controllers:{
        		files: {        	        
        	        'resources/js/controllers/dist/lilapp.min.js': ['resources/js/controllers/dist/lilapp.js']
        	      }
        	},
        	services:{
        		files: {        	        
        	        'resources/js/services/dist/lilapp.min.js': ['resources/js/services/dist/lilapp.js']
        	      }
        	},
        	directives:{
        		files: {        	        
        	        'resources/js/directives/dist/ui.min.js': ['resources/js/directives/dist/ui.js']
        	      }
        	}
        },
        concat: {
        	controllers:{
	            options: {
	              separator: ';\n',
	            },
	            files: {        	        
	    	        'resources/js/controllers/dist/lilapp.js': ['resources/js/controllers/mantenimientos/**/*.js']
	    	    }
        	},
        	services:{
	            options: {
	              separator: ';\n',
	            },
	            files: {        	        
	    	        'resources/js/services/dist/lilapp.js': ['resources/js/services/rest/**/*.js']
	    	    }
        	},
        	directives:{
	            options: {
	              separator: ';\n',
	            },
	            files: {        	        
	    	        'resources/js/directives/dist/ui.js': ['resources/js/directives/ui/**/*.js']
	    	    }
        	}
        }
    });

    // Load the plugin that provides the "less" task.
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-angular-templates');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');
    
    // Default task(s).
    grunt.registerTask('default', ['less']);

};
