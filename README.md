## Dependencies
This project must be run with Grails 2.4.4 on JDK7

## OAuth Demo
 - Start the [OAuth provider app](https://github.com/domurtag/oauth2-provider) with `grails run-app`
 - Start this app (an OAuth client) on port 9090 with `grails run-app -Dserver.port=9090`
 - On the homepage of the client app click the "OAuth Login" link. This redirects to the provider app and prompts you to login. 
 - Enter the username "me", password "password", and click the "Authorize" button in the confirmation dialog that follows. The access token issued by the provider is displayed in the browser.
