package controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CientificaController implements Initializable {

    private static double memoria = 0;
    @FXML
    private Label resultado;
    @FXML
    private AnchorPane fondo;
    @FXML
    private Button btn_aux;

    private File file;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File aux = new File("src/historial/historial.txt");
        file = new File(aux.getAbsolutePath());
    }

    //abro la vista de la calculadora normal
    @FXML
    private void click_vuelta_calc(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/FXMLDocument.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //muestro el contenido por pantalla en el label reslt
    private void mostrarContenido(String in) {
        this.resultado.setText(this.resultado.getText() + in);
    }

    //muestro numeros
    @FXML
    private void numeros(ActionEvent event) {
        String numero = ((Button) (event.getSource())).getText();
        mostrarContenido(numero);
    }

    //sumo el valor del label a la memoria
    @FXML
    private void memorias_mas(ActionEvent event) {
        String texto = this.resultado.getText();
        double result = Operaciones.evaluarExpresion(texto);
        memoria += result;
        this.resultado.setText(memoria + "");
    }

    //Espacio de memoria menos, se encarga de restar la memoria con el texto del label
    @FXML
    private void memorias_menos(ActionEvent event) {
        String texto = this.resultado.getText();
        double result = Operaciones.evaluarExpresion(texto);
        memoria -= result;
        this.resultado.setText(memoria + "");
    }

    //resetea el label principal
    @FXML
    private void resetear(ActionEvent event) {
        this.resultado.setText("");
    }

    @FXML
    private void cambiar_signo(ActionEvent event) {
        if(this.resultado.getText().charAt(0)=='-'){
            this.resultado.setText(this.resultado.getText().substring(1));
        }else{
            this.resultado.setText("-"+this.resultado.getText());
        } 
    }

    //Muestro las operaciones
    @FXML
    private void operacion(ActionEvent event) {
        String value = ((Button) (event.getSource())).getText();
        if (value.contains("^")) {
            mostrarContenido("^");
        } else {
            mostrarContenido(value);
        }
    }

    //Realiza las cuentas de la calculadora y escribe en un fichero los resultados
    @FXML
    private void click_igual(ActionEvent event) {
        String texto = this.resultado.getText();
        Operaciones.escribirFichero(texto + "=", file);
        double result = Operaciones.evaluarExpresion(texto);
        this.resultado.setText(Operaciones.parsearDoubleToInt(result));
        Operaciones.escribirFichero(resultado.getText() + "\n", file);
    }

    //Abre Divisa
    @FXML
    private void convertidor_click(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/Divisas.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Esto abre un filechosoer para selecionar el css para cambiar el color
    @FXML
    private void click_cambio_color(ActionEvent event) {
        FileChooser fchooser = new FileChooser();
        fchooser.setTitle("Seleccionar Hoja de estilos.");
        fchooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSS", "*.css")
        );
        File css_styles = fchooser.showOpenDialog(new Stage());
        if (css_styles != null) {
            String cssFilePath = css_styles.toURI().toString();
            Scene scene = this.btn_aux.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(cssFilePath);
        }
    }

    @FXML
    private void key_pressed(KeyEvent event) {
        //Evento es en el anchorpane y es key pressed
        String val = event.getText();
        if(val.equals("+")||val.equals("-")||val.equals("*")||val.equals("/")||val.equals("^")){
            this.mostrarContenido(val);
            return;
        }
        if (Operaciones.isNumeric(val)) {
            this.mostrarContenido(val);
        }
    }

    //abro la vista historial
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
