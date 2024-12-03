package Controladores;

import static Controladores.VistaloginCController.user;
import Modelos.Carrito;
import Modelos.ListaDeseo;
import Modelos.ModeloProducto;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class VistaListaDeseosController implements Initializable {

    @FXML
    private Label home;
    @FXML
    private SplitMenuButton SelecOptions;
    @FXML
    private MenuItem WishList;
    @FXML
    private MenuItem PurchaseHistory;
    @FXML
    private ImageView CardStore;
    @FXML
    private Label CountCardStore;
    @FXML
    private Label NameUser;
    @FXML
    private ImageView ExitStore;
    @FXML
    private TableView<ModeloProducto> tableCardProducts;
    @FXML
    private TableColumn<ModeloProducto, String> colNameProduct;
    @FXML
    private TableColumn<ModeloProducto, Double> colPrice;
    @FXML
    private TableColumn<ModeloProducto, String> colAction;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable tarea = new Runnable() {
            @Override
            public void run() {
                ListaDeseo carrito = new ListaDeseo();
                Carrito c = new Carrito();
                int totalProductos = c.obtenerTotalProductosEnArchivo(user);
                NameUser.setText(user);

                Platform.runLater(() -> {
                    CountCardStore.setText(String.valueOf(totalProductos));

                    List<ModeloProducto> productosDelCarrito = carrito.obtenerProductosDelArchivo(user);
                    ObservableList<ModeloProducto> observableProductos = FXCollections.observableArrayList(productosDelCarrito);
                    tableCardProducts.setItems(observableProductos);

                    colNameProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

                    colPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

                    colAction.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));
                    colAction.setCellFactory(param -> new TableCell<ModeloProducto, String>() {
                        private final Button deleteButton = new Button("Delete");
                        private final Button addButton = new Button("Add cart");

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(deleteButton);
                                deleteButton.setStyle("-fx-background-color: red; -fx-font-weight: bold; -fx-cursor: hand;");
                                addButton.setStyle("-fx-background-color: green; -fx-font-weight: bold; -fx-cursor: hand;");

                                deleteButton.setOnAction(event -> {
                                    ModeloProducto productoSeleccionado = getTableView().getItems().get(getIndex());
                                    String nombreProducto = productoSeleccionado.getName();

                                    // Eliminar el producto del archivo
                                    carrito.eliminarProductoDelArchivo(user, nombreProducto);

                                    // Actualizar la tabla, contador y total
                                    Platform.runLater(() -> {
                                        ObservableList<ModeloProducto> productosActualizados = FXCollections.observableArrayList(carrito.obtenerProductosDelArchivo(user));
                                        tableCardProducts.setItems(productosActualizados);

                                        int nuevosTotalProductos = productosActualizados.size();
                                        CountCardStore.setText("" + nuevosTotalProductos);

                                    });
                                });

                                // Acción del botón Agregar
                                addButton.setOnAction(event -> {
                                    ModeloProducto productoSeleccionado = getTableView().getItems().get(getIndex());

                                    // Solicitar la cantidad al usuario
                                    String cantidadStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad:");
                                    if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                                        try {
                                            int cantidad = Integer.parseInt(cantidadStr.trim());

                                            // Lógica para agregar el producto al carrito
                                            c.agregarProducto(productoSeleccionado, cantidad);  // Método que agrega el producto
                                            c.guardarCarritoEnArchivo(NameUser.getText());  // Guarda el carrito en el archivo

                                            // Confirmación al usuario
                                            JOptionPane.showMessageDialog(null, "Producto agregado exitosamente al carrito!");

                                            // Actualizar contador de productos en la interfaz
                                            Platform.runLater(() -> {
                                                int totalProductosActualizados = c.obtenerTotalProductosEnArchivo(user);
                                                CountCardStore.setText(String.valueOf(totalProductosActualizados));
                                            });
                                        } catch (NumberFormatException e) {
                                            JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                });

                                //Crear contenedor para los botones
                                HBox buttonBox = new HBox(5, deleteButton, addButton);
                                setGraphic(buttonBox); // Establecer el HBox como contenido de la celda
                            }
                        }
                    });

                });
            }
        };

        scheduler.scheduleAtFixedRate(tarea, 0, 1, TimeUnit.SECONDS);
    }

    @FXML
    private void ActionHome(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vistatienda.fxml"));
            Parent root = loader.load();

            VistatiendaController controlador = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

            stage.setOnCloseRequest(e -> {
                try {
                    controlador.closeWindows2("/Vistas/VistaListaDeseos.fxml", home);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Stage myStage = (Stage) this.home.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void ActioViewWishList(ActionEvent event) {
    }

    @FXML
    private void ActionPurchaseHistory(ActionEvent event) {
    }

    @FXML
    private void ActionViewCardStore(MouseEvent event) {
        try {
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vistacarrito.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistacarritoController controlador = loader.getController();

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
                    controlador.closeWindows("/Vistas/VistaListaDeseos.fxml", CardStore);  // Llamar al método para reabrir el menú
                } catch (IOException ex) {
                    Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Cerrar la ventana del menú actual
            Stage myStage = (Stage) this.CardStore.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ActionExitStore(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VistaloginC.fxml"));
            Parent root = loader.load();

            VistaloginCController controlador = loader.getController();
            Scene scene = new Scene(root);
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();

            Stage myStage = (Stage) this.ExitStore.getScene().getWindow();
            myStage.close();

            loginStage.setOnCloseRequest(e -> {
                Platform.exit();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeWindows(String url, ImageView button) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        Stage myStage = (Stage) button.getScene().getWindow();
        myStage.close();
    }
}
