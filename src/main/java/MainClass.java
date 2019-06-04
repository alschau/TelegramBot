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
            telegramBotsApi.registerBot(new JamesBot("565706184:AAFyo2oQdNbcc_RjDgo_0Oe1EPm4jBKnbsk", "james13_bot"));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
