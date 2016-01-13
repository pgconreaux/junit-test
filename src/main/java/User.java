/**
 * Created by patrickconreaux on 1/11/16.
 */
public class User {

    private String name;
    private boolean isAllowed;

    public User(String name) {
        this.name = name;
    }

    public boolean getHasPermissionToModifyRecipients() {
        return isAllowed;
    }

    public void setHasPermissionToModifyRecipients(boolean isAllowed) {
        this.isAllowed = isAllowed;
    }
}
