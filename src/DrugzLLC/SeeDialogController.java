package DrugzLLC;

import DrugzLLC.Tables.Doctor;
import DrugzLLC.Tables.Patient;
import DrugzLLC.Tables.See;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeeDialogController {
    // takes in a patient and links them to (a) doctors

    private Stage dialogStage;
    public Label dialogTitle;
    public TableView<Doctor> doctorTableView;
    private Patient patient;

    public void setDialogStage(Stage dialogStage, Patient patient) {
        this.dialogStage = dialogStage;
        dialogTitle.setText("Link who patient : " + "" + " sees.");
        this.patient = patient;
        initDoctorTableView();
    }


    public void onCancelClicked() {
        dialogStage.close();

    }

    public void onCreateLink() {
        if (doctorTableView.getSelectionModel().getSelectedItems() != null) {
            ObservableList<Doctor> doctorsObservableList;
                doctorsObservableList = doctorTableView.getSelectionModel().getSelectedItems();
//                Doctor doctor = doctorTableView.getSelectionModel().getSelectedItem();

                for (Doctor doctor : doctorsObservableList) {
                    // make connection between patient and doctors
                    See see = new See(doctor.getId(), patient.getSsn());
                    JDBCTools.insertIntoSee(Main.getConnection(), see);
                }

            dialogStage.close();
        }
        // else error??
        // unlink here too???
    }

    private void initDoctorTableView() {
        doctorTableView.getColumns().clear();

        TableColumn<Doctor, String> idColumn = new TableColumn<>("ID Number");
        idColumn.setCellValueFactory(new PropertyValueFactory<>(Doctor.ID));

        TableColumn<Doctor, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>(Doctor.LOCATION));

        TableColumn<Doctor, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>(Doctor.NAME));

        doctorTableView.setItems(getDoctorObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Controller.Table.Doctors.name())));

        doctorTableView.getColumns().add(idColumn);
        doctorTableView.getColumns().add(locationColumn);
        doctorTableView.getColumns().add(nameColumn);
        doctorTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // todo remove copy paste
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
}
