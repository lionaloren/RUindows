package ruindows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ChRUmeApp {

	Scene scene;
	Stage mainStage;
    MediaPlayer videoPlayer;
    MediaPlayer audioPlayer;
    VBox websiteContent;
    TextField searchBar;
    Label label;
    StackPane contentPane;
    StackPane homeStack;

    static final Set<String> usedFileNames = new HashSet<>();
    static final List<String> stockImages = new ArrayList<>();
    static final Map<String, String> savedImageMappings = new HashMap<>();

    String currentFileName = null;
    
    public ChRUmeApp(StackPane homeStack) {
        this.homeStack = homeStack;

        mainStage = new Stage();
        mainStage.setTitle("ChRUme");

        BorderPane root = new BorderPane();
        searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setPrefWidth(350);
        searchBar.setOnMouseClicked(e -> loadWebsite(searchBar.getText().trim()));
        searchBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loadWebsite(searchBar.getText().trim());
            }
        });

        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> loadWebsite(searchBar.getText().trim()));

        HBox topBar = new HBox(searchBar, searchBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setStyle("-fx-border-color: transparent;");

        websiteContent = new VBox();
        websiteContent.setStyle("-fx-padding: 0 10 10 10;");

        root.setTop(topBar);
        root.setCenter(websiteContent);

        loadWebsite("");

        scene = new Scene(root, 700, 400);
        scene.getRoot().setStyle("-fx-background-radius: 10 10 0 0; -fx-background-insets: 0;");

        mainStage.setScene(scene);
        mainStage.centerOnScreen();
        mainStage.setOnCloseRequest(e -> stopMedia());
        mainStage.show();
    }

    private void stopMedia() {
        if (videoPlayer != null) videoPlayer.pause();
        if (audioPlayer != null) audioPlayer.stop();
    }

    private HBox createHeader(String logoPath, String titleText) {
        ImageView logo = new ImageView(new Image(getClass().getResource(logoPath).toExternalForm()));
        logo.setFitHeight(20);
        logo.setPreserveRatio(true);

        Label title = new Label(titleText);
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20");

        HBox header = new HBox(5, logo, title);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private BorderPane createWebsitePane(HBox header, VBox vbox) {
        BorderPane pane = new BorderPane();
        pane.setTop(header);
        pane.setCenter(vbox);
        BorderPane.setAlignment(header, Pos.TOP_LEFT);
        return pane;
    }

    private void loadWebsite(String input) {
        stopMedia();
        websiteContent.getChildren().clear();

        switch (input) {
            case "":
                break;
            case "RUtube.net":
                loadRUtube();
                break;
            case "RUtify.net":
                loadRUtify();
                break;
            case "stockimages.net":
                loadStockImages();
                break;
            default:
                VBox error = new VBox();
                error.setAlignment(Pos.CENTER);
                Label notFound = new Label("This site can't be reached");
                notFound.setStyle("-fx-font-weight: bold; -fx-font-size: 30;");
                Label domainLabel = new Label(input + " does not exist, try checking your spelling");
                domainLabel.setStyle("-fx-font-size: 15;");
                error.getChildren().addAll(notFound, domainLabel);
                websiteContent.getChildren().add(error);
                websiteContent.setStyle("-fx-background-color: transparent;");
                websiteContent.setAlignment(Pos.CENTER);
                break;
        }
    }

    private void loadRUtube() {
        HBox header = createHeader("youtube-logo.png", "RUtube");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefHeight(300);

        Media video = new Media(getClass().getResource("DiamondJack.mp4").toExternalForm());
        videoPlayer = new MediaPlayer(video);
        MediaView mediaView = new MediaView(videoPlayer);
        mediaView.setFitWidth(500);
        mediaView.setPreserveRatio(true);

        Button play = new Button("Play");
        Button pause = new Button("Pause");

        play.setOnAction(e -> videoPlayer.play());
        pause.setOnAction(e -> videoPlayer.pause());

        HBox controls = new HBox(play, pause);
        controls.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(mediaView, controls);

        websiteContent.getChildren().add(createWebsitePane(header, vbox));
        websiteContent.setStyle("-fx-background-color: #3d3d3d; -fx-padding: 10;");
    }

    private void loadRUtify() {
        HBox header = createHeader("spotify-logo.png", "RUtify");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefHeight(300);

        Media audio = new Media(getClass().getResource("PromQueen.mp3").toExternalForm());
        audioPlayer = new MediaPlayer(audio);

        Slider slider = new Slider();
        slider.setMaxWidth(400);
        slider.setStyle("-fx-background-color: transparent;");
        slider.setDisable(false);

        audioPlayer.setOnReady(() -> {
            slider.setMax(audioPlayer.getTotalDuration().toSeconds());
        });

        audioPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!slider.isValueChanging()) {
                slider.setValue(newTime.toSeconds());
            }
        });

        slider.setOnMousePressed(event -> audioPlayer.pause());
        slider.setOnMouseReleased(event -> {
            audioPlayer.seek(Duration.seconds(slider.getValue()));
            audioPlayer.play();
        });

        Button play = new Button("Play");
        Button pause = new Button("Pause");

        play.setOnAction(e -> {
            if (audioPlayer.getStatus() == MediaPlayer.Status.STOPPED || 
                audioPlayer.getCurrentTime().greaterThanOrEqualTo(audioPlayer.getTotalDuration())) {
                audioPlayer.seek(Duration.ZERO);
            }
            audioPlayer.play();
        });

        pause.setOnAction(e -> audioPlayer.pause());

        HBox controls = new HBox(10, play, pause);
        controls.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(slider, controls);

        websiteContent.getChildren().add(createWebsitePane(header, vbox));
        websiteContent.setStyle("-fx-background-color: black; -fx-padding: 10;");
    }

    private void loadStockImages() {
        VBox content = new VBox(10);
        content.setAlignment(Pos.TOP_CENTER);

        addImageWithDownload(content, "cat-image1.jpg", "orangecat.jpg");
        addImageWithDownload(content, "cat-image2.jpg", "graycat.jpg");
        addImageWithDownload(content, "cat-image3.jpeg", "blackcat.jpg");
        addImageWithDownload(content, "cat-image4.jpeg", "graycat.jpg");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: black;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        websiteContent.getChildren().clear();
        websiteContent.getChildren().add(scrollPane);
        websiteContent.setStyle("-fx-padding: 0;");
    }

    private void addImageWithDownload(VBox container, String imageFile, String defaultName) {
        Image image = new Image(getClass().getResource(imageFile).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(420);
        imageView.setFitHeight(420);

        Button download = new Button("Download");
        download.setOnAction(e -> {
        	TextInputDialog renameDialog = new TextInputDialog(defaultName);
        	renameDialog.setTitle("Save file");
        	renameDialog.setHeaderText("Save file");
        	renameDialog.setContentText("Rename file:");
        	
            TextInputDialog dialog = new TextInputDialog(defaultName);
            dialog.setTitle("Save file");
        	dialog.setHeaderText("Save file");
        	dialog.setContentText("Rename file:");

            dialog.showAndWait().ifPresent(fileName -> {
                if (!fileName.toLowerCase().endsWith(".jpg")) {
                    fileName += ".jpg";
                }

                String baseName = fileName.substring(0, fileName.length() - 4);
                if (!isAlphanumeric(baseName)) {
                	showError("File name is invalid", "File name must be alphanumeric!");
                    return;
                }

                for (String name : usedFileNames) {
                    if (name.equalsIgnoreCase(fileName)) {
                    	showError("File with that name already exists!", "A file with that name has already been made");
                        return;
                    }
                }
                
                usedFileNames.add(fileName);
                currentFileName = fileName;
                stockImages.add(fileName);
                savedImageMappings.put(fileName, imageFile);

                addImageShortcutToDesktop(fileName, image);
            });
        });

        HBox row = new HBox(20, imageView, download);
        row.setAlignment(Pos.CENTER);
        container.getChildren().add(row);
    }

    private void addImageShortcutToDesktop(String filename, Image imageIcon) {
        if (homeStack == null) return;

        ImageView icon = new ImageView(imageIcon);
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        icon.setStyle("-fx-cursor: hand;");
        icon.setOnMouseClicked(e -> {
            String originalImage = savedImageMappings.get(filename);
            if (originalImage != null) {
                new PhotoEditorApp(filename, originalImage);
            }
        });
        
        label = new Label(filename);
        label.setPrefWidth(icon.getFitWidth());
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-text-fill: black;");

        label.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                TextInputDialog renameDialog = new TextInputDialog(label.getText());
                renameDialog.setTitle("Save file");
            	renameDialog.setHeaderText("Save file");
            	renameDialog.setContentText("Rename file:");

                renameDialog.showAndWait().ifPresent(newName -> {
                    if (!newName.toLowerCase().endsWith(".jpg")) {
                        newName += ".jpg";
                    }

                    String baseName = newName.substring(0, newName.length() - 4);                    
                    if (!isAlphanumeric(baseName)) {
                        showError("File name is invalid", "File name must be alphanumeric!");
                        return;
                    }

                    for (String name : stockImages) {
                        if (name.equalsIgnoreCase(newName)) {
                            showError("File with that name already exists!", "A file with that name has already been made");
                            return;
                        }
                    }

                    stockImages.remove(currentFileName);
                    stockImages.add(newName);
                    savedImageMappings.put(newName, savedImageMappings.remove(currentFileName));
                    
                    PhotoEditorApp.renameVirtualFile(currentFileName, newName);
                    PhotoEditorApp.updateOpenWindowFilename(currentFileName, newName);
                    
                    currentFileName = newName;
                    label.setText(newName);
                    mainStage.setTitle(newName);

                    String finalNewName = newName;
                    icon.setOnMouseClicked(event -> {
                        String originalImage = savedImageMappings.get(finalNewName);
                        if (originalImage != null) {
                            new PhotoEditorApp(finalNewName, originalImage);
                        }
                    });
                });
            }
        });

        VBox shortcut = new VBox(5, icon, label);
        shortcut.setUserData("chrome-shortcut");
        shortcut.setAlignment(Pos.TOP_LEFT);

        HBox shortcutColumns = null;
        for (Node node : homeStack.getChildren()) {
            if (node instanceof HBox) {
                shortcutColumns = (HBox) node;
                break;
            }
        }

        if (shortcutColumns == null) return;

        VBox targetColumn = null;
        for (Node node : shortcutColumns.getChildren()) {
            if (node instanceof VBox) {
                VBox column = (VBox) node;
                if (column.getChildren().size() < 5) {
                    targetColumn = column;
                    break;
                }
            }
        }

        if (targetColumn == null) {
            targetColumn = new VBox(30);
            shortcutColumns.getChildren().add(targetColumn);
        }

        targetColumn.getChildren().add(shortcut);
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private boolean isAlphanumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!(ch >= 'A' && ch <= 'Z') && !(ch >= 'a' && ch <= 'z') && !(ch >= '0' && ch <= '9')) {
                return false;
            }
        }
        return true;
    }
}
