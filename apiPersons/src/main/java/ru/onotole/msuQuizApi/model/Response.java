package ru.onotole.msuQuizApi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by onotole on 21/04/2017.
 */
@Getter
@AllArgsConstructor
public class Response {
    private String body;

    public void addPrefix(String prefix) {
        this.body = prefix + "\n" + body;
    }
    public void addPostfix(String postfix) {
        this.body = body + "\n" + postfix;
    }
}
