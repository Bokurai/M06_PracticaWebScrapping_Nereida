package practica_scrapping_nereida;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** This is a reference to address the class as a root element for the XML.
 * @author bokurai
 */
@XmlRootElement
@XmlType(propOrder = {"titulo", "autores", "resumen", "resumen_texto"})
/** This class is used to contain Libro attributes, getters/setters and a couple of methods to use.
 * @author bokurai
 */
public class Libro {

    private String titulo;
    private List<String> autores;
    private String resumen;
    private String resumen_texto;
    /**A HashSet used to store all the Tema objects, to mantain unique values and prevent duplicates.
     */
    private Set<Tema> temas;

    /** A semi-empty constructor to address some situations.
     * @author bokurai
     */
    public Libro() {
        this.autores = new ArrayList<>();
        this.temas = new HashSet<>();
    }

    /** The main constructor of the class.
     * @author bokurai
     */
    public Libro(String titulo, List<String> autores, String resumen, String resumen_texto, Set<Tema> temas) {
        this.titulo = titulo;
        this.autores = autores;
        this.resumen = resumen;
        this.resumen_texto = resumen_texto;
        this.temas = temas;
    }

    /**Getter method for titulo.
     * @author bokurai
     **/
    @XmlElement
    public String getTitulo() {
        return titulo;
    }

    /**Setter method for titulo.
     * @author bokurai
     **/
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**Getter method for autores.
     * @author bokurai
     **/
    @XmlElement
    public List<String> getAutores() {
        return autores;
    }

    /**Setter method for autores.
     * @author bokurai
     **/
    public void setAutores(List<String> autores) {
        this.autores = autores;
    }

    /**Getter method for resumen.
     * @author bokurai
     **/
    @XmlElement
    public String getResumen() {
        return resumen;
    }

    /**Setter method for resumen.
     * @author bokurai
     **/
    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    /**Getter method for resumen_texto.
     * @author bokurai
     **/
    @XmlElement
    public String getResumen_texto() {
        return resumen_texto;
    }

    /**Setter method for resumen_texto.
     * @author bokurai
     **/
    public void setResumen_texto(String resumen_texto) {
        this.resumen_texto = resumen_texto;
    }

    /**Getter method for Temas HashSet.
     * @author bokurai
     **/
    public Set<Tema> getTemas() {
        return temas;
    }

    /**Method to add all the Tema instances.
     * @author bokurai
     **/
    public void addTemas(Set<Tema> temas) {
        this.temas.addAll(temas);
    }

}
