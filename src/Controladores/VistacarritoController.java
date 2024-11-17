package Controladores;

import static Controladores.VistaloginCController.user;
import Modelos.Carrito;
import Modelos.HistorialCompras;
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
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class VistacarritoController implements Initializable {

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
    private Label home;
    @FXML
    private TableView<ModeloProducto> tableCardProducts;
    @FXML
    private TableColumn<ModeloProducto, String> colNameProduct;
    @FXML
    private TableColumn<ModeloProducto, Integer> colAmount;
    @FXML
    private TableColumn<ModeloProducto, Double> colPrice;
    @FXML
    private TableColumn<ModeloProducto, Double> colTotal;
    @FXML
    private TableColumn<ModeloProducto, String> colAction;
    @FXML
    private Label CountTotal;
    @FXML
    private Button btnMakePayment;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable tarea = new Runnable() {
            @Override
            public void run() {
                Carrito carrito = new Carrito();
                int totalProductos = carrito.obtenerTotalProductosEnArchivo(user);
                NameUser.setText(user);

                Platform.runLater(() -> {
                    CountCardStore.setText("" + totalProductos);

                    List<ModeloProducto> productosDelCarrito = carrito.obtenerProductosDelArchivo(user);
                    ObservableList<ModeloProducto> observableProductos = FXCollections.observableArrayList(productosDelCarrito);
                    tableCardProducts.setItems(observableProductos);

                    colNameProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
                    colAmount.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCantidadCarrito()).asObject());
                    colPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
                    colTotal.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice() * cellData.getValue().getCantidadCarrito()).asObject());

                    colAction.setCellValueFactory(cellData -> new SimpleStringProperty("Delete"));
                    colAction.setCellFactory(param -> new TableCell<ModeloProducto, String>() {
                        private final Button deleteButton = new Button("Delete");

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(deleteButton);
                                deleteButton.setStyle("-fx-background-color: red; -fx-font-weight: bold; -fx-cursor: hand;");
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

                                        // Recalcular el total directamente desde la tabla
                                        double nuevoTotal = calcularTotalDesdeTabla();
                                        CountTotal.setText(String.format("$%.2f", nuevoTotal));
                                    });
                                });
                            }
                        }
                    });

                    // Calcular y mostrar el total inicial desde la tabla
                    double totalInicial = calcularTotalDesdeTabla();
                    CountTotal.setText(String.format("$%.2f", totalInicial));
                });
            }
        };

        scheduler.scheduleAtFixedRate(tarea, 0, 1, TimeUnit.SECONDS);
    }

    private double calcularTotalDesdeTabla() {
        double total = 0.0;
        for (ModeloProducto producto : tableCardProducts.getItems()) {
            total += producto.getPrice() * producto.getCantidadCarrito();
        }
        return total;
    }

    @FXML
    private void ActioViewWishList(ActionEvent event) {
    }

    @FXML
    private void ActionPurchaseHistory(ActionEvent event) {
         try {
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Vistahistorial.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistahistorialController controlador = loader.getController();

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
                    controlador.closeWindows("/Vistas/Vistacarrito.fxml", CardStore);  // Llamar al método para reabrir el menú
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
    private void ActionViewCardStore(MouseEvent event) {
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
                    controlador.closeWindows2("/Vistas/Vistacarrito.fxml", home);
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
    private void ActionMakePayment(ActionEvent event) {
        Carrito carrito = new Carrito();
        List<ModeloProducto> productosDelCarrito = carrito.obtenerProductosDelArchivo(user);

        if (!productosDelCarrito.isEmpty()) {
            HistorialCompras historial = new HistorialCompras();

            // Agregar la compra al historial y generar la factura
            historial.agregarCompra(productosDelCarrito, user);

            // Generar archivo acumulativo de historial
            String rutaArchivo = "historial_compras.txt";
            historial.generarArchivo(rutaArchivo);

            // Limpiar el carrito después del pago
            carrito.vaciarCarrito(user);

            // Actualizar la vista
            Platform.runLater(() -> {
                tableCardProducts.setItems(FXCollections.observableArrayList());
                CountCardStore.setText("0");
                CountTotal.setText("$0.00");
            });

            JOptionPane.showMessageDialog(null, "Purchase made successfully. History generated in: " + rutaArchivo);
            
        } else {
            JOptionPane.showMessageDialog(null, "The cart is empty. Payment cannot be made.");
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
