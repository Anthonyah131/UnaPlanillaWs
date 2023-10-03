package cr.ac.una.unaplanilla;

import cr.ac.una.unaplanilla.model.Gasto;
import cr.ac.una.unaplanilla.model.Salonero;
import cr.ac.una.unaplanilla.util.FlowController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    public List<Gasto> gastos = FXCollections.observableArrayList();
    public List<Salonero> saloneros = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/cr/ac/una/unaplanilla/resources/LogoUNArojo.png")));
        stage.setTitle("UNA PLANILLA");
        FlowController.getInstance().goViewInWindow("LogInView");

//        Gasto gasto0 = new Gasto(50, "De putas");
//        Gasto gasto1 = new Gasto(1000, "De putas1");
//        Gasto gasto2 = new Gasto(56, "De putas2");
//        Gasto gasto3 = new Gasto(100, "De putas3");
//
//        gastos.add(gasto0);
//        gastos.add(gasto1);
//        gastos.add(gasto2);
//        gastos.add(gasto3);
//
//        double gastosTotal = gastos.stream().filter(gasto -> gasto.monto * 1.15 < 100).mapToDouble(gasto -> gasto.monto * 1.15).sum();
//
//        System.out.println("Total de gastos después del IVA y filtrado: " + gastosTotal);
//        Salonero salonero1 = new Salonero(19, false, 1000.0, 51.0);
//        Salonero salonero2 = new Salonero(30, false, 1100.0, 60.0);
//        Salonero salonero3 = new Salonero(22, true, 950.0, 40.0);
//        Salonero salonero4 = new Salonero(28, false, 1050.0, 55.0);
//        Salonero salonero5 = new Salonero(32, true, 1200.0, 70.0);
//        Salonero salonero6 = new Salonero(18, false, 700.0, 78.0);
//        Salonero salonero7 = new Salonero(15, true, 400.0, 30.0);
//        Salonero salonero8 = new Salonero(56, false, 1550.0, 86.0);
//        Salonero salonero9 = new Salonero(34, false, 1050.0, 45.0);
//        Salonero salonero10 = new Salonero(21, true, 600.0, 70.0);
//
//        saloneros.add(salonero1);
//        saloneros.add(salonero2);
//        saloneros.add(salonero3);
//        saloneros.add(salonero4);
//        saloneros.add(salonero5);
//        saloneros.add(salonero6);
//        saloneros.add(salonero7);
//        saloneros.add(salonero8);
//        saloneros.add(salonero9);
//        saloneros.add(salonero10);
//
//        double monto = obtenerMontoMinimoDePropinas(18, 50);
//
//        System.out.println("El monto es: " + monto);
    }

    public static void main(String[] args) {
        launch();
    }

    Predicate<Salonero> noEsTemporal() {
        return salonero -> !salonero.getTemporal();
    }

    Predicate<Salonero> edadMayorQue(int edadMinima) {
        return salonero -> salonero.getEdad() > edadMinima;
    }

    Predicate<Salonero> tienePropinaMinima(double propinaMinima) {
        return salonero -> salonero.getPropina() >= propinaMinima;
    }

    public double obtenerMontoMinimoDePropinas(int edadMinima, double propinaMinima) {
        double montoMinimoPropinas = saloneros.stream()
                .filter(noEsTemporal().and(edadMayorQue(edadMinima)).and(tienePropinaMinima(propinaMinima)))
                .mapToDouble(Salonero::getPropina)
                .min()
                .orElse(0);

        saloneros.stream()
                .filter(noEsTemporal().and(edadMayorQue(edadMinima)).and(tienePropinaMinima(propinaMinima)).and(salonero -> salonero.getPropina() >= propinaMinima))
                .forEach(salonero -> salonero.setSalarioBase(salonero.getSalarioBase() * 1.10));

        saloneros.stream().forEach(salonero -> System.out.println("Edad: " + salonero.getEdad()
                + " Propina: " + salonero.getPropina() + " Salario: " + salonero.getSalarioBase()));

        return montoMinimoPropinas;
    }

}
