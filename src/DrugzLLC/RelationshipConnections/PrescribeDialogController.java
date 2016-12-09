package DrugzLLC.RelationshipConnections;

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.TableTools;
import DrugzLLC.Tables.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class PrescribeDialogController {

        private Stage dialogStage;
        public Label dialogTitle;
        public TableView<Prescription> prescriptionTableView;
        private Doctor doctor;

        public void setDialogStage(Stage dialogStage, Doctor doctor) {
            this.dialogStage = dialogStage;
            dialogTitle.setText("Link what prescription patient : " + doctor.getName() + " prescribes.");
            this.doctor = doctor;
            TableTools.initPrescriptionTableView(prescriptionTableView);
        }

        public void onCancelClicked() {
            dialogStage.close();
        }

        public void onCreateLink() {
            if (prescriptionTableView.getSelectionModel().getSelectedItems() != null) {
                ObservableList<Prescription> prescriptionObservableList;
                prescriptionObservableList = prescriptionTableView.getSelectionModel().getSelectedItems();
                for (Prescription prescription : prescriptionObservableList) {
                    // todo why int???????
                    Prescribe prescribe = new Prescribe(prescription.getRx(), Integer.parseInt(doctor.getId()));
                    JDBCTools.insertIntoPrescripe(Main.getConnection(), prescribe);
                }
                dialogStage.close();
            }
            // else error??
            // unlink here too???
        }


}
