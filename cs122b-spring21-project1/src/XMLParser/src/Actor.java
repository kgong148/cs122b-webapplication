public class Actor {

    private String name;
    private int dob;
    private String actorID;

    public Actor(){
    }

    public Actor(String name, int dob, String actorID) {
        this.name = name;
        this.dob = dob;
        this.actorID = actorID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getActorID() { return ("" + actorID); }
    public void setActorID(String actorID){ this.actorID = actorID; }

    public int getDob() { return dob; }
    public void setDob(int dob) { this.dob = dob; }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Actor Details - ");
        sb.append("Name:" + getName());
        sb.append(", ");
        sb.append("Dob:" + getDob());
        sb.append(", ");
        sb.append("ActorID:" + getActorID());
        sb.append(".");

        return sb.toString();
    }
}
