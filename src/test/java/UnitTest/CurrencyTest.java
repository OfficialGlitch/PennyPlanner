package UnitTest;

import GUI.ConvertCurrency;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import javafx.application.Platform;


import java.util.concurrent.CountDownLatch;

public class CurrencyTest {

	private ConvertCurrency convertCurrency;

	@BeforeEach
	public void setUp() {
		convertCurrency = new ConvertCurrency();
		// This call is assumed to fetch and populate conversionRates with real data
		convertCurrency.fetchCurrencyRates();
	}
//this is an integration test

	@BeforeAll
	public static void setup() throws InterruptedException {
		// Initialize JavaFX Toolkit
		Thread t = new Thread(() -> Platform.startup(() -> {
		}));
		t.start();
		t.join();
	}

	@Test
	void testFetchCurrencyRates() throws Exception {
		ConvertCurrency convertCurrency = new ConvertCurrency();
		convertCurrency.fetchCurrencyRates();

		// Assert that conversion rates are not null and contain expected keys
		assertNotNull(convertCurrency.getConversionRates(), "Conversion rates should not be null");
		assertTrue(convertCurrency.getConversionRates().containsKey("USD"), "Conversion rates should include USD");
		assertTrue(convertCurrency.getConversionRates().get("USD") > 0, "USD conversion rate should be greater than 0");
	}
	@Test
	public void conversionlogictest() throws  InterruptedException {
		// Ensure the fetch operation has completed before proceeding.
		// This may require adjustments based on how your actual fetchCurrencyRates method is implemented.

		double amountInUSD = 100;
		// Since we're dealing with live data, we can't predict the exact conversion rate,
		// but we know that 100 USD should logically convert to more than 85 EUR based on historical data.

		// Wait for the API response - in practice, this might require using CompletableFuture, CountDownLatch, or similar.
		Thread.sleep(5000); // Example sleep to wait for fetch operation. Not recommended for real tests.

		double convertedAmount = convertCurrency.convertAmount(amountInUSD, "USD", "EUR");

		assertTrue(convertedAmount > 85, "100 USD should convert to more than 85 EUR.");
	}
	@Test
	void testHandleUnexpectedData() {
		ConvertCurrency convertCurrency = new ConvertCurrency();

		// Call the method to fetch rates. Since this is an integration test, it will attempt to fetch real data.
		// We cannot guarantee the API will return an error or be down, but we can ensure our application does not crash.
		assertDoesNotThrow(() -> convertCurrency.fetchCurrencyRates(), "Application should not throw an exception when fetching rates");

		// Check if the application handles unexpected responses gracefully. For example, it could be checking if
		// the conversion rates map is not empty or if some error state is set. Adjust the assertions according to your app design.
		assertFalse(convertCurrency.getConversionRates().isEmpty(), "Conversion rates should not be empty after fetching");

		// Additional assertions can be made based on how you expect your application to react to real-world scenarios.
		// For instance, checking if an error message is displayed or if a specific error handling routine is triggered.
	}
	@Test
	void refreshRatesLogicTest() throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);

		Platform.runLater(() -> {
			ConvertCurrency convertCurrency = new ConvertCurrency();

			try {
				// Simulate initial fetch
				convertCurrency.fetchCurrencyRates();
				assertNotNull(convertCurrency.getConversionRates());
				int sizeBeforeRefresh = convertCurrency.getConversionRates().size();

				// Simulate clicking the refresh button
				convertCurrency.handlerefresh(null);
				Thread.sleep(1000); // Wait a bit for the refresh to complete
				// Assuming getConversionRates() method exists for testing
				assertNotNull(convertCurrency.getConversionRates());
				assertTrue(convertCurrency.getConversionRates().size() >= sizeBeforeRefresh);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		});

		latch.await();
	}
}