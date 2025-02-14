/**
 * Die Klasse CustomList repräsentiert eine Verkette Liste mit einem Zeiger auf eine <code>CustomList</code> und mit <code>Player</code> objekte als Daten.
 * <p>
 * Diese Verkette Liste wird für die <code>Tournament</code> Klasse als hilfe benutzt, speziell für die effiziente einsortieren von Spieler nach
 * ihrer gespielten Zeit.
 * </p>
 * @see Tournament#pairParticipantsNew(int, int) ()
 */
public class CustomList {
    /**
     * Zeigt auf das nächste element in die Liste.
     * Wenn diese null ist dann ist das das Ende der Liste.
     */
    private CustomList pointer;
    /**
     * Die Daten eines Listenelementes.
     */
    private Player data;

    /**
     * Konstruktor die eine Instanz von CustomList erstellt und sowohl der Pointer als auch die Daten Attribut auf null setzt.
     */
    public CustomList(){
        this.pointer = null;
        this.data = null;
    }

    /**
     * Konstruktor die eine Instanz on <code>CustomList</code> erstellt und die Attribute initialisiert.
     * @param pointer Objekt von CustomList auf den dieses Objekt zeigen soll.
     * @param data  Objekt von Player die, die Daten dieses Objekt repräsentiert
     * @see #pointer
     * @see #data
     */
    public CustomList(CustomList pointer, Player data){
        this.pointer = pointer;
        this.data = data;
    }

    /**
     * Liefert der Vorgänger von dem Listenelement zurück, dessen Daten der gleiche Index hat wie der Eingabeparameter.
     * <p>
     * Diese funktion dient als hilfsfunktion für die <code>Tournament</code> Klasse.
     * Es wird als hilfe benutzt um eines Spielers nach die Spielzeit die der hat richtig einsortieren.
     * </p>
     * @param index das index einen Spieler, abhängig von der position in der Teilnehmerarray eines Turniers.
     * @return Vorgänger des gesuchten Listenelementes.
     */
    public CustomList getCustomListWithPlayerIndex(int index){
        CustomList tmp = this;
        while(true){
            if(tmp.getPointer().getData().getIndex() == index){
                break;
            }
            tmp = tmp.getPointer();
        }
        return tmp;
    }

    /**
     * Gibt die gesamte Liste in das Terminal aus.
     */
    public void printList(){
        CustomList tmp = this.getPointer();
        while(tmp != null){
            System.out.println("Player: " + tmp.getData().getIndex() + " Time: " + tmp.getData().getTimePlayed());
            tmp = tmp.getPointer();
        }
    }

    //Getter und Setter
    public Player getData(){
        return this.data;
    }

    public CustomList getPointer(){
        return this.pointer;
    }

    public void setData(Player data){
        this.data = data;
    }

    public void setPointer(CustomList pointer){
        this.pointer = pointer;
    }
}
