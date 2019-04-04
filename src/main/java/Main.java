import org.jsoup.*;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        Crawler MyCrawler = new Crawler(100);
        MyCrawler.doCrawl("https://sobakainfo.ru/porody/");
        System.out.println("Crawling was finished succesfully");
        MyCrawler.returnResults("index.txt");
        System.out.println("Results returned");
        MyCrawler.getPageRanksInFile(MyCrawler.getPRMap());

    }


}
