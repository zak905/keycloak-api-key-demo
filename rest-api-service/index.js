const http = require('http');

const PORT = 8280;

function checkApiKey(apiKey, callback) {
    let authServer = process.env.AUTH_SERVER_URL;
    let realmName = process.env.REALM_NAME;

    console.log(`checking api key ${apiKey}, auth server ${authServer}`);

    http.get("http://" + authServer + "/auth/realms/" + realmName + "/check?apiKey=" + apiKey, (authResponse) => {
        console.log(`received ${authResponse.statusCode} status from Keycloak`);
        callback(authResponse.statusCode === 200);
    }).on('error', (err) => {
        console.error(`auth request failed: ${err.message}`);
        callback(false);
    });
}

const server = http.createServer((req, res) => {
    const apiKey = req.headers["x-api-key"];

    checkApiKey(apiKey, (authorized) => {
        if (!authorized) {
            res.writeHead(401);
            res.end();
            return;
        }

        if (req.method === 'GET' && req.url === '/') {
            console.log("returning response");
            res.writeHead(200, { "Content-Type": "application/json" });
            res.end('{"forecast" : "weather is cool today"}');
        } else {
            res.writeHead(404);
            res.end();
        }
    });
});

server.listen(PORT, () => {
    const addr = server.address();
    console.log("service running at http://%s:%s", addr.address, addr.port);
});
