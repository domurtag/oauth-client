package org.example

import groovyx.net.http.HTTPBuilder
import org.codehaus.groovy.grails.commons.GrailsApplication

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.*
import static groovyx.net.http.Method.POST


class OauthService {

    String getAccessToken(String authCode) {

        def url = 'http://localhost:8080/oauth2-provider/oauth/token'

        def params = [
                grant_type: 'authorization_code',
                code: authCode,
                client_id: 'my-client'
        ]

        new HTTPBuilder(url).request(POST, JSON) {
            uri.query = params

            response.success = { resp, json ->
                json.access_token
            }

            response.failure = { resp, json ->
                log.error "HTTP error code: $resp.status, status line: $resp.statusLine, "

                json.each { key, value ->
                    log.error "$key: $value"
                }
            }
        }
    }
}