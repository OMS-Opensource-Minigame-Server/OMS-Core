package fun.reallyisnt.oms.common;

public enum Permissions {
    COMMANDS_MESSAGE("oms.commands.msg");

    private final String node;

    Permissions(String s) {
        this.node = s;
    }

    public String getNode() {
        return node;
    }
}
