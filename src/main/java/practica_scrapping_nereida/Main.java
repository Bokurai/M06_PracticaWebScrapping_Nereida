package practica_scrapping_nereida;

//importamos las clases necesarias

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    private static WebDriver driver;

    public static void main(String[] args) {

        //establecemos los paths
        System.out.println(System.getenv("PATH"));
        System.out.println(System.getenv("HOME"));

        //redirigimos la carga de la operación principal a GeckoDriver
        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
        FirefoxOptions options = new FirefoxOptions();
        driver = new FirefoxDriver(options);

        //navegamos hasta la página web en concreto
        driver.get("https://www.lecturalia.com/");

        //establecemos un TimeWait implícito para que a la hora de encontrar los elementos se espere a que el driver proceda
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //para evitar clicar manualmente el Aceptar las cookies del navegador cada vez que iniciamos el programa
        driver.findElement(By.id("didomi-notice-agree-button")).click();
        WebElement libros = driver.findElement(By.linkText("Libros"));
        libros.click();


        //EXTRACCIÓN DE ENLACES Y DATOS

        //primero creamos la instancia de Categoría
        Categoria catLiteratura = new Categoria("Literatura");

        //luego para evitar repeticiones de instancias de la clase Tema, creamos un set para cada categoría, ya que hay Temas exclusivos de cada uno
        Set<Tema> conjuntoTemasLiteratura = new HashSet<>();

        //1.Categoría: Literatura
        WebElement literatura = driver.findElement(By.linkText("Literatura"));
        literatura.click();

        //extraeremos los libros de la segunda página
        saltarpaginaencategoria();

        //creamos un arraylist para guardar los enlaces a partir de linksLiteratura
        List<String> litEnlaces = new ArrayList<>();

        //buscamos el div correspondiente a la lista de libros y los propios elementos li del la lista desordenada
        WebElement divLibros = driver.findElement(By.className("datalist"));
        List<WebElement>linksLiteratura = divLibros.findElements(By.tagName("li"));

        //como queremos extraer sólo 50 enlaces de la propia página, creamos un bucle en un método aparte y esclarecemos los parámetros para este caso
        int maxEnlacesLit = Math.min(2,linksLiteratura.size());
        extraerLinks(maxEnlacesLit, linksLiteratura, litEnlaces);

        //tras invocar el método, por conveniencia convertiremos litEnlaces a un array simple
        String[] literaturaEnlacesArray = litEnlaces.toArray(new String[0]);

        //invocamos el método para extraer los elementos de los enlaces
        procesarEnlaces(conjuntoTemasLiteratura, literaturaEnlacesArray, catLiteratura);

        //una vez acabe el proceso, vuelve a la página de Literatura
        driver.navigate().back();

        //para crear los CSV
        File temas_y_libros = CSVCreator.CSV_temas_y_libros(new ArrayList<>(conjuntoTemasLiteratura), "temas_y_libros.csv");
        File temas_y_categoria = CSVCreator.CSV_temas_y_categoria(catLiteratura, "temas_y_categoria.csv");
    }


    //método para poder ir a otras páginas en la misma categoría
    public static void saltarpaginaencategoria(){
        WebElement siguiente = driver.findElement(By.linkText("Siguiente"));
        siguiente.click();
    }

    //método para extraer enlaces
    public static void extraerLinks(int maxEnlaces, List<WebElement> cosas,List<String> links) throws NoSuchElementException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //con un for estableciendo el máximo de 50 elementos, recogemos los enlaces
        for (int i = 0; i < maxEnlaces; i++) {
            WebElement elementosLi = cosas.get(i);
            try {
                // buscamos el tag a utilizando wait
                WebElement link = wait.until(ExpectedConditions.elementToBeClickable(elementosLi.findElement(By.tagName("a"))));

                // extraemos el texto del atributo href
                String urlLi = link.getAttribute("href");

                // finalmente, lo añadimos a la lista
                links.add(urlLi);
            } catch (NoSuchElementException e) {
                System.out.println("No se ha encontrado el elemento u-u");
            }
        }
    }

    //método para procesar los enlaces en un bucle
    private static void procesarEnlaces(Set<Tema> conjuntoTemas, String[] enlacesArray, Categoria categoria) {
        for (String enlace : enlacesArray) {
            extraerInfo(enlace, conjuntoTemas, categoria);
        }
    }

    //método para extraer la info
    private static void extraerInfo(String enlaceLibro, Set<Tema> conjuntoTemas, Categoria categoria) {
        driver.get(enlaceLibro);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Extraer información del libro
        Libro libro = new Libro();

        try {
            //para incluir el tema en su Set
            WebElement temaElem = driver.findElement(By.xpath("//div[@class='profile__data']//p[strong='Temas:']"));
            WebElement temaElemTexto = temaElem.findElement(By.tagName("a"));
            String temaNombre = temaElemTexto.getText().trim();

            Tema tema = new Tema(temaNombre, categoria);

            //comprobamos que el tema exista en el Set
            if (!conjuntoTemas.contains(tema)) {
                conjuntoTemas.add(tema);
            }

            // ahora, establecemos la integración entre los objetos
            tema.addLibro(libro);

            categoria.addTema(tema);

            libro.addTemas(conjuntoTemas);

            // Manejo individual para cada campo
            try {
                WebElement tituloElem = driver.findElement(By.cssSelector("h1.title"));
                libro.setTitulo(tituloElem.getText().trim());
            } catch (NoSuchElementException e) {
                System.out.println("Elemento 'Título' no encontrado: " + e.getMessage());
                libro.setTitulo("Título no disponible");
            }

            try {
                List<WebElement> autorElems = driver.findElements(By.xpath("//div[@class='web-middle__highlight profile__header']/strong/a"));
                List<String> autores = autorElems.stream().map(WebElement::getText).collect(Collectors.toList());
                libro.setAutores(autores);
            } catch (NoSuchElementException e) {
                System.out.println("Elemento 'Autor' no encontrado: " + e.getMessage());
                libro.setAutores(Collections.singletonList("Autor no disponible"));
            }

            try {
                WebElement resumElem = driver.findElement(By.cssSelector("div.profile__text h2#ProfileBiography"));
                libro.setResumen(resumElem.getText().trim());
            } catch (NoSuchElementException e) {
                System.out.println("Elemento 'Resumen' no encontrado: " + e.getMessage());
                libro.setResumen("Resumen no disponible");
            }

            try {
                List<WebElement> resumTxtElems = driver.findElements(By.cssSelector("div.profile__text p"));
                String alltxt = resumTxtElems.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining("\n"));
                libro.setResumen_texto(alltxt);
            } catch (NoSuchElementException e) {
                System.out.println("Elemento 'Texto del resumen' no encontrado: " + e.getMessage());
                libro.setResumen_texto("Texto del resumen no disponible");
            }

        } catch (NoSuchElementException e) {
            // En caso de no haber información para el tema
            System.out.println("Elemento 'Temas' no encontrado: " + e.getMessage());
        } finally {
            // Actualizar la lista de temas en la categoría
            categoria.setTemas(conjuntoTemas);
        }
    }




}

