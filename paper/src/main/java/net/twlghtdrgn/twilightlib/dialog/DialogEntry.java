package net.twlghtdrgn.twilightlib.dialog;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class DialogEntry {
    private final String entryID;
    private final String prompt;
    private final Map<String, String> answers = new HashMap<>();

    public DialogEntry addAnswer(String answer, String action) {
        this.answers.put(answer, action);
        return this;
    }
}
