package GUI.dialogs;

import GUI.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;
import models.Category;
import models.money.Expense;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddExpenseTypeDialog {
	@FXML
	private ComboBox<Category> categoryPicker;
	@FXML
	private TextField nameField;
	private List<Category> categories;
	
	@FXML
	public void initialize() {
		categories = App.sf().openSession().createNamedQuery("getAllCategories", Category.class)
			.setParameter("user", App.getCurrentUser().getID()).getResultList();
		categoryPicker.getItems().setAll(categories);
		categoryPicker.setConverter(new StringConverter<>() {
			@Override
			public String toString(Category object) {
				return object != null ? object.getName() : "";
			}
			@Override
			public Category fromString(String string) {
				return categoryPicker.getItems().stream().filter(object ->
					object.getName().equals(string)).findFirst().orElse(null);
			}
		});
		
		categoryPicker.setOnKeyPressed(t -> {
			categoryPicker.hide();
		});
		categoryPicker.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.DOWN) {
				if (!categoryPicker.isShowing()) {
					categoryPicker.show();
				}
				return;
			} else if (event.getCode() == KeyCode.ENTER)
				return;
			if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT || event.getCode().equals(KeyCode.SHIFT) || event.getCode().equals(KeyCode.CONTROL)
				|| event.isControlDown() || event.getCode() == KeyCode.HOME
				|| event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
				return;
			}
			
			categoryPicker.getItems().clear();
			categoryPicker.getItems().addAll(filterItems(categoryPicker.getEditor().getText()));
			if(!categoryPicker.getItems().isEmpty()) {
				categoryPicker.show();
			}
		});
	}
	
	private ObservableList<Category> filterItems(String input) {
		ObservableList<Category> filtered = FXCollections.observableArrayList();
		Pattern regex = Pattern.compile(".*?" + input + ".*?");
		for (Category c : categories) {
			int matches = 0;
			Matcher matcher = regex.matcher(c.getName().toLowerCase());
			while (matcher.find()) {
				matches++;
			}
			if (matches > 0) {
				filtered.add(c);
			}
		}
		return filtered;
	}
	
	public Expense getData() {
		Expense e = new Expense();
		e.setName(nameField.getText());
		e.setCategory(categoryPicker.getValue());
		return e;
	}
}