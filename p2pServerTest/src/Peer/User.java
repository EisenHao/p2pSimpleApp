package Peer;

// 客户端的 用户类

public class User {

    private String userName; //用户名
    private String password; //密码

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
        this.userName = null;
        this.password = null;
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

    @Override
    public String toString() {
        return "User [username=" + userName + ", password=" + password + "]";
    }
}
