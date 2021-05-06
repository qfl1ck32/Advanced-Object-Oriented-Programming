package Services;

import JSONParser.JSONParser;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;
import Audit.Audit;

import enumerations.SignupResponse;

public class UserSignUpService {
    public static SignupResponse registerUser(String username, String email, String password) {

        if(Stream.of(username, email, password).anyMatch(String::isEmpty)) {
            return SignupResponse.EMPTY_FIELDS;
        }

        JSONParser currentUsersParser = new JSONParser("src/assets/users.json");
        String currentID;

        while ((currentID = currentUsersParser.nextKey()) != null) {
            JSONParser anotherUserParser = currentUsersParser.getJSONParser(currentID);

            if (anotherUserParser.getString("username").equalsIgnoreCase(username)) {
                return SignupResponse.USERNAME_ALREADY_USED;
            }

            if (anotherUserParser.getString(email).equalsIgnoreCase(email)) {
                return SignupResponse.EMAIL_ALREADY_USED;
            }
        }

        JSONParser userData = new JSONParser();

        String uuid = UUID.randomUUID().toString().replace("-", "");

        userData.put("username", username);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("address", "");
        userData.put("deliveryIDS", Collections.emptyList());
        userData.put("cart", Collections.emptyList());
        userData.put("isLoggedIn", false);

        currentUsersParser.put(uuid, userData);

        currentUsersParser.saveModifications();

        Audit.getInstance().userRegistered();

        return SignupResponse.SUCCESS;
    }
}
