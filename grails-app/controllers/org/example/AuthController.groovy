package org.example

class AuthController {

    OauthService oauthService

    /**
     * The callback action for OAuth2 login
     */
    def callback(String code) {
        def response = oauthService.getAccessToken(code)
        log.info "Exchanged auth code $code for access token $response.access_token"
        render text: response, contentType: 'application/json'
    }
}
