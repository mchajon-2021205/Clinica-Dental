/*
NOmbre: Marlon David Chajon Suchite
Codigo Tecnico: IN5AV
Carné: 2021205
Fecha Creacion: 05/04/2022
Fecha Modificacion: 10/05/2022
*/

package org.marlonchajon.system;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.marlonchajon.controller.DoctoresController;
import org.marlonchajon.controller.EspecialidadesController;
import org.marlonchajon.controller.MedicamentosController;
import org.marlonchajon.controller.MenuPrincipalController;
import org.marlonchajon.controller.PacientesController;
import org.marlonchajon.controller.ProgramadorController;
import org.marlonchajon.controller.RecetasController;



public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA = "/org/marlonchajon/view/";
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Hapyy Teeth 2022");
        escenarioPrincipal.getIcons().add(new Image("/org/marlonchajon/image/HapyyTeeth.png"));
        menuPrincipal();
        escenarioPrincipal.show();
    }

        
        public void menuPrincipal(){
            
            try{
                 MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",421,418);
                 ventanaMenu.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
            
            
        }
        
        public void ventanaProgramador(){
            try{
                ProgramadorController vistaProgramador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml",530,400);
                vistaProgramador.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
                
        }
        
        public void ventanaPacientes(){
            try{
                PacientesController vistaPacientes = (PacientesController) cambiarEscena("PacientesView.fxml",1000,400);
                vistaPacientes.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
       }        
        
        public void ventanaMedicamentos(){
            try{
                MedicamentosController vistaMedicamentos = (MedicamentosController) cambiarEscena("MedicamentosView.fxml",600,400); 
                vistaMedicamentos.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void ventanaEspecialidades(){
            try{
                EspecialidadesController vistaEspecialidades = (EspecialidadesController) cambiarEscena("EspecialidadesView.fxml",600,400);
                vistaEspecialidades.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void ventanaDoctores(){
            try{
                DoctoresController vistaDoctores = (DoctoresController) cambiarEscena("DoctoresView.fxml",1000,400);
                vistaDoctores.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void ventanaRecetas(){
            try{
                RecetasController vistaRecetas = (RecetasController) cambiarEscena("RecetasView.fxml",700,400);
                vistaRecetas.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    
       
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        
        
        return resultado;
    }
    
}
