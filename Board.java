import java.util.*;
import java.io.*;

public class Board {
    byte[][] board;
    public static final int[][] direction = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 1 }, { 1, 1 }, { 1, -1 },
            { -1, -1 } };

    public Board() {
    }

    public Board(Board b) {
        this.board = Utility.copy2darray(b.board);
    }

    public Board(BufferedReader br) {
        board = new byte[12][12];
        try {
            for (byte i = 0; i < 12; i++) {
                String line = br.readLine();
                for (byte j = 0; j < 12; j++) {
                    if (line.charAt(j) == 'X') {
                        board[i][j] = 2;
                    } else if (line.charAt(j) == 'O') {
                        board[i][j] = 1;
                    } else {
                        board[i][j] = 0;
                    }
                }
            }

            if (br != null) {
                br.close();
            }

        } catch (Exception e) {
            System.out.println("Error Reading Input inside Board class" + e.toString());
        }
    }

    /* Check if the provided board is in start position */
    public boolean startBoard() {
        int countO = 0;
        int countX = 0;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (board[i][j] == 2)
                    countX++;
                else if (board[i][j] == 1)
                    countO++;
            }
        }
        if (countX == countO && countX == 4) {
            if (board[2][2] == 2 && board[2][3] == 1 && board[3][2] == 1 && board[3][3] == 2) {
                if (board[8][8] == 2 && board[8][9] == 1 && board[9][8] == 1 && board[9][9] == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public Board playMoveGetNewBoard(byte i, byte j, byte playerColor) {
        Board b = new Board(this);
        b.board[i][j] = playerColor;
        // From this position traverse in all 8 directions to update all the hit pieces
        // of the opponent
        for (int m = 0; m < direction.length; m++) {
            byte k = (byte) (i + direction[m][0]);
            byte l = (byte) (j + direction[m][1]);
            // Move till there is opponent color piece found
            while (k >= 0 && k < 12 && l >= 0 && l < 12 && b.board[k][l] == Utility.opponentColor(playerColor)) {
                k += direction[m][0];
                l += direction[m][1];
            }
            if (k >= 0 && k < 12 && l >= 0 && l < 12 && b.board[k][l] == playerColor) {
                // Found a black on border, need to flip opponent pieces till that position
                k = (byte) (i + direction[m][0]);
                l = (byte) (j + direction[m][1]);
                while (b.board[k][l] == Utility.opponentColor(playerColor)) {
                    b.board[k][l] = playerColor;
                    k += direction[m][0];
                    l += direction[m][1];
                }
            }
        }

        return b;
    }

    public List<Coordinate> generateValidMoves(byte playerColor) {
        List<Coordinate> moves = new ArrayList<>(40);

        if (isValidMove((byte) 0, (byte) 0, playerColor)) {
            moves.add(new Coordinate((byte) 0, (byte) 0));
        } else if (isValidMove((byte) 0, (byte) 11, playerColor)) {
            moves.add(new Coordinate((byte) 0, (byte) 11));
        } else if (isValidMove((byte) 11, (byte) 0, playerColor)) {
            moves.add(new Coordinate((byte) 11, (byte) 0));
        } else if (isValidMove((byte) 11, (byte) 11, playerColor)) {
            moves.add(new Coordinate((byte) 11, (byte) 11));
        }
        byte i = 0;
        byte j = 1;
        while (j < 11) {
            if (isValidMove(i, j, playerColor)) {
                moves.add(new Coordinate(i, j));
            }
            j++;
        }
        i = 1;
        j = 0;
        while (i < 11) {
            if (isValidMove(i, j, playerColor)) {
                moves.add(new Coordinate(i, j));
            }
            i++;
        }
        i = 11;
        j = 1;
        while (j < 11) {
            if (isValidMove(i, j, playerColor)) {
                moves.add(new Coordinate(i, j));
            }
            j++;
        }
        i = 1;
        j = 11;
        while (i < 11) {
            if (isValidMove(i, j, playerColor)) {
                moves.add(new Coordinate(i, j));
            }
            i++;
        }

        for (i = 1; i < 11; i++) {
            for (j = 1; j < 11; j++) {
                if (isValidMove(i, j, playerColor)) {
                    moves.add(new Coordinate(i, j));
                }
            }
        }
        return moves;
    }

    public boolean isValidMove(byte i, byte j, byte playerColor) {
        if (board[i][j] != 0) {
            return false;
        }
        // From this position traverse in all 8 directions to check if there is a valid
        // situation from this position as the move
        // Up
        for (int m = 0; m < direction.length; m++) {
            byte k = (byte) (i + direction[m][0]);
            byte l = (byte) (j + direction[m][1]);
            byte countOpponent = 0;
            while (k >= 0 && k < 12 && l >= 0 && l < 12 && board[k][l] != 0) {
                if (board[k][l] == Utility.opponentColor(playerColor)) {
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

    public int countTotalPieces() {
        int count = 0;
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {
                if (board[i][j] == 1 || board[i][j] == 2) {
                    count++;
                }
            }
        }
        return count;
    }

}
