import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class QuizHandler extends TelegramLongPollingBot{
    private final String URL = "http://localhost:8089/";//System.getenv().get("PERSON_API_URL");
    private final String BOT_NAME = "onotolemobilebot";//System.getenv().get("BOT_NAME");
    private final String BOT_TOKEN = "257063477:AAHh42H3Wm1gq52uRKW8o6TcOqTi-1aBN3I";//System.getenv().get("BOT_TOKEN");
    private RequestsToApi requests = new RequestsToApi();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = requests.processInputMessage(URL, update.getMessage());
            SendMessage sendMessage = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText(text);
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


}
