package Estructuras;

public class NodoDoble<T>{
    private T dato;
    private NodoDoble<T> ant;
    private NodoDoble<T> sig;

    public NodoDoble(T dato){
        this.dato= dato;
        ant= null;
        sig= null;
    }

    public T getDato() {return dato;}
    public NodoDoble<T> getSig() {
        return sig;
    }
    public NodoDoble<T> getAnt() {return ant;}

    public void setSig(NodoDoble<T> sig) {
        this.sig = sig;
    }
    public void setAnt(NodoDoble<T> ant) {
        this.ant = ant;
    }
    public void setDato(T dato) {this.dato = dato;}
}

