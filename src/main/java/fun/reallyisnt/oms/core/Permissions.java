package fun.reallyisnt.oms.core;

public enum Permissions {
    COMMANDS_MESSAGE("oms.commands.msg");

    private String node;

    Permissions(String s) {
        this.node = s;
    }

    public String getNode() {
        return node;
    }
}
