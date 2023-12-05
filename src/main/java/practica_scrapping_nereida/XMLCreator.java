package practica_scrapping_nereida;

//incluimos los imports necesarios
import org.w3c.dom.*;
import org.w3c.dom.Element;
import javax.xml.bind.*;
import javax.xml.parsers.*;
import java.util.*;
import javax.xml.transform.stream.*;
public class XMLCreator {
public static Document  convertir_categoria_XML (Categoria categoria) {
    //iniciamos las instancias de las librerías que necesitamos
   try {
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       DocumentBuilder builder = factory.newDocumentBuilder();
       Document doc = builder.newDocument();

       //MAQUETACIÓN DE ELEMENTOS

       //Root:Nombre de la página web
       Element docElement = doc.createElement("lecturalia");
       doc.appendChild(docElement);

       //Categoria
       Element categoriaElement = doc.createElement(categoria.getNombre_categoria());
       docElement.appendChild(categoriaElement);

       //Tema:como hay un set de temas se añade con un for
       Set<Tema> temas = categoria.getTemas();

       for (Tema tema : temas) {
           Element temaElement = doc.createElement("tema");
           temaElement.appendChild(doc.createTextNode(tema.getNombre_tema()));
           categoriaElement.appendChild(temaElement);
       }

       //Libro: Al igual que con Temas, trabajamos con otro set que se declaró en categoria
       Set<Libro> libros = categoria.getLibros();

       for (Libro libro : libros) {
           Element libroElement = doc.createElement("libro");

           //título
           Element tituElement = doc.createElement("título");
           tituElement.appendChild(doc.createTextNode(libro.getTitulo()));
           libroElement.appendChild(tituElement);

           //autores
           Element autoresElement = doc.createElement("autores");
           for (String autor : libro.getAutores()) {
               Element autorElement = doc.createElement("autor");
               autorElement.appendChild(doc.createTextNode(autor));
               autoresElement.appendChild(autorElement);
           }
           libroElement.appendChild(autoresElement);

           //titulo del resumen
           Element resumtituloElement = doc.createElement("resumen");
           resumtituloElement.appendChild(doc.createTextNode(libro.getResumen()));
           libroElement.appendChild(resumtituloElement);

           //texto del resumen
           Element resumtxtElement = doc.createElement("resumen_texto");
           resumtxtElement.appendChild(doc.createTextNode(libro.getResumen_texto()));
           libroElement.appendChild(resumtxtElement);

           //los temas que contenga el libro, ya que estos se extraen a partir de la ficha del libro en la web
           Set<Tema> temasLibro = libro.getTemas();
           for (Tema temaLibro : temasLibro) {
               Element temaLibroElement = doc.createElement("tema_libro");
               temaLibroElement.appendChild(doc.createTextNode(temaLibro.getNombre_tema()));
               libroElement.appendChild(temaLibroElement);
           }
           categoriaElement.appendChild(libroElement);

       }
       return doc;
   }catch (ParserConfigurationException e) {
       e.printStackTrace();
       return null;
   }

}
}
