package GUI.util;

import GUI.App;
import GUI.ExpenseTableController;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import models.instances.ExpenseInstance;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class EditableTreeTableCell<T> extends TextFieldTreeTableCell<ExpenseTreeTableItem, T> {
	private BiConsumer<ExpenseTreeTableItem, T> setter;
	private StringConverter<T> converter;
	private boolean escPressed = false;
	private TextField field;
	
	public EditableTreeTableCell(Function<T, String> textFun, StringConverter<T> converter, BiConsumer<ExpenseTreeTableItem, T> setter) {
		super(converter);
		this.setter = setter;
		this.converter = converter;
		if (getTableColumn() != null)
			this.getTableColumn().setOnEditCommit(e -> {
				setter.accept(e.getRowValue().getValue(), e.getNewValue());
			});
	}
	
	@Override
	public void startEdit() {
		if (!getTableColumn().isEditable()) return;
		if (this.getTableRow().getTreeItem().getValue() instanceof CategoryIntermediate &&
			!getTableColumn().getId().equals(ExpenseTableController.NAME_COLUMN)) return;
		super.startEdit();
		if (field == null) {
			field = getTextField();
		}
		if (isEditing()) {
			escPressed = false;
			
			beginEdit(field);
		}
	}
	
	@Override
	public void cancelEdit() {
		if (escPressed) {
			super.cancelEdit();
			setText(getCurrentText());
		} else {
			if (field != null)
				commitEdit(getConverter().fromString(field.getText()));
		}
		setGraphic(null);
	}
	
	@Override
	public void commitEdit(T newValue) {
		if (!isEditing()) return;
		final TreeTableView<ExpenseTreeTableItem> table = getTreeTableView();
		updateItem(newValue, false);
		
		super.commitEdit(newValue);
		setter.accept(this.getTableRow().getTreeItem().getValue(), newValue);
		if (table != null) {
			table.edit(-1, null);
		}
		
	}
	
	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (isEmpty()) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				setText(null);
				setGraphic(field);
			} else {
				if (field != null) {
					field.setText(getCurrentText());
				}
				setText(getCurrentText());
				setGraphic(null);
				if(this.getTableRow().getTreeItem().getParent() != null)
					if (this.getTableRow().getTreeItem().getParent().getValue() == null && !this.getTableColumn().getId().equals(ExpenseTableController.NAME_COLUMN)) {
						this.setAlignment(Pos.CENTER);
					} else {
						this.setAlignment(Pos.CENTER_LEFT);
					}
			}
		}
	}
	private TextField getTextField() {
		final TextField textField = new TextField(getCurrentText());
		textField.setOnAction(ev -> {
			this.commitEdit(converter.fromString(textField.getText()));
			ev.consume();
		});
		textField.setOnKeyPressed(t -> {
			escPressed = t.getCode() == KeyCode.ESCAPE;
		});
		return textField;
	}
	
	private void beginEdit(final TextField field) {
		field.setText(getCurrentText());
		setText(null);
		setGraphic(field);
		field.requestFocus();
	}
	
	private String getCurrentText() {
		return getConverter().toString(getItem());
	}
}
