import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import DeckOfCards.CartaInglesa;
import Estructuras.Nodo;

public class EightOffGUI extends Application {

    private EightOffJuego juego;

    private ComboBox<String> cbOrigenZona;
    private ComboBox<Integer> cbOrigenIndice;
    private ComboBox<String> cbDestinoZona;
    private ComboBox<Integer> cbDestinoIndice;

    private Button btnMover;
    private Button btnUndo;
    private Button btnPista;
    private Label lblMensaje;

    private GridPane gridBases;
    private GridPane gridReserva;
    private GridPane gridTablero;

    @Override
    public void start(Stage primaryStage) {
        juego = new EightOffJuego();
        juego.iniciarJuego();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1b4d2e;");

        // panel izq
        VBox menuIzquierdo = crearMenuIzquierdo();
        root.setLeft(menuIzquierdo);

        // panel centro
        VBox areaJuego = crearAreaJuego();
        root.setCenter(areaJuego);

        actualizarInterfaz();

        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setTitle("Juego Eight Off");
        primaryStage.setMaximized(true);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox crearMenuIzquierdo() {
        VBox menu = new VBox(14);
        menu.setPadding(new Insets(14));
        menu.setPrefWidth(280);
        menu.setStyle("-fx-background-color: #143d24; -fx-background-radius: 8;");

        Label lblTituloBases = new Label("BASES");
        lblTituloBases.setTextFill(Color.WHITE);
        lblTituloBases.setFont(Font.font("System", FontWeight.BOLD, 14));

        // gridpane bases
        gridBases = new GridPane();
        gridBases.setHgap(10);
        gridBases.setVgap(10);

        // COMBOBOX
        Label lblOrigen = new Label("Origen:");
        lblOrigen.setTextFill(Color.WHITE);
        cbOrigenZona = new ComboBox<>();
        cbOrigenZona.getItems().addAll("Tablero", "Reserva");
        cbOrigenZona.setValue("Tablero");
        cbOrigenIndice = new ComboBox<>();
        actualizarOpcionesIndice(cbOrigenZona.getValue(), cbOrigenIndice);
        cbOrigenZona.setOnAction(e -> actualizarOpcionesIndice(cbOrigenZona.getValue(), cbOrigenIndice));

        HBox boxOrigen = new HBox(8, cbOrigenZona, cbOrigenIndice);

        Label lblDestino = new Label("Destino:");
        lblDestino.setTextFill(Color.WHITE);
        cbDestinoZona = new ComboBox<>();
        cbDestinoZona.getItems().addAll("Tablero", "Reserva", "Bases");
        cbDestinoZona.setValue("Tablero");
        cbDestinoIndice = new ComboBox<>();
        actualizarOpcionesIndice(cbDestinoZona.getValue(), cbDestinoIndice);
        cbDestinoZona.setOnAction(e -> actualizarOpcionesIndice(cbDestinoZona.getValue(), cbDestinoIndice));

        HBox boxDestino = new HBox(8, cbDestinoZona, cbDestinoIndice);

        // botones de accion
        btnMover = new Button("Realizar Movimiento");
        btnMover.setMaxWidth(Double.MAX_VALUE);
        btnMover.setOnAction(e -> ejecutarMovimiento());

        btnUndo = new Button("Deshacer Movimiento");
        btnUndo.setMaxWidth(Double.MAX_VALUE);
        btnUndo.setOnAction(e -> {
            if (juego.deshacer()) {
                lblMensaje.setText("Movimiento deshecho.");
                actualizarInterfaz();
            } else {
                lblMensaje.setText("No hay movimientos registrados para deshacer.");
            }
        });

        btnPista = new Button("Pista");
        btnPista.setMaxWidth(Double.MAX_VALUE);
        btnPista.setOnAction(e -> lblMensaje.setText(juego.obtenerPista()));

        // mensaje panel
        lblMensaje = new Label("");
        lblMensaje.setTextFill(Color.YELLOW);
        lblMensaje.setWrapText(true);

        menu.getChildren().addAll(lblTituloBases, gridBases, lblOrigen, boxOrigen, lblDestino, boxDestino,
                btnMover, btnUndo, btnPista, lblMensaje
        );
        return menu;
    }

    private void actualizarOpcionesIndice(String zona, ComboBox<Integer> cbIndice) {
        cbIndice.getItems().clear();
        int limite = zona.equalsIgnoreCase("Bases") ? 4 : 8;
        for (int i = 1; i <= limite; i++) {
            cbIndice.getItems().add(i);
        }
        cbIndice.setValue(1);
    }

    private VBox crearAreaJuego() {
        VBox area = new VBox(10); // espacion entre reserva y tablero
        area.setPadding(new Insets(5, 0, 0, 15));

        gridReserva = new GridPane();
        gridReserva.setHgap(50); // espacio horizontal reserva

        gridTablero = new GridPane();
        gridTablero.setHgap(50); // espacio horizontal columnas
        gridTablero.setVgap(4);  // espacio vertical entre cartas

        area.getChildren().addAll(gridReserva, gridTablero);
        return area;
    }

    private void actualizarInterfaz() {
        // limpiar contenedores visuales
        gridBases.getChildren().clear();
        gridReserva.getChildren().clear();
        gridTablero.getChildren().clear();

        // dibujar bases
        for (int i = 0; i < 4; i++) {
            CartaInglesa carta = juego.getBases(i).estaVacia() ? null : juego.getBases(i).obtenerUltimo();
            Label lblBase = crearLabelCarta(carta);
            gridBases.add(lblBase, i % 2, i / 2);
        }

        // dibujar reserva
        for (int i = 0; i < 8; i++) {
            CartaInglesa carta = juego.getReserva(i).estaVacia() ? null : juego.getReserva(i).obtenerUltimo();
            Label lblReservaCard = crearLabelCarta(carta);
            gridReserva.add(lblReservaCard, i, 0);
        }

        // dibujar tablero
        for (int col = 0; col < 8; col++) {
            // Etiqueta indicadora de columna
            Label lblCol = new Label("Col " + (col + 1));
            lblCol.setTextFill(Color.WHITE);
            lblCol.setFont(Font.font("System", FontWeight.BOLD, 12));
            gridTablero.add(lblCol, col, 0);

            if (juego.getTablero(col).estaVacia()) {
                Label lblVacio = crearLabelCarta(null);
                gridTablero.add(lblVacio, col, 1);
            } else {
                Nodo<CartaInglesa> actual = juego.getTablero(col).getInicio();
                int fila = 1; // Las cartas comienzan a apilarse desde la fila 1
                while (actual != null) {
                    Label lblCarta = crearLabelCarta(actual.getDato());
                    gridTablero.add(lblCarta, col, fila);
                    actual = actual.getSig();
                    fila++;
                }
            }
        }
    }

    private Label crearLabelCarta(CartaInglesa carta) {
        Label label = new Label();
        label.setPrefSize(62, 88);
        label.setMinSize(62, 88);
        label.setMaxSize(62, 88);
        label.setAlignment(Pos.CENTER);

        if (carta == null) {
            label.setText("");
            label.setStyle("-fx-background-color: rgba(255, 255, 255, 0.08); " + "-fx-border-color: rgba(255, 255, 255, 0.35); " +
                    "-fx-border-radius: 5; " + "-fx-background-radius: 5;"
            );
        } else {
            carta.makeFaceUp();
            label.setText(carta.toString());

            boolean esRoja = carta.getColor().equalsIgnoreCase("rojo");
            String colorTexto = esRoja ? "#d63031" : "#2d3436";

            label.setStyle("-fx-background-color: white; " + "-fx-border-color: #2d3436; " + "-fx-border-width: 1.5; " +
                    "-fx-border-radius: 5; " + "-fx-background-radius: 5; " + "-fx-font-weight: bold; " + "-fx-font-size: 18px;"
            );
            label.setTextFill(Color.web(colorTexto));
        }
        return label;
    }

    private void ejecutarMovimiento() {
        String origenZ = cbOrigenZona.getValue();
        int origenIdx = cbOrigenIndice.getValue() - 1;
        String destinoZ = cbDestinoZona.getValue();
        int destinoIdx = cbDestinoIndice.getValue() - 1;

        boolean exito = false;

        if (origenZ.equals("Tablero") && destinoZ.equals("Tablero")) {
            exito = juego.movimientoCartasTablero(origenIdx, destinoIdx);
        } else if (origenZ.equals("Tablero") && destinoZ.equals("Reserva")) {
            exito = juego.moverTableroAReserva(origenIdx, destinoIdx);
        } else if (origenZ.equals("Reserva") && destinoZ.equals("Tablero")) {
            exito = juego.moverReservaATablero(origenIdx, destinoIdx);
        } else if (destinoZ.equals("Bases")) {
            exito = juego.moverABase(origenZ, origenIdx, destinoIdx);
        }

        if (exito) {
            lblMensaje.setText("Movimiento realizado.");
            actualizarInterfaz();
        } else {
            lblMensaje.setText("Movimiento invalido.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}