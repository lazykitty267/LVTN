package entity;

import java.util.List;

/**
 * Created by lazyk on 7/24/2017.
 */

public class User {
    private Long id;

    private String name;

    private String userRole;

    private String username;

    private String password;

    private List<Long>  managerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getManagerId() {
        return managerId;
    }

    public void setManagerId(List<Long> managerId) {
        this.managerId = managerId;
    }
}
