package GUI.util;

import javafx.geometry.Pos;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;

import java.util.function.Function;

public class GenericTreeTableCellFactory<T> implements javafx.util.Callback<TreeTableColumn<ExpenseTreeTableItem, T>, TreeTableCell<ExpenseTreeTableItem, T>> {
	private Function<T, String> textGetter;
	
	public GenericTreeTableCellFactory(Function<T, String> textFun) {
		this.textGetter = textFun;
	}
	
	@Override
	public TreeTableCell<ExpenseTreeTableItem, T> call(TreeTableColumn<ExpenseTreeTableItem, T> param) {
		TreeTableCell<ExpenseTreeTableItem, T> cell = new TreeTableCell<>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				this.setItem(item);
				this.setText(GenericTreeTableCellFactory.this.textGetter.apply(item));
				this.setEditable(!(item instanceof CategoryIntermediate));
				if(this.getTableRow().getTreeItem() != null) {
					if(this.getTableRow().getTreeItem().getValue() instanceof CategoryIntermediate) {
						this.setAlignment(Pos.CENTER);
					} else {
						this.setAlignment(Pos.CENTER_LEFT);
					}
				}
			}
		};
		return cell;
	}
}
