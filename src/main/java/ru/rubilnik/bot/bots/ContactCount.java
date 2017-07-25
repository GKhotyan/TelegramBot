package ru.rubilnik.bot.bots;

import lombok.*;
import ru.rubilnik.bot.bots.data.PatternType;

/**
 * Created by Alexey on 20.07.2017.
 */
@EqualsAndHashCode
public class ContactCount {

    private static final int OVERFLOW_BARRIER = 4;
    @Getter
    private String name;
    private PatternType type;
    private int count;

    static ContactCount emptyInstance() {
        ContactCount contactCount = new ContactCount();
        contactCount.name = "";
        contactCount.type = PatternType.AVDOTYA;
        contactCount.count = 0;
        return contactCount;
    }

    public void updateContact(@NonNull String name, @NonNull PatternType type) {
        if (this.name.equals(name) && this.type == type && type.isCount()) {
            this.count++;
        } else {
            this.name = name;
            this.type = type;
            count = 1;
        }
    }

    public boolean isOverflowed() {
        return count > OVERFLOW_BARRIER;
    }

}
