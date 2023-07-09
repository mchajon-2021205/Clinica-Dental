package org.marlonchajon.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.marlonchajon.bean.Doctor;
import org.marlonchajon.bean.Receta;
import org.marlonchajon.db.Conexion;
import org.marlonchajon.report.GenerarReporte;
import org.marlonchajon.system.Principal;


public class RecetasController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Receta> listaReceta;
    private ObservableList<Doctor> listaDoctor;
    private DatePicker fReceta;
    
    @FXML private TextField txtCodigoReceta;
    @FXML private ComboBox cmbNumeroColegiado;
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
        cargarDatos();
        fReceta = new DatePicker(Locale.ENGLISH);
        fReceta.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fReceta.getCalendarView().todayButtonTextProperty().set("Today");
        fReceta.getCalendarView().setShowWeeks(false);
        grpFechas.add(fReceta, 3, 0);
        fReceta.getStylesheets().add("/org/marlonchajon/resource/DatePicker.css");
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(true);
    }
    
    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory<Receta, Date>("fechaReceta"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Receta, String>("numeroColegiado"));
    }
    
    public void selccionarElemento(){
        if(tblRecetas.getSelectionModel().getSelectedItem() != null){
            txtCodigoReceta.setText(String.valueOf(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            fReceta.selectedDateProperty().set(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getFechaReceta());
            cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
    }
}
    public Doctor buscarDoctor(int numeroColegiado) {
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, numeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Doctor(registro.getInt("numeroColegiado"),
                        registro.getString("nombresDoctor"),
                        registro.getString("apellidosDoctor"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEspecialidad")
                );
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
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
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarDoctores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"),
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
    
        
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                txtCodigoReceta.setEditable(false);
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
        Receta registro = new Receta();
        registro.setFechaReceta(fReceta.getSelectedDate());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarReceta(?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.setInt(2, registro.getNumeroColegiado());
            procedimiento.execute();
            listaReceta.add(registro);
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
                if(tblRecetas.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro?","Eliminar Receta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if(respuesta == JOptionPane.YES_NO_OPTION){
                         try{
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarReceta(?)}");
                             procedimiento.setInt(1,((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                             procedimiento.execute();
                             listaReceta.remove(tblRecetas.getSelectionModel().getSelectedIndex());
                             limpiarControles();
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                     }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                     }
//                activarControles();
                break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblRecetas.getSelectionModel().getSelectedItem() !=null){
                   activarControles();
                   cmbNumeroColegiado.setDisable(true);
                   txtCodigoReceta.setEditable(false);
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
                desactivarControles();
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarReceta(?, ?, ?)}");
            Receta registro = (Receta)tblRecetas.getSelectionModel().getSelectedItem();
            registro.setFechaReceta(fReceta.getSelectedDate());
            registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
            
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.setInt(3, registro.getNumeroColegiado());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblRecetas.getSelectionModel().getSelectedItem() !=null){
                imprimirReporte();
                }else
                     JOptionPane.showMessageDialog(null, "Deve seleccionar un elemento");
                break;
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
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codReceta = ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta();
        parametros.put("codReceta", codReceta);
        parametros.put("rutaFondo", RecetasController.class.getResource("/org/marlonchajon/image/reporteR.png"));
        parametros.put("rutaLogo", RecetasController.class.getResource("/org/marlonchajon/image/HapyyTeeth.png"));
        parametros.put("rutaFirmaº", RecetasController.class.getResource("/org/marlonchajon/image/firma.png"));
        GenerarReporte.mostrarReporte("ReporteRecetas.jasper","Reporte De Recetas", parametros);
    }
    
    
    
    public void desactivarControles(){
        txtCodigoReceta.setEditable(false);
        cmbNumeroColegiado.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoReceta.setEditable(true);
        cmbNumeroColegiado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoReceta.clear();
        tblRecetas.getSelectionModel().clearSelection();
        fReceta.setSelectedDate(null);
        cmbNumeroColegiado.getSelectionModel().clearSelection();
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
