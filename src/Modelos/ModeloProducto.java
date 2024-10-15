/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author andre
 */
public class ModeloProducto {

    private String name;
    private String image;
    private String category;
    private int amount;
    private int cantidadCarrito;
    private int id;
    private double price;
    private static Set<Integer> idsGenerados = new HashSet<>();
    private static Random random = new Random();

    public ModeloProducto(String name, String image, String category, int amount, double price) {
        this.name = name;
        this.image = image;
        this.category = category;
        this.amount = amount;
        this.id = generarId();
        this.price = price;
    }

    // Sobrecarga del constructor para aceptar un ID directamente
    public ModeloProducto(String name, String image, String category, int amount, double price, int id) {
        this.name = name;
        this.image = image;
        this.category = category;
        this.amount = amount;
        this.price = price;
        this.id = id; // Utilizar el ID proporcionado
    }

    // Constructor con solo nombre y precio (para creación desde archivo)
    public ModeloProducto(String name, double price) {
        this.name = name;
        this.price = price;
        this.amount = 0; // Por defecto, la cantidad es 0
        this.cantidadCarrito = 0; // Inicialmente, no hay cantidad seleccionada en el carrito
        this.id = generarId();
        this.image = ""; // Se puede dejar vacío o asignar valor por defecto
        this.category = ""; // Se puede dejar vacío o asignar valor por defecto
    }

    // Constructor adicional para facilitar la implementación en otras clases
    public ModeloProducto(String name) {
        this.name = name;
        this.amount = 0; // Por defecto, la cantidad es 0
        this.cantidadCarrito = 0; // Inicialmente, no hay cantidad seleccionada en el carrito
        this.price = 0.0; // Por defecto, el precio es 0
        this.id = generarId();
        this.image = ""; // Se puede dejar vacío o asignar valor por defecto
        this.category = ""; // Se puede dejar vacío o asignar valor por defecto
    }

    // Constructor con solo nombre y precio (para poder crear un producto a partir del archivo)
//    public ModeloProducto(String name, double price) {
//        this.name = name;
//        this.price = price;
//    }
    public int getCantidadCarrito() {
        return cantidadCarrito; // Devuelve la cantidad seleccionada por el usuario en el carrito
    }

    // Método para ajustar la cantidad del producto en el carrito
    public void setCantidadCarrito(int cantidadCarrito) {
        this.cantidadCarrito = cantidadCarrito; // Asignar la cantidad seleccionada para el carrito
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        // Formato simple para guardar en archivo
        return name + "," + image + "," + category + "," + amount + "," + price + "," + id;
    }

    public static ModeloProducto fromString(String linea) {
        // Separar por comas y convertir valores numéricos
        String[] datos = linea.split(",");
        return new ModeloProducto(
                datos[0].trim(),
                datos[1].trim(),
                datos[2].trim(),
                Integer.parseInt(datos[3].trim()),
                Double.parseDouble(datos[4].trim()),
                Integer.parseInt(datos[5].trim())
        );
    }

    private int generarId() {
        int nuevoId;
        do {
            nuevoId = random.nextInt(100000); // Genera un número aleatorio entre 0 y 99999
        } while (idsGenerados.contains(nuevoId)); // Verifica si el ID ya ha sido generado
        idsGenerados.add(nuevoId); // Agrega el ID al conjunto
        return nuevoId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ModeloProducto that = (ModeloProducto) obj;
        return name.equals(that.name); // Comparar solo por nombre, o agrega más campos si es necesario
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // Usar el mismo campo que en equals
    }

}
