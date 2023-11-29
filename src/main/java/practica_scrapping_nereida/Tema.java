package practica_scrapping_nereida;

public class Tema {
    private String nombre_tema;
    private Categoria categoria;

    public Tema(String nombre_tema, Categoria categoria) {
        this.nombre_tema = nombre_tema;
        this.categoria = categoria;
    }

    public String getNombre_tema() {
        return nombre_tema;
    }

    public void setNombre_tema(String nombre_tema) {
        this.nombre_tema = nombre_tema;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
