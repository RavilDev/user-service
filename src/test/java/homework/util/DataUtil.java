package homework.util;

import homework.dto.request.UserRequestTo;
import homework.dto.response.UserResponseTo;
import homework.entity.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DataUtil {
    public static final long ID = 1L;
    public static final String NAME = "Ravil";
    public static final int AGE = 23;
    public static final String EMAIL = "eagle@app.ru";
    public static final Timestamp CREATED = new Timestamp(System.currentTimeMillis());
    public static final long COUNT = 6;

    private DataUtil() {}

    public static List<UserResponseTo> getUserResponseToList() {
        List<UserResponseTo> userResponseToList = new ArrayList<>();
        for (int i = 1; i <= COUNT; i++) {
            UserResponseTo userResponseTo;
            if (i == 1) {
                userResponseTo = getUserResponseTo();
            } else {
                userResponseTo = new UserResponseTo();
                userResponseTo.setId((long) i);
                userResponseTo.setName(NAME + i);
                userResponseTo.setAge(AGE + i);
                userResponseTo.setEmail(EMAIL + i);
                userResponseTo.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            }
            userResponseToList.add(userResponseTo);
        }
        return userResponseToList;
    }

    public static UserResponseTo getUserResponseTo() {
        UserResponseTo userResponseTo = new UserResponseTo();
        userResponseTo.setId(ID);
        userResponseTo.setName(NAME);
        userResponseTo.setAge(AGE);
        userResponseTo.setEmail(EMAIL);
        userResponseTo.setCreatedAt(CREATED);
        return userResponseTo;
    }

    public static UserRequestTo getUserRequestTo() {
        UserRequestTo userRequestTo = new UserRequestTo();
        userRequestTo.setName(NAME);
        userRequestTo.setAge(AGE);
        userRequestTo.setEmail(EMAIL);
        return userRequestTo;
    }

    public static User getUser() {
        User user = new User();
        user.setId(ID);
        user.setName(NAME);
        user.setAge(AGE);
        user.setEmail(EMAIL);
        user.setCreatedAt(CREATED);
        return user;
    }
}
