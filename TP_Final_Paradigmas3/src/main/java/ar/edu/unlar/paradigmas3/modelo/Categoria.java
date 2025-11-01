package ar.edu.unlar.paradigmas3.modelo;

public class Categoria {
    private Integer idCategoria;
    private String nombre;

    public Categoria() {
    }

    public Categoria(String nombre, Integer idCategoria) {
        this.nombre = nombre;
        this.idCategoria = idCategoria;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
