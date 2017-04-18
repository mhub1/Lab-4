package pkgPokerBLL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Table implements Serializable {

	private UUID TableID;

	// Change this from ArrayList to HashMap.
	private HashMap<UUID, Player> TablePlayers = new HashMap<UUID, Player>();

	public Table() {
		super();
		TableID = UUID.randomUUID();
	}

	public Table AddPlayerToTable(Player p) {
		TablePlayers.put(UUID.randomUUID(), p);
		return this;
	}

	public Table RemovePlayerFromTable(Player p) {
		TablePlayers.remove(p);
		return this;
	}

	public HashMap<UUID, Player> GetTablePlayers() {
		return TablePlayers;
	}
}
