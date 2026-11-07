package DeckOfCards;
/**
 * Write a description of class Mazo here.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
import Estructuras.ListaDobleCircular;
import Estructuras.NodoDoble;

import java.util.ArrayList;
import java.util.Collections;

public class Mazo {
    private ListaDobleCircular<CartaInglesa> cartas = new ListaDobleCircular<>();

    public Mazo() {
        ListaDobleCircular<CartaInglesa> mazo;
        inicializarMazo();
    }

    public void inicializarMazo(){
        cartas= new ListaDobleCircular<>();
        for(Palo p: Palo.values()){
            for(int valor= 2; valor<= 14; valor++){
                CartaInglesa carta= new CartaInglesa(valor, p, p.getColor());
                cartas.insertaFin(carta);
            }
        }
    }

    public ListaDobleCircular<CartaInglesa> getCartas() {
        return cartas;
    }

    public void mezclar(){
        if(cartas.estaVacia()){return;}

        ArrayList<CartaInglesa> aux = new ArrayList<>();
        while(!cartas.estaVacia()){
            NodoDoble<CartaInglesa> nodo= cartas.eliminaInicio();
            if(nodo!=null){
                aux.add(nodo.getDato());
            }
        }
        Collections.shuffle(aux);
        for(CartaInglesa c: aux){
            cartas.insertaFin(c);
        }
    }

    public CartaInglesa repartirCarta(){
        if(cartas.estaVacia()){return null;}
        NodoDoble<CartaInglesa> nodo= cartas.eliminaInicio();
        if(nodo!=null){
            return nodo.getDato();
        }
        return null;
    }

    public boolean estaVacio(){
        return cartas.estaVacia();
    }

    @Override
    public String toString() {
        return cartas.toString();
    }
}
