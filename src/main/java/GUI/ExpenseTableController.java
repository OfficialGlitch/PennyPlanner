package GUI;

import GUI.dialogs.AddExpenseTypeDialog;
import GUI.util.CategoryIntermediate;
import GUI.util.EditableTreeTableCell;
import GUI.util.ExpenseTreeTableItem;
import GUI.util.GenericTableCellFactory;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import models.Category;
import models.TimePeriod;
import models.instances.ExpenseInstance;
import models.money.Expense;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

public class ExpenseTableController implements Initializable {
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
	@FXML
	private Button addExpenseBtn;
	@FXML
	private Button addCategoryBtn;
	
	private TimePeriod timePeriod;
	
	private final TreeItem<ExpenseTreeTableItem> troot = new TreeItem<>(null);
	
	public static final String NAME_COLUMN = "expnse.name";
	
	public void setFields(TimePeriod tp) {
		this.timePeriod = tp;
//		mainTable.setEditable(true);
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
		return (cdf) -> {
			prop.set(getter.apply(cdf));
			if (cdf.getValue().getValue() instanceof ExpenseInstance) {
				cdf.getTreeTableColumn().setEditable(true);
			}
			cdf.getValue().valueProperty().addListener(createListener());
			return prop;
		};
	}
	
	public void setupTable() {
		updateRows();
		nameCol.setId(NAME_COLUMN);
		differenceCol.setEditable(false);
		nameCol.setCellValueFactory(cellFactory(cdf -> {
			if (cdf.getValue().getValue() == null) {
				return "";
			}
			return cdf.getValue().getValue().name();
		}));
		projectedCostCol.setCellValueFactory(cellFactory(cdf -> {
			if (cdf.getValue().getValue() == null) {
				return 0.0d;
			}
			return cdf.getValue().getValue().getProjectedCost(timePeriod);
		}));
		actualCostCol.setCellValueFactory(cellFactory(cdf -> {
			if (cdf.getValue().getValue() == null) {
				return 0.0d;
			}
			return cdf.getValue().getValue().getCost(timePeriod);
		}));
		differenceCol.setCellValueFactory(cellFactory(cdf -> {
			if (cdf.getValue().getValue() == null) {
				return 0.0d;
			}
			return cdf.getValue().getValue().difference(timePeriod);
		}));
		nameCol.setCellFactory(x -> new EditableTreeTableCell<>(
			s -> s,
			new DefaultStringConverter(),
			(item, value) -> {
				if (item instanceof ExpenseInstance) {
					var ei = (ExpenseInstance) item;
					ei.getExpense().setName(value);
					mergeItem(ei);
				} else if (item instanceof CategoryIntermediate) {
					var ci = (CategoryIntermediate) item;
					ci.category.setName(value);
					mergeItem(ci.category);
				}
			})
		);
		projectedCostCol.setCellFactory(x -> new EditableTreeTableCell<>(
			s -> String.format("%.2f", s),
			new DoubleStringConverter(),
			(item, value) -> {
				if (item instanceof ExpenseInstance) {
					var ei = (ExpenseInstance) item;
					ei.setProjectedCost(value);
					mergeItem(ei);
				}
			})
		);
		actualCostCol.setCellFactory(x -> new EditableTreeTableCell<>(
			s -> String.format("%.2f", s),
			new DoubleStringConverter(),
			(item, value) -> {
				if (item instanceof ExpenseInstance) {
					var ei = (ExpenseInstance) item;
					ei.setCost(value);
					mergeItem(ei);
				}
			})
		);
		differenceCol.setCellFactory(new GenericTableCellFactory<>(s -> String.format("%.1f", s)));
		updateRows();
	}
	
	void mergeItem(Object o) {
		Platform.runLater(() -> {
			try (Session s = App.sf().openSession()) {
				var tx = s.beginTransaction();
				s.merge(o);
				tx.commit();
				updateSubtotal();
				s.refresh(o);
			}
			updateSubtotal();
		});
	}
	
	public void updateRows() {
		List<Category> categories;
		
		List<TreeItem<ExpenseTreeTableItem>> items = new ArrayList<>();
		
		try (Session s = App.sf().openSession()) {
			categories = s.createNamedQuery("getAllCategories", Category.class)
				.setParameter("user", App.getCurrentUser().getID()).getResultList();
//			s.refresh(timePeriod);
		}
		
		for (Category cat : categories) {
			TreeItem<ExpenseTreeTableItem> ti = new TreeItem<>(new CategoryIntermediate(cat));
			List<ExpenseInstance> eis = App.s().createNamedQuery("getExpenseInstancesForCategory", ExpenseInstance.class)
				.setParameter("tp", timePeriod.ID.get()).setParameter("cat", cat.ID.get()).getResultList();
			List<TreeItem<ExpenseTreeTableItem>> list = new ArrayList<>();
			eis.forEach(e -> {
				list.add(new TreeItem<>(e));
			});
			ti.getChildren().setAll(list);
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainTable.setShowRoot(false);
		mainTable.setRoot(troot);
		var service = new ScheduledService<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						if (mainTable.getRoot() != null)
							mainTable.getRoot().getChildren().forEach(x -> {
								x.getValue().updateExpenses(timePeriod);
							});
						updateSubtotal();
						return null;
					}
				};
			}
		};
		service.setPeriod(Duration.seconds(1));
		service.start();
	}
	public void addExpense(ActionEvent ev) {
		Dialog<ButtonType> dialog = new Dialog<>();
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("dialogs/AddExpenseTypeDialog.fxml"));
		AddExpenseTypeDialog controller;
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		try {
			dialog.getDialogPane().setContent(loader.load());
		} catch(IOException e) {
			throw new RuntimeException("Failed to instantiate dialog");
		}
		controller = loader.getController();
		Optional<ButtonType> res = dialog.showAndWait();
		if(res.get() == ButtonType.OK) {
			Expense expense = controller.getData();
			App.doWork(x -> {
				x.persist(expense);
			});
			ExpenseInstance ei = new ExpenseInstance();
			ei.setExpense(expense);
			ei.setMonth(timePeriod);
			App.doWork(x -> {
				x.persist(ei);
			});
			updateRows();
		}
	}
	
	public void addCategory(ActionEvent event) {
	
	}
}
