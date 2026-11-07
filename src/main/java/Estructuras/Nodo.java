package Estructuras;

public class Nodo<T>{

    private T dato;
    private Nodo<T> sig;

    public Nodo(T dato){
        sig= null;
        this.dato= dato;
    }

    public T getDato(){return dato;}

    public Nodo<T> getSig(){return sig;}

    public void setDato(T dato){this.dato = dato;}

    public void setSig(Nodo<T> sig){this.sig= sig;}

    @Override
    public String toString() {
        return "Nodo{" +
                "info=" + dato +
                ", sig=" + sig +
                '}';
    }

}

