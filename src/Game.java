import java.util.Random;

/**
 * Diese Klasse repräsentiert eine Runde von einem Turnier zwischen zwei Spieler.
 * @see Tournament#pairParticipants(int, int)
 */
public class Game {
    /**
     * Ist die Erwartungswert für die Normalverteilung um variable rundenzeiten simulieren zu können.
     */
    private int roundTimeMean;
    /**
     * Dieses Attribut ist die Standardabweichung für die Normalverteilung bei der Rundenzeit generierung.
     */
    private int roundTimeSd;

    /**
     * Konstruktor die eine Instanz von dem <code>Game</code> Klasse erstellt und ihre Attribute initialisiert.
     * @param roundTimeMean Erwartungswert für die Normalverteilung bei der Rundenzeit generierung
     * @param roundTimeSd Standardabweichung für die Normalverteilung bei der Rundenzeit generierung
     * @see #roundTimeMean
     * @see #roundTimeSd
     */
    public Game(int roundTimeMean, int roundTimeSd){
        this.roundTimeMean = roundTimeMean;
        this.roundTimeSd = roundTimeSd;
    }

    /**
     * Startet ein Spiel zwischen zwei Spieler.
     * <p>
     * Diese funktion wird aus der <code>Tournament</code> Klasse aufgerufen nachdem ein Spieler ein Gegner zugeteilt wurde.
     * Um das Spiel zu Simulieren ruft diese Methode {@link #playGame(Player[], int, int, int, int)} auf und leitet alle Parameter weiter.
     * </p>
     * @param participants  Teilnehmer Array des Turniers, die aus Player Objekten besteht
     * @param gameTime  dauer des Turniers in Sekunden
     * @param multiplier    eine zahl die mit 25 multipliziert wird und somit die Grenze für die Spieler berechnet
     * @param index     index der spielende Spieler
     * @param playerIndex   index der Spieler der als einzige die Spiele verlässt
     * @see Tournament#pairParticipants(int, int)
     */
    public void startGame(Player[] participants, int gameTime, int multiplier, int index, int playerIndex) {
        playGame(participants,gameTime,multiplier,index,playerIndex);
    }
    /**
     * Startet ein Spiel zwischen alle Paarungen.
     * <p>
     * Wird aus der <code>Tournament</code> Klasse aufgerufen nachdem jede Spieler ein Gegner gefunden hat, wenn dies möglich ist.
     * Ruft für jede Spieler paar {@link #playGame(Player[], int, int, int, int)} auf.
     * </p>
     * @param participants  Teilnehmer Array des Turniers, die aus Player Objekten besteht
     * @param gameTime  dauer des Turniers in Sekunden
     * @param multiplier    eine Zahl die mit 25 multipliziert wird und somit die Grenze für die Spieler berechnet
     * @param playerIndex   index der Spieler der als einzige die Spiele verlässt
     * @see Tournament#pairParticipantsFirstTime()
     */
    public void startGameFirstTime(Player[] participants, int gameTime, int multiplier, int playerIndex){

        for(int i = 0; i < participants.length; i++){

            if(!participants[i].getIsInGame()){
                continue;
            }

            playGame(participants,gameTime,multiplier,i,playerIndex);

        }

    }

    /**
    * Simuliert ein Spiel zwischen zwei Spieler.
    * <p>
    * Diese Methode wird von {@link #startGame(Player[], int, int, int, int)} und {@link #startGameFirstTime(Player[], int, int, int)} aufgerufen.
    * Um das Spiel zwischen die zwei Spieler zu simulieren wird das {@link Tournament#getParticipants()} und das {@link Player#getIndex()} übergeben um zu wissen,
    * welche zwei Spieler gegeneinander Spielen.
    * Dessen Spielerindex übergeben wurde, ist <code>player1</code> und sein Gegner ist <code>player2</code>.
    * </p>
    * <p>
    * Es wird auch einen multiplier mitgegeben die mit 25 multipliziert wird und somit die höchste Rating berechnet gegen die der Spieler noch spielt.
    * Auf diese Wert wird überprüft ob der {@link Player#getRating()} von <code>player2</code> diesen Wert überschreitet. Wenn dies der fall ist dann verlässt das <code>player1</code> das Spiel.
    * Ansonsten wird auf das Rating von den <code>player1</code> überprüft ob diese die Grenze überschreitet. Wenn ja dann verlässt die <code>player2</code> das Spiel.
    * Außerdem wird das Ganze immer nur auf ein Spieler begrenzt das heißt, dass um herauszufinden zu können welche die optimalste Grenze für ein Spieler ist spiele zu verlassen,
    * um sein Punkte zu maximieren, ohne dass die Punkteanzahl von Verlassen das Spiels von andere Spielern beeinflusst wird, bezieht sich die Grenze immer nur an den bestimmten Spieler die mit den zusätzlich übergebenen <code>playerIndex</code> identifiziert wird.
    * </p>
    * <p>
    * Wenn keine das Spiel verlässt, dann wird zuerst eine Zeit von {@link #roundTimeMean} und {@link #roundTimeSd} berechnet und an die zwei Spieler dazuaddiert.
    * Um zu prüfen, ob das Spiel zu lange dauert, wird auch die {@link Tournament#getGameTime()} übergeben. Ist {@link Player#getTimePlayed()} größer als das Turnierdauer, wird das Spiel abgebrochen und keine bekommt Punkte.
    * Ist das nicht der Fall, dann wird eine zufallszahl zwischen 0 (inclusive) und 1 (exclusive) erstellt und auch die Gewinnwahrscheinlichkeit und die Wahrscheinlichkeit für das Unentschieden berechnet für <code>player1</code>.
    * Wenn der Zufallszahl kleiner ist, als die ausgerechnete Gewinnwahrscheinlichkeit, dann hat <code>player1</code> gewonnen. Wenn diese Zufallszahl größer ist als die Gewinnwahrscheinlichkeit aber kleiner
    * als die Gewinnwahrscheinlichkeit + die Wahrscheinlichkeit für Unentschieden, dann ist das ein Unentschieden. Ansonsten wenn der generierten Zufallszahl größer ist als
    * die Gewinnwahrscheinlichkeit + die Wahrscheinlichkeit für Unentschieden, dann hat <code>player1</code> verloren. Der Spieler der gewinnt bekommt 2 Punkte und der Verlierer 0. Beim Unentschieden bekommen beide Spieler 1 Punkt.
    * </p>
    * Wenn das Spiel nicht verlassen wird, von einem der Spieler, dann wird bei beiden Spieler {@link Player#getGamesPlayed()} um 1 erhöht und {@link Player#getIsInGame()} auf falsch gesetzt.
     * <p>
     * Es wird außerdem noch die Ratings der beiden Spieler angepasst, die abhängig von dem Rating der beiden Spieler abhängt und der Ausgang des Spieles zwischen die zwei Spieler.
     * Wenn eine der Spieler das Spiel verlässt, gilt das als ein verlorenes Spiel für den Spieler und ein gewonnenes Spiel für seine Gegner.
    * @param participants  Teilnehmer Array des Turniers, die aus Player Objekten besteht
    * @param gameTime  dauer des Turniers in Sekunden
    * @param multiplier    eine zahl die mit 25 multipliziert wird und somit die Grenze für die Spieler berechnet
    * @param index     index der spielende Spieler
    * @param playerIndex   index der Spieler der als einzige die Spiele verlässt
    * @see Player#getOpponentIndex()
    * @see Player#addTimePlayed(int)
    * @see Player#addGamesPlayed(int)
    * @see Player#setIsInGame(boolean)
    * @see Player#addPoints(int)
    * @see Random#nextGaussian(double, double)
    * @see Random#nextDouble()
    * @see #playerLeftGame(Player, Player)
    * @see #calculateWinProbability(int)
    * @see #calculateDrawProbability(int)
     * @see #adjustRating(Player, double, int)
    * @see Math#round(double)
    */
    public void playGame(Player[] participants, int gameTime, int multiplier, int index ,int playerIndex){
        Random random = new Random();
        double winProbability;
        double drawProbability;
        double randomValue;
        int roundTime;
        Player player1;
        Player player2;
        double s1 = 0.0;
        double s2 = 0.0;
        int ratingDifference;
        //int realMultiplier1 = multiplier;
        //int realMultiplier2 = multiplier;

        player1 = participants[index];
        player2 = participants[player1.getOpponentIndex()];
        ratingDifference = player1.getRating()-player2.getRating();
        /*System.out.println("Player: " + player1.getIndex() + " Opponent: " + player1.getOpponentIndex());
        System.out.println("Player1 Old Rating: " + player1.getRating());
        System.out.println("Player2 Old Rating: " + player2.getRating());*/
        /*if(player1.getIndex() != playerIndex){
            realMultiplier1 = player1.multiplier;
        }
        if(player2.getIndex() != playerIndex){
            realMultiplier2 = player2.multiplier;
        }*/
        if(player1.getIndex() == playerIndex && player1.getRating()+multiplier*25 < player2.getRating()){
            playerLeftGame(player1,player2);
            s2 = 1.0;
        }
        else if(player2.getIndex() == playerIndex && player2.getRating()+multiplier*25 < player1.getRating()){
            playerLeftGame(player2,player1);
            s1 = 1.0;
        }
        else{
            roundTime = (int)Math.round(random.nextGaussian(roundTimeMean,roundTimeSd));
            while(roundTime <= 0){
                roundTime = (int)Math.round(random.nextGaussian(roundTimeMean,roundTimeSd));
            }
            player1.addTimePlayed(roundTime);
            player2.addTimePlayed(roundTime);
            player1.setIsInGame(false);
            player2.setIsInGame(false);

            if(player1.getTimePlayed() > gameTime){
                return;
            }

            randomValue = random.nextDouble();
            winProbability = calculateWinProbability(ratingDifference);
            drawProbability = calculateDrawProbability(ratingDifference);
               /* System.out.println("RANDOM_ " +randomValue);
                System.out.println("WINPROBABILITY: " + winProbability);
                System.out.println("DRAWPROBABILITY: " + drawProbability);*/
            if(randomValue <= winProbability){
                player1.addPoints(2);
                s1 = 1.0;
                //System.out.println(player1.getId() + " won!");
                // System.out.println("Won!");
            }
            else if(randomValue <= winProbability+drawProbability){
                player1.addPoints(1);
                player2.addPoints(1);
                s1 = 0.5;
                s2 = 0.5;
                //System.out.println(player1.getId() + " Draw!");
            }
            else{
                player2.addPoints(2);
                s2 = 1.0;
                //System.out.println(player1.getId() + " lost!");
            }
            player1.addGamesPlayed(1);
            player2.addGamesPlayed(1);
        }
        adjustRating(player1,s1,ratingDifference);
        adjustRating(player2,s2,-ratingDifference);
        //System.out.println("Player1 new Rating: " + player1.getRating());
        //System.out.println("Player2 new Rating: " + player2.getRating());
    }

    /**
     * Erhöht die Anzahl der verlassenen Spiele der Spieler, der dem Spiel verlässt und addiert Strafzeit zu der Zeit des Spielers, falls zu viele Spiele verlassen wurde von dem Spieler.
     * <p>
     * Spieler der den Spiel verlässt ist <code>player1</code> und sein Gegner die das Spiel nicht verlässt ist <code>player2</code>
     * Diese Funktion wird von {@link #playGame(Player[], int, int, int, int)} aufgerufen, falls ein Spieler das Spiel verlässt.
     * Dann wird der <code>player1</code> zuerst die Attribut {@link Player#getGamesLeft()} um 1 erhöht. Falls die Anzahl der verlassene Spiele von <code>player1</code> größer als 3 wird,
     * dann wird die (Anzahl der Verlassen Spiele - 3) * 30 zu die Attribut {@link Player#getTimePlayed()} von <code>player1</code> dazuaddiert als Strafzeit.
     * Ansonsten wird beide Spieler 10 zu deren Zeit dazuaddiert, um zu simulieren, dass die mindestens 10 Sekunden spielen müssen und erst danach das Spiel verlassen dürfen.
     * Letztendlich wird <code>player2</code> zwei punkte dazuaddiert, weil <code>player1</code> das Spiel verlassen hat und somit <code>player2</code> das Spiel gewonnen hat.
     * Die Attribut {@link Player#getIsInGame()} wird von beiden Spieler auf falsch gesetzt.
     * </p>
     * @param player1   Spieler der dem Spiel verlässt
     * @param player2   Gegner von dem ersten Spieler
     * @see Player#addGamesLeft()
     * @see Player#addGamesPlayed(int)
     * @see Player#addTimePlayed(int)
     * @see Player#addPoints(int)
     * @see Player#setIsInGame(boolean)
     */
    private void playerLeftGame(Player player1, Player player2){
        //System.out.println(player1.getId()+" Left");
        player1.addGamesLeft();
        player1.addGamesPlayed(1);
        if(player1.getGamesLeft() > 3){
            player1.addTimePlayed((player1.getGamesLeft()-3)*30);
        }
        player1.addTimePlayed(10);
        player2.addTimePlayed(10);
        player2.addGamesPlayed(1);
        player2.addPoints(2);
        player1.setIsInGame(false);
        player2.setIsInGame(false);
    }

    /**
     * Die Funktion liefert die Gewinnwahrscheinlichkeit anhand von Bewertungsdifferenz.
     * <p>
     * Wird von {@link #playGame(Player[], int, int, int, int)} aufgerufen als hilfsfunktion, um die Gewinnwahrscheinlichkeit ein Spielers gegenüber sein Gegners zu berechnen.
     * Um die Gewinnwahrscheinlichkeiten durch die Bewertungsdifferenz zu berechnen, wird die Formel von ELO benutzt.
     * Siehe <a href="https://en.wikipedia.org/wiki/Elo_rating_system#Formal_derivation_for_win/draw/loss_games">Formal derivation for win/draw/loss games</a>.
     * </p>
     * @param ratingDifference ist die Differenz der Bewertung zwischen zwei Spieler
     * @return die Gewinnwahrscheinlichkeit bei der bestimmten Bewertungsdifferenz
     */
    private double calculateWinProbability(int ratingDifference){
        return (Math.pow(10,(ratingDifference/400.0)))/(Math.pow(10,(-ratingDifference/400.0))+2+Math.pow(10,(ratingDifference/400.0)));
    }

    /**
     * Die Funktion liefert die Wahrscheinlichkeit für ein Unentschieden anhand von Bewertungsdifferenz.
     * <p>
     * Wird von {@link #playGame(Player[], int, int, int, int)} aufgerufen als hilfsfunktion, um die Wahrscheinlichkeit für ein Untentscieden zwischen zwei Spielers zu berechnen.
     * Um die Wahrscheinlichkeit für einen Unentschieden durch die Bewertungsdifferenz zu berechnen wird der Formel von ELO verwendet.
     * Diese ruft die funktion {@link #calculateWinProbability(int)} zwei mal auf.
     * Einmal wird mit die negative Bewertungsdifferenz um die Niederlagenwahrscheinlichkeit zu berechnen.
     * Siehe <a href="https://en.wikipedia.org/wiki/Elo_rating_system#Formal_derivation_for_win/draw/loss_games">Formal derivation for win/draw/loss games</a>.
     * </p>
     * @param ratingDifference ist die Differenz der Bewertung zwischen zwei Spieler
     * @return die Wahrscheinlichkeit für Unentschieden bei der bestimmten Bewertungsdifferenz
     */
    private double calculateDrawProbability(int ratingDifference){
        return 2*Math.sqrt(calculateWinProbability(ratingDifference)*calculateWinProbability(-ratingDifference));
    }

    /**
     * Passt die {@link Player#getRating()} der über gegebenen Spieler abhängig dem Ergebnis des aktuellen Spiels gegen seinen Gegner und die Ratings von den beiden Spieler.
     * <p>
     * Wird in {@link #playGame(Player[], int, int, int, int)} aufgerufen.
     * Um die Bewertungen der Spieler anzupassen wird die Formel von ELO verwendet.
     * Diese ruft als hilfe {@link #helpMethodForAdjustRating(int)} auf.
     * Siehe <a href="https://en.wikipedia.org/wiki/Elo_rating_system#Formal_derivation_for_win/draw/loss_games">Formal derivation for win/draw/loss games</a>.
     * und Siehe <a https://en.wikipedia.org/wiki/Elo_rating_system#Most_accurate_K-factor">Most accurate K-factor</a>.
     * @param player Spieler dessen Rating angepasst werden soll
     * @param s Faktor die abhängig von dem Ergebnis dem aktuellsten Spiel ist, die der über gegebene Spieler gespielt hat. Es ist 1, wenn der Spieler gewonnen hat, 0.5 bei Unentschieden und 0, wenn der Spieler verloren hat.
     * @param ratingDifference Bewertungsunterschied zwischen der über gegebenen Spieler und sein Gegner
     */
    public void adjustRating(Player player, double s, int ratingDifference){
        int k = 32;
        int playerRating = player.getRating();
        if(playerRating >= 2100 && playerRating <= 2400){
            k = 24;
        }
        else if(playerRating > 2400){
            k = 16;
        }
        //System.out.println("k: " + k + " s: " + s + " ratingDiff: " + ratingDifference);
        double result = (playerRating + k * (s - helpMethodForAdjustRating(ratingDifference)));
        int newRating;
        if(result-(int)result < 0.5){
            newRating = (int) result;
        }else{
            newRating = (int) result + 1;
        }
        player.setRating(newRating);
    }

    /**
     * Hilfsmethode für {@link #adjustRating(Player, double, int)} um ein Teil der formel zu berechnen.
     * @param ratingDifference  Bewertungsunterschied zwischen der über gegebenen Spieler und sein Gegner
     * @return gibt das Ergebnis der Formel zurück
     */
    private double helpMethodForAdjustRating(int ratingDifference){
        return (Math.pow(10,(ratingDifference/400.0)) + 2/2)/(Math.pow(10,(-ratingDifference/400.0))+2+Math.pow(10,(ratingDifference/400.0)));
    }

    //Getters und Setters

    public int getRoundTimeMean(){
        return this.roundTimeMean;
    }

    public int getRoundTimeSd(){
        return this.roundTimeSd;
    }

    public void setRoundTimeMean(int roundTimeMean){
        this.roundTimeMean = roundTimeMean;
    }

    public void setRoundTimeSd(int roundTimeSd){
        this.roundTimeSd = roundTimeSd;
    }
}
