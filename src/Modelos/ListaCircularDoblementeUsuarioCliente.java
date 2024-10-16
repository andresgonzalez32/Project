/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;


public class ListaCircularDoblementeUsuarioCliente {
    
    private NodoUsuarioCliente cabeza;
    private NodoUsuarioCliente cola;

    public ListaCircularDoblementeUsuarioCliente() {
        this.cabeza = null;
        this.cola = null;
    }

    public NodoUsuarioCliente getCabeza() {
        return cabeza;
    }

    public void setCabeza(NodoUsuarioCliente cabeza) {
        this.cabeza = cabeza;
    }

    public NodoUsuarioCliente getCola() {
        return cola;
    }

    public void setCola(NodoUsuarioCliente cola) {
        this.cola = cola;
    }
    
    

    // Agregar a la lista
    public void add(ModeloUsuarioCliente usuario) {
        NodoUsuarioCliente nuevo = new NodoUsuarioCliente(usuario);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
            cabeza.setNext(cabeza);
            cabeza.setPrevious(cola);
        } else {
            cola.setNext(nuevo);
            nuevo.setPrevious(cola);
            nuevo.setNext(cabeza);
            cabeza.setPrevious(nuevo);
            cola = nuevo;
        }
    }

    // Mostrar los elementos de la lista
    public void show() {
        if (cabeza == null) {
            System.out.println("La lista está vacía.");
            return;
        }
        NodoUsuarioCliente actual = cabeza;
        do {
            System.out.println(actual.getUser().toString());
            actual = actual.getNext();
        } while (actual != cabeza);
    }
}
