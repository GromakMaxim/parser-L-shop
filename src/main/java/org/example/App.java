package org.example;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        //"Chrome/4.0.249.0 Safari/532.5"
        ParserHandler ph = new ParserHandler("Chrome/89.0.4389.86 Safari/537.36",
                "http://www.ya.ru");
        ph.init();
    }
}
