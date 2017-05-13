package ru.onotole.msuQuizApi.model;

/**
 * Created by onotole on 19/04/2017.
 */
public class Phrases {
    public static final String WELCOME = "Приветствую тебя в игре, посвященной Дню мехмата!\n\n" +
            "Для начала сообщи мне название своей команды и Фамилию Имя участников через запятую в одно сообщение. В команде может быть не более 5 человек.\n\n" +
            "После твоего сообщения запустится таймер на 45 минут.";
    public static final String LETS_START_GAME = "Начнем игру!\n\n" +
            "Я буду присылать вам вопросы, нужно правильно и как можно быстрее на них отвечать. " +
            "У вас по %s попытки ответа на каждый вопрос. Все ответы - целые, положительные числа. " +
            "Побеждает команда, набравшая наибольшее количество правильных ответов быстрее остальных.\n\n" +
            "Если считаете, что нашли ошибку, то сохраняйте переписку и после игры покажите организатору.\n" +
            "Внимание, время пошло!\n" +
            "Первое задание: ";
    public static final String CONGRATULATION = "Поздравляю! Ваша команда %s прошла игру за %02d:%02d:%02d и " +
            "набрала баллов: %d.\n\n Подходите к сцене после 17:00, будет награждение победителей.";
    public static final String REST_TIME = "PS. У вас осталось %d минут";
    public static final String WRONG_ANSWER = "Ответ неверный.";
    public static final String WRONG_ANSWER_NO_MORE_ATTEMPTS = WRONG_ANSWER + " Больше попыток нет :(";
    public static final String WRONG_ANSWER_LEFT_ATTEMPTS = WRONG_ANSWER + " Попыток осталось: %s\n";
    public static final String RIGHT_ANSWER = "Верный ответ!\n";
    public static final String NEXT_TASK = "Следующее задание:\n";
    public static final String INCORRECT_INPUT = "Я не понял что ты умеешь ввиду. Попробуй еще раз.";
}
