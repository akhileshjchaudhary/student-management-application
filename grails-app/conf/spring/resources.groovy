package spring

import com.practice.demo.UserPasswordEncoderListener
import com.practice.demo.CustomJwtAuthenticationFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
//import com.practice.demo.CorsFilter


// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)

    customJwtAuthenticationFilter(CustomJwtAuthenticationFilter) {
        grailsApplication = ref('grailsApplication')
    }

    jwtFilterRegistrationBean(FilterRegistrationBean) {
        filter = ref('customJwtAuthenticationFilter')
        order = -100 // Ensure JWT runs before Spring Security
        urlPatterns = ['/*']
    }

    // ✅ Register CORS filter with correct name
//    corsFilter(CorsFilter)
//
//    corsFilterRegistrationBean(FilterRegistrationBean) {
//        filter = ref('corsFilter')  // refer to correct bean name
//        order = -200
//        urlPatterns = ['/*']
//    }
}
