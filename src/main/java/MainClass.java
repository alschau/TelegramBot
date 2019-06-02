import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//##########################################################################################
//
// Created by Aline Schaufelberger on 29.05.2019 - 17:33
//
//##########################################################################################

public class MainClass {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new JamesBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
