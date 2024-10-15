package Modelos;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author andre
 */
public class ModeloUsuarioAdministrador {

    private String name;
    private String user;
    private String passwork;
    private String rol;
    private int id;
    private static Set<Integer> idsGenerados = new HashSet<>();
    private static Random random = new Random();

    public ModeloUsuarioAdministrador(String name, String user, String passwork) {
        this.name = name;
        this.user = user;
        this.passwork = passwork;
        this.rol = "Administrador";
        this.id = generarId();
    }

    public ModeloUsuarioAdministrador(String name, String user, String passwork, int id) {
        this.name = name;
        this.user = user;
        this.passwork = passwork;
        this.rol = "Administrador";
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswork() {
        return passwork;
    }

    public void setPasswork(String passwork) {
        this.passwork = passwork;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name + "," + user + "," + passwork + "," + rol + "," + id;
    }

    public static ModeloUsuarioAdministrador fromString(String linea) {
        // Separar por comas y convertir valores numéricos
        String[] datos = linea.split(",");
        return new ModeloUsuarioAdministrador(
                datos[0].trim(),
                datos[1].trim(),
                datos[2].trim(),
                Integer.parseInt(datos[4].trim())
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

}
