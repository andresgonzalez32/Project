package Modelos;

/**
 *
 * @author andre
 */
public class ListaCircularDobleUsuario {

    private NodoUsuario cabeza;
    private NodoUsuario cola;

    public ListaCircularDobleUsuario() {
        this.cabeza = null;
        this.cola = null;
    }

    public NodoUsuario getCabeza() {
        return cabeza;
    }

    public void setCabeza(NodoUsuario cabeza) {
        this.cabeza = cabeza;
    }

    public NodoUsuario getCola() {
        return cola;
    }

    public void setCola(NodoUsuario cola) {
        this.cola = cola;
    }
    
    

    // Agregar a la lista
    public void add(ModeloUsuarioAdministrador usuario) {
        NodoUsuario nuevo = new NodoUsuario(usuario);
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
        NodoUsuario actual = cabeza;
        do {
            System.out.println(actual.getUser().toString());
            actual = actual.getNext();
        } while (actual != cabeza);
    }
}
