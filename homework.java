import java.io.*;
import java.util.*;

public class homework {
    public static byte globalPlayerColor = 0;

    public static void main(String args[]) {
        byte playerColor;
        Coordinate result = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String color = reader.readLine();
            if (color.charAt(0) == 'X') {
                playerColor = 2;
            } else {
                playerColor = 1;
            }
            globalPlayerColor = playerColor;
            String time = reader.readLine();
            String[] times = time.split(" ");
            float myTime = Float.parseFloat(times[0]);
            float opponentTime = Float.parseFloat(times[1]);
            Board b = new Board(reader);
            // if(myTime>250) {
            // result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 11, true);
            // } else if(myTime>200) {
            // result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 9, true);
            // } else if(myTime>100) {
            // result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 8, true);
            // } else if(myTime>50) {
            // result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 7, true);
            // } else if(myTime>0) {
            // result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 5, true);
            // }
            result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE, Integer.MAX_VALUE, (byte) 5);

        } catch (Exception e) {
            System.out.println("Error Reading Input");
            e.printStackTrace(System.out);
        }
        try {
            // Writing output.txt
            FileWriter file = new FileWriter("output.txt");
            BufferedWriter writer = new BufferedWriter(file);
            String output = null;
            output = ((char) ('a' + result.second) + "" + (result.first + 1));
            writer.write(output);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to output" + e.toString());
            e.printStackTrace(System.out);
        }
    }

    public static byte[][] copy2darray(byte[][] input) {
        byte[][] copy = new byte[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                copy[i][j] = input[i][j];
            }
        }
        return copy;
    }

    public static byte opponentColor(byte playerColor) {
        switch (playerColor) {
            case 1:
                return 2;
            default:
                return 1;
        }
    }
}

class MM {
    public static Coordinate alphaBetaSearch(Board b, byte playerColor, int alpha, int beta, byte depth) {
        List<Coordinate> children = b.generateValidMoves(playerColor);
        Coordinate bestChild = null;
        int bestChildUtility = Integer.MIN_VALUE;
        for (Coordinate c : children) {
            Board newBoard = b.playMoveGetNewBoard(c.first, c.second, playerColor);
            int childUtility = miniMax(newBoard, homework.opponentColor(playerColor), alpha, beta,
                    (byte) (depth - 1), false);

            if (childUtility > bestChildUtility) {
                bestChildUtility = childUtility;
                bestChild = c;
                alpha = Math.max(alpha, childUtility);
            }

            if (beta <= alpha) {
                break;
            }
        }
        return bestChild;
    }

    public static int miniMax(Board b, byte playerColor, int alpha, int beta, byte depth,
            boolean maximizingPlayer) {
        if (depth == 0) {
            return b.utilityCornerEvaluation(homework.globalPlayerColor)
                    + b.utilityFrontierDiscs(homework.globalPlayerColor)
                    + b.utilityCornerCloseness(homework.globalPlayerColor);
        }
        List<Coordinate> children = b.generateValidMoves(playerColor);
        if (children.size() == 0) {
            return b.utilityCornerEvaluation(homework.globalPlayerColor)
                    + b.utilityFrontierDiscs(homework.globalPlayerColor)
                    + b.utilityCornerCloseness(homework.globalPlayerColor);
        }
        int bestChildUtility = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Coordinate c : children) {
            Board newBoard = b.playMoveGetNewBoard(c.first, c.second, playerColor);
            int childUtility = miniMax(newBoard, homework.opponentColor(playerColor), alpha, beta,
                    (byte) (depth - 1), !maximizingPlayer);
            if (maximizingPlayer) {
                if (childUtility > bestChildUtility) {
                    bestChildUtility = childUtility;
                    alpha = Math.max(alpha, childUtility);
                }
            } else {
                if (childUtility < bestChildUtility) {
                    bestChildUtility = childUtility;
                    beta = Math.min(beta, childUtility);
                }
            }
            if (beta <= alpha) {
                break;
            }
        }

        return bestChildUtility;
    }
}

// class UtilityObject {
// int utility;
// Coordinate action;

// public UtilityObject(int u, Coordinate a) {
// utility = u;
// action = a;
// }
// }

class Board {
    byte[][] board;
    public static final int[][] direction = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 1 }, { 1, 1 }, { 1, -1 }, { -1, -1 } };

    public Board() {
    }

    public Board(Board b) {
        this.board = homework.copy2darray(b.board);
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

    public Board playMoveGetNewBoard(byte i, byte j, byte playerColor) {
        Board b = new Board();
        b.board = homework.copy2darray(this.board);
        b.board[i][j] = playerColor;
        // From this position traverse in all 8 directions to update all the hit pieces
        // of the opponent
        for (int m = 0; m < direction.length; m++) {
            byte k = (byte) (i + direction[m][0]);
            byte l = (byte) (j + direction[m][1]);
            while (k >= 0 && k < 12 && l >= 0 && l < 12 && b.board[k][l] == homework.opponentColor(playerColor)) {
                k += direction[m][0];
                l += direction[m][1];
            }
            if (k >= 0 && k < 12 && l >= 0 && l < 12 && b.board[k][l] == playerColor) {
                // Found a black on border, need to flip opponent pieces till that position
                k = (byte) (i + direction[m][0]);
                l = (byte) (j + direction[m][1]);
                while (b.board[k][l] == homework.opponentColor(playerColor)) {
                    b.board[k][l] = playerColor;
                    k += direction[m][0];
                    l += direction[m][1];
                }
            }
        }

        return b;
    }

    public boolean isFinalState() {
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Coordinate> generateValidMoves(byte playerColor) {
        long start = System.nanoTime();
        List<Coordinate> moves = new ArrayList<>(20);
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {
                if (isValidMove(i, j, playerColor)) {
                    moves.add(new Coordinate(i, j));
                }
            }
        }
        long exectime = System.nanoTime() - start;
        double exectimeD = (double) exectime / 1000000000d;
        // System.out.println("Play Moves Get 2 Exec:" + exectimeD);
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
            // Top right diagonal
            byte k = (byte) (i + direction[m][0]);
            byte l = (byte) (j + direction[m][1]);
            byte countOpponent = 0;
            while (k >= 0 && k < 12 && l >= 0 && l < 12 && board[k][l] != 0) {
                if (board[k][l] == homework.opponentColor(playerColor)) {
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

    public int countPieces(byte playerColor) {
        int score = 0;
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {
                if (board[i][j] == playerColor) {
                    score++;
                }
            }
        }
        return score;
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

    public int openCloseEvaluation(byte playerColor) {
        // Check number of closed for opponent and increment the score based on that

        // Horizontal
        int score = 0;
        final int CORNER_PENALTY = 0;
        final int OPPONENT_CLOSE_WEIGHT = 2;
        final int OPPONENT_OPEN_WEIGHT = 1;
        for (byte i = 0; i < 12; i++) {
            byte startl = 0;
            while (startl < 12) {
                byte l = startl;
                byte startOpp = 0;
                byte endOpp = 0;
                while (l < 12 && board[i][l] != homework.opponentColor(playerColor)) {
                    l++;
                }
                startOpp = l;
                while (l < 12 && board[i][l] == homework.opponentColor(playerColor)) {
                    l++;
                }
                endOpp = l;
                startl = l;

                // Found opponent piece
                if (startOpp < 12) {
                    int corner = 0;
                    int playerClose = 0;
                    int emptyClose = 0;
                    if (startOpp == 0) {
                        corner++;
                    } else if (board[i][startOpp - 1] == 0) {
                        emptyClose++;
                    } else if (board[i][startOpp - 1] == playerColor) {
                        playerClose++;
                    }
                    if (endOpp == 12) {
                        corner++;
                    } else if (board[i][endOpp] == 0) {
                        emptyClose++;
                    } else if (board[i][endOpp] == playerColor) {
                        playerClose++;
                    }
                    score += CORNER_PENALTY * corner;
                    if (playerClose == 1 && emptyClose == 1) {
                        score += OPPONENT_CLOSE_WEIGHT * (endOpp - startOpp);
                    }
                    if (emptyClose == 2) {
                        score += 1;
                    }
                }
            }
        }

        // Vertical
        for (byte j = 0; j < 12; j++) {
            byte startk = 0;
            while (startk < 12) {
                byte k = startk;
                byte startOpp = 0;
                byte endOpp = 0;
                while (k < 12 && board[k][j] != homework.opponentColor(playerColor)) {
                    k++;
                }
                startOpp = k;
                while (k < 12 && board[k][j] == homework.opponentColor(playerColor)) {
                    k++;
                }
                endOpp = k;
                startk = k;

                // Found opponent piece
                if (startOpp < 12) {
                    int corner = 0;
                    int playerClose = 0;
                    int emptyClose = 0;
                    if (startOpp == 0) {
                        corner++;
                    } else if (board[startOpp - 1][j] == 0) {
                        emptyClose++;
                    } else if (board[startOpp - 1][j] == playerColor) {
                        playerClose++;
                    }
                    if (endOpp == 12) {
                        corner++;
                    } else if (board[endOpp][j] == 0) {
                        emptyClose++;
                    } else if (board[endOpp][j] == playerColor) {
                        playerClose++;
                    }
                    score += CORNER_PENALTY * corner;
                    if (playerClose == 1 && emptyClose == 1) {
                        score += OPPONENT_CLOSE_WEIGHT * (endOpp - startOpp);
                    }
                    if (emptyClose == 2) {
                        score += OPPONENT_OPEN_WEIGHT;
                    }
                }
            }
        }

        // TopLeft to Middle diagonal
        for (byte i = 0; i < 12; i++) {
            byte currentk = i;
            byte currentl = 0;
            while (currentk >= 0) {
                byte k = currentk;
                byte l = currentl;
                byte startOppk = 0;
                byte startOppl = 0;
                byte endOppk = 0;
                byte endOppl = 0;
                while (k >= 0 && board[k][l] != homework.opponentColor(playerColor)) {
                    k--;
                    l++;
                }
                startOppk = k;
                startOppl = l;
                while (k >= 0 && board[k][l] == homework.opponentColor(playerColor)) {
                    k--;
                    l++;
                }
                endOppk = k;
                endOppl = l;
                currentk = k;
                currentl = l;

                // Found opponent piece
                if (startOppk >= 0) {
                    int corner = 0;
                    int playerClose = 0;
                    int emptyClose = 0;
                    if (startOppk == i) {
                        corner++;
                    } else if (board[startOppk + 1][startOppl - 1] == 0) {
                        emptyClose++;
                    } else if (board[startOppk + 1][startOppl - 1] == playerColor) {
                        playerClose++;
                    }
                    if (endOppk == -1) {
                        corner++;
                    } else if (board[endOppk][endOppl] == 0) {
                        emptyClose++;
                    } else if (board[endOppk][endOppl] == playerColor) {
                        playerClose++;
                    }
                    score += CORNER_PENALTY * corner;
                    if (playerClose == 1 && emptyClose == 1) {
                        score += OPPONENT_CLOSE_WEIGHT * (endOppl - startOppl);
                    }
                    if (emptyClose == 2) {
                        score += OPPONENT_OPEN_WEIGHT;
                    }
                }
            }
        }

        // Middle to BottomRight diagonal
        for (byte j = 1; j < 12; j++) {
            byte currentk = 11;
            byte currentl = j;
            while (currentl < 12) {
                byte k = currentk;
                byte l = currentl;
                byte startOppk = 0;
                byte startOppl = 0;
                byte endOppk = 0;
                byte endOppl = 0;
                while (l < 12 && board[k][l] != homework.opponentColor(playerColor)) {
                    k--;
                    l++;
                }
                startOppk = k;
                startOppl = l;
                while (l < 12 && board[k][l] == homework.opponentColor(playerColor)) {
                    k--;
                    l++;
                }
                endOppk = k;
                endOppl = l;
                currentk = k;
                currentl = l;

                // Found opponent piece
                if (startOppl < 12) {
                    int corner = 0;
                    int playerClose = 0;
                    int emptyClose = 0;
                    if (startOppl == j) {
                        corner++;
                    } else if (board[startOppk + 1][startOppl - 1] == 0) {
                        emptyClose++;
                    } else if (board[startOppk + 1][startOppl - 1] == playerColor) {
                        playerClose++;
                    }
                    if (endOppl == 12) {
                        corner++;
                    } else if (board[endOppk][endOppl] == 0) {
                        emptyClose++;
                    } else if (board[endOppk][endOppl] == playerColor) {
                        playerClose++;
                    }
                    score += CORNER_PENALTY * corner;
                    if (playerClose == 1 && emptyClose == 1) {
                        score += OPPONENT_CLOSE_WEIGHT * (endOppl - startOppl);
                    }
                    if (emptyClose == 2) {
                        score += OPPONENT_OPEN_WEIGHT;
                    }
                }
            }
        }
        // TopRight to Middle diagonal
        for (byte i = 0; i < 12; i++) {
            byte currentk = i;
            byte currentl = 11;
            while (currentk >= 0) {
                byte k = currentk;
                byte l = currentl;
                byte startOppk = 0;
                byte startOppl = 0;
                byte endOppk = 0;
                byte endOppl = 0;
                while (k >= 0 && board[k][l] != homework.opponentColor(playerColor)) {
                    k--;
                    l--;
                }
                startOppk = k;
                startOppl = l;
                while (k >= 0 && board[k][l] == homework.opponentColor(playerColor)) {
                    k--;
                    l--;
                }
                endOppk = k;
                endOppl = l;
                currentk = k;
                currentl = l;

                // Found opponent piece
                if (startOppk >= 0) {
                    int corner = 0;
                    int playerClose = 0;
                    int emptyClose = 0;
                    if (startOppk == i) {
                        corner++;
                    } else if (board[startOppk + 1][startOppl + 1] == 0) {
                        emptyClose++;
                    } else if (board[startOppk + 1][startOppl + 1] == playerColor) {
                        playerClose++;
                    }
                    if (endOppk == -1) {
                        corner++;
                    } else if (board[endOppk][endOppl] == 0) {
                        emptyClose++;
                    } else if (board[endOppk][endOppl] == playerColor) {
                        playerClose++;
                    }
                    score += CORNER_PENALTY * corner;
                    if (playerClose == 1 && emptyClose == 1) {
                        score += OPPONENT_CLOSE_WEIGHT * (startOppl - endOppl);
                    }
                    if (emptyClose == 2) {
                        score += OPPONENT_OPEN_WEIGHT;
                    }
                }
            }
        }
        // Middle to BottomLeft diagonal
        for (byte j = 10; j >= 0; j--) {
            byte currentk = 11;
            byte currentl = j;
            while (currentl >= 0) {
                byte k = currentk;
                byte l = currentl;
                byte startOppk = 0;
                byte startOppl = 0;
                byte endOppk = 0;
                byte endOppl = 0;
                while (l >= 0 && board[k][l] != homework.opponentColor(playerColor)) {
                    k--;
                    l--;
                }
                startOppk = k;
                startOppl = l;
                while (l >= 0 && board[k][l] == homework.opponentColor(playerColor)) {
                    k--;
                    l--;
                }
                endOppk = k;
                endOppl = l;
                currentk = k;
                currentl = l;

                // Found opponent piece
                if (startOppl >= 0) {
                    int corner = 0;
                    int playerClose = 0;
                    int emptyClose = 0;
                    if (startOppl == j) {
                        corner++;
                    } else if (board[startOppk + 1][startOppl + 1] == 0) {
                        emptyClose++;
                    } else if (board[startOppk + 1][startOppl + 1] == playerColor) {
                        playerClose++;
                    }
                    if (endOppl == -1) {
                        corner++;
                    } else if (board[endOppk][endOppl] == 0) {
                        emptyClose++;
                    } else if (board[endOppk][endOppl] == playerColor) {
                        playerClose++;
                    }
                    score += CORNER_PENALTY * corner;
                    if (playerClose == 1 && emptyClose == 1) {
                        score += OPPONENT_CLOSE_WEIGHT * (startOppl - endOppl);
                    }
                    if (emptyClose == 2) {
                        score += OPPONENT_OPEN_WEIGHT;
                    }
                }
            }
        }
        return score;
    }

    public int utilityCornerCloseness(byte playerColor) {
        int playerScore = 0;
        int opponentScore = 0;
        byte opponentColor = homework.opponentColor(playerColor);
        if (board[0][0] == 0) {
            // Top left corner
            if (board[0][1] == playerColor)
                playerScore--;
            else if (board[0][1] == opponentColor)
                opponentScore--;
            if (board[1][1] == playerColor)
                playerScore--;
            else if (board[1][1] == opponentColor)
                opponentScore--;
            if (board[1][0] == playerColor)
                playerScore--;
            else if (board[1][0] == opponentColor)
                opponentScore--;
        }

        if (board[11][0] == 0) {
            // Bottom left corner
            if (board[11][1] == playerColor)
                playerScore--;
            else if (board[11][1] == opponentColor)
                opponentScore--;
            if (board[10][1] == playerColor)
                playerScore--;
            else if (board[10][1] == opponentColor)
                opponentScore--;
            if (board[10][0] == playerColor)
                playerScore--;
            else if (board[10][0] == opponentColor)
                opponentScore--;
        }
        if (board[0][11] == 0) {
            // Top Right Corner
            if (board[0][10] == playerColor)
                playerScore--;
            else if (board[0][10] == opponentColor)
                opponentScore--;
            if (board[1][10] == playerColor)
                playerScore--;
            else if (board[1][10] == opponentColor)
                opponentScore--;
            if (board[1][11] == playerColor)
                playerScore--;
            else if (board[1][11] == opponentColor)
                opponentScore--;
        }

        if (board[11][11] == 0) {
            // Bottom Right Corner
            if (board[11][10] == playerColor)
                playerScore--;
            else if (board[11][10] == opponentColor)
                opponentScore--;
            if (board[10][10] == playerColor)
                playerScore--;
            else if (board[10][10] == opponentColor)
                opponentScore--;
            if (board[10][11] == playerColor)
                playerScore--;
            else if (board[10][11] == opponentColor)
                opponentScore--;
        }
        return 30 * (playerScore - opponentScore);
    }

    public int utilityCountPieces(byte playerColor) {
        int playerCount = countPieces(playerColor);
        int opponentCount = countPieces(homework.opponentColor(playerColor));
        int result = 4 * (playerCount - opponentCount);
        if (countTotalPieces() < 40) {
            return -result;
        } else {
            return result;
        }
    }

    public int utilityPlayerMobility(byte playerColor) {
        int playerMobility = generateValidMoves(playerColor).size();
        int opponentMobility = generateValidMoves(homework.opponentColor(playerColor)).size();
        return 10 * (playerMobility - opponentMobility);

    }

    public int utilityOpenCloseEvaluation(byte playerColor) {
        int playerScore = openCloseEvaluation(playerColor);
        int opponentScore = openCloseEvaluation(homework.opponentColor(playerColor));
        return 30 * (playerScore - opponentScore);
    }

    public int utilityCornerEvaluation(byte playerColor) {
        int playerScore1 = 0;
        int opponentScore1 = 0;
        byte opponentColor = homework.opponentColor(playerColor);

        if (board[0][0] == playerColor) {
            playerScore1 += 1;
        } else if (board[0][0] == opponentColor) {
            opponentScore1 += 1;
        }
        if (board[11][11] == playerColor) {
            playerScore1 += 1;
        } else if (board[11][11] == opponentColor) {
            opponentScore1 += 1;
        }
        if (board[11][0] == playerColor) {
            playerScore1 += 1;
        } else if (board[11][0] == opponentColor) {
            opponentScore1 += 1;
        }
        if (board[0][11] == playerColor) {
            playerScore1 += 1;
        } else if (board[0][11] == opponentColor) {
            opponentScore1 += 1;
        }

        int playerScore2 = 0;
        int opponentScore2 = 0;

        byte i = 0;
        byte j = 1;
        while (j < 11) {
            if (board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (board[i][j] == opponentColor) {
                opponentScore2 += 1;
            }
            j++;
        }
        i = 1;
        j = 0;
        while (i < 11) {
            if (board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (board[i][j] == opponentColor) {
                opponentScore2 += 1;
            }
            i++;
        }

        i = 11;
        j = 1;
        while (j < 11) {
            if (board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (board[i][j] == opponentColor) {
                opponentScore2 += 1;
            }
            j++;
        }
        i = 1;
        j = 11;
        while (i < 11) {
            if (board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (board[i][j] == opponentColor) {
                opponentScore2 += 1;
            }
            i++;
        }
        int result = 0;
        result += 50 * (playerScore1 - opponentScore1);
        result += 10 * (playerScore2 - opponentScore2);
        return result;
    }

    public int utilityFrontierDiscs(byte playerColor) {
        int playerScore = 0;
        int opponentScore = 0;
        for (byte i = 1; i < 11; i++) {
            for (byte j = 1; j < 11; j++) {
                if (board[i][j] != 0) {
                    if (board[i - 1][j] == 0 || board[i][j - 1] == 0 || board[i - 1][j - 1] == 0 || board[i + 1][j] == 0
                            || board[i][j + 1] == 0 || board[i + 1][j + 1] == 0 || board[i + 1][j - 1] == 0
                            || board[i - 1][j + 1] == 0) {
                        if (board[i][j] == playerColor)
                            playerScore--;
                        else
                            opponentScore--;
                    }
                }
            }
        }
        return 5 * (playerScore - opponentScore);
    }

    public int fastUtility() {
        // Implement this
        return 0;
    }

}

class Coordinate {
    public byte first, second;
    int heuristic = 0;

    public Coordinate(byte first, byte second) {
        this.first = first;
        this.second = second;
    }
}
