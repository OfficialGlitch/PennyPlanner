package GUI;

import GUI.util.CategoryIntermediate;
import GUI.util.ExpenseTreeTableItem;
import GUI.util.GenericTableCellFactory;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import models.Category;
import models.TimePeriod;
import models.instances.ExpenseInstance;
import models.money.Expense;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExpenseTableController {
	@FXML
	public TreeTableView<ExpenseTreeTableItem> mainTable;
	@FXML
	public TreeTableColumn<ExpenseTreeTableItem, String> nameCol;
	@FXML
	public TreeTableColumn<ExpenseTreeTableItem, Double> projectedCostCol;
	@FXML
	public TreeTableColumn<ExpenseTreeTableItem, Double> actualCostCol;
	@FXML
	public TreeTableColumn<ExpenseTreeTableItem, Double> differenceCol;
	@FXML
	public AnchorPane subtotalWrapper;
	@FXML
	public Text subtotalLabel;
	@FXML
	public Text dollarSign;
	@FXML
	public Text subtotal;
	private TimePeriod timePeriod;
	
	private final TreeItem<ExpenseTreeTableItem> troot = new TreeItem<>(null);
	public void setFields(TimePeriod tp) {
		this.timePeriod = tp;
		this.setupTable();
	}
	
	public <T> WeakInvalidationListener createListener() {
		InvalidationListener listener = (observable -> {
			ObservableValue<T> obs = (ObservableValue<T>) observable;
			App.doWork(s -> {
				s.merge(obs.getValue());
			});
			ExpenseTableController.this.updateRows();
			updateSubtotal();
		});
		return new WeakInvalidationListener(listener);
	}
	
	public <T> Callback<TreeTableColumn.CellDataFeatures<ExpenseTreeTableItem, T>, ObservableValue<T>> cellFactory(
			Function<TreeTableColumn.CellDataFeatures<ExpenseTreeTableItem, T>, T> getter
	) {
		ObjectProperty<T> prop = new SimpleObjectProperty<>();
		prop.addListener(createListener());
		return (cdf) -> {
			prop.set(getter.apply(cdf));
			return prop;
		};
	}
	
	public void setupTable() {
		nameCol.setCellValueFactory(cellFactory(cdf -> cdf.getValue().getValue().getName()));
		projectedCostCol.setCellValueFactory(cellFactory(cdf -> cdf.getValue().getValue().getProjectedCost(timePeriod)));
		actualCostCol.setCellValueFactory(cellFactory(cdf -> cdf.getValue().getValue().getCost(timePeriod)));
		differenceCol.setCellValueFactory(cellFactory(cdf -> cdf.getValue().getValue().difference(timePeriod)));
		nameCol.setCellFactory(new GenericTableCellFactory<>(s -> s));
		projectedCostCol.setCellFactory(new GenericTableCellFactory<>(s -> String.format("%.2f", s)));
		actualCostCol.setCellFactory(new GenericTableCellFactory<>(s -> String.format("%.2f", s)));
		differenceCol.setCellFactory(new GenericTableCellFactory<>(s -> String.format("%.2f", s)));
		differenceCol.setEditable(false);
		updateRows();
		mainTable.setRoot(troot);
	}
	
	public void updateRows() {
		List<Category> categories;
		
		List<TreeItem<ExpenseTreeTableItem>> items = new ArrayList<>();
		
		try (Session s = App.s()) {
			categories = s.createNamedQuery("getAllCategories", Category.class)
					.setParameter("user", App.getCurrentUser()).getResultList();
		}
		
		for (Category cat : categories) {
			TreeItem<ExpenseTreeTableItem> ti = new TreeItem<>(new CategoryIntermediate(cat));
			try (Session s = App.s()) {
				List<ExpenseInstance> eis = s.createNamedQuery("getExpenseInstancesForCategory", ExpenseInstance.class)
						.setParameter("tp", timePeriod.ID).setParameter("ec", cat.ID).getResultList();
				List<TreeItem<ExpenseTreeTableItem>> list = new ArrayList<>();
				eis.forEach(e -> {
					list.add(new TreeItem<>(e));
				});
				ti.getChildren().setAll(list);
			}
			items.add(ti);
		}
		troot.getChildren().setAll(items);
		updateSubtotal();
	}
	
	public void updateSubtotal() {
		double st = 0d;
		for (var it : troot.getChildren()) {
			st += it.getValue().getCost(timePeriod);
		}
		subtotal.setText(String.format("%.2f", st));
	}
}
