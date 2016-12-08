package DrugzLLC;

import DrugzLLC.Tables.Doctor;
import DrugzLLC.Tables.Patient;
import DrugzLLC.Tables.Prescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public TableView<Doctor> doctorTableView;
    public TableView<Patient> patientTableView;
    public TableView<Prescription> prescriptionTableView;
    public TextField searchBarTextField;
    public Button deleteButton;

    public Button prescribeButton;
    public Button haveButton;
    public Button seeButton;

    private Table currentTable;

    public Label userFeedBackLabel;

    public void onSearchComplete() {
        userFeedBackLabel.setText("Search results for: " +  searchBarTextField.getText());
        switch (currentTable) {
            case Doctors:
                if (JDBCTools.isItemInTable(Main.getConnection(), Table.Doctors.name(), Doctor.NAME_JDBC_KEY, searchBarTextField.getText())) {
                    doctorTableView.setItems(getDoctorObservableList(JDBCTools.getResultSetInDB(
                            Main.getConnection(),
                            Table.Doctors.name(),
                            Doctor.NAME_JDBC_KEY,
                            searchBarTextField.getText()
                    )));
                } else {
                    showErrorAlertDialog("Doctor not found.");
                }
                break;
            case Patients:
                if (JDBCTools.isItemInTable(Main.getConnection(), Table.Patients.name(), Patient.LNAME_JDBC_KEY, searchBarTextField.getText())) {
                    patientTableView.setItems(getPatientObservableList(JDBCTools.getResultSetInDB(
                            Main.getConnection(), Table.Patients.name(),
                            Patient.LNAME_JDBC_KEY,
                            searchBarTextField.getText())));
                } else {
                    showErrorAlertDialog("Patient not found.");
                }
                break;
            case Prescriptions:
                if (JDBCTools.isItemInTable(Main.getConnection(), Table.Prescriptions.name(), Prescription.RX_JDBC_KEY, searchBarTextField.getText())) {
                    prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.getResultSetInDB(
                            Main.getConnection(),
                            Table.Prescriptions.name(),
                            Prescription.RX_JDBC_KEY,
                            searchBarTextField.getText())));
                } else {
                    showErrorAlertDialog("Prescription not found.");
                }
                break;
            case have:
                break;
            case prescribe:
                break;
            case see:
                break;
        }
    }

    public void onDoctorsClicked() {
        currentTable = Table.Doctors;
        searchBarTextField.setPromptText("Search doctors name");
        userFeedBackLabel.setText("");

        doctorTableView.setVisible(true);
        patientTableView.setVisible(false);
        prescriptionTableView.setVisible(false);

        haveButton.setVisible(false);
        seeButton.setVisible(false);
        prescribeButton.setVisible(true);

        doctorTableView.setItems(getDoctorObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Table.Doctors.name())));
    }

    public void onPatientsClicked() {
        currentTable = Table.Patients;
        // todo add filter to search bar
        searchBarTextField.setPromptText("Search patients last name");
        userFeedBackLabel.setText("");

        doctorTableView.setVisible(false);
        patientTableView.setVisible(true);
        prescriptionTableView.setVisible(false);

        prescribeButton.setVisible(false);
        haveButton.setVisible(true);
        seeButton.setVisible(true);

        patientTableView.setItems(getPatientObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Table.Patients.name())));
    }

    public void onPrescriptionsClicked() {
        currentTable = Table.Prescriptions;
        searchBarTextField.setPromptText("Search prescription #");
        userFeedBackLabel.setText("");

        prescribeButton.setVisible(false);
        haveButton.setVisible(false);
        seeButton.setVisible(false);

        doctorTableView.setVisible(false);
        patientTableView.setVisible(false);
        prescriptionTableView.setVisible(true);

        prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Table.Prescriptions.name())));
    }

    public void onSeeClicked() {
        Patient patient = patientTableView.getSelectionModel().getSelectedItem();
// todo show label of who was searched for
        if (patient != null) {
            onDoctorsClicked();
            userFeedBackLabel.setText("Showing which doctor(s) patient : " + patient.getLastName() + " sees.");

            // check for num??
            doctorTableView.setItems(getDoctorObservableList(JDBCTools.getResultSetNaturalJoinInDB(
                    Main.getConnection(),
                    Table.Doctors.name(),
                    Table.see.name(),
                    Patient.SSN_JDBC_KEY,
                    patient.getSsn()
            )));
        }


    }

    public void onHaveClicked() {
        Patient patient = patientTableView.getSelectionModel().getSelectedItem();
// todo show label of who was searched for
        if (patient != null) {
            onPrescriptionsClicked();
            userFeedBackLabel.setText("Showing which prescription(s) patient : " + patient.getLastName() + " has.");
            // check for num????
            prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.getResultSetNaturalJoinInDB(
                    Main.getConnection(),
                    Table.Prescriptions.name(),
                    Table.have.name(),
                    Patient.SSN_JDBC_KEY,
                    patient.getSsn()
            )));
        }

    }

    public void onPrescribeClicked() {
        Doctor doctor = doctorTableView.getSelectionModel().getSelectedItem();
        if(doctor != null) {
            onPrescriptionsClicked();
            userFeedBackLabel.setText("Showing which prescription(s) doctor : " + doctor.getName() + " prescribes.");
            prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.getResultSetNaturalJoinInDB(
                    Main.getConnection(),
                    Table.Prescriptions.name(),
                    Table.prescribe.name(),
                    Doctor.ID_JDBC_KEY,
                    doctor.getId()
            )));
        }
    }

    public void onAddClicked() {
        switch (currentTable) {
            case Doctors:
                showAddDoctorDialog();
                onDoctorsClicked();
                break;
            case Patients:
                showAddPatientDialog();
                onPatientsClicked();
                break;
            case Prescriptions:
                showAddPrescriptionDialog();
                onPrescriptionsClicked();
                break;
            case have:
                break;
            case prescribe:
                break;
            case see:
                break;
        }
    }

    public void onDeleteClicked() {
        switch (currentTable) {
            case Doctors:
                deleteSelectedDoctors();
                break;
            case Patients:
                deleteSelectedPatients();
                break;
            case Prescriptions:
                deleteSelectedPrescriptions();
                break;
            case have:
                break;
            case prescribe:
                break;
            case see:
                break;
        }
    }

    public void onEditClicked() {
        switch (currentTable) {
            case Doctors:
                break;
            case Patients:
                break;
            case Prescriptions:
                break;
            case have:
                break;
            case prescribe:
                break;
            case see:
                break;
        }
    }

    private void showAddDoctorDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add_doctor_dialog.fxml"));
            StackPane stackPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(stackPane);

            dialogStage.setScene(scene);
            AddDoctorDialogController addDoctorDialogController = loader.getController();
            addDoctorDialogController.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAddPatientDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add_patient_dialog.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(anchorPane);

            dialogStage.setScene(scene);
            AddPatientDialogController addPatientDialogController = loader.getController();
            addPatientDialogController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAddPrescriptionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add_prescription_dialog.fxml"));
            StackPane stackPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(stackPane);

            dialogStage.setScene(scene);
            AddPrescriptionDialogController addPrescriptionDialogController = loader.getController();
            addPrescriptionDialogController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedPatients() {
        ObservableList<Patient> patientObservableList, allPatients;
        allPatients = patientTableView.getItems();
        patientObservableList = patientTableView.getSelectionModel().getSelectedItems();
        for (Patient patient : patientObservableList) {
            // delete from data base
            JDBCTools.deleteFromPatient(Main.getConnection(), patient.getSsn());
        }

        patientObservableList.forEach(allPatients::remove);

    }

    private void deleteSelectedDoctors() {
        // TODO remove check
        if (doctorTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Doctor");
            alert.setHeaderText("Are you sure you would like to delete " +
                    doctorTableView.getSelectionModel().getSelectedItem().getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ObservableList<Doctor> doctorsObservableList, allDoctors;
                allDoctors = doctorTableView.getItems();
                doctorsObservableList = doctorTableView.getSelectionModel().getSelectedItems();
                for (Doctor doctor : doctorsObservableList) {
                    // delete from data base
                    JDBCTools.deleteFromDoctor(Main.getConnection(), doctor.getId());
                }

                doctorsObservableList.forEach(allDoctors::remove);
            }
        }
    }

    private void deleteSelectedPrescriptions() {
        ObservableList<Prescription> prescriptionObservableList, allPrescriptions;
        allPrescriptions = prescriptionTableView.getItems();
        prescriptionObservableList = prescriptionTableView.getSelectionModel().getSelectedItems();
        for (Prescription prescription : prescriptionObservableList) {
            // delete from data base
            JDBCTools.deleteFromPrescription(Main.getConnection(), prescription.getRx());
        }

        prescriptionObservableList.forEach(allPrescriptions::remove);
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

    private void initDoctorTableView() {
        doctorTableView.getColumns().clear();

        TableColumn<Doctor, String> idColumn = new TableColumn<>("ID Number");
        idColumn.setCellValueFactory(new PropertyValueFactory<>(Doctor.ID));

        TableColumn<Doctor, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>(Doctor.LOCATION));

        TableColumn<Doctor, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>(Doctor.NAME));

        doctorTableView.setItems(getDoctorObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Table.Doctors.name())));

        doctorTableView.getColumns().add(idColumn);
        doctorTableView.getColumns().add(locationColumn);
        doctorTableView.getColumns().add(nameColumn);
    }

    private void initPatientTableView() {
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

        patientTableView.setItems(getPatientObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Table.Patients.name())));

        patientTableView.getColumns().add(ssnColumn);
        patientTableView.getColumns().add(firstNameColumn);
        patientTableView.getColumns().add(middleNameColumn);
        patientTableView.getColumns().add(lastNameColumn);
        patientTableView.getColumns().add(dateOfBirthColumn);
        patientTableView.getColumns().add(insuranceNameColumn);
        patientTableView.getColumns().add(addressNameColumn);
    }

    private void initPrescriptionTableView() {
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

        prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Table.Prescriptions.name())));

        prescriptionTableView.getColumns().add(rxColumn);
        prescriptionTableView.getColumns().add(nameColumn);
        prescriptionTableView.getColumns().add(numSuppliedColumn);
        prescriptionTableView.getColumns().add(numRefillsColumn);
        prescriptionTableView.getColumns().add(sideEffectsColumn);
    }

    private void showErrorAlertDialog(String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDoctorTableView();
        initPatientTableView();
        initPrescriptionTableView();

        onPatientsClicked();
//        patientTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Patient>() {
//            @Override
//            public void changed(ObservableValue<? extends Patient> observable, Patient oldValue, Patient newValue) {
//                deleteButton.setVisible(true);
//                System.out.println(oldValue + " nv " + newValue);
//            }
//        });
    }

    private enum Table {
        Patients, Prescriptions, Doctors, see, prescribe, have;
    }
}
