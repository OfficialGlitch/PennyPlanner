package GUI.com.example.loan;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class LoanInfo implements Serializable{
	private static final long serialVersionUID = 6130139203999619115L; // Ensure consistent serialization
	private transient SimpleDoubleProperty loanAmount;
	private transient SimpleDoubleProperty interestRate;
	private transient SimpleIntegerProperty paymentPeriod;
	private transient SimpleDoubleProperty monthlyPayment;

	public LoanInfo(double loanAmount, double interestRate, int paymentPeriod) {
		this.loanAmount = new SimpleDoubleProperty(loanAmount);
		this.interestRate = new SimpleDoubleProperty(interestRate);
		this.paymentPeriod = new SimpleIntegerProperty(paymentPeriod);
		this.monthlyPayment = new SimpleDoubleProperty();
	}
	public LoanInfo(double loanAmount, double interestRate, int paymentPeriod, double monthlyPayment) {
		this.loanAmount = new SimpleDoubleProperty(loanAmount);
		this.interestRate = new SimpleDoubleProperty(interestRate);
		this.paymentPeriod = new SimpleIntegerProperty(paymentPeriod);
		this.monthlyPayment = new SimpleDoubleProperty(monthlyPayment);
	}
	public double getLoanAmount() {

		return loanAmount.get();
	}

	public DoubleProperty loanAmountProperty() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount.set(loanAmount);
	}

	public double getInterestRate() {
		return interestRate.get();
	}

	public DoubleProperty interestRateProperty() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate.set(interestRate);
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

	public void setPaymentPeriod(int paymentPeriod) {
		this.paymentPeriod.set(paymentPeriod);
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

}
