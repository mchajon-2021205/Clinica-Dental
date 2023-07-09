package org.marlonchajon.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.marlonchajon.bean.Receta;
import org.marlonchajon.system.Principal;


public class RecetasController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Receta> listarReceta;
    
    @FXML private TextField txtCodigoReceta;
    @FXML private GridPane grpFechas;
    @FXML private TableView tblRecetas;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colFechaReceta;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
    }

    
    
    
    
    
    public void desactivarControles(){
        txtCodigoReceta.setEditable(false);
    }
     
    public void activarControles(){
        txtCodigoReceta.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoReceta.clear();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
