package practica_scrapping_nereida;

import com.opencsv.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**This class is used to convert all the important objects into two CSVs: one for Tema-Categoria, and another for Tema-Libro
 * @author bokurai
 **/
public class CSVCreator {

    /**This method uses a parameter to reference the Tema HasshSet and a String as a placeholder for an output file.
     * It's used to convert all the Tema and Libro objects to a CSV.
     * @author bokurai
     **/
    static public File CSV_temas_y_libros(List<Tema> temas, String nom_arxiu) {;
        File arxiu = new File(nom_arxiu);
        try {

            CSVWriter writer = new CSVWriter(new FileWriter(arxiu));
            writer.writeNext(new String[]{"Tema", "Título del libro", "Autores del libro", "Título resumen", "Resumen del libro"});
            for (Tema tema : temas) {
                for (Libro libro : tema.getLibros()) {
                    writer.writeNext(new String[]{
                            tema.getNombre_tema(),
                            libro.getTitulo(),
                            String.join(", ", libro.getAutores()),
                            libro.getResumen(),
                            libro.getResumen_texto()
                    });
                }
            }
            writer.flush();
            writer.close();
            return arxiu;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**This method uses a parameter to reference a Categoria instance and a String as a placeholder for an output file.
     * It's used to convert all the Tema and Categoria objects to a CSV.
     * @author bokurai
     **/
    static public File CSV_temas_y_categoria(Categoria categoria, String nom_arxiu) {;
        File arxiu = new File(nom_arxiu);
        try {

            CSVWriter writer = new CSVWriter(new FileWriter(arxiu));
            writer.writeNext(new String[]{"Categoria", "Temas"});
            writer.writeNext(new String[]{ categoria.getNombre_categoria()});
            for (Tema tema : categoria.getTemas()) {
                writer.writeNext(new String[]{
                        tema.getNombre_tema()
                });
            }
            writer.flush();
            writer.close();
            return arxiu;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

