package cn.zmdo.magicunit;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
	private static final int SEED_SIZE = 4;
	
    @Override
    public void start(Stage stage) throws IOException {
    	
    	BorderPane borderPane = new BorderPane();
    	borderPane.setPadding(new Insets(0, 10, 10,10));
    	// 画布
    	PatternCanvas patternCanvas = new PatternCanvas(132, 132);
    	
    	// 种子显示
    	HBox seedBox = new HBox();
		Label seedLabel = new Label("种子");
		seedLabel.setPrefSize(30, 20);
		
		TextField seedField = new TextField();
		seedField.setPrefSize(220, 20);
		
		seedBox.getChildren().addAll(seedLabel, seedField);
		
		// 功能按钮
		VBox bottonsBox = new VBox();
		bottonsBox.setSpacing(12);
		bottonsBox.setPadding(new Insets(10, 10, 10,10) );
		
		Button randomGenerateButton = new Button("随机生成");
		randomGenerateButton.setPrefSize(110, 20);
		randomGenerateButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				String seed = RandomTool.getRandomKey(SEED_SIZE);
				seedField.setText( seed );
				patternCanvas.generatePattern(seed);
			}
		});
		
		Button newSeedButton = new Button("创建种子");
		newSeedButton.setPrefSize(110, 20);
		newSeedButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				String seed = RandomTool.getRandomKey(SEED_SIZE);
				seedField.setText( seed );
			}
		});
		
		Button generateButton = new Button("生成图案");
		generateButton.setPrefSize(110, 20);
		generateButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				patternCanvas.generatePattern(seedField.getText());
			}
		});
		
		Button copyButton = new Button("复制到剪切板");
		copyButton.setPrefSize(110, 20);
		copyButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent clipboardContent = new ClipboardContent();
				clipboardContent.put(DataFormat.PLAIN_TEXT, seedField.getText());
				clipboard.setContent(clipboardContent);
			}
		});
		
		bottonsBox.getChildren().addAll(
				randomGenerateButton,
				newSeedButton,
				generateButton,
				copyButton);
		
		// 设置布局
		borderPane.setCenter(patternCanvas);
		borderPane.setRight(bottonsBox);
		borderPane.setBottom(seedBox);
    	
        scene = new Scene(borderPane, 270, 180);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}