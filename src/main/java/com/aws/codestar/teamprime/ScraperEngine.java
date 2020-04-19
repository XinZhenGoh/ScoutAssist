package com.aws.codestar.teamprime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.BreakIterator;


public class ScraperEngine {
    //This is to disguise as browsers
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static List<SearchResult> ScraperESPN(List<String> urlList, String player) throws Exception {
        Document page;
        Document googleNewsPage;
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        String title,url,date,body,snippet, snippetHolder = "";
        String author = "";
        int articleCount=urlList.size();
        for (int i = 0; i < articleCount; i++) {///do stuff for each url
            page = Jsoup.connect(urlList.get(i)).userAgent(USER_AGENT).get();


            page.select("div.author span").remove();
            page.select("picture").remove();
            Element link = page.select("div.article-body").first();
            title = (page.select("header[class=article-header]")).text();
            url = urlList.get(i);
            author = (page.select("div.author")).text();
            date = (page.select("span[data-dateformat]")).text();
            try {
                body = (link.select("p")).text();
            }
            catch(Exception BodyError) {
                System.out.println("error getting body");
                body = "";
            }

            try
            {
                author = (page.select("div.author span")).text();
                author = author.replaceAll("ESPN Insider", "");
                author = author.replaceAll("ESPN Senior Writer", "");
                author = author.replaceAll("ESPN Staff Writer", "");


            }
            catch(Exception AuthorError) {
                System.out.println("error getting author");
            }
            try
            {
                author += (page.select("div.author.has-bio")).text();
                author = author.replaceAll("ESPN Insider", "");
                author = author.replaceAll("ESPN Senior Writer", "");
                author = author.replaceAll("ESPN Staff Writer", "");

            }
            catch(Exception AuthorError) {
                System.out.println("error getting author");

            }

            try
            {
                author = (page.select("div.author")).text();
                author = author.replaceAll("ESPN Insider", "");
                author = author.replaceAll("ESPN Senior Writer", "");
                author = author.replaceAll("ESPN Staff Writer", "");


            }
            catch(Exception AuthorError) {
                System.out.println("error getting author");
            }
            try {
                snippetHolder = (page.select("p")).text();
                snippet = grabSnippet(snippetHolder);
            }

            catch(Exception Snippeterror) {
                System.out.println("error getting snippet");
                snippet = "";
            }
            String bodTemp;

            String retString =  "Title: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body +" \n\n\n";
            System.out.println("\nPlayer: " + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body);
            System.out.println(" ");
            resultList.add(new SearchResult(title, url,author,date,body, snippet));
        }
        return resultList;
    }

    public static List<SearchResult> ScraperYahoo(List<String> urlList, String player, String retString) throws Exception {
        Document page;
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        String title,url,date,body,author ="";
        String snippet, snippetHolder = "";
        int articleCount = urlList.size();
        for (int i = 0; i < articleCount; i++) {///do stuff for each url
            try {
                page = Jsoup.connect(urlList.get(i)).userAgent(USER_AGENT).get();
                title = (page.select("h1")).text();
                url = urlList.get(i);
                date = (page.select("time")).text();
                if (date.length() == 0) {
                    date = (page.select("div time")).text();
                }
                body = (page.select("p")).text();
                try {
                    author = (page.select("div.author-name")).text();
                } catch (Exception e)
                {
                    System.out.println("Dont worry about this error");
                }
                try {
                    author += (page.select("div.author a")).text();
                } catch (Exception e) {
                    System.out.println("Dont worry about this error");
                }
                try {
                    author += (page.select("div.author div")).text();
                } catch (Exception e) {
                    System.out.println("Dont worry about this error");
                }
                try {
                    author += (page.select("span.provider-link a")).text();
                } catch (Exception e) {
                    System.out.println("Dont worry about this error");
                }
                try
                {
                    snippetHolder = (page.select("p")).text();
                    snippet = grabSnippet(snippetHolder);
                }
                catch(Exception CaptainError) {
                    System.out.println("error getting snippet");
                    snippet = "";
                }
                String bodTemp;

                retString +=  "Player: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body +" \n\n\n";
                System.out.println("\nPlayer: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body);
                System.out.println(" ");
                resultList.add(new SearchResult(title, url,author,date,body,snippet));
            } catch (Exception e) {
                System.out.println("Scraper Yahoo Error - Scraper General");
            }
        }
        return resultList;
    }

    public static List<SearchResult> ScraperNY(List<String> urlList, String player, String retString) throws Exception {
        Document page;
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        String title="",url="",date="",body="",author = "", snippet="";
        int articleCount=urlList.size();
        for (int i = 0; i < articleCount; i++) {///do stuff for each url
            page = Jsoup.connect(urlList.get(i)).userAgent(USER_AGENT).get();

            try {
                page.select("picture").remove();

                page.select("p").first().remove();
                page.select("p").first().remove();
                title = (page.select("h1 span")).text();
                if (title.length() == 0) {
                    title = (page.select("h1")).text();
                }
            }catch (Exception removeAndTitleError)
            {
                System.out.println("error getting title");
            }
            try {


                url = urlList.get(i);
                author = (page.select("span.css-1baulvz")).text();
            }catch (Exception authorError)
            {
                System.out.println("error getting author");
            }
            try {


                date = (page.select("li time")).text();
                body = (page.select("p")).text();
                snippet=grabSnippet(body);
            }
            catch(Exception BodyError){
                System.out.println("error getting body and date");
            }

            retString +=  "Player: "+ player+ "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body +"\nSnippet: " + snippet+" \n\n\n";
            System.out.println("\nPlayer: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body+"\nSnippet: " + snippet);
            System.out.println(" ");
            resultList.add(new SearchResult(title, url,author,date,body,snippet ));
        }
        return resultList;
    }


    public static List<SearchResult> ScraperBleacherReport(List<String> urlList, String player, String retString) throws Exception {
        Document page;
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        String title="" +
                "",url="",date="",body="",author = "";
        String snippet, snippetHolder = "";
        System.out.println(" ");
        int articleCount=urlList.size();
        for (int i = 0; i < articleCount; i++) {///do stuff for each url
            page = Jsoup.connect(urlList.get(i)).userAgent(USER_AGENT).get();
            page.select("div.atom.twitterEmbed").remove();
            try {
                title = (page.select("div#main-content h1")).first().text();
                url = urlList.get(i);
                author = (page.select("span.atom.authorInfo span.name")).first().text();
                date = (page.select("span.date")).text();
                body = (page.select("div#main-content p")).text();
            }
            catch (Exception e)
            {
                System.out.println("Error getting some article data");
            }
            try
            {
                snippetHolder = (page.select("div#main-content p")).text();
                snippet = grabSnippet(snippetHolder);
            }
            catch(Exception CaptainError) {
                System.out.println("error getting snippet");
                snippet = "";
            }


            retString +=  "Player: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body +" \n\n\n";
            System.out.println("Player: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body);
            System.out.println(" ");
            resultList.add(new SearchResult(title, url,author,date,body,snippet));
        }
        return resultList;
    }

    public static List<SearchResult> ScraperSportsIllustrated(List<String> urlList,String player, String retString) throws Exception {
        Document page;
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        String title="",url="",date="",body="",author = "", snippet="";
        System.out.println(" ");
        int articleCount=urlList.size();
        for (int i = 0; i < articleCount; i++) {///do stuff for each url
//currently expecting name to google... needs to change to connect to url
            page = Jsoup.connect(urlList.get(i)).userAgent(USER_AGENT).get();
            page.select("div.atom.twitterEmbed").remove();
            try {
                title = (page.select("h1")).text();
            }
            catch (Exception e){
                System.out.println("error grabbing title");
            }
            url = urlList.get(i);
            try {
                author = (page.select("a.bold.author-name")).first().text();
            }catch (Exception e){
                System.out.println("error grabbing author");
            }
            try {
                date = (page.select("div.published-date")).text();
            }catch (Exception e){
                System.out.println("error grabbing date");
            }

            try {
                body = (page.select("div#article-body")).text();
                snippet=grabSnippet(body);
            }
            catch (Exception e)
            {
                System.out.println("error grabbing body or snippet");
            }

            retString +=  "Player: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body +" \n\n\n";
            System.out.println("Player: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body);
            System.out.println(" ");
            resultList.add(new SearchResult(title, url,author,date,body,snippet));
        }
        return resultList;
    }

    //method to split names into first and last
//    public  static String[] playerFistLastName(String name)
//    {
//        String[] playerFinal= new String[2];
//        if(name.contains(" ")) {
//            playerFinal = name.split(" ");
//        }
//        else if ((name.split("(?=\\p{Upper})").length>1) )
//        {
//            playerFinal = name.split("(?=\\p{Upper})");
//        }
//        else
//        {
//            playerFinal[0]= name;
//            playerFinal[1] = "";
//        }
//        return playerFinal;
//    }


    public static List<SearchResult> ScraperFox(List<String> urlList, String player, String retString) throws Exception {
        Document page;
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        String title,url,date,body,author = "";
        String snippet = "";
        int articleCount = urlList.size();
        for (int i = 0; i < articleCount; i++) {
            page = Jsoup.connect(urlList.get(i)).userAgent(USER_AGENT).get();
            page.select("picture").remove();
            Element link = page.select("div.article-body").first();
            title = (page.select("h1")).text();
            url = urlList.get(i);
            boolean error = false;
            try {
                author = (page.select("ul span.fiso-article-byline__list-item-author")).text();
                String temp2 = author.replaceAll("\\s", "");
                if (author.isEmpty() || temp2.length() == 0) {
                    error = true;
                }
            } catch (Exception e2) {
                author = "";
                System.out.println("couldn't find author");
                error = true;
            }

            date = ((page.select("time")).text()).replaceAll("\\sat.*|\\s[0-9][0-9]:[0-9][0-9].*|\\s[0-9]:[0-9][0-9].*","");
            page.select("b").remove();
            body = ((page.select("p")).text()).replaceAll("\\sHelp | Press.*", "");
            Character temp=body.charAt(body.length()-1);
            if (temp.equals('|'))
                body=body.substring(0, body.length()-1);
            try
            {
                snippet = grabSnippet(body);
            }
            catch(Exception CaptainError) {
                System.out.println("error getting snippet");
                snippet = "";
            }


            retString +=  "Player: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body +" \n\n\n";
            System.out.println("\nPlayer: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body);
            System.out.println(" ");
            resultList.add(new SearchResult( title, url, author, date, body,snippet));
        }
        return resultList;
    }

    public static List<SearchResult>ScraperCBS(List<String> urlList, String player, String retString) throws Exception {
        Document page;
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        String title,url,date,body,author,snippet = "";

        int articleCount = urlList.size();

        for (int i = 0; i < articleCount; i++) {///do stuff for each url
            page = Jsoup.connect(urlList.get(i)).userAgent(USER_AGENT).get();
            try {
                page.select("div.author-bio-box").remove();
                page.select("div.author span").remove();
            } catch (Exception e) {
                e.printStackTrace();
            }
            page.select("picture").remove();
            Element link = page.select("div.article-body").first();
            title = (page.select("h1")).text();
            url = urlList.get(i);
            boolean error = false;
            author = "";
            try {
                System.out.println("In try");
                author = (page.select("div.ul span.fiso-article-byline__list-item-author")).text();
                String temp2 = author.replaceAll("\\s", "");
                error = false;
                System.out.println("***" + author + "***+" + temp2 + "++");
                if (author.isEmpty() || temp2.length() == 0) {
                    error = true;
                }
            } catch (Exception e1) {
                System.out.println("Couldnt use method 1");
                error = true;
            }
            if (error) {
                try
                {
                    author = (page.select("div.ul.article.author.row span")).text();
                    String temp3 = author.replaceAll("\\s", "");
                    error = false;
                    System.out.println("***" + author + "***+" + temp3 + "++");
                    if (author.isEmpty() || temp3.length() == 0) {
                        error = true;
                    }
                } catch (Exception e1) {

                    System.out.println("Couldnt use method 2");
                    error = true;
                }
            }
            if (error) {
                try {
                    author = page.select("div.article-head").select("ul.article-author").select("a").text();
                    String temp3 = author.replaceAll("\\s", "");
                    error = false;
                    if (author.isEmpty() || temp3.length() == 0) {
                        error = true;
                    } else {
                        Pattern ptn = Pattern.compile("@.*");
                        Matcher mtch = ptn.matcher(author);

                        author = (mtch.replaceAll("")).trim();
                    }
                }
                catch (Exception e3) {
                    author = "";
                    System.out.println("Couldnt use method 2");
                    error = true;
                }
            }
            if (error) {
                author = "";
            }
            date =  page.select("div.article-head").select("ul.article-author").select("li.date-published time").text();
            try{
                page.select("figure").remove();
            }catch(Exception e){
                System.out.println("Threw error on figure");
            }
            body = ((page.select("p")).text()).replaceAll("\\sI agree that CBS Sports can send me the.*","");
            try
            {
                snippet = grabSnippet(body);
            }
            catch(Exception CaptainError) {
                System.out.println("error getting snippet");
                snippet = "";
            }
            String bodTemp;

            retString +=  "Player: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body +" \n\n\n";
            System.out.println("\nPlayer: "+ player + "\nTitle: " + title + "      \nURL: " + url + "\nAuthor: " + author + "\nDate: " + date +"\nBody: " + body);
            System.out.println(" ");
            resultList.add(new SearchResult(title, url, author, date, body,snippet));
        }
        return resultList;
    }


    public static String grabSnippet(String snippetHolder)
    {
        int f = 0;
        int count = 0;
        String newSnippet = "";

        for (int k = 0; k < snippetHolder.length() && count < 5; k++) {
            char c = snippetHolder.charAt(k);
            if (c == '.' || c == '?' || c == '!') {
                newSnippet = newSnippet + snippetHolder.substring(f, k + 1);
                f = k + 1;
                count++;
            }
        }
        return newSnippet;
    }


}