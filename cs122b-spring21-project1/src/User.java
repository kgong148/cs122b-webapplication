/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class User {

    private final String username;
    public final String userId;
    public User(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }

}
