package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Random;

public class PasswordGeneratorBot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "RndPwdGen_Bot";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
                SendMessage msg = new SendMessage();
                msg.setChatId(update.getMessage().getChatId());
                String sayHello = """                                              
                        Этот бот создан для генерации паролей.
                        Вы можете направить боту число от 1 до 1024 и он сгенерирует вам пароль соответствующей длины.
                        """;
                msg.setText(sayHello);
                sendMessage(msg);
            } else if (update.hasMessage() && update.getMessage().hasText()) {
                SendMessage msg = new SendMessage();
                msg.setChatId(update.getMessage().getChatId());
                String passwordLength = (update.getMessage().getText());

                if (isNumeric(passwordLength) && isAvailableValue(passwordLength)) {
                    int len = Integer.parseInt(passwordLength);
                    String userPassword = String.valueOf(generatePassword(len));
                    msg.setText(userPassword);
                } else {
                    String errorMessage = """                                            
                            Бот понимает только положительные числовые значения в интервале от 1 до 1024.
                            """;
                    msg.setText(errorMessage);
                }
                sendMessage(msg);
            }
        }
    }

    private void sendMessage(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    static char[] generatePassword(int len) {
        String capitalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()<>?,./;:'";
        String values = capitalChars + smallChars + numbers + symbols;
        Random random = new Random();
        char[] password = new char[len];
        for (int i = 0; i < len; i++) {
            password[i] = values.charAt(random.nextInt(values.length()));
        }
        return password;
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    private static boolean isAvailableValue(String str) {
        int minPasswordLength = 1;
        int maxPasswordLength = 1024;
        return Integer.parseInt(str) >= minPasswordLength && Integer.parseInt(str) <= maxPasswordLength;
    }

}
