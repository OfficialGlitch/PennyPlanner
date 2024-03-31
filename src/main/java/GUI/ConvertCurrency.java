package GUI;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.chart.LineChart;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


// Imports for HTTP client and JSON processing
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import javafx.application.Platform;



import javafx.scene.chart.NumberAxis;

import javafx.scene.control.ListCell;

import javafx.scene.chart.CategoryAxis;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


import javafx.util.converter.DoubleStringConverter;






public class ConvertCurrency implements Initializable {

	@FXML
	public ComboBox<Currency> outputCurrencyComboBox;

	@FXML
	public ComboBox<Currency> inputCurrencyComboBox;

	@FXML
	public TextField outputAmount;

	@FXML
	public TextField inputAmount;

	@FXML
	private CheckBox autoCheckBoxButton;



	@FXML
	private Button convertButton;
@FXML
public Button refreshrate;


	public XYChart.Series<String, Number> seriesUSD = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesEUR = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesINR = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesGBP = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesCAD = new XYChart.Series<>();

	public XYChart.Series<String, Number> seriesAUD = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesBGN = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesBRL = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesCHF = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesCNY = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesDKK = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesHKD = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesHRK = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesILS = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesMYR = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesNZD = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesPLN = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesRON = new XYChart.Series<>();
	public XYChart.Series<String, Number> seriesSGD = new XYChart.Series<>();














	private java.util.Map<Currency, Double> latestRates;

	@FXML
	public LineChart<String, Number> lineChart;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;

	private final static DoubleStringConverter DOUBLE_STRING_CONVERTER = new DoubleStringConverter();

	private final static DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#0.000");

	private final ObservableList<Currency> currencies = FXCollections.observableArrayList();

	private final ChangeListener<String> inputAmountChangeListener = (observable, oldValue, newValue) -> convertAction(null);

	@FXML
	public void handlerefresh(ActionEvent event) {
		fetchCurrencyRates();
		lineChart.getData().clear();
		lineChart.getData().addAll(seriesUSD, seriesEUR, seriesGBP, seriesCAD, seriesAUD,seriesBGN,seriesBRL,seriesCHF,seriesCNY,
			seriesDKK,seriesHKD,seriesHRK,seriesILS,seriesMYR,seriesNZD,seriesPLN,seriesRON,seriesSGD);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<Currency> currencies = FXCollections.observableArrayList(Currency.values());


		currencies.addAll(Currency.values());
		inputCurrencyComboBox.setItems(currencies);
		outputCurrencyComboBox.setItems(currencies);
		inputCurrencyComboBox.getSelectionModel().selectFirst();
		outputCurrencyComboBox.getSelectionModel().selectLast();
		autoCheckBoxButton.setSelected(false);
		inputCurrencyComboBox.setCellFactory(lv -> new ListCell<>() {
			@Override
			protected void updateItem(Currency item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "" : item.getDescription());
			}
		});

		outputCurrencyComboBox.setCellFactory(lv -> new ListCell<>() {
			@Override
			protected void updateItem(Currency item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "" : item.getDescription());
			}
		});

		// Optionally, ensure the selected item is displayed with the description
		inputCurrencyComboBox.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(Currency item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "" : item.getDescription());
			}
		});

		outputCurrencyComboBox.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(Currency item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "" : item.getDescription());
			}
		});


		inputCurrencyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			convertAction(null);
		});
		outputCurrencyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			convertAction(null);
		});
		fetchCurrencyRates();
		// Initialize series
		seriesUSD.setName("USD");
		seriesEUR.setName("EUR");
		seriesINR.setName("INR");
		seriesGBP.setName("GBP");
		seriesCAD.setName("CAD");
		seriesAUD.setName("AUD");
		seriesBGN.setName("BGN");
		seriesBRL.setName("BRL");
		seriesCHF.setName("CHF");
		seriesCNY.setName("CNY");
		seriesDKK.setName("DKK");
		seriesHKD.setName("HKD");
		seriesHRK.setName("HRK");
		seriesILS.setName("ILS");
		seriesMYR.setName("MYR");
		seriesNZD.setName("NZD");
		seriesPLN.setName("PLN");
		seriesRON.setName("RON");
		seriesSGD.setName("SGD");


		// Add series to chart

		lineChart.getData().addAll(seriesUSD, seriesEUR, seriesGBP, seriesCAD, seriesAUD,seriesBGN,seriesBRL,seriesCHF,seriesCNY,
			seriesDKK,seriesHKD,seriesHRK,seriesILS,seriesMYR,seriesNZD,seriesPLN,seriesRON,seriesSGD);
	}
	public Map<String, Double> conversionRates = new HashMap<>();

	public void fetchCurrencyRates() {
		String apiKey = "fca_live_c8FaXQlw6z4eOjp2EH8e0ZiBpjxcGAa26NFWtJQL";
		String baseUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=" + apiKey;


			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl)).build();

			client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(responseBody -> {
					try {
						JSONObject json = new JSONObject(responseBody);
						JSONObject rates = json.getJSONObject("data");
						Iterator<String> keys = rates.keys();

						while (keys.hasNext()) {
							String key = keys.next();
							double rate = rates.getDouble(key);
							conversionRates.put(key, rate);
							Platform.runLater(() -> {
								seriesUSD.getData().add(new XYChart.Data<>("USD", conversionRates.get("USD")));
								seriesEUR.getData().add(new XYChart.Data<>("EUR", conversionRates.get("EUR")));
								seriesINR.getData().add(new XYChart.Data<>("INR", conversionRates.get("INR")));
								seriesGBP.getData().add(new XYChart.Data<>("GBP", conversionRates.get("GBP")));
								seriesCAD.getData().add(new XYChart.Data<>("CAD", conversionRates.get("CAD")));
								seriesAUD.getData().add(new XYChart.Data<>("AUD", conversionRates.get("AUD")));
								seriesBGN.getData().add(new XYChart.Data<>("BGN", conversionRates.get("BGN")));
								seriesBRL.getData().add(new XYChart.Data<>("BRL", conversionRates.get("BRL")));
								seriesCHF.getData().add(new XYChart.Data<>("CHF", conversionRates.get("CHF")));
								seriesCNY.getData().add(new XYChart.Data<>("CNY", conversionRates.get("CNY")));
								seriesDKK.getData().add(new XYChart.Data<>("DKK", conversionRates.get("DKK")));
								seriesHKD.getData().add(new XYChart.Data<>("HKD", conversionRates.get("HKD")));
								seriesHRK.getData().add(new XYChart.Data<>("HRK", conversionRates.get("HRK")));
								seriesILS.getData().add(new XYChart.Data<>("ILS", conversionRates.get("ILS")));
								seriesMYR.getData().add(new XYChart.Data<>("MYR", conversionRates.get("MYR")));
								seriesNZD.getData().add(new XYChart.Data<>("NZD", conversionRates.get("NZD")));
								seriesPLN.getData().add(new XYChart.Data<>("PLN", conversionRates.get("PLN")));
								seriesRON.getData().add(new XYChart.Data<>("RON", conversionRates.get("RON")));
								seriesSGD.getData().add(new XYChart.Data<>("SGD", conversionRates.get("SGD")));



							});
						}

						// Signal UI or logs that rates are updated
						System.out.println("Rates updated: " + conversionRates);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}).join();
		}



	@FXML
	public void convertAction(ActionEvent actionEvent) {
		Currency inputCurrency = inputCurrencyComboBox.getValue();
		Currency outputCurrency = outputCurrencyComboBox.getValue();
		String inputText = inputAmount.getText().trim(); // Trim to remove leading/trailing whitespace.

		// If automatic conversion is enabled and input is empty, quietly return without error.
		if (autoCheckBoxButton.isSelected() && inputText.isEmpty()) {
			return;
		}

		if (!inputText.isEmpty() && !isNumeric(inputText)) {
			showAlert("Conversion Error", "Please enter a proper numeric value.");
			return; // Exit the method early if input is not numeric
		}

		double inputValue = 0;
		if (!inputText.isEmpty()) {
			inputValue = Double.parseDouble(inputText);
		}

		if (inputValue < 0) {
			showAlert("Conversion Error", "Please enter a positive value.");
			return;
		}

		// Proceed with the conversion if the input is valid...
		if (conversionRates.containsKey(inputCurrency.getCode()) && conversionRates.containsKey(outputCurrency.getCode())) {
			double rate = conversionRates.get(outputCurrency.getCode()) / conversionRates.get(inputCurrency.getCode());
			double convertedValue = inputValue * rate;
			outputAmount.setText(String.format("%.2f", convertedValue));
		} else {
			showAlert("Conversion Error", "Real-time conversion rates are currently unavailable.");
		}
	}







	private void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	@FXML
	private void clearAction(ActionEvent actionEvent) {
		inputAmount.setText("");
		outputAmount.setText("");
	}

	@FXML
	private void switchAutomaticConversion(ActionEvent actionEvent) {
		if (autoCheckBoxButton.isSelected()) {
			convertButton.setDisable(true);
			inputAmount.textProperty().addListener(inputAmountChangeListener);
			convertAction(null);
		} else {
			convertButton.setDisable(false);
			inputAmount.textProperty().removeListener(inputAmountChangeListener);
		}
	}

	private static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	//these methods are for testing purposes
	//This method returns the current conversion rates stored in the conversionRates map.
	public Map<String, Double> getConversionRates() {
		return this.conversionRates;
	}
	//This method performs a currency conversion from one currency to another based on the provided conversion rates.
	public double convertAmount(double amount, String fromCurrencyCode, String toCurrencyCode) {
		double rate = conversionRates.get(toCurrencyCode) / conversionRates.get(fromCurrencyCode);
		return amount * rate;
	}

}




