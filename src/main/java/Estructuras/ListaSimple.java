package Estructuras;

public class ListaSimple<T>{

    private Nodo<T> inicio;
    public Nodo<T> getInicio(){return inicio;}

    public ListaSimple(){
        inicio=null;
    }

    /*
    conecta el nuevo nodo a la lista, actualiza el inicio
     */
    public void insertaInicio(T dato){
        Nodo<T> n= new Nodo<>(dato);
        n.setSig(inicio); //el nuevo nodo va antes del que iba al inicio
        inicio= n; //referencia al nuevo primer nodo
    }

    /*
    recorre desde el inicio de la lista, cuando ya no haya siguiente se inserta ahí el nodo
     */
    public void insertaFin(T dato){
        Nodo<T> n= new Nodo<>(dato);
        if(inicio==null){ //si no hay nada, agregamos ese nodo como el primer elemento
            inicio=n;
        }else{
            Nodo<T> r= inicio; //empezar desde el inicio
            while(r.getSig()!= null){ //cuando llegue a un nodo que no tenga sig elem, ese es el final
                r= r.getSig(); //avanzar al sig nodo
            }
            r.setSig(n); //agregar al final el nuevo nodo
        }
    }

    /*
    elimina el primer dato de una lista y lo regresa
     */
    public T eliminaInicio(){
        if(inicio==null){
            System.out.println("Lista vacía");
            return null;
        }else{
            //tomar dato, apuntar al segundo nodo y retornar dato eliminado
            //como no hay nada que apunte al primero, se elimina solo
            T dato= inicio.getDato();
            inicio= inicio.getSig(); //ahora el segundo dato es el inicio
            return dato;
        }
    }

    /*
    recorrer hasta llegar al último elemento de la lista y eliminar su referencia
     */
    public T eliminaFin(){
        if(inicio==null){
            System.out.println("Lista vacía");
            return null;
        }

        T dato;
        if(inicio.getSig() == null) { //solo hay un nodo en la lista
            dato = inicio.getDato();
            inicio = null;
            return dato;
        }
        // hay varios nodos en la lista
        Nodo<T> r= inicio; // aux para avanzar al ultimo nodo
        Nodo<T> a= r; //nodo anterior
        while(r.getSig()!=null) {
            a = r; //la pos actual será la anterior en la prox vuelta
            r = r.getSig(); //apuntar al siguiente nodo como actual
        }
        dato= r.getDato(); //almacenar ultimo dato
        a.setSig(null); //eliminar ultimo (desde la penultima posicion)
        return dato;
    }

    public boolean estaVacia(){return inicio==null;}

    public T obtenerUltimo(){
        if(inicio==null){
            return null;
        }
        Nodo<T> r= inicio;
        while(r.getSig()!=null){
            r= r.getSig();
        }
        return r.getDato();
    }

}

