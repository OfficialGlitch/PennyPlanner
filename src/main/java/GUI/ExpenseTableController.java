package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ExpenseTableController {
    @FXML
    public TreeTableView mainTable;
    @FXML
    public TreeTableColumn nameCol;
    @FXML
    public TreeTableColumn projectedCostCol;
    @FXML
    public TreeTableColumn actualCostCol;
    @FXML
    public TreeTableColumn differenceCol;
    @FXML
    public AnchorPane subtotalWrapper;
    @FXML
    public Text subtotalLabel;
    @FXML
    public Text dollarSign;
    @FXML
    public Text subtotal;
}
