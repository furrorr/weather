import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    //создаем метод, который забирает всю страницу и возвращает какую то сущность, которая называется Document
    //Document - что то типа строки с html кодом страницы в иерархии
    private static Document getPage() throws IOException {       //исключения выбрасываются выше

        //создает строку url которая хранит адрес странцы
        String url = "https://pogoda.spb.ru/";

        //мы хотим получить документ page (метод parse парсит страницу и формирует объект документ)
        //перендаем в parse объект URL с адресом страницы (url) и пишем время ответа
        Document page = Jsoup.parse(new URL(url), 3000);

        //метод возвращает page, который сформировался
        return page;
    }
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString(String stringDate) throws Exception {

        Matcher mathcer = pattern.matcher(stringDate);
        if (mathcer.find()) {
            return mathcer.group();
        }
        throw new Exception("Can`t extract date from string");
    }

    private static int printFourValues(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn = values.get(3);
            boolean isMorning = valueLn.text().contains("Утро");
            if (isMorning) {
                iterationCount = 3;
            }
        }

            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index + i);

                for (Element td : valueLine.select("td")) {
                    //System.out.print(td.text() + "\t");
                    System.out.print(td.text() + "    ");
                }
                System.out.println();
            }
        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class = wt]").first();
        Elements names = tableWth.select("tr[class = wth]");
        Elements values = tableWth.select("tr[valign = top]");

        int index = 0;

        for (Element name : names) {
            String dateString = name.select("th[id = dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "\t\t\t Явления \t\t\t Температура \t\t\t Давление \t\t\t Влажность \t\t\t Ветер");
            int iterationCount = printFourValues(values, index);
            index = index + iterationCount;
        }


    }
}
