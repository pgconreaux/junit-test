# Code Analysis/ Whitebox Testing

The JUnit tests included in the project may be executed with the following command:

    ./gradlew test
        
The three bugs identified are the following:

## Bug #1
### Description

The `if` statement checks that `successfulMessages` is empty to allow adding a recipient to the messages.  
However, the logic should check that 'successfulMessages` is _non-empty_.

### Test Escape

The current test does not verify that the `addRecipient` call within the conditional is executed.

### Defective Code
 
    if (successfulMessages.isEmpty()) {

    
### Fixed Code

    if (!successfulMessages.isEmpty()) {

### New Test

A test for this bug would need to verify that `addRecipient` is called when `successfulMessages` is non-empty thus satisfying the `if` condition.
This can be accomplished by using a mock object for the `recipientManager` member and verifying that is invoked.
The following is a test case that exhibits this verification:

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
        
## Bug #2

### Description 

The `addRecipient` call passes the `currentUser`.  
However, it should pass the `recipient` user to be added
as the recipient to the `successfulMessages`.

### Test Escape 

The current test does not verify that the `addRecipient` call within the conditional is executed, nor does it
verify that the correct user is passed as the recipient.

### Defective Code
  
    recipientManager.addRecipient(currentUser, successfulMessages);
     
### Fixed Code
 
    recipientManager.addRecipient(recipient, successfulMessages);
    
### New Test

A test for this bug would need to verify that the `recipient` user is passed as an argument to `addRecipient`.
This test would require distinct `user` and `recipient` user variables.  
A mock object could be used for the `recipientManager` member and verify that the provided `user` argument
is the `recipient` object.
The following is a test case that exhibits this verification:
  
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

## Bug #3

### Description 

The `canReceiveMessage` method should return false as the default.
 
### Test Escape

The current test always satisfies the conditional and doesn't cover the case where the `if` statement is `false`.
 
### Defective Code
  
    if (currentUser.equals(recipient) || currentUser.getHasPermissionToModifyRecipients()) {
        return message.getAddRecipientAllowed(recipient);
    }
    return true;
     
### Fixed Code
 
   if (currentUser.equals(recipient) || currentUser.getHasPermissionToModifyRecipients()) {
        return message.getAddRecipientAllowed(recipient);
   }
   return false;

    
### New Test

A test for this bug would need to introduce a user that differs from the recipient and does not have permissions
to modify recipients. This would cause the `if` condition to be false and return `false`. An assertion is introduced
to verify that size of the returned `failedMessages` list is equivalent to the total number of messages since the
user did not have permissions to add a recipient.
   
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
   