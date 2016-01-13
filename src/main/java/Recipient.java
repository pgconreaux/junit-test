import java.util.ArrayList;

/**
 * Created by patrickconreaux on 1/11/16.
 */
public class Recipient {

    private RecipientMgr recipientManager;

    public Recipient(RecipientMgr mgr) {
        this.recipientManager = mgr;
    }

    /**
     * Adds a recipient to all of the supplied Messages.
     * <p>
     * If there is partial success, the Messages which we can modify will
     * be modified and the ones that cannot will be returned in an ArrayList.
     *
     * @param messages    the list of messages to update
     * @param currentUser the user to run the operation as
     * @param recipient   the recipient to add to the Messages
     * @return an ArrayList<Message> containing the Messages that could not be modified
     */
    public ArrayList<Message> addRecipientToAllMessages(final ArrayList<Message> messages, final User currentUser, final User recipient) {
        ArrayList<Message> successfulMessages = new ArrayList<Message>();
        ArrayList<Message> failedMessages = new ArrayList<Message>();

        for (Message message : messages) {
            if (canReceiveMessage(message, currentUser, recipient)) {
                successfulMessages.add(message);
            } else {
                failedMessages.add(message);
            }
        }
        /**
         * BUG #1:
         * if (successfulMessages.isEmpty()) {
         * Escape: Test doesn't check that addRecipient is called
         * Fix: should check for NOT empty in order to add recipients to successful messages
         * Test: if successfulMessages is non-empty, addRecipient is called
         */
        if (!successfulMessages.isEmpty()) {
            /**
             *  BUG #2:
             *  recipientManager.addRecipient(currentUser, successfulMessages);
             *  Escape: Test doesn't check that addRecipient is given the recipient user
             *  Fix: Should pass recipient not currentUser
             *  Test: when successfulMessages is non-empty, addRecipient is called with the recipient
             */
            recipientManager.addRecipient(recipient, successfulMessages);
        }
        return failedMessages;
    }

    private boolean canReceiveMessage(Message message, User currentUser, User recipient) {
        if (currentUser.equals(recipient) || currentUser.getHasPermissionToModifyRecipients()) {
            return message.getAddRecipientAllowed(recipient);
        }
        /**
         * BUG #3:
         * return true;
         * Escape: Test doesn't cover case where if statement is not satisfied
         * Fix: Should return false as the default
         * Test: A currentUser that is not the recipient AND does NOT have permissions returns false
         */
        return false;
    }
}
