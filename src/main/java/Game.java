import java.io.Serializable;
import java.util.*;

//##########################################################################################
//
// Created by Aline Schaufelberger on 01.06.2019 - 20:24
//
//##########################################################################################


public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    Person fragesteller;
    ArrayList<Person> player;
    String frage = "";
    ArrayList<Antwort> antworten = new ArrayList<>();

    Map<Integer, Person> prepared = new HashMap<>();
    Map<Person,Person> vote = new HashMap<>();


    JamesBot bot;
    String masterantwort;
    Boolean readyToVote = false;


    public Game(ArrayList<Person> player, Person startPerson, JamesBot bot) {
        this.fragesteller = startPerson;
        this.player = player;
        this.bot = bot;

        // Get Question from Start Player
        getFrage();
        if (masterantwort != null && this.antworten.size() == player.size()-1){
            prepare();
        }
        if (readyToVote){
            vote();
        }
    }

    public void getFrage() {
        sendNachrichtAnPlayer("Das Spiel beginnt, " + fragesteller.getUser().getFirstName() + " überlegt sich schon die Frage!");
        bot.sendNachrichtNorm("Deine Frage?", fragesteller.getId());
        fragesteller.fragesteller = true;
    }

    public void sendNachrichtAnPlayer(String nachricht){
        for (Person per : this.player) {
            if (per.getId() != fragesteller.getId()) {
                bot.sendNachrichtNorm(nachricht, per.getId());
            }
        }
    }

    public void sendNachrichtAnAlle(String nachricht){
        for (Person per : this.player) {
            bot.sendNachrichtNorm(nachricht,per.getId());
        }
    }

    public void addAntwort(String antwort, Person per){
        per.antworter = false;
        per.voter = true;
        Antwort ant = new Antwort(per, antwort, false);
        antworten.add(ant);
        if(this.antworten.size() == player.size()-1){
            if (this.masterantwort != null){
                sendNachrichtAnAlle("Alle haben ihre Antworten abgegeben!");
                prepare();
            }
        }
    }

    public void setFrage(String Frage){
        this.frage = Frage;
        sendNachrichtAnAlle("Die Frage lautet:\n" + frage);
        sendNachrichtAnPlayer("Was ist deine Antwort?");
        for (Person per : this.player) {
            if (per.getId() != fragesteller.getId()) {
                per.antworter = true;
            }
        }
        bot.sendNachrichtNorm("Wie lautet die richtige Antwort?", fragesteller.getId());
    }

    public void setMasterAntwort(String antwort, Person person){
        fragesteller.fragesteller = false;
        System.out.println(fragesteller.getUser().getFirstName());
        System.out.println(antwort);
        this.masterantwort = antwort;
        Antwort ant = new Antwort(person, antwort,true);
        antworten.add(ant);
        System.out.println("Set!");
    }

    public void prepare(){
        Collections.shuffle(antworten);
        for (int i=0; i<this.antworten.size(); i++) {
            antworten.get(i).setI(i);
            sendNachrichtAnAlle(i + " - " + antworten.get(i).getAntwort());
        }
        readyToVote = true;
    }

    public void addVotes(int i, Person person){

    }

    public void vote() {
        //vote.put((String) vote.keySet().toArray()[], );
    }

    public void points() {
        // ..Punkte zuteilen..
    }

    public Person getFragesteller() {
        return fragesteller;
    }

    public void setFragesteller(Person fragesteller) {
        this.fragesteller = fragesteller;
    }


    public ArrayList<Person> getPlayer() {
        return player;
    }
}
