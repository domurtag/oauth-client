class UrlMappings {

	static mappings = {

        "/oauth/callback/enfield" {
            controller = 'oauth'
            action = 'callback'
        }

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
