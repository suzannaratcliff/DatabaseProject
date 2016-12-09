package DrugzLLC;

import DrugzLLC.Tables.Doctor;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SeeDialogController {
    // takes in a patient and links them to (a) doctors

    private Stage dialogStage;
    public Label dialogTitle;
    public TableView<Doctor> doctorTableView;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogTitle.setText("Link who patient : " + "" + " sees.");

    }


    public void onCancelClicked() {
        dialogStage.close();

    }

    public void onCreateLink() {
        dialogStage.close();
    }
}
