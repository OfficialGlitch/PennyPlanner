package GUI.util;

import GUI.ExpenseTableController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.StringConverter;
import models.instances.IncomeInstance;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class GenericTableCellFactory<T> implements Callback<TableColumn<IncomeInstance, T>, TableCell<IncomeInstance, T>> {
	private Function<T, String> textGetter;
	private StringConverter<T> converter;
	private BiConsumer<IncomeInstance, T> setter;
	public GenericTableCellFactory(Function<T, String> textFun, StringConverter<T> converter, BiConsumer<IncomeInstance, T> setter) {
		textGetter = textFun;
		this.converter = converter;
		this.setter = setter;
	}
	@Override
	public TableCell<IncomeInstance, T> call(TableColumn<IncomeInstance, T> param) {
		return new GenericEditableTableCell<T>(this.textGetter, this.converter, setter);
	}
	public static class GenericEditableTableCell<T> extends TextFieldTableCell<IncomeInstance, T> {
		
		private final BiConsumer<IncomeInstance, T> setter;
		private final StringConverter<T> converter;
		private boolean escPressed = false;
		private TextField field;
		
		public GenericEditableTableCell(Function<T, String> textFun, StringConverter<T> converter, BiConsumer<IncomeInstance, T> setter) {
			super(converter);
			this.setter = setter;
			this.converter = converter;
			if (getTableColumn() != null)
				this.getTableColumn().setOnEditCommit(e -> {
					setter.accept(e.getRowValue(), e.getNewValue());
				});
		}
		
		@Override
		public void startEdit() {
			if (!getTableColumn().isEditable()) return;
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
			final TableView<IncomeInstance> table = getTableView();
			updateItem(newValue, false);
			
			super.commitEdit(newValue);
			setter.accept(this.getTableRow().getItem(), newValue);
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
}
