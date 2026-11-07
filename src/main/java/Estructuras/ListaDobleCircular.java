package Estructuras;

public class ListaDobleCircular<T>{
    private NodoDoble<T> inicio;
    private NodoDoble<T> fin;

    public ListaDobleCircular(){

    }

    public boolean estaVacia(){
        return inicio==null;
    }

    public void insertaInicio(T dato){
        NodoDoble<T> n= new NodoDoble<>(dato);
        if(inicio==null){
            inicio= fin= n;
            n.setSig(inicio);
            n.setAnt(fin);
        }else{
            n.setSig(inicio);
            n.setAnt(fin);
            inicio.setAnt(n);
            fin.setSig(n);
            inicio= n;
        }
    }

    public void insertaFin(T dato){
        NodoDoble<T> n= new NodoDoble<>(dato);
        if(inicio==null){
            inicio= fin= n;
            n.setSig(inicio);
            n.setAnt(fin);
        }else{
            n.setSig(inicio);
            n.setAnt(fin);
            fin.setSig(n);
            inicio.setAnt(n);
            fin= n;
        }
    }

    public NodoDoble<T> eliminaInicio(){
        if(inicio==null){
            System.out.println("Lista vacía");
            return null;
        }else{
            NodoDoble<T> n= new NodoDoble<>(inicio.getDato());
            if(inicio==fin){
                inicio= fin= null;
                return n;
            }
            fin.setSig(inicio.getSig());
            inicio= inicio.getSig();
            inicio.setAnt(fin);
            return n;
        }
    }

    public NodoDoble<T> eliminaFin(){
        if(inicio==null){
            System.out.println("Lista vacía");
            return null;
        }else{
            NodoDoble<T> n= new NodoDoble<>(fin.getDato());
            if(inicio==fin){
                inicio= fin= null;
                return n;
            }
            fin= fin.getAnt();
            fin.setSig(inicio);
            inicio.setAnt(fin);
            return n;
        }
    }

    public String mostrarLista(){
        if(inicio==null){
            return "Lista vacía";
        }
        String lista= "";
        NodoDoble<T> n= inicio;
        do{
            lista+= n.getDato()+ ", ";
            n= n.getSig();
        }while(n!=inicio);
        return lista;
    }

    public String mostrarListaRecursiva(){
        if(inicio==null){
            return ".";
        }
        return mostrarListaRecursiva(inicio);
    }

    private String mostrarListaRecursiva(NodoDoble<T> n){
        if(n==fin){
            return n.getDato()+ ".";
        }else{
            return n.getDato()+ ", "+ mostrarListaRecursiva(n.getSig());
        }
    }

    public NodoDoble<T> getInicio(){return inicio;}
    public void setInicio(NodoDoble<T> dato){inicio= dato;}
    public NodoDoble<T> getFin(){return fin;}
    public void setFin(NodoDoble<T> dato){fin= dato;}
}
