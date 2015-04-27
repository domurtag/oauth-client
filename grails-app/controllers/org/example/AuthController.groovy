package org.example

class AuthController {

    OauthService oauthService

    /**
     * The callback action for OAuth2 login
     */
    def callback(String code) {
        def response = oauthService.exchangeAuthCode(code)

        if (response.error) {
            log.error "Auth code exchange failed: $response"
        } else {
            session.accessToken = response
            log.info "Exchanged auth code $code for access token $response"
        }

        redirect uri: '/'
    }

    def clearToken() {
        session.removeAttribute('accessToken')
        redirect uri: '/'
    }
}
