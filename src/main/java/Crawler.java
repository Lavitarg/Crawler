import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Crawler {

    public Crawler(int numberOfFiles) {
        this.numberofFiles = numberOfFiles;
    }

    //обьявляем переменную-счетчик строк, и итоговый лист ссылок
    static Integer counterOfStrings = new Integer(1);
    int numberofFiles;
    public ArrayList<String> finalListofLinks = new ArrayList(numberofFiles);

    public void doCrawl(String URL) throws IOException {

        //создаем новый файл, с именем, соответствующим номеру строки с этой ссылкой в итоговом документе
        File outputFile = new File("C:/Study/Idea_Projects/Crawler/src/main/resources/OutputFiles", counterOfStrings.toString() + ".txt");
        outputFile.createNewFile();
        FileWriter writer = new FileWriter(outputFile);
        // получаем штмлку, и вытаскиваем из нее текст бади
        Document currentPage = Jsoup.connect(URL).get();
        writer.write(currentPage.body().text());
        writer.flush();
        writer.close();
        // получаем коллекцию всех ссылок с этой страницы
        Elements links = currentPage.select("a");
        for(Element link:links){
            if(!finalListofLinks.contains(link.attr("href")) && counterOfStrings<=100 && !link.attr("href").contains("#")&& !link.attr("href").contains(".jpg")){
                finalListofLinks.add(link.attr("href"));
                counterOfStrings++;
                doCrawl(link.attr("href"));
            }
        }

    }
    public void returnResults(String nameOfFile) throws IOException {
        File outputFile = new File("C:/Study/Idea_Projects/Crawler/src/main/resources/OutputFiles", nameOfFile);
        outputFile.createNewFile();
        FileWriter writer = new FileWriter(outputFile);
        for (String link: finalListofLinks) {
            System.out.println(link);
            writer.write(link+"\n");
        }
        writer.flush();
        writer.close();
    }

    public HashMap<String,Double> getPRMap()  throws IOException {
        ArrayList<String> pages = finalListofLinks;
        HashMap<String,ArrayList<String>> linksToOtherP= new HashMap<String, ArrayList<String>>();

        for(String page:pages){

            ArrayList<String> outerLinks = new ArrayList<String>();
            //получаем страницу
            Document currentPage= Jsoup.connect(page).get();
            //вытаскиваем все ссылки
            Elements links = currentPage.select("a");
            for(Element link:links){
                if(!outerLinks.contains(link.attr("href"))&&!link.attr("href").contains("#")&& !link.attr("href").contains(".jpg")){
                    outerLinks.add(link.attr("href"));
                }
            }

            linksToOtherP.put(page,outerLinks);
        }
        System.out.println("Links prepared");

        return calculatePageRanks(linksToOtherP);

    }

    private HashMap calculatePageRanks (HashMap<String,ArrayList<String>> linksMap){
        //creating of outputMap
        HashMap<String,Double> resultMap = new HashMap<String, Double>();
        //setting initial values
        for(String link:linksMap.keySet()){
            resultMap.put(link,0.15);
        }
        for(int i=0;i<1000;i++) {
            //идем по всем ключам итоговой мапы
            for (String pageURL : resultMap.keySet()) {
                //для каждого идем по ключам мапы ссылок, если лист ссылок содержит адрес текущей страницы, добавляем значение ее пр в формулу
                for (String link2 : linksMap.keySet()) {
                    if (link2 != pageURL) {
                        if (linksMap.get(link2).contains(pageURL)) {
                            resultMap.put(pageURL, resultMap.get(pageURL) + (0.85 + (resultMap.get(link2) / linksMap.get(link2).size())));
                        }
                    }
                }
            }

        }
        System.out.println("Page ranks calculated");
        return resultMap;
    }

    public void getPageRanksInFile (HashMap<String, Double> map) throws IOException{
        File outputFile = new File("C:/Study/Idea_Projects/Crawler/src/main/resources/OutputFiles","PR.txt");
        FileWriter writer = new FileWriter(outputFile);
        for(String page:map.keySet()){
            writer.write(page+map.get(page).toString()+"          "+"\n");
        }
        writer.flush();
        writer.close();
        System.out.println("Page ranks outputted.");
    }
}
