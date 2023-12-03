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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        options.addPreference("dom.webnotifications.serviceworker.enabled", false);
        options.addPreference("dom.webnotifications.enabled", false);
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
        int maxEnlacesLit = Math.min(50,linksLiteratura.size());
        extraerLinks(maxEnlacesLit, linksLiteratura, litEnlaces);

        //tras invocar el método, por conveniencia convertiremos litEnlaces a un array simple
        String[] literaturaEnlacesArray = litEnlaces.toArray(new String[0]);

        //invocamos los métodos para extraer los libros
        procesarEnlaces(conjuntoTemasLiteratura, literaturaEnlacesArray, catLiteratura);

        //vovler a la página anterior
        driver.navigate().back();


        // Imprimir libros de la categoría Literatura
        System.out.println("Categoría: " + catLiteratura.getNombre_categoria());
        for (Tema tema : catLiteratura.getTemas()) {
            System.out.println("  Tema: " + tema.getNombre_tema());
            for (Libro libro : tema.getLibros()) {
                System.out.println("    Libro: " + libro.getTitulo());
                System.out.println("      Autor: " + libro.getAutor());
                System.out.println("      Editorial: " + libro.getEditorial());
                System.out.println("      Fecha de publicación: " + libro.getFecha_publicacion());
                System.out.println("      Resumen: " + libro.getResumen());
                System.out.println("      Texto del resumen: " + libro.getResumen_texto());
            }
        }


    }

    //método para poder saltar los anuncios cada vez que hagamos cambio de página
    public static void saltaranuncios() {
        WebElement anuncios = driver.findElement(By.id("ad_position_box"));
        try {
            if (anuncios.isDisplayed()) {
                driver.findElement(By.id("dismiss-button")).click();
            }
        } catch (Exception e) {
            e.getMessage();
        }
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
    private static void extraerInfo(String enlaceLibro, Set<Tema> conjuntoTemas,  Categoria categoria) {
        driver.get(enlaceLibro);


        // Extraer información del libro
        Libro libro = new Libro();

        try {
            //para incluir el tema en su Set
            WebElement temaElem = driver.findElement(By.xpath("//div[@class='profile__data']//p[strong='Temas:']"));
            WebElement temaElemTexto = temaElem.findElement(By.tagName("a"));
            String temaNombre = temaElemTexto.getText().trim();

            Tema tema = new Tema(temaNombre,categoria);

            //comprobamos que el tema exista en el Set
            if (!conjuntoTemas.contains(tema)) {
                conjuntoTemas.add(tema);
            }

            // Añadir libro al tema
            tema.addLibro(libro);

            WebElement tituloElem = driver.findElement(By.cssSelector("h1.title"));
            libro.setTitulo(tituloElem.getText().trim());

            WebElement autorElem = driver.findElement(By.xpath("//div[@class='web-middle__highlight profile__header']/strong/a"));
            libro.setAutor(autorElem.getText().trim());

            WebElement editElem = driver.findElement(By.xpath("//div[@class='profile__data']//p/strong[text()='Editorial:']"));
            libro.setEditorial(editElem.getText().trim());

            WebElement fechaElem = driver.findElement(By.xpath("//div[@class='profile__data']//p/strong[text()='Año publicación:']"));
            libro.setFecha_publicacion(fechaElem.getText().trim());

            WebElement resumElem = driver.findElement(By.cssSelector("div.profile__text h2#ProfileBiography"));
            libro.setResumen(resumElem.getText().trim());

            List<WebElement> resumTxtElems = driver.findElements(By.cssSelector("div.profile__text p"));
            String alltxt = resumTxtElems.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining("\n"));
            libro.setResumen_texto(alltxt);

        } catch (NoSuchElementException e) {
            //en caso de no haber información, utilizaremos estos setters para el objeto
            e.printStackTrace();
            libro.setTitulo("Título no disponible");
            libro.setAutor("Autor no disponible");
            libro.setEditorial("Editorial no disponible");
            libro.setFecha_publicacion("Fecha de publicación no disponible");
            libro.setResumen("Resumen no disponible");
            libro.setResumen_texto("Texto del resumen no disponible");
        }finally {
            // Volver a la página anterior
            driver.navigate().back();

            // Actualizar la lista de temas en la categoría
            categoria.setTemas(conjuntoTemas);
        }

    }



}

