package GUI.Loan;

import GUI.App;
import models.money.User;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalDateStringConverter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class LoanController {
	@FXML
	private TextField loanAmountTextField;

	@FXML
	private TextField interestRateTextField;

	@FXML
	private TextField paymentPeriodTextField;

	@FXML
	private TextField startDateTextField;

	@FXML
	private TextField endDateTextField;

	@FXML
	private TableView<LoanInfo> loanDetailsTableView;

	@FXML
	private Button deleteButton;

	@FXML
	private Button saveButton;

	@FXML
	private Button addButton;

	@FXML
	private TextField remainingAmountTextField;
	public TextField getLoanAmountTextField() {
		return loanAmountTextField;
	}

	public TextField getInterestRateTextField() {
		return interestRateTextField;
	}

	public TextField getPaymentPeriodTextField() {
		return paymentPeriodTextField;
	}

	public TableView<LoanInfo> getLoanDetailsTableView() {
		return loanDetailsTableView;
	}
	private static ObservableList<LoanInfo> loanInfoList;
	@FXML
	public void initialize() {
		// Set up columns in the table view
		loanInfoList = FXCollections.observableArrayList();

		TableColumn<LoanInfo, Double> loanAmountColumn = new TableColumn<>("Loan Amount");
		loanAmountColumn.setCellValueFactory(cellData -> cellData.getValue().loanAmountProperty().asObject());
		loanAmountColumn.setPrefWidth(118);

		TableColumn<LoanInfo, Double> interestRateColumn = new TableColumn<>("Interest Rate");
		interestRateColumn.setCellValueFactory(cellData -> cellData.getValue().interestRateProperty().asObject());
		interestRateColumn.setPrefWidth(120);

		TableColumn<LoanInfo, Integer> paymentPeriodColumn = new TableColumn<>("Payment Period");
		paymentPeriodColumn.setCellValueFactory(cellData -> cellData.getValue().paymentPeriodProperty().asObject());
		paymentPeriodColumn.setPrefWidth(138);

		TableColumn<LoanInfo, Double> monthlyPaymentColumn = new TableColumn<>("Monthly Payment");
		monthlyPaymentColumn.setCellValueFactory(cellData ->  cellData.getValue().monthlyPaymentProperty().asObject());
		monthlyPaymentColumn.setPrefWidth(120);

		TableColumn<LoanInfo, LocalDate> startDateColumn = new TableColumn<>("Start Date");
		startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		startDateColumn.setPrefWidth(110);
		startDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		startDateColumn.setOnEditCommit(event -> {
			LoanInfo loan = event.getRowValue();
			loan.setStartDate(event.getNewValue());
			// Recalculate end date based on payment period and update table
			LocalDate newEndDate = event.getNewValue().plusMonths(loan.getPaymentPeriod());
			loan.setEndDate(newEndDate);
			loanDetailsTableView.refresh();
		});

		TableColumn<LoanInfo, LocalDate> endDateColumn = new TableColumn<>("End Date");
		endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		endDateColumn.setPrefWidth(110);
		endDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		// New column for remaining loan amount
		TableColumn<LoanInfo, Double> remainingLoanColumn = new TableColumn<>("Remaining Loan");
		remainingLoanColumn.setCellValueFactory(cellData -> {
			double remainingLoan = cellData.getValue().calculateRemainingLoan();
			return new SimpleDoubleProperty(remainingLoan).asObject();
		});
		remainingLoanColumn.setPrefWidth(120);


		// Add the columns to the table view
		loanDetailsTableView.getColumns().addAll(startDateColumn, endDateColumn);
    // Load saved data if available
		loadSavedData();

		loanDetailsTableView.setItems(loanInfoList);
		loanDetailsTableView.refresh();

		// Set selection mode to SINGLE
		loanDetailsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		loanDetailsTableView.getColumns().addAll(loanAmountColumn, interestRateColumn, paymentPeriodColumn, monthlyPaymentColumn, remainingLoanColumn);

		// Bind loanInfoList to TableView
		loanDetailsTableView.setItems(loanInfoList);

		ObservableList<LoanInfo> data = FXCollections.observableArrayList();
		//loanDetailsTableView.setItems(data);
		loanInfoList.addListener((ListChangeListener<LoanInfo>) change -> {
			while (change.next()) {
				if (change.wasAdded()) {
					for (LoanInfo loan : change.getAddedSubList()) {
						double monthlyPayment = calculateMonthlyPayment(loan.getLoanAmount(), loan.getInterestRate(), loan.getPaymentPeriod());
						loan.setMonthlyPayment(monthlyPayment);
					}
				}
			}
		});

		loanDetailsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				// Enable the delete button when a row is selected
				deleteButton.setDisable(false);
			} else {
				// Disable the delete button when no row is selected
				deleteButton.setDisable(true);
			}
		});

	}
	@FXML
	public void handleAddButton() {
		// Handle the action when the Add button is clicked
		try {
			double loanAmount = Double.parseDouble(loanAmountTextField.getText());
			double interestRate = Double.parseDouble(interestRateTextField.getText());
			int paymentPeriod = Integer.parseInt(paymentPeriodTextField.getText());

			double monthlyPayment = calculateMonthlyPayment(loanAmount, interestRate, paymentPeriod);
			// Get the current date for the start date
			LocalDate startDate = LocalDate.now();
			// Calculate the end date based on the payment period
			LocalDate endDate = startDate.plusMonths(paymentPeriod);

			LoanInfo newLoan = new LoanInfo(loanAmount, interestRate, paymentPeriod, monthlyPayment, startDate, endDate);
			newLoan.setRemainingLoan(newLoan.calculateRemainingLoan());
			loanInfoList.add(newLoan);


			// Set the items of the TableView to the updated loanInfoList
			loanDetailsTableView.setItems(loanInfoList);

			// Clear input fields after adding the loan
			loanAmountTextField.clear();
			interestRateTextField.clear();
			paymentPeriodTextField.clear();
		} catch (NumberFormatException e) {
			// Handle parsing errors
			System.out.println("Invalid input. Please enter valid numeric values.");
		}
	}

	@FXML
	public void handleDeleteButton() {
		// Get the selected item in the TableView
		LoanInfo selectedLoan = loanDetailsTableView.getSelectionModel().getSelectedItem();
		if (selectedLoan != null) {
			// Delete the selected loan from the database
			String username = getCurrentUsername(); // Get the current username
			// Remove the selected loan from the list
			loanInfoList.remove(selectedLoan);
			loanDetailsTableView.refresh();
			if (LoanDatabaseManager.deleteLoanForUser(username, selectedLoan)) {
				// Remove the selected item from the list if deleted from the database successfully
				loanInfoList.remove(selectedLoan);
				loanDetailsTableView.refresh();
				// Show success message
				showInfoAlert("Loan deleted successfully.");
			}
		} else {
			// Handle case where no row is selected
			showInfoAlert("Please select a row to delete.");
		}
	}

	@FXML
	public void handleSaveButton() {
		/// Check if any loans are available to save
		if (loanDetailsTableView.getItems().isEmpty()) {
			showErrorAlert("No loans to save.");
			return;
		}

		// Attempt to save each loan
		String username = getCurrentUsername();
		if (username == null) {
			showErrorAlert("User not logged in.");
			return;
		}

		for (LoanInfo loan : loanDetailsTableView.getItems()) {
			if (!LoanDatabaseManager.saveLoanForUser(username, loan)) {
				showErrorAlert("Failed to save one or more loans.");
				return;
			}
		}
		// Display success message if all loans are saved
		showInfoAlert("Loans saved successfully.");
	}

	// Helper method to display an information alert
	private void showInfoAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Helper method to display an error alert
	private void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}



	private double calculateMonthlyPayment(double loanAmount, double interestRate, int paymentPeriod) {
		// Perform the calculation to get the monthly payment
		// Use the appropriate formula for loan amortization
		double monthlyInterestRate = 0.0;
		if (monthlyInterestRate < 0){
			System.out.println("Enter a number greater than zero");
		}else{
			monthlyInterestRate = interestRate / 12.0;
		}
		int numberOfPayments = paymentPeriod;
		double monthlyPayment = (loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -numberOfPayments));
		// Format the result to two decimal places
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		return Double.parseDouble(decimalFormat.format(monthlyPayment));
	}


	private void loadSavedData() {
		// Get the current username
		String username = getCurrentUsername();
		// Retrieve loan information for the current user
		List<LoanInfo> loans = LoanDatabaseManager.getLoansForUser(username);

		// Add the retrieved loans to the observable list
		loanInfoList.addAll(loans);
	}


	// This method retrieves the current username from the application context
	public String getCurrentUsername() {
		// Assuming you have a method to get the currently logged-in user from the application context
		User currentUser = App.getCurrentUser();

		// Check if a user is currently logged in
		if (currentUser != null) {
			// Return the username of the current user
			return currentUser.getUsername();
		} else {
			// Return null or throw an exception if no user is logged in
			return null;
		}
	}

	public class LoanDatabaseManager {
		private static final String DATABASE_URL = "jdbc:sqlite:Accounts.db";

		// Establishes a connection to the SQLite database
		private static Connection getConnection() throws SQLException {
			return DriverManager.getConnection(DATABASE_URL);
		}

		// Closes the database connection
		private static void closeConnection(Connection conn) {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Authenticates a user based on username and password
		public static boolean authenticateUser(String username, String password) {
			String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
			try (Connection conn = getConnection();
					 PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, username);
				stmt.setString(2, password);
				try (ResultSet rs = stmt.executeQuery()) {
					return rs.next(); // Returns true if a matching user is found
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		// Saves loan information for a user
		public static boolean saveLoanForUser(String username, LoanInfo loan) {
			String sql = "INSERT INTO Loans (username, loanAmount, interestRate, paymentPeriod, startDate, endDate, monthlyPayment, remainingLoan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try (Connection conn = getConnection();
					 PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, username);
				stmt.setDouble(2, loan.getLoanAmount());
				stmt.setDouble(3, loan.getInterestRate());
				stmt.setInt(4, loan.getPaymentPeriod());
				stmt.setObject(5, loan.getStartDate());
				stmt.setObject(6, loan.getEndDate());
				stmt.setDouble(7, loan.getMonthlyPayment());
				stmt.setDouble(8, loan.getRemainingLoan());
				int rowsAffected = stmt.executeUpdate();
				return rowsAffected > 0; // Returns true if the loan was successfully saved
			}
			catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		// Retrieves loan information for a user
		public static List<LoanInfo> getLoansForUser(String username) {
			List<LoanInfo> loans = new ArrayList<>();
			String sql = "SELECT * FROM Loans WHERE username = ?";
			try (Connection conn = getConnection();
					 PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, username);
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						double loanAmount = rs.getDouble("loanAmount");
						double interestRate = rs.getDouble("interestRate");
						int paymentPeriod = rs.getInt("paymentPeriod");
						LocalDate startDate = rs.getObject("startDate", LocalDate.class);
						LocalDate endDate = rs.getObject("endDate", LocalDate.class);
						double monthlyPayment = rs.getDouble("monthlyPayment");
						double remainingLoan = rs.getDouble("remainingLoan");
						loans.add(new LoanInfo(loanAmount, interestRate, paymentPeriod, startDate, endDate, monthlyPayment, remainingLoan));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return loans;
		}
		public static boolean deleteLoanForUser(String username, LoanInfo loan) {
			String sql = "DELETE FROM Loans WHERE username = ? AND loanAmount = ? AND interestRate = ? AND paymentPeriod = ? AND startDate = ? AND endDate = ? AND monthlyPayment = ? AND remainingLoan = ?";
			try (Connection conn = getConnection();
					 PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, username);
				stmt.setDouble(2, loan.getLoanAmount());
				stmt.setDouble(3, loan.getInterestRate());
				stmt.setInt(4, loan.getPaymentPeriod());
				stmt.setObject(5, loan.getStartDate());
				stmt.setObject(6, loan.getEndDate());
				stmt.setDouble(7, loan.getMonthlyPayment());
				stmt.setDouble(8, loan.getRemainingLoan());
				int rowsAffected = stmt.executeUpdate();
				return rowsAffected > 0; // Returns true if the loan was successfully deleted
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}

	}
}

