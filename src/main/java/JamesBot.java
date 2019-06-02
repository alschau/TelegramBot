import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//##########################################################################################
//
// Created by Aline Schaufelberger on 29.05.2019 - 17:33
//
//##########################################################################################

public class JamesBot extends TelegramLongPollingBot {
    static final String Admin = "265903135";
    String token = "565706184:AAFyo2oQdNbcc_RjDgo_0Oe1EPm4jBKnbsk";
    String name = "james13_bot";

    String group = "--";
    String norm = "--";

    public boolean send = false;

    /*
    ArrayList<User> user = new ArrayList<>();
    ArrayList<Game> games = new ArrayList<>();
    ArrayList<Person> person = new ArrayList<>();


    public JamesBot(String token, String name){
        this.token = token;
        this.name = name;
        sendNachrichtAdmin("Gestartet!");
    }
    */


    public void onUpdateReceived(Update update) {

        //System.out.println(update.getMessage().getText());
        //System.out.println(update.getMessage().getFrom().getFirstName());

        String command=update.getMessage().getText();
        SendMessage message = new SendMessage();
        Long[] users = new Long[50];
        users[5] = 1234567L;

        if (command.equals("hello")){
            message.setText("Hello, "+update.getMessage().getFrom().getFirstName());
        }

        if (command.equals("/start")){
            //message.setText("Starting the Game!\nLanguage: "+update.getMessage().getFrom().getLanguageCode());
            for (int i=0; i<users.length; i++) {
                if (users[i] == null || users[i] < 10){
                    users[i]= update.getMessage().getChatId();
                }
            }
        }

        if (command.equals("/chat")){
            System.out.println("hi");
            for (int i=0; i<users.length; i++) {
                if (users[i] != null){
                    System.out.print(users[i]);
                }
            }
        }

        if (command.equals("/first")){
            System.out.println(update.getMessage().getFrom().getFirstName());
            message.setText(update.getMessage().getFrom().getFirstName());
        }

        if (command.equals("/last")){
            System.out.println(update.getMessage().getFrom().getLastName());
            message.setText(update.getMessage().getFrom().getLastName());
        }

        if (command.equals("/name")){
            System.out.println(update.getMessage().getFrom().getFirstName()+" "+ update.getMessage().getFrom().getLastName());
            message.setText(update.getMessage().getFrom().getFirstName()+" "+update.getMessage().getFrom().getLastName());
        }

        //message.setChatId((users[0]));
        message.setChatId(update.getMessage().getChatId());
        // Chat ID Aline: Long 265903135
        // Chat ID Marc: Long 748488681

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /*
    int sendNachrichtUser(String nachricht, Person person) {
        SendMessage TestNachricht = new SendMessage();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId(person.getUser().getId() + "");
        TestNachricht.setText(nachricht);
        try {
            return sendMessage(TestNachricht).getMessageId();
        } catch (Exception e) {
            System.out.println(e);
            sendNachrichtAdmin(e.toString());
            if (e.toString().contains("retry after")) {
                try {
                    int time = Integer.parseInt(e.toString().split("after")[1]);
                    SendMessage errMsg = new SendMessage();
                    errMsg.enableMarkdown(true);
                    errMsg.setChatId(person.getUser().getId() + "");
                    errMsg.setText("Es gibt leider einen Telegram Alfsweep-Cooldown. Probiere es nochmal in " + time + " Sekunden!");
                } catch (Exception e1) {
                    sendNachrichtAdmin(e.toString());
                }
            }
            //sendNachrichtNorm("Oh " + stu.getUser().getFirstName() + ", ich kann dir nicht schreiben! Bitte klicke hier:\n@Klassenbotbot\nGehe auf den Chat mit mir und drücke start.");
        }
        return -1;
    }

    void sendNachricht(String nachricht, long chatid) {
        SendMessage TestNachricht = new SendMessage();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId(chatid + "");
        TestNachricht.setText(nachricht);
        try {
            sendMessage(TestNachricht).getMessageId();
        } catch (Exception e) {
            sendNachrichtAdmin("Fehler beim normalen Nachrichten versenden:\n" + nachricht + "\n" + e.toString());
        }
    }

    void sendNachrichtGroup(String nachricht) {
        SendMessage TestNachricht = new SendMessage();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId(group + "");
        TestNachricht.setText(nachricht);
        try {
            sendMessage(TestNachricht);
        } catch (Exception e) {
            sendNachrichtAdmin("`Fehler bei send to Group:`\n" + e.toString());
        }
    }

    void sendNachrichtNorm(String nachricht) {
        SendMessage TestNachricht = new SendMessage();
        TestNachricht.enableMarkdown(true);
        TestNachricht.setChatId(norm + "");
        TestNachricht.setText(nachricht);
        try {
            sendMessage(TestNachricht);
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
            sendMessage(TestNachricht);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    public String getBotUsername() {
        return this.name;
    }

    public String getBotToken() {
        return this.token;
    }


}

