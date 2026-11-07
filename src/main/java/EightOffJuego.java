import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import Estructuras.ListaSimple;
import Estructuras.Pila;

public class EightOffJuego{
    private ListaSimple<CartaInglesa>[] tablero; //arreglo de 8 columnas
    private ListaSimple<CartaInglesa>[] reserva;
    private ListaSimple<CartaInglesa>[] bases;
    private Mazo mazo;
    private Pila<Undo> pilaUndo;

    public EightOffJuego(){
        tablero= (ListaSimple<CartaInglesa>[]) new ListaSimple[8];
        reserva= (ListaSimple<CartaInglesa>[]) new ListaSimple[8];
        bases= (ListaSimple<CartaInglesa>[]) new ListaSimple[4];
        mazo= new Mazo();
        pilaUndo= new Pila<>();
        iniciarJuego();
    }

    public void iniciarJuego(){
        //inicializar 8 columnas de tablero y 8 espacios libres
        for(int i= 0; i< 8; i++){
            tablero[i]= new ListaSimple<>();
            reserva[i]= new ListaSimple<>();
        }
        // inicializar bases
        for(int i= 0; i< 4; i++){
            bases[i]= new ListaSimple<>();
        }
        mazo.inicializarMazo();
        mazo.mezclar();

        // se reparten 6 cartas para cada columna del tablero
        for(int c= 0; c< 6; c++){
            for(int i= 0; i< 8; i++){
                CartaInglesa carta= mazo.repartirCarta();
                if(carta!=null){
                    tablero[i].insertaFin(carta);
                }
            }
        }
        // 4 cartas sobrantes a reserva
        for(int i= 0; i< 4; i++){
            CartaInglesa carta= mazo.repartirCarta();
            if(carta!=null){
                reserva[i].insertaFin(carta);
            }
        }
    }

    /*
    MOVIMIENTOS DE CARTAS
     */

    public boolean movimientoCartasTablero(int origen, int destino){
        // rangos validos y que no se quiera mover a la misma columna
        if(origen < 0 || origen >= 8 || destino < 0 || destino >= 8 || origen == destino){
            return false;
        }
        // no hay carta en esa columna
        if(tablero[origen].estaVacia()){
            return false;
        }

        // en columna vacía solo se acepta Rey
        CartaInglesa cartaOrigen= tablero[origen].obtenerUltimo();
        if(tablero[destino].estaVacia()){
            if(cartaOrigen.getValor()==13){
                CartaInglesa c= tablero[origen].eliminaFin();
                tablero[destino].insertaFin(c);
                pilaUndo.push(new Undo("tablero", origen, "tablero", destino, c));
                return true;
            }
            return false;
        }

        // verificar que sean del mismo palo y que vaya de forma descendente
        CartaInglesa cartaDestino= tablero[destino].obtenerUltimo();
        if(cartaOrigen.getPalo()==cartaDestino.getPalo() && cartaOrigen.getValor()==cartaDestino.getValor()-1){
            CartaInglesa c= tablero[origen].eliminaFin();
            tablero[destino].insertaFin(c);
            pilaUndo.push(new Undo("tablero", origen, "tablero", destino, c));
            return true;
        }
        return false;
    }

    public boolean moverTableroAReserva(int origen, int destino){
        if(origen < 0 || origen >= 8 || destino < 0 || destino >= 8){
            return false;
        }
        if(tablero[origen].estaVacia() || !reserva[destino].estaVacia()){
            return false;
        }

        CartaInglesa c= tablero[origen].eliminaFin();
        reserva[destino].insertaFin(c);
        pilaUndo.push(new Undo("tablero", origen, "reserva", destino, c));
        return true;
    }

    public boolean moverReservaATablero(int origen, int destino){
        if(origen < 0 || origen >= 8 || destino < 0 || destino >= 8){
            return false;
        }
        if(reserva[origen].estaVacia()){
            return false;
        }

        CartaInglesa cartaOrigen= reserva[origen].obtenerUltimo();

        // si la columna esta vacia solo puede ir un Rey
        if(tablero[destino].estaVacia()){
            if(cartaOrigen.getValor()==13){
                CartaInglesa c= reserva[origen].eliminaFin();
                tablero[destino].insertaFin(c);
                pilaUndo.push(new Undo("reserva", origen, "tablero", destino, c));
                return true;
            }
            return false;
        }

        // mover carta de reserva a tablero
        CartaInglesa cartaDestino= tablero[destino].obtenerUltimo();
        if(cartaOrigen.getPalo()==cartaDestino.getPalo() && cartaOrigen.getValor()==cartaDestino.getValor()-1){
            CartaInglesa c= reserva[origen].eliminaFin();
            tablero[destino].insertaFin(c);
            pilaUndo.push(new Undo("reserva", origen, "tablero", destino, c));
            return true;
        }
        return false;
    }

    public boolean moverABase(String origenTipo, int origenIndice, int baseIndice){
        if(baseIndice<0 || baseIndice>=4){
            return false;
        }
        if(origenIndice<0 || origenIndice>=8){
            return false;
        }
        CartaInglesa cartaOrigen= null;
        // obtener si viene del tablero o base
        if(origenTipo.equalsIgnoreCase("tablero") && !tablero[origenIndice].estaVacia()){
            cartaOrigen= tablero[origenIndice].obtenerUltimo();
        }else if(origenTipo.equalsIgnoreCase("reserva") && !reserva[origenIndice].estaVacia()){
            cartaOrigen= reserva[origenIndice].obtenerUltimo();
        }

        if(cartaOrigen==null){
            return false;
        }

        // base vacia, para iniciar ocupa un As
        if(bases[baseIndice].estaVacia()){
            if(cartaOrigen.getValor()==14){
                guardarBase(origenTipo, origenIndice, baseIndice);
                return true;
            }
        }else{ // ya hay cartas en la base
            CartaInglesa cartaBase= bases[baseIndice].obtenerUltimo();
            // ascendente y mismo palo
            if(cartaOrigen.getPalo() == cartaBase.getPalo() && cartaBase.esLaSiguiente(cartaOrigen)){
                guardarBase(origenTipo, origenIndice, baseIndice);
                return true;
            }
        }
        return false;
    }

    private void guardarBase(String origenTipo, int origenIndice, int baseIndice){
        CartaInglesa c= null;
        if(origenTipo.equalsIgnoreCase("tablero")){
            c= tablero[origenIndice].eliminaFin();
        }else{
            c= reserva[origenIndice].eliminaFin();
        }
        bases[baseIndice].insertaFin(c);
        pilaUndo.push(new Undo(origenTipo, origenIndice, "bases", baseIndice, c));
    }

    /*
    DESHACER MOVIMIENTOS
     */

    public boolean deshacer(){
        if(pilaUndo.pilaVacia()){
            return false;
        }

        Undo movimiento= pilaUndo.pop();
        CartaInglesa carta= movimiento.getCarta();

        // quitar carta de su destino
        if(movimiento.getDestinoTipo().equalsIgnoreCase("tablero")){
            tablero[movimiento.getIdxD()].eliminaFin();
        }else if(movimiento.getDestinoTipo().equalsIgnoreCase("reserva")){
            reserva[movimiento.getIdxD()].eliminaFin();
        }else if(movimiento.getDestinoTipo().equalsIgnoreCase("bases")){
            bases[movimiento.getIdxD()].eliminaFin();
        }

        // regresar carta al origen
        if(movimiento.getOrigenTipo().equalsIgnoreCase("tablero")){
            tablero[movimiento.getIdxO()].insertaFin(carta);
        }else if(movimiento.getOrigenTipo().equalsIgnoreCase("reserva")){
            reserva[movimiento.getIdxO()].insertaFin(carta);
        }

        return true;
    }

    /*
    PISTAS
     */

    public String obtenerPista(){
        // evalua si alguna carta puede ir a la base
        for(int i= 0; i< 8; i++){
            if(!tablero[i].estaVacia()){ //que hallan cartas en la columna
                CartaInglesa c= tablero[i].obtenerUltimo();
                for(int b= 0; b< 4; b++){ //solo entran As
                    if(bases[b].estaVacia() && c.getValor()==14){ //base no iniciada, requiere As
                        return "Mover As de la columna "+(i+1)+" a la base "+(b+1);
                    // base iniciada, verificar si es del mismo palo y es un valor mayor (cumplir ascendente)
                    }else if(!bases[b].estaVacia() && bases[b].obtenerUltimo().getPalo()==c.getPalo() && c.getValor()==bases[b].obtenerUltimo().getValor()+1){
                        return "Mueve el "+c+" de la Columna "+(i+1)+" a la Base "+(b+1);
                    }
                }
            }
        }

        // evaluar movimientos de reserva a tablero
        for (int r = 0; r < 8; r++) {
            // si la casilla de reserva no está vacía extraemos su carta
            if (!reserva[r].estaVacia()) {
                CartaInglesa cr = reserva[r].obtenerUltimo();
                // comparar con las 8 columnas de tablero
                for (int t = 0; t < 8; t++) {
                    if (tablero[t].estaVacia()) {
                        // columna vacía solo acepta valor 13 (Rey)
                        if (cr.getValor() == 13) {
                            return "Mueve el " + cr + " de la reserva a la columna " + (t + 1);
                        }
                    } else {
                        CartaInglesa cd = tablero[t].obtenerUltimo();
                        // mismo palo y valor consecutivo descendente
                        if (cr.getPalo() == cd.getPalo() && cr.getValor() == cd.getValor() - 1) {
                            return "Mueve el " + cr + " de la reserva a la columna " + (t + 1);
                        }
                    }
                }
            }
        }

        // evaluar si hay movimientos entre columnas
        for(int i= 0; i< 8; i++){
            if(!tablero[i].estaVacia()){ //columna no vacia
                CartaInglesa co= tablero[i].obtenerUltimo(); //obtener carta visible
                for(int j= 0; j< 8; j++){ // compara carta de origen con las demas del tablero
                    if(i!=j && !tablero[j].estaVacia()){ // no mover carta a la misma columna y que no este vacia
                        CartaInglesa cd= tablero[j].obtenerUltimo(); // obtener carta ultima de la columna
                        if(co.getPalo()==cd.getPalo() && co.getValor()==cd.getValor()-1){
                            return "Mueve a "+co+" de la columna "+(i+1)+" a la columna "+(j+1);
                        }
                    }
                }
            }
        }

        return "No hay movimientos disponibles";
    }

    public boolean hayMovimientosDisponibles(){
        return !obtenerPista().equals("No hay movimientos disponibles");
    }

    public boolean haGanado(){
        int totalCartasBases= 0;
        for(int i= 0; i< 4; i++){
            CartaInglesa c= bases[i].obtenerUltimo();
            if(c!=null && c.getValor()==13){ // llega al Rey
                totalCartasBases+= 13;
            }
        }
        if(totalCartasBases==52){
            return true;
        }else{
            return false;
        }
    }

    public ListaSimple<CartaInglesa> getTablero(int indice) { return tablero[indice]; }
    public ListaSimple<CartaInglesa> getReserva(int indice) { return reserva[indice]; }
    public ListaSimple<CartaInglesa> getBases(int indice) { return bases[indice]; }

    public ListaSimple<CartaInglesa> getTableau(int indice) { return tablero[indice]; }
    public ListaSimple<CartaInglesa> getCeldasLibres(int indice) { return reserva[indice]; }

    public ListaSimple<CartaInglesa>[] getTableau() { return tablero; }
    public ListaSimple<CartaInglesa>[] getCeldasLibres() { return reserva; }
    public ListaSimple<CartaInglesa>[] getBases() { return bases; }
    public Pila<Undo> getPilaUndo() { return pilaUndo; }

}

