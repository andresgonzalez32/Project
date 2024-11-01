/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;
import javafx.application.Platform;
import Modelos.ModeloArchivo;
import Modelos.ModeloProducto;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class VistaactualizarproductosController implements Initializable {

    @FXML
    private Button btnViewRegisterProduct;
    @FXML
    private Button btnViewUpdateProduct;
    @FXML
    private Button btnViewShowProduct;
    @FXML
    private ImageView btnExit;
    @FXML
    private Label txtNameImage;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtCategory;
    @FXML
    private Button btnSelectImage;
    @FXML
    private Button btnUpdate;

    private ModeloProducto product;
    String urlImage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void ActionViewRegisterProduct(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vistaregistrarproductos.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistaregistrarproductosController controlador = loader.getController();

            // Crear una nueva escena para la vista de login
            Scene scene = new Scene(root);

            // Crear un nuevo stage para la vista de login
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Indicar qué hacer al cerrar la ventana de login
            stage.setOnCloseRequest(e -> {
                try {
                    controlador.closeWindows("/Vistas/Vistaactualizarproductos.fxml", this.btnViewRegisterProduct);  // Llamar al método para reabrir el menú
                } catch (IOException ex) {
                    Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Cerrar la ventana del menú actual
            Stage myStage = (Stage) this.btnViewRegisterProduct.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ActionViewUpdateProduct(ActionEvent event) {
    }

    @FXML
    private void ActionViewShowProduct(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vistalistarproductos.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistalistarproductosController controlador = loader.getController();

            // Crear una nueva escena para la vista de login
            Scene scene = new Scene(root);

            // Crear un nuevo stage para la vista de login
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(e -> {
                try {
                    controlador.closeWindows("/Vistas/Vistaactualizarproductos.fxml", this.btnViewShowProduct);  // Llamar al método para reabrir el menú
                } catch (IOException ex) {
                    Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Cerrar la ventana del menú actual
            Stage myStage = (Stage) this.btnViewShowProduct.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ActionExit(MouseEvent event) {
        try {
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VistaloginA.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistaloginAController controlador = loader.getController();

            // Crear una nueva escena para la vista de login
            Scene scene = new Scene(root);

            // Crear un nuevo stage para la vista de login
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();

            // Cerrar la ventana actual
            Stage myStage = (Stage) this.btnExit.getScene().getWindow();
            myStage.close();

            // Terminar todos los procesos de la aplicación
            loginStage.setOnCloseRequest(e -> {
                Platform.exit(); // Cierra todos los procesos de la aplicación
            });

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ActionSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");

        // Filtros de archivo opcionales
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        // Obtener el Stage desde el botón u otro nodo de la escena
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Mostrar el diálogo para seleccionar un archivo
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            this.urlImage = selectedFile.getAbsolutePath();
            this.txtNameImage.setText("" + selectedFile.getName());
            //System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());
        } else {
            //System.out.println("No se seleccionó ningún archivo.");
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

    @FXML
    private void ActionUpdateProduct(ActionEvent event) {
        // Obtener datos de los campos de texto
        String name = txtName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int amount = Integer.parseInt(txtAmount.getText());
        String category = txtCategory.getText();
        String image = urlImage;

        // Actualizar el objeto ModeloProducto
        product.setName(name);
        product.setPrice(price);
        product.setAmount(amount);
        product.setCategory(category);
        product.setImage(image);

        // Guardar el producto actualizado en el archivo
        try {
            ModeloArchivo.actualizarProductoEnArchivo(product);
            JOptionPane.showMessageDialog(null, "Product successfully update", "Message", JOptionPane.INFORMATION_MESSAGE, null);
            this.clearCamp();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProduct(ModeloProducto product) {
        this.product = product;
        // Asignar valores a los campos de texto
        txtName.setText(product.getName());
        txtPrice.setText(String.valueOf(product.getPrice()));
        txtAmount.setText(String.valueOf(product.getAmount()));
        txtCategory.setText(product.getCategory());
        txtNameImage.setText(product.getImage());
    }

    public void clearCamp() {
        this.txtName.setText("");
        this.txtNameImage.setText("");
        this.txtCategory.setText("");
        this.txtAmount.setText("");
        this.txtPrice.setText("");
    }
}
