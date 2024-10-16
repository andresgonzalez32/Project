package Modelos;

/**
 *
 * @author andre
 */
public class NodoUsuario {

    // Private fields
    private ModeloUsuarioAdministrador user;
    private NodoUsuario next;
    private NodoUsuario previous;

    // Constructor
    public NodoUsuario(ModeloUsuarioAdministrador user) {
        this.user = user;
        this.next = null;
        this.previous = null;
    }

    // Getter and setter for the user
    public ModeloUsuarioAdministrador getUser() {
        return user;
    }

    public void setUser(ModeloUsuarioAdministrador user) {
        this.user = user;
    }

    // Getter and setter for the next node
    public NodoUsuario getNext() {
        return next;
    }

    public void setNext(NodoUsuario next) {
        this.next = next;
    }

    // Getter and setter for the previous node
    public NodoUsuario getPrevious() {
        return previous;
    }

    public void setPrevious(NodoUsuario previous) {
        this.previous = previous;
    }
}
