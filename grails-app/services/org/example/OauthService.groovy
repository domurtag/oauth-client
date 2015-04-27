package org.example

import groovy.json.JsonSlurper
import groovyx.net.http.*
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

import javax.annotation.PreDestroy

class OauthService {

    LinkGenerator grailsLinkGenerator
    GrailsApplication grailsApplication

    @Lazy
    private oauthProvider = grailsApplication.config.oauthProvider.baseUrl

    private HttpClient httpclient = new DefaultHttpClient()

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

        HttpPost httpPost = new HttpPost(oauthProviderAccessTokenUrl)
        List<NameValuePair> postParams = params.collect {
            new BasicNameValuePair(it.key, it.value)
        }

        httpPost.entity = new UrlEncodedFormEntity(postParams)
        HttpResponse response = httpclient.execute(httpPost)

        try {
            String responseBody = response.entity.content.text
            log.debug "Access token response: $responseBody"
            EntityUtils.consume(response.entity)

            new JsonSlurper().parseText(responseBody)

        } finally {
            httpPost.releaseConnection()
        }
    }

    @PreDestroy
    private void close() {
        httpclient.connectionManager.shutdown()
    }
}