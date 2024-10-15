/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andre
 */
public class ArrayListProducts {
    private List<ModeloProducto> products;
    
    public ArrayListProducts(){
        this.products = new ArrayList<>();
    }
    
    //Add products to List
    public void addProduct(ModeloProducto product){
        this.products.add(product);
    }
    
    //Get products
    public List<ModeloProducto> getProducts(){
        return this.products;
    }
}
