package controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DateController implements Initializable {

    @FXML
    private Label label_result;
    @FXML
    private Button btn_calcular;
    @FXML
    private DatePicker datepicker1;
    @FXML
    private DatePicker datepicker2;
    @FXML
    private AnchorPane fondo;
    private File file;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File aux = new File("src/historial/historial.txt");
        file = new File(aux.getAbsolutePath());
    }

    public void calcular() {
        LocalDate date1 = datepicker1.getValue();
        LocalDate date2 = datepicker2.getValue();

        if (date1 != null && date2 != null) {
            Duration duration = Duration.between(date1.atStartOfDay(), date2.atStartOfDay());
            long days = duration.toDays();
            long hours = duration.toHours();
            long minutes = duration.toMinutes();
            this.label_result.setText(days+" días "+hours+" horas y "+minutes+" minutos de diferencia.");
            Operaciones.escribirFichero(this.datepicker1.getValue().toString()+"|"+this.datepicker2.getValue().toString()+"@", file);
            Operaciones.escribirFichero(days+"dd/"+hours+"h/"+minutes+"min"+"\n", file);
        } else {
            this.label_result.setText("Seleccione dos fechas para calcular la diferencia.");
        }
    }

    @FXML
    private void onclick(ActionEvent event) {
        calcular();
    }

    @FXML
    private void cambiar_divisa(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/Divisas.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void cambiar_normal(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/FXMLDocument.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void cambiar_cientifica(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/Cientifica.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void cambiar_estilo(ActionEvent event) {
        FileChooser fchooser = new FileChooser();
        fchooser.setTitle("Seleccionar Hoja de estilos.");
        fchooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSS", "*.css")
        );
        File css_styles = fchooser.showOpenDialog(new Stage());
        if (css_styles != null) {
            String cssFilePath = css_styles.toURI().toString();
            Scene scene = this.btn_calcular.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(cssFilePath);
        }
    }

    @FXML
    private void abrir_historial(ActionEvent event) {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/vista/Historial.fxml"));
            Pane root = (Pane) fxml.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Historial");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
