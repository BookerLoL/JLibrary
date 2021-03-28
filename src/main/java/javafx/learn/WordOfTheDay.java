package main.javafx.learn;

import java.time.LocalDate;
import java.util.Calendar;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javafx.application.Application;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

//https://stackoverflow.com/questions/27047447/customized-context-menu-on-javafx-webview-webengine
//http://www.java2s.com/Tutorials/Java/JavaFX/1500__JavaFX_WebEngine.htm
public class WordOfTheDay extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		WebView browser = new WebView();
		browser.setContextMenuEnabled(false);

		addCustomContextMenu(browser);
		WebEngine webEngine = browser.getEngine();
		gatherAllWordOfTheDay(webEngine);

		Scene scene = new Scene(browser);
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
	}

	private void addCustomContextMenu(WebView webView) {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem about = new MenuItem("About");
		about.setOnAction(e -> System.out.println("About"));
		contextMenu.getItems().add(about);

		webView.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				contextMenu.show(webView, e.getScreenX(), e.getScreenY());
			} else {
				contextMenu.hide();
			}
		});
	}

	private void gatherAllWordOfTheDay(WebEngine webEngine) {
		String urlStart = "https://www.merriam-webster.com/word-of-the-day/";
		LocalDate todaysDate = LocalDate.now();
		Calendar current = new Calendar.Builder()
				.setDate(todaysDate.getYear(), todaysDate.getMonthValue(), todaysDate.getDayOfMonth()).build();
		Calendar past = new Calendar.Builder().setDate(2014, 8, 22).build();

		webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == State.SUCCEEDED) {
				org.w3c.dom.Document xmlDom = webEngine.getDocument();

				NodeList nodes = xmlDom.getElementsByTagName("H1");

				for (int i = 0; i < nodes.getLength(); i++) {
					System.out.println(current.get(Calendar.YEAR) + "-" + current.get(Calendar.MONTH) + "-"
							+ current.get(Calendar.DAY_OF_MONTH) + "\t" + nodes.item(i).getTextContent());
				}

				if (current.after(past)) {
					current.add(Calendar.DAY_OF_MONTH, -1);
					webEngine.load(urlStart + current.get(Calendar.YEAR) + "-"
							+ (current.get(Calendar.MONTH) < 10 ? "0" + current.get(Calendar.MONTH)
									: current.get(Calendar.MONTH))
							+ "-" + (current.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + current.get(Calendar.DAY_OF_MONTH)
									: current.get(Calendar.DAY_OF_MONTH)));
				}
			}
		});

		webEngine.load(urlStart + urlStart + current.get(Calendar.YEAR) + "-"
				+ (current.get(Calendar.MONTH) < 10 ? "0" + current.get(Calendar.MONTH) : current.get(Calendar.MONTH))
				+ "-" + (current.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + current.get(Calendar.DAY_OF_MONTH)
						: current.get(Calendar.DAY_OF_MONTH)));
	}

}
