import java.util.*;

/**
 * Repräsentiert ein Turnier mit einer bestimmten Anzahl von Teilnehmer, eine bestimmte Turnierdauer, und weitere Hilfsvariable um ein Turnier simulieren zu können.
 * <p>
 * Um ein Turnier simulieren zu können, muss nach dem ein Objekt von diese Klasse erstellt worden ist, die {@link #fillAndSortParticipantNormalDistribution(double, double, Player)}
 * zuerst ausgeführt werden. Danach kann {@link #startTournament(int, int)} aufgerufen werden um das gesamte Turnier simulieren zu können.
 */
public class Tournament {

    /**
     * Anfang der verketteten Liste, die einen Zeiger auf die Erste element hat, aber keine daten hat.
     * <p>
     * Hilfsobjekt um effizient Spieler nach ihrer Spielzeit in eine Liste einsortieren zu können
     * @see #pairParticipants(int, int)
     */
    private CustomList startList;
    /**
     * Ende der verketteten Liste, die einen Zeiger auf das Letzte element hat, aber keine daten hat.
     * <p>
     * Hilfsobjekt um effizient Spieler nach ihrer Spielzeit in eine Liste einsortieren zu können
     * @see #pairParticipants(int, int)
     */
    private CustomList endList;

    /**
     * Enthält das aktuelle Listenelement.
     * <p>
     * Hilfsvariable für die Gegnersuche.
     * @see #pairParticipants(int, int)
     */
    private CustomList indexIdentifier;

    /**
     * Turnierdauer in Sekunden.
     * <p>
     * Wird verwendet, um nachzuprüfen, ob irgendein Spieler diese Zeit schon überschritten hat.
     * @see #pairParticipants(int, int)
     * @see #pairTwoPlayers(Player)
     * @see Game#playGame(Player[], int, int, int, int)
     */
    private int gameTime;

    /**
     * Teilnehmer des Turniers.
     * <p>
     * Wird nach die Bewertung des Spielers sortiert.
     * Wird verwendet für die Gegnersuche und Spielsimulation.
     * Alle Spieler werden auch durch diesen Array verwaltet.
     * @see #pairParticipants(int, int)
     * @see #pairParticipantsFirstTime()
     * @see #pairTwoPlayers(Player)
     * @see #sortInCustomListOne(Player, CustomList)
     * @see #sortInCustomListOneOpponent(Player)
     * @see #resetTournament()
     * @see #resetPlayerAttributes()
     */
    private Player[] participants;

    /**
     * Teilnehmer des Turniers nach deren Zeit sortiert.
     * <p>
     * Wird als hilfsarray verwendet um das <code>startList</code> und <code>endList</code> zu initialisieren nach der erste Runde.
     * @see #pairParticipantsFirstTime()
     * @see #initializeCustomList()
     */
    private Player[] participantsSortedWithTime;

    /**
     * Repräsentiert ein Spiel.
     * <p>
     * Wird benutzt nach die Gegnersuche ein Spieler, um ein Spiel zwischen zwei Spieler auszuführen können.
     * @see #startTournament(int, int)
     */
    public Game game;

    /**
     * Array von dem Rating der Teilnehmer.
     * <p>
     * Diese ist eine Hilfsvariable für die effiziente bestimmung von der Ober- und Untergrenze der potenzielle Gegner eines Spielers.
     * @see #getUpperIndex(int[], Player)
     * @see #getLowerIndex(int[], Player)
     */
    public int[] participantsRating;

    /**
     * Anzahl der Spieler dessen Spielzeit mindestens so viel ist wie der Turnierdauer.
     * <p>
     * Dient als hilfsvariable um zu überprüfen, ob jede Spieler schon mindestens so viel Spielzeit hat
     * wie der Turnierdauer und somit das Turnier beendet werden kann.
     * @see #startTournament(int, int)
     */
    private int globalCounter;
    private int counter;

    /**
     * Konstruktor die eine <code>Tournament</code> instanz erzeugt.
     * <p>
     * <code>gameTime</code> wird initialisiert.
     * Es wird eine neue <code>Game</code> instanz erstellt durch {@link Game#Game(int, int)}.
     * Die <code>globalCounter</code> wird mit 0 initialisiert und für <code>startList</code> und <code>endList</code> wird eine neue <code>CustomList</code> Instanz mit {@link CustomList#CustomList()} erstellt.
     * Außerdem wird <code>participantsRating</code>, <code>participantsSortedWithTime</code> und <code>participants</code> einen Array mit einem größe von <code>participantsSize</code> initialisiert.
     * @param gameTime  Dauer des Turniers in Sekunden
     * @param participantsSize  Anzahl der Teilnehmer des Turniers
     * @param roundTimeMean Erwartungswert für die Normalverteilung bei der Rundenzeit generierung
     * @param roundTimeSd   Standardabweichung für die Normalverteilung bei der Rundenzeit generierung
     * @see #gameTime
     * @see #participants
     * @see #game
     * @see #participantsRating
     * @see #participantsSortedWithTime
     * @see #globalCounter
     * @see #startList
     * @see #endList
     */
    public Tournament(int gameTime, int participantsSize, int roundTimeMean, int roundTimeSd){
        this.gameTime = gameTime;
        this.participants = new Player[participantsSize];
        game = new Game(roundTimeMean,roundTimeSd);
        //list = new ArrayList<>();
        participantsRating = new int[participantsSize];
        this.participantsSortedWithTime = new Player[participantsSize];
        globalCounter = 0;
        startList = new CustomList();
        endList = new CustomList();

    }

    /**
     * Füllt {@link #participants} mit <code>Player</code> instanzen, die eine zufällige normalverteilte rating bekommen, und sortiert diese nach dem Rating der Spieler.
     * <p>
     * Bevor das Array sortiert wird, wird noch der eigene Spieler hinzugefügt.
     * Außerdem werden, nachdem das <code>participants</code> Array aufgefüllt und sortiert wurde, die {@link Player#getIndex()} Attribut jede <code>Player</code> instanz mit
     * der Position des Spielers in die <code>participants</code> Array überschrieben.
     * Letztendlich wird {@link #participantsSortedWithTime} mit dem werte von dem <code>participants</code> Array gefüllt und {@link #participantsRating} mit dem Rating der Spieler
     * von dem <code>participants</code> array gefüllt.
     * @param mean  Erwartungswert für die Normalverteilung dem Rating der Spieler
     * @param sd    Standardabweichung für die Normalverteilung dem Rating der Spieler
     * @param myPlayer  Eigene Spieler die auch in das Turnier teilnimmt
     */
    public void fillAndSortParticipantNormalDistribution(double mean, double sd, Player myPlayer){
        Random random = new Random();
        for(int i = 0; i < this.participants.length-1; i++){
            this.participants[i] = new Player("Player"+i,(int)Math.round(random.nextGaussian(mean,sd)));
        }
        this.participants[participants.length-1] = myPlayer;
        Arrays.sort(participants,Comparator.comparingInt(Player::getRating));
        for(int i = 0; i < this.participants.length; i++){
            this.participants[i].setIndex(i);
            participantsRating[i] = this.participants[i].getRating();
            this.participantsSortedWithTime[i] = this.participants[i];
        }
    }

    /**
     * Gibt jede Teilnehmer in das Terminal aus.
     * <p>
     * Es wird die {@link Player#getId()} und {@link Player#getRating()} ausgegeben.
     */
    public void printParticipants(){
        for(int i = 0; i < participants.length; i++){
            System.out.println(participants[i].getId() + ": " + "Rating: " + participants[i].getRating());
        }
    }

    /**
     * Startet das Turnier und beendet dies, wenn jede Spieler eine Spielzeit >= {@link #gameTime} hat.
     * <p>
     * Muss davor {@link #fillAndSortParticipantNormalDistribution(double, double, Player)} aufgerufen werden.
     * Zuerst wird versucht, da diese der Anfang des Turniers ist und somit jede Spieler eine Spielzeit von 0 Sekunden haben,
     * jede Spieler, abhängig von dem Rating, einen Gegner zu finden. Wenn jedes Paar gespielt hat, wird {@link #participantsSortedWithTime}
     * nach der Spielzeit des Spielers sortiert und mithilfe dessen {@link #startList} und {@link #endList} initialisiert. Nachdem wird immer nur ein Spieler ein Gegner suchen, und zwar der Spieler der am wenigsten
     * spielzeit hat, um das Turnier so realitätsnah wie möglich zu realisieren.
     * @param multiplier eine zahl die mit 25 multipliziert wird und somit die Grenze für das Verlassen der Spiele für die Spieler berechnet
     * @param playerIndex   index der Spieler der als einzige die Spiele verlässt
     * @see #pairParticipantsFirstTime()
     * @see #pairParticipants(int, int)
     * @see #initializeCustomList()
     * @see Game#startGameFirstTime(Player[], int, int, int)
     * @see Game#startGame(Player[], int, int, int, int)
     */
    public void startTournament(int multiplier, int playerIndex){
        boolean isTournamentEnd = false;
        pairParticipantsFirstTime();
        /*for (int i = 0; i < participants.length; i++) {
            System.out.print("first: " + participants[i].getIndex() + " IsInGame: " + participants[i].getIsInGame());
            System.out.println(" second: " + participants[i].getOpponentIndex() + " IsInGame: " + participants[i].getIsInGame());
        }*/
        game.startGameFirstTime(participants,gameTime,multiplier,playerIndex);
        Arrays.sort(participantsSortedWithTime, Comparator.comparingInt(Player::getTimePlayed));
        initializeCustomList();
        //startList.printList();
        while(!isTournamentEnd){
            pairParticipants(multiplier,playerIndex);
            if(globalCounter == participants.length){
                isTournamentEnd = true;
            }
        }
    }

    /**
     * Initialisiert {@link #startList} und {@link #endList} mithilfe von {@link #participantsSortedWithTime}.
     * <p>
     * Wird innerhalb von {@link #startTournament(int, int)} aufgerufen, nachdem die Paare, die am Anfang gebildet wurde, alle gespielt haben.
     * {@link #indexIdentifier} wird auch initialisiert, und zwar mit dem ersten Element der Liste.
     * @see #pairParticipantsFirstTime()
     * @see Game#startGameFirstTime(Player[], int, int, int)
     */
    public void initializeCustomList(){
        CustomList tmp = startList;
        for(int i = 0; i < participantsSortedWithTime.length; i++){
            tmp.setPointer(new CustomList(null,participantsSortedWithTime[i]));
            tmp = tmp.getPointer();
        }
        endList.setPointer(tmp);
        indexIdentifier = startList.getPointer();
    }

    /**
     * Erstellt Paare für die gesamte Teilnehmerliste so weit wie möglich.
     * <p>
     * Es wird versucht jede Spieler ein Gegner unter die Teilnehmer zu finden.
     * Die Paarung erfolgt ganz am Anfang des Turniers, das heißt, dass keine der Teilnehmer noch ein Spiel gespielt hat.
     * Somit erfolgt die Paarung nur abhängig von der Bewertung der Spieler.
     * <p>
     * Es wird die gesamte Teilnehmerarray durchgegangen. Wenn ein Spieler als Gegner ein andere Spieler
     * zugeteilt wird, dann wird von beide Spieler {@link Player#getIsInGame()} auf <code>true</code> gesetzt
     * und die gegenseitige Index für {@link Player#getOpponentIndex()} zugewiesen.
     * Mit den Spieler Attribut <code>isInGame</code> kann auch erkannt werden, ob diese Spieler schon
     * als ein Gegner ein andere Spieler zugeteilt wurde und kann somit diese überspringen werden.
     * <p>
     * Um die Spieler ein andere Spieler als Gegner zuzuteilen, wird von der Spieler, der den Gegner sucht,
     * die obere Grenze berechnet. Es muss nur die Obere grenze und nicht die untere Grenze berechnet werden,
     * weil die Paarung erfolgt durch den durchlauf von der Teilnehmerarray was nach dem Rating der Teilnehmer
     * sortiert wurde und weshalb der Spieler, der ein Gegner sucht, ist der Spieler, der das niedrigste Rating hat,
     * unter die Spieler die noch nicht in einem Spiel sind. Damit wird ein Spieler zufällig ausgewählt, die innerhalb dieser Grenze liegt und noch nicht
     * als Gegner ein andere Spieler zugeteilt wurde, und als Gegner von dem suchenden Spieler zugeteilt.
     * <p>
     * Es gibt einige Grenzfälle, die durch den Aufruf von {@link #checkEdgeCase(int, Player)} behandelt wird.
     * @see #participants
     * @see #participantsRating
     * @see Player#getRating()
     * @see #getUpperIndex(int[], Player)
     * @see #getLowerIndex(int[], Player)
     * @see Random#nextInt(int, int)
     */
    public void pairParticipantsFirstTime(){

        //Gegner Index
        int opponentIndex = -1;
        Random random = new Random();

        //Index der Gegnersuchende Spieler
        int index;
        int upperBoundIndex = -1;

        //Suchende Spieler
        Player player = null;

        for(int i = 0; i < participants.length; i++){

            index = i;
            if(participants[index].getIsInGame()){
               // System.out.println(index);
                continue;
            }

            participants[i].setIsInGame(true);

            player = participants[index];
            //System.out.println("PLAYERRATING: " + player.getRating());
            //legt die obere grenzindex fest für Gegnersuchende Spieler
            upperBoundIndex = getUpperIndex(participantsRating,player);

            //System.out.println("Upper " + upperBoundIndex);

            ArrayList<Integer> indexList = new ArrayList<>();
            for(int j = index+1; j <= upperBoundIndex; j++){
                if(participants[j].getIsInGame()){
                    continue;
                }
                indexList.add(j);
            }
            //System.out.println(indexList);
            if(indexList.isEmpty()){
                if(checkEdgeCase(upperBoundIndex,player)){
                    continue;
                }
                else{
                    break;
                }
            }
            opponentIndex = indexList.get(random.nextInt(indexList.size()));

            player.setOpponentIndex(opponentIndex);
            participants[opponentIndex].setOpponentIndex(index);
            participants[opponentIndex].setIsInGame(true);

            //System.out.println("Opponent Index: " + opponentIndex);

        }

    }

    /**
     * Liefer den Index eines Spielers von der Teilnehmerarray, die die obere Grenze des übergegebenen Spielers ist.
     * <p>
     * Es wird anhand der Bewertung des übergegebene Spielers die Bewertung berechnet, gegen die er höchstens Spielen kann.
     * Anhand diese berechnete Bewertung wird eine binäre Suche an {@link #participantsRating} gemacht und das Index,
     * wo sich der Bewertung in das Array befindet, wird zurückgegeben.
     * Wenn die Bewertung nicht in das Array ist, dann wird der Index zurückgegeben, wo der Bewertung eingefügt wäre in
     * das Array minus 1.
     * @param array     Array von den Teilnehmerbewertungen sortiert
     * @param player    Spieler dessen Obergrenze bestimmt sein soll
     * @return  Obergrenzindex in das Teilnehmerarray für den übergegebene Spieler
     * @see Player#getRating()
     * @see Arrays#binarySearch(int[], int)
     */
    public int getUpperIndex(int[] array, Player player){
        int upperBoundIndex = Arrays.binarySearch(array,player.getRating()+400);
        if(upperBoundIndex < 0){
            upperBoundIndex = (upperBoundIndex*(-1))-2;
        }

        return upperBoundIndex;
    }

    /**
     * Prüft Grenzfälle für die {@link #pairParticipantsFirstTime()} und liefert eine Entsprechende boolean wert zurück, abhängig von Grenzfall.
     * <p>
     * Mögliche Grenzfälle:
     * <ul>
     *  <li>Ungerade anzahl von Teilnehmer</li>
     *  <li>Die Bewertung einen Spieler ist zu groß/klein um einen Spieler unter der gesamten Teilnehmerarray als Gegner zu finden</li>
     *  <li>Die Bewertung einen/mehreren Spieler ist zu groß/klein um einen Spieler unter die Teilnehmer,die noch keinen Paar haben, als Gegner zu finden. Außerdem haben diese Spieler auch keinen gemeinsamen Spieler die beide als Gegner haben könnten</li>
     *  <li>Die Bewertung einen/mehreren Spieler ist zu groß/klein um einen Spieler unter die Teilnehmer,die noch keinen Paar haben, als Gegner zu finden. Es gibt aber gemeinsame Gegner</li>
     * </ul>
     * Wenn die Anzahl der Teilnehmer Ungerade ist, bekommt der letzte Spieler die noch kein Gegner hat, auch kein Gegner.
     * Es wird dann <code>false</code> zurückgeliefert.
     * <p>
     * Spieler, die mit dem üblichen Spieler die kein Paar haben, nicht spielen können und diese auch keinen gemeinsamen Spieler haben, die sie als Gegner haben könnten, die schon ein Paar haben,
     * bekommen auch kein Gegner. Es wird dann <code>true</code> zurückgeliefert.
     * <p>
     * Wenn aber diese Spieler doch einen oder mehrere gemeinsamen Spieler haben, die gegen beide Spieler spielen könnten,
     * dann wird zufällig eine dieser gemeinsamen Spieler ausgewählt und die Gegner der diese gemeinsamen Spieler zu der Spieler,
     * mit der niedrigeren Bewertung, als Gegner zugeteilt, und
     * der gemeinsame Spieler als Gegner zu der Spieler mit der größeren Bewertung zugeteilt.
     * @param upperBoundIndex   Obergrenzindex in das Teilnehmerarray für den übergegebene Spieler
     * @param player    Spieler die kein Gegner findet
     * @return  false, wenn diese der letzte Spieler und kein Gegner finden kann, ansonsten true
     * @exception IllegalArgumentException  Wenn ein Spieler so großen oder kleinen Bewertung hat, dass mit keinem Teilnehmer des Turniers spielen kann.
     * @see #participants
     * @see Player#getRating()
     * @see #getUpperIndex(int[], Player)
     * @see Player#setIsInGame(boolean)
     * @see Player#setOpponentIndex(int)
     * @see Random#nextInt()
     */

    private boolean checkEdgeCase(int upperBoundIndex, Player player){
        int opponentIndex;
        if(upperBoundIndex+1 >= participants.length){
            player.setIsInGame(false);
            return false;
        }
        else if(participants[upperBoundIndex].getRating()+400 < participants[upperBoundIndex+1].getRating()){
            if(upperBoundIndex+2 >= participants.length || player.getIndex() == 0){
                throw new IllegalArgumentException("one player cannot match with anyone");
            }
            player.setIsInGame(false);
            return true;
        }
        else{
            int alternateLowerBoundIndex = getLowerIndex(participantsRating,participants[upperBoundIndex+1]);
            Random random = new Random();
           // System.out.println("ALTERNATE: " + alternateLowerBoundIndex);
            opponentIndex = random.nextInt(alternateLowerBoundIndex,upperBoundIndex+1);
           // System.out.println("opponent LULULU index: " + opponentIndex);
            player.setOpponentIndex(participants[opponentIndex].getOpponentIndex());
            participants[participants[opponentIndex].getOpponentIndex()].setOpponentIndex(player.getIndex());
            participants[opponentIndex].setIsInGame(false);
            return true;
        }

    }

    /**
     * Simulierung der Gegner suche für die Spieler mit der niedrigsten Spielzeit, wenn das möglich ist, Ausführung des Spiels für die beiden Spieler und einsortierung nach der aktualisierte Spielzeit.
     * <p>
     * Der Gegnersuchende Spieler ist <code>player</code>.
     * Es wird für der <code>player</code>, die an der erste element von {@link #startList} liegt, zuerst versucht ein Gegner zu suchen.
     * Um ein Gegner für <code>player</code> zu finden, wird die untere und oberer Grenze von <code>player</code> berechnet.
     * Die Gegnersuche hängt nicht nur von der Bewertung der Spieler ab, sondern auch die Spielzeit der Teilnehmer.
     * Es wird immer versucht ein Gegner zu finden, bei dem der Spielzeit einen minimalen differenz zu der Spielzeit von <code>player</code> hat.
     * Wenn <code>player</code> einen Gegner finden könnte, wird dann das Spiel zwischen die beiden Spieler ausgeführt.
     * Nachdem wird <code>player</code> und sein Gegner, nach der gespielten Zeit, richtig in <code>startList</code> einsortiert.
     * Es gibt aber ein paar Grenzfälle falls <code>player</code> kein Gegner findet:
     * <ul>
     *     <li>Wenn {@link #globalCounter} >= Anzahl der Teilnehmer, dann wird die Funktion beendet ohne ausführung des Spiels und einsortierung}</li>
     *     <li>Wenn Spielzeit von <code>player</code> >= Turnierdauer, dann wird nur <code>player</code> einsortiert aber es wird kein Spiel ausgeführt und der Funktion wird beendet</li>
     *     <li>
     *         Wenn beide vorher genannte Fälle nicht eintreten, dann enthält {@link #indexIdentifier} das nächste <code>CustomList</code> element und der Funktion wird beendet.
     *         Somit wird beim nächsten aufruf dieser Funktion wird <code>player</code> nicht mehr das erste, sondern das zweite Element was natürlich auch ein andere Spieler ist.
     *         Diese Fall kann eintreten wenn <code>player</code> keinen Gegner gefunden hat, weil er zum Beispiel schon gegen einer diese Spieler gerade eben gespielt hat.
     *         Das heißt, dass <code>player</code> noch Spieler in diese Turnier hätte gegen die er Spielen könnte. In die beiden davor genannte Fällen ist dies nicht der fall.
     *     </li>
     * </ul>
     * Es kann eintreten, dass bei der letzten genannten Grenzfall, mehrere Spieler noch gibt deren Spielzeit kleiner ist als die Turnierdauer und die können eigentlich gegeneinander Spielen,
     * aber da sie schon davor gegeneinander gespielt haben, findet keine davon ein Gegner. Zum Beispiel wir haben 3 Spieler: Spieler 1, Spieler 2 und Spieler 3. Spieler 1 kann von der Bewertung
     * her gegen Spieler 2 spielen aber gegen Spieler 3 nicht. Aber Spieler 1 hat schon gegen Spieler 2 davor gespielt, somit findet Spieler 1 kein Gegner. Spieler 2 kann von der Bewertung her
     * gegen beide Spieler spielen. Da Spieler 1 schon gegen Spieler 2 gespielt hat, kann Spieler 2 nicht Spieler 1 als gegner haben. Spieler 3 hat aber in sein letzten spiel gegen Spieler 2 gespielt
     * somit, kann Spieler 2 auch nicht gegen Spieler 3 spielen und Spieler 3 kann auch nicht von der Bewertung her gegen Spieler 1 spielen. Daher findet keine der 3 Spieler ein Gegner. Dieser Fall wird
     * gelöst, indem man die Spielerzeit von dem Spieler bei der <code>indexIdentifier</code> betrachtet. Wenn diese Spielerzeit dieser Spieler >= Turnierdauer, dann bedeutet das, dass
     * <code>indexIdentifier</code> schon über alle vorhanden Spieler durchgegangen ist deren Spielzeit kleiner als das Turnierdauer ist und keinen Gegner gefunden haben. Dann wird {@link Player#getOpponentIndex()} von
     * allen Spieler, die noch spielen können, auf -1 gesetzt. Somit kann jede Spieler gegen jede Spieler spielen, dessen Bewertung passt.
     * @param multiplier eine zahl die mit 25 multipliziert wird und somit die Grenze für das Verlassen der Spiele für die Spieler berechnet
     * @param playerIndex   index der Spieler der als einzige die Spiele verlässt
     * @see #endList
     * @see #gameTime
     * @see Player#getTimePlayed()
     * @see Player#getIsInGame()
     * @see #getUpperIndex(int[], Player)
     * @see #getLowerIndex(int[], Player)
     * @see #pairTwoPlayers(Player)
     * @see #sortInCustomListOne(Player, CustomList)
     * @see #sortInCustomListOneOpponent(Player) (Player, CustomList)
     */
    public void pairParticipants(int multiplier, int playerIndex) {

        int index;

        Player player = null;
        if(indexIdentifier.getData().getTimePlayed() >= gameTime){
            setOpponentIndexForEveryPlayer();
            indexIdentifier = startList.getPointer();
            counter = 0;
        }
        //index = participantsSortedWithTime[0].getIndex();
        index = indexIdentifier.getData().getIndex();
        participants[index].setIsInGame(true);

        player = participants[index];
        //System.out.println(index);

        //legt die obere grenzindex fest für die Spieler die myPlayer als stärkste gegner bekommen könnte
        //upperBoundIndex = getUpperIndex(participantsRating, player);

        //legt die untere grenzindex fest für die Spieler die myPlayer als schwächste gegner bekommen könnte
        //lowerBoundIndex = getLowerIndex(participantsRating, player);

        //System.out.println("Niedrigere Grenze: " + lowerBoundIndex);
        //System.out.println("Höhere Grenze: " + upperBoundIndex);

        pairTwoPlayers(player);


        if(!player.getIsInGame()){
            if(globalCounter >= participants.length){
                return;
            }
            else if(player.getTimePlayed() >= gameTime){
                sortInCustomListOne(player,indexIdentifier);
            }
            else if(counter == (participants.length - globalCounter)){
                //System.out.println(counter);
                //System.out.println(globalCounter);
                CustomList tmp = startList.getPointer();
                while(tmp.getData().getTimePlayed() < gameTime){
                    tmp.getData().setTimePlayed(gameTime);
                    tmp = tmp.getPointer();
                }
                globalCounter = participants.length;
            }
            else{
                indexIdentifier = indexIdentifier.getPointer();

            }
            return;
        }else{
            game.startGame(participants,gameTime,multiplier,index,playerIndex);
            if(player.getTimePlayed() >= gameTime){
                globalCounter++;
            }
            if(participants[player.getOpponentIndex()].getTimePlayed() >= gameTime){
                globalCounter++;
            }
        }
       /* if(startList.getPointer().getData().getIndex() != indexIdentifier.getData().getIndex()){
            sortInCustomList(player,startList.getCustomListWithPlayerIndex(player.getIndex()));
            indexIdentifier = startList.getPointer();
        }
        else{
            sortInCustomList(player,startList);
        }*/
        sortInCustomListOne(player,indexIdentifier);
        sortInCustomListOneOpponent(player);

        indexIdentifier = startList.getPointer();
        //startList.printList();
       // System.out.println();
        //printParticipants();

    }

    /**
     * Sortiert ein Spieler anhand deren Spielzeit in {@link #startList} ein.
     * <p>
     * Es wird anhand der übergegebenen Listelement von der Spieler das Element berechnet, die sich vor der
     * das übergegebene Listelement befindet und in <code>tmpStart</code> gespeichert.
     * Das einzusortierende Spielerelement wird in <code>tmp</code> gespeichert.
     * <code>currentPlayer</code> wird dann mit tmp initialisiert.
     * Es wird überprüft, ob die Spielerzeit der einzusortierende Spieler größer ist als die Spielerzeit des Spielers auf
     * das letzte Element. Wenn das der Fall ist, dann muss nicht die ganze Liste durchgegangen werden, sondern die zeiger
     * von <code>tmpStart</code> wird nicht mehr auf <code>tmp</code> zeigen, sondern auf die nachfolgende element von <code>tmp</code>.
     * <code>tmp</code> wird dann auf {@link #endList} zeigen und das vorherige letzte element nicht mehr auf <code>endlist</code>
     * sondern auf <code>tmp</code> zeigen. Letztendlich wird <code>endList</code> auf <code>tmp</code> zeigen.
     * <p>
     * Wenn der Spielerzeit der einzusortierende Spieler nicht größer als das von dem Spieler auf das letzte Element, dann
     * wird die Liste ab <code>tmp</code> durchgegangen bis die richtige Stelle findet.
     * Wenn die richtige Stelle gefunden wurde, dann wird <code>tmpStart</code> auf die Nachfolger von <code>tmp</code> zeigen.
     * Hierbei wird der aktuelle Gegner des einzusortierenden Spieler übersprungen, da diese noch unsortiert in die Liste liegt
     * und somit Spieler, die nach dem aktuellen Gegner in die Liste befinden, einer niedrigeren Spielzeit als der unsortierte Gegner haben können.
     * <code>tmp</code> zeigt auf die Nachfolgende Element der gefundenen Stelle und das Vorgängerelement zeigt dann auf <code>tmp</code>.
     * @param player    der einzusortierende Spieler
     * @param start     Listenelement der einzusortierende Spieler
     * @see Player#getTimePlayed()
     * @see Player#getOpponentIndex()
     * @see Player#getIndex() 
     * @see CustomList#getPointer()
     * @see CustomList#getData()
     * @see CustomList#getCustomListWithPlayerIndex(int)
     * @see #pairParticipants(int, int)
     * @see CustomList
     */
    public void sortInCustomListOne(Player player, CustomList start){

        CustomList tmpStart = startList.getCustomListWithPlayerIndex(start.getData().getIndex());
        CustomList tmp = tmpStart.getPointer();
        CustomList currentPlayer = tmpStart.getPointer();

        if(!(player.getTimePlayed() <= tmp.getPointer().getData().getTimePlayed()) || player.getOpponentIndex() == tmp.getPointer().getData().getIndex()){
            if(player.getTimePlayed() >= endList.getPointer().getData().getTimePlayed()){
                //tmp = startList.getPointer();
                tmpStart.setPointer(tmp.getPointer());
                endList.getPointer().setPointer(tmp);
                endList.setPointer(tmp);
                tmp.setPointer(null);
            }
            else{
                for(int i = 0; i < participantsSortedWithTime.length; i++){;

                    if(player.getTimePlayed() <= currentPlayer.getPointer().getData().getTimePlayed() && player.getOpponentIndex() != currentPlayer.getPointer().getData().getIndex()){
                        tmpStart.setPointer(tmp.getPointer());
                        tmp.setPointer(currentPlayer.getPointer());
                        currentPlayer.setPointer(tmp);
                        break;
                    }
                    currentPlayer = currentPlayer.getPointer();
                }
            }

        }
    }

    /**
     * Sortiert ein den Gegner anhand deren Spielzeit in {@link #startList} ein.
     * <p>
     * Sehr ähnlich zur {@link #sortInCustomListOne(Player, CustomList)}.
     * Einzige Unterschied ist, dass hier nicht mehr auf aktuellsten gegner bei der Sortierung beachtet wird,
     * weil diese schon Einsortiert wurde. Da diese schon einsortiert wurde, sollte man diese nicht überspringen.
     * @param player    Spieler deren Gegner einzusortieren ist
     * @see #pairParticipants(int, int)
     */
    public void sortInCustomListOneOpponent(Player player){
        CustomList tmpStart = startList.getCustomListWithPlayerIndex(player.getOpponentIndex());
        CustomList tmp = tmpStart.getPointer();
        CustomList currentPlayer = tmpStart.getPointer();


        if(!(participants[player.getOpponentIndex()].getTimePlayed() <= tmp.getPointer().getData().getTimePlayed())) {
            if(participants[player.getOpponentIndex()].getTimePlayed() >= endList.getPointer().getData().getTimePlayed()){
                tmpStart.setPointer(tmp.getPointer());
                endList.getPointer().setPointer(tmp);
                endList.setPointer(tmp);
                tmp.setPointer(null);
            }
            else{
                for (int i = 0; i < participantsSortedWithTime.length; i++) {
                    if (participants[player.getOpponentIndex()].getTimePlayed() <= currentPlayer.getPointer().getData().getTimePlayed()) {
                        tmpStart.setPointer(tmp.getPointer());
                        tmp.setPointer(currentPlayer.getPointer());
                        currentPlayer.setPointer(tmp);
                        break;
                    }
                    currentPlayer = currentPlayer.getPointer();
                }
            }

        }
    }

    /**
     * Sucht einen Gegner für einen Spieler, abhängig von dem {@link Player#getRating()} und {@link Player#getTimePlayed()} sind, wenn diese möglich ist.
     * <p>
     * Diese Funktion wird von {@link #pairParticipants(int, int)} aufgerufen, um einen Spieler
     * ein Gegner zu finden. Die Gegner suchende Spieler ist meistens der Spieler mit der niedrigsten Spielzeit.
     * Es gibt Ausnahmefälle, indem nicht der Spieler mit der niedrigsten Spielzeit ein Gegner sucht, sondern der Spieler mit der nächstniedrigere
     * Spielzeit oder die drittniedrigsten Spieler usw., falls der davorliegenden Spieler keine Gegner gefunden haben.
     * <p>
     * Die Liste von Spieler die nach ihrer Spielzeit sortiert ist, wird durchgegangen bis die möglichen Gegner gefunden werden.
     * In normalfall ist oder sind, da mehrere Spieler identische Spielzeit haben können, das der Spieler mit dem nächstniedrigeren Spielzeit
     * exklusiv die suchende Spieler selbst und der Gegner gegen die er die Runde davor gespielt hat.
     * Außerdem werden noch natürlich spieler mit zu hohen oder niedrigen Rating in dem Fall mit einem Rating größer oder kleiner als 400 übersprungen.
     * Wenn mehrere potenzielle Gegner gibt, wird das durch eine zufallszahl ausgewählt welche Gegner den gegner suchende Spieler als Gegner bekommt.
     * <p>
     * Der gefundene Gegner kann die Gleiche Spielzeit wie der suchende Spieler haben, wenn diese aber nicht der Fall ist,
     * wird die Spielzeit von dem zwei Spieler angepasst, um warten zu simulieren.
     * Angepasst bedeutet, dass bei unterschiedliche Spielzeiten wird der Spielzeit der Spieler auf die Spielzeit von dem Gegner gesetzt.
     * Zudem werden die {@link Player#setOpponentIndex(int)} von beiden Spieler gesetzt und {@link Player#setIsInGame(boolean)} von der Gegner auf <code>true</code> gesetzt.
     * <p>
     * Es gibt 2 Ausnahmefälle. Wenn der Spieler keine Gegner findet, aber es gibt Spieler, gegen die der Spieler abhängig von dem Rating spielen könnte.
     * Das wäre zum Beispiel der Fall, wenn nur noch zwei Spieler übrig sind und die davor gegeneinander Gespielt haben.
     * In diesen Fall wird einen counter erhöht und am Ende dieser Funktion wird geprüft ob der counter > 0 ist. In den vorher genannten Fall ist größer 0 und
     * der zweitniedrigste spieler sucht sich einen Gegner. Im zweiten Ausnahmefall findet der Spieler wieder kein Gegner, aber in diesen Fall könnte der Spieler auch gegen
     * keine Spieler mehr spielen. Deswegen wird der spielzeit der Spieler auf {@link #gameTime} gesetzt.
     * @param player    Gegner suchende Spieler
     * @see Random#nextInt()
     * @see #startList
     * @see #participants
     */
    public void pairTwoPlayers(Player player){
        int timeDiff = 100_001;
        int minTimeDiff = 100_000;
        int minTime = 0;
        int opponentIndex = -1;
        int potentialOpponentTime = -1;
        int normalIndex;
        int playableOpponents = 0;
        CustomList tmp = indexIdentifier.getPointer();

        //in diese liste werden indexe von spieler gespeichert die den selben timeDiff haben
        ArrayList<Integer> sameTimeDiffList = new ArrayList<>();

        /*System.out.print("[");
        for(int i = 0; i < participants.length; i++){
            if(participants[i].getIsInGame()){
                System.out.print(participants[i].getIndex()+", ");
            }
        }
        System.out.print(player.getOpponentIndex());
        System.out.println("]");
        /*for(int i = 0; i < participantsSortedWithTime.length; i++){
            System.out.println("Spieler: " + participantsSortedWithTime[i].getIndex() + " Zeit: " +participantsSortedWithTime[i].getTimePlayed());
        }*/

        for(int j = 1; j<participantsSortedWithTime.length;j++){
            //normalIndex = participantsSortedWithTime[j].getIndex();
            //normalIndex = j;
            normalIndex = tmp.getData().getIndex();
            /*if(normalIndex == player.getIndex()){

            }*/
            if(participants[normalIndex].getTimePlayed() >= gameTime){
                break;
            }
            else if(participants[normalIndex].getRating() > player.getRating()+400 || participants[normalIndex].getRating() < player.getRating()-400){
                //System.out.println("Zu groß oder Klein: " + normalIndex);
            }
            else if((normalIndex == player.getOpponentIndex() || participants[normalIndex].getOpponentIndex() == player.getIndex()) && globalCounter < participants.length-2){
               // System.out.println(normalIndex);
                playableOpponents++;
            }
            /*else if(participants[normalIndex].getIsInGame()){

                //System.out.println(normalIndex);
            }*/
            /*else if(j == player.getIndex()){
                continue;
            }*/
            else if(minTime == 0){
                minTime = participants[normalIndex].getTimePlayed();
                opponentIndex = normalIndex;
                sameTimeDiffList.add(normalIndex);
            }
            else{
                potentialOpponentTime = participants[normalIndex].getTimePlayed();
                if(minTime != potentialOpponentTime){
                    break;
                }else{
                    sameTimeDiffList.add(normalIndex);
                }

                /*timeDiff = participants[j].getTimePlayed()-player.getTimePlayed();
                if(timeDiff < 0){
                    timeDiff *= (-1);
                }
                if(minTimeDiff > timeDiff){
                    minTimeDiff = timeDiff;
                    opponentIndex = j;
                    if(sameTimeDiffList.size() > 1){
                        sameTimeDiffList.clear();
                        sameTimeDiffList.add(j);
                    }
                    else if(sameTimeDiffList.size() == 1){
                        sameTimeDiffList.set(0,j);
                    }
                    else{
                        sameTimeDiffList.add(j);
                    }
                }
                else if(minTimeDiff == timeDiff){
                    sameTimeDiffList.add(j);
                }*/
            }
            tmp = tmp.getPointer();
        }
        //System.out.println(sameTimeDiffList);
        if(opponentIndex == -1){
            if(playableOpponents == 0){
                //System.out.println("GLOBALCOUNTER: " + globalCounter);
                if(globalCounter == participants.length-2){
                   // System.out.println("ENDE!");
                    player.setTimePlayed(gameTime);
                    indexIdentifier.getPointer().getData().setTimePlayed(gameTime);
                    globalCounter = participants.length;
                }
                else{
                    //player.setTimePlayed(gameTime);
                    //globalCounter++;
                    counter++;
                }
            }
            player.setIsInGame(false);
            return;
        }
        //System.out.println("Time from Player Before: " + player.getTimePlayed());
        //System.out.println("Time from Opponent Before: " + participants[opponentIndex].getTimePlayed());
        //System.out.println("TIMEDIFF:"+timeDiff);
        if(sameTimeDiffList.size() > 1){
            Random random = new Random();
            opponentIndex = sameTimeDiffList.get(random.nextInt(sameTimeDiffList.size()));
        }
        if(participants[opponentIndex].getTimePlayed() > player.getTimePlayed()){
            player.addTimePlayed(participants[opponentIndex].getTimePlayed() - player.getTimePlayed());
        }else{
            participants[opponentIndex].addTimePlayed(player.getTimePlayed() - participants[opponentIndex].getTimePlayed());
        }
        /*System.out.println("Time from Player After: " + player.getTimePlayed());
        System.out.println("Time from Opponent After: " + participants[opponentIndex].getTimePlayed());
        System.out.println("OPPONENTINDEX: " + opponentIndex);*/
        participants[opponentIndex].setIsInGame(true);
        player.setOpponentIndex(opponentIndex);
        participants[opponentIndex].setOpponentIndex(player.getIndex());
        //System.out.println("counter: " + counter);
        counter = 0;

    }

    public void setGameTimeForEveryPlayer(){
        for(int i = 0; i < participants.length; i++){
            if(participants[i].getTimePlayed() < gameTime){
                participants[i].setTimePlayed(gameTime);
            }
        }
        globalCounter = participants.length;
    }

    /**
     * Setzt {@link Player#getOpponentIndex()} für jede Teilnehmer, dessen Spielzeit noch kleiner als das Turnierdauer ist, auf -1.
     * <p>
     * Diese Funktion wird von {@link #pairParticipants(int, int)} bei der Ausnahmefall aufgerufen, wenn zum Beispiel nur noch zwei Spieler gibt, die davor schon gegeneinander gespielt haben,
     * aber die Spielzeit von beide Spieler ist noch kleiner als das Turnierdauer.
     */
    public void setOpponentIndexForEveryPlayer(){
        for(int i = 0; i < participants.length; i++){
            if(participants[i].getTimePlayed() < gameTime){
                participants[i].setOpponentIndex(-1);
            }
        }
    }

    /**
     * Setzt {@link Player#getGamesLeft()} für jede Turnierteilnehmer um auf die Konsole ausgeben zu können.
     * @param gamesLeftArray    Array von dem anzahl der verlassenen Spiele für jede Teilnehmer
     * @see #printTable(double, int[]) 
     */
    private void setPlayerGamesLeft(int[] gamesLeftArray){
        for(int i = 0; i < participants.length; i++){
            participants[i].setGamesLeft(gamesLeftArray[i]);
        }
    }

    /**
     * Ausgabe auf die Konsole von dem Spieler mit dem entsprechenden Punkte, Anzahl der verlassenen Spiele, Anzahl der Gespielte Spieler und Zeit.
     * <p>
     * Wenn nur ein Turnier gespielt wird, wird dann <code>tournamentPlayed</code> 1.
     * Bei mehrere Turniere wird <code>tournamentPlayed</code> die Anzahl der Turniere entsprechen, da
     * eine durchschnittliche Punkteanzahl ausgegeben wird, die der Spieler in diesem Turniere durchschnittlich erhalten hat.
     * Außerdem wird, wenn mehrere Turniere gespielt werden, ein Array von der gesamten Anzahl der Verlassen-Spiele für jeden Teilnehmer
     * benötigt, da diese nach jedem Turnier auf 0 gesetzt wird, weil sonst die Berechnung von Spielzeit für die Spieler im nächsten Turnier
     * nicht stimmen würde.
     * Wenn nur ein Turnier gespielt wird, sollte dann <code>gamesLeftArray</code> <code>null</code> sein.
     * @param tournamentPlayed  ist die Anzahl wie viele Turniere gespielt wurde
     * @param gamesLeftArray    Array von dem anzahl der verlassenen Spiele für jede Teilnehmer
     */
    public void printTable(double tournamentPlayed, int[] gamesLeftArray){
        if(gamesLeftArray != null){
            setPlayerGamesLeft(gamesLeftArray);
        }
        Arrays.sort(participants,Comparator.comparingInt(Player::getPoints).reversed());
        for(int i = 0; i < participants.length; i++){
            System.out.print(participants[i].getId() + " Points: " + participants[i].getPoints()/tournamentPlayed);
            System.out.print(" Games Left: " + participants[i].getGamesLeft());
            System.out.println(" Games Played: " + participants[i].getGamesPlayed() + " Time: " + participants[i].getTimePlayed());
            participants[i].setGamesLeft(0);
        }
    }

    /**
     * Liefer den Index eines Spielers von der Teilnehmerarray, die die untere Grenze des über gegebenen Spielers ist.
     * <p>
     * Es wird anhand der Bewertung des über gegebene Spielers die niedrigste Bewertung berechnet, gegen die er noch Spielen kann.
     * Anhand diese berechnete Bewertung wird eine binäre Suche an {@link #participantsRating} gemacht und das Index,
     * wo sich der Bewertung in das Array befindet, wird zurückgegeben.
     * Wenn die Bewertung nicht in das Array ist, dann wird der Index zurückgegeben, wo der Bewertung eingefügt wäre in
     * das Array minus 1.
     * @param array     Array von den Teilnehmerbewertungen sortiert
     * @param player    Spieler dessen Untergrenze bestimmt sein soll
     * @return  Untergrenzindex in das Teilnehmerarray für den über gegebene Spieler
     * @see Player#getRating()
     * @see Arrays#binarySearch(int[], int)
     */
    public int getLowerIndex(int[] array, Player player){
        int lowerBoundIndex = Arrays.binarySearch(array,player.getRating()-400);
        if(lowerBoundIndex < 0){
            lowerBoundIndex = (lowerBoundIndex*(-1))-1;
        }

        return lowerBoundIndex;
    }

    /**
     * Setzt Spielzeit und die Anzahl der verlassenen Spiele von jedem Teilnehmer und {@link #globalCounter} zurück.
     * <p>
     *  Wenn mehrere Turniere demselben Turnierobjekt durchgeführt werden, müssen bevor die erneuten ausführung diese Werte zurückgesetzt werden
     */
    public void resetTournament(){
        for(int i = 0; i < participants.length; i++){
            participants[i].setTimePlayed(0);
            //participantsSortedWithTime[i].setTimePlayed(0);
            //participants[i].setPoints(0);
            participants[i].setGamesLeft(0);
            //participants[i].setGamesPlayed(0);
        }
        globalCounter = 0;
        //list = new ArrayList<>();
    }

    /**
     * Zurücksetzung der Punkte und die Anzahl der Spieler von jedem Turnierteilnehmer.
     */
    public void resetPlayerAttributes(){
        for(int i = 0; i < participants.length; i++){
            participants[i].setPoints(0);
            participants[i].setGamesPlayed(0);
        }
        //Arrays.sort(participants,Comparator.comparingInt(Player::getRating));
    }

    //Getter und Setter
    public Player[] getParticipants(){
        return this.participants;
    }
    public int[] getParticipantsRating(){
        return this.participantsRating;
    }

    public Player[] getParticipantsSortedWithTime(){
        return this.participantsSortedWithTime;
    }
    public int getGameTime(){
        return this.gameTime;
    }

    public void setGlobalCounter(int globalCounter){
        this.globalCounter = globalCounter;
    }

  /*  public void pairParticipants(/*int[][] participantsPairing*///) {
        //long timeStart = System.currentTimeMillis();
        //long timeEnd;
        //Mit counter wird überprüft wann jede Spieler einen Paar gefunden hat, auch index für die Paare
        //int counter = 0;

        //Gegner Index
        //int opponentIndex = -1;

        //ArrayList um die SpielerIndexe zu Sammeln die schon ein Paar gefunden haben
        //ArrayList<Integer> list = new ArrayList<>();

        //Integer Array in dem die Ratings von allen Spieler sind um upper- und lowerindex leichter zu berechnen
        //int[] array = Arrays.stream(participants).mapToInt(Player::getRating).toArray();

        //Spieler Array die nach der Spielzeit sortiert werden
        //int[][] participantsPairing2 = null;

        //Exclude array besteht aus list um leichter auf die elemente zuzugreifen
        //int[] excludeArray = null;

        //Enthält alle Paare die zugelost werden
        //int[][] result = new int[participants.length / 2][3];

        //Enthält alle vorherige Paare um spieler die gegeneinander gespielt haben in die nächste runde nicht wieder gegeneinander spielen
        //ist aber nach die 2 Spalte sortiert und nicht nach die erste
        //Player[] participantsSortedWithTime = null;
/*
        int temp = -1;
        int index;
        int upperBoundIndex = -1;
        int lowerBoundIndex = -1;
        int indexInList = -1;
        //boolean firstTime = true;

        Player player = null;
/*
        for (int i = 0; i < result.length; i++) {
            result[i][0] = -1;
            result[i][1] = -1;
        }*/

        //Wenn ein mal schon paare entstanden sind dann werden die arrays auch gefüllt
        //Arrays.sort(participantsPairing,Comparator.comparingInt(x->x[2]));
        //participantsPairing2 = new int[participantsPairing.length][2];
        /*participantsSortedWithTime = new Player[participants.length];
        int helpCounter = 0;
        for (int i = 0; i < participantsSortedWithTime.length; i++) {
            //participantsPairing2[i] = participantsPairing[helpCounter][i%2];
            participantsSortedWithTime[i] = participants[i];
        }*/
    //    Arrays.sort(participantsSortedWithTime, Comparator.comparingInt(Player::getTimePlayed));
       /* for (int i = 0; i < participants.length; i++) {
            //participantsPairing2[i][0] = participantsPairing[i][0];
            //participantsPairing2[i][1] = participantsPairing[i][1];
            //System.out.println("Time: " + participantsSortedWithTime[i].getTimePlayed());
                System.out.print("first: " + participants[i].getIndex() + " IsInGame: " + participants[i].getIsInGame());
                System.out.println(" second: " + participants[i].getOpponentIndex() + " IsInGame: " + participants[i].getIsInGame());
        }*/
        //Arrays.sort(participantsPairing, Comparator.comparingInt(a -> a[0]));
        //Arrays.sort(participantsPairing2, Comparator.comparingInt(a -> a[1]));
/*
        for (int i = 0; i < participants.length; i++) {

            index = participantsSortedWithTime[i].getIndex();
            //index = participantsPairing[i][i%2];
            //System.out.println(index);
            /*if (list.contains(index)) {
                if(!skipList.isEmpty() && skipList.contains(index)){
                    skipList.remove(Integer.valueOf(index));
                }
                continue;
            }*/
     //       if(participants[index].getIsInGame()){
                /*if(!skipList.isEmpty() && skipList.contains(index)){
                    skipList.remove(Integer.valueOf(index));
                }*/
      /*          continue;
            }
            //list.add(index);
            participants[index].setIsInGame(true);
            //excludeArray = list.stream().mapToInt(x -> x).toArray();
            //playerIndex = randomIntWithExclude(0, participants.length,excludeArray);
            player = participants[index];

            //legt die obere grenzindex fest für die Spieler die myPlayer als stärkste gegner bekommen könnte
            upperBoundIndex = getUpperIndex(participantsRating, player);

            //legt die untere grenzindex fest für die Spieler die myPlayer als schwächste gegner bekommen könnte
            lowerBoundIndex = getLowerIndex(participantsRating, player);

            //ArrayList<Integer> listForRange = getIntegers(list, lowerBoundIndex, upperBoundIndex);
           /* if (!skipList.isEmpty() && skipList.contains(index)) {
                skipList.remove(Integer.valueOf(index));
                //listForRange.addAll(skipList);
               // System.out.println("LISTFORRANGESKIPLIST: "+listForRange);
                //System.out.println("SKIPLIST: " + skipList);
            }
/*
            temp = binarySearchInt2DMinimum(participantsPairing, index, 0);
            if (temp >= 0) {
                indexInList = participantsPairing[temp][1];
            } else {
                temp = binarySearchInt2DMinimum(participantsPairing2, index, 1);
                if (temp >= 0) {
                    indexInList = participantsPairing2[temp][0];
                }
            }*/
            // System.out.println("INDEX IN LIST:" + indexInList);
            /*if (indexInList >= 0) {
                listForRange.add(indexInList);
            }*/

            //System.out.println("Niedrigere Grenze: " + lowerBoundIndex);
            //System.out.println("Höhere Grenze: " + upperBoundIndex);
            //System.out.println("Exclude Listengröße: " + skipList.size());


            //opponentIndex = multipleTimePairing(lowerBoundIndex, upperBoundIndex, listForRange, player, participantsSortedWithTime, i);
      /*      multipleTimePairing(lowerBoundIndex, upperBoundIndex, player, i, counter);
            counter++;
       /* if (opponentIndex == -1) {
            skipList.add(index);
            continue;
        }
/*
        if (counter >= result.length) {
            break;
        }
        System.out.println("Opponent Index: " + opponentIndex);
        //result[counter][0] = index;
        //result[counter][1] = opponentIndex;
        //list.add(opponentIndex);
        counter++;*/
   /*     }
        // timeEnd = System.currentTimeMillis();
        //System.out.println("PairParticipants Dauer: " + (timeEnd - timeStart)+" Millisekunden");
        // return result;
    }*/
}
