/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.ModeloArchivo;
import Modelos.ModeloProducto;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class VistalistarproductosController implements Initializable {

    @FXML
    private Button btnViewRegisterProduct;
    private Button btnViewUpdateProduct;
    @FXML
    private Button btnViewShowProduct;
    @FXML
    private ImageView btnExit;
    @FXML
    private TableView<ModeloProducto> tableProducts;
    @FXML
    private TableColumn<ModeloProducto, Integer> colID;
    @FXML
    private TableColumn<ModeloProducto, String> colName;
    @FXML
    private TableColumn<ModeloProducto, Double> colPrice;
    @FXML
    private TableColumn<ModeloProducto, Integer> colAmount;
    @FXML
    private TableColumn<ModeloProducto, String> colCategory;
    @FXML
    private TableColumn<ModeloProducto, String> colImage;
    @FXML
    private TableColumn<ModeloProducto, Void> colAction;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar las columnas
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));

        // Columna de Acción
        colAction.setCellFactory(column -> new TableCell<ModeloProducto, Void>() {
            private final Button editButton = new Button("Editar");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                    editButton.setStyle("-fx-background-color: yellow; -fx-font-weight: bold; -fx-cursor: hand;");
                    editButton.setOnAction(event -> {
                        ModeloProducto product = getTableView().getItems().get(getIndex());
                        openEditProductView(product);  // Llamar al método para abrir la vista de edición
                    });
                }
            }

            private void openEditProductView(ModeloProducto product) {
                try {
                    // Cargar el archivo FXML de la vista de edición
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vistaactualizarproductos.fxml"));
                    Parent root = loader.load();

                    // Obtener el controlador de la nueva vista
                    VistaactualizarproductosController controlador = loader.getController();
                    controlador.setProduct(product);  // Pasar el producto seleccionado al controlador de edición

                    // Crear una nueva escena para la vista de edición
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();

                    stage.setOnCloseRequest(e -> {
                        try {
                            controlador.closeWindows("/Vistas/Vistalistarproductos.fxml", btnViewShowProduct);  // Llamar al método para reabrir el menú
                        } catch (IOException ex) {
                            Logger.getLogger(VistalistarproductosController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                    // Cerrar la ventana actual si es necesario
                    Stage myStage = (Stage) tableProducts.getScene().getWindow();
                    myStage.close();

                } catch (IOException ex) {
                    Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        // Llenar la tabla con los datos
        try {
            List<ModeloProducto> products = ModeloArchivo.fileReaderProducts();
            tableProducts.getItems().setAll(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    controlador.closeWindows("/Vistas/Vistalistarproductos.fxml", this.btnViewRegisterProduct);  // Llamar al método para reabrir el menú
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
            stage.show();

            // Indicar qué hacer al cerrar la ventana de login
            stage.setOnCloseRequest(e -> {
                try {
                    controlador.closeWindows("/Vistas/Vistalistarproductos.fxml", this.btnViewUpdateProduct);  // Llamar al método para reabrir el menú
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
            List<ModeloProducto> products = ModeloArchivo.fileReaderProducts();
            tableProducts.getItems().setAll(products);
//            for (ModeloProducto product : products) {
//                System.out.println("Id: " + product.getId() + "\n"
//                        + "Name: " + product.getName() + "\n"
//                        + "Price: " + product.getPrice() + "\n"
//                        + "Amount: " + product.getAmount() + "\n"
//                        + "Category: " + product.getCategory() + "\n"
//                        + "Url Image: " + product.getImage());
//            }
        } catch (IOException e) {
            e.printStackTrace();
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
