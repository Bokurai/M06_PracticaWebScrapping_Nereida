package practica_scrapping_nereida;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@XmlRootElement
@XmlType(propOrder = {"titulo", "autores", "resumen", "resumen_texto"})
public class Libro {

    private String titulo;
    private List<String> autores;
    private String resumen;
    private String resumen_texto;
    private Set<Tema> temas;

    public Libro() {
        this.autores = new ArrayList<>();
        this.temas = new HashSet<>();
    }

    public Libro(String titulo, List<String> autores, String resumen, String resumen_texto, Set<Tema> temas) {
        this.titulo = titulo;
        this.autores = autores;
        this.resumen = resumen;
        this.resumen_texto = resumen_texto;
        this.temas = temas;
    }

    @XmlElement
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @XmlElement
    public List<String> getAutores() {
        return autores;
    }

    public void setAutores(List<String> autores) {
        this.autores = autores;
    }

    @XmlElement
    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    @XmlElement
    public String getResumen_texto() {
        return resumen_texto;
    }

    public void setResumen_texto(String resumen_texto) {
        this.resumen_texto = resumen_texto;
    }

    public Set<Tema> getTemas() {
        return temas;
    }

    public void addTemas(Set<Tema> temas) {
        this.temas.addAll(temas);
    }

}
