package pkgPoker.app.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import pkgPoker.app.MainApp;
import pkgPokerEnum.eAction;
import pkgPokerEnum.eGame;
import pkgPokerBLL.Action;
import pkgPokerBLL.GamePlay;
import pkgPokerBLL.Player;
import pkgPokerBLL.Table;

public class PokerTableController implements Initializable {

	// Reference to the main application.
	private MainApp mainApp;

	public PokerTableController() {
	}

	@FXML
	private Label lblWinningPlayer;
	@FXML
	private Label lblWinningHand;

	@FXML
	private Label lblPlayerPos1;
	@FXML
	private Label lblPlayerPos2;

	@FXML
	private Label dishPlayer;

	@FXML
	private ImageView imgViewDealerButtonPos1;
	@FXML
	private ImageView imgViewDealerButtonPos2;

	@FXML
	private BorderPane OuterBorderPane;

	@FXML
	private Label lblNumberOfPlayers;
	@FXML
	private TextArea txtPlayerArea;

	@FXML
	private Button btnStartGame;
	@FXML
	private ToggleButton btnPos1SitLeave;
	@FXML
	private ToggleButton btnPos2SitLeave;


	@FXML
	private Label lblPos1Name;
	@FXML
	private Label lblPos2Name;


	@FXML
	private HBox hBoxDeck;

	@FXML
	private HBox hboxP1Cards;
	@FXML
	private HBox hboxP2Cards;

	@FXML
	private HBox hboxCommunity;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handlePlay() {
	}

	@FXML
	public void GetGameState() {
	}

	//TODO: Lab #4 - Complete (fix) setiPlayerPosition
	public void btnSitLeave_Click(ActionEvent event) {
		Action action = null;
		Button btn = (Button)event.getSource();
		if(btn.equals(btnPos1SitLeave)){
			if (mainApp.getPlayer().getiPlayerPosition() == 1){
				action = new Action(eAction.Leave, mainApp.getPlayer());
			}
			else{
				action = new Action(eAction.Sit, mainApp.getPlayer());
				mainApp.getPlayer().setiPlayerPosition(1);
			}		
			
		}
		else if(btn.equals(btnPos2SitLeave)){
			if (mainApp.getPlayer().getiPlayerPosition() == 2){
				action = new Action(eAction.Leave, mainApp.getPlayer());
			}
			else{
				action = new Action(eAction.Sit, mainApp.getPlayer());
				mainApp.getPlayer().setiPlayerPosition(2);
			}			
		}

		// Send the Action to the Hub
		mainApp.messageSend(action);
	}

	public void MessageFromMainApp(String strMessage) {
		System.out.println("Message received by PokerTableController: " + strMessage);
	}

	private Label getPlayerLabel(int iPosition) {
		switch (iPosition) {
		case 1:
			return lblPlayerPos1;
		case 2:
			return lblPlayerPos2;
		default:
			return null;
		}
	}

	private ToggleButton getSitLeave(int iPosition) {
		switch (iPosition) {
		case 1:
			return btnPos1SitLeave;
		case 2:
			return btnPos2SitLeave;
		default:
			return null;
		}
	}

	private HBox getCardHBox(int iPosition) {
		switch (iPosition) {
		case 0:
			return hboxCommunity;
		case 1:
			return hboxP1Cards;
		case 2:
			return hboxP2Cards;
 
		default:
			return null;
		}

	}

	//TODO: Lab #4 Complete the implementation
	public void Handle_TableState(Table HubPokerTable) {
		HashMap<UUID, Player> playerList = HubPokerTable.GetTablePlayers();
		for(int i = 1; i <= 2; i++){ //Number of positions
			for(int j = 0; j < playerList.size(); j++)
				if(playerList.get(j).getiPlayerPosition() == i){
					lblPos1Name.setText(playerList.get(j).getPlayerName());
					if(mainApp.getPlayer().getPlayerID().equals(playerList.get(j).getPlayerID())){
						if(i == 1)
							btnPos1SitLeave.setText("Leave");
						else if(i == 2)
							btnPos2SitLeave.setText("Leave");
					}
					else{
						if(i == 1)
							lblPlayerPos1.setText("-Taken-");
						else if(i == 2)
							lblPlayerPos2.setText("-Taken-");
					}
					i = 3; //Number of positions + 1
				}
		}
	}

	public void Handle_GameState(GamePlay HubPokerGame) {
		
	}

	private ImageView BuildImage(int iCardNbr) {
		String strImgPath;
		if (iCardNbr == 0) {
			strImgPath = "/img/b2fv.png";
		} else {
			strImgPath = "/img/" + iCardNbr + ".png";
		}

		ImageView i1 = new ImageView(new Image(getClass().getResourceAsStream(strImgPath), 75, 75, true, true));
		return i1;
	}

	@FXML
	void btnStart_Click(ActionEvent event) {
		// Start the Game
		Action act = new Action(eAction.StartGame, mainApp.getPlayer());

		// figure out which game is selected in the menu
		eGame gme = eGame.getGame(Integer.parseInt(mainApp.getRuleName().replace("PokerGame", "")));

		// Set the gme in the action
		act.seteGame(gme);

		// Send the Action to the Hub
		mainApp.messageSend(act);
	}

	@FXML
	void btnDeal_Click(ActionEvent event) {

		// Set the new Deal action
		Action act = new Action(eAction.Draw, mainApp.getPlayer());

		// Send the Action to the Hub
		mainApp.messageSend(act);

	}

	@FXML
	public void btnFold_Click(ActionEvent event) {
		Button btnFold = (Button) event.getSource();
		switch (btnFold.getId().toString()) {
		case "btnPlayer1Fold":
			// Fold for Player 1
			break;
		case "btnPlayer2Fold":
			// Fold for Player 2
			break;
		case "btnPlayer3Fold":
			// Fold for Player 3
			break;
		case "btnPlayer4Fold":
			// Fold for Player 4
			break;

		}
	}

	@FXML
	public void btnCheck_Click(ActionEvent event) {
		Button btnCheck = (Button) event.getSource();
		switch (btnCheck.getId().toString()) {
		case "btnPlayer1Check":
			// Check for Player 1
			break;
		case "btnPlayer2Check":
			// Check for Player 2
			break;
		case "btnPlayer3Check":
			// Check for Player 3
			break;
		case "btnPlayer4Check":
			// Check for Player 4
			break;
		}
	}

	private void FadeButton(Button btn) {
		FadeTransition ft = new FadeTransition(Duration.millis(3000), btn);
		ft.setFromValue(1.0);
		ft.setToValue(0.3);
		ft.setCycleCount(4);
		ft.setAutoReverse(true);

		ft.play();
	}

}