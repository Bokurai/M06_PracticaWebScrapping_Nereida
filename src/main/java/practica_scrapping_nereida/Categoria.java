package practica_scrapping_nereida;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@XmlRootElement(name = "lecturalia")
public class Categoria {

    private String nombre_categoria;
    private Set<Tema> conjuntoTemas;


    public Categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
        this.conjuntoTemas = new HashSet<>();
    }

    @XmlElement
    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

    @XmlElement(name = "literatura")
    public Set<Tema> getTemas() {
        return conjuntoTemas;
    }

    public void setTemas(Set<Tema> conjuntoTemas) {
        this.conjuntoTemas = conjuntoTemas;
    }
    public void addTema(Tema tema) {
        conjuntoTemas.add(tema);
    }

    //para obtener el objeto completo
    public Set<Libro> getLibros() {
        Set<Libro> libros = new HashSet<>();
        for (Tema tema : conjuntoTemas) {
            libros.addAll(tema.getLibros());
        }
        return libros;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(nombre_categoria, categoria.nombre_categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre_categoria);
    }
}
