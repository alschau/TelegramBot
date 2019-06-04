import org.telegram.telegrambots.meta.api.objects.User;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

//##########################################################################################
//
// Created by Aline Schaufelberger on 01.06.2019 - 20:26
//
//##########################################################################################

public class Person implements Serializable {
    User user;
    int id;
    int points = 0;
    long privatchat = 0;
    long groupchat = 0;
    long game = 0;
    long win = 0;
    long lose = 0;
    // Status für Check ob Spieler auf Telegram & in Game ist
    boolean status = false;

    public Person(User user) {
        this.user = user;
        this.id = user.getId();
        this.setPoints(points);
    }

    public User getUser() {
        if (user !=null)
            return user;
        else
            return null;
    }

    synchronized void game() {
        long game = this.getGame();
        this.setGame(++game);
    }

    synchronized void win(int betrag) {
        long won = this.getWin();
        this.setWin(++won);

    }

    synchronized void lose(int betrag) {
        long lost = this.getLose();
        this.setLose(++lost);
    }

    public int getId(){
        return id;
    }

    // SETTERS & GETTERS
    public void setUser(User user) {
        this.user = user;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String getPointsString() {
        return points + " Points";
    }

    void reset(){
        this.setPoints(0);
    }

    public long getGame() {
        return game;
    }

    public void setGame(long black) {
        this.game = black;
    }

    public long getWin() {
        return win;
    }

    public void setWin(long bwin) {
        this.win = bwin;
    }

    public long getLose() {
        return lose;
    }

    public void setLose(long blose) {
        this.lose = blose;
    }

}
