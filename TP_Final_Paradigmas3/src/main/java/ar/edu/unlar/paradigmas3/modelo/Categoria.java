package ar.edu.unlar.paradigmas3.modelo;

public class Categoria {
    private Integer idCategoria;
    private String nombre;

    public Categoria() {
    }
//para busqueda por id y por nombre de categoria
    public Categoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Categoria(Integer idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return String.format("Categoria: %s", this.nombre);
    }
}
