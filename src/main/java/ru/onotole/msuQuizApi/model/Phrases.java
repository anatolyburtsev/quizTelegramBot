package ru.onotole.msuQuizApi.model;

/**
 * Created by onotole on 19/04/2017.
 */
public class Phrases {
    public static final String WELCOME = "Начнем игру! Я буду присылать тебе вопросы, тебе нужно правильно на них отвечать." +
            "У тебя по %s попыток ответа на каждый вопрос. Все ответы - целые, положительные числа. На твой результат " +
            "влияет не только количество правильных ответов, но и время прохождения всех заданий. " +
            "Время пошло! " +
            "Первое задание: ";
    public static final String CONGRATUATION = "Поздравляю! Ты прошел игру за %d:%d:%d и набрал %d баллов!";
    public static final String WRONG_ANSWER = "Ответ неверный. ";
    public static final String WRONG_ANSWER_NO_MORE_ATTEMPTS = WRONG_ANSWER + "Больше попыток нет. Следующее задание:\n";
    public static final String WRONG_ANSWER_LEFT_ATTEMPTS = WRONG_ANSWER + "Попыток осталось: %s \n";
    public static final String RIGHT_ANSWER = "Верный ответ!\n";
    public static final String NEXT_TASK = "Следующее задание:\n";
}
