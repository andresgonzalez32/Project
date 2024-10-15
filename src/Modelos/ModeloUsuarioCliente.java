/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ModeloUsuarioCliente {

    private String name;
    private String lastname;
    private String user;
    private String passwork;
    private String rol;
    private int id;
    private static Set<Integer> idsGenerados = new HashSet<>();
    private static Random random = new Random();

    public ModeloUsuarioCliente(String name, String lastname, String user, String passwork) {
        this.name = name;
        this.lastname = lastname;
        this.user = user;
        this.passwork = passwork;
        this.rol = "Cliente";
        this.id = generarId();
    }

    public ModeloUsuarioCliente(String name, String lastname, String user, String passwork, int id) {
        this.name = name;
        this.lastname = lastname;
        this.user = user;
        this.passwork = passwork;
        this.rol = "Cliente";
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
        return name + "," + lastname + "," + user + "," + passwork + "," + rol + "," + id;
    }

    public static ModeloUsuarioCliente fromString(String linea) {
        // Separar por comas y convertir valores numéricos
        String[] datos = linea.split(",");
        System.out.println(datos[0]);
        System.out.println(datos[1]);
        System.out.println(datos[2]);
        System.out.println(datos[3]);
        System.out.println(datos[4]);
        System.out.println(datos[5]);
        return new ModeloUsuarioCliente(
                datos[0].trim(),
                datos[1].trim(),
                datos[2].trim(),
                datos[3].trim(),
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
}
