package SRMovieDB.ML;

import SRMovieDB.ML.MovieML;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MovieResponse {
    private int page;
    private List<MovieML> results;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

    // Constructores
    public MovieResponse() {}

    // Getters y Setters
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public List<MovieML> getResults() { return results; }
    public void setResults(List<MovieML> results) { this.results = results; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }
}