package Controladores;

import Modelos.ArrayListProducts;
import Modelos.ModeloArchivo;
import Modelos.ModeloProducto;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class VistaregistrarproductosController implements Initializable {

    @FXML
    private Button btnViewRegisterProduct;
    @FXML
    private Button btnViewUpdateProduct;
    @FXML
    private Button btnViewShowProduct;
    @FXML
    private ImageView btnExit;
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
    private Button btnRegister;
    @FXML
    private Label txtNameImage;

    String urlImage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesario
    }

    @FXML
    private void ActionViewRegisterProduct(ActionEvent event) {

    }

    @FXML
    private void ActionViewUpdateProduct(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vistaactualizarproductos.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistaactualizarproductosController controlador = loader.getController();

            // Crear una nueva escena para la vista de login
            Scene scene = new Scene(root);

            // Crear un nuevo stage para la vista de login
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

            // Indicar qué hacer al cerrar la ventana de login
            stage.setOnCloseRequest(e -> {
                try {
                    controlador.closeWindows("/Vistas/Vistaregistrarproductos.fxml", this.btnViewUpdateProduct);  // Llamar al método para reabrir el menú
                } catch (IOException ex) {
                    Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Cerrar la ventana del menú actual
            Stage myStage = (Stage) this.btnViewUpdateProduct.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            stage.setMaximized(true);
            stage.show();

            // Indicar qué hacer al cerrar la ventana de login
            stage.setOnCloseRequest(e -> {
                try {
                    controlador.closeWindows("/Vistas/Vistaregistrarproductos.fxml", this.btnViewShowProduct);  // Llamar al método para reabrir el menú
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
            System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }

    @FXML
    private void ActionRegister(ActionEvent event) {
        ArrayListProducts lisProduct = new ArrayListProducts();

        String name = this.txtName.getText();
        String image = this.urlImage;
        String category = this.txtCategory.getText();
        int amount = Integer.parseInt(this.txtAmount.getText());
        double price = Double.parseDouble(this.txtPrice.getText());

        lisProduct.addProduct(new ModeloProducto(name, image, category, amount, price));

        try {
            ModeloArchivo.saveProductsFile(lisProduct.getProducts());
            JOptionPane.showMessageDialog(null, "Product successfully registered", "Message", JOptionPane.INFORMATION_MESSAGE, null);
            this.clearCamp();
        } catch (Exception e) {
        }
    }

    public void clearCamp() {
        this.txtName.setText("");
        this.txtNameImage.setText("");
        this.txtCategory.setText("");
        this.txtAmount.setText("");
        this.txtPrice.setText("");
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
