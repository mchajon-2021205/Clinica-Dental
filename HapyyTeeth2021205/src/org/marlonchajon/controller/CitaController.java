package org.marlonchajon.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.marlonchajon.bean.Cita;
import org.marlonchajon.bean.Doctor;
import org.marlonchajon.bean.Paciente;
import org.marlonchajon.db.Conexion;
import org.marlonchajon.system.Principal;


public class CitaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cita> listaCita;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<Doctor> listaDoctor;
    private DatePicker fCita;
    
    @FXML private TextField txtCodigoCita;
    @FXML private TextField txtHoraCita; 
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtTratamiento;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private GridPane grpFechas;
    @FXML private TableView tblCitas;
    @FXML private TableColumn colCodigoCita;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colHoraCita;
    @FXML private TableColumn colTratamiento;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colCodigoPaciente;
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
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFechas.add(fCita, 3, 0);
        fCita.getStylesheets().add("/org/marlonchajon/resource/DatePicker.css");
        cmbCodigoPaciente.setItems(getPaciente());
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(true);
    }
    
    public void cargarDatos(){
        tblCitas.setItems(getCita());
        colCodigoCita.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoCita"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Cita, Date>("fechaCita"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<Cita, Time>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<Cita, String>("tratamiento"));      
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Cita, String>("descripcion"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Cita, String>("codigoPaciente"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Cita, String>("numeroColegiado"));
    }    
    
    public void seleccionarElemento(){
        if(tblCitas.getSelectionModel().getSelectedItem() !=null){
            txtCodigoCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
            fCita.selectedDateProperty().set(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
            txtHoraCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
            txtTratamiento.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento());
            txtDescripcion.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDescripcion());
            cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
            cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
            
        }
    }
    
    public Paciente buscarPaciente(int codigoPaciente) {
        Paciente resultado = null;
       
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente(registro.getInt("codigoPaciente"),
                                    registro.getString("nombresPaciente"),
                                    registro.getString("apellidosPaciente"),
                                    registro.getString("sexo"),
                                    registro.getDate("fechaNacimiento"),
                                    registro.getString("direccionPaciente"),
                                    registro.getString("telefonoPersonal"),
                                    registro.getDate("fechaPrimeraCita"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
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
    
    
    
    
    
    
    
    
    public ObservableList<Cita> getCita(){
      ArrayList<Cita> lista = new ArrayList<Cita>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarCitas()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
               lista.add(new Cita(resultado.getInt("codigoCita"),
                                     resultado.getDate("fechaCita"),
                                     resultado.getTime("horaCita"),
                                     resultado.getString("tratamiento"),
                                     resultado.getString("descripcion"),
                                     resultado.getInt("codigoPaciente"),
                                     resultado.getInt("numeroColegiado")));    
            } 
       }catch(Exception e){
            e.printStackTrace();
        }
         
         return listaCita = FXCollections.observableArrayList(lista);
    }
    

    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarPacientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                                    resultado.getString("nombresPaciente"),
                                    resultado.getString("apellidosPaciente"),
                                    resultado.getString("sexo"),
                                    resultado.getDate("fechaNacimiento"),
                                    resultado.getString("direccionPaciente"),
                                    resultado.getString("telefonoPersonal"),
                                    resultado.getDate("fechaPrimeraCita")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPaciente = FXCollections.observableArrayList(lista);
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
                txtCodigoCita.setEditable(false);
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
        Cita registro = new Cita();
        registro.setFechaCita(fCita.getSelectedDate());
        DateFormat formatoHora = new SimpleDateFormat("HH:mm");
            try{
                registro.setHoraCita(new Time(formatoHora.parse(txtHoraCita.getText()).getTime()));
            }catch(Exception e){
                e.printStackTrace();
            }
            
        registro.setTratamiento(txtTratamiento.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarCita(?, ?, ?, ?, ?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(2, registro.getHoraCita());
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescripcion());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCita.add(registro);
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
                if(tblCitas.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro?", "Eliminar Cita", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                      if(respuesta == JOptionPane.YES_NO_OPTION){
                          try{
                              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarCita(?)}");
                              procedimiento.setInt(1, ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                              procedimiento.execute();
                              listaCita.remove(tblCitas.getSelectionModel().getSelectedIndex());
                              limpiarControles();
                          }catch(Exception e){
                              e.printStackTrace();
                          }
                      }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                  }
                
                 break;
        }
    }
     
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCitas.getSelectionModel().getSelectedItem() !=null){
                    activarControles();
                    cmbCodigoPaciente.setDisable(true);
                    cmbNumeroColegiado.setDisable(true);
                    txtCodigoCita.setEditable(false);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/marlonchajon/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/marlonchajon/image/cancelar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe selecionar un elemento");
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
            PreparedStatement procedimiento =Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarCita(?, ?, ?, ?, ?, ?, ?)}");
            Cita registro = (Cita)tblCitas.getSelectionModel().getSelectedItem();
            registro.setFechaCita(fCita.getSelectedDate());
            DateFormat formatoHora = new SimpleDateFormat("HH:mm");
            try{
                registro.setHoraCita(new Time(formatoHora.parse(txtHoraCita.getText()).getTime()));
            }catch(Exception e){
                e.printStackTrace();
            }
            registro.setTratamiento(txtTratamiento.getText());
            registro.setDescripcion(txtDescripcion.getText());
          
            procedimiento.setInt(1, registro.getCodigoCita());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(3, registro.getHoraCita());
            procedimiento.setString(4, registro.getTratamiento());
            procedimiento.setString(5, registro.getDescripcion());
            procedimiento.setInt(6, registro.getCodigoPaciente());
            procedimiento.setInt(7, registro.getNumeroColegiado());
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
        txtCodigoCita.setEditable(false);
        txtDescripcion.setEditable(false);
        txtTratamiento.setEditable(false);
        txtHoraCita.setEditable(false);
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setDisable(true);   
    }
    
    public void activarControles(){
        txtCodigoCita.setEditable(true);
        txtDescripcion.setEditable(true);
        txtTratamiento.setEditable(true);
        txtHoraCita.setEditable(true);
        cmbCodigoPaciente.setDisable(false);
        cmbNumeroColegiado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCita.clear();
        txtDescripcion.clear();
        txtTratamiento.clear();
        txtHoraCita.clear();
        tblCitas.getSelectionModel().clearSelection();
        fCita.setSelectedDate(null);
        cmbCodigoPaciente.getSelectionModel().clearSelection();
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

