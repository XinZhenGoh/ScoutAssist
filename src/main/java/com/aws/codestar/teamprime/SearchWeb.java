package com.aws.codestar.teamprime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.ThreadLocalRandom;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import org.apache.http.HttpHost;
public class SearchWeb {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String MESSAGE_FORMAT = "Hello %s!";
    //We need a real browser user agent or Google will block our request with a 403 - Forbidden
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    private static final String SEARCH_URL = "https://google.com/search?";

//    @RequestMapping(path = "/urls/{website}/{player}/{num}/{type}/", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity searchGetURLS(@PathVariable String website, @PathVariable String player, @PathVariable int num, @PathVariable String type) {
//        return ResponseEntity.ok(createResponseUrls(website, player, num, type));
//    }

    //Below we can specify any player(s) and website
//    @RequestMapping(path = "/scrape/{web}/{players}/{num}/{type}/{ret}", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity searchMultiplePlayers(@PathVariable String web, @PathVariable String players, @PathVariable int num, @PathVariable String type, @PathVariable String ret) {
//        return ResponseEntity.ok(createGeneralScrape(players, web, num, type, ret));
//    }

    public static String createResponseUrls(String website, String player, int numberQueries, String type, int max_time) {
        String retString = "";
        List<String> urls;
        try {
            urls = SearchWeb.SearchLinks(website, player, numberQueries, type, max_time);

        } catch (Exception e) {
            retString = "Error getting links\n caused by SearchWeb.searchlinks";
            System.out.println("SearchWeb.searchLinks ERROR");
            e.printStackTrace();
            return retString;
        }
        for (int i = 0; i < urls.size(); i++) {
            retString += urls.get(i) + "  \n";
        }
        return retString;
    }

    public static String createGeneralScrape(String players, List<String> domainList, int numberQueries, String type, int max_time, boolean INC_all,  boolean INC_authors, boolean INC_titles, boolean INC_bodies, boolean INC_dates, boolean INC_urls, boolean INC_snippet) {
        //split the players string into a list of names, assuming seperated by “$”
        if (players.matches(".*[^a-zA-Z0-9 .$-].*"))
            return "Error bad request: player name(s) contains a non alphanumerical character other than space, period, dash or dollar sign\nTry again.";
        String playerList[] = players.split("\\$", 0);

        for(int i = 0; i < playerList.length; i++) {
            try {
                int num = Integer.parseInt(playerList[i]);
                // get searchName
                playerList[i] = SqlDao.GetName(num);
            } catch (NumberFormatException e) {
                playerList[i] = playerList[i];
            }
        }

        boolean urlError = false;
        boolean scrapError = false;
        boolean jsonMapError = false;

        System.out.println("Length of playerlist = " + Integer.toString(playerList.length));
        List<SearchResult> retList;
        String retString = " ";
        String jsonString;
        String webName = "test";

        ArrayList<List<SearchResult>> finalList = new ArrayList();
        for (int i = 0; i < playerList.length; i++) {
            List<SearchResult> listPerPlayer=new ArrayList();
            for (int domain = 0; domain < domainList.size(); domain++) {
                webName = domainList.get(domain);

                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
                } catch (Exception e) {
                    System.out.println("time error!");
                }
                System.out.println("current player = " + playerList[i]);
                List<String> urls = new ArrayList<>();
                try {
                    //webName in the form “name.com”
                    urls = SearchWeb.SearchLinks(webName, playerList[i], numberQueries, type, max_time);

                } catch (Exception e) {
                    jsonString = "ERROR 100: Could not retrieve Sports Illustrated links";
                    System.out.println("SEARCHER ERROR");
                    e.printStackTrace();
                    urlError = true;
                }

                try {
                    //numberQueries
                    switch (webName) {

                        case "si.com":// edit below
                            retList = ScraperEngine.ScraperSportsIllustrated(urls, playerList[i], retString);
                            listPerPlayer.addAll(retList);
                            //OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), finalList);
                            break;
                        case "foxsports.com":
                            retList = ScraperEngine.ScraperFox(urls, playerList[i], retString);
                            listPerPlayer.addAll(retList);
                            //OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), finalList);
                            break;

                        case "cbssports.com":
                            retList = ScraperEngine.ScraperCBS(urls, playerList[i], retString);
                            listPerPlayer.addAll(retList);
                            //OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), finalList);
                            break;

                        case "espn.com":
                            retList = ScraperEngine.ScraperESPN(urls, playerList[i]);
                            listPerPlayer.addAll(retList);
                            //OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), finalList);
                            break;

                        case "sports.yahoo.com":
                            retList = ScraperEngine.ScraperYahoo(urls, playerList[i], retString);
                            listPerPlayer.addAll(retList);
                            //OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), finalList);
                            break;

                        case "nytimes.com":
                            retList = ScraperEngine.ScraperNY(urls, playerList[i], retString);
                            listPerPlayer.addAll(retList);
                            //OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), finalList);
                            break;

                        case "bleacherreport.com":
                            retList = ScraperEngine.ScraperBleacherReport(urls, playerList[i], retString);
                            listPerPlayer.addAll(retList);
                            //OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), finalList);
                            break;
                    }
                } catch (Exception e) {

                    System.out.println("SCRAPER ERROR");
                    e.printStackTrace();
                    scrapError = true;
                }
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
                } catch (Exception e) {
                    System.out.println("time error!");
                }
            }
            finalList.add(listPerPlayer);

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
            } catch (Exception e) {
                System.out.println("time error!");
            }
        }

        jsonString = "";
        System.out.println("finalLIst size is: "+Integer.toString(finalList.size()));
        //&& !ret.contains("t") && !ret.contains("u") && !ret.contains("p") && !ret.contains("b")
        if (INC_all) {
            String temp = "";

            ObjectMapper mapper = new ObjectMapper();
            Boolean error = false;
            for (int playerVal = 0; playerVal < finalList.size(); playerVal++) {
                System.out.println("finalLIst.get(index) size is: "+Integer.toString(finalList.get(playerVal).size()));
                try {
                    if (playerVal==0){
                        jsonString="{\""+playerList[playerVal]+"\": [";
                    }
                    else{
                        jsonString+=",\n\""+playerList[playerVal]+"\": [";
                    }
                    temp = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(finalList.get(playerVal));
                    System.out.println("works: " + playerVal + "  " + temp);
                } catch (Exception e) {
                    error = true;
                    System.out.println("Error Mapping search result value to json");
                    System.out.println("finallist at playerVal  "+finalList.get(playerVal).toString() +"size of list at playerval "+Integer.toString(finalList.get(playerVal).size()));
                    e.printStackTrace();
                }
                if (!error) {
                    jsonString += temp.replaceAll("\\[", "").replaceAll("\\]", "");
                    jsonString += "]";
                }

            }
            jsonString += "\n}";

        } else {


            //make list of hashtables
            ObjectMapper mapper = new ObjectMapper();
            String temp = "";
            ArrayList<Hashtable<String, String>> hashList = new ArrayList<Hashtable<String, String>>();
            for (int player = 0; player < finalList.size(); player++) {
                //then go through for each
                if (player==0){
                    jsonString="{\""+playerList[player]+"\": [";
                }
                else{
                    jsonString+=",\n\""+playerList[player]+"\": [";
                }
                for (int eachArticle = 0; eachArticle < finalList.get(player).size(); eachArticle++) {
                    //System.out.println("ret is"+ret);
                    Hashtable<String, String> articleTable = new Hashtable();


                    if (INC_urls) {
                        articleTable.put("url", ((finalList.get(player)).get(eachArticle)).getUrl());
                    }
                    if (INC_dates) {
                        articleTable.put("date", ((finalList.get(player)).get(eachArticle)).getDate());
                    }
                    if (INC_titles) {
                        articleTable.put("title", ((finalList.get(player)).get(eachArticle)).getTitle());
                    }
                    if (INC_authors) {
                        articleTable.put("author", ((finalList.get(player)).get(eachArticle)).getAuthor());
                    }
                    if (INC_snippet) {
                        articleTable.put("snippet", ((finalList.get(player)).get(eachArticle)).getSnippet());
                    }
                    if (INC_bodies) {
                        articleTable.put("body", ((finalList.get(player)).get(eachArticle)).getBody());
                    }

                    hashList.add(articleTable);
                    try {
                        //changed it from += to equals
                        temp = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(hashList.get(eachArticle));
                        String addval="";
                        if (eachArticle!=0)
                            addval=",";
                        jsonString = jsonString  +addval+ temp.replaceAll("\\[", "").replaceAll("\\]", "");
                    } catch (Exception e) {
                        System.out.println("MAPPING ERROR");
                        jsonMapError = true;
                        e.printStackTrace();
                    }

                }
                jsonString += "]";
            }
            jsonString += "\n}";
        }
        if (jsonString.equals("[ ]")) {
            jsonString = "ERROR: ";
            if (jsonMapError) {
                jsonString = "mapping to Json Format";
            }
            if (urlError) {
                jsonString += "\nError: getting urls";
            }
            if (scrapError) {
                jsonString = "\nError: scraping article(s)";
            }
        }
        return jsonString;
    }


    public static Document getDoc(String webName, String type, int pgNum) throws Exception {
        Document doc;
        if (type != "") {
            doc = Jsoup.connect(SEARCH_URL)
                    .userAgent(USER_AGENT)
                    .data("q", webName)
                    .data("tbm", "nws")
                    .data("num", "100")
                    .data(type, "hdtb-tls")
                    .data("tbs", "sbd:1")
                    .data("start", "" + pgNum)
                    .method(Connection.Method.GET)
                    .referrer("https://www.google.com/").get();
        } else {
            doc = Jsoup.connect(SEARCH_URL)
                    .userAgent(USER_AGENT)
                    .data("num", "100")
                    .data("q", webName)
                    .data("tbm", "nws")
                    .data("start", "" + pgNum)
                    .method(Connection.Method.GET)
                    .referrer("https://www.google.com/").get();
        }
        return doc;
    }


    /*Function Searchlinks: takes the website namSearchLinkse, the player name, number of urls to retrieve and the type of search method (by relevent, recent and so on)
     *If want to search on any website simple "" (an empty string must be passed
     */
    public static List<String> SearchLinks(String domain, String searchName, int queryNum, String type, int time) throws Exception {

        try{
            int num = Integer.parseInt(searchName);
            // get searchName
            searchName = SqlDao.GetName(num);
        } catch (NumberFormatException e) {
            searchName = searchName;
        }

        List<String> urls = new ArrayList<>();
        Document doc;
        int searchNum = queryNum;
        int pageCount = 0;
        List<String> holder = new ArrayList<>();
        boolean done = false;
        boolean repeatState = false;
        int repeatCounter = 0;

        //ALL BELOW should be passes as parameters "args" to the code
        long currTime = System.currentTimeMillis();
        long startTime = currTime;

        //**TIMER function add here to prevent infinite loop****//
        while (!done) {
            String webName = searchName + " " + domain;

            if (repeatState) {
                repeatCounter++;
                urls = new ArrayList<>();
            }
            if (searchNum <= 100) {// searchNUm>=10
                pageCount = 1 + repeatCounter;
            } else if (searchNum % 100 != 0) {
                pageCount = searchNum / 100 + 1 + repeatCounter;// change all queryNum to searchNum
            } else {
                pageCount = searchNum / 100 + repeatCounter;
            }
            int j = 0;
            if (repeatState) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
            }
            for (int i = 0; i < pageCount; i++) {

                switch (type) {
                    case ("rec"):// accepts type=rec for recent
                        doc = getDoc(webName, "hdtb-t1", i);
                        break;
                    case ("rec-year"):
                        doc = getDoc(webName, "hdtb-t1 hdtb-t1-sel", i);
                        break;
                    default:// accepting rel for relevant articls
                        doc = getDoc(webName, "", i);
                }

                for (Element result : doc.select("h3.r a")) {

                    final String url = result.attr("href");
                    if (!holder.contains(url) && (url.toLowerCase()).contains(domain.toLowerCase()) && ! ((url.toLowerCase()).contains("video"))) {
                        urls.add(url);
                        //Delete the following for testing:
                        System.out.println(j);
                        j++;
                    }
                    System.out.println(url);
                    if (j > searchNum - 1) {
                        break;
                    }
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
                }
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
                if (((System.currentTimeMillis() - startTime) / 60000) > time) {
                    done = true;
                    System.out.println("\nnumbers of URLS retrieved = " + Integer.toString(urls.size()) + "\n");
                    for (i = 0; i < urls.size(); i++) {
                        String currentURL = urls.get(i).toLowerCase();
                        System.out.println("URL " + Integer.toString(i) + " = " + currentURL);
                        if ((currentURL.contains(domain.toLowerCase())) && !(currentURL.contains("video"))) {
                            holder.add(currentURL);
                        } else {
                            System.out.println("URLS failed to append - SearchLinks");
                        }
                    }
                    break;
                }
            }

            int j2 = 0;
            System.out.println("\nnumbers of URLS retrieved = " + Integer.toString(urls.size()) + "\n");
            for (int i = 0; i < urls.size(); i++) {
                String currentURL = urls.get(i).toLowerCase();
                System.out.println("URL " + Integer.toString(i) + " = " + currentURL);
                if (currentURL.contains(domain.toLowerCase()) &&!(currentURL.contains("video"))) {
                    holder.add(currentURL);
                } else {
                    System.out.println("URLS failed to append - SearchLinks");
                }
                j2++;
            }
            if (holder.size() < queryNum) {
                done = false;
                searchNum = queryNum - holder.size();
                repeatState = true;
                repeatCounter++;
            } else {
                done = true;
            }

            if (((System.currentTimeMillis() - startTime) / 60000) > time) {
                done = true;
            }
        }
        return holder;
    }
}














