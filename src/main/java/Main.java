import org.jsoup.*;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        Crawler MyCrawler = new Crawler(100);
        MyCrawler.doCrawl("https://sobakainfo.ru/porody/");
        MyCrawler.returnResults("index.txt");

    }


}
