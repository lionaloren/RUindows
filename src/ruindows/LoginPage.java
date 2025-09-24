package ruindows;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends Application {

	Scene scene;
	Stage mainStage;
	ImageView background, profile;
	Label welcome, wrongPass;
	PasswordField password;
	Button loginBtn;
	HBox loginBox;
	VBox completeBox;

	private static final String myPassword = "123";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		mainStage = primaryStage;

		background = new ImageView(new Image(getClass().getResource("nature.jpg").toExternalForm()));
		background.setFitWidth(1200);
		background.setFitHeight(600);

		profile = new ImageView(new Image(getClass().getResource("default_profile_pic.png").toExternalForm()));
		profile.setFitWidth(250);
		profile.setFitHeight(250);

		welcome = new Label("Welcome to RU24-2!");
		welcome.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold");

		wrongPass = new Label("Wrong password!");
		wrongPass.setStyle("-fx-text-fill: red; -fx-font-size: 15px; -fx-font-weight: bold");
		wrongPass.setVisible(false);

		password = new PasswordField();
		password.setPromptText("Enter password");
		password.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-radius: 5; -fx-border-width: 1.5; -fx-text-fill: white; -fx-padding: 8 12; -fx-font-size: 14;");

		loginBtn = new Button("Login");
		loginBtn.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-radius: 5; -fx-border-width: 1.5; -fx-text-fill: white; -fx-padding: 8 12; -fx-font-size: 14;");

		loginBtn.setOnAction(e -> {
			if (password.getText().equals(myPassword)) {
				wrongPass.setVisible(false);
				HomePage home = new HomePage(primaryStage, this);
				home.show();
			} else {
				wrongPass.setVisible(true);
			}
		});

		loginBox = new HBox(10, password, loginBtn);
		loginBox.setAlignment(Pos.CENTER);
		loginBox.setPadding(new Insets(20));
		loginBox.setMaxWidth(400);

		completeBox = new VBox(10, profile, welcome, loginBox, wrongPass);
		completeBox.setAlignment(Pos.CENTER);
		completeBox.setPadding(new Insets(20));
		VBox.setMargin(loginBox, new Insets(-20, 0, 0, 0));
		VBox.setMargin(wrongPass, new Insets(-20, 0, 0, 0));

		StackPane root = new StackPane();
		scene = new Scene(root, 1200, 600);

		root.getChildren().addAll(background, completeBox);
		StackPane.setAlignment(completeBox, Pos.CENTER);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
