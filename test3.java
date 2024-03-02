import java.util.*;
public class MyClass {
                    static int board[][] ={{0,0,0,0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,2,1,0,0,0,0,0},
                                            {0,0,0,0,0,1,2,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0,0,0,0},
                                            {0,0,0,0,0,0,0,0,0,0,0,0}};
    static byte playerColor = 2;
    static byte opponentColor =1;
                        
    public static void main(String args[]) {

                        

        List<Coordinate> moves = new ArrayList<>();
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {
                if (isValidMove(i, j, playerColor)) {
                    moves.add(new Coordinate(i, j));
                }
            }
        }

        // System.out.println("Play Moves Get 2 Exec:" + exectimeD);
        for(Coordinate c:moves) {
            board[c.first][c.second] = 99;
        }
        for(int m=0;m<12;m++) {
        for(int n=0;n<12;n++) {
            System.out.print(board[m][n]+" ");
        }
        System.out.println();
        }

    
    }
    
    public static boolean isValidMove(byte i, byte j, byte playerColor) {

        if (board[i][j] != 0) {
            return false;
        }
        // From this position traverse in all 8 directions to check if there is a valid
        // situation from this position as the move
        // Up
        byte k = (byte) (i - 1);
        byte countOpponent = 0;
        while (k >= 0 && board[k][j] != 0) {
            if (board[k][j] == opponentColor) {
                countOpponent++;
            } else if (board[k][j] == playerColor) {
                if (countOpponent > 0)
                    return true;
                break;
            }
            k -= 1;
        }

        // Down
        k = (byte) (i + 1);
        countOpponent = 0;
        while (k < 12 && board[k][j] != 0) {
            if (board[k][j] == opponentColor) {
                countOpponent++;
            } else if (board[k][j] == playerColor) {
                if (countOpponent > 0)
                    return true;
                break;
            }
            k += 1;
        }

        // Left
        byte l = (byte) (j - 1);
        countOpponent = 0;
        while (l >= 0 && board[i][l] != 0) {
            if (board[i][l] == opponentColor) {
                countOpponent++;
            } else if (board[i][l] == playerColor) {
                if (countOpponent > 0)
                    return true;
                break;
            }
            l -= 1;
        }

        // Right
        l = (byte) (j + 1);
        countOpponent = 0;
        while (l < 12 && board[i][l] != 0) {
            if (board[i][l] == opponentColor) {
                countOpponent++;
            } else if (board[i][l] == playerColor) {
                if (countOpponent > 0)
                    return true;
                break;
            }
            l += 1;
        }

        // Top right diagonal
        k = (byte) (i - 1);
        l = (byte) (j + 1);
        countOpponent = 0;
        while (k >= 0 && l < 12 && board[k][l] != 0) {
            if (board[k][l] == opponentColor) {
                countOpponent++;
            } else if (board[k][l] == playerColor) {
                if (countOpponent > 0)
                    return true;
                break;
            }
            k -= 1;
            l += 1;
        }

        // Bottom right diagonal
        k = (byte) (i + 1);
        l = (byte) (j + 1);
        countOpponent = 0;
        while (k < 12 && l < 12 && board[k][l] != 0) {
            if (board[k][l] == opponentColor) {
                countOpponent++;
            } else if (board[k][l] == playerColor) {
                if (countOpponent > 0)
                    return true;
                break;
            }
            k += 1;
            l += 1;
        }

        // Bottom left diagonal
        k = (byte) (i + 1);
        l = (byte) (j - 1);
        countOpponent = 0;
        while (k < 12 && l >= 0 && board[k][l] != 0) {
            if (board[k][l] == opponentColor) {
                countOpponent++;
            } else if (board[k][l] == playerColor) {
                if (countOpponent > 0)
                    return true;
                break;
            }
            k += 1;
            l -= 1;
        }

        // Top left diagonal
        k = (byte) (i - 1);
        l = (byte) (j - 1);
        countOpponent = 0;
        while (k >= 0 && l >= 0 && board[k][l] != 0) {
            if (board[k][l] == opponentColor) {
                countOpponent++;
            } else if (board[k][l] == playerColor) {
                if (countOpponent > 0)
                    return true;
                break;
            }
            k -= 1;
            l -= 1;
        }

        return false;
    }
}

class Coordinate {
    public byte first, second;

    public Coordinate(byte first, byte second) {
        this.first = first;
        this.second = second;
    }
}