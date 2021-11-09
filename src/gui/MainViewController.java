package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}
	
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	//CARREGA UMA TELA - synchronized serve para impedir que o método seja interrompido
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			//PEGANDO A REFERENCIA DA CENA
			Scene mainScene = Main.getMainScene();
			
			//GUARDA A REFERENCIA DO VBox DA TELA PRINCIPAL
			//GETROOT PEGA O PRIMEIRO ELEMENTO DA VIEW
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//PEGA O PRIMEIRO FILHO NA VBox QUE NESTE CASO É O MENUBAR
			Node mainMenu = mainVBox.getChildren().get(0);
			
			//EXCLUI TODOS OS FILHOS DA MAINVBOX
			mainVBox.getChildren().clear();
			//ADICIONA O MENUBAR, QUE FOI GUARDADO NA MAINMENU
			mainVBox.getChildren().add(mainMenu);
			//ADICIONA TODOS OS FILHOS DA TELA QUE VAI SER CARREGADA "absoluteName"
			mainVBox.getChildren().addAll(newVBox.getChildren());
	
			//EXECUTAM A FUNÇÃO PASSADA COMO ARGUMENTO
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		}catch(IOException e) {
			Alerts.showAlert("IO EXCEPTION", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
