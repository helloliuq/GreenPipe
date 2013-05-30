/*
 * JSON-RPC JavaScript client
 *
 * $Id: jsonrpc.js,v 1.36.2.1 2005/12/09 13:15:32 mclark Exp $
 *
 * Copyright (c) 2003-2004 Jan-Klaas Kollhof
 * Copyright (c) 2005 Michael Clark, Metaparadigm Pte Ltd
 *
 * This code is based on Jan-Klaas' JavaScript o lait library (jsolait).
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public (LGPL)
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details: http://www.gnu.org/
 *
 */


/* escape a character */

escapeJSONChar =
function escapeJSONChar(c)
{
    if(c == "\"" || c == "\\") return "\\" + c;
    else if (c == "\b") return "\\b";
    else if (c == "\f") return "\\f";
    else if (c == "\n") return "\\n";
    else if (c == "\r") return "\\r";
    else if (c == "\t") return "\\t";
    var hex = c.charCodeAt(0).toString(16);
    if(hex.length == 1) return "\\u000" + hex;
    else if(hex.length == 2) return "\\u00" + hex;
    else if(hex.length == 3) return "\\u0" + hex;
    else return "\\u" + hex;
};


/* encode a string into JSON format */

escapeJSONString =
function escapeJSONString(s)
{
    /* The following should suffice but Safari's regex is b0rken
       (doesn't support callback substitutions)
       return "\"" + s.replace(/([^\u0020-\u007f]|[\\\"])/g,
       escapeJSONChar) + "\"";
    */

    /* Rather inefficient way to do it */
    var parts = s.split("");
    for(var i=0; i < parts.length; i++) {
	var c =parts[i];
	if(c == '"' ||
	   c == '\\' ||
	   c.charCodeAt(0) < 32 ||
	   c.charCodeAt(0) >= 128)
	    parts[i] = escapeJSONChar(parts[i]);
    }
    return "\"" + parts.join("") + "\"";
};


/* Marshall objects to JSON format */

toJSON = function toJSON(o)
{
    if(o == null) {
	return "null";
    } else if(o.constructor == String) {
	return escapeJSONString(o);
    } else if(o.constructor == Number) {
	return o.toString();
    } else if(o.constructor == Boolean) {
	return o.toString();
    } else if(o.constructor == Date) {
	return '{javaClass: "java.util.Date", time: ' + o.valueOf() +'}';
    } else if(o.constructor == Array) {
	var v = [];
	for(var i = 0; i < o.length; i++) v.push(toJSON(o[i]));
	return "[" + v.join(", ") + "]";
    } else {
	var v = [];
	for(attr in o) {
	    if(o[attr] == null) v.push("\"" + attr + "\": null");
	    else if(typeof o[attr] == "function"); /* skip */
	    else v.push(escapeJSONString(attr) + ": " + toJSON(o[attr]));
	}
	return "{" + v.join(", ") + "}";
    }
};


/* JSONRpcClient constructor */

JSONRpcClient =
function JSONRpcClient_ctor(serverURL, user, pass, objectID)
{
    this.serverURL = serverURL;
    this.user = user;
    this.pass = pass;
    this.objectID = objectID;
    this.listMethodsDone = false;
    
    var this_object = this;
    var callback = function(obj){
       try {
	    eval("obj = " + obj.text);
      } catch(e) {
	    throw new JSONRpcClient.Exception(550, "error parsing result");
      }
      if(obj.error)
	    throw new JSONRpcClient.Exception(obj.error.code, obj.error.msg,
					  obj.error.trace);
	  res = obj.result;
	  this_object._addMethods(res);
	  this_object.listMethodsDone = true;
    }
    /* Add standard methods */
    if(this.objectID) {
	this._addMethods(["listMethods"]);
	var req = this._makeRequest("listMethods", [],callback);
    } else {
	this._addMethods(["system.listMethods"]);
	var req = this._makeRequest("system.listMethods", [],callback);
    }
    this._sendRequest(req);
    //this._addMethods(m);
    //wait until the callback is finished
    //while(!result
    
};

/* JSONRpcCLient.Exception */

JSONRpcClient.Exception =
function JSONRpcClient_Exception_ctor(code, message, javaStack)
{
    this.code = code;
    var name;
    if(javaStack) {
	this.javaStack = javaStack;
	var m = javaStack.match(/^([^:]*)/);
	if(m) name = m[0];
    }
    if(name) this.name = name;
    else this.name = "JSONRpcClientException";
    this.message = message;
};

JSONRpcClient.Exception.CODE_REMOTE_EXCEPTION = 490;
JSONRpcClient.Exception.CODE_ERR_CLIENT = 550;
JSONRpcClient.Exception.CODE_ERR_PARSE = 590;
JSONRpcClient.Exception.CODE_ERR_NOMETHOD = 591;
JSONRpcClient.Exception.CODE_ERR_UNMARSHALL = 592;
JSONRpcClient.Exception.CODE_ERR_MARSHALL = 593;

JSONRpcClient.Exception.prototype = new Error();

JSONRpcClient.Exception.prototype.toString =
function JSONRpcClient_Exception_toString(code, msg)
{
    return this.name + ": " + this.message;
};


/* Default top level exception handler */

JSONRpcClient.default_ex_handler =
function JSONRpcClient_default_ex_handler(e) { alert(e); };


/* Client settable variables */

JSONRpcClient.toplevel_ex_handler = JSONRpcClient.default_ex_handler;
JSONRpcClient.profile_async = false;
JSONRpcClient.max_req_active = 1;
JSONRpcClient.requestId = 1;


/* JSONRpcClient implementation */

JSONRpcClient.prototype._createMethod =
function JSONRpcClient_createMethod(methodName)
{
    var fn=function()
    {
	var args = [];
	var callback = null;
	for(var i=0;i<arguments.length;i++) args.push(arguments[i]);
	if(typeof args[0] == "function") callback = args.shift();
	var req = fn.client._makeRequest.call(fn.client, fn.methodName,
					      args, callback);
	if(callback == null) {
	    return fn.client._sendRequest.call(fn.client, req);
	} else {
	    //JSONRpcClient.async_requests.push(req);
	    //JSONRpcClient.kick_async();
	    //return req.requestId;
	    return fn.client._sendRequest.call(fn.client, req);
	}
    };
    fn.client = this;
    fn.methodName = methodName;
    return fn;
};

JSONRpcClient.prototype._addMethods =
function JSONRpcClient_addMethods(methodNames)
{
    for(var i=0; i<methodNames.length; i++) {
	var obj = this;
	var names = methodNames[i].split(".");
	for(var n=0; n<names.length-1; n++) {
	    var name = names[n];
	    if(obj[name]) {
		obj = obj[name];
	    } else {
		obj[name]  = new Object();
		obj = obj[name];
	    }
	}
	var name = names[names.length-1];
	if(!obj[name]) {
	    var method = this._createMethod(methodNames[i]);
	    obj[name] = method;
	}
    }
};

JSONRpcClient._getCharsetFromHeaders =
function JSONRpcClient_getCharsetFromHeaders(http)
{
    try {
	var contentType = http.getResponseHeader("Content-type");
	var parts = contentType.split(/\s*;\s*/);
	for(var i =0; i < parts.length; i++) {
	    if(parts[i].substring(0, 8) == "charset=")
		return parts[i].substring(8, parts[i].length);
	}
    } catch (e) {}
    return "UTF-8"; /* default */
};

/* Async queue globals */
JSONRpcClient.async_requests = [];
JSONRpcClient.async_inflight = {};
JSONRpcClient.async_responses = [];
JSONRpcClient.async_timeout = null;
JSONRpcClient.num_req_active = 0;

JSONRpcClient._async_handler =
function JSONRpcClient_async_handler()
{
    JSONRpcClient.async_timeout = null;

    while(JSONRpcClient.async_responses.length > 0) {
	var res = JSONRpcClient.async_responses.shift();
	if(res.canceled) continue;
	if(res.profile) res.profile.dispatch = new Date();
	try {
	    res.cb(res.result, res.ex, res.profile);
	} catch(e) {
	    JSONRpcClient.toplevel_ex_handler(e);
	}
    }

    while(JSONRpcClient.async_requests.length > 0 &&
	  JSONRpcClient.num_req_active < JSONRpcClient.max_req_active) {
	var req = JSONRpcClient.async_requests.shift();
	if(req.canceled) continue;
	req.client._sendRequest.call(req.client, req);
    }
};

JSONRpcClient.kick_async =
function JSONRpcClient_kick_async()
{
    if(JSONRpcClient.async_timeout == null)
	JSONRpcClient.async_timeout =
	    setTimeout(JSONRpcClient._async_handler, 0);
};

JSONRpcClient.prototype._makeRequest =
function JSONRpcClient_makeRequest(methodName, args, cb)
{
    var req = {};
    req.client = this;
    req.requestId = JSONRpcClient.requestId++;

    var obj = {};
    obj.id = req.requestId;
    if (this.objectID)
	obj.method = ".obj#" + this.objectID + "." + methodName;
    else
	obj.method = methodName;
    obj.params = args;

    if (cb) req.cb = cb;
    if (JSONRpcClient.profile_async)
	req.profile = { "submit": new Date() };
    req.data = toJSON(obj);

    return req;
};

JSONRpcClient.prototype._sendRequest =
function JSONRpcClient_sendRequest(req)
{
    var params={};
    params[gadgets.io.RequestParameters.AUTHORIZATION] =
       gadgets.io.AuthorizationType.OAUTH;
    //params[gadgets.io.RequestParameters.OAUTH_TOKEN_NAME] ="wwj";
    //params[gadgets.io.RequestParameters.OAUTH_REQUEST_TOKEN]="gadgetConsumer";
    //params[gadgets.io.RequestParameters.OAUTH_REQUEST_TOKEN_SECRET]="gadgetSecret";
    
    params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.TEXT;
    params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;
    var rpcdata = {rpc:req.data}
    params[gadgets.io.RequestParameters.POST_DATA]= gadgets.io.encodeValues(rpcdata);
    var wait_res = false;
    var res;
    var response = function (obj){
      //alert(obj.text);
      try {
	    eval("obj = " + obj.text);
      } catch(e) {
	    throw new JSONRpcClient.Exception(550, "error parsing result");
      }
      if(obj.error)
	    throw new JSONRpcClient.Exception(obj.error.code, obj.error.msg,
					  obj.error.trace);
	  res = obj.result;
      //req.cb
      wait_res = true;
      return res;
    }
    if (req.cb)
      gadgets.io.makeRequest(this.serverURL, req.cb, params);
    else
      gadgets.io.makeRequest(this.serverURL, response,params);
    /** if (!req.cb){
       while(!wait_res);
       return res;
    }**/
};