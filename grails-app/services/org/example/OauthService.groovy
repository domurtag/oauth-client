package org.example

import groovyx.net.http.HTTPBuilder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

class OauthService {

    LinkGenerator grailsLinkGenerator
    GrailsApplication grailsApplication

    @Lazy
    private oauthProvider = grailsApplication.config.oauthProvider.baseUrl

    String getAccessToken(String authCode) {

        // a "Redirect URI mismatch" error will occur if the redirect_uri param is omitted, even though
        // we've already been called back by the time this code is invoked
        // https://github.com/bluesliverx/grails-spring-security-oauth2-provider/issues/67
        def callback = grailsLinkGenerator.link(controller: 'auth', action: 'callback', absolute: true)

        def params = [
                // the scope param is not required by the OAuth spec. it's a workaround for this issue
                // https://github.com/bluesliverx/grails-spring-security-oauth2-provider/issues/64
                scope: 'read',
                grant_type: 'authorization_code',
                code: authCode,
                client_id: 'my-client',
                redirect_uri: callback
        ]


        def oauthProviderAccessTokenUrl = "$oauthProvider/oauth/token"

        new HTTPBuilder(oauthProviderAccessTokenUrl).request(POST, JSON) {
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