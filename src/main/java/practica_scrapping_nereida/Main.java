package practica_scrapping_nereida;

//Importamos las clases necesarias
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.util.concurrent.TimeUnit;

public class Main {
    private static WebDriver driver;

    public static void main(String[] args) {

        //Establecemos los paths
        System.out.println(System.getenv("PATH"));
        System.out.println(System.getenv("HOME"));

        //Redirigimos la carga de la operación principal a GeckoDriver
        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
        FirefoxOptions options = new FirefoxOptions();
        driver = new FirefoxDriver(options);

        //navegamos hasta la página web en concreto
        driver.get("https://www.lecturalia.com/");

        //Establecemos un TimeWait implícito para que a la hora de encontrar los elementos se espere a que el driver proceda
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Para evitar clicar manualmente el Aceptar las cookies del navegador cada vez que iniciamos el programa
        driver.findElement(By.id("didomi-notice-agree-button")).click();
        WebElement libros = driver.findElement(By.linkText("Libros"));
        libros.click();

        //Empezamos con la categoría literatura
        WebElement literatura = driver.findElement(By.linkText("Literatura"));
        literatura.click();
        saltaranuncios();

        //vamos a la segunda página
        saltarpaginaencategoria();


        driver.close();
    }

    //método para poder saltar los anuncios cada vez que hagamos cambio de página
    public static void saltaranuncios() {
        WebElement anuncios = driver.findElement(By.id("ad_position_box"));
        try {
            if (anuncios.isDisplayed()) {
                driver.findElement(By.id("dismiss-button")).click();
            }else {

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
}