import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//##########################################################################################
//
// Created by Aline Schaufelberger on 29.05.2019 - 17:33
//
//##########################################################################################

public class PlinioBot extends TelegramLongPollingBot {
    static final String Admin = "265903135";
    String token;
    String name;
    public boolean locked = true;
    ArrayList<Person> persons = new ArrayList<>();
    public ArrayList<Person> player = new ArrayList<>();

    public PlinioBot(String token, String name){
        this.token = token;
        this.name = name;
        sendNachrichtAdmin("Buenvenutti nel Casa Plinio!");

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

        for (Person s : persons) {
            if (s.getId() == user.getId()) {
                person = s;
                break;
            }
        }



        if(locked){
            sendNachrichtNorm("Enter Password:", user.getId());
            if(command.equals("che")){
                locked = false;
            }
        } else {

            System.out.println(command);

            if (command.equals("/start")) {
                sendNachrichtNorm("Buenvenutti nel Casa Plinio!", user.getId());

                SendPhoto msg = new SendPhoto().setChatId(chatid).setPhoto("https://im4.ezgif.com/tmp/ezgif-4-1933bc61c091.png").setCaption("");
                try {
                    this.execute(msg);
                    ; // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chatid)
                        .setText("Please choose one of the following options:");
                // Create ReplyKeyboardMarkup object
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                // Create the keyboard (list of keyboard rows)
                List<KeyboardRow> keyboard = new ArrayList<>();
                // Create a keyboard row
                KeyboardRow row = new KeyboardRow();
                // Set each button, you can also use KeyboardButton objects if you need something else than text
                row.add("Welcome Guide");
                row.add("Before leaving");
                // Add the first row to the keyboard
                keyboard.add(row);
                // Create another keyboard row
                row = new KeyboardRow();
                // Set each button for the second line
                row.add("Drinks");
                row.add("Else");
                // Add the second row to the keyboard
                keyboard.add(row);
                // Set the keyboard to the markup
                keyboardMarkup.setKeyboard(keyboard);
                // Add it to the message
                message.setReplyMarkup(keyboardMarkup);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            List<String> hello = Arrays.asList("Hello", "hello", "hi", "Hi", "ciao", "Ciao");
            if (hello.contains(command)) {
                sendNachrichtNorm("Hello, " + update.getMessage().getFrom().getFirstName(), user.getId());
            }

            if (command.equals("/Drinks")) {
                System.out.println("es lauft!!");
            }
        }



// #########################################################################################



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


        if(command.equals("/end")){
            for(Person per : player){
                sendNachrichtNorm("The Game is Over!", per.getId());
                // Show stats (personal stats)
                // Reset everything (maybe keep amount of wins)
                // End the Game..
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

    public String getItems(Person p) {
        if(!player.contains(p))
            return "Join";
        else{
            String back = "1";
            for(int i = 2; i<player.size();i++){
                back += ","+i;
            }
            return back;
        }
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

