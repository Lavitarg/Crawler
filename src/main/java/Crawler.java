import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Crawler {

    public Crawler(int numberOfFiles) {
        this.numberofFiles = numberOfFiles;
    }

    //обьявляем переменную-счетчик строк, и итоговый лист ссылок
    static Integer counterOfStrings = new Integer(1);
    int numberofFiles;
    ArrayList<String> finalListofLinks = new ArrayList(numberofFiles);

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
}
