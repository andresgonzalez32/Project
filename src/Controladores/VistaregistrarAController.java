/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.ListaCircularDobleUsuario;
import Modelos.ModeloArchivoUsuarioAdministrador;
import Modelos.ModeloUsuarioAdministrador;
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
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class VistaregistrarAController implements Initializable {

    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPasswork;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnViewLogin;
    @FXML
    private TextField txtName;

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
        ListaCircularDobleUsuario listaUsuarios = new ListaCircularDobleUsuario();

        //Recepcionamos los campos del formulario
        String name = this.txtName.getText();
        String user = this.txtUser.getText();
        String passwork = this.txtPasswork.getText();

        ModeloUsuarioAdministrador usuario = new ModeloUsuarioAdministrador(name, user, passwork);

        listaUsuarios.add(usuario);

        // Mostrar usuarios en la lista
//        System.out.println("Usuarios en la lista circular:");
//        listaUsuarios.show();
        // Guardar la lista de usuarios en un archivo
        try {
            ModeloArchivoUsuarioAdministrador.saveUsersFile(listaUsuarios);
            clear();
        } catch (IOException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }

    }

    @FXML
    private void ActionViewLogin(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VistaloginA.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistaloginAController controlador = loader.getController();

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
                    Logger.getLogger(VistaMenuController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Cerrar la ventana del menú actual
            Stage myStage = (Stage) this.btnViewLogin.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeWindows() throws IOException {
        // Cargar el archivo FXML del menú
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VistaloginA.fxml"));
        Parent root = loader.load();

        // Crear una nueva escena para el menú
        Scene scene = new Scene(root);

        // Crear un nuevo stage para el menú
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // Cerrar la ventana de login actual
        Stage myStage = (Stage) this.btnViewLogin.getScene().getWindow();
        myStage.close();
    }

    public void clear() {
        this.txtName.setText("");
        this.txtUser.setText("");
        this.txtPasswork.setText("");
    }

}
