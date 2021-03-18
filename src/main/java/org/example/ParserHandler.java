package org.example;

import org.apache.log4j.Logger;
import org.example.SettingsReaders.EndpointsFileReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserHandler {
    private static final Logger log = Logger.getLogger(ParserHandler.class);
    private String userAgent;//является идентификатором, который сообщается посещаемому сайту. На многих сайтах он является важнейшим критерием для антиспам фильтра.
    private String referrer;//содержит URL источника запроса.
    private int PAGE_LIMITER = 5000;

    public ParserHandler(String userAgent, String referrer) throws IOException {
        this.userAgent = userAgent;
        this.referrer = referrer;
    }

    public void init() throws IOException {
        EndpointsFileReader endpointsFileReader = new EndpointsFileReader();
        List<String> endpoints = endpointsFileReader.getEndpoints();
        String url = endpointsFileReader.getURL();

        for (String endpoint : endpoints) {
            log.info("trying to connect with: " + endpoint);
            System.out.println("trying to connect with: " + endpoint);
            for (int i = 1; i < PAGE_LIMITER + 1; i++) {
                if (i == 1) {
                    String link = url + "/" + endpoint;
                    parse(link);
                } else {
                    String link = endpointsFileReader.getURL() + "/" + endpoint + "?page=" + i;
                    if (tryToConnect(link)) {
                        parse(link);
                    } else{
                        break;
                    }
                }
            }
        }
    }

    private void parse(String link) throws IOException {
        System.out.println("parsing... " + link);
        Document docHTML = Jsoup.connect(link)
                .userAgent(userAgent)
                .referrer(referrer)
                .get();

        Elements cards = docHTML.getElementsByClass("product-preview-card");
        for (Element card :cards){
            String resultLine = card.addClass("product-preview-card__text").tagName("a").text()
                    .replace("Быстрый просмотр ","")
                    .replace(" Доставка Самовывоз ", "---")
                    .replace(" В корзину","");

            System.out.println(resultLine);
            log.info(resultLine);
        }
    }

    private boolean tryToConnect(String link) throws IOException {
        Document docHTML = Jsoup.connect(link)
                .userAgent(userAgent)
                .referrer(referrer)
                .get();
        if (docHTML.getElementsByClass("product-preview-card")
                .addClass("product-preview-card__text")
                .tagName("a").text().isEmpty()) {
            return false;
        }
        return true;
    }


}
