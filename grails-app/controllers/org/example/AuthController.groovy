package org.example

class AuthController {

    OauthService oauthService

    static allowedMethods = [callback: 'GET', clearToken: 'GET', refreshToken: 'GET']

    /**
     * The authorization code callback
     */
    def callback(AuthCodeResponse authCodeResponse) {

        if (authCodeResponse.code) {
            def accessTokenResponse = oauthService.exchangeAuthCode(authCodeResponse.code)

            if (accessTokenResponse.access_token) {
                session.accessToken = accessTokenResponse
                log.info "Exchanged auth code $authCodeResponse.code for access token $session.accessToken.access_token"

            } else {
                log.error "Access token request failed: $accessTokenResponse"
            }
        } else {
            log.error "Auth code request failed: ${[error: authCodeResponse.error, error_description: authCodeResponse.error_description]}"
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

class AuthCodeResponse {
    String code
    String error
    String error_description
}