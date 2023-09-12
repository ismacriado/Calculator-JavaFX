package controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HistorialController implements Initializable {

    private File file;
    @FXML
    private TableView<Configuracion> tableView;
    @FXML
    private TableColumn<Configuracion, String> col1;
    @FXML
    private TableColumn<Configuracion, String> col2;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Button volver;

    private ObservableList<Configuracion> listaConfiguraciones = FXCollections.observableArrayList();

    private HashMap<String, String> cuentas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        file = new File("C:\\Users\\wades\\Documents\\NetBeansProjects\\CalculadoraFX_ISMA\\src\\historial\\historial.txt");
        cuentas = new HashMap<>();
        col1.setCellValueFactory(new PropertyValueFactory<>("propiedad"));
        col2.setCellValueFactory(new PropertyValueFactory<>("valor"));
        readFile();
    }

    private void readFile() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(this.file));
            String r;
            while ((r = br.readLine()) != null) {
                String leido = r;
                if (leido.contains("=")) {
                    String[] arr = leido.split("\\=");
                    listaConfiguraciones.add(new Configuracion(arr[0], arr[1]));
                }else if(leido.contains("@")){
                    String[] arr = leido.split("\\@");
                    listaConfiguraciones.add(new Configuracion(arr[0],arr[1]));
                }
            }
            this.tableView.setItems(listaConfiguraciones);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    

    @FXML
    private void click_volver(ActionEvent event) {
        Stage ventanaActual = (Stage) this.volver.getScene().getWindow();
        ventanaActual.close();
    }

    @FXML
    private void borrar(ActionEvent event) {
        ObservableList<Configuracion> elementosSeleccionados = this.tableView.getSelectionModel().getSelectedItems();
        int ind=0;
        for (Configuracion elemento : elementosSeleccionados) {
            ind = this.listaConfiguraciones.indexOf(elemento)+1;
            this.listaConfiguraciones.remove(elemento);
            Operaciones.borrarLinea(ind,this.file);
        }
    }

    @FXML
    private void clear_table(ActionEvent event) {
        this.listaConfiguraciones.clear();
        Operaciones.clearFichero(file);
    }
}
