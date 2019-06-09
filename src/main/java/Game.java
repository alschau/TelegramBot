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
    Integer votes = 0;
    ArrayList<Antwort> antworten = new ArrayList<>();
    ArrayList<Person> rankingList = new ArrayList<>();

    JamesBot bot;
    Antwort masterantwort;

    Boolean readyToVote = false;


    public Game(ArrayList<Person> player, Person startPerson, JamesBot bot) {
        this.fragesteller = startPerson;
        this.player = player;
        this.bot = bot;

        // Get Question from Start Player
        getFrage();
        if (masterantwort != null && this.antworten.size() == player.size()-1){
            shuffleAndSend();
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
        if(antworten.size() == player.size()){
            if (this.masterantwort != null){
                sendNachrichtAnAlle("Alle haben ihre Antworten abgegeben!");
                shuffleAndSend();
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
        Antwort ant = new Antwort(person, antwort,true);
        this.masterantwort = ant;
        antworten.add(ant);
        System.out.println("Set!");
        if (this.antworten.size() == player.size()){
            shuffleAndSend();
        }
    }

    public void shuffleAndSend(){
        Collections.shuffle(antworten);
        for (int i=0; i<this.antworten.size(); i++) {
            antworten.get(i).setI(i);
            sendNachrichtAnAlle(i + " - " + antworten.get(i).getAntwort());
        }
        sendNachrichtAnPlayer("Please Vote! \n Enter a number.");
        readyToVote = true;
    }

    public void Vote(int i, Person person){
        bot.sendNachrichtNorm("Okay", person.getId());
        person.voter = false;
        votes++;
        for (Antwort ant : this.antworten){
            if (i == ant.getI()){
                System.out.println(ant.isMaster());
                if (ant.isMaster()){
                    person.addPoints();
                    ant.addVoter(person);
                    System.out.println(person.getUser().getFirstName() + " chose wisely!");
                } else {
                    ant.getPerson().addPoints();
                    ant.addVoter(person);
                    System.out.println(person.getUser().getFirstName() + " has been fooled!");
                }
            }
        }
        ranking();
        if (votes == this.antworten.size() -1){
            fools();
        }
    }

    public void fools() {
        for (Antwort ant : this.antworten){
            if(!ant.isMaster() && ant.getMyVoters().size()!=0) {
                bot.sendNachrichtNorm("You have fooled: ", ant.getPerson().getId());
                for (Person person : ant.getMyVoters()) {
                    bot.sendNachrichtNorm(person.user.getFirstName(), ant.getPerson().getId());
                }
            } else if (ant.isMaster()){
                bot.sendNachrichtNorm("List of Correct guesses: ", ant.getPerson().getId());
                for (Person person : ant.getMyVoters()) {
                    bot.sendNachrichtNorm(person.user.getFirstName(), ant.getPerson().getId());
                }
            }
        }
    }

    public void ranking(){
        if (rankingList.size() == 0){
            for (Person person : player){
                rankingList.add(person);
            }
            // Sort by person.getPoints BUBBLE SORT
        } else {
            //update ranking
        }
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
