package DrugzLLC.Tools;

import DrugzLLC.Tables.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientTools {

    public static TableView<Patient> createTableViewFromResultSet(ResultSet resultSet) {
        TableColumn<Patient, String> ssnColumn = new TableColumn<>("Social Security Numer");
        ssnColumn.setCellValueFactory(new PropertyValueFactory<>(Patient.SSN));

        TableColumn<Patient, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>(Patient.FIRST_NAME));

        TableColumn<Patient, String> middleNameColumn = new TableColumn<>("Middle Name");
        middleNameColumn.setCellValueFactory(new PropertyValueFactory<>(Patient.MIDDLE_NAME));

        TableColumn<Patient, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>(Patient.LAST_NAME));

        TableColumn<Patient, String> dateOfBirthColumn = new TableColumn<>("Date Of Birth");
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>(Patient.DATE_OF_BIRTH));

        TableColumn<Patient, String> insuranceNameColumn = new TableColumn<>("Insurance Company");
        insuranceNameColumn.setCellValueFactory(new PropertyValueFactory<>(Patient.INSURANCE_NAME));

        TableColumn<Patient, String> addressNameColumn = new TableColumn<>("Address");
        addressNameColumn.setCellValueFactory(new PropertyValueFactory<>(Patient.ADDRESS));

        TableView<Patient> patientTableView = new TableView<>();
        patientTableView.setItems(getPatientObservableList(resultSet));

        patientTableView.getColumns().add(ssnColumn);
        patientTableView.getColumns().add(firstNameColumn);
        patientTableView.getColumns().add(middleNameColumn);
        patientTableView.getColumns().add(lastNameColumn);
        patientTableView.getColumns().add(dateOfBirthColumn);
        patientTableView.getColumns().add(insuranceNameColumn);
        patientTableView.getColumns().add(addressNameColumn);

        return patientTableView;
    }

    private static ObservableList<Patient> getPatientObservableList(ResultSet resultSet) {

        ObservableList<Patient> patients = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                Patient patient = new Patient(resultSet.getString(Patient.SSN_JDBC_KEY),
                        resultSet.getString(Patient.FNAME_JDBC_KEY),
                        resultSet.getString(Patient.MNAME_JDBC_KEY),
                        resultSet.getString(Patient.LNAME_JDBC_KEY),
                        resultSet.getString(Patient.DOB_JDBC_KEY),
                        resultSet.getString(Patient.INSURANCE_JDBC_KEY),
                        resultSet.getString(Patient.ADDRESS_JDBC_KEY)
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

}
