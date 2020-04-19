package com.aws.codestar.teamprime;
public class SearchResult {


    private final String title;
    private final String url;
    private final String author;
    private final String date;
    private final String body;
    private final String snippet;

    public SearchResult(String title, String url, String author, String date, String body, String snippet) {

        this.title = title;
        this.url = url;
        this.author = author;
        this.date = date;
        this.body = body;
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public String getSnippet() {return snippet; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;

        result = 31 * result + (url != null ? url.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}