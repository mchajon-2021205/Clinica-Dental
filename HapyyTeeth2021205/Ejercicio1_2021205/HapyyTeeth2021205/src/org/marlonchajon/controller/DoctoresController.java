package org.marlonchajon.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.marlonchajon.bean.Doctor;
import org.marlonchajon.bean.Especialidad;
import org.marlonchajon.db.Conexion;
import org.marlonchajon.system.Principal;


public class DoctoresController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Doctor> listaDoctor;
    private ObservableList<Especialidad> listaEspecialidad;
    
    @FXML private TextField txtNumeroColegiado;
    @FXML private TextField txtNombresDoctor;
    @FXML private TextField txtApellidosDoctor;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TableView tblDoctores;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private TableColumn colNombresDoctor;
    @FXML private TableColumn colApellidosDoctor;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEspecialidades;
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

    public void cargarDatos(){
        tblDoctores.setItems(listaDoctor);
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("numeroColegiado"));
        
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista= new ArrayList<Doctor>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarDoctores()}");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               lista.add(new Doctor(resultado.getInt("numeroDoctor"),
                                    resultado.getString("nombresDoctor"),
                                    resultado.getString("apellidosDoctor"),
                                    resultado.getString("telefonoContacto"),
                                    resultado.getInt("codigoEspecialidad")));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);
    }
    
    
    
    
    
    
    
    public void desactivarControles(){
        txtNumeroColegiado.setEditable(false);
        txtNombresDoctor.setEditable(false);
        txtApellidosDoctor.setEditable(false);
        txtTelefonoContacto.setEditable(false);
    }
    
    public void activarControles(){
        txtNumeroColegiado.setEditable(true);
        txtNombresDoctor.setEditable(true);
        txtApellidosDoctor.setEditable(true);
        txtTelefonoContacto.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNumeroColegiado.clear();
        txtNombresDoctor.clear();
        txtApellidosDoctor.clear();
        txtTelefonoContacto.clear();
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
