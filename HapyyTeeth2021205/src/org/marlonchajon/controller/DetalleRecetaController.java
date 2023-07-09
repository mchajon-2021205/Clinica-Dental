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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.marlonchajon.bean.DetalleReceta;
import org.marlonchajon.bean.Medicamento;
import org.marlonchajon.bean.Receta;
import org.marlonchajon.db.Conexion;
import org.marlonchajon.system.Principal;

 
public class DetalleRecetaController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleReceta> listaDetalleReceta;
    private ObservableList<Receta> listaReceta;
    private ObservableList<Medicamento> listaMedicamento;
    
    @FXML private TextField txtCodigoDetalleReceta;
    @FXML private TextField txtDosis;
    @FXML private ComboBox cmbCodigoReceta;
    @FXML private ComboBox cmbCodigoMedicamento;
    @FXML private TableView tblDetalleRecetas;
    @FXML private TableColumn colCodigoDetalleReceta;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn colCodigoMedicamento;
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
        cmbCodigoReceta.setItems(getReceta());
        cmbCodigoMedicamento.setItems(getMedicamento());
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
    }

    public void cargarDatos(){
        tblDetalleRecetas.setItems(getDetalleReceta());
        colCodigoDetalleReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoDetalleReceta"));
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, String>("codigoReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta, String>("dosis"));
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta, String>("codigoMedicamento"));
    }
    
    public void selccionarElemento(){
        if(tblDetalleRecetas.getSelectionModel().getSelectedItem() != null){
            txtCodigoDetalleReceta.setText(String.valueOf(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            txtDosis.setText(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getDosis());
            cmbCodigoReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            cmbCodigoMedicamento.getSelectionModel().select(buscarMedicamento(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
    }
}
    public Receta buscarReceta(int codigoReceta) {
        Receta resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarReceta(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Receta(registro.getInt("codigoReceta"),
                                       registro.getDate("fechaReceta"),
                                       registro.getInt("numeroColegiado"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }   
    
    public Medicamento buscarMedicamento(int codigoMedicamento) {
        Medicamento resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarMedicamento(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medicamento(registro.getInt("codigoMedicamento"),
                                            registro.getString("nombreMedicamento"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    } 

    
    public ObservableList<DetalleReceta> getDetalleReceta(){
        ArrayList<DetalleReceta> lista = new ArrayList<DetalleReceta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarDetalleRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleReceta(resultado.getInt("codigoDetalleReceta"),
                                     resultado.getString("dosis"),
                                     resultado.getInt("codigoReceta"),
                                     resultado.getInt("codigoDetalleReceta")));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaDetalleReceta = FXCollections.observableArrayList(lista);
    }

    
     public ObservableList<Receta> getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Receta(resultado.getInt("codigoReceta"),
                                     resultado.getDate("fechaReceta"),
                                     resultado.getInt("numeroColegiado")));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaReceta = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Medicamento>  getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"),
                                          resultado.getString("nombreMedicamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicamento = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);;
                txtCodigoDetalleReceta.setEditable(false);
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
        DetalleReceta registro = new DetalleReceta();
        registro.setDosis(txtDosis.getText());
        registro.setCodigoReceta(((Receta)cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
        registro.setCodigoMedicamento(((Medicamento)cmbCodigoMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarDetalleReceta(?, ?, ?)}");
            procedimiento.setString(1, registro.getDosis());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.setInt(3, registro.getCodigoMedicamento());
            procedimiento.execute();
            listaDetalleReceta.add(registro);
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
                if(tblDetalleRecetas.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro?","Eliminar Detalle Receta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarDetalleReceta(?)}");
                             procedimiento.setInt(1,((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta());
                             procedimiento.execute();  
                             listaDetalleReceta.remove(tblDetalleRecetas.getSelectionModel().getSelectedIndex());
                             limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                           }
                        }
                }else{
                    JOptionPane.showMessageDialog(null, "Deve Seleccionar un elemento");
                }
//                  activarControles();  
                  break;
        }
    }
    
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblDetalleRecetas.getSelectionModel().getSelectedItem() !=null){
                   activarControles();
                   cmbCodigoReceta.setDisable(true);
                   cmbCodigoMedicamento.setDisable(true);
                   txtCodigoDetalleReceta.setEditable(false);
                   btnEditar.setText("Actualizar");
                   btnReporte.setText("Cancelar");
                   btnNuevo.setDisable(true);
                   btnEliminar.setDisable(true);
                   imgEditar.setImage(new Image("/org/marlonchajon/image/actualizar.png"));
                   imgReporte.setImage(new Image("/org/marlonchajon/image/cancelar.png"));
                   tipoDeOperacion = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                break;
            case ACTUALIZAR:
                actualizar();
                cargarDatos();
                limpiarControles();
                desactivarControles();;
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

    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarDetalleReceta(?, ?, ?, ?)}");
            DetalleReceta registro = (DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem();
           
            registro.setDosis(txtDosis.getText());            
            procedimiento.setInt(1, registro.getCodigoDetalleReceta());
            procedimiento.setString(2, registro.getDosis());
            procedimiento.setInt(3, registro.getCodigoReceta());
            procedimiento.setInt(4, registro.getCodigoMedicamento());
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
        txtCodigoDetalleReceta.setEditable(false);
        txtDosis.setEditable(false);
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoDetalleReceta.setEditable(true);
        txtDosis.setEditable(true);
        cmbCodigoReceta.setDisable(false);
        cmbCodigoMedicamento.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoDetalleReceta.clear();
        txtDosis.clear();
        tblDetalleRecetas.getSelectionModel().clearSelection();
        cmbCodigoReceta.getSelectionModel().clearSelection();
        cmbCodigoMedicamento.getSelectionModel().clearSelection();
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
