import DeckOfCards.CartaInglesa;

public class Undo{
    private String origen; // tablero o celdas libres
    private int idxO; //columna de la carta
    private String destino; // tablero o celdas a donde llega la carta
    private int idxD; //guarda posicion en la columna
    private CartaInglesa carta; //carta movida

    public Undo(String origenTipo, int origenIdx, String destinoTipo, int destinoIdx, CartaInglesa c){
        this.origen= origenTipo;
        this.idxO = origenIdx;
        this.destino= destinoTipo;
        this.idxD = destinoIdx;
        this.carta= c;
    }

    public String getOrigenTipo(){return origen;}
    public int getIdxO(){return idxO;}
    public String getDestinoTipo(){return destino;}
    public int getIdxD(){return idxD;}
    public CartaInglesa getCarta(){return carta;}
}


