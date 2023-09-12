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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    private static double guardar_memoria = 0;
    @FXML
    private AnchorPane fondo;
    @FXML
    private Label resultado;

    private File file;
    @FXML
    private Button btn_aux;
    @FXML
    private MenuBar menu_bar;
    private ColorPicker colorPicker;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File aux = new File("src/historial/historial.txt");
        file = new File(aux.getAbsolutePath());
        colorPicker = new ColorPicker();
    }

    //función para concatenar el texto del label resultado
    private void mostrarcontenido(String cont) {
        this.resultado.setText(this.resultado.getText() + cont);
    }

    //muestra los números por pantalla
    @FXML
    private void numeros(ActionEvent event) {
        String value = ((Button) (event.getSource())).getText();
        mostrarcontenido(value);
    }

    //resetea el label de los resultados
    @FXML
    private void resetear(ActionEvent event) {
        this.resultado.setText("");
    }

    //muestra la operación por pantalla
    @FXML
    private void operacion(ActionEvent event) {
        String oper = ((Button) event.getSource()).getText();
        mostrarcontenido(oper);
    }

    //ejecuta la operación
    @FXML
    private void igual(ActionEvent event) {
        String texto = this.resultado.getText();
        Operaciones.escribirFichero(texto + "=", file);
        double result = Operaciones.evaluarExpresion(texto);
        this.resultado.setText(Operaciones.parsearDoubleToInt(result));
        Operaciones.escribirFichero(resultado.getText() + "\n", file);
    }

    //suma los valores en memoria
    @FXML
    private void guardar_m_mas(ActionEvent event) {
        String texto = this.resultado.getText();
        double result = Operaciones.evaluarExpresion(texto);
        guardar_memoria += result;
        this.resultado.setText(guardar_memoria + "");
    }

    //resta los valores guardados en memoria
    @FXML
    private void guardar_m_menos(ActionEvent event) {
        String texto = this.resultado.getText();
        double result = Operaciones.evaluarExpresion(texto);
        guardar_memoria -= result;
        this.resultado.setText(guardar_memoria + "");
    }

    //abre la vista de la calculadora Científica
    @FXML
    private void click_item_bar(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/Cientifica.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //abre la vista de las divisas
    @FXML
    private void conversion_abrir(ActionEvent event) {
        try {
            AnchorPane panel = FXMLLoader.load(getClass().getResource("/vista/Divisas.fxml"));
            fondo.getChildren().setAll(panel);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    //abre la vista del Historial
    @FXML
    private void click_historial(ActionEvent event) {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/vista/Historial.fxml"));
            Pane root = (Pane) fxml.load();
            HistorialController controlador = fxml.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Historial");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //entradas por teclado, solo números y signos como +-*/^
    @FXML
    private void key_pressed(KeyEvent event) {
        //Evento es en el anchorpane y es key pressed
        String val = event.getText();
        if(val.equals("+")||val.equals("-")||val.equals("*")||val.equals("/")||val.equals("^")){
            this.mostrarcontenido(val);
            return;
        }
        if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.ENTER) {
            return;
        } else if (Operaciones.isNumeric(val)) {
            this.mostrarcontenido(val);
        }

    }

    //abre un color picker y modifica el estilo del fondo
    @FXML
    private void cambio_color(ActionEvent event) {
        
        colorPicker.setOnAction(e ->{
            Color color = colorPicker.getValue();
            String hex = String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
            this.fondo.setStyle("-fx-background-color: "+hex+";");
        });
        this.fondo.getChildren().add(colorPicker);
        AnchorPane.setRightAnchor(colorPicker, 10.0);
        
    }

    //elimina el color picker de la scene
    @FXML
    private void cerrar_color_picker(ActionEvent event) {
        this.fondo.getChildren().remove(colorPicker);
    }

    @FXML
    private void cambiar_signo(ActionEvent event) {
        if(this.resultado.getText().charAt(0)=='-'){
            this.resultado.setText(this.resultado.getText().substring(1));
        }else{
            this.resultado.setText("-"+this.resultado.getText());
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
