package org.testgen.db.model;

import org.dizitart.no2.objects.Id;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    @Id
    private String name;
    private String userName;
    private String password;
    private UserType userType;
    private String authCheckUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getAuthCheckUser() {
        return authCheckUser;
    }

    public void setAuthCheckUser(String authCheckUser) {
        this.authCheckUser = authCheckUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, userName);
    }
}
