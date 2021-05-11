import java.util.ArrayList;

public class Cast {

    private String movieId;
    private ArrayList<String> castMembers;

    public Cast(){
        castMembers = new ArrayList<String>();
    }

    public Cast(String movieId, ArrayList<String> castMembers) {
        this.movieId = movieId;
        this.castMembers = castMembers;
    }

    public String getMovieId() {
        return movieId;
    }
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }


    public ArrayList<String> getCastMembers() { return castMembers; }
    public void setCastMember(String name) { this.castMembers.add(name); }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Cast Details - ");
        sb.append("MovieId:" + getMovieId());
        sb.append("\n");
        sb.append("--Actors--\n");
        for(int i = 0; i < castMembers.size(); i++) {
            //sb.append(castMembers.get(i) + "\n");
            String curr = castMembers.get(i);
            // get  actor id
            int tempID = 0;
            for(int j = 0; j < curr.length(); j++){
                tempID += curr.charAt(j);
            }
            sb.append("" + tempID + "\n");
        }
        return sb.toString();
    }
}
