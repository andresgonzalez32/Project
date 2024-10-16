package Modelos;

import java.io.*;
import javax.swing.JOptionPane;

/**
 *
 * @author andre
 */
public class ModeloArchivoUsuarioAdministrador {

    // Guardar los usuarios en un archivo
    public static void saveUsersFile(ListaCircularDobleUsuario usuarios) throws IOException {
        // Leer los usuarios existentes del archivo
        ListaCircularDobleUsuario usuariosExistentes = fileReaderUsers();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarioAdministrador.txt", true))) {
            NodoUsuario actual = usuarios.getCabeza();
            if (actual != null) {
                do {
                    // Verificar si el usuario ya existe
                    if (!existeUsuario(usuariosExistentes, actual.getUser())) {
                        writer.write(actual.getUser().toString());
                        writer.newLine();
                        JOptionPane.showMessageDialog(null, "User successfully registered", "Message", JOptionPane.INFORMATION_MESSAGE, null);
                    } else {
                        JOptionPane.showMessageDialog(null, "The user already exists", "Message", JOptionPane.INFORMATION_MESSAGE, null);
                    }
                    actual = actual.getNext();
                } while (actual != usuarios.getCabeza());
            }
        }
    }

    // Leer los usuarios desde el archivo
    public static ListaCircularDobleUsuario fileReaderUsers() throws IOException {
        ListaCircularDobleUsuario usuarios = new ListaCircularDobleUsuario();
        File file = new File("usuarioAdministrador.txt");
        
        // Verifica si el archivo existe, si no, lo crea
        if (!file.exists()) {
            file.createNewFile(); // Crea un archivo vacío
        }

        // Lee los usuarios desde el archivo
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                usuarios.add(ModeloUsuarioAdministrador.fromString(linea));
            }
        }
        return usuarios;
    }

    // Verificar si un usuario ya existe en la lista
    private static boolean existeUsuario(ListaCircularDobleUsuario usuariosExistentes, ModeloUsuarioAdministrador nuevoUsuario) {
        NodoUsuario actual = usuariosExistentes.getCabeza();
        if (actual != null) {
            do {
                ModeloUsuarioAdministrador usuarioExistente = actual.getUser();
                // Compara por nombre de usuario o cualquier otro criterio de unicidad
                if (usuarioExistente.getUser().equals(nuevoUsuario.getUser())) {
                    return true; // El usuario ya existe
                }
                actual = actual.getNext();
            } while (actual != usuariosExistentes.getCabeza());
        }
        return false; // El usuario no existe
    }

    // Actualizar un usuario en el archivo
    public static void actualizarUsuarioEnArchivo(ModeloUsuarioAdministrador usuarioActualizado) throws IOException {
        // Leer todos los usuarios del archivo
        ListaCircularDobleUsuario usuarios = fileReaderUsers();

        NodoUsuario actual = usuarios.getCabeza();
        if (actual != null) {
            do {
                ModeloUsuarioAdministrador usuario = actual.getUser();
                if (usuario.getId() == usuarioActualizado.getId()) {
                    // Reemplazar el usuario encontrado por el actualizado
                    actual.setUser(usuarioActualizado);
                    break;
                }
                actual = actual.getNext();
            } while (actual != usuarios.getCabeza());
        }

        // Guardar todos los usuarios de vuelta en el archivo (sobrescribiendo)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarioAdministrador.txt"))) {
            NodoUsuario nodoActual = usuarios.getCabeza();
            if (nodoActual != null) {
                do {
                    writer.write(nodoActual.getUser().toString());
                    writer.newLine();
                    nodoActual = nodoActual.getNext();
                } while (nodoActual != usuarios.getCabeza());
            }
        }
    }
    
    // Método para hacer login
    public static boolean login(String user, String password) throws IOException {
        ListaCircularDobleUsuario usuarios = fileReaderUsers();
        NodoUsuario actual = usuarios.getCabeza();
        if (actual != null) {
            do {
                ModeloUsuarioAdministrador usuario = actual.getUser();
                // Verifica si el usuario y la contraseña coinciden
                if (usuario.getUser().equals(user) && usuario.getPasswork().equals(password) && usuario.getRol().equals("Administrador")) {
                    return true; // Login exitoso
                }
                actual = actual.getNext();
            } while (actual != usuarios.getCabeza());
        }
        return false; // Login fallido
    }
}
