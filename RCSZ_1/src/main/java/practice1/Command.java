package practice1;

import java.util.Arrays;
import java.util.Objects;

public class Command {

    private CommandType commandId;
    private String userId;
    private byte[] commandBody;

    public Command(CommandType commandId, String userId, byte[] commandBody) {
        this.commandId = commandId;
        this.userId = userId;
        this.commandBody = commandBody;
    }

    public Command() {
    }

    public CommandType getCommandId() {
        return commandId;
    }

    public void setCommandId(CommandType commandId) {
        this.commandId = commandId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte[] getCommandBody() {
        return commandBody;
    }

    public void setCommandBody(byte[] coomandBody) {
        this.commandBody = coomandBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return commandId == command.commandId && Objects.equals(userId, command.userId) && Arrays.equals(commandBody, command.commandBody);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(commandId, userId);
        result = 31 * result + Arrays.hashCode(commandBody);
        return result;
    }

    @Override
    public String toString() {
        return "Command {" +
                "command id: " + commandId +
                ", user id: " + userId +
                ", command body: " + Arrays.toString(commandBody) + "}";
    }
}
