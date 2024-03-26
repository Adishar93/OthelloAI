
import java.io.*;
import java.util.*;

public class Refree {
    /*
     * Start state of board
     * ............
     * ............
     * ..XO........
     * ..OX........
     * ............
     * ............
     * ............
     * ............
     * ........XO..
     * ........OX..
     * ............
     * ............
     */

    public final static int O = 1;
    public final static int X = 2;
    static int board[][] = {
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, X, O, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, O, X, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, X, O, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, O, X, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    // When flipPieces is false, Bot1 plays first as 'O' and Bot2 plays second as
    // 'X'
    // When flipPieces is true, Bot2 plays first as 'O' and Bot1 plays second as 'X'

    final static boolean flipPieces = true;
    static float bot1Time = 300;
    static float bot2Time = 300;

    public static void main(String args[]) {

        boolean move1 = false;
        boolean move2 = false;
        float b1 = 0f;
        float b2 = 0f;

        System.out.println("Initial board state:");
        printBoard();

        // while (limitDepth > 0 && bot2Time > 0 && bot1Time > 0)
        while (true) {

            List<Coordinate> movesPlayer1 = generateValidMoves(O);
            if (movesPlayer1.size() > 0) {
                move1 = true;

                if (!flipPieces) {
                    setupIOFile(1, O);
                    long start = System.nanoTime();
                    OthelloAI.main(args);
                    long end = System.nanoTime();
                    System.out.println("Time taken by Bot1: " + (double) (end - start) / 1000000000d + " seconds");
                    System.out.println("Bot1 played a move ->");
                    b1 += (float) (end - start) / 1000000000f;
                    // bot1Time -= (float) (end - start) / 1000000000f;
                } else {
                    setupIOFile(2, O);
                    long start = System.nanoTime();
                    OthelloAI2.main(args);
                    long end = System.nanoTime();
                    System.out.println("Time taken by Bot2: " + (double) (end - start) / 1000000000d + " seconds");
                    System.out.println("Bot2 played a move ->");
                    b2 += (float) (end - start) / 1000000000f;
                    // bot2Time -= (float) (end - start) / 1000000000f;
                }
                boolean valid = readOutputAndUpdateBoard(O, movesPlayer1);
                if (!valid) {
                    System.out.println("Invalid Move!");
                    return;
                }
                printBoard();
            }

            List<Coordinate> movesPlayer2 = generateValidMoves(X);
            if (movesPlayer2.size() > 0) {
                move2 = true;
                if (!flipPieces) {
                    setupIOFile(2, X);
                    long start = System.nanoTime();
                    OthelloAI2.main(args);
                    long end = System.nanoTime();
                    System.out.println("Time taken by Bot2: " + (double) (end - start) / 1000000000d + " seconds");
                    System.out.println("Bot2 played a move ->");
                    b2 += (float) (end - start) / 1000000000f;
                    // bot2Time -= (float) (end - start) / 1000000000f;
                } else {
                    setupIOFile(1, X);
                    long start = System.nanoTime();
                    OthelloAI.main(args);
                    long end = System.nanoTime();
                    System.out.println("Time taken by Bot1: " + (double) (end - start) / 1000000000d + " seconds");
                    System.out.println("Bot1 played a move ->");
                    b1 += (float) (end - start) / 1000000000f;
                    // bot1Time -= (float) (end - start) / 1000000000f;
                }

                boolean valid = readOutputAndUpdateBoard(X, movesPlayer2);
                if (!valid) {
                    System.out.println("Invalid Move!");
                    return;
                }
                printBoard();
            }

            if (!move1 && !move2)
                break;

            move1 = false;
            move2 = false;
            System.out.println("Total Time taken by Bot1: " + b1 + "s Bot2: " + b2 + "s");
        }

        declareWinner();
    }

    public static void declareWinner() {
        int countX = 1;
        int countO = 0;
        for (byte i = 0; i < board.length; i++) {
            for (byte j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1)
                    countO++;
                if (board[i][j] == 2)
                    countX++;
            }
        }
        if (countX == countO)
            System.out.println("Its a draw! X: " + countX + " O: " + countO);
        else if (countX > countO) {
            if (!flipPieces) {
                System.out.println("Bot2 Won!");
            } else {
                System.out.println("Bot1 Won!");
            }
            System.out.println("X is the winner by X: " + countX + " O: " + countO);
        } else if (countX < countO) {
            if (!flipPieces) {
                System.out.println("Bot1 Won!");
            } else {
                System.out.println("Bot2 Won!");
            }
            System.out.println("O is the winner by O: " + countO + " X: " + countX);
        }

    }

    public static List<Coordinate> generateValidMoves(int playerColor) {
        List<Coordinate> moves = new ArrayList<>(20);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (isValidMove(i, j, playerColor)) {
                    moves.add(new Coordinate((byte) i, (byte) j));
                }
            }
        }
        return moves;
    }

    public static boolean isValidMove(int i, int j, int playerColor) {
        if (board[i][j] != 0) {
            return false;
        }
        int[][] direction = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 1 }, { 1, 1 }, { 1, -1 }, { -1, -1 } };
        // From this position traverse in all 8 directions to check if there is a valid
        // situation from this position as the move
        // Up
        for (int m = 0; m < direction.length; m++) {
            // Top right diagonal
            int k = i + direction[m][0];
            int l = j + direction[m][1];
            int countOpponent = 0;
            while (k >= 0 && k < 12 && l >= 0 && l < 12 && board[k][l] != 0) {
                if (board[k][l] == Utility.opponentColor((byte) playerColor)) {
                    countOpponent++;
                } else if (board[k][l] == playerColor) {
                    if (countOpponent > 0)
                        return true;
                    break;
                }
                k += direction[m][0];
                l += direction[m][1];
            }
        }
        return false;
    }

    public static void setupIOFile(int botId, int playerColor) {
        try {
            // Writing input.txt
            File f = new File("input.txt");
            FileWriter file = new FileWriter(f);
            BufferedWriter writer = new BufferedWriter(file);
            StringBuilder output = new StringBuilder("");

            if (playerColor == O)
                output.append("O");
            else
                output.append("X");

            output.append("\n");
            if (botId == 1) {
                output.append(bot1Time + " " + bot2Time);
            } else {
                output.append(bot2Time + " " + bot1Time);
            }

            output.append("\n");
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == 0) {
                        output.append('.');
                    } else if (board[i][j] == 1) {
                        output.append("O");
                    } else {
                        output.append("X");
                    }
                }
                output.append("\n");
            }
            writer.write(output.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to input.txt" + e.toString());
            e.printStackTrace(System.out);
        }
    }

    public static boolean readOutputAndUpdateBoard(int playerColor, List<Coordinate> movesList) {
        boolean validMove = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
            String move = reader.readLine();

            if (reader != null)
                reader.close();

            char first = move.charAt(0);
            String second = move.substring(1);
            if (playerColor == 1) {
                System.out.print("O ");
            } else {
                System.out.print("X ");
            }
            System.out.println("chose move: " + first + "," + second);

            int y = first - 'a';
            int x = Integer.parseInt(second) - 1;
            for (Coordinate c : movesList) {
                if (c.first == x && c.second == y) {
                    // Found Valid Move
                    validMove = true;
                    break;
                }
            }
            if (!validMove)
                return false;

            playMoveUpdateBoard(x, y, playerColor);

        } catch (Exception e) {
            System.out.println("Error Reading Output.txt");
            e.printStackTrace(System.out);
        }

        return true;
    }

    public static void playMoveUpdateBoard(int i, int j, int playerColor) {
        int[][] direction = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 1 }, { 1, 1 }, { 1, -1 }, { -1, -1 } };
        board[i][j] = playerColor;
        // From this position traverse in all 8 directions to update all the hit pieces
        // of the opponent
        for (int m = 0; m < direction.length; m++) {
            int k = i + direction[m][0];
            int l = j + direction[m][1];
            while (k >= 0 && k < 12 && l >= 0 && l < 12 && board[k][l] == Utility.opponentColor((byte) playerColor)) {
                k += direction[m][0];
                l += direction[m][1];
            }
            if (k >= 0 && k < 12 && l >= 0 && l < 12 && board[k][l] == playerColor) {
                // Found a black on border, need to flip opponent pieces till that position
                k = i + direction[m][0];
                l = j + direction[m][1];
                while (board[k][l] == Utility.opponentColor((byte) playerColor)) {
                    board[k][l] = playerColor;
                    k += direction[m][0];
                    l += direction[m][1];
                }
            }
        }
    }

    public static void printBoard() {
        System.out.println();
        System.out.print("  ");
        for (char i = 0; i < 12; i++) {
            System.out.print(" "+(char) ('a' + i));
        }
        System.out.println();
        for (int m = 0; m < 12; m++) {
            if (m + 1 < 10)
                System.out.print(m + 1+"  ");
            else
                System.out.print(m + 1+" ");
            for (int n = 0; n < 12; n++) {
                if (board[m][n] == 0)
                    System.out.print(". ");
                else if (board[m][n] == O)
                    System.out.print("O ");
                else if (board[m][n] == X)
                    System.out.print("X ");

            }
            System.out.println();
        }
        System.out.println();
    }
}