### BACKEND START ###
backend buyersguideapi10 {
    .host = "127.0.0.1";
    .port = "8080";
  .probe = {
      .url = "/videoapi/3.0/healthcheck";
      .interval = 3s;
      .window = 5;
      .threshold = 2;
  }
}
### BACKEND END ###

### VCL_RECV START ###
if (req.url ~ "^/videoapi/3\.0") {

  /* Set the correct backend based on the URI */
  set req.backend = buyersguideapi10;

  /* Capture the correct IP from the client */
  if (req.restarts == 0) {
        if (req.http.x-forwarded-for) {
            set req.http.X-Forwarded-For =
                req.http.X-Forwarded-For + ", " + client.ip;
        } else {
            set req.http.X-Forwarded-For = client.ip;
        }
    }

  /* Send request to pipe if we are not a request type inthe following list */
  if (req.request != "GET" &&
    req.request != "HEAD" &&
    req.request != "PUT" &&
    req.request != "POST" &&
    req.request != "TRACE" &&
    req.request != "OPTIONS" &&
    req.request != "DELETE") {
    /* Non-RFC2616 or CONNECT which is weird. */
    return (pipe);
  }
  
  /* Only cache get and head requests */
  if (req.request != "GET" && req.request != "HEAD") {
    return (pass);
  } 

  /* Set the grace for healthy and non-healthy statuses */
  if (req.backend.healthy) {
    set req.grace = 30s;
  } else {
    set req.grace = 24h;  
  }

  /* Send the request to lookup in cache */
  return (lookup);
}
### VCL_RECV END ###

### VCL_FETCH START ###
if (req.url ~ "^/videoapi/3\.0") {

  /* Do not cache the following list of http status codes */
  if (beresp.status == 404 ||
    beresp.status == 400 ||
    beresp.status == 503 ||
    beresp.status == 500 ||
    beresp.status == 206) {
      set beresp.http.X-Cacheable = "NO: beresp.status";
      set beresp.http.X-Cacheable-status = beresp.status;
      return (hit_for_pass);
  } else {
    /* Cache all other responses and set the object lifetime grace and ttl */
    set beresp.ttl = 300s;
    set beresp.grace = 24h;
    return (deliver);
  }
}
### VCL_FETCH END ###