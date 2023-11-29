package practica_scrapping_nereida;

public class Libro {

    private String titulo;
    private String autor;

    private String editorial;
    private String fecha_publicacion;
    private String resumen;
    private String resumen_texto;
    private Tema tema;

    public Libro(String titulo, String autor, String editorial, String fecha_publicacion, String resumen,String resumen_texto, Tema tema) {
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.fecha_publicacion = fecha_publicacion;
        this.resumen = resumen;
        this.tema = tema;
        this.resumen_texto = resumen_texto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(String fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getResumen_texto() {
        return resumen_texto;
    }

    public void setResumen_texto(String resumen_texto) {
        this.resumen_texto = resumen_texto;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }
}
