import java.util.ArrayList;

public class Antwort {

    private int i;
    private Person person;
    private String antwort;
    private boolean master =false;
    private Integer votes = 0;
    private ArrayList<Person> myVoters = new ArrayList<>();

    public Antwort(Person person, String antwort,boolean master){
        this.person = person;
        this.antwort = antwort;
        this.master = master;
    }

    public void addVoter(Person person){
        myVoters.add(person);
        votes++;
    }

    public Boolean isMaster() { return master; }

    public int getI() {
        return i;
    }

    public void setI(int newi){
        this.i = newi;
    }

    public Person getPerson(){
        return person;
    }

    public void setPerson(Person p){
        this.person = p;
    }

    public String getAntwort(){
        return antwort;
    }

    public void setAntwort(String s){
        this.antwort = s;
    }

    public ArrayList<Person> getMyVoters(){return myVoters;}
}
