package fun.reallyisnt.oms.bungeecore.server.lobby;

import java.util.Comparator;

public class LobbySorter implements Comparator<LobbyWrapper> {

    /**
     * First attempts to sort by which one has less than half of the max slots,
     * if it is found that one is sorted first, otherwise it is sorted by player count
     * and if none of that can be handled it is then sorted by the server id
     */
    @Override
    public int compare(LobbyWrapper first, LobbyWrapper second) {
        if (first.getPlayerCount() < (first.getMaxPlayerCount() / 2) && second.getPlayerCount() >= (second.getMaxPlayerCount() / 2)) {
            return -1;
        }

        if (second.getPlayerCount() < (second.getMaxPlayerCount() / 2) && first.getPlayerCount() >= (first.getMaxPlayerCount() / 2)) {
            return 1;
        }

        if (first.getPlayerCount() < (first.getMaxPlayerCount() / 2)) {
            if (first.getPlayerCount() > second.getPlayerCount()) {
                return -1;
            } else if (second.getPlayerCount() > first.getPlayerCount()) {
                return 1;
            }
        } else {
            if (first.getPlayerCount() < second.getPlayerCount()) {
                return -1;
            } else if (second.getPlayerCount() < first.getPlayerCount()) {
                return 1;
            }
        }

        // Sort by ordinal first otherwise by string comparing
        try {
            String[] firstName = first.getName().split("-");
            String[] secondName = second.getName().split("-");
            if (Integer.parseInt(firstName[firstName.length-1]) < Integer.parseInt(secondName[secondName.length-1]))
                return -1;
            else if (Integer.parseInt(secondName[secondName.length-1]) < Integer.parseInt(firstName[firstName.length-1]))
                return 1;
        } catch (NumberFormatException ignored) { }

        return String.CASE_INSENSITIVE_ORDER.compare(first.getName(), second.getName());
    }
}
