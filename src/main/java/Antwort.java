import java.util.ArrayList;

public class Antwort {

    private int i;
    private Person person;
    private String antwort;
    private boolean ismaster =false;
    private Integer votes = 0;
    private ArrayList<Person> myVoters;

    public Antwort(Person person, String antwort,boolean ismaster){
        this.i = i;
        this.person = person;
        this.antwort = antwort;
        this.ismaster = ismaster;
    }

    public void voting(Person person){

    }

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
}
