package com.aws.codestar.teamprime.controller;

import com.aws.codestar.teamprime.SearchWeb;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic Spring web service controller that handles all GET requests.
 */
@RestController
@RequestMapping("/")
public class Controllers {

    private static final String MESSAGE_FORMAT = "Hello %s!";

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity helloWorldGet(@RequestParam(value = "name", defaultValue = "Team Prime!!!") String name) {
        return ResponseEntity.ok(createResponse(name));
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity helloWorldPost(@RequestParam(value = "name", defaultValue = "TEAM PRIME!") String name) {
        return ResponseEntity.ok(createResponse(name));

    }

    private String createResponse(String name) {
        return new JSONObject().put("Output", String.format(MESSAGE_FORMAT, name)).toString();
    }

    //Below we can specify any player(s) and website
    /*
     * type: rel or rec (relative or recent searches)
     * */

    @RequestMapping(path = "/search/scrape", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity searchMultiplePlayers(@RequestParam(value = "count", defaultValue = "5") int count,
                                                @RequestParam(value = "search", defaultValue = "Harry Giles") String searchString,
                                                @RequestParam(value = "search_type", defaultValue = "rel") String type,
                                                @RequestParam(value = "ret_all", defaultValue = "true") boolean INC_all,
                                                @RequestParam(value = "ret_authors", defaultValue = "false") boolean INC_authors,
                                                @RequestParam(value = "ret_titles", defaultValue = "false") boolean INC_titles,
                                                @RequestParam(value = "ret_bodies", defaultValue = "false") boolean INC_bodies,
                                                @RequestParam(value = "ret_dates", defaultValue = "false") boolean INC_dates,
                                                @RequestParam(value = "ret_urls", defaultValue = "false") boolean INC_urls,
                                                @RequestParam(value = "ret_snippets", defaultValue = "false") boolean INC_snippets,
                                                @RequestParam(value = "max_time", defaultValue = "60") int max_time,
                                                @RequestParam(value = "any_web", defaultValue = "false") boolean any_web,
                                                @RequestParam(value = "br", defaultValue = "false") boolean br,
                                                @RequestParam(value = "cbs", defaultValue = "false") boolean cbs,
                                                @RequestParam(value = "espn", defaultValue = "false") boolean espn,
                                                @RequestParam(value = "fox", defaultValue = "false") boolean fox,
                                                @RequestParam(value = "nytimes", defaultValue = "false") boolean nytimes,
                                                @RequestParam(value = "si", defaultValue = "false") boolean si,
                                                @RequestParam(value = "yahoo", defaultValue = "false") boolean yahoo) {
        boolean error = false;
        List<String> web = new ArrayList<String>();
        // String[] web = new String[7];
        int domainCount = 0;
        try {
            if (br) {
                web.add("bleacherreport.com");
                domainCount++;
            }
            if (cbs) {
                web.add("cbssports.com");
                domainCount++;
            }
            if (espn) {
                web.add("espn.com");
                domainCount++;
            }
            if (fox) {
                web.add("foxsports.com");
                domainCount++;
            }
            if (nytimes) {
                web.add("nytimes.com");
                domainCount++;
            }
            if (si) {
                web.add("si.com");
                domainCount++;
            }
            if (yahoo) {
                web.add("sports.yahoo.com");
                domainCount++;
            }
            if (any_web || domainCount == 0) {
                web.add("bleacherreport.com");
                web.add("cbssports.com");
                web.add("espn.com");
                web.add("foxsports.com");
                web.add("nytimes.com");
                web.add("si.com");
                web.add("sports.yahoo.com");
            }

            System.out.println("INC_all: " + Boolean.toString(INC_all)  + "       INC_authors: " + Boolean.toString(INC_authors) + "       INC_titles: " + Boolean.toString(INC_titles) + "     INC_bodies: " + Boolean.toString(INC_bodies) + "     INC_dates:" + Boolean.toString(INC_dates) + "    INC_urls:" + Boolean.toString(INC_urls) + "INC_snippets" + Boolean.toString(INC_snippets));
            String searchResults = SearchWeb.createGeneralScrape(searchString, web, count, type, max_time, INC_all,  INC_authors, INC_titles, INC_bodies, INC_dates, INC_urls, INC_snippets);

            return ResponseEntity.ok(searchResults);

        } catch (Exception e) {
            System.out.println("Error handling search scrape");
        }
        //handle error here with Bad entry
        return ResponseEntity.badRequest().

                build();
    }

    @RequestMapping(path = "/search/urls", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity searchGetURLS(@RequestParam(value = "website", defaultValue = "espn.com") String website,
                                        @RequestParam(value = "count", defaultValue = "5") int count,
                                        @RequestParam(value = "search", defaultValue = "Harry Giles") String searchString,
                                        @RequestParam(value = "type", defaultValue = "rel") String type,
                                        @RequestParam(value = "max_time", defaultValue = "60") int max_time) {

        return ResponseEntity.ok(SearchWeb.createResponseUrls(website, searchString, count, type, max_time));

    }
}
