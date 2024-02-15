package GUI.dialogs;

import GUI.App;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.Category;
import models.money.Expense;

import java.util.List;

public class AddExpenseTypeDialog
{
	@FXML
	private ComboBox<Category> categoryPicker;
	@FXML
	private TextField nameField;
	
	@FXML
    public void initialize() {
			List<Category> categories = App.s().createNamedQuery("getAllCategories", Category.class)
				.setParameter("user", App.getCurrentUser().getID()).getResultList();
			categoryPicker.getItems().setAll(categories);
    }
		public Expense getData() {
			Expense e = new Expense();
			e.setName(nameField.getText());
			e.setCategory(categoryPicker.getValue());
			return e;
		}
}