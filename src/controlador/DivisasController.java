package controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DivisasController implements Initializable {

    private final HashMap<String, Double> valoresLongitud = new HashMap<>();
    private HashMap<String, Integer> valoresTiempo = new HashMap<>();

    @FXML
    private ComboBox<String> tipo_conversion;
    @FXML
    private ComboBox<String> valor_origen;
    @FXML
    private ComboBox<String> valor_destino;
    @FXML
    private Label label_result;
    @FXML
    private AnchorPane fondo;
    private Operaciones op;
    @FXML
    private Button btn_aux;
    private File file;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File aux = new File("src/historial/historial.txt");
        file = new File(aux.getAbsolutePath());
        
        tipo_conversion.getItems().addAll(
                "Divisa",
                "Longitud",
                "Tiempo"
        );
        op = new Operaciones();

        valoresLongitud.put("mm", 0.001);
        valoresLongitud.put("cm", 0.01);
        valoresLongitud.put("dm", 0.1);
        valoresLongitud.put("dm", 0.1);
        valoresLongitud.put("m", 1.0);
        valoresLongitud.put("km", 1000.0);
        //----------------------------------------
        valoresTiempo.put("s", 60);
        valoresTiempo.put("m", 60);
        valoresTiempo.put("h", 60);
        valoresTiempo.put("dia", 24);
        valoresTiempo.put("año", 355);
    }

    //muestra el contenido concatenando texto en el label
    private void mostrarContenido(String in) {
        this.label_result.setText(this.label_result.getText() + in);
    }

    //obtiene los valores y los muestra por pantalla
    @FXML
    private void click(ActionEvent event) {
        String value = ((Button) (event.getSource())).getText();
        mostrarContenido(value);
    }

    //resetea el label result
    @FXML
    private void resetear(ActionEvent event) {
        this.label_result.setText("");
    }

    //ejecuta las operaciones de conversión tanto de Divisas,longitud y tiempo
    @FXML
    private void igual(ActionEvent event) {
        if (label_result.getText().isEmpty()) {
            return;
        }

        switch (tipo_conversion.getSelectionModel().getSelectedItem()) {
            case "Divisa" -> {
                //utilizo una API REST para obtener el resultado del valor unitario y realizo una regla de tres para sacar el valor en proporción
                String requestAPI = Operaciones.requestAPI(valor_origen.getSelectionModel().getSelectedItem());
                double a = Operaciones.divisa(requestAPI, valor_destino.getSelectionModel().getSelectedItem());
                double out = Operaciones.realizarCalculoDivisa(1, a, Double.parseDouble(label_result.getText()));
                Operaciones.escribirFichero(label_result.getText() + " " + valor_origen.getSelectionModel().getSelectedItem() + "@", file);
                this.label_result.setText(Operaciones.parsearDoubleToInt(out));
                Operaciones.escribirFichero(label_result.getText() + " " + valor_destino.getSelectionModel().getSelectedItem() + "\n", file);
            }
                
            case "Longitud" -> {
                //obtengo los valores de los combobox y consulto el hashmap para realizar las operaciones de longitud
                String key_origen = valor_origen.getSelectionModel().getSelectedItem();
                String key_destino = valor_destino.getSelectionModel().getSelectedItem();
                double origen = valoresLongitud.get(key_origen);
                double destino = valoresLongitud.get(key_destino);

                double result = Double.parseDouble(this.label_result.getText());
                Operaciones.escribirFichero(label_result.getText() + " " + valor_origen.getSelectionModel().getSelectedItem() + "@", file);
                this.label_result.setText(Operaciones.parsearDoubleToInt(result * origen / destino));
                Operaciones.escribirFichero(label_result.getText() + " " + valor_destino.getSelectionModel().getSelectedItem() + "\n", file);
            }

            case "Tiempo" -> {
                //calculo el tiempo en milisegundos y realizo las operaciones correspondientes
                String result = op.calcularTiempo(this.label_result.getText(), this.valor_origen.getSelectionModel().getSelectedItem(), valor_destino.getSelectionModel().getSelectedItem());
                Operaciones.escribirFichero(label_result.getText() + " " + valor_origen.getSelectionModel().getSelectedItem() + "@", file);
                this.label_result.setText(Operaciones.parsearDoubleToInt(Double.parseDouble(result)));
                Operaciones.escribirFichero(label_result.getText() + " " + valor_destino.getSelectionModel().getSelectedItem() + "\n", file);
            }
        }
    }

    //cuando abra el combobox que aparezcan sus correspondienets items el los combobox asociados
    private void abrir_combo(Event event) {
        switch (tipo_conversion.getSelectionModel().getSelectedItem()) {
            case "Divisa" -> {
                valor_origen.getItems().addAll(
                        "EUR",
                        "USD",
                        "GBP",
                        "JPY"
                );
                valor_destino.getItems().addAll(
                        "EUR",
                        "USD",
                        "GBP",
                        "JPY"
                );
            }
            case "Longitud" -> {
                valor_origen.getItems().addAll(
                        "mm",
                        "cm",
                        "dm",
                        "m",
                        "dcm",
                        "hm",
                        "km"
                );
                valor_destino.getItems().addAll(
                        "mm",
                        "cm",
                        "dm",
                        "m",
                        "dcm",
                        "hm",
                        "km"
                );
            }
            case "Tiempo" -> {
                valor_origen.getItems().addAll(
                        "milisegundos",
                        "segundos",
                        "minutos",
                        "horas",
                        "dias",
                        "semanas",
                        "años"
                );
                valor_destino.getItems().addAll(
                        "milisegundos",
                        "segundos",
                        "minutos",
                        "horas",
                        "dias",
                        "semanas",
                        "años"
                );
            }
        }
    }

    @FXML
    private void abrir_combo_origen(Event event) {
        switch (tipo_conversion.getSelectionModel().getSelectedItem()) {
            case "Divisa" -> {
                valor_origen.getItems().clear();
                valor_origen.getItems().addAll(
                        "EUR",
                        "USD",
                        "GBP",
                        "JPY"
                );
            }
            case "Longitud" -> {
                valor_origen.getItems().clear();
                valor_origen.getItems().addAll(
                        "mm",
                        "cm",
                        "dm",
                        "m",
                        "dcm",
                        "hm",
                        "km"
                );
            }
            case "Tiempo" -> {
                valor_origen.getItems().clear();
                valor_origen.getItems().addAll(
                        "milisegundos",
                        "segundos",
                        "minutos",
                        "horas",
                        "dias",
                        "semanas",
                        "años"
                );
            }
        }
    }

    @FXML
    private void abrir_combo_destino(Event event) {
        switch (tipo_conversion.getSelectionModel().getSelectedItem()) {
            case "Divisa" -> {
                valor_destino.getItems().clear();
                valor_destino.getItems().addAll(
                        "EUR",
                        "USD",
                        "GBP",
                        "JPY"
                );
            }

            case "Longitud" -> {
                valor_destino.getItems().clear();
                valor_destino.getItems().addAll(
                        "mm",
                        "cm",
                        "dm",
                        "m",
                        "dcm",
                        "hm",
                        "km"
                );
            }
            case "Tiempo" -> {
                valor_destino.getItems().clear();
                valor_destino.getItems().addAll(
                        "milisegundos",
                        "segundos",
                        "minutos",
                        "horas",
                        "dias",
                        "semanas",
                        "años"
                );
            }
        }
    }

    //cambio la vista a la calculadora científica
    @FXML
    private void ir_calc_cientifica(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/Cientifica.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //cambio la vista a la calculadora normal
    @FXML
    private void ir_calc_normal(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/FXMLDocument.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //cambio el estilo de la scene abriendo un filechooser y seleccionando un CSS
    @FXML
    private void cambio_color(ActionEvent event) {
        FileChooser fchooser = new FileChooser();
        fchooser.setTitle("Seleccionar Hoja de estilos.");
        fchooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSS", "*.css")
        );
        File css_styles = fchooser.showOpenDialog(new Stage());
        if (css_styles != null) {
            String cssFilePath = css_styles.toURI().toString();
            System.out.println(cssFilePath);
            Scene scene = this.btn_aux.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(cssFilePath);
        }
    }

    //abro la vista historial
    @FXML
    private void historial(ActionEvent event) {
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

    //cuando pulso teclas que registre solo números
    @FXML
    private void key_pressed(KeyEvent event) {
        String val = event.getText();
        if (Operaciones.isNumeric(val)) {
            this.mostrarContenido(val);
        }
    }

    @FXML
    private void abrir_fechas(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/Date.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
