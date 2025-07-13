// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.practice.demo.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.practice.demo.UserRole'
grails.plugin.springsecurity.authority.className = 'com.practice.demo.Role'

grails.plugin.springsecurity.securityConfigType = 'Annotation'
grails.plugin.springsecurity.useSecurityAnnotation = true

//grails.cors.enabled = true
//grails.cors.allowedOrigins = ['http://localhost:3333']
//grails.cors.allowedMethods = ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS']
//grails.cors.allowedHeaders = ['Content-Type', 'Accept']
//grails.cors.allowCredentials = true

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
		[pattern: '/error',          access: ['permitAll']],
		[pattern: '/index',          access: ['permitAll']],
		[pattern: '/index.gsp',      access: ['permitAll']],
		[pattern: '/shutdown',       access: ['permitAll']],
		[pattern: '/assets/**',      access: ['permitAll']],
		[pattern: '/**/js/**',       access: ['permitAll']],
		[pattern: '/**/css/**',      access: ['permitAll']],
		[pattern: '/**/images/**',   access: ['permitAll']],
		[pattern: '/**/favicon.ico', access: ['permitAll']],
		[pattern: '/login/page/**',  access: ['permitAll']],
		[pattern: '/my/login/**',    access: ['permitAll']],
		[pattern: '/api/login/**',   access: ['permitAll']],
		[pattern: '/api/logout',     access: ['permitAll']],
		[pattern: '/teachers/**',    access: ['ROLE_ADMIN']],
		[pattern: '/**',             access: ['isAuthenticated()']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
		[pattern: '/error',          filters: 'none'],
		[pattern: '/assets/**',      filters: 'none'],
		[pattern: '/**/js/**',       filters: 'none'],
		[pattern: '/**/css/**',      filters: 'none'],
		[pattern: '/**/images/**',   filters: 'none'],
		[pattern: '/**/favicon.ico', filters: 'none'],
		[pattern: '/api/login',      filters: 'none'],
//		[pattern: '/api/login',      filters: 'corsFilter'],
		[pattern: '/api/logout',     filters: 'none'],
		[pattern: '/login/page/**',  filters: 'none'],
		[pattern: '/my/login',       filters: 'none'],
		[pattern: '/**',             filters: 'JOINED_FILTERS']
]
