package Controladores;

import Modelos.ListaCircularDobleUsuario;
import Modelos.ListaCircularDoblementeUsuarioCliente;
import Modelos.ModeloArchivoUsuarioAdministrador;
import Modelos.ModeloArchivoUsuarioCliente;
import Modelos.ModeloUsuarioAdministrador;
import Modelos.ModeloUsuarioCliente;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class VistaregistraCController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPasswork;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnViewLogin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void ActionRegister(ActionEvent event) {
        // Crear una lista circular doble
        ListaCircularDoblementeUsuarioCliente listaUsuarios = new ListaCircularDoblementeUsuarioCliente();

        //Recepcionamos los campos del formulario
        String name = this.txtName.getText();
        String lastname = this.txtLastName.getText();
        String user = this.txtUser.getText();
        String passwork = this.txtPasswork.getText();

        ModeloUsuarioCliente usuario = new ModeloUsuarioCliente(name, lastname, user, passwork);

        listaUsuarios.add(usuario);

        // Mostrar usuarios en la lista
//        System.out.println("Usuarios en la lista circular:");
//        listaUsuarios.show();
        // Guardar la lista de usuarios en un archivo
        try {
            ModeloArchivoUsuarioCliente.saveUsersFile(listaUsuarios);
            clear();
        } catch (IOException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void ActionViewLogin(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VistaloginC.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistaloginCController controlador = loader.getController();

            // Crear una nueva escena para la vista de login
            Scene scene = new Scene(root);

            // Crear un nuevo stage para la vista de login
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Indicar qué hacer al cerrar la ventana de login
            stage.setOnCloseRequest(e -> {
                try {
                    controlador.closeWindows("/Vistas/VistaMenu.fxml", btnViewLogin);  // Llamar al método para reabrir el menú
                } catch (IOException ex) {
                    Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Cerrar la ventana del menú actual
            Stage myStage = (Stage) this.btnViewLogin.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeWindows(String url, Button button) throws IOException {
        // Cargar el archivo FXML del menú
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        Parent root = loader.load();

        // Crear una nueva escena para el menú
        Scene scene = new Scene(root);

        // Crear un nuevo stage para el menú
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // Cerrar la ventana de login actual
        Stage myStage = (Stage) button.getScene().getWindow();
        myStage.close();
    }
    
     public void clear() {
        this.txtName.setText("");
        this.txtUser.setText("");
        this.txtPasswork.setText("");
        this.txtLastName.setText("");
    }
}
