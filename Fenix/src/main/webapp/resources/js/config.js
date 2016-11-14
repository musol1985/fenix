materialAdmin
    .config(function ($stateProvider, $urlRouterProvider){
        $urlRouterProvider.otherwise("/home");


        $stateProvider
        
            //------------------------------
            // HOME
            //------------------------------

            .state ('home', {
                url: '/home',
                templateUrl: 'resources/views/home.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                     'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css',
                                    'resources/vendors/bower_components/fullcalendar/dist/fullcalendar.min.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                insertBefore: '#app-level-js',
                                files: [
                                    'resources/vendors/sparklines/jquery.sparkline.min.js',
                                    'resources/vendors/bower_components/jquery.easy-pie-chart/dist/jquery.easypiechart.min.js',
                                    'resources/vendors/bower_components/simpleWeather/jquery.simpleWeather.min.js',
                                    'resources/vendors/input-mask/input-mask.min.js',
                                    'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js',
                                ]
                            }
                        ])
                    }
                }
            })

            //------------------------------
            // CONFIGURACION
            //------------------------------
            .state ('configuracion', {
                url: '/configuracion',
                templateUrl: 'resources/views/common.html'
            })

            .state('configuracion.centro', {
                url: '/centro',
                templateUrl: 'resources/views/tables.html'
            })
            
            .state('configuracion.centros', {
                url: '/centros',
                templateUrl: 'resources/views/centros.html'
            })

            .state('configuracion.usuarios', {
                url: '/usuarios',
                templateUrl: 'resources/views/usuarios.html'
            })
            
            .state('configuracion.prestaciones', {
                url: '/prestaciones',
                templateUrl: 'resources/views/prestaciones.html'
            })
            
            .state('configuracion.horarios', {
                url: '/horarios',
                templateUrl: 'resources/views/horarios.html'
            })

            .state('configuracion.horario', {
                url: '/horario/:id',
                controller: 'editorHorario',
                templateUrl: 'resources/views/horario.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                        	{
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/fullcalendar/dist/fullcalendar.min.css'
                                ]
                            },
                            {                            	
                            	serie:true,
                                name: 'vendors',
                                files: [ 
                                	'resources/vendors/bower_components/lz-string/libs/lz-string.min.js',
                                	'resources/vendors/blocky/blockly_compressed.js',
                                	'resources/vendors/blocky/blocks_compressed.js',
                                	'resources/vendors/blocky/javascript_compressed.js',
                                	'resources/vendors/blocky/msg/js/es.js',
                                	'resources/js/blockly/horarios/bloques.js',
                                	'resources/js/blockly/horarios/generador.js',
                                    'resources/vendors/bower_components/moment/min/moment.min.js',
                                    'resources/vendors/bower_components/fullcalendar/dist/fullcalendar.js',
                                    'resources/vendors/bower_components/fullcalendar/dist/locale/es.js',
                                    'resources/vendors/bower_components/angular-smart-table/dist/smart-table.js'                                    
                                ]
                            }
                        ])
                    }
                }
            })
            //------------------------------
            // CITAS
            //------------------------------
            .state ('citacion', {
                url: '/citacion',
                templateUrl: 'resources/views/common.html'
            })
            
            .state ('citacion.citas', {
                url: '/citas',
                templateUrl: 'resources/views/citas.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css',
                                    'resources/vendors/bower_components/fullcalendar/dist/fullcalendar.min.css',
                                    'resources/vendors/bower_components/chosen/chosen.min.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js',
                                    'resources/vendors/bower_components/moment/min/moment.min.js',
                                    'resources/vendors/bower_components/fullcalendar/dist/fullcalendar.min.js',
                                    'resources/vendors/bower_components/fullcalendar/dist/locale/es.js',
                                    'resources/vendors/bower_components/angular-dragdrop/src/angular-dragdrop.min.js',
                                    'resources/vendors/bower_components/jquery-ui/jquery-ui.min.js',
                                    'resources/vendors/bower_components/chosen/chosen.jquery.js',
                                    'resources/vendors/bower_components/angular-chosen-localytics/dist/angular-chosen.js'
                                ]
                            }
                        ])
                    }
                }
            })
            
            //------------------------------
            // TEST
            //------------------------------
            .state ('test', {
                url: '/test',
                templateUrl: 'resources/views/common.html'
            })
            
        	
            .state ('test.edu', {
                url: '/form-components',
                templateUrl: 'resources/views/form-components.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/nouislider/jquery.nouislider.css',
                                    'resources/vendors/farbtastic/farbtastic.css',
                                    'resources/vendors/bower_components/summernote/dist/summernote.css',
                                    'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css',
                                    'resources/vendors/bower_components/chosen/chosen.min.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/input-mask/input-mask.min.js',
                                    'resources/vendors/bower_components/nouislider/jquery.nouislider.min.js',
                                    'resources/vendors/bower_components/moment/min/moment.min.js',
                                    'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js',
                                    'resources/vendors/bower_components/summernote/dist/summernote.min.js',
                                    'resources/vendors/fileinput/fileinput.min.js',
                                    'resources/vendors/bower_components/chosen/chosen.jquery.js',
                                    'resources/vendors/bower_components/angular-chosen-localytics/chosen.js',
                                    'resources/vendors/bower_components/angular-farbtastic/angular-farbtastic.js'
                                ]
                            }
                        ])
                    }
                }
            })

            //------------------------------
            // HEADERS
            //------------------------------
            .state ('headers', {
                url: '/headers',
                templateUrl: 'resources/views/common-2.html'
            })

            .state('headers.textual-menu', {
                url: '/textual-menu',
                templateUrl: 'resources/views/textual-menu.html'
            })

            .state('headers.image-logo', {
                url: '/image-logo',
                templateUrl: 'resources/views/image-logo.html'
            })

            .state('headers.mainmenu-on-top', {
                url: '/mainmenu-on-top',
                templateUrl: 'resources/views/mainmenu-on-top.html'
            })


            //------------------------------
            // TYPOGRAPHY
            //------------------------------
        
            .state ('typography', {
                url: '/typography',
                templateUrl: 'resources/views/typography.html'
            })


            //------------------------------
            // WIDGETS
            //------------------------------
        
            .state ('widgets', {
                url: '/widgets',
                templateUrl: 'resources/views/common.html'
            })

            .state ('widgets.widgets', {
                url: '/widgets',
                templateUrl: 'resources/views/widgets.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/mediaelement/build/mediaelementplayer.css',
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/bower_components/mediaelement/build/mediaelement-and-player.js',
                                    'resources/vendors/bower_components/autosize/dist/autosize.min.js'
                                ]
                            }
                        ])
                    }
                }
            })

            .state ('widgets.widget-templates', {
                url: '/widget-templates',
                templateUrl: 'resources/views/widget-templates.html',
            })


            //------------------------------
            // TABLES
            //------------------------------
        
            .state ('tables', {
                url: '/tables',
                templateUrl: 'resources/views/common.html'
            })
            
            .state ('tables.tables', {
                url: '/tables',
                templateUrl: 'resources/views/tables.html'
            })
            
            .state ('tables.data-table', {
                url: '/data-table',
                templateUrl: 'resources/views/data-table.html'
            })

        
            //------------------------------
            // FORMS
            //------------------------------
            .state ('form', {
                url: '/form',
                templateUrl: 'resources/views/common.html'
            })

            .state ('form.basic-form-elements', {
                url: '/basic-form-elements',
                templateUrl: 'resources/views/form-elements.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/bower_components/autosize/dist/autosize.min.js'
                                ]
                            }
                        ])
                    }
                }
            })

            .state ('form.form-components', {
                url: '/form-components',
                templateUrl: 'resources/views/form-components.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/nouislider/jquery.nouislider.css',
                                    'resources/vendors/farbtastic/farbtastic.css',
                                    'resources/vendors/bower_components/summernote/dist/summernote.css',
                                    'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css',
                                    'resources/vendors/bower_components/chosen/chosen.min.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/input-mask/input-mask.min.js',
                                    'resources/vendors/bower_components/nouislider/jquery.nouislider.min.js',
                                    'resources/vendors/bower_components/moment/min/moment.min.js',
                                    'resources/vendors/bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js',
                                    'resources/vendors/bower_components/summernote/dist/summernote.min.js',
                                    'resources/vendors/fileinput/fileinput.min.js',
                                    'resources/vendors/bower_components/chosen/chosen.jquery.js',
                                    'resources/vendors/bower_components/angular-chosen-localytics/chosen.js',
                                    'resources/vendors/bower_components/angular-farbtastic/angular-farbtastic.js'
                                ]
                            }
                        ])
                    }
                }
            })
        
            .state ('form.form-examples', {
                url: '/form-examples',
                templateUrl: 'resources/views/form-examples.html'
            })
        
            .state ('form.form-validations', {
                url: '/form-validations',
                templateUrl: 'resources/views/form-validations.html'
            })
        
            
            //------------------------------
            // USER INTERFACE
            //------------------------------
        
            .state ('user-interface', {
                url: '/user-interface',
                templateUrl: 'resources/views/common.html'
            })
        
            .state ('user-interface.ui-bootstrap', {
                url: '/ui-bootstrap',
                templateUrl: 'resources/views/ui-bootstrap.html'
            })

            .state ('user-interface.colors', {
                url: '/colors',
                templateUrl: 'resources/views/colors.html'
            })

            .state ('user-interface.animations', {
                url: '/animations',
                templateUrl: 'resources/views/animations.html'
            })
        
            .state ('user-interface.box-shadow', {
                url: '/box-shadow',
                templateUrl: 'resources/views/box-shadow.html'
            })
        
            .state ('user-interface.buttons', {
                url: '/buttons',
                templateUrl: 'resources/views/buttons.html'
            })
        
            .state ('user-interface.icons', {
                url: '/icons',
                templateUrl: 'resources/views/icons.html'
            })
        
            .state ('user-interface.alerts', {
                url: '/alerts',
                templateUrl: 'resources/views/alerts.html'
            })

            .state ('user-interface.preloaders', {
                url: '/preloaders',
                templateUrl: 'resources/views/preloaders.html'
            })

            .state ('user-interface.notifications-dialogs', {
                url: '/notifications-dialogs',
                templateUrl: 'resources/views/notification-dialog.html'
            })
        
            .state ('user-interface.media', {
                url: '/media',
                templateUrl: 'resources/views/media.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/mediaelement/build/mediaelementplayer.css',
                                    'resources/vendors/bower_components/lightgallery/light-gallery/css/lightGallery.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/bower_components/mediaelement/build/mediaelement-and-player.js',
                                    'resources/vendors/bower_components/lightgallery/light-gallery/js/lightGallery.min.js'
                                ]
                            }
                        ])
                    }
                }
            })
        
            .state ('user-interface.other-components', {
                url: '/other-components',
                templateUrl: 'resources/views/other-components.html'
            })
            
        
            //------------------------------
            // CHARTS
            //------------------------------
            
            .state ('charts', {
                url: '/charts',
                templateUrl: 'resources/views/common.html'
            })

            .state ('charts.flot-charts', {
                url: '/flot-charts',
                templateUrl: 'resources/views/flot-charts.html',
            })

            .state ('charts.other-charts', {
                url: '/other-charts',
                templateUrl: 'resources/views/other-charts.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/sparklines/jquery.sparkline.min.js',
                                    'resources/vendors/bower_components/jquery.easy-pie-chart/dist/jquery.easypiechart.min.js',
                                ]
                            }
                        ])
                    }
                }
            })
        
        
            //------------------------------
            // CALENDAR
            //------------------------------
            
            .state ('calendar', {
                url: '/calendar',
                templateUrl: 'resources/views/calendar.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/fullcalendar/dist/fullcalendar.min.css',
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/bower_components/moment/min/moment.min.js',
                                    'resources/vendors/bower_components/fullcalendar/dist/fullcalendar.min.js'
                                ]
                            }
                        ])
                    }
                }
            })
        
        
            //------------------------------
            // PHOTO GALLERY
            //------------------------------
            
             .state ('photo-gallery', {
                url: '/photo-gallery',
                templateUrl: 'resources/views/common.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/lightgallery/light-gallery/css/lightGallery.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/bower_components/lightgallery/light-gallery/js/lightGallery.min.js'
                                ]
                            }
                        ])
                    }
                }
            })

            //Default
        
            .state ('photo-gallery.photos', {
                url: '/photos',
                templateUrl: 'resources/views/photos.html'
            })
        
            //Timeline
    
            .state ('photo-gallery.timeline', {
                url: '/timeline',
                templateUrl: 'resources/views/photo-timeline.html'
            })
        
        
            //------------------------------
            // GENERIC CLASSES
            //------------------------------
            
            .state ('generic-classes', {
                url: '/generic-classes',
                templateUrl: 'resources/views/generic-classes.html'
            })
        
            
            //------------------------------
            // PAGES
            //------------------------------
            
            .state ('pages', {
                url: '/pages',
                templateUrl: 'resources/views/common.html'
            })
            
        
            //Profile
        
            .state ('pages.profile', {
                url: '/profile',
                templateUrl: 'resources/views/profile.html'
            })
        
            .state ('pages.profile.profile-about', {
                url: '/profile-about',
                templateUrl: 'resources/views/profile-about.html'
            })
        
            .state ('pages.profile.profile-timeline', {
                url: '/profile-timeline',
                templateUrl: 'resources/views/profile-timeline.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/lightgallery/light-gallery/css/lightGallery.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/bower_components/lightgallery/light-gallery/js/lightGallery.min.js'
                                ]
                            }
                        ])
                    }
                }
            })

            .state ('pages.profile.profile-photos', {
                url: '/profile-photos',
                templateUrl: 'resources/views/profile-photos.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'css',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/lightgallery/light-gallery/css/lightGallery.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/bower_components/lightgallery/light-gallery/js/lightGallery.min.js'
                                ]
                            }
                        ])
                    }
                }
            })
        
            .state ('pages.profile.profile-connections', {
                url: '/profile-connections',
                templateUrl: 'resources/views/profile-connections.html'
            })
        
        
            //-------------------------------
        
            .state ('pages.listview', {
                url: '/listview',
                templateUrl: 'resources/views/list-view.html'
            })
        
            .state ('pages.messages', {
                url: '/messages',
                templateUrl: 'resources/views/messages.html'
            })
        
            .state ('pages.pricing-table', {
                url: '/pricing-table',
                templateUrl: 'resources/views/pricing-table.html'
            })
        
            .state ('pages.contacts', {
                url: '/contacts',
                templateUrl: 'resources/views/contacts.html'
            })
        
            .state ('pages.invoice', {
                url: '/invoice',
                templateUrl: 'resources/views/invoice.html'
            })
                                
            .state ('pages.wall', {
                url: '/wall',
                templateUrl: 'resources/views/wall.html',
                resolve: {
                    loadPlugin: function($ocLazyLoad) {
                        return $ocLazyLoad.load ([
                            {
                                name: 'vendors',
                                insertBefore: '#app-level',
                                files: [
                                    'resources/vendors/bower_components/autosize/dist/autosize.min.js',
                                    'resources/vendors/bower_components/lightgallery/light-gallery/css/lightGallery.css'
                                ]
                            },
                            {
                                name: 'vendors',
                                files: [
                                    'resources/vendors/bower_components/mediaelement/build/mediaelement-and-player.js',
                                    'resources/vendors/bower_components/lightgallery/light-gallery/js/lightGallery.min.js'
                                ]
                            }
                        ])
                    }
                }
            })
            
            //------------------------------
            // BREADCRUMB DEMO
            //------------------------------
            .state ('breadcrumb-demo', {
                url: '/breadcrumb-demo',
                templateUrl: 'resources/views/breadcrumb-demo.html'
            })
    });
