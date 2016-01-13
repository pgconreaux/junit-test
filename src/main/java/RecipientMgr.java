import java.util.List;

/**
 * Created by patrickconreaux on 1/11/16.
 */
public interface RecipientMgr {

    public void addRecipient(User currentUser, List<Message> successfulMessages);
}
