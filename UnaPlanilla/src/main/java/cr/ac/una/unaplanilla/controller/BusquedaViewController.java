/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.unaplanilla.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.unaplanilla.model.EmpleadoDto;
import cr.ac.una.unaplanilla.model.TipoPlanillaDto;
import cr.ac.una.unaplanilla.service.EmpleadoService;
import cr.ac.una.unaplanilla.service.TipoPlanillaService;
import cr.ac.una.unaplanilla.util.Formato;
import cr.ac.una.unaplanilla.util.Mensaje;
import cr.ac.una.unaplanilla.util.Respuesta;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ANTHONY
 */
public class BusquedaViewController extends Controller implements Initializable {

    @FXML
    private VBox vbxParametros;
    @FXML
    private JFXButton btnFiltrar;
    @FXML
    private Label lblTitulo;
    @FXML
    private TableView tbvResultados;
    @FXML
    private JFXButton btnAceptar;

    private EventHandler<KeyEvent> keyEnter;
    private ObservableList<EmpleadoDto> empleados = FXCollections.observableArrayList();
    private ObservableList<TipoPlanillaDto> tipoPlanillas = FXCollections.observableArrayList();
    Object resultado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onMousePressenTbvResultados(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            onActionBtnAceptar(null);
        }
    }

    @FXML
    private void onActionBtnAceptar(ActionEvent event) {
        resultado = tbvResultados.getSelectionModel().getSelectedItem();
        this.getStage().close();
    }

    public Object getResultado() {
        return resultado;
    }

    public void busquedaEmpleados() {
        try {
            lblTitulo.setText("Busqueda Empleados");

            JFXTextField txtCedula = new JFXTextField();
            txtCedula.setLabelFloat(true);
            txtCedula.setPromptText("Cedula");
            txtCedula.setOnKeyPressed(keyEnter);
            txtCedula.setTextFormatter(Formato.getInstance().cedulaFormat(40));

            JFXTextField txtNombre = new JFXTextField();
            txtNombre.setLabelFloat(true);
            txtNombre.setPromptText("Nombre");
            txtNombre.setTextFormatter(Formato.getInstance().letrasFormat(30));
            txtNombre.setOnKeyPressed(keyEnter);

            JFXTextField txtPApellido = new JFXTextField();
            txtPApellido.setLabelFloat(true);
            txtPApellido.setPromptText("Primer Apellido");
            txtPApellido.setTextFormatter(Formato.getInstance().letrasFormat(15));

            JFXTextField txtSApellido = new JFXTextField();
            txtSApellido.setLabelFloat(true);
            txtSApellido.setPromptText("Segundo Apellido");
            txtSApellido.setTextFormatter(Formato.getInstance().letrasFormat(15));

            vbxParametros.getChildren().clear();
            vbxParametros.getChildren().add(txtCedula);
            vbxParametros.getChildren().add(txtNombre);
            vbxParametros.getChildren().add(txtPApellido);
            vbxParametros.getChildren().add(txtSApellido);

            tbvResultados.getColumns().clear();
            tbvResultados.getItems().clear();

            TableColumn<EmpleadoDto, String> tbcId = new TableColumn<>("Id");
            tbcId.setPrefWidth(30);
            tbcId.setCellValueFactory(cd -> cd.getValue().id);

            TableColumn<EmpleadoDto, String> tbcCedula = new TableColumn<>("Cedula");
            tbcCedula.setPrefWidth(50);
            tbcCedula.setCellValueFactory(cd -> cd.getValue().cedula);

            TableColumn<EmpleadoDto, String> tbcNombre = new TableColumn<>("Nombre");
            tbcNombre.setPrefWidth(100);
            tbcNombre.setCellValueFactory(cd -> cd.getValue().nombre);

            TableColumn<EmpleadoDto, String> tbcPApellido = new TableColumn<>("Primer Apellido");
            tbcPApellido.setPrefWidth(100);
            tbcPApellido.setCellValueFactory(cd -> cd.getValue().primerApellido);

            TableColumn<EmpleadoDto, String> tbcSApellido = new TableColumn<>("Segundo Apellido");
            tbcSApellido.setPrefWidth(130);
            tbcSApellido.setCellValueFactory(cd -> cd.getValue().segundoApellido);

            tbvResultados.getColumns().add(tbcId);
            tbvResultados.getColumns().add(tbcCedula);
            tbvResultados.getColumns().add(tbcNombre);
            tbvResultados.getColumns().add(tbcPApellido);
            tbvResultados.getColumns().add(tbcSApellido);
            tbvResultados.refresh();

            btnFiltrar.setOnAction((ActionEvent event) -> {
                String cedula = txtCedula.getText();

                String nombre = txtNombre.getText();

                String pApellido = txtPApellido.getText();

                String sApellido = txtSApellido.getText();

                cargarEmpleados(cedula, nombre, pApellido, sApellido);
            });
        } catch (Exception ex) {
            Logger.getLogger(BusquedaViewController.class.getName()).log(Level.SEVERE, "Error consultando los empleado", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar empleado", getStage(), "Ocurrio un error consultando los empleados");
        }
    }

    public void busquedaPlanillas() {
        try {
            lblTitulo.setText("Búsqueda de Planillas");

            JFXTextField txtId = new JFXTextField();
            txtId.setLabelFloat(true);
            txtId.setPromptText("Id");
            txtId.setTextFormatter(Formato.getInstance().maxLengthFormat(4));
            txtId.setOnKeyPressed(keyEnter);

            JFXTextField txtCodigo = new JFXTextField();
            txtCodigo.setLabelFloat(true);
            txtCodigo.setPromptText("Codigo");
            txtCodigo.setTextFormatter(Formato.getInstance().maxLengthFormat(4));
            txtCodigo.setOnKeyPressed(keyEnter);

            JFXTextField txtDescripcion = new JFXTextField();
            txtDescripcion.setLabelFloat(true);
            txtDescripcion.setPromptText("Descripcion");
            txtDescripcion.setTextFormatter(Formato.getInstance().letrasFormat(40));
            txtDescripcion.setOnKeyPressed(keyEnter);

            JFXTextField txtPlaxMes = new JFXTextField();
            txtPlaxMes.setLabelFloat(true);
            txtPlaxMes.setPromptText("Planillas por mes");
            txtPlaxMes.setOnKeyPressed(keyEnter);
            txtPlaxMes.setTextFormatter(Formato.getInstance().integerFormat());

            vbxParametros.getChildren().clear();
            vbxParametros.getChildren().add(txtId);
            vbxParametros.getChildren().add(txtCodigo);
            vbxParametros.getChildren().add(txtPlaxMes);
            vbxParametros.getChildren().add(txtDescripcion);

            tbvResultados.getColumns().clear();
            tbvResultados.getItems().clear();

            TableColumn<TipoPlanillaDto, String> tbcId = new TableColumn<>("Id");
            tbcId.setPrefWidth(50);
            tbcId.setCellValueFactory(cd -> cd.getValue().id);

            TableColumn<TipoPlanillaDto, String> tbcCodigo = new TableColumn<>("codigo");
            tbcCodigo.setPrefWidth(100);
            tbcCodigo.setCellValueFactory(cd -> cd.getValue().codigo);

            TableColumn<TipoPlanillaDto, String> tbcDescripcion = new TableColumn<>("Descripcion");
            tbcDescripcion.setPrefWidth(150);
            tbcDescripcion.setCellValueFactory(cd -> cd.getValue().descripcion);

            TableColumn<TipoPlanillaDto, String> tbcPlanxMes = new TableColumn<>("Planilla por mes");
            tbcPlanxMes.setPrefWidth(150);
            tbcPlanxMes.setCellValueFactory(cd -> cd.getValue().planillaPorMes);

            tbvResultados.getColumns().add(tbcId);
            tbvResultados.getColumns().add(tbcCodigo);
            tbvResultados.getColumns().add(tbcDescripcion);
            tbvResultados.getColumns().add(tbcPlanxMes);
            tbvResultados.refresh();

            btnFiltrar.setOnAction((ActionEvent event) -> {
                tbvResultados.getItems().clear();
                String id = txtId.getText();

                String codigo = txtCodigo.getText();

                String descripcion = txtDescripcion.getText();

                String planillasPorMes = txtPlaxMes.getText();

                cargarTipoPlanillas(id, codigo, descripcion, planillasPorMes);
            });
        } catch (Exception ex) {
            Logger.getLogger(BusquedaViewController.class.getName()).log(Level.SEVERE, "Error consultando los planillas.", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar planillas", getStage(), "Ocurrio un error consultado los planillas.");
        }
    }

    private void cargarEmpleados(String cedula, String nombre, String pApellido, String sApellido) {
        EmpleadoService service = new EmpleadoService();
        Respuesta respuesta = service.getEmpleados();

        if (respuesta.getEstado()) {
            ObservableList<EmpleadoDto> empleadoDto = FXCollections.observableList((List<EmpleadoDto>) respuesta.getResultado("Empleados"));
            empleados.clear();

            if (cedula != null && !cedula.isBlank()) {
                String idBuscado = cedula.toLowerCase();
                empleadoDto = empleadoDto.stream()
                        .filter(p -> p.getCedula().toLowerCase().contains(idBuscado))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if (nombre != null && !nombre.isBlank()) {
                String codigoBuscado = nombre.toLowerCase();
                empleadoDto = empleadoDto.stream()
                        .filter(p -> p.getNombre().toLowerCase().contains(codigoBuscado))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if (pApellido != null && !pApellido.isBlank()) {
                String descripcionBuscada = pApellido.toLowerCase();
                empleadoDto = empleadoDto.stream()
                        .filter(p -> p.getPrimerApellido().toLowerCase().contains(descripcionBuscada))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if (sApellido != null && !sApellido.isBlank()) {
                String plamxmesBuscado = sApellido.toLowerCase();
                empleadoDto = empleadoDto.stream()
                        .filter(p -> p.getSegundoApellido().toLowerCase().contains(plamxmesBuscado))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            empleados.addAll(empleadoDto);
            tbvResultados.setItems(empleados);
            tbvResultados.refresh();
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar empleados", getStage(), respuesta.getMensaje());
        }
    }

    private void cargarTipoPlanillas(String id, String codigo, String descripcion, String plamxmes) {
        TipoPlanillaService service = new TipoPlanillaService();
        Respuesta respuesta = service.getTipoPlanillas();

        if (respuesta.getEstado()) {
            ObservableList<TipoPlanillaDto> tipoPlanilla = FXCollections.observableList((List<TipoPlanillaDto>) respuesta.getResultado("TipoPlanillas"));
            tipoPlanillas.clear();

            if (id != null && !id.isBlank()) {
                String idBuscado = id.toLowerCase();
                tipoPlanilla = tipoPlanilla.stream()
                        .filter(p -> p.getId().toString().toLowerCase().contains(idBuscado))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if (codigo != null && !codigo.isBlank()) {
                String codigoBuscado = codigo.toLowerCase();
                tipoPlanilla = tipoPlanilla.stream()
                        .filter(p -> p.getCodigo().toLowerCase().contains(codigoBuscado))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if (descripcion != null && !descripcion.isBlank()) {
                String descripcionBuscada = descripcion.toLowerCase();
                tipoPlanilla = tipoPlanilla.stream()
                        .filter(p -> p.getDescripcion().toLowerCase().contains(descripcionBuscada))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if (plamxmes != null && !plamxmes.isBlank()) {
                String plamxmesBuscado = plamxmes.toLowerCase();
                tipoPlanilla = tipoPlanilla.stream()
                        .filter(p -> p.getPlanillaPorMes().toString().toLowerCase().contains(plamxmesBuscado))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            tipoPlanillas.addAll(tipoPlanilla);
            tbvResultados.setItems(tipoPlanillas);
            tbvResultados.refresh();
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar Tipo Planillas", getStage(), respuesta.getMensaje());
        }
    }
}
