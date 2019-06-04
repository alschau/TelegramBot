import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    Map<Person,String> antworten = new HashMap<>();
    Map<Person,String> vote = new HashMap<>();
    JamesBot bot;
    String masterantwort;

    public Game(ArrayList<Person> player, Person startPerson, JamesBot bot) {
        this.fragesteller = startPerson;
        this.player = player;
        this.bot = bot;

        // Spielablauf
        // 1: Get Question from Start Player
        getFrage();

        // 2: Get Answers from All (every User in ArrayList player)

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
        this.antworten.put(per,antwort);
        per.antworter = false;
        if(this.antworten.size() == player.size() - 1){
            sendNachrichtAnAlle("Alle haben ihre Antworten abgegeben!");
            for(String stuff: this.antworten.values())
                sendNachrichtAnAlle(stuff);
            sendNachrichtAnAlle(masterantwort);
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

    public void setMasterAntwort(String antwort){
        fragesteller.fragesteller = false;
        System.out.println(fragesteller.getUser().getFirstName());
        System.out.println(antwort);
        this.masterantwort = antwort;
        System.out.println("Set!");
    }




    public synchronized void vote(Person p) {

        //vote.put((String) vote.keySet().toArray()[], );
    }

    public synchronized void points() {
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
