
package SRMovieDB.ML;

public class MovieML {

    private boolean adult;
    private String backdrop_path;
    private int genre_ids;
    private int id;
    private String orignal_language;
    private String orignial_title;
    private String overview;
    private int popularity;
    private String poster_path;
    private String release_date;
    private String title;
    private boolean video = true;
    private int vote_average;
    private int vote_count;
 
    
    public MovieML(){}

    public MovieML(boolean adult, String backdrop_path, int genre_ids, int id, String orignal_language, String orignial_title, String overview, int popularity, String poster_path, String release_date, String title, int vote_average, int vote_count) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.genre_ids = genre_ids;
        this.id = id;
        this.orignal_language = orignal_language;
        this.orignial_title = orignial_title;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.title = title;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public int getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int genre_ids) {
        this.genre_ids = genre_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrignal_language() {
        return orignal_language;
    }

    public void setOrignal_language(String orignal_language) {
        this.orignal_language = orignal_language;
    }

    public String getOrignial_title() {
        return orignial_title;
    }

    public void setOrignial_title(String orignial_title) {
        this.orignial_title = orignial_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public int getVote_average() {
        return vote_average;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
    
    
}
