package garg.prateek.popularmoviesstageone.utilities;

public class Movie {
    private int id;
    private  float averageVotes;
    private int voteCounts;
    private String originalTitle;
    private String title;
    private double popularity;
    private String backdropPath;
    private String overview;
    private String releaseDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAverageVotes() {
        return averageVotes;
    }

    public void setAverageVotes(float averageVotes) {
        this.averageVotes = averageVotes;
    }

    public int getVoteCounts() {
        return voteCounts;
    }

    public void setVoteCounts(int voteCounts) {
        this.voteCounts = voteCounts;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    private String posterPath;
}
