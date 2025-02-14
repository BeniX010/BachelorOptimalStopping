import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        int participantsSize = 10;
        int[] resultArray = new int[participantsSize];
        int[][] participantsPairing;
        Tournament[] tournaments = new Tournament[17];
        Thread[] threads = new Thread[17];
        boolean isTournamentEnd = false;
        Tournament tournament = new Tournament(1200,participantsSize,300,60);
        Player myPlayer = new Player("MyPlayer",1200);
        tournament.fillAndSortParticipantNormalDistribution(1500,150,myPlayer);
        /*fillTest(tournament);
        tournament.initializeCustomList();
        tournament.pairParticipantsNew(0,0);
        tournament.pairParticipantsNew(0,0);
        tournament.pairParticipantsNew(0,0);
        tournament.pairParticipantsNew(0,0);
        tournament.pairParticipantsNew(0,0);*/
        //fillParticipantsWithPredefinedValues(tournament);
        long startTime = System.currentTimeMillis();
        long endTime;
        tournament.printParticipants();
        tournament.startTournamentGameVersion3(1,0);
        /*for(int i = 0; i < tournaments.length; i++){
            tournaments[i] = new Tournament(3600,participantsSize,300,60);
        }
        for(int i = 0; i < tournaments.length; i++){
            fillParticipantsWithPredefinedValues(tournaments[i]);
        }

        double[] averagePoints = new double[17];
        double maxPoints = 0.0;
        int multiplierIndex = 0;
        int playerIndex = 10;

        for(int i = 0; i < threads.length; i++){
            int threadIndex = i;
            threads[i] = new Thread(()->{
                //int[] gamesLeftArray = new int[participantsSize];
                //Player[] participantsResult = new Player[participantsSize];

                    //System.out.println(threadIndex);
                    for(int z = 0; z < 10000; z++){
                        tournaments[threadIndex].startTournamentGameVersion3(threadIndex,playerIndex);
                        /*for(int k = 0; k < participantsSize; k++){
                            gamesLeftArray[tournaments[threadIndex].getParticipants()[k].getIndex()] += tournaments[threadIndex].getParticipants()[k].getGamesLeft();
                        }*/
                       /*tournaments[threadIndex].resetTournament();
                    }
                    averagePoints[threadIndex] = tournaments[threadIndex].getParticipants()[playerIndex].getPoints() / 10_000.0;
                    /*for(int k = 0; k < participantsSize; k++){
                        participantsResult[k] = tournaments[threadIndex].getParticipants()[k];
                    }*/
                    //printTable(participantsResult,10000.0,gamesLeftArray);
                    //System.out.println();
/*
            });
        }

        for(int i = 0; i < threads.length; i++){
            threads[i].start();
        }
        for(int i = 0; i < threads.length; i++){
            threads[i].join();
        }

        for(int z = 0; z < averagePoints.length; z++){
            if(averagePoints[z] > maxPoints){
                maxPoints = averagePoints[z];
                multiplierIndex = z;
            }
        }
        if(multiplierIndex >= 0){
            System.out.println("Maximale Punktanzahl: " + maxPoints + " mit einen Range von: +" + multiplierIndex * 25);
        }else{
            System.out.println("Maximale Punktanzahl: " + maxPoints + " mit einen Range von: " + multiplierIndex * 25);
        }
        */
        endTime = System.currentTimeMillis();
        System.out.println("Runtime in seconds: " + (endTime-startTime));

    /*    int[] gamesLeftArray = new int[participantsSize];
        Player[] participantsResult = new Player[participantsSize];
        double[] averagePoints = new double[17];
        double maxPoints = 0.0;
        int multiplierIndex = 0;
        tournament.printParticipants();
        System.out.println();
/*
            tournament.startTournamentGameVersion3(12);
            for(int k = 0; k < participantsSize; k++){
                participantsResult[k] = tournament.getParticipants()[k];
                gamesLeftArray[k] += tournament.getParticipants()[k].getGamesLeft();
            }
            printTable(participantsResult,1.0,gamesLeftArray);
*/

        /*for(int i = 0; i < 10000; i++){
            tournament.startTournamentGameVersion3(0,0);
            tournament.resetTournament();
        }*/
      /*  tournament.startTournamentGameVersion3(0,0);
        for(int k = 0; k < participantsSize; k++){
            participantsResult[k] = tournament.getParticipants()[k];
            //gamesLeftArray[k] += tournament.getParticipants()[k].getGamesLeft();
        }
        printTable(participantsResult,10000.0,gamesLeftArray);
        /*for(int k = 0; k < participantsSize; k++){
            participantsResult[k] = tournament.getParticipants()[k];
            //gamesLeftArray[k] += tournament.getParticipants()[k].getGamesLeft();
        }
        printTable(participantsResult,10000.0,gamesLeftArray);
        tournament.resetPlayerAttributes();*/
       /*int playerIndex = 7;
        //System.out.println();
        for(int j = 16; j >= 0; j--){
            //System.out.println(j);
            for(int i = 0; i < 10000; i++){
                tournament.startTournamentGameVersion3(j,playerIndex);
                for(int k = 0; k < participantsSize; k++){
                    gamesLeftArray[tournament.getParticipants()[k].getIndex()] += tournament.getParticipants()[k].getGamesLeft();
                }
                tournament.resetTournament();
            }
            averagePoints[j] = tournament.getParticipants()[playerIndex].getPoints() / 10_000.0;
            for(int k = 0; k < participantsSize; k++){
                participantsResult[k] = tournament.getParticipants()[k];
            }
            printTable(participantsResult,10000.0,gamesLeftArray);

            System.out.println();
            Arrays.fill(gamesLeftArray, 0);
            tournament.resetPlayerAttributes();
        }
        for(int i = 0; i < averagePoints.length; i++){
            if(averagePoints[i] > maxPoints){
                maxPoints = averagePoints[i];
                multiplierIndex = i;
            }
        }

        System.out.println("Maximale Punktanzahl: " + maxPoints + " mit einen Range von: +" + multiplierIndex * 25);
*/
    }

    public static void printTable(Player[] participantsResult, double mean, int[] gamesLeft){
        Arrays.sort(participantsResult,Comparator.comparingInt(Player::getPoints).reversed());
        for(int i = 0; i < participantsResult.length; i++){
            System.out.print(participantsResult[i].getId() + " Points: " + participantsResult[i].getPoints()/mean);
            System.out.print(" Games Left: " + gamesLeft[participantsResult[i].getIndex()]);
            System.out.println(" Games Played: " + participantsResult[i].getGamesPlayed() + " Time: " + participantsResult[i].getTimePlayed());
        }
    }

    public static void fillParticipantsWithPredefinedValues(Tournament tournament){
        tournament.getParticipants()[0] = new Player("Player"+9,888);
        tournament.getParticipants()[1] = new Player("Player"+8,1088);
        tournament.getParticipants()[2] = new Player("MyPlayer",1200);
        tournament.getParticipants()[3] = new Player("Player"+10,1223);
        tournament.getParticipants()[4] = new Player("Player"+47,1284);
        tournament.getParticipants()[5] = new Player("Player"+44,1289);
        tournament.getParticipants()[6] = new Player("Player"+30,1311);
        tournament.getParticipants()[7] = new Player("Player"+16,1313);
        tournament.getParticipants()[8] = new Player("Player"+48,1316);
        tournament.getParticipants()[9] = new Player("Player"+21,1348);
        tournament.getParticipants()[10] = new Player("Player"+33,1350);
        tournament.getParticipants()[11] = new Player("Player"+3,1370);
        tournament.getParticipants()[12] = new Player("Player"+39,1377);
        tournament.getParticipants()[13] = new Player("Player"+1,1385);
        tournament.getParticipants()[14] = new Player("Player"+32,1387);
        tournament.getParticipants()[15] = new Player("Player"+17,1400);
        tournament.getParticipants()[16] = new Player("Player"+42,1408);
        tournament.getParticipants()[17] = new Player("Player"+2,1437);
        tournament.getParticipants()[18] = new Player("Player"+6,1444);
        tournament.getParticipants()[19] = new Player("Player"+43,1444);
        tournament.getParticipants()[20] = new Player("Player"+22,1448);
        tournament.getParticipants()[21] = new Player("Player"+26,1455);
        tournament.getParticipants()[22] = new Player("Player"+36,1464);
        tournament.getParticipants()[23] = new Player("Player"+37,1478);
        tournament.getParticipants()[24] = new Player("Player"+11,1482);
        tournament.getParticipants()[25] = new Player("Player"+20,1482);
        tournament.getParticipants()[26] = new Player("Player"+25,1486);
        tournament.getParticipants()[27] = new Player("Player"+24,1505);
        tournament.getParticipants()[28] = new Player("Player"+28,1510);
        tournament.getParticipants()[29] = new Player("Player"+15,1516);
        tournament.getParticipants()[30] = new Player("Player"+41,1517);
        tournament.getParticipants()[31] = new Player("Player"+0,1519);
        tournament.getParticipants()[32] = new Player("Player"+38,1535);
        tournament.getParticipants()[33] = new Player("Player"+5,1541);
        tournament.getParticipants()[34] = new Player("Player"+27,1560);
        tournament.getParticipants()[35] = new Player("Player"+19,1563);
        tournament.getParticipants()[36] = new Player("Player"+34,1563);
        tournament.getParticipants()[37] = new Player("Player"+29,1566);
        tournament.getParticipants()[38] = new Player("Player"+40,1569);
        tournament.getParticipants()[39] = new Player("Player"+7,1571);
        tournament.getParticipants()[40] = new Player("Player"+23,1579);
        tournament.getParticipants()[41] = new Player("Player"+4,1580);
        tournament.getParticipants()[42] = new Player("Player"+13,1590);
        tournament.getParticipants()[43] = new Player("Player"+14,1593);
        tournament.getParticipants()[44] = new Player("Player"+18,1633);
        tournament.getParticipants()[45] = new Player("Player"+46,1639);
        tournament.getParticipants()[46] = new Player("Player"+45,1646);
        tournament.getParticipants()[47] = new Player("Player"+31,1658);
        tournament.getParticipants()[48] = new Player("Player"+35,1713);
        tournament.getParticipants()[49] = new Player("Player"+12,1741);

        for(int i = 0; i < tournament.getParticipants().length; i++){
            tournament.getParticipants()[i].setIndex(i);
            tournament.getParticipantsRating()[i] = tournament.getParticipants()[i].getRating();
            tournament.getParticipantsSortedWithTime()[i] = tournament.getParticipants()[i];
        }
    }

    public static void fillTest(Tournament tournament){
        tournament.getParticipants()[0] = new Player("Player"+9,888);
        tournament.getParticipants()[1] = new Player("Player"+8,1088);
        tournament.getParticipants()[2] = new Player("MyPlayer",1200);
        tournament.getParticipants()[3] = new Player("Player"+10,1223);
        tournament.getParticipants()[4] = new Player("Player"+47,1284);
        tournament.getParticipants()[5] = new Player("Player"+44,1289);
        tournament.getParticipants()[6] = new Player("Player"+30,1311);
        tournament.getParticipants()[7] = new Player("Player"+16,1313);
        tournament.getParticipants()[8] = new Player("Player"+48,1316);
        tournament.getParticipants()[9] = new Player("Player"+21,1348);
        tournament.getParticipants()[1].setTimePlayed(1200);
        tournament.getParticipants()[2].setTimePlayed(1200);
        tournament.getParticipants()[3].setTimePlayed(1200);
        tournament.getParticipants()[5].setTimePlayed(1200);
        tournament.getParticipants()[6].setTimePlayed(1200);
        tournament.getParticipants()[7].setTimePlayed(1200);
        tournament.getParticipants()[8].setTimePlayed(1200);

        tournament.getParticipants()[0].setOpponentIndex(4);
        tournament.getParticipants()[4].setOpponentIndex(9);
        tournament.getParticipants()[9].setOpponentIndex(8);

        tournament.getParticipants()[0].setTimePlayed(1180);
        tournament.getParticipants()[4].setTimePlayed(1190);
        tournament.getParticipants()[9].setTimePlayed(1170);

        for(int i = 0; i < tournament.getParticipants().length; i++){
            tournament.getParticipants()[i].setIndex(i);
            tournament.getParticipantsRating()[i] = tournament.getParticipants()[i].getRating();
            tournament.getParticipantsSortedWithTime()[i] = tournament.getParticipants()[i];
        }
        Arrays.sort(tournament.getParticipantsSortedWithTime(), Comparator.comparingInt(Player::getTimePlayed));
    }

}