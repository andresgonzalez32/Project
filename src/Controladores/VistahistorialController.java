/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import static Controladores.VistaloginCController.user;
import Modelos.Carrito;
import Modelos.HistorialCompras;
import Modelos.ModeloArchivo;
import Modelos.ModeloProducto;
import Modelos.ProductoHistorial;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class VistahistorialController implements Initializable {

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
    private TableView<ProductoHistorial> tableCardProducts;
    @FXML
    private TableColumn<ProductoHistorial, String> colNameProduct;
    @FXML
    private TableColumn<ProductoHistorial, Double> colTotal;
    @FXML
    private TableColumn<ProductoHistorial, String> colDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Task<Void> tarea = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Obtener el total de productos
                Carrito carrito = new Carrito();
                int totalProductos = carrito.obtenerTotalProductosEnArchivo(user);
                NameUser.setText(user);

                // Actualizar la interfaz gráfica en el hilo de JavaFX
                Platform.runLater(() -> {
                    CountCardStore.setText(String.valueOf(totalProductos));
                    actualizarTabla();
                });

                return null;
            }
        };

        // Programar la tarea para que se ejecute cada 1 segundo
        scheduler.scheduleAtFixedRate(tarea, 0, 1, TimeUnit.SECONDS);
    }

    public void actualizarTabla() {
        // Crear un conjunto para asegurarse de que no haya duplicados
        Set<ProductoHistorial> historialSinDuplicados = new HashSet<>();

        // Cargar el historial de compras desde el archivo
        HistorialCompras h = new HistorialCompras();
        List<ProductoHistorial> historialDeCompras = h.cargarHistorial("historial_compras.txt", user);

        // Verificar si se ha cargado el historial correctamente
        if (historialDeCompras != null && !historialDeCompras.isEmpty()) {
            // Añadir cada producto al conjunto, que eliminará duplicados automáticamente
            historialSinDuplicados.addAll(historialDeCompras);

            // Convertir el conjunto a una lista observable para la tabla
            ObservableList<ProductoHistorial> historial = FXCollections.observableArrayList(historialSinDuplicados);

            // Asignar los productos al TableView
            tableCardProducts.setItems(historial);

            // Configurar las columnas de la tabla
            colNameProduct.setCellValueFactory(new PropertyValueFactory<>("nameProducts"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

            // Forzar la actualización de la tabla
            tableCardProducts.refresh();
        } else {
            // En caso de que no haya datos en el historial, mostrar un mensaje (opcional)
            System.out.println("No se encontraron productos en el historial.");
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
            stage.show();

            // Indicar qué hacer al cerrar la ventana de login
            stage.setOnCloseRequest(e -> {
                try {
                    controlador.closeWindows("/Vistas/Vistahistorial.fxml", CardStore);  // Llamar al método para reabrir el menú
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
            // Cargar el archivo FXML de la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VistaloginC.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            VistaloginCController controlador = loader.getController();

            // Crear una nueva escena para la vista de login
            Scene scene = new Scene(root);

            // Crear un nuevo stage para la vista de login
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();

            // Cerrar la ventana actual
            Stage myStage = (Stage) this.ExitStore.getScene().getWindow();
            myStage.close();

            // Terminar todos los procesos de la aplicación
            loginStage.setOnCloseRequest(e -> {
                Platform.exit(); // Cierra todos los procesos de la aplicación
            });

        } catch (IOException ex) {
            Logger.getLogger(VistaMenuController.class.getName()).log(Level.SEVERE, null, ex);
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
