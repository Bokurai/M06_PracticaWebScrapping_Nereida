package practica_scrapping_nereida;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** This is a reference to address the class as a root element for the XML.
 * @author bokurai
 */
@XmlRootElement(name = "lecturalia")
/* This is a reference to address the class as a root element for the XML.
  @author bokurai
 */
public class Categoria {

    /**A String to reference the name of the main book theme, e.g. Literature */
    private String nombre_categoria;
    /**A HashSet used to store all the Tema objects, to mantain unique values and prevent duplicates.
     */
    private Set<Tema> conjuntoTemas;

    /**A constructor for the class.
     * @author bokurai
     */
    public Categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
        this.conjuntoTemas = new HashSet<>();
    }

    /**Getter method for nombre_categoria.
     * @author bokurai
     **/
    @XmlElement
    public String getNombre_categoria() {
        return nombre_categoria;
    }

    /**Setter method for nombre_categoria.
     * @author bokurai
     **/
    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

    /**Getter for the Tema HashSet.
     * @author bokurai
     **/
    @XmlElement(name = "literatura")
    public Set<Tema> getTemas() {
        return conjuntoTemas;
    }

    /**Setter for the Tema HashSet.
     * @author bokurai
     **/
    public void setTemas(Set<Tema> conjuntoTemas) {
        this.conjuntoTemas = conjuntoTemas;
    }

    /**Method to add a Tema object into the HashSet.
     * @author bokurai
     **/
    public void addTema(Tema tema) {
        conjuntoTemas.add(tema);
    }

    /**Getter method for all the Libro instances
     * @author bokurai
     **/
    //para obtener el objeto completo
    public Set<Libro> getLibros() {
        Set<Libro> libros = new HashSet<>();
        for (Tema tema : conjuntoTemas) {
            libros.addAll(tema.getLibros());
        }
        return libros;
    }

    /**Comparator for Categoria
     * @author bokurai
     **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(nombre_categoria, categoria.nombre_categoria);
    }

    /**HashCode for nombre_categoria.
     * @author bokurai
     **/
    @Override
    public int hashCode() {
        return Objects.hash(nombre_categoria);
    }
}
