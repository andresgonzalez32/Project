/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andre
 */
public class ModeloArchivo {

    public static void saveProductsFile(List<ModeloProducto> products) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("productd.txt", true))) {
            for (ModeloProducto product : products) {
                writer.write(product.toString());
                writer.newLine();
            }
        }
    }

    public static List<ModeloProducto> fileReaderProducts() throws IOException {
        List<ModeloProducto> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("productd.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                products.add(ModeloProducto.fromString(linea));
            }
        }
        return products;
    }

    public static void actualizarProductoEnArchivo(ModeloProducto productoActualizado) throws IOException {
        // Leer todos los productos del archivo
        List<ModeloProducto> productos = fileReaderProducts();

        // Buscar el producto por su ID
        for (int i = 0; i < productos.size(); i++) {
            ModeloProducto producto = productos.get(i);
            if (producto.getId() == productoActualizado.getId()) {
                // Reemplazar el producto encontrado por el actualizado
                productos.set(i, productoActualizado);
                break;
            }
        }

        // Guardar todos los productos de vuelta en el archivo (sobrescribiendo)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("productd.txt"))) {
            for (ModeloProducto producto : productos) {
                writer.write(producto.toString());
                writer.newLine();
            }
        }
    }

}
