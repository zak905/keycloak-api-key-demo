var express = require('express')
const http = require('http');
var app = express()


app.use(function (req, res, next) {
    //always received as lower case by node
    let apiKey = req.headers["x-api-key"];

    let authServer = process.env.AUTH_SERVER_URL
    let realmName = process.env.REALM_NAME;

    console.log(`checking api key ${apiKey}, auth server ${authServer}`)

    http.get("http://"+authServer+"/auth/realms/"+realmName+"/check?apiKey="+apiKey, (authResponse) => {
        console.log(`received ${authResponse.statusCode} status from Keycloak`)
       if (authResponse.statusCode == 200) {
           next()
       } else {
          res.status(401).send();
       }
    });
  })


app.get('/', function (req, res) {        
    console.log("returning response")
    res.status(200).set("Content-Type", "application/json").send('{"forecast" : "weather is cool today"}')
})

var server = app.listen(8280, function () {
    console.log("service running at http://%s:%s", server.address().address, server.address().port)
 })