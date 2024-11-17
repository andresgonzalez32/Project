package Controladores;

import static Controladores.VistaloginCController.user;
import Modelos.Carrito;
import Modelos.ModeloArchivo;
import Modelos.ModeloProducto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class VistatiendaController implements Initializable {

    @FXML
    private GridPane gridPane;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Cargamos los productos del archivo
            List<ModeloProducto> productos = ModeloArchivo.fileReaderProducts();
            mostrarProductos(productos);

            // Crear un objeto ScheduledExecutorService para programar la tarea
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            Runnable tarea = new Runnable() {
                @Override
                public void run() {
                    // Crear un carrito y actualizar el contador
                    Carrito carrito = new Carrito();
                    int totalProductos = carrito.obtenerTotalProductosEnArchivo(user);
                    NameUser.setText(user);

                    // Usar Platform.runLater para asegurarse de que la actualización se haga en el hilo de la interfaz gráfica
                    Platform.runLater(() -> {
                        // Actualizar el contador en la interfaz (asumimos que CountCardStore es un componente de interfaz)
                        CountCardStore.setText("" + totalProductos);  // Asumiendo que CountCardStore es una referencia a tu componente
                    });
                }
            };

            // Programar la tarea para que se ejecute cada 1 segundo
            scheduler.scheduleAtFixedRate(tarea, 0, 1, TimeUnit.SECONDS);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Configura el espaciado entre las filas y columnas
        gridPane.setHgap(15);  // Espacio horizontal entre las tarjetas
        gridPane.setVgap(30);  // Espacio vertical entre las filas
    }

    

    private void mostrarProductos(List<ModeloProducto> productos) {
        Map<String, List<ModeloProducto>> productosPorCategoria = agruparPorCategoria(productos);

        int columna = 0;
        int fila = 1;

        // Iterar sobre las categorías
        for (String categoria : productosPorCategoria.keySet()) {
            List<ModeloProducto> productosDeCategoria = productosPorCategoria.get(categoria);

            // Mostrar la categoría como título en la fila
            Label categoriaLabel = new Label(categoria);
            categoriaLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 30px; -fx-text-fill: #333333;");
            gridPane.add(categoriaLabel, columna, fila);
            fila++; // Aumentamos una fila después del título

            // Ahora, agregar los productos de esa categoría a las filas
            for (ModeloProducto producto : productosDeCategoria) {
                VBox vbox = crearTarjetaProducto(producto);
                gridPane.add(vbox, columna, fila);
                columna++;

                // Cuando alcanzamos 3 productos en una fila, reiniciamos la columna y pasamos a la siguiente fila
                if (columna == 3) {
                    columna = 0;
                    fila++;
                }
            }

            // Si la fila no está llena, se ajustan los productos a la siguiente fila sin dejar espacios vacíos
            if (columna > 0 && columna < 3) {
                columna = 0;
                fila++;
            }

            // Espacio adicional entre categorías
            fila++;
        }
    }

    private Map<String, List<ModeloProducto>> agruparPorCategoria(List<ModeloProducto> productos) {
        Map<String, List<ModeloProducto>> productosPorCategoria = new HashMap<>();

        for (ModeloProducto producto : productos) {
            String categoria = producto.getCategory();

            if (!productosPorCategoria.containsKey(categoria)) {
                productosPorCategoria.put(categoria, new ArrayList<>());
            }

            productosPorCategoria.get(categoria).add(producto);
        }

        return productosPorCategoria;
    }

    private VBox crearTarjetaProducto(ModeloProducto producto) {

        Carrito carrito = new Carrito();
        VBox vbox = new VBox();
        vbox.setId("productBox" + producto.getId()); // ID único basado en el ID del producto
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));  // Relleno dentro de la tarjeta para que los contenidos no estén pegados a los bordes
        vbox.setStyle("-fx-border-color: #000000; -fx-background-color: #f5f5f5; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Crear los elementos de la tarjeta
        ImageView imagenProducto = new ImageView(new Image("file:" + producto.getImage()));
        imagenProducto.setFitHeight(123);
        imagenProducto.setFitWidth(199);

        Label nombreLabel = new Label(producto.getName());
        nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #333333;");

        Label precioLabel = new Label("$" + producto.getPrice());
        precioLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #333333;");

        Button agregarButton = new Button("Agregar");
        agregarButton.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5;");
        agregarButton.setMaxWidth(Double.MAX_VALUE); // Ocupa todo el ancho
        agregarButton.setOnMouseEntered(e -> agregarButton.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-background-color: #45a049; -fx-text-fill: white; -fx-border-radius: 5;"));
        agregarButton.setOnMouseExited(e -> agregarButton.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5;"));
        //agregarButton.setOnMouseClicked(e -> c.agregarProducto(producto));
        agregarButton.setOnMouseClicked(e -> {
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la cantidad:"));
            carrito.agregarProducto(producto, cantidad);
            carrito.guardarCarritoEnArchivo(NameUser.getText());
            JOptionPane.showMessageDialog(null, "Product successfully added!");

        });
        Button guardarButton = new Button("Guardar en la lista de deseos");
        guardarButton.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-border-radius: 5;");
        guardarButton.setMaxWidth(Double.MAX_VALUE); // Ocupa todo el ancho
        guardarButton.setOnMouseEntered(e -> guardarButton.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-background-color: #1e88e5; -fx-text-fill: white; -fx-border-radius: 5;"));
        guardarButton.setOnMouseExited(e -> guardarButton.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-border-radius: 5;"));
        guardarButton.setOnMouseClicked(e -> System.out.println("Has presionado el botón agregar a la lista de deseos"));

        // Agregar los elementos al VBox
        vbox.getChildren().addAll(imagenProducto, nombreLabel, precioLabel, agregarButton, guardarButton);

        // Crear margen entre las tarjetas (margen inferior + márgenes laterales)
        VBox.setMargin(vbox, new Insets(0, 15, 15, 15)); // margen superior, lateral derecho, inferior y lateral izquierdo

        return vbox;
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

    public void closeWindows2(String url, Label button) throws IOException {
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
                    controlador.closeWindows("/Vistas/Vistatienda.fxml", CardStore);  // Llamar al método para reabrir el menú
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
                    controlador.closeWindows("/Vistas/Vistatienda.fxml", CardStore);  // Llamar al método para reabrir el menú
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

}
