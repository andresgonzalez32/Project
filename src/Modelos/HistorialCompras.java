package Modelos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.JOptionPane;

public class HistorialCompras {

    private Queue<String> historial;

    public HistorialCompras() {
        this.historial = new LinkedList<>();
    }

    public void agregarCompra(List<ModeloProducto> productos, String usuario) {
        StringBuilder registro = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = sdf.format(new Date());

        double totalCompra = 0.0;

        registro.append("Usuario: ").append(usuario).append("\n");
        registro.append("Fecha: ").append(fecha).append("\n");
        registro.append("Productos:\n");

        for (ModeloProducto producto : productos) {
            double totalProducto = producto.getPrice() * producto.getCantidadCarrito();
            totalCompra += totalProducto;

            registro.append("- ").append(producto.getName())
                    .append(", Cantidad: ").append(producto.getCantidadCarrito())
                    .append(", Precio Unitario: $").append(producto.getPrice())
                    .append(", Total: $").append(totalProducto)
                    .append("\n");
        }

        registro.append("TOTAL DE LA COMPRA: $").append(String.format("%.2f", totalCompra)).append("\n");
        registro.append("----------------------------------------------------\n");
        historial.add(registro.toString());

        // Generar factura
        generarFactura(usuario, productos, totalCompra, fecha);
        JOptionPane.showMessageDialog(null, "Successfully generated invoice");

        // Guardar el historial actualizado en el archivo
        generarArchivo("historial_compras.txt");
    }

    private void generarFactura(String usuario, List<ModeloProducto> productos, double totalCompra, String fecha) {
        SimpleDateFormat sdfArchivo = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String nombreArchivo = "factura_" + usuario + "_" + sdfArchivo.format(new Date()) + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write("***** FACTURA *****\n");
            writer.write("Usuario: " + usuario + "\n");
            writer.write("Fecha: " + fecha + "\n");
            writer.write("----------------------------\n");
            writer.write("Productos:\n");

            for (ModeloProducto producto : productos) {
                writer.write("- " + producto.getName()
                        + ", Cantidad: " + producto.getCantidadCarrito()
                        + ", Precio Unitario: $" + producto.getPrice()
                        + ", Total: $" + (producto.getPrice() * producto.getCantidadCarrito()) + "\n");
            }

            writer.write("----------------------------\n");
            writer.write("TOTAL: $" + String.format("%.2f", totalCompra) + "\n");
            writer.write("***** GRACIAS POR SU COMPRA *****\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generarArchivo(String rutaArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            while (!historial.isEmpty()) {
                writer.write(historial.poll());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<ProductoHistorial> cargarHistorial(String rutaArchivo, String usuarioFiltrado) {
        List<ProductoHistorial> productos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean agregarProductos = false;
            String fechaActual = null;
            double totalCompra = 0.0;
            StringBuilder productosEnCompra = new StringBuilder(); // Para concatenar productos

            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();

                // Verificar si es el usuario filtrado
                if (linea.startsWith("Usuario:")) {
                    String usuario = linea.split(":")[1].trim();
                    agregarProductos = usuario.equals(usuarioFiltrado);
                }

                // Capturar la fecha de la compra
                if (agregarProductos && linea.startsWith("Fecha:")) {
                    // Agregar la compra anterior antes de procesar la nueva fecha
                    if (productosEnCompra.length() > 0) {
                        productos.add(new ProductoHistorial(productosEnCompra.toString(), totalCompra, fechaActual));
                        productosEnCompra.setLength(0); // Reiniciar productos
                        totalCompra = 0.0; // Reiniciar total
                    }
                    fechaActual = linea.split(": ", 2)[1].trim();
                }

                // Leer los productos si es el usuario correcto
                if (agregarProductos && linea.startsWith("- ")) {
                    String[] partes = linea.substring(2).split(", ");
                    String nombre = partes[0]; // Nombre del producto
                    double precioTotal = Double.parseDouble(partes[3].split(": \\$")[1]); // Total del producto

                    // Concatenar el producto en la misma celda
                    if (productosEnCompra.length() > 0) {
                        productosEnCompra.append(", ");
                    }
                    productosEnCompra.append(nombre);

                    totalCompra += precioTotal; // Acumular el total
                }

                // Detectar el fin de los datos de un usuario
                if (linea.startsWith("TOTAL DE LA COMPRA:")) {
                    if (productosEnCompra.length() > 0) {
                        productos.add(new ProductoHistorial(productosEnCompra.toString(), totalCompra, fechaActual));
                    }
                    productosEnCompra.setLength(0); // Reiniciar productos
                    totalCompra = 0.0; // Reiniciar total
                    agregarProductos = false; // Detener hasta que aparezca el próximo usuario
                }
            }

            // Agregar la última compra si no se agregó
            if (productosEnCompra.length() > 0) {
                productos.add(new ProductoHistorial(productosEnCompra.toString(), totalCompra, fechaActual));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return productos;
    }

}
