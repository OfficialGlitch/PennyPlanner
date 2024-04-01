package GUI;

import GUI.util.*;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import models.instances.IncomeInstance;
import models.money.Income;
import org.hibernate.Session;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

public class IncomeTableController implements Initializable
{
	@FXML
	private AnchorPane actualTotalWrapper;
	@FXML
	private Text actualTotal;
	@FXML
	private Text actualTotalLabel;
	@FXML
	private Text actualTotalCurrency;
	@FXML
	private TableView<IncomeInstance> mainTable;
	@FXML
	private TableColumn<IncomeInstance, String> nameCol;
	@FXML
	private TableColumn<IncomeInstance, Double> actualCol;
	@FXML
	private TableColumn<IncomeInstance, Double> projectedCol;
	@FXML
	private TableColumn<IncomeInstance, Double> differenceCol;
	@FXML
	private Button addIncomeBtn;
	@FXML
	private AnchorPane projectedWrapper;
	@FXML
	private Text projectedTotal;
	@FXML
	private Text projectedLabel;
	@FXML
	private Text projectedCurrency;
	@FXML
	private AnchorPane differenceWrapper;
	@FXML
	private Text totalDifference;
	@FXML
	private Text differenceLabel;
	@FXML
	private Text differenceCurrencySign;
	private TimePeriod timePeriod;
	
	public void setFields(TimePeriod tp) {
		this.timePeriod = tp;
		this.setup();
	}
	public void setup() {
		setRows();
		nameCol.setEditable(true);
		actualCol.setEditable(true);
		projectedCol.setEditable(true);
		nameCol.setCellValueFactory(cellFactory(cdf -> {
			if (cdf.getValue() == null) {
				return "";
			}
			return cdf.getValue().getIncomeSource().getName();
		}));
		projectedCol.setCellValueFactory(cellFactory(cdf -> {
			if (cdf.getValue() == null) {
				return 0.0d;
			}
			return cdf.getValue().getProjected();
		}));
		actualCol.setCellValueFactory(cellFactory(cdf -> {
			if (cdf.getValue() == null) {
				return 0.0d;
			}
			return cdf.getValue().getAmount();
		}));
		differenceCol.setCellValueFactory(cellFactory(cdf -> {
			if (cdf.getValue() == null) {
				return 0.0d;
			}
			return cdf.getValue().getAmount() - cdf.getValue().getProjected();
		}));
		nameCol.setCellFactory(new GenericTableCellFactory<>(
			s -> s,
			new DefaultStringConverter(),
			(item, value) -> {
				item.getIncomeSource().setName(value);
				mergeItem(item);
				mergeItem(item.getIncomeSource());
			})
		);
		projectedCol.setCellFactory(new GenericTableCellFactory<>(
			s -> String.format("%.2f", s),
			new DoubleStringConverter(),
			(item, value) -> {
				item.setProjected(value);
				mergeItem(item);
			})
		);

		actualCol.setCellFactory(column -> new TableCell<IncomeInstance, Double>() {
			private final TextField textField = new TextField();
			private final DoubleStringConverter converter = new DoubleStringConverter();

			@Override
			public void startEdit() {
				if (!isEmpty()) {
					super.startEdit();
					textField.setText(getItem().toString());
					setText(null);
					setGraphic(textField);
					textField.requestFocus();
				}
			}

			@Override
			public void cancelEdit() {
				super.cancelEdit();
				setText(converter.toString(getItem()));
				setGraphic(null);
			}

			@Override
			public void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else if (isEditing()) {
					setText(null);
					textField.setText(converter.toString(getItem()));
					setGraphic(textField);
				} else {
					setText(converter.toString(getItem()));
					setGraphic(null);
				}
			}

			@Override
			public void commitEdit(Double newValue) {
				// This is where you can perform the validation
				if (newValue == null || newValue >= 0) {
					super.commitEdit(newValue);
					IncomeInstance instance = getTableView().getItems().get(getIndex());
					instance.setAmount(newValue);
					mergeItem(instance); // Save the updated instance
				} else {
					// Optionally, show an alert dialog to inform the user that negative values are not allowed
					Alert alert = new Alert(Alert.AlertType.ERROR, "Negative values are not allowed.");
					alert.showAndWait();
				}
			}

			private void createTextField() {
				textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
				textField.setOnAction(evt -> commitEdit(converter.fromString(textField.getText())));
				textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
					if (!newValue) {
						commitEdit(converter.fromString(textField.getText()));
					}
				});
			}

			{
				createTextField();
			}
		});

		differenceCol.setCellFactory(new GenericTableCellFactory<>(
			s -> String.format("%.1f", s),
			new DoubleStringConverter(),
			(ii, d) -> {}
			)
		);
		setRows();
	}
	public void setRows() {
		List<IncomeInstance> incomes;
		incomes = App.s().createNamedQuery("getIncomesForTimePeriod", IncomeInstance.class)
			.setParameter("user", App.getCurrentUser().getID())
			.setParameter("month", timePeriod.getID())
			.getResultList();
		mainTable.getItems().setAll(incomes);
		mainTable.itemsProperty().addListener(createListener());
		updateTotal();
	}
	@FXML
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		var ss = new ScheduledService<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<>() {
					@Override
					protected Void call() throws Exception {
						updateTotal();
						return null;
					}
				};
			}
		};
		ss.setPeriod(Duration.seconds(1));
		ss.start();
	}
	void mergeItem(Object o) {
		Platform.runLater(() -> {
			try (Session s = App.sf().openSession()) {
				var tx = s.beginTransaction();
				s.merge(o);
				tx.commit();
				updateTotal();
				s.refresh(o);
			}
			updateTotal();
		});
	}
	public <T> Callback<TableColumn.CellDataFeatures<IncomeInstance, T>, ObservableValue<T>> cellFactory(
		Function<TableColumn.CellDataFeatures<IncomeInstance, T>, T> getter
	) {
		ObjectProperty<T> prop = new SimpleObjectProperty<>();
		return (cdf) -> {
			prop.set(getter.apply(cdf));
			return prop;
		};
	}
	public <T> WeakInvalidationListener createListener() {
		InvalidationListener listener = (observable -> {
			ObservableList<T> obs = (ObservableList<T>) observable;
			obs.forEach(x -> {
				App.doWork(s -> {
					s.merge(x);
				});
			});
			IncomeTableController.this.setRows();
			updateTotal();
		});
		return new WeakInvalidationListener(listener);
	}
	public double getIncomeAmount() {
		double ret = 0d;
			for(var it : mainTable.getItems()) {
				ret += it.getAmount();
			}
			return ret;
	}
	public double getProjectedIncomeAmount() {
		double ret = 0d;
		for(var it : mainTable.getItems()) {
			ret += it.getProjected();
		}
		return ret;
	}
	public void updateTotal() {
		double actualTotal = getIncomeAmount();
		double projectedTotal = getProjectedIncomeAmount();
		double difference = 0d;
		for (var it : mainTable.getItems()) {
			difference += it.getAmount() - it.getProjected() ;
		}
		totalDifference.getStyleClass().clear();
		if(difference < 0) {
			totalDifference.getStyleClass().addAll("text", "danger");
		} else if(difference > 0) {
			totalDifference.getStyleClass().addAll("text", "success");
		}
		this.actualTotal.setText(String.format("%.2f", actualTotal));
		this.totalDifference.setText(String.format("%.2f", difference));
		this.projectedTotal.setText(String.format("%.2f", projectedTotal));
	}
	
	@FXML
	public void addIncome(ActionEvent ev) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.getDialogPane().getStylesheets().add(App.class.getResource("style.css").toExternalForm());
		dialog.setTitle("Add new income source");
		dialog.setGraphic(null);
		dialog.setHeaderText(null);
		Optional<String> res = dialog.showAndWait();
		if(res.isPresent()) {
			if (!res.get().isEmpty()) {
				Income cat = new Income();
				cat.setName(res.get());
				cat.setUser(App.getCurrentUser());
				App.doWork(x -> {
					x.persist(cat);
				});
				var tps = App.s().createNamedQuery("timePeriodsForYear", TimePeriod.class)
						.setParameter("uid", App.getCurrentUser().getID())
							.setParameter("year", timePeriod.getYear()).getResultList();
				for (TimePeriod tp : tps) {
					IncomeInstance ii = new IncomeInstance();
					ii.setIncomeSource(cat);
					ii.setMonth(tp);
					
					App.doWork(x -> {
						x.persist(ii);
					});
				}
				setRows();
			}
		}
	}
	@FXML
	public void deleteIncome(ActionEvent ev) {
		// Get the selected item from the table
		IncomeInstance selectedInstance = mainTable.getSelectionModel().getSelectedItem();
		System.out.println("THIS IS THE INSTANCE: "+selectedInstance);
		// Check if an item is selected
		if (selectedInstance != null) {
			Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
			confirmDeleteAlert.setTitle("Delete Income Source");
			confirmDeleteAlert.setHeaderText("Confirm deletion");
			confirmDeleteAlert.setContentText("Are you sure you want to delete this income source?");

			Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				// Perform the deletion in a background task
				Platform.runLater(() -> {
					try (Session session = App.sf().openSession()) {
						var tx = session.beginTransaction();
						// Corrected HQL query to use proper entity name and parameter binding
						String hql = "delete from incomes where id = :id";
						int deletedEntities = session.createQuery(hql)
							.setParameter("id", selectedInstance.getID())
							.executeUpdate();
						System.out.println("Entities deleted: " + deletedEntities); // For debugging
						tx.commit();
					}
					// Update the table view after deletion
					setRows();
				});
			}
		} else {
			// If no item is selected, show an alert to the user
			Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
			noSelectionAlert.setTitle("No Selection");
			noSelectionAlert.setHeaderText("No Income Source Selected");
			noSelectionAlert.setContentText("Please select an income source to delete.");
			noSelectionAlert.showAndWait();
		}
	}

}