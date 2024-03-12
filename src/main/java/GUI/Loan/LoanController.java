package GUI.Loan;

import GUI.App;
import GUI.ExpenseTableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalDateStringConverter;
import models.TimePeriod;
import java.time.LocalDate;
import java.io.*;
import java.time.LocalDate;


public class LoanController {
	@FXML
	private TextField loanAmountTextField;

	@FXML
	private TextField interestRateTextField;

	@FXML
	private TextField paymentPeriodTextField;

	@FXML
	private TableView<LoanInfo> loanDetailsTableView;

	@FXML
	private Button calculateButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button saveButton;

	@FXML
	private Button addButton;
	private ObservableList<LoanInfo> loanInfoList;
	@FXML
	public void initialize() {
		// Set up columns in the table view
		// Set up columns in the table view


		loanInfoList = FXCollections.observableArrayList();

		TableColumn<LoanInfo, Double> loanAmountColumn = new TableColumn<>("Loan Amount");
		loanAmountColumn.setCellValueFactory(cellData -> cellData.getValue().loanAmountProperty().asObject());
		loanAmountColumn.setPrefWidth(118);

		TableColumn<LoanInfo, Double> interestRateColumn = new TableColumn<>("Interest Rate");
		interestRateColumn.setCellValueFactory(cellData -> cellData.getValue().interestRateProperty().asObject());
		interestRateColumn.setPrefWidth(132);

		TableColumn<LoanInfo, Integer> paymentPeriodColumn = new TableColumn<>("Payment Period");
		paymentPeriodColumn.setCellValueFactory(cellData -> cellData.getValue().paymentPeriodProperty().asObject());
		paymentPeriodColumn.setPrefWidth(139);

		TableColumn<LoanInfo, Double> monthlyPaymentColumn = new TableColumn<>("Monthly Payment");
		monthlyPaymentColumn.setCellValueFactory(cellData -> cellData.getValue().monthlyPaymentProperty().asObject());
		monthlyPaymentColumn.setPrefWidth(168);

		TableColumn<LoanInfo, LocalDate> startDateColumn = new TableColumn<>("Start Date");
		startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		startDateColumn.setPrefWidth(120);
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
		endDateColumn.setPrefWidth(120);
		endDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));


		// Add the columns to the table view
		loanDetailsTableView.getColumns().addAll(startDateColumn, endDateColumn);

		// Load saved data if available
		loadSavedData();
		loanDetailsTableView.setItems(loanInfoList);
		loanDetailsTableView.refresh();

		// Set selection mode to SINGLE
		loanDetailsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);



		loanDetailsTableView.getColumns().addAll(loanAmountColumn, interestRateColumn, paymentPeriodColumn, monthlyPaymentColumn);

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
	private void handleCalculateButton() {
		// Check if any of the text fields are empty
		if (loanAmountTextField.getText().isEmpty() ||
			interestRateTextField.getText().isEmpty() ||
			paymentPeriodTextField.getText().isEmpty()) {
			// Display an error message or handle the empty fields as appropriate
			// For example:
			System.out.println("Please fill in all fields.");
			return; // Exit the method early
		}

		// Handle the action when the Calculate button is clicked
		// Retrieve loan amount, interest rate, and payment period from text fields

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
			loanInfoList.add(newLoan);

			// Clear input fields after adding the loan
			loanAmountTextField.clear();
			interestRateTextField.clear();
			paymentPeriodTextField.clear();
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter valid numeric values.");
		}
	}

	@FXML
	private void handleDeleteButton() {
		// Get the selected item in the TableView
		LoanInfo selectedLoan = loanDetailsTableView.getSelectionModel().getSelectedItem();
		if (selectedLoan != null) {
			// Remove the selected item from the list
			loanInfoList.remove(selectedLoan);
			loanDetailsTableView.refresh();
			System.out.println("Delete button pressed");
		}
		else {
			// Handle case where no row is selected
			System.out.println("Please select a row to delete.");
		}
	}


	private void clearTableView() {
		loanDetailsTableView.getItems().clear();
	}

	@FXML
	private void handleSaveButton() {
		// Handle the action when the Save button is clicked
		saveData();
	}

	@FXML
	private void handleAddButton() {

		// Handle the action when the Add button is clicked
		try {
			double loanAmount = Double.parseDouble(loanAmountTextField.getText());
			double interestRate = Double.parseDouble(interestRateTextField.getText());
			int paymentPeriod = Integer.parseInt(paymentPeriodTextField.getText());

			// Get the current date for the start date
			LocalDate startDate = LocalDate.now();
			// Calculate the end date based on the payment period
			LocalDate endDate = startDate.plusMonths(paymentPeriod);


			LoanInfo newLoan = new LoanInfo(loanAmount, interestRate, paymentPeriod,startDate, endDate);
			loanDetailsTableView.getItems().add(newLoan);

			System.out.println("Add button clicked");

			// Clear input fields after adding the loan
			loanAmountTextField.clear();
			interestRateTextField.clear();
			paymentPeriodTextField.clear();
		} catch (NumberFormatException e) {
			// Handle parsing errors
			System.out.println("Invalid input. Please enter valid numeric values.");
		}
	}

	private double calculateMonthlyPayment(double loanAmount, double interestRate, int paymentPeriod) {
		// Perform the calculation to get the monthly payment
		// Use the appropriate formula for loan amortization
		// For example:
		double monthlyInterestRate = interestRate / 12.0;
		int numberOfPayments = paymentPeriod * 12;
		return (loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -numberOfPayments));
	}


	private void loadSavedData() {

	}

	private void saveData() {


	}
}
