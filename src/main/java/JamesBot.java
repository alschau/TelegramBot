import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

//##########################################################################################
//
// Created by Aline Schaufelberger on 29.05.2019 - 17:33
//
//##########################################################################################

public class JamesBot extends TelegramLongPollingBot {
    static final String Admin = "265903135";
    String token = "565706184:AAFyo2oQdNbcc_RjDgo_0Oe1EPm4jBKnbsk";
    String name = "james13_bot";
    public boolean send = false;
    ArrayList<Game> games = new ArrayList<>();
    Game game;
    ArrayList<Person> persons = new ArrayList<>();
    public ArrayList<Person> player = new ArrayList<>();

    public JamesBot(String token, String name){
        this.token = token;
        this.name = name;
        sendNachrichtAdmin("James steht Ihnen zu Diensten!");
    }


    public void onUpdateReceived(Update update) {

        String command = update.getMessage().getText();
        Long chatid = update.getMessage().getChatId();
        User user = update.getMessage().getFrom();
        Person person = null;

        if (command == null)
            return;
        if (command.length() < 2)
            return;

        boolean hinzu = true;

        for (Person s : persons) {
            if (s.getId() == user.getId()) {
                hinzu = false;
                person = s;
                break;
            }
        }

        System.out.println(command);

        if (hinzu) {
            person = new Person(user);
            persons.add(person);
        }
        if (person.getUser() == null) {
            person.setUser(user);
        }

        if (command.equals("hello")) {
            sendNachrichtNorm("Hello"+ update.getMessage().getFrom().getFirstName() ,user.getId());
        }

        if (command.equals("/join")) {
            if(player.contains(person)){
                //bereits gejoint
                sendNachrichtNorm("You've already joined the Game.", user.getId());
            }else{
                //Knnest ihn nicht
                sendNachrichtNorm("Joining the game!", user.getId());
                player.add(person);
            }
        }

        if (command.equals("/chat")) {
            sendNachrichtNorm("Your Chat ID is: " + update.getMessage().getChatId(), user.getId());
            sendNachrichtNorm("-------GETTING ALL CHAT IDs-------", user.getId());
            for(Person p : persons){
                sendNachrichtNorm(Integer.toString(p.getId()), user.getId());
            }
        }

        if (command.equals("/exit")) {
            if(player.contains(person)){
                //Spiel verlassen
                sendNachrichtNorm("Leaving the Game!", user.getId());
                player.remove(person);

            }else{
                //Kann nicht verlassen
                sendNachrichtNorm("You can't exit because You haven't joined a Game.", user.getId());
            }
        }

        if (command.equals("/name")) {
            System.out.println(update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());
            sendNachrichtNorm(update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName(),user.getId());
        }
        if(person.antworter && ! command.startsWith("/") ){
            game.addAntwort(command.trim(),person);
        }

        if(person.fragesteller && ! command.startsWith("/")){
            if(game.frage.equals(""))
                game.setFrage(command.trim());
            else
                game.setMasterAntwort(command.trim());
        }

        if(command.equals("/start")){

            for(Person p : player) {
                sendNachrichtNorm("Starting the Game!", p.getId());
            }
            game = new Game(player, person, this);

        }

        // Chat ID Aline: Long 265903135
        // Chat ID Marc: Long 748488681
        // Chat ID Ülkü: Long 754741889
    }

    public String getBotUsername() {
        return this.name;
    }

    public String getBotToken() {
        return this.token;
    }


    void sendNachrichtNorm(String nachricht,int chatid) {
        SendMessage TestNachricht = new SendMessage();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId(chatid+"");
        TestNachricht.setText(nachricht);
        try {
            execute(TestNachricht);
        } catch (Exception e) {
            sendNachrichtAdmin("`Fehler bei send to Norm:`\n" + e.toString());
        }
    }

    void sendNachrichtAdmin(String nachricht) {
        SendMessage TestNachricht = new SendMessage();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId(Admin);
        TestNachricht.setText(nachricht);
        try {
            execute(TestNachricht);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

