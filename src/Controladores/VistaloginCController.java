/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.ModeloArchivoUsuarioAdministrador;
import Modelos.ModeloArchivoUsuarioCliente;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class VistaloginCController implements Initializable {

    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPasswork;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnViewRegister;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    public static String user;

    @FXML
    private void ActionLogin(ActionEvent event) throws IOException {
        this.user = this.txtUser.getText();
        String password = this.txtPasswork.getText();

        if (ModeloArchivoUsuarioCliente.login(user, password)) {
            try {
                // Cargar el archivo FXML de la vista de tienda
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vistatienda.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la nueva vista
                VistatiendaController controlador = loader.getController();

              
                // Crear una nueva escena para la vista de tienda
                Scene scene = new Scene(root);

                // Crear un nuevo stage para la vista de tienda
                Stage stage = new Stage();
                stage.setScene(scene);
                //stage.setResizable(false);
                stage.setMaximized(true);
                stage.show();

                // Indicar qué hacer al cerrar la ventana de login
                stage.setOnCloseRequest(e -> {
                    try {
                        controlador.closeWindows("/Vistas/VistaloginC.fxml", btnLogin);
                    } catch (IOException ex) {
                        Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                // Cerrar la ventana del menú actual
                Stage myStage = (Stage) this.btnLogin.getScene().getWindow();
                myStage.close();

            } catch (IOException ex) {
                Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect credentials", "Message", JOptionPane.INFORMATION_MESSAGE, null);
        }
    }

    @FXML
    private void ActionViewRegister(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VistaregistraC.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistaregistraCController controlador = loader.getController();

            // Crear una nueva escena para la vista de login
            Scene scene = new Scene(root);

            // Crear un nuevo stage para la vista de login
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Indicar qué hacer al cerrar la ventana de login
            stage.setOnCloseRequest(e -> {
                try {
                    controlador.closeWindows("/Vistas/VistaMenu.fxml", btnViewRegister);  // Llamar al método para reabrir el menú
                } catch (IOException ex) {
                    Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Cerrar la ventana del menú actual
            Stage myStage = (Stage) this.btnViewRegister.getScene().getWindow();
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
}
