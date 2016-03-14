package databeans;

public class MessageWrapper {
    String Message;

    public MessageWrapper() {
        Message = "";
    }

    public MessageWrapper(String m) {
        Message = m;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
