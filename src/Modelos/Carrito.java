package Modelos;

import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.*;

public class Carrito {

    private Map<ModeloProducto, Integer> productos;  // Mapa de productos y cantidades

    public Carrito() {
        productos = new HashMap<>();
    }

    // Agregar un producto al carrito con la cantidad deseada
    public void agregarProducto(ModeloProducto producto, int cantidad) {
        productos.put(producto, productos.getOrDefault(producto, 0) + cantidad);
    }

    // Obtener los productos del carrito
    public Map<ModeloProducto, Integer> getProductos() {
        return productos;
    }

    // Calcular el total de la compra
    public double calcularTotal() {
        double total = 0;
        for (Map.Entry<ModeloProducto, Integer> entry : productos.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();  // Multiplicar precio por cantidad
        }
        return total;
    }

    public void guardarCarritoEnArchivo(String username) {
        // Definir el archivo
        File archivo = new File("carrito_" + username + ".txt");
        Map<String, ModeloProducto> productosEnArchivo = new HashMap<>();
        Map<String, Integer> cantidadesEnArchivo = new HashMap<>();
        boolean archivoExistente = archivo.exists();

        try {
            // Leer el archivo existente si ya existe
            if (archivoExistente) {
                try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        if (linea.startsWith("Producto:")) {
                            // Extraer datos del producto
                            String[] partes = linea.split(", ");
                            String nombreProducto = partes[0].split(": ")[1];
                            double precioProducto = Double.parseDouble(partes[1].split(": ")[1].substring(1)); // Quitar el signo '$'
                            int cantidadProducto = Integer.parseInt(partes[2].split(": ")[1]);

                            // Crear un producto con los datos del archivo
                            ModeloProducto productoActual = new ModeloProducto(nombreProducto, "", "", 0, precioProducto);

                            // Guardar el producto y la cantidad leída
                            productosEnArchivo.put(nombreProducto, productoActual);
                            cantidadesEnArchivo.put(nombreProducto, cantidadProducto);
                        }
                    }
                }
            }

            // Actualizar los productos del archivo con los nuevos productos del carrito
            for (Map.Entry<ModeloProducto, Integer> entry : productos.entrySet()) {
                ModeloProducto producto = entry.getKey();
                int cantidadNueva = entry.getValue();

                if (productosEnArchivo.containsKey(producto.getName())) {
                    // Si el producto ya está en el archivo, sumamos las cantidades correctamente
                    int cantidadActual = cantidadesEnArchivo.get(producto.getName());
                    cantidadesEnArchivo.put(producto.getName(), cantidadActual + cantidadNueva);
                } else {
                    // Si el producto no está en el archivo, lo agregamos como nuevo
                    productosEnArchivo.put(producto.getName(), producto);
                    cantidadesEnArchivo.put(producto.getName(), cantidadNueva);
                }
            }

            // Escribir los datos actualizados en el archivo
            try (FileWriter writer = new FileWriter(archivo, false)) {
                writer.write("Usuario: " + username + "\n");
                double total = 0;
                for (String nombreProducto : productosEnArchivo.keySet()) {
                    ModeloProducto producto = productosEnArchivo.get(nombreProducto);
                    int cantidad = cantidadesEnArchivo.get(nombreProducto);
                    double precio = producto.getPrice();
                    writer.write("Producto: " + producto.getName() + ", Precio: $" + precio + ", Cantidad: " + cantidad + "\n");
                    total += precio * cantidad;
                }
                writer.write("Total: $" + total + "\n");
            }

            // Limpiar el mapa local de productos para evitar duplicados en la siguiente ejecución
            productos.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int obtenerTotalProductosEnArchivo(String username) {
        int totalProductos = 0;
        File archivo = new File("carrito_" + username + ".txt");

        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    // Solo procesar líneas que contienen información de productos
                    if (linea.startsWith("Producto:")) {
                        System.out.println("Línea procesada: " + linea); // Depuración
                        // Dividir la línea en partes para extraer la cantidad
                        // Ejemplo de línea: Producto: Product 129012, Precio: $128912.0, Cantidad: 22
                        String[] partes = linea.split(", ");
                        if (partes.length == 3) { // Asegurarse de que haya exactamente 3 partes
                            // Extraer la cantidad de la tercera parte
                            String cantidadStr = partes[2].split(": ")[1].trim();  // Separa por ": " y elimina espacios
                            int cantidad = Integer.parseInt(cantidadStr);  // Convertir la cantidad a entero
                            System.out.println("Cantidad extraída: " + cantidad); // Depuración
                            totalProductos += cantidad;  // Sumar la cantidad al total
                        } else {
                            System.err.println("Formato incorrecto: " + linea); // Depuración en caso de error
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("El archivo no existe: " + archivo.getAbsolutePath());
        }

        return totalProductos;
    }

    public List<ModeloProducto> obtenerProductosDelArchivo(String username) {
        List<ModeloProducto> productosList = new ArrayList<>();
        File archivo = new File("carrito_" + username + ".txt");

        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                Map<String, ProductoInfo> productosMap = new HashMap<>(); // Usamos una clase auxiliar para almacenar precio y cantidad

                while ((linea = reader.readLine()) != null) {
                    // Solo procesar líneas que contienen información de productos
                    if (linea.startsWith("Producto:")) {
                        String[] partes = linea.split(", ");
                        if (partes.length == 3) {
                            String nombreProducto = partes[0].split(": ")[1];
                            double precioProducto = Double.parseDouble(partes[1].split(": ")[1].substring(1)); // Quitar el signo '$'
                            int cantidadProducto = Integer.parseInt(partes[2].split(": ")[1]);

                            // Verificar si el producto ya está en el mapa
                            ProductoInfo infoProducto = productosMap.get(nombreProducto);
                            if (infoProducto != null) {
                                // Si ya está, actualizar la cantidad
                                infoProducto.cantidad += cantidadProducto;
                            } else {
                                // Si no está, agregar el producto al mapa
                                productosMap.put(nombreProducto, new ProductoInfo(precioProducto, cantidadProducto));
                            }
                        }
                    }
                }

                // Convertir el mapa en una lista de productos con las cantidades correctas
                for (Map.Entry<String, ProductoInfo> entry : productosMap.entrySet()) {
                    String nombreProducto = entry.getKey();
                    ProductoInfo info = entry.getValue();

                    // Crear el producto y agregarlo a la lista
                    ModeloProducto producto = new ModeloProducto(nombreProducto, "", "", 0, info.precio);
                    producto.setCantidadCarrito(info.cantidad); // Asignar la cantidad
                    productosList.add(producto);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return productosList;
    }

    public void eliminarProductoDelArchivo(String username, String nombreProducto) {
        File archivo = new File("carrito_" + username + ".txt");
        List<String> lineasActualizadas = new ArrayList<>();

        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                boolean productoEliminado = false;
                double totalActualizado = 0;

                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith("Producto:") && linea.contains("Producto: " + nombreProducto + ",")) {
                        productoEliminado = true;
                        String[] partes = linea.split(", ");
                        double precio = Double.parseDouble(partes[1].split(": ")[1].substring(1));
                        int cantidad = Integer.parseInt(partes[2].split(": ")[1]);
                        totalActualizado -= precio * cantidad;
                    } else if (linea.startsWith("Total:")) {
                        // No agregamos la línea del total, lo recalcularemos más adelante.
                    } else {
                        // Si no es el producto a eliminar, lo agregamos a la nueva lista.
                        lineasActualizadas.add(linea);

                        // Si la línea es un producto, recalculamos el total.
                        if (linea.startsWith("Producto:")) {
                            String[] partes = linea.split(", ");
                            double precio = Double.parseDouble(partes[1].split(": ")[1].substring(1));
                            int cantidad = Integer.parseInt(partes[2].split(": ")[1]);
                            totalActualizado += precio * cantidad;
                        }
                    }
                }

                // Agregar el nuevo total al final.
                lineasActualizadas.add("Total: $" + totalActualizado);

                // Escribir las líneas actualizadas al archivo.
                try (FileWriter writer = new FileWriter(archivo, false)) {
                    for (String lineaActualizada : lineasActualizadas) {
                        writer.write(lineaActualizada + "\n");
                    }
                }

                if (productoEliminado) {
                    System.out.println("Producto eliminado: " + nombreProducto);
                } else {
                    System.out.println("Producto no encontrado: " + nombreProducto);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("El archivo no existe: " + archivo.getAbsolutePath());
        }
    }

    public void vaciarCarrito(String usuario) {
        try {
            FileWriter writer = new FileWriter("carrito_" + usuario + ".txt");
            writer.write(""); // Sobrescribir con un archivo vacío
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// Clase auxiliar para almacenar el precio y la cantidad del producto
    private static class ProductoInfo {

        double precio;
        int cantidad;

        ProductoInfo(double precio, int cantidad) {
            this.precio = precio;
            this.cantidad = cantidad;
        }
    }

}
