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
import java.util.List;
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

        System.out.println("Enlaces literatura como array:");
        for (String enlace : literaturaEnlacesArray) {
            System.out.println(enlace);
        }

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

        //ahora extraemos la información de los enlaces
        for (String enlace : ensayoEnlacesArray) {
            extraerInfo(enlace);
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

    //método para extraer la info
    public static void extraerInfo(String enla) {
        //primero navegamos a la página que contiene el enlace
        driver.navigate().to(enla);
    }

    //método para categorizar y subcategorizar los libros
}
