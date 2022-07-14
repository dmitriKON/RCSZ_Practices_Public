package backend;

import org.json.simple.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;

public class UserService {
    public DatabaseManager db;

    public UserService(DatabaseManager db) {
        this.db = db;
    }

    public User get_user_by_username(String username) throws SQLException {
        return this.db.get_user_by_username(username);
    }

    public User get_user_by_id(int id) throws SQLException {
        return this.db.get_user_by_id((int) id);
    }

    public boolean verify_login(JSONObject user_json) throws SQLException, NoSuchAlgorithmException {
        String username = (String) user_json.get("username");
        String password = (String) user_json.get("password");
        User user = this.get_user_by_username(username);
        return Objects.equals(user.getPassword(), PasswordEncryptor.encrypt_password(password));
    }
}

