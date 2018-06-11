package edu.jonathan.lookforcardprices.searchengine.service;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UrlReaderService {

    protected static final Logger logger = Logger.getLogger(UrlReaderService.class);

    public Document readUrlDocument(String url) {
        logger.debug("Connecting to: "+url);
        Document doc = null;
        try{
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                    .timeout(90000)
                    .method(Connection.Method.GET)
                    .validateTLSCertificates(false)
                    .execute()
                    .parse();
        }catch(Exception e){
            logger.error("Connecting in url: "+url);
            logger.error(e);
            logger.trace(e.getCause() + "\nTry to connect by another way..");
            doc = parseDocument( readUrl(url, null) );
        }
        return doc;
    }

    private Document parseDocument(String html){
        Document document = null;
        try{
            document = Jsoup.parse(html);

        }catch (Exception e) {
            logger.error(e);
        }
        return document;
    }

    private String readUrl(String url, String charset){
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader in = getPageBuffer(url, charset);

            if( in != null){
                String inputLine;
                while ((inputLine = in.readLine()) != null){
                    builder.append(inputLine);
                }
                in.close();
            }
        }catch (MalformedURLException e) {
            logger.error(e);
        }catch (IOException e) {
            logger.error(e);
        }

        if( builder.toString().contains("") ){
            logger.trace("Error Reading the page");
        }

        return builder.toString();
    }

    private BufferedReader getPageBuffer(String url, String charset)throws IOException {
        URL oracle = null;
        URLConnection yc = null;
        BufferedReader in = null;

        for( int i = 0; i < 10; i++ ){
            try{
                oracle = new URL( url );
                yc = oracle.openConnection();
                if( charset == null || charset.isEmpty() ){
                    in = new BufferedReader(new InputStreamReader( yc.getInputStream() ));
                }else{
                    in = new BufferedReader(new InputStreamReader( yc.getInputStream(), charset ));
                }

                break;
            }catch( ConnectException e){
                logger.trace("ConnectException - Try number["+i+"]: "+url);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    logger.error(e2);
                }
            }catch( UnknownHostException e){
                logger.trace("UnknownHostException - Try number["+i+"]: "+url);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    logger.error(e2);
                }
            }

        }

        return in;
    }
}
