package org.example

class OauthController {

    OauthService oauthService

    static allowedMethods = [callback: 'GET', clearToken: 'GET']

    /**
     * The callback action for OAuth2 login
     */
    def callback(String code) {

        if (params.error) {
            log.error "Auth code exchange failed: $params.error"
        } else {
            def accessTokenResponse = oauthService.exchangeAuthCode(code)

            session.accessToken = accessTokenResponse
            log.info "Exchanged auth code $code for access token $accessTokenResponse"
        }

        redirect uri: '/'
    }

    def clearToken() {
        session.removeAttribute('accessToken')
        redirect uri: '/'
    }

    def refreshToken() {

        if (session.accessToken) {
             def response = oauthService.refreshToken(session.accessToken.refresh_token)

            if (response.error) {
                log.error "Access token refresh failed: $response"

            } else {
                session.accessToken = response
                log.info "Refreshed access token $response"
            }
        }

        redirect uri: '/'
    }
}
