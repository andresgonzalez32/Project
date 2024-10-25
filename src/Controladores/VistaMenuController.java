/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class VistaMenuController implements Initializable {

    @FXML
    private Button btnViewLoginA;
    @FXML
    private Button btnViewLoginC;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void ActionViewLoginA(ActionEvent event) throws IOException {
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
                    controlador.closeWindows("/Vistas/VistaMenu.fxml", btnViewLoginA);  // Llamar al método para reabrir el menú
                } catch (IOException ex) {
                    Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Cerrar la ventana del menú actual
            Stage myStage = (Stage) this.btnViewLoginA.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ActionViewLoginC(ActionEvent event) {
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
                    controlador.closeWindows("/Vistas/VistaMenu.fxml", btnViewLoginC);  // Llamar al método para reabrir el menú
                } catch (IOException ex) {
                    Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Cerrar la ventana del menú actual
            Stage myStage = (Stage) this.btnViewLoginC.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
