<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
    <style type="text/css" media="screen">
    #status {
        background-color: #eee;
        border: .2em solid #fff;
        margin: 2em 2em 1em;
        padding: 1em;
        width: 12em;
        float: left;
        -moz-box-shadow: 0px 0px 1.25em #ccc;
        -webkit-box-shadow: 0px 0px 1.25em #ccc;
        box-shadow: 0px 0px 1.25em #ccc;
        -moz-border-radius: 0.6em;
        -webkit-border-radius: 0.6em;
        border-radius: 0.6em;
    }

    .ie6 #status {
        display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
    }

    #status ul {
        font-size: 0.9em;
        list-style-type: none;
        margin-bottom: 0.6em;
        padding: 0;
    }

    #status li {
        line-height: 1.3;
    }

    #status h1 {
        text-transform: uppercase;
        font-size: 1.1em;
        margin: 0 0 0.3em;
    }

    #page-body {
        margin: 2em 1em 1.25em 18em;
    }

    h2 {
        margin-top: 1em;
        margin-bottom: 0.3em;
        font-size: 1em;
    }

    p {
        line-height: 1.5;
        margin: 0.25em 0;
    }

    #controller-list ul {
        list-style-position: inside;
    }

    #controller-list li {
        line-height: 1.3;
        list-style-position: inside;
        margin: 0.25em 0;
    }

    @media screen and (max-width: 480px) {
        #status {
            display: none;
        }

        #page-body {
            margin: 0 1em 1em;
        }

        #page-body h1 {
            margin-top: 0;
        }
    }

    #refresh {
        margin-top: 20px;
    }

    #refresh button {
        font-size: 16px;
        background-color: #EEEEEE;
        font-weight: bold;
    }

    .top-spacer {
        margin-top: 20px;
    }
    </style>
</head>

<body>
<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

<div id="status" role="complementary">
    <h1>Application Status</h1>
    <ul>
        <li>App version: <g:meta name="app.version"/></li>
        <li>Grails version: <g:meta name="app.grails.version"/></li>
        <li>Groovy version: ${GroovySystem.getVersion()}</li>
        <li>JVM version: ${System.getProperty('java.version')}</li>
        <li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
        <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
        <li>Domains: ${grailsApplication.domainClasses.size()}</li>
        <li>Services: ${grailsApplication.serviceClasses.size()}</li>
        <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
    </ul>

    <h1>Installed Plugins</h1>
    <ul>
        <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
            <li>${plugin.name} - ${plugin.version}</li>
        </g:each>
    </ul>
</div>

<div id="page-body" role="main">
    <g:set var="redirectUrl" value="${g.createLink(controller: 'auth', action: 'callback', absolute: true)}"/>
    <h2>
        <a href="http://127.0.0.1:9000/#/oauth?response_type=code&client_id=grails-client&scope=profile&redirect_uri=${redirectUrl}">OAuth Login</a>
    </h2>

    <g:if test="${session.accessToken}">
        <oauth:renderToken/>
        <h2>
            <g:link controller="auth" action="clearToken">Clear Token</g:link>
        </h2>

        <h2>
            <g:link controller="auth" action="refreshToken">Refresh Token</g:link>
        </h2>
    </g:if>
    <g:else>
        An access token has not been issued to this client. Click the link above to initiate the OAuth login.
    </g:else>

    <h2 class="top-spacer">Kerp Provider</h2>
    <p>
        To use Kerp as the provider for this client, make sure the client is running on port 9090 and register a client
        in the provider's <code>BootStrap.groovy</code> with the following configuration:
    </p>

    <p class="top-spacer">
        <code>
            <pre>
new Client(
    clientId: 'grails-client',
    authorizedGrantTypes: ['authorization_code', 'refresh_token'],
    authorities: ['ROLE_CLIENT'],
    scopes: ['profile', 'scenario', 'snapshot', 'assessment'],
    redirectUris: ['http://localhost:9090/oauth-client/auth/callback'],
    clientSecret: 'secret'
)
            </pre>
        </code>
    </p>
</div>
</body>
</html>
