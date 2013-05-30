function jsonrpcpost(url,method,data,callback) {
    var jparams = {};
    jparams[gadgets.io.RequestParameters.HEADERS] = {
              "Content-Type" : "application/json",
    }
    jparams[gadgets.io.RequestParameters.CONTENT_TYPE]  = gadgets.io.ContentType.JSON;
    jparams[gadgets.io.RequestParameters.METHOD]        = gadgets.io.MethodType.POST;
    var rpcdata = { "id":1, "method":method, "params":data };
    jparams[gadgets.io.RequestParameters.POST_DATA]= gadgets.json.stringify(rpcdata);
    gadgets.io.makeRequest(url, callback, jparams );
}

