package practica_scrapping_nereida;

//importamos las clases necesarias
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

        //para poder guardar todos los libros, hacemos un Map con List
        List<Map<String, Map<String, String>>> listaDefinitiva = new ArrayList<>();

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

        //para extraer la información de los libros y añadirla al Map
        List<Map<String, Map<String, String>>> librosLiteratura = extraerInfo(literaturaEnlacesArray, "Literatura");
        listaDefinitiva.addAll(librosLiteratura);

        //vovler a la página anterior
        driver.navigate().back();

        //2.Categoría: Ensayo
        //para este seguiremos los mismos pasos que la categoría anterior, cambiando un par de nombres de variables en el proceso
        WebElement ensayo = driver.findElement(By.linkText("Ensayo"));
        ensayo.click();

        //extraeremos los libros de la segunda página
        saltarpaginaencategoria();

        //creamos un arraylist para guardar los enlaces a partir de linksEnsayo
        List<String> ensEnlaces = new ArrayList<>();

        //para volver a encontrar el divLibros una vez pase a la siguiente categoría
        divLibros = driver.findElement(By.className("datalist"));

        //buscamos el div correspondiente a la lista de libros y los propios elementos li del la lista desordenada
        List<WebElement>linksEnsayo = divLibros.findElements(By.tagName("li"));

        //como queremos extraer sólo 50 enlaces de la propia página, creamos un bucle en un método aparte y esclarecemos los parámetros para este caso
        int maxEnlacesEns = Math.min(50, linksEnsayo.size());
        extraerLinks(maxEnlacesEns, linksEnsayo, ensEnlaces);

        //tras invocar el método, por conveniencia convertiremos ensEnlaces a un array simple
        String[] ensayoEnlacesArray = ensEnlaces.toArray(new String[0]);

       //para extraer la información de los libros y añadirla al Map
        List<Map<String, Map<String, String>>> librosEnsayo = extraerInfo(ensayoEnlacesArray, "Ensayo");
        listaDefinitiva.addAll(librosEnsayo);

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

    //método para extraer la info
    public static List<Map<String, Map<String, String>>>extraerInfo(String[] enlaces, String categoria) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //definimos el Map
        List<Map<String, Map<String, String>>> categoLibros = new ArrayList<>();
        //primero con un bucle navegamos a la página que contiene el enlace y extraemos la información
        for (String enlace: enlaces){

            //en este caso he decidido usar JavaScript para que cuando acabe un libro de extraer la info, vuelva atrás y cargue el siguiente en la misma pestaña
            ((JavascriptExecutor) driver).executeScript("window.open(arguments[0],'_self');", enlace);

            //ahora a extraer la información
            //creamos un Hashmap dentro del otro map
            Map<String,String> info = new HashMap<>();

            WebElement tituloElem = driver.findElement(By.cssSelector("h1.title"));
            String titulo = tituloElem.getText().trim();
            info.put("Título", titulo);

            WebElement autorElem = driver.findElement(By.cssSelector("div.web-middle__highlight.profile__header p strong:contains('Año publicación:') + a"));
            String autor = autorElem.getText().trim();
            info.put("Autor", autor);

            WebElement editElem = driver.findElement(By.xpath("//div[@class='profile__data']//p/strong[text()='Editorial:']"));
            String editorial = editElem.getText().trim();
            info.put("Editorial", editorial);

            WebElement fechaElem = driver.findElement(By.xpath("//div[@class='profile__data']//p/strong[text()='Editorial:']"));
            String fecha_publicacion = fechaElem.getText().trim();
            info.put("Fecha publicación", fecha_publicacion);

            WebElement resumElem = driver.findElement(By.xpath("//strong[contains(@href, '')][1]"));
            String resumen = resumElem.getText().trim();
            info.put("Resumen", resumen);

            WebElement resumTxtElem = driver.findElement(By.xpath("//strong[contains(@href, '')][1]"));
            String resumen_texto = resumTxtElem.getText().trim();
            info.put("Resumen texto", resumen_texto);

            //ahora utilizaremos el elemento Tema para clasificarlo como un Map de libros y representar el diagrama

            WebElement temaElem = driver.findElement(By.xpath("//*[@id=\"ext_web\"]/main/div[2]/div/div[1]/ul/li[3]/p/a"));
            String tema = temaElem.getText().trim();

            Map<String, String> temaMap = new HashMap<>();
            temaMap.put("Tema", tema);
            temaMap.put("Categoría", categoria);


            Map<String, String> libroMap = new HashMap<>(info);


            Map<String, Map<String, String>> categoriaTemaLibroMap = new HashMap<>();
            categoriaTemaLibroMap.put("Libro", libroMap);
            categoriaTemaLibroMap.put("Tema y Categoría", temaMap);

            categoLibros.add(categoriaTemaLibroMap);


            driver.close();
            driver.switchTo().window(driver.getWindowHandles().iterator().next());
        }

            return categoLibros;
    }
}
