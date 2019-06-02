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
    //private static final long serialVersionUID = 1L;
    Person person;
    String frage;
    long start;
    //Map<String, Integer> antwort = new HashMap<>();
    //ArrayList<Person> player = new ArrayList<>();

    /*
    public Game(Person player, String frage, String[] antworten) {
        this.person = player;
        this.frage = frage;
        for (int i = 0; i < antworten.length; i++)
            antwort.put(antworten[i].trim(), 0);
        start = System.currentTimeMillis();
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getFrage() {
        return frage;
    }

    public Map<String, Integer> getAntwort() {
        return antwort;
    }


    public ArrayList<Person> getPlayer() {
        return player;
    }

    public synchronized void vote(int was, Person p) {
        //player.add(p);
        //antwort.put((String) antwort.keySet().toArray()[was - 1], (int) antwort.values().toArray()[was - 1] + 1);
    }

    public synchronized boolean delete() {
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        float elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);
        float stunden = elapsedTimeMin / 60;
        if (stunden >= 12)
            return true;
        return false;
    }
    */
}
