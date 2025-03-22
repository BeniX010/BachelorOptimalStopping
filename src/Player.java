
/**
 * Die Klasse <code>Player</code> repräsentiert ein Spieler mit eindeutiger ID und mit seinem bewertung, punkte und Anzahl der verlassenen Spiele und einige Hilfsvariablen.
 * In die <code>Tournament</code> Klasse werden die Teilnehmer als ein Array von <code>Player</code> Objekten dargestellt.
 * @see Tournament#getParticipants()
 */
public class Player {

    /**
     * Eindeutige bezeichner für einen Spieler.
     */
    private String id;
    /**
     * Ist die Bewertung einen Spieler.
     * Es ist eine wert die, die Stärke von dem Spieler angibt.
     * Dieser Wert hilft bei der Gegnersuche einen passenden Gegner mit ähnlichen bewertung zu finden.
     * @see Tournament#pairParticipants(int, int)
     */
    private int rating;
    /**
     * Points sind die punkte einen Spieler die in das Turnier erzielt wurde.
     */
    private double points;
    /**
     * Gibt an, wie viele Spiele der Spieler verlassen hat.
     * Dies wird während der Turniere aktualisiert und wird dadurch eine entsprechende
     * Strafe and dem Spieler hinzugefügt, wenn diese mehr als eine bestimmte Anzahl an Spiele Verlassen hat.
     * @see Game#startGame(Player[], int, int, int, int)
     * @see Game#startGameFirstTime(Player[], int, int, int)
     */
    private int gamesLeft;

    /**
     * Die Zeit in Sekunden die, der Spieler im Turnier verbracht hat.
     * Wenn zwei Spieler gegeneinander Spielen werden beide Spieler die Zeit zugewiesen die, für dem Spiel verbracht haben.
     * Wenn das Spiel von der Spieler verlassen wird, wird die Strafzeit zu diese Attribut zugewiesen abhängig von der Anzahl der verlassenen
     * Spiele, die der Spieler abwarten muss, bevor wieder einen Gegner suchen kann.
     * @see #gamesLeft
     * @see Game#startGameFirstTime(Player[], int, int, int)
     * @see Game#startGame(Player[], int, int, int, int)
     */
    private int timePlayed;

    /**
     * Anzahl der Spiele.
     * Wird erhöht, wenn der Spieler ein Gegner hat unabhängig davon, ob er dem Spiel verlässt oder durchspielt.
     * @see Game#startGameFirstTime(Player[], int, int, int)
     * @see Game#startGame(Player[], int, int, int, int)
     */
    private int gamesPlayed;

    /**
     * Gibt an, an welche Stelle diese Spieler sich bei dem Teilnehmerarray im Turnier, die nach dem Rating sortiert ist, befindet.
     * Diese ist eine hilfsvariable die bei der Gegnersuche des Spielers hilft.
     * @see Tournament#pairParticipants(int, int)
     */
    private int index;

    /**
     * Diese Attribute gibt an, ob ein Spieler in einem Spiel ist oder nicht.
     * Ist eine hilfsvariable, die hilft bei der Spielerpaarung beim ersten Mal.
     * Wenn die teilnehmeranzahl ungerade ist kann der Spieler erkannt werden, die keinen gegner hat.
     * Außerdem ist es auch hilfreich zu erkennen, ob ein Spieler noch überhaupt gegen einen gegner spielen kann.
     * @see Tournament#pairParticipantsFirstTime()
     * @see Tournament#pairParticipants(int, int)
     * @see Game#startGameFirstTime(Player[], int, int, int)
     * @see Game#startGame(Player[], int, int, int, int)
     */
    private boolean isInGame;

    /**
     * Ist die Stelle, wo sich die aktuellen Gegner in die Teilnehmerarray in dem Turnier dieser Spieler befindet.
     * <p>
     * Diese ist ebenso eine Hilfsvariable um das Spiel zwischen zwei Spieler darstellen zu können.
     * Weiterhin hilft diese Attribute auch bei der Gegnersuche.
     * Es wird vermieden die gleichen Gegner zu bekommen.
     * Ausnahme nur, wenn es keine andere möglichkeit mehr gibt.
     * </p>
     * @see Game#startGameFirstTime(Player[], int, int, int)
     * @see Game#startGame(Player[], int, int, int, int)
     * @see Tournament#pairParticipantsFirstTime()
     * @see Tournament#pairParticipants(int, int)
     * @see Tournament#pairTwoPlayers(int, int, Player)
     */
    private int opponentIndex;

    /**
     * Konstruktor um einen Spieler zu erstellen mit einer eindeutigen ID und sein Rating.
     * Die Anzahl der Punkte und die Anzahl der verlassenen Spiele werden mit 0 initializiert.
     * Diese werden während des Turniers verändert und beziehen sich auf ein Turnier.
     * @param id eindeutige Name
     * @param rating bewertungswert
     * @see #id
     * @see #rating
     * @see #points
     * @see #gamesLeft
     * @see #timePlayed
     * @see #gamesPlayed
     * @see #index
     * @see #isInGame
     * @see #opponentIndex
     */
    public Player(String id, int rating){
        this.id = id;
        this.rating = rating;
        this.points = 0;
        this.gamesLeft = 0;
        this.timePlayed = 0;
        this.gamesPlayed = 0;
        this.index = 0;
        this.isInGame = false;
        this.opponentIndex = -1;
    }

    /**
     * Es wird 1 zu Attribut {@link #gamesLeft} addiert.
     */
    public void addGamesLeft(){
        this.gamesLeft++;
    }

    /**
     * Addiert Punkte zu der bestehenden Punkteanzahl.
     * @param point Anzahl der Punkte die zu der Punkteanzahl dazuaddiert werden soll
     * @see #points
     */
    public void addPoints(double point){
        this.points += point;
    }

    /**
     * Addiert Zeit in Sekunden zu der bestehenden Zeit.
     * @param time Zeit in Sekunde
     * @see #timePlayed
     */
    public void addTimePlayed(int time){
        this.timePlayed += time;
    }

    /**
     * Addiert die Spielanzahl zu der bestehenden Spielanzahl.
     * @param game Anzahl der Spiele
     * @see #gamesPlayed
     */
    public void addGamesPlayed(int game){
        this.gamesPlayed += game;
    }

    //Getters and Setters
    public String getId(){
        return this.id;
    }

    public int getRating(){
        return this.rating;
    }

    public double getPoints(){
        return this.points;
    }

    public int getGamesLeft(){
        return this.gamesLeft;
    }

    public int getTimePlayed(){
        return this.timePlayed;
    }

    public int getGamesPlayed(){
        return this.gamesPlayed;
    }

    public int getIndex(){
        return this.index;
    }

    public boolean getIsInGame(){
        return this.isInGame;
    }

    public int getOpponentIndex(){
        return this.opponentIndex;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public void setGamesLeft(int gamesLeft){
        this.gamesLeft = gamesLeft;
    }

    public void setTimePlayed(int timePlayed){
        this.timePlayed = timePlayed;
    }

    public void setGamesPlayed(int gamesPlayed){
        this.gamesPlayed = gamesPlayed;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void setIsInGame(boolean isInGame){
        this.isInGame = isInGame;
    }

    public void setOpponentIndex(int opponentIndex){
        this.opponentIndex = opponentIndex;
    }
}
