package edu.jonathan.lookforcardprices.searchengine.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UrlReaderService {

    public Document readUrlDocument(String url) {
        System.out.println(url);
        Document doc = null;
        try{
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                    .timeout(30000)
                    .method(Connection.Method.GET)
                    .validateTLSCertificates(false)
                    .execute()
                    .parse();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getCause() + "\nTry to connect by another way..");
            doc = parseDocument( readUrl(url, null) );
        }
        return doc;
    }

    private Document parseDocument(String html){
        Document document = null;
        try{
            document = Jsoup.parse(html);

        }catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        if( builder.toString().contains("") ){
            System.out.println("Possivelmente Ocorreu Erro ao read a pagina");
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
                System.out.println("ConnectException - Tentando novamente["+i+"]: "+url);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }catch( UnknownHostException e){
                System.out.println("UnknownHostException - Tentando novamente["+i+"]: "+url);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }

        }

        return in;
    }
}
