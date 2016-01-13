import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by patrickconreaux on 1/11/16.
 */
public class RecipientTest {


    Recipient rec;

    @Mock
    RecipientMgr mockMgr;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        rec = new Recipient(mockMgr);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddRecipientToAllMessages() throws Exception {
        User user = new User("admin");
        user.setHasPermissionToModifyRecipients(true);
        Message goodMessage = new Message("Test 1");
        Message badMessage = new Message("Test 2");
        goodMessage.setAddRecipientAllowed(user, true);
        badMessage.setAddRecipientAllowed(user, false);
        ArrayList<Message> messages = new ArrayList<Message>();
        messages.add(goodMessage);
        messages.add(badMessage);
        // Assert that we pass two Messages into the function
        assertEquals(2, messages.size());
        ArrayList<Message> result = rec.addRecipientToAllMessages(messages, user, user);
        // Assert that only one Message failed to be updated
        assertEquals(1, result.size());
    }

    @Test
    public void testAddRecipientToAllMessagesCallsAddRecipient() throws Exception {
        User user = new User("admin");
        user.setHasPermissionToModifyRecipients(true);
        Message goodMessage = new Message("Test 1");
        Message badMessage = new Message("Test 2");
        goodMessage.setAddRecipientAllowed(user, true);
        badMessage.setAddRecipientAllowed(user, false);
        ArrayList<Message> messages = new ArrayList<Message>();
        messages.add(goodMessage);
        messages.add(badMessage);
        // Assert that we pass two Messages into the function
        assertEquals(messages.size(), 2);
        ArrayList<Message> result = rec.addRecipientToAllMessages(messages, user, user);
        // Assert that only one Message failed to be updated
        assertEquals(result.size(), 1);
        verify(mockMgr).addRecipient(any(User.class), any(List.class));
    }

    @Test
    public void testAddRecipientToAllMessagesCallsAddRecipientWithTheRecipient() throws Exception {
        User user = new User("admin");
        User recipient = new User("user");
        user.setHasPermissionToModifyRecipients(true);
        recipient.setHasPermissionToModifyRecipients(true);
        Message goodMessage = new Message("Test 1");
        Message badMessage = new Message("Test 2");
        goodMessage.setAddRecipientAllowed(recipient, true);
        badMessage.setAddRecipientAllowed(recipient, false);
        ArrayList<Message> messages = new ArrayList<Message>();
        messages.add(goodMessage);
        messages.add(badMessage);
        // Assert that we pass two Messages into the function
        assertEquals(messages.size(), 2);
        ArrayList<Message> result = rec.addRecipientToAllMessages(messages, user, recipient);
        // Assert that only one Message failed to be updated
        assertEquals(result.size(), 1);
        // Verify that recipient is passed to addRecipient, not the user
        verify(mockMgr).addRecipient(same(recipient), any(List.class));
    }

    @Test
    public void testAddRecipientToAllMessagesDoesNotModifyIfUserNotPermitted() throws Exception {
        User user = new User("anonymous");
        User recipient = new User("user");
        // Set the user permission to false
        user.setHasPermissionToModifyRecipients(false);
        recipient.setHasPermissionToModifyRecipients(true);
        Message goodMessage = new Message("Test 1");
        Message badMessage = new Message("Test 2");
        goodMessage.setAddRecipientAllowed(recipient, true);
        badMessage.setAddRecipientAllowed(recipient, false);
        ArrayList<Message> messages = new ArrayList<Message>();
        messages.add(goodMessage);
        messages.add(badMessage);
        // Assert that we pass two Messages into the function
        assertEquals(2, messages.size());
        ArrayList<Message> result = rec.addRecipientToAllMessages(messages, user, recipient);
        // Assert that BOTH Messages failed to be updated due to insufficient permissions
        assertEquals(2, result.size());
    }
}