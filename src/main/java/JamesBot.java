import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;

//##########################################################################################
//
// Created by Aline Schaufelberger on 29.05.2019 - 17:33
//
//##########################################################################################

public class JamesBot extends TelegramLongPollingBot {
    static final String Admin = "265903135";
    String token;
    String name;
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
        if (command.length() < 1)
            return;

        boolean hinzu = true;

        for (Person s : persons) {
            if (s.getId() == user.getId()) {
                hinzu = false;
                person = s;
                break;
            }
        }

        /*
        if (persons.size()>1){
            game.SetAntwortenReady = false;
        }
        */

        System.out.println(command);

        if (hinzu) {
            person = new Person(user);
            persons.add(person);
        }
        if (person.getUser() == null) {
            person.setUser(user);
        }

        // Getting the Questions and Answers from Everyone (Antworter and Fragesteller)
        if(person.antworter && ! command.startsWith("/") ){
            game.addAntwort(command.trim(),person);
        } else if (person.voter && ! command.startsWith("/") && isNumeric(command)){

            Integer i = Integer.valueOf(command);
            game.Vote(i, person);
        }

        if(person.fragesteller && ! command.startsWith("/")){
            if(game.frage.equals(""))
                game.setFrage(command.trim());
            else
                game.setMasterAntwort(command.trim(), person);
        }


        if (command.equals("hello")) {
            sendNachrichtNorm("Hello"+ update.getMessage().getFrom().getFirstName() ,user.getId());
        }

        if (command.equals("/join") || command.equalsIgnoreCase("join")) {
            if(player.contains(person)){
                //bereits gejoint
                sendNachricht("You've already joined the Game.", person);
            }else{
                //kennst ihn nicht

                player.add(person);
                sendNachricht("Joining the game!", person);
            }
        }

        if (command.equals("/leave") || command.equalsIgnoreCase("exit")) {
            if(player.contains(person)){
                //Spiel verlassen
                sendNachrichtNorm("Leaving the Game!", user.getId());
                player.remove(person);

            }else{
                //Kann nicht verlassen
                sendNachrichtNorm("You can't exit because You haven't joined a Game.", user.getId());
            }
        }

        if(command.equals("/start") || command.equalsIgnoreCase("start")){
            if(!player.contains(person))
                sendNachricht("Willkommen bei \"Nobody\'s perfect\" " + person.getUser().getFirstName(),person);
            else {
                for (Person p : player) {
                    sendNachricht("Starting the Game!", p);
                }
                game = new Game(player, person, this);
            }

        }

        if(command.equals("/end")){
            for(Person per : player){
                sendNachrichtNorm("The Game is Over!", per.getId());
                // Show stats (personal stats)
                // Reset everything (maybe keep amount of wins)
                // End the Game..
            }
        }

        if (command.equals("/stats")) {
            sendNachrichtNorm("Sterne: "+person.getSterne(), person.getId());
            sendNachrichtNorm("Ranking: ", person.getId());
            for(int i=0; i<game.rankings.length; i++){
                if (game.rankings[i] != null) {
                    sendNachrichtNorm(i + ": " + game.rankings[i].getUser().getFirstName() + " - "
                            + game.rankings[i].getPoints() + " Points", person.getId());
                }
            }
        }

        if (command.equals("/name")) {
            System.out.println(update.getMessage().getFrom().getFirstName() + " " +
                    update.getMessage().getFrom().getLastName());
            sendNachrichtNorm(update.getMessage().getFrom().getFirstName() + " " +
                    update.getMessage().getFrom().getLastName(),user.getId());
        }
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

    void sendNachricht(String nachricht,Person p) {
        SendMessage TestNachricht = new SendMessage();
        ReplyKeyboardMarkup back = new ReplyKeyboardMarkup();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId(p.getId()+"");
        String item = getItems(p);
        String[] buttonArray = item.split(",");
        java.util.List<KeyboardRow> commands = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.addAll(Arrays.asList(buttonArray));
        commands.add(row);
        back.setResizeKeyboard(true);
        back.setOneTimeKeyboard(false);
        back.setKeyboard(commands);
        back.setSelective(true);
        TestNachricht.setReplyMarkup(back);
        TestNachricht.setText(nachricht.replaceAll("(?=[]\\[+|`'{}^_~*\\\\])", "\\\\"));
        try {
            execute(TestNachricht);
        } catch (Exception e) {

        }
    }
    public String getItems(Person p) {
        if(!player.contains(p))
            return "Join";
        else if(!p.voter)
            return "Exit,Start";
        else{
            String back = "1";
            for(int i = 2; i<player.size();i++){
                back += ","+i;
            }
            return back;
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

    // Helper function to check if String is a number
    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}

