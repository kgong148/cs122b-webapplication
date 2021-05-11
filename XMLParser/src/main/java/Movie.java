public class Movie {

    private String director;
    private int year;
    private String id;
    private String title;
    private String genre;
    private int genreId;

    public Movie(){

    }

    public Movie(String director, int year, String id,String title, String genre, int genreId) {
        this.director = director;
        this.year = year;
        this.id  = id;
        this.title = title;
        this.genre = genre;
        this.genreId = genreId;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getGenreId(){ return genreId; }
    public void setGenreId(int genreId) { this.genreId = genreId; }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Movie Details - ");
        sb.append("Title:" + getTitle());
        sb.append(", ");
        sb.append("Director:" + getDirector());
        sb.append(", ");
        sb.append("Id:" + getId());
        sb.append(", ");
        sb.append("Year:" + getYear());
        sb.append(", ");
        sb.append("Genre:" + getGenre());
        sb.append(", ");
        sb.append("GenreId:" + getGenreId());
        sb.append(".");

        return sb.toString();
    }
}
