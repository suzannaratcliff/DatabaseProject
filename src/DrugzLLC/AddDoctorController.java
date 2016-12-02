package DrugzLLC;

import DrugzLLC.Tables.Doctor;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddDoctorController {

    public TextField nameTextField;
    public TextField locationTextField;
    public TextField idNumberTextField;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void onCancelClicked() {
        dialogStage.close();
    }

    public void onAddClicked() {
        Doctor doctor = new Doctor(idNumberTextField.getText(), locationTextField.getText(), idNumberTextField.getText());
        boolean inserted = JDBCTools.insertIntoDoctor(Main.connection, doctor);
        if (inserted) {
            dialogStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Error!");
            alert.setContentText("Doctor: " + nameTextField.getText() + "with the Id: " + idNumberTextField.getText() + " is already in the system.");
            alert.showAndWait();
        }
    }
}
