package org.example

import groovyx.net.http.HTTPBuilder
import groovy.json.JsonSlurper
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST
import javax.annotation.PreDestroy

class OauthService {

    LinkGenerator grailsLinkGenerator
    GrailsApplication grailsApplication

    private parseWithHttpBuilder = true

    @Lazy
    private oauthProvider = grailsApplication.config.oauthProvider.baseUrl

    private HttpClient httpClient = new DefaultHttpClient()

    /**
     * Exchange an authorization code for an access token
     * @param authCode
     * @return
     */
    def exchangeAuthCode(String authCode) {

        def callback = grailsLinkGenerator.link(controller: 'auth', action: 'callback', absolute: true)
        def oauthProviderAccessTokenUrl = "$oauthProvider/oauth/token"

        def params = [
                // the scope param is not required by the OAuth spec. it's a workaround for this issue
                // https://github.com/bluesliverx/grails-spring-security-oauth2-provider/issues/64
                scope       : 'read',
                grant_type  : 'authorization_code',
                code        : authCode,
                client_id   : 'my-client',
                redirect_uri: callback
        ]

        if (parseWithHttpBuilder) {
            new HTTPBuilder(oauthProviderAccessTokenUrl).request(POST, JSON) {
                uri.query = params

                response.success = { resp, json ->
                    json
                }

                response.failure = { resp, json ->
                    log.error "HTTP error code: $resp.status, status line: $resp.statusLine, "

                    json.each { key, value ->
                        log.error "$key: $value"
                    }
                }
            }
        } else {
            HttpPost httpPost = new HttpPost(oauthProviderAccessTokenUrl)
            List<NameValuePair> postParams = params.collect {
                new BasicNameValuePair(it.key, it.value)
            }

            httpPost.entity = new UrlEncodedFormEntity(postParams)
            HttpResponse response = httpClient.execute(httpPost)

            try {
                String responseBody = response.entity.content.text
                log.debug "Access token response: $responseBody"
                EntityUtils.consume(response.entity)

                new JsonSlurper().parseText(responseBody)

            } finally {
                httpPost.releaseConnection()
            }
        }
    }

    @PreDestroy
    private void close() {
        httpClient.connectionManager.shutdown()
    }
}