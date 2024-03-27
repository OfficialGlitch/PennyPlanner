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



import models.Category;
import org.json.JSONObject;
import org.json.JSONException;

// Utility imports
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.converter.DoubleStringConverter;

import java.text.DecimalFormat;




public class ConvertCurrency implements Initializable {

	@FXML
	private ComboBox<Currency> outputCurrencyComboBox;

	@FXML
	private ComboBox<Currency> inputCurrencyComboBox;

	@FXML
	private TextField outputAmount;

	@FXML
	private TextField inputAmount;

	@FXML
	private CheckBox autoCheckBoxButton;

	@FXML
	private Button convertButton;
	private XYChart.Series<String, Number> seriesUSD = new XYChart.Series<>();
	private XYChart.Series<String, Number> seriesEUR = new XYChart.Series<>();
	private XYChart.Series<String, Number> seriesINR = new XYChart.Series<>();
	private XYChart.Series<String, Number> seriesGBP = new XYChart.Series<>();
	private XYChart.Series<String, Number> seriesCAD = new XYChart.Series<>();

	private XYChart.Series<String, Number> seriesAUD = new XYChart.Series<>();


	private java.util.Map<Currency, Double> latestRates; // Assume this is updated by your fetch method

	@FXML
	private LineChart<String, Number> lineChart;
	@FXML
	private CategoryAxis xAxis; // Ensure this matches your FXML definition
	@FXML
	private NumberAxis yAxis;

	private final static DoubleStringConverter DOUBLE_STRING_CONVERTER = new DoubleStringConverter();

	private final static DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#0.000");

	private final ObservableList<Currency> currencies = FXCollections.observableArrayList();

	private final ChangeListener<String> inputAmountChangeListener = (observable, oldValue, newValue) -> convertAction(null);



	// Assuming 'conversionRates' is a Map<String, Double> with currency codes as keys and conversion rates as values

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		currencies.addAll(Currency.values());
		inputCurrencyComboBox.setItems(currencies);
		outputCurrencyComboBox.setItems(currencies);
		inputCurrencyComboBox.getSelectionModel().selectFirst();
		outputCurrencyComboBox.getSelectionModel().selectLast();
		autoCheckBoxButton.setSelected(false);



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





		// Add series to chart
		lineChart.getData().addAll(seriesUSD, seriesEUR, seriesGBP, seriesCAD, seriesAUD);

	}
	private Map<String, Double> conversionRates = new HashMap<>();

	public void fetchCurrencyRates() {
		String apiKey = "fca_live_c8FaXQlw6z4eOjp2EH8e0ZiBpjxcGAa26NFWtJQL";
		String baseUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=" + apiKey; // Fixed URL formation


			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl)).build();

			client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(responseBody -> {
					try {
						JSONObject json = new JSONObject(responseBody);
						JSONObject rates = json.getJSONObject("data"); // Assuming 'data' contains the rates
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






							});
						}

						// Signal UI or logs that rates are updated
						System.out.println("Rates updated: " + conversionRates);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}).join();
		}


	private int conversionCount = 0;

	@FXML
	private void convertAction(ActionEvent actionEvent) {
		Currency inputCurrency = inputCurrencyComboBox.getValue();
		Currency outputCurrency = outputCurrencyComboBox.getValue();
		String inputText = inputAmount.getText().trim(); // Trim to remove leading/trailing whitespace.

		if (!inputText.isEmpty()) {
			// Check for non-numeric input
			if (!isNumeric(inputText)) {
				showAlert("Conversion Error", "Please enter a proper value.");
				return; // Exit the method early if validation fails
			}
		}

		double inputValue = Double.parseDouble(inputText);

		if (inputValue < 0) {
			showAlert("Conversion Error", "Please enter a proper value.");
			return;
		}

		// Check if the conversion rates for the selected currencies are available
		if (conversionRates.containsKey(inputCurrency.getCode()) && conversionRates.containsKey(outputCurrency.getCode())) {
			double rate = conversionRates.get(outputCurrency.getCode()) / conversionRates.get(inputCurrency.getCode());
			double convertedValue = inputValue * rate;
			outputAmount.setText(String.format("%.2f", convertedValue));


		} else {
			showAlert("Conversion Error", "Real-time conversion rates are currently unavailable.");

		}}
	private void populateChartWithConversionRates() {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Conversion Rates");

		conversionRates.forEach((currency, rate) -> {
			series.getData().add(new XYChart.Data<>(currency, rate));
		});

		Platform.runLater(() -> {
			lineChart.getData().clear(); // Clear previous data
			lineChart.getData().add(series);
		});
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
		return str != null && str.matches("^[0-9]*\\.?[0-9]+$");
	}

	}



