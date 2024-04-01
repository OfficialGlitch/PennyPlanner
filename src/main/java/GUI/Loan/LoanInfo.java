package GUI.Loan;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

import javafx.beans.property.*;


public class LoanInfo implements Serializable{
	private static final long serialVersionUID = 6130139203999619115L; // Ensure consistent serialization
	private transient SimpleDoubleProperty loanAmount;
	private transient SimpleDoubleProperty interestRate;
	private transient SimpleIntegerProperty paymentPeriod;
	private transient SimpleDoubleProperty monthlyPayment;
	private ObjectProperty<LocalDate> startDate;
	private ObjectProperty<LocalDate> endDate;
	private transient DoubleProperty remainingLoan;



	public LoanInfo(double loanAmount, double interestRate, int paymentPeriod, double monthlyPayment, LocalDate startDate, LocalDate endDate) {
		this.loanAmount = new SimpleDoubleProperty(loanAmount);
		this.interestRate = new SimpleDoubleProperty(interestRate);
		this.paymentPeriod = new SimpleIntegerProperty(paymentPeriod);
		this.monthlyPayment = new SimpleDoubleProperty(monthlyPayment);
		this.startDate = new SimpleObjectProperty<>(startDate);
		this.endDate = new SimpleObjectProperty<>(startDate.plusMonths(paymentPeriod));
		calculateRemainingLoan();
	}
	public LoanInfo(double loanAmount, double interestRate, int paymentPeriod, LocalDate startDate, LocalDate endDate, double monthlyPayment, double remainingLoan) {
		this.loanAmount = new SimpleDoubleProperty(loanAmount);
		this.interestRate = new SimpleDoubleProperty(interestRate);
		this.paymentPeriod = new SimpleIntegerProperty(paymentPeriod);
		this.monthlyPayment = new SimpleDoubleProperty(monthlyPayment);
		this.startDate = new SimpleObjectProperty<>(startDate);
		this.endDate = new SimpleObjectProperty<>(startDate.plusMonths(paymentPeriod));
		this.remainingLoan = new SimpleDoubleProperty(remainingLoan);
		this.startDate.addListener((obs, oldDate, newDate) -> {
			this.startDate.set(newDate);
			calculateRemainingLoan(); // Recalculate remaining loan when start date changes
		});
	}

	public double getLoanAmount() {

		return loanAmount.get();
	}

	public DoubleProperty loanAmountProperty() {
		return loanAmount;
	}


	public double getInterestRate() {
		return interestRate.get();
	}

	public DoubleProperty interestRateProperty() {
		return interestRate;
	}


	// Custom serialization methods
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeDouble(loanAmount.get());
		out.writeDouble(interestRate.get());
		out.writeInt(paymentPeriod.get());
		out.writeDouble(monthlyPayment.get()); // Serialize the double value
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		double loanAmountValue = in.readDouble();
		double interestRateValue = in.readDouble();
		int paymentPeriodValue = in.readInt();
		double monthlyPaymentValue = in.readDouble();

		loanAmount = new SimpleDoubleProperty(loanAmountValue);
		interestRate = new SimpleDoubleProperty(interestRateValue);
		paymentPeriod = new SimpleIntegerProperty(paymentPeriodValue);
		monthlyPayment = new SimpleDoubleProperty(monthlyPaymentValue);
	}

	public int getPaymentPeriod() {
		return paymentPeriod.get();
	}

	public IntegerProperty paymentPeriodProperty() {
		return paymentPeriod;
	}

	public DoubleProperty monthlyPaymentProperty() {
		return monthlyPayment;
	}
	public double getMonthlyPayment() {
		return monthlyPayment.get();
	}

	public void setMonthlyPayment(double monthlyPayment) {
		this.monthlyPayment.set(monthlyPayment);
	}
	public LocalDate getStartDate() {
		return startDate.get();
	}

	public ObjectProperty<LocalDate> startDateProperty() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate.set(startDate);
	}

	public LocalDate getEndDate() {
		return endDate.get();
	}

	public ObjectProperty<LocalDate> endDateProperty() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate.set(endDate);
	}
	public double calculateRemainingLoan() {
		LocalDate currentDate = LocalDate.now();
		if (currentDate.isBefore(endDate.get())) {
			LocalDate date = startDate.get();
			double remainingAmount;
			double totalInterest = (monthlyPayment.get() * paymentPeriod.get()) - loanAmount.get();
			remainingAmount = loanAmount.get() + totalInterest;
			while (date.isBefore(currentDate)) {
				double monthlyInterest = remainingAmount * (interestRate.get() /100 / 12.0);
				remainingAmount -= (monthlyPayment.get() - monthlyInterest);
				date = date.plusMonths(1); // Move to the next month
			}
			DecimalFormat decimalFormat = new DecimalFormat("#0.00");
			return Double.parseDouble(decimalFormat.format(remainingAmount >= 0 ? remainingAmount : 0));

		} else {
			return 0; // Loan fully paid off
		}
	}

	public double getRemainingLoan() {
		if (remainingLoan == null) {
			remainingLoan = new SimpleDoubleProperty();
		}
		return remainingLoan.get();
	}

	// Setter method for remainingLoan property
	public void setRemainingLoan(double remainingLoan) {
		if (this.remainingLoan == null) {
			this.remainingLoan = new SimpleDoubleProperty();
		}
		this.remainingLoan.set(remainingLoan);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LoanInfo loanInfo = (LoanInfo) o;
		return Double.compare(loanInfo.getLoanAmount(), getLoanAmount()) == 0 &&
			Double.compare(loanInfo.getInterestRate(), getInterestRate()) == 0 &&
			loanInfo.getPaymentPeriod() == getPaymentPeriod() &&
			Double.compare(loanInfo.getMonthlyPayment(), getMonthlyPayment()) == 0 &&
			Double.compare(loanInfo.getRemainingLoan(), getRemainingLoan()) == 0 &&
			Objects.equals(getStartDate(), loanInfo.getStartDate()) &&
			Objects.equals(getEndDate(), loanInfo.getEndDate());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLoanAmount(), getInterestRate(), getPaymentPeriod(), getMonthlyPayment(), getStartDate(), getEndDate(), getRemainingLoan());
	}
}

