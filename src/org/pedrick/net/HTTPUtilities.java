package org.pedrick.net;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import sun.misc.BASE64Encoder;

/*=======================================================================

*FILE:         HTTPUtilities.java
*
*DESCRIPTION:  Class for make HTTP Requests
*REQUIREMENTS: 
*AUTHOR:       Patrick Facco
*VERSION:      1.0
*CREATED:      5-nov-2014
*LICENSE:      GNU/GPLv3
*COPYRIGTH:    Patrick Facco 2014
*This program is free software: you can redistribute it and/or modify
*it under the terms of the GNU General Public License as published by
*the Free Software Foundation, either version 3 of the License, or
*(at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program.  If not, see <http://www.gnu.org/licenses/>.
*========================================================================
* 
*/

public class HTTPUtilities
{
    /**
     * Performs an HTTP GET Request
     * @param url the target url with parameters e.i. http://mytarget.com?par1=x&par2=y
     * @param userAgent The user agent to use
     * @return String the server response as a String
     * @throws java.net.MalformedURLException if the url is malformed
     * @throws java.io.IOException if an error occurs on read/write to/from streams
     */
    public static String httpGet(String url, String userAgent) 
            throws MalformedURLException, IOException
    {
        String ris = "";
        URL urltoget = new URL(url);
        URLConnection urlConnection = urltoget.openConnection();
        urlConnection.setDoInput (true);
	urlConnection.setDoOutput (true);
	urlConnection.setUseCaches (false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("User-Agent", userAgent);
        urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        urlConnection.setRequestProperty("Referer", url);
        urlConnection.setRequestProperty("Keep-Alive", "115");
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                urlConnection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
           ris += inputLine;
        in.close();

        return ris;
    }

    /**
     * Performs an HTTP GET Request with basic authentication
     * @param url the target url with parameters e.i. http://mytarget.com?par1=x&par2=y
     * @param userAgent The user agent to use
     * @param username the username for authentication
     * @param password the password for authentication
     * @return String the server response as a String
     * @throws java.net.MalformedURLException if the url is malformed
     * @throws java.io.IOException if an error occurs on read/write to/from streams
     */
    public static String httpGetWithBasicAuthentication(String url, String userAgent,
            String username, String password) throws MalformedURLException, IOException
    {
        String ris = "";
        URL urltoget = new URL(url);
        URLConnection urlConnection = urltoget.openConnection();
        urlConnection.setDoInput (true);
	urlConnection.setDoOutput (true);
	urlConnection.setUseCaches (false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("User-Agent", userAgent);
        urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        urlConnection.setRequestProperty("Referer", url);
        urlConnection.setRequestProperty("Keep-Alive", "115");
        urlConnection.setRequestProperty("Authorization", "Basic " + encode(username+":"+password));
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                urlConnection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
           ris += inputLine;
        in.close();

        return ris;
    }

    /**
     * Utility method for String Base 64 encoding 
     * @param source the source String
     * @return String the string encoded
     */
    private static String encode(String source)
    {
        return new BASE64Encoder().encode(source.getBytes());
    }

    /**
     * Performs an HTTP POST Request
     * @param url the target url to post
     * @param dataToPost url params to include into request
     * @param userAgent The user agent to use
     * @return String the server response as a String
     * @throws java.net.MalformedURLException if the url is malformed
     * @throws java.io.IOException if an error occurs on read/write to/from streams
     */
    public static String httpPost(String url, String dataToPost, String userAgent)
            throws MalformedURLException, IOException
    {
        String ris = "";
        //String dtopost = URLEncoder.encode(dataToPost, "UTF-8");
        String dtopost = dataToPost;
        URL urltopost = new URL(url);
        URLConnection urlConnection  = urltopost.openConnection();
	urlConnection.setDoInput (true);
	urlConnection.setDoOutput (true);
	urlConnection.setUseCaches (false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("User-Agent", userAgent);
        urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        urlConnection.setRequestProperty("Referer", url);
        urlConnection.setRequestProperty("Keep-Alive", "115");
        urlConnection.setRequestProperty("Content-Length", dtopost.length() + "" );
        DataOutputStream out = new DataOutputStream (urlConnection.getOutputStream());


        out.writeBytes(dtopost);
        out.flush();
        out.close();
        BufferedReader bufline = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	String str;
	while (null != ((str = bufline.readLine())))
            ris += str;

	bufline.close ();
        return ris;
    }
    
    /**
     * Performs an HTTP POST Request With Basic Authentication
     * @param url the target url to post
     * @param dataToPost url params to include into request
     * @param userAgent The user agent to use
     * @param username
     * @param password
     * @return String the server response as a String
     * @throws java.net.MalformedURLException if the url is malformed
     * @throws java.io.IOException if an error occurs on read/write to/from streams
     */
    public static String httpPostWithBasicAuth(String url, String dataToPost, String userAgent, String username, String password)
            throws MalformedURLException, IOException
    {
        String ris = "";
        //String dtopost = URLEncoder.encode(dataToPost, "UTF-8");
        String dtopost = dataToPost;
        URL urltopost = new URL(url);
        URLConnection urlConnection  = urltopost.openConnection();
	urlConnection.setDoInput (true);
	urlConnection.setDoOutput (true);
	urlConnection.setUseCaches (false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("User-Agent", userAgent);
        urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        urlConnection.setRequestProperty("Referer", url);
        urlConnection.setRequestProperty("Keep-Alive", "115");
        urlConnection.setRequestProperty("Content-Length", dtopost.length() + "" );
        urlConnection.setRequestProperty("Authorization", "Basic " + encode(username+":"+password));
        DataOutputStream out = new DataOutputStream (urlConnection.getOutputStream());


        out.writeBytes(dtopost);
        out.flush();
        out.close();
        BufferedReader bufline = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	String str;
	while (null != ((str = bufline.readLine())))
            ris += str;

	bufline.close ();
        return ris;
    }
   
    /**
     * Performs an HTTP GET Request with the Firefox user agent
     * @param url the target url with parameters e.i. http://mytarget.com?par1=x&par2=y
     * @return String the server response as a String
     * @throws java.net.MalformedURLException if the url is malformed
     * @throws java.io.IOException if an error occurs on read/write to/from streams
     */
    public static String httpMozillaGet(String url) throws MalformedURLException, IOException
    {
        String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:33.0) Gecko/20100101 Firefox/33.0";
        return httpGet(url, userAgent);
    }

    /**
     * Performs an HTTP POST Request with Firefox useragent
     * @param url the target url to post
     * @param dataToPost url params to include into request
     * @return String the server response as a String
     * @throws java.net.MalformedURLException if the url is malformed
     * @throws java.io.IOException if an error occurs on read/write to/from streams
     */
    public static String httpMozillaPost(String url, String dataToPost) throws MalformedURLException, IOException
    {
        String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:33.0) Gecko/20100101 Firefox/33.0";
        return httpPost(url, dataToPost, userAgent);
    }

    /**
     * Performs an HTTP GET Request with basic authentication and Firefox user agent
     * @param url the target url with parameters e.i. http://mytarget.com?par1=x&par2=y
     * @param username the username for authentication
     * @param password the password for authentication
     * @return String the server response as a String
     * @throws java.net.MalformedURLException if the url is malformed
     * @throws java.io.IOException if an error occurs on read/write to/from streams
     */
    public static String httpGetMozillaWithBasicAuthentication(String url,
            String username, String password) throws MalformedURLException, IOException
    {
        String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:33.0) Gecko/20100101 Firefox/33.0";
        return httpGetWithBasicAuthentication(url, userAgent, username, password);
    }  
}
