import java.util.List;


public class PokerSimulator {
	
	
	
	private PokerManager manager;
	private Table table;

	public static void main(String[] args) {
		PokerSimulator simulator = new PokerSimulator();
		
		simulator.setUpGame();
		
	}
	
	public void setUpGame() {
		manager = new PokerManager();
		table = new Table("testtable", 25,50);
		setUpPlayersForTable(5);
	}
	
	private void setUpPlayersForTable(int numOfPlayers) {
		for (int i = 1; i <= numOfPlayers; i++) {
			table.addPlayerToTable(new Player("p"+i, Player.PlayerType.NORMAL));
		}
		
	}
	
}
