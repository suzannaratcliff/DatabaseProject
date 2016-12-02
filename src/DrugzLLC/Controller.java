package DrugzLLC;

import DrugzLLC.Tables.Doctor;
import DrugzLLC.Tables.Patient;
import DrugzLLC.Tables.Prescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Connection connection;

    @FXML
    TableView<Doctor> doctorTableView;
    @FXML
    TableView<Patient> patientTableView;
    @FXML
    TableView<Prescription> prescriptionTableView;


    public void onSearchComplete() {


    }

    public void onDoctorsPressed() {
        doctorTableView.setVisible(true);
        patientTableView.setVisible(false);
        prescriptionTableView.setVisible(false);

        doctorTableView.getColumns().clear();

        TableColumn<Doctor, String> idColumn = new TableColumn<>("ID Number");
        idColumn.setCellValueFactory(new PropertyValueFactory<>(Doctor.ID));

        TableColumn<Doctor, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>(Doctor.LOCATION));

        TableColumn<Doctor, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>(Doctor.NAME));

        doctorTableView.setItems(getDoctorObservableList(JDBCTools.retrieveAllItems(connection, "Doctors")));

        doctorTableView.getColumns().add(idColumn);
        doctorTableView.getColumns().add(locationColumn);
        doctorTableView.getColumns().add(nameColumn);
    }

    public void onPatientsPressed() {
        doctorTableView.setVisible(false);
        patientTableView.setVisible(true);
        prescriptionTableView.setVisible(false);

        patientTableView.getColumns().clear();

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

        patientTableView.setItems(getPatientObservableList(JDBCTools.retrieveAllItems(connection, "Patients")));

        patientTableView.getColumns().add(ssnColumn);
        patientTableView.getColumns().add(firstNameColumn);
        patientTableView.getColumns().add(middleNameColumn);
        patientTableView.getColumns().add(lastNameColumn);
        patientTableView.getColumns().add(dateOfBirthColumn);
        patientTableView.getColumns().add(insuranceNameColumn);
        patientTableView.getColumns().add(addressNameColumn);
    }

    public void onPrescriptionsPressed() {
        doctorTableView.setVisible(false);
        patientTableView.setVisible(false);
        prescriptionTableView.setVisible(true);

        prescriptionTableView.getColumns().clear();

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

        prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.retrieveAllItems(connection, "Prescriptions")));

        prescriptionTableView.getColumns().add(rxColumn);
        prescriptionTableView.getColumns().add(nameColumn);
        prescriptionTableView.getColumns().add(numSuppliedColumn);
    }

    private ObservableList<Doctor> getDoctorObservableList(ResultSet resultSet) {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                Doctor doctor = new Doctor(
                        resultSet.getString(Doctor.ID_JDBC_KEY),
                        resultSet.getString(Doctor.LOCATION_JDBC_KEY),
                        resultSet.getString(Doctor.NAME_JDBC_KEY)
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    private ObservableList<Patient> getPatientObservableList(ResultSet resultSet) {

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

    private ObservableList<Prescription> getPrescriptionObservableList(ResultSet resultSet) {
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Connect to the database
        connection = JDBCTools.connect("jdbc:sqlite:Pharmacy.db");

    }
}
