package org.example

class AuthController {

    OauthService oauthService

    /**
     * The callback action for OAuth2 login
     */
    def callback(String code) {
        String accessToken = oauthService.getAccessToken(code)
        session.accessToken = accessToken
        render text: "Exchanged auth code '$code' for access token '$accessToken'"
    }
}
