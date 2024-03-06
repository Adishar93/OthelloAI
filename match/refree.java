 import java.io.*;
import java.util.*;

public class refree {
    static int board[][] = {
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    // When flipPieces is false, homework plays first 'O' and homework2 plays second
    // 'X'
    // When flipPieces is true, homework2 plays first 'O' and homework plays second
    // 'X'
    final static boolean flipPieces = true;

    public static void main(String args[]) {

        boolean move1 = false;
        boolean move2 = false;
        int limitDepth = 5;
        double hw = 0d;
        double hw2 = 0d;

        System.out.println("Initial board state:");
        printBoard();

        while (limitDepth > 0) {

            List<Coordinate> movesPlayer1 = generateValidMoves(1);
            if (movesPlayer1.size() > 0) {
                move1 = true;
                setupIOFile(1);
                if (!flipPieces) {
                    long start = System.nanoTime();
                    homework.main(args);
                    long end = System.nanoTime();
                    System.out.println("Time taken by homework: " + (double) (end - start) / 1000000000d + " seconds");
                    hw += (double) (end - start) / 1000000000d;
                } else {
                    long start = System.nanoTime();
                    homework2.main(args);
                    long end = System.nanoTime();
                    System.out.println("Time taken by homework2: " + (double) (end - start) / 1000000000d + " seconds");
                    hw2 += (double) (end - start) / 1000000000d;
                }
                boolean valid = readOutputAndUpdateBoard(1, movesPlayer1);
                if (!valid) {
                    System.out.println("Invalid Move!");
                    return;
                }
                printBoard();
            }

            List<Coordinate> movesPlayer2 = generateValidMoves(2);
            if (movesPlayer2.size() > 0) {
                move2 = true;
                setupIOFile(2);

                if (!flipPieces) {
                    long start = System.nanoTime();
                    homework2.main(args);
                    long end = System.nanoTime();
                    System.out.println("Time taken by homework2: " + (double) (end - start) / 1000000000d + " seconds");
                    hw2 += (double) (end - start) / 1000000000d;
                } else {
                    long start = System.nanoTime();
                    homework.main(args);
                    long end = System.nanoTime();
                    System.out.println("Time taken by homework: " + (double) (end - start) / 1000000000d + " seconds");
                    hw += (double) (end - start) / 1000000000d;
                }

                boolean valid = readOutputAndUpdateBoard(2, movesPlayer2);
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
            System.out.println("Total Time homework: " + hw + " homework2: " + hw2);
            // limitDepth--;
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
                System.out.println("homework2 Won!");
            } else {
                System.out.println("homework Won!");
            }
            System.out.println("X is the winner by X: " + countX + " O: " + countO);
        } else if (countX < countO) {
            if (!flipPieces) {
                System.out.println("homework Won!");
            } else {
                System.out.println("homework2 Won!");
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
                if (board[k][l] == opponentColor(playerColor)) {
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

    public static void setupIOFile(int playerColor) {
        try {
            // Writing output.txt
            FileWriter file = new FileWriter("input.txt");
            BufferedWriter writer = new BufferedWriter(file);
            StringBuilder output = new StringBuilder("");

            if (playerColor == 1)
                output.append("O");
            else
                output.append("X");

            output.append("\n");
            output.append("201 201");
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
                System.out.print(" O ");
            } else {
                System.out.print(" X ");
            }
            System.out.println("Player chose move1: " + first + "," + second);

            int y = first - 'a';
            int x = Integer.parseInt(second) - 1;

            System.out.println("Player chose move: " + x + "," + y);
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
            while (k >= 0 && k < 12 && l >= 0 && l < 12 && board[k][l] == opponentColor(playerColor)) {
                k += direction[m][0];
                l += direction[m][1];
            }
            if (k >= 0 && k < 12 && l >= 0 && l < 12 && board[k][l] == playerColor) {
                // Found a black on border, need to flip opponent pieces till that position
                k = i + direction[m][0];
                l = j + direction[m][1];
                while (board[k][l] == opponentColor(playerColor)) {
                    board[k][l] = playerColor;
                    k += direction[m][0];
                    l += direction[m][1];
                }
            }
        }
    }

    public static int opponentColor(int playerColor) {
        if (playerColor == 1) {
            return 2;
        }
        return 1;
    }

    public static void printBoard() {
        for (int m = 0; m < 12; m++) {
            for (int n = 0; n < 12; n++) {
                if (board[m][n] == 0)
                    System.out.print(". ");
                else if (board[m][n] == 1)
                    System.out.print("O ");
                else if (board[m][n] == 2)
                    System.out.print("X ");

            }
            System.out.println();
        }
    }
}