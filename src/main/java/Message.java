import java.util.HashMap;
import java.util.Map;

/**
 * Created by patrickconreaux on 1/11/16.
 */
public class Message {
    private String name;

    private Map<User, Boolean> userPerms = new HashMap<>();

    public Message(String name) {
        this.name = name;
    }

    public boolean getAddRecipientAllowed(User recipient) {
        return userPerms.get(recipient);
    }

    public void setAddRecipientAllowed(User recipient, boolean isAllowed) {
        userPerms.put(recipient, isAllowed);
    }
}
