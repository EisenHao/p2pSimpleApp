package sendFile;
import Peer.User;

//抽象的 消息虚拟类， 实现Serializable接口

public class Message {
    protected User fromWho;
    protected String text;

    public User getFromWho() {
        return fromWho;
    }

    public String getText() {
        return text;
    }

    //构造函数， 构造一条消息
    protected Message(User fromWho, String text) {
        this.fromWho = fromWho;
        this.text = text;
    }
}
