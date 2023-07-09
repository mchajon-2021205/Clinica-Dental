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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.marlonchajon.bean.Especialidad;
import org.marlonchajon.db.Conexion;
import org.marlonchajon.system.Principal;


public class EspecialidadesController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaEspecialidad;
    
    @FXML private TextField txtCodigoEspecialidad;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblEspecialidades;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colDescripcion;
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
        cargarDatos();
    }

    public void cargarDatos(){
        tblEspecialidades.setItems(getEspecialidad());
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Especialidad, String>("descripcion"));
    }
    
    public void seleccionarElemento(){
        if(tblEspecialidades.getSelectionModel().getSelectedItem() !=null){
            txtCodigoEspecialidad.setText(String.valueOf(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
            txtDescripcion.setText(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getDescripcion());
            
        }
    }
    
    public ObservableList<Especialidad> getEspecialidad(){
         ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarEspecialidades()}");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                            resultado.getString("descripcion")));
         }
         }catch(Exception e){
             e.printStackTrace();
         }
     
         return listaEspecialidad = FXCollections.observableArrayList(lista);
    }
    
    
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                txtCodigoEspecialidad.setEditable(false);
                imgNuevo.setImage(new Image("/org/marlonchajon/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/marlonchajon/image/cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/marlonchajon/image/agregar.png"));
                imgEliminar.setImage(new Image("/org/marlonchajon/image/eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Especialidad registro = new Especialidad();
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarEspecialidad(?)}");
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.execute();
        }catch(Exception e){
           e.printStackTrace();
            }
    }    
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/marlonchajon/image/agregar.png"));
                imgEliminar.setImage(new Image("/org/marlonchajon/image/eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblEspecialidades.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro?", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                      if(respuesta == JOptionPane.YES_NO_OPTION){
                          try{
                              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarEspecialidad(?)}");
                              procedimiento.setInt(1, ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                              procedimiento.execute();
                              listaEspecialidad.remove(tblEspecialidades.getSelectionModel().getSelectedIndex());
                          }catch(Exception e){
                              e.printStackTrace();
                             }
                        }
                }else {
                    JOptionPane.showMessageDialog(null,"Deve seleccionar un elemento");
                    }
//                 activarControles();
                break;
            }  
        }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblEspecialidades.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/marlonchajon/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/marlonchajon/image/cancelar.png"));
                    activarControles();
                    txtCodigoEspecialidad.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Deve seleccionar un elemento");
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/marlonchajon/image/editar.png"));
                imgReporte.setImage(new Image("/org/marlonchajon/image/reporte.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;    
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarEspecialidad(?, ?)}");
            Especialidad registro = (Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/marlonchajon/image/editar.png"));
                imgReporte.setImage(new Image("/org/marlonchajon/image/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            
        }
    }
    
    public void desactivarControles(){
        txtCodigoEspecialidad.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEspecialidad.setEditable(true);
        txtDescripcion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEspecialidad.clear();
        txtDescripcion.clear();
        
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
