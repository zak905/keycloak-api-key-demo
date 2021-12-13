## How to run 

you can run the project by running the following from a terminal: `mvn -f api-key-module package && mvn -f dashboard-service package && docker-compose up`

Note: You need to add `auth-server` to your hosts file (`/etc/hosts` for linux) and map it to localhost.

## Testing

1. Navigate to localhost:8180 in a browser, you will redirected to keycloak for authentication
2. you need register a new user, after which you will be redirected to the main dashboard page which will show your API key
3. copy the API key and use it to call the API: `curl -v -H "x-api-key: $THE_API_KEY" localhost:8200`, if you omit the API key, you will get 401 status

More explanations can be found in this blog [post](http://www.zakariaamine.com/2019-06-14/extending-keycloak)
