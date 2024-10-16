package Modelos;

public class ProductoHistorial {

    private String nameProducts;
    private double total;
    private String date;

    // Constructor
    public ProductoHistorial(String nameProducts, double total, String date) {
        this.nameProducts = nameProducts;
        this.total = total;
        this.date = date;
    }

    // Getters y setters
    public String getNameProducts() {
        return nameProducts;
    }

    public void setNameProducts(String nameProduct) {
        this.nameProducts = nameProduct;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    // Método para obtener los productos como una cadena separada por comas
    public String getFormattedProductNames() {
        return String.join(", ", nameProducts);
    }

    public static ProductoHistorial fromString(String linea) {
        String[] datos = linea.split(",");
        return new ProductoHistorial(
                datos[0].trim(), // nombre del producto
                Double.parseDouble(datos[1].trim()), // total
                datos[2].trim() // fecha
        );
    }

}
