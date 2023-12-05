package practica_scrapping_nereida;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"nombre_tema", "libros"})

public class Tema {
    private String nombre_tema;
    private Categoria categoria;

    /**An instance for the Libros List of objects
     * @author bokurai
     **/
    private List<Libro> libros;

    /**Main constructor of the class.
     * @author bokurai
     **/
    public Tema(String nombre_tema, Categoria categoria) {
        this.nombre_tema = nombre_tema;
        this.categoria = categoria;
        this.libros = new ArrayList<>();
    }

    @XmlElement
    /**Getter method for nombre_tema.
     * @author bokurai
     **/
    public String getNombre_tema() {
        return nombre_tema;
    }

    /**Setter method for nombre_tema.
     * @author bokurai
     **/
    public void setNombre_tema(String nombre_tema) {
        this.nombre_tema = nombre_tema;
    }

    /**Getter method for Categoria class.
     * @author bokurai
     **/
    public Categoria getCategoria() {
        return categoria;
    }

    /**Setter method for Categoria class.
     * @author bokurai
     **/
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @XmlElement
    public List<Libro> getLibros() {
        return libros;
    }

    public void addLibro(Libro libro) {
        libros.add(libro);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tema otroTema = (Tema) obj;
        return Objects.equals(nombre_tema, otroTema.nombre_tema);
    }

    @Override
    public int hashCode() {
        return nombre_tema == null ? 0 : nombre_tema.hashCode();
    }
}
