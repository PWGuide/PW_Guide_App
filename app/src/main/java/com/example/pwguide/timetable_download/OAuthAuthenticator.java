package com.example.pwguide.timetable_download;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base64;

import org.apache.http.client.utils.URIBuilder;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class OAuthAuthenticator
{
    private final String host = "apps.usos.pw.edu.pl";
    private final String requestTokenPath = "/services/oauth/request_token";
    private final String accessTokenPath = "/services/oauth/access_token";
    private final String consumerKey = "";
    private final String consumerSecret = "";
    private final String callback = "oob";

    public String generateOAuthUriForUSOS(String method, String path, String[][] additionalParams, String secret_token) {
        long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        String nonce = nonceGenerator();

        ArrayList<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + consumerKey);
        parameters.add("oauth_nonce=" + nonce);
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_version=1.0");
        for (int i = 0; i < additionalParams.length; i++) {
            parameters.add(additionalParams[i][0] + "=" + additionalParams[i][1]);
        }
        Collections.sort( parameters );

        StringBuilder parametersList = new StringBuilder();

        for (int i = 0; i < parameters.size(); i++) {
            parametersList.append((i > 0) ? "&" : "").append(parameters.get(i));
        }
        String signatureString = null;
        try {
            signatureString = method + "&" +
                    URLEncoder.encode( "https://" + host + path, StandardCharsets.UTF_8.toString()) + "&" +
                    URLEncoder.encode(parametersList.toString(), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        String signature = null;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(
                    (consumerSecret + "&" + (secret_token == null ? "" : secret_token)).getBytes(), "HmacSHA1" );
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHMAC = mac.doFinal( signatureString.getBytes() );
            signature = Base64.encodeBase64String(rawHMAC);
        } catch (Exception e) {
            System.err.println("Unable to append signature");
            return null;
        }

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost(host);
        builder.setPath(path);
        builder.addParameter("oauth_consumer_key", consumerKey);
        builder.addParameter("oauth_nonce", nonce);
        builder.addParameter("oauth_signature_method", "HMAC-SHA1");
        builder.addParameter("oauth_timestamp", Long.toString(timestamp));
        builder.addParameter("oauth_version", "1.0");
        builder.addParameter("oauth_signature", signature);
        for (int i = 0; i < additionalParams.length; i++) {
            builder.addParameter(additionalParams[i][0], additionalParams[i][1]);
        }

        URI uri = null;
        try {
            uri = builder.build().toURL().toURI();
            System.out.println(uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return uri.toString();
    }

    public String generateOauthHeader( String method ) {
        long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        String nonce = nonceGenerator();

        ArrayList< String > parameters = new ArrayList< String >();
        parameters.add( "oauth_consumer_key=" + consumerKey );
        parameters.add( "oauth_nonce=" + nonce );
        parameters.add( "oauth_timestamp=" + timestamp );
        parameters.add( "oauth_signature_method=HMAC-SHA1" );

        try {
            parameters.add( "oauth_callback=" + URLEncoder.encode(callback, StandardCharsets.UTF_8.toString()) );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        parameters.add( "oauth_version=1.0" );
        parameters.add("scopes=" + "studies");

        Collections.sort( parameters );
        StringBuffer parametersList = new StringBuffer();

        for ( int i = 0; i < parameters.size(); i++ ) {
            parametersList.append( ( ( i > 0 ) ? "&" : "" ) + parameters.get( i ) );
        }

        String signatureString = null;
        try {
            signatureString = method + "&" +
                    URLEncoder.encode( "https://" + host + requestTokenPath, StandardCharsets.UTF_8.toString()) + "&" +
                    URLEncoder.encode( parametersList.toString(), StandardCharsets.UTF_8.toString() );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String signature = null;

        try
        {
            SecretKeySpec signingKey = new SecretKeySpec(
                    ( consumerSecret + "&" ).getBytes(), "HmacSHA1" );
            Mac mac = Mac.getInstance( "HmacSHA1" );
            mac.init( signingKey );
            System.out.println("SIGNATURE BASE: " + signatureString);
            byte[] rawHMAC = mac.doFinal( signatureString.getBytes() );
            signature = Base64.encodeBase64String(rawHMAC); //Base64.encodeBytes( rawHMAC );
        }
        catch ( Exception e )
        {
            System.err.println( "Unable to append signature" );
            System.exit( 0 );
        }

        URIBuilder builder = new URIBuilder();
                builder.setScheme("https");
                builder.setHost(host);
                builder.setPath(requestTokenPath);
                builder.addParameter("oauth_callback", callback);
                builder.addParameter("oauth_consumer_key", consumerKey);
                builder.addParameter("oauth_nonce", nonce);
                builder.addParameter("oauth_signature_method", "HMAC-SHA1");
                builder.addParameter("oauth_timestamp", Long.toString(timestamp));
                builder.addParameter("oauth_version", "1.0");
                builder.addParameter("scopes", "studies"); //czy offline access jest potrzebny
                builder.addParameter("oauth_signature", signature);

        URI uri = null;
                try {
                    uri = builder.build().toURL().toURI();
                    System.out.println(uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
        return uri.toString();
    }

    public String generateOauthHeaderAccessToken( String method, String token, String secret_token, String verifier ) {
        long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        String nonce = nonceGenerator();

        ArrayList<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + consumerKey);
        parameters.add("oauth_nonce=" + nonce);
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_version=1.0");
        parameters.add("oauth_token=" + token);
        parameters.add("oauth_verifier=" + verifier);

        Collections.sort( parameters );
        StringBuffer parametersList = new StringBuffer();

        for ( int i = 0; i < parameters.size(); i++ ) {
            parametersList.append( ( ( i > 0 ) ? "&" : "" ) + parameters.get( i ) );
        }

        String signatureString = null;
        try {
            signatureString = method + "&" +
                    URLEncoder.encode( "https://" + host + accessTokenPath, StandardCharsets.UTF_8.toString()) + "&" +
                    URLEncoder.encode( parametersList.toString(), StandardCharsets.UTF_8.toString() );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String signature = null;

        try
        {
            SecretKeySpec signingKey = new SecretKeySpec(
                    ( consumerSecret + "&" + secret_token).getBytes(), "HmacSHA1" );
            Mac mac = Mac.getInstance( "HmacSHA1" );
            mac.init( signingKey );
            System.out.println("SIGNATURE BASE: " + signatureString);
            byte[] rawHMAC = mac.doFinal( signatureString.getBytes() );
            signature = Base64.encodeBase64String(rawHMAC); //Base64.encodeBytes( rawHMAC );
        }
        catch ( Exception e )
        {
            System.err.println( "Unable to append signature" );
            System.exit( 0 );
        }

        System.out.println( signature );

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost(host);
        builder.setPath(accessTokenPath);
        builder.addParameter("oauth_consumer_key", consumerKey);
        builder.addParameter("oauth_nonce", nonce);
        builder.addParameter("oauth_signature_method", "HMAC-SHA1");
        builder.addParameter("oauth_timestamp", Long.toString(timestamp));
        builder.addParameter("oauth_token", token);
        builder.addParameter("oauth_verifier", verifier);
        builder.addParameter("oauth_version", "1.0");
        builder.addParameter("oauth_signature", signature);

        URI uri = null;
        try {
            uri = builder.build().toURL().toURI();
            System.out.println(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return uri.toString();
    }

    public String generateOauthHeaderDownloadTT( String method, String token, String secret_token) {
        long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        String nonce = nonceGenerator();
        String pathTT = "/services/tt/student";

        ArrayList<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + consumerKey);
        parameters.add("oauth_nonce=" + nonce);
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_version=1.0");
        parameters.add("oauth_token=" + token);

        Collections.sort( parameters );
        StringBuffer parametersList = new StringBuffer();

        for ( int i = 0; i < parameters.size(); i++ ) {
            parametersList.append( ( ( i > 0 ) ? "&" : "" ) + parameters.get( i ) );
        }

        String signatureString = null;
        try {
            signatureString = method + "&" +
                    URLEncoder.encode( "https://" + host + pathTT, StandardCharsets.UTF_8.toString()) + "&" +
                    URLEncoder.encode( parametersList.toString(), StandardCharsets.UTF_8.toString() );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String signature = null;

        try
        {
            System.out.println(consumerSecret + "&" + secret_token);
            SecretKeySpec signingKey = new SecretKeySpec(
                    ( consumerSecret + "&" + secret_token).getBytes(), "HmacSHA1" );
            Mac mac = Mac.getInstance( "HmacSHA1" );
            mac.init( signingKey );
            System.out.println("SIGNATURE BASE: " + signatureString);
            byte[] rawHMAC = mac.doFinal( signatureString.getBytes() );
            signature = Base64.encodeBase64String(rawHMAC); //Base64.encodeBytes( rawHMAC );
        }
        catch ( Exception e )
        {
            System.err.println( "Unable to append signature" );
            System.exit( 0 );
        }

        System.out.println( signature );

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost(host);
        builder.setPath(pathTT);
        builder.addParameter("oauth_consumer_key", consumerKey);
        builder.addParameter("oauth_nonce", nonce);
        builder.addParameter("oauth_signature_method", "HMAC-SHA1");
        builder.addParameter("oauth_timestamp", Long.toString(timestamp));
        builder.addParameter("oauth_token", token);
        builder.addParameter("oauth_version", "1.0");
        builder.addParameter("oauth_signature", signature);

        URI uri = null;
        try {
            uri = builder.build().toURL().toURI();
            System.out.println(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return uri.toString();
    }

    private String nonceGenerator(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 22; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        return stringBuilder.toString();
    }
}