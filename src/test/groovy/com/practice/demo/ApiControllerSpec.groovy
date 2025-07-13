package com.practice.demo

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class ApiControllerSpec extends Specification implements ControllerUnitTest<ApiController> {

     void "test index action"() {
        when:
        controller.index()

        then:
        status == 200

     }
}
