package bots;

import lombok.*;

/**
 * Created by Alexey on 20.07.2017.
 */
@EqualsAndHashCode
class ContactCount {

    private static final int OVERFLOW_BARRIER = 3;
    @Getter
    private String name;
    private RequestType type;
    private int count;

    static ContactCount emptyInstance() {
        ContactCount contactCount = new ContactCount();
        contactCount.name = "";
        contactCount.type = RequestType.AVDOTYA;
        contactCount.count = 0;
        return contactCount;
    }

    public void updateContact(@NonNull String name, @NonNull RequestType type) {
        if (this.name.equals(name) && this.type == type) {
            this.count++;
        } else {
            this.name = name;
            this.type = type;
            count = 1;
        }
    }

    public boolean isOverflowed() {
        return count >= OVERFLOW_BARRIER;
    }

}
