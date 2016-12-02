package DrugzLLC.Tools;

import DrugzLLC.Tables.Prescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrescriptionTools {

    public static TableView<Prescription> createTableViewFromResultSet(ResultSet resultSet) {
        TableColumn<Prescription, Integer> rxColumn = new TableColumn<>("RX");
        rxColumn.setCellValueFactory(new PropertyValueFactory<>(Prescription.RX));

        TableColumn<Prescription, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>(Prescription.NAME));

        TableColumn<Prescription, Integer> numSuppliedColumn = new TableColumn<>("Number Supplied");
        numSuppliedColumn.setCellValueFactory(new PropertyValueFactory<>(Prescription.NUMBER_SUPPLIED));

        TableColumn<Prescription, Integer> numRefillsColumn = new TableColumn<>("Number of Refills");
        numRefillsColumn.setCellValueFactory(new PropertyValueFactory<>(Prescription.NUMBER_REFILLS));

        TableColumn<Prescription, String> sideEffectsColumn = new TableColumn<>("Side Effects");
        sideEffectsColumn.setCellValueFactory(new PropertyValueFactory<>(Prescription.SIDE_EFFECTS));

        TableView<Prescription> prescriptionTableView = new TableView<>();
        prescriptionTableView.setItems(getPrescriptionObservableList(resultSet));

        prescriptionTableView.getColumns().add(rxColumn);
        prescriptionTableView.getColumns().add(nameColumn);
        prescriptionTableView.getColumns().add(numSuppliedColumn);

        return prescriptionTableView;
    }

    private static ObservableList<Prescription> getPrescriptionObservableList(ResultSet resultSet) {
        ObservableList<Prescription> prescriptions = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                Prescription prescription = new Prescription(
                        resultSet.getInt(Prescription.RX_JDBC_KEY),
                        resultSet.getString(Prescription.NAME_JDBC_KEY),
                        resultSet.getInt(Prescription.NUM_SUPPLIED_JDBC_KEY),
                        resultSet.getInt(Prescription.NUM_REFILLS_JDBC_KEY),
                        resultSet.getString(Prescription.SIDE_EFFECTS_JDBC_KEY)
                );
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }

}
