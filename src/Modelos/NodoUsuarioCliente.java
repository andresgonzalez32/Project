/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

/**
 *
 * @author ASUS
 */
public class NodoUsuarioCliente {
    
    // Private fields
    private ModeloUsuarioCliente user;
    private NodoUsuarioCliente next;
    private NodoUsuarioCliente previous;

    // Constructor
    public NodoUsuarioCliente(ModeloUsuarioCliente user) {
        this.user = user;
        this.next = null;
        this.previous = null;
    }


    // Getter and setter for the user
    public ModeloUsuarioCliente getUser() {
        return user;
    }

    public void setUser(ModeloUsuarioCliente user) {
        this.user = user;
    }

    // Getter and setter for the next node
    public NodoUsuarioCliente getNext() {
        return next;
    }

    public void setNext(NodoUsuarioCliente next) {
        this.next = next;
    }

    // Getter and setter for the previous node
    public NodoUsuarioCliente getPrevious() {
        return previous;
    }

    public void setPrevious(NodoUsuarioCliente previous) {
        this.previous = previous;
    }
}
