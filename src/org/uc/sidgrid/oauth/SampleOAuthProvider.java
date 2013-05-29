/*
 * Copyright 2007 AOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uc.sidgrid.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;
import net.oauth.server.OAuthServlet;

/**
 * Utility methods for providers that store consumers, tokens and secrets in 
 * local cache (HashSet). Consumer key is used as the name, and its credentials are 
 * stored in HashSet.
 *
 * @author Praveen Alavilli
 */
public class SampleOAuthProvider {

    public static final OAuthValidator VALIDATOR = new SimpleOAuthValidator();

    private static final Map<String, OAuthConsumer> ALL_CONSUMERS 
                    = Collections.synchronizedMap(new HashMap<String,OAuthConsumer>(10));
    
    private static final Collection<OAuthAccessor> ALL_TOKENS = new HashSet<OAuthAccessor>();

    private static Properties consumerProperties = null;
    private static Log log = LogFactory.getLog(SampleOAuthProvider.class);

    private static final String PUBLIC_KEY =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0YjCwIfYoprq/FQO6lb3asXrx"
      + "LlJFuCvtinTF5p0GxvQGu5O3gYytUvtC2JlYzypSRjVxwxrsuRcP3e641SdASwfr"
      + "mzyvIgP08N4S0IFzEURkV1wp/IpH7kH41EtbmUmrXSwfNZsnQRE5SYSOhh+LcK2w"
      + "yQkdgcMv11l4KoBkcwIDAQAB";

    private static final String CERTIFICATE =
    	"-----BEGIN CERTIFICATE-----\n"+
    	"MIIDBDCCAm2gAwIBAgIJAK8dGINfkSTHMA0GCSqGSIb3DQEBBQUAMGAxCzAJBgNV\n"+
    	"BAYTAlVTMQswCQYDVQQIEwJDQTEWMBQGA1UEBxMNTW91bnRhaW4gVmlldzETMBEG\n"+
    	"A1UEChMKR29vZ2xlIEluYzEXMBUGA1UEAxMOd3d3Lmdvb2dsZS5jb20wHhcNMDgx\n"+
    	"MDA4MDEwODMyWhcNMDkxMDA4MDEwODMyWjBgMQswCQYDVQQGEwJVUzELMAkGA1UE\n"+
    	"CBMCQ0ExFjAUBgNVBAcTDU1vdW50YWluIFZpZXcxEzARBgNVBAoTCkdvb2dsZSBJ\n"+
    	"bmMxFzAVBgNVBAMTDnd3dy5nb29nbGUuY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GN\n"+
    	"ADCBiQKBgQDQUV7ukIfIixbokHONGMW9+ed0E9X4m99I8upPQp3iAtqIvWs7XCbA\n"+
    	"bGqzQH1qX9Y00hrQ5RRQj8OI3tRiQs/KfzGWOdvLpIk5oXpdT58tg4FlYh5fbhIo\n"+
    	"VoVn4GvtSjKmJFsoM8NRtEJHL1aWd++dXzkQjEsNcBXwQvfDb0YnbQIDAQABo4HF\n"+
    	"MIHCMB0GA1UdDgQWBBSm/h1pNY91bNfW08ac9riYzs3cxzCBkgYDVR0jBIGKMIGH\n"+
    	"gBSm/h1pNY91bNfW08ac9riYzs3cx6FkpGIwYDELMAkGA1UEBhMCVVMxCzAJBgNV\n"+
    	"BAgTAkNBMRYwFAYDVQQHEw1Nb3VudGFpbiBWaWV3MRMwEQYDVQQKEwpHb29nbGUg\n"+
    	"SW5jMRcwFQYDVQQDEw53d3cuZ29vZ2xlLmNvbYIJAK8dGINfkSTHMAwGA1UdEwQF\n"+
    	"MAMBAf8wDQYJKoZIhvcNAQEFBQADgYEAYpHTr3vQNsHHHUm4MkYcDB20a5KvcFoX\n"+
    	"gCcYtmdyd8rh/FKeZm2me7eQCXgBfJqQ4dvVLJ4LgIQiU3R5ZDe0WbW7rJ3M9ADQ\n"+
    	"FyQoRJP8OIMYW3BoMi0Z4E730KSLRh6kfLq4rK6vw7lkH9oynaHHWZSJLDAp17cP\n"+
    	"j+6znWkN9/g=\n"+
    	"-----END CERTIFICATE-----";
    public static synchronized void loadConsumers(
            ServletConfig config) throws IOException {
        Properties p = consumerProperties;
        if (p == null) {
            p = new Properties();
            String resourceName = "/"
                    + SampleOAuthProvider.class.getPackage().getName().replace(
                    ".", "/") + "/provider.properties";
            URL resource = SampleOAuthProvider.class.getClassLoader()
            .getResource(resourceName);
            if (resource == null) {
                throw new IOException("resource not found: " + resourceName);
            }
            InputStream stream = resource.openStream();
            try {
                p.load(stream);
            } finally {
                stream.close();
            }
        }
        consumerProperties = p;
        
        // for each entry in the properties file create a OAuthConsumer
        for(Map.Entry prop : p.entrySet()) {
            String consumer_key = (String) prop.getKey();
            log.info("add property for "+consumer_key);
            // make sure it's key not additional properties
            //if(!consumer_key.contains(".")){
                String consumer_secret = (String) prop.getValue();
                if(consumer_secret != null){
                    String consumer_description = (String) p.getProperty(consumer_key + ".description");
                    String consumer_callback_url =  (String) p.getProperty(consumer_key + ".callbackURL");
                    String publicKey = (String) p.getProperty(consumer_key + ".RSA-SHA1.PublicKey");
                    String cert = (String) p.getProperty(consumer_key + ".RSA-SHA1.X509Certificate");
                    // Create OAuthConsumer w/ key and secret
                    OAuthConsumer consumer = new OAuthConsumer(
                            consumer_callback_url, 
                            consumer_key, 
                            consumer_secret, 
                            null);
                    consumer.setProperty("name", consumer_key);
                    consumer.setProperty("description", consumer_description);
                    if (publicKey!=null){
                    	consumer.setProperty("RSA-SHA1.PublicKey",publicKey);
                    	//consumer.setProperty("RSA-SHA1.X509Certificate", CERTIFICATE);
                    }
                    if (cert!=null){
                    	consumer.setProperty("RSA-SHA1.X509Certificate",cert);
                    }
                    ALL_CONSUMERS.put(consumer_key, consumer);
                }
            //}
        }
        
    }

    public static synchronized OAuthConsumer getConsumer(
            OAuthMessage requestMessage)
            throws IOException, OAuthProblemException {
        
        OAuthConsumer consumer = null;
        // try to load from local cache if not throw exception
        String consumer_key = requestMessage.getConsumerKey();
        log.info("consumer "+consumer_key+" is here");
        consumer = SampleOAuthProvider.ALL_CONSUMERS.get(consumer_key.trim());
        if (consumer == null){
        	log.error("consumer "+consumer_key+" is not registered");
        	//consumer = SampleOAuthProvider.ALL_CONSUMERS.get("www.google.com");
        }
        if(consumer == null) {
            OAuthProblemException problem = new OAuthProblemException("token_rejected");
            throw problem;
        }
        
        return consumer;
    }
    
    /**
     * Get the access token and token secret for the given oauth_token. 
     */
    public static synchronized OAuthAccessor getAccessor(OAuthMessage requestMessage)
            throws IOException, OAuthProblemException {
        
        // try to load from local cache if not throw exception
        String consumer_token = requestMessage.getToken();
        OAuthAccessor accessor = null;
        for (OAuthAccessor a : SampleOAuthProvider.ALL_TOKENS) {
            if(a.requestToken != null) {
                if (a.requestToken.equals(consumer_token)) {
                    accessor = a;
                    break;
                }
            } else if(a.accessToken != null){
                if (a.accessToken.equals(consumer_token)) {
                    accessor = a;
                    break;
                }
            }
        }
        
        if(accessor == null){
            OAuthProblemException problem = new OAuthProblemException("token_expired");
            throw problem;
        }
        
        return accessor;
    }

    /**
     * Set the access token 
     */
    public static synchronized void markAsAuthorized(OAuthAccessor accessor, String userId)
            throws OAuthException {
        
        log.info("setting accessor in markAsAuthorized");       
        // first remove the accessor from cache
        ALL_TOKENS.remove(accessor);
        
        accessor.setProperty("user", userId); 
        accessor.setProperty("authorized", Boolean.TRUE);
        
        // update token in local cache
        ALL_TOKENS.add(accessor);
    }
    

    /**
     * Generate a fresh request token and secret for a consumer.
     * 
     * @throws OAuthException
     */
    public static synchronized void generateRequestToken(
            OAuthAccessor accessor)
            throws OAuthException {

        // generate oauth_token and oauth_secret
        String consumer_key = (String) accessor.consumer.getProperty("name");
        // generate token and secret based on consumer_key
        
        // for now use md5 of name + current time as token
        String token_data = consumer_key + System.nanoTime();
        String token = DigestUtils.md5Hex(token_data);
        // for now use md5 of name + current time + token as secret
        String secret_data = consumer_key + System.nanoTime() + token;
        String secret = DigestUtils.md5Hex(secret_data);
        
        accessor.requestToken = token;
        accessor.tokenSecret = secret;
        accessor.accessToken = null;
        
        // add to the local cache
        ALL_TOKENS.add(accessor);
        log.info("generated a request token for "+accessor.consumer.getProperty("name"));
    }
    
    /**
     * Generate a fresh request token and secret for a consumer.
     * 
     * @throws OAuthException
     */
    public static synchronized void generateAccessToken(OAuthAccessor accessor)
            throws OAuthException {

        // generate oauth_token and oauth_secret
        String consumer_key = (String) accessor.consumer.getProperty("name");
        // generate token and secret based on consumer_key
        
        // for now use md5 of name + current time as token
        String token_data = consumer_key + System.nanoTime();
        String token = DigestUtils.md5Hex(token_data);
        // first remove the accessor from cache
        ALL_TOKENS.remove(accessor);
        
        accessor.requestToken = null;
        accessor.accessToken = token;
        
        // update token in local cache
        ALL_TOKENS.add(accessor);
        log.info("generated a access token for "+accessor.consumer.getProperty("name"));
    }

    public static void handleException(Exception e, HttpServletRequest request,
            HttpServletResponse response, boolean sendBody)
            throws IOException, ServletException {
        String realm = (request.isSecure())?"https://":"http://";
        realm += request.getLocalName();
        OAuthServlet.handleException(response, e, realm, sendBody); 
    }

}
