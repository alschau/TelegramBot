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
    Person person;
    ArrayList<Person> player;
    String frage;
    Map<String, Person> antwort = new HashMap<>();
    Map<String, Person> vote = new HashMap<>();

    public Game(ArrayList player, Person startPerson) {
        this.person = startPerson;
        this.player = player;
        System.out.println("Is this happening? - YES!!");
        // Spielablauf
        // 1: Get Question from Start Player
        String q = question(this.person);
        // 2: Get Answers from All (every User in ArrayList player)
        for (Person p : this.player){
            antwort.put(answer(p), p);
        }
    }

    public synchronized String question(Person p) {
        // Get Question from StartPerson
        System.out.println("And this? - Yes!!");

        // Start Player: Enter the Question:
        // The Rest: Waiting for <name of start player> to enter the Question..

        frage = getFrage();
        return frage;
    }

    public synchronized String answer(Person person) {
        // Get Answers from Everyone
        String a = "A";
        return a;
    }

    public synchronized void vote(Person p) {

        //vote.put((String) vote.keySet().toArray()[], );
    }

    public synchronized void points() {
        // ..Punkte zuteilen..
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getFrage() {
        return "hello";
    }


    public ArrayList<Person> getPlayer() {
        return player;
    }
}
