package GUI;
import java.util.ArrayList;

public class Database {

	ArrayList<Object> database = new ArrayList<Object>();
	ArrayList<Object> sub_database = new ArrayList<Object>();

	// Get User Information with Id
	public ArrayList<Object> InfoGetter(int id){
		
		return (ArrayList<Object>)(database.get(id));
	}
	
	// get specific subject from user, returns object for List(eg Expenses);
	public double specificItem(int id, int subject, int index) {
		
		return (double)(((ArrayList)this.InfoGetter(id).get(subject)).get(index));
	}
	
	// Add new User
	public void addPerson(ArrayList<Object> added){
		if(added.size() < 6) {
			System.out.println("Missing arguments should be 6 but have " + added.size());
		}
		else {
			database.add(added);
		}
	}
	

	public void modifyPerson(int id, int subject, Object replacement){
		this.InfoGetter(id).set(subject, replacement);
	}
	
	// Sum a defined category, eg. Expenses, Income.
	public double sumSubject(int id, int subject) {
		double sum = 0;
		for(int i = 0; i < ((ArrayList)(this.InfoGetter(id).get(subject))).size(); i++)
		    sum += this.specificItem(id, subject, i);
		return sum;
	}
	
	
	public static void main(String[] args) {
	 Database d = new Database();
	 
	 // EXAMPLE FUNCTIONS
	 
	 // Adding Person 1
	 ArrayList<Object> added = new ArrayList<Object>();
	 
	 added.add(1);added.add("john");added.add("pass2");
	 
	 ArrayList<Object> Expenses = new ArrayList<Object>();
	 Expenses.add(1.1);Expenses.add(2.1);added.add(Expenses);
	 
	 ArrayList<Object> Incomes = new ArrayList<Object>();
	 Incomes.add(3.1);Incomes.add(4.1);added.add(Incomes);
	 
	 ArrayList<Object> Investments = new ArrayList<Object>();
	 Investments.add(100.1);Investments.add(200.1);added.add(Investments);
	 
	 
	 d.addPerson(added);
	 
	 // Adding Person 2
	 ArrayList<Object> added2 = new ArrayList<Object>();
	 
	 added2.add(1);added2.add("joe");added2.add("pass2");
	 added2.add(1.1);added2.add(2.1);added2.add(5.1);
	 
	 d.addPerson(added2);
	 
	 System.out.println(d.InfoGetter(0));
	 System.out.println(d.InfoGetter(1));
	 
	 // Modifying
	 d.modifyPerson(0, 1, 3);
	 
	 // Printing
	 System.out.println(d.InfoGetter(0));
	 System.out.println(d.specificItem(0, 3, 0));
	 System.out.println(d.sumSubject(0, 5));
	}
}
