import java.io.*;
import java.util.*;

public class homework {
    public static void main(String args[]) {
        byte playerColor;
        // ArrayList<Coordinate> list = new ArrayList<>();
        // Set<Coordinate> locations = null;
        Coordinate c = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String color = reader.readLine();
            if (color.charAt(0) == 'X') {
                playerColor = 2;
            } else {
                playerColor = 1;
            }

            String time = reader.readLine();
            String[] times = time.split(" ");
            float myTime = Float.parseFloat(times[0]);
            float opponentTime = Float.parseFloat(times[1]);
            Board b = new Board(reader);
            long start = System.nanoTime();
            c = MM.alphaBetaSearch(b, playerColor, (byte) 7);
            long exectime = System.nanoTime() - start;
            double exectimeD = (double) exectime / 1000000000d;
            // System.out.println("Minmax Exec:" + exectimeD);
            // locations = b.generateValidMoves(playerColor);
            // for (Coordinate c : locations) {
            // list.add(c);
            // }

        } catch (Exception e) {
            System.out.println("Error Reading Input");
            e.printStackTrace(System.out);
        }
        try {
            // Writing output.txt
            FileWriter file = new FileWriter("output.txt");
            BufferedWriter writer = new BufferedWriter(file);
            String output = null;
            // if (locations.size() == 1) {
            // for (Coordinate p : locations) {
            // output = ((char) ('a' + p.second) + "" + (p.first + 1));
            // }
            // } else {
            // Random r = new Random();
            // byte[] rb = new byte[1];
            // r.nextBytes(rb);
            // while (rb[0] < 0 || rb[0] >= list.size()) {
            // r.nextBytes(rb);
            // }
            // Coordinate generatedLocation = list.get(rb[0]);
            // output = ((char) ('a' + generatedLocation.second) + "" +
            // (generatedLocation.first + 1));
            // }
            output = ((char) ('a' + c.second) + "" + (c.first + 1));
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
        if (playerColor == 1) {
            return 2;
        }
        return 1;
    }
}

class MM {
    public static Coordinate alphaBetaSearch(Board b, byte playerColor, byte depth) {
        UtilityObject uo = maxValue(b, playerColor, depth, -Double.MAX_VALUE, Double.MAX_VALUE);
        return uo.action;
    }

    public static UtilityObject maxValue(Board b, byte playerColor, byte depth, double alpha, double beta) {
        if (b.isFinalState()) {
            return new UtilityObject(b.utility(playerColor), null);
        }
        List<Coordinate> children = b.generateValidMoves2(playerColor);
        if (children.size() == 0 || depth <= 0) {
            return new UtilityObject(
                    0.35d * b.utility4(playerColor) + 0.35d * b.utility5(playerColor) + 0.25d * b.utility2(playerColor)
                            + 0.05d * b.utility(playerColor),
                    null);
        }
        depth--;
        double maxValue = -Double.MAX_VALUE;
        Coordinate bestChild = null;
        UtilityObject result = null;
        for (Coordinate c : children) {
            Board newBoard = b.playMoveGetNewBoard(c.first, c.second, playerColor);
            result = minValue(newBoard, playerColor, depth, alpha, beta);
            if (result.utility > maxValue) {
                maxValue = result.utility;
                bestChild = c;
            }
            if (maxValue >= beta) {
                return new UtilityObject(maxValue, bestChild);
            }
            alpha = Math.max(alpha, maxValue);
        }
        return new UtilityObject(maxValue, bestChild);
    }

    public static UtilityObject minValue(Board b, byte playerColor, byte depth, double alpha, double beta) {
        if (b.isFinalState()) {
            return new UtilityObject(b.utility(playerColor), null);
        }
        List<Coordinate> children = b.generateValidMoves2(homework.opponentColor(playerColor));
        if (children.size() == 0 || depth <= 0) {
            return new UtilityObject(
                    0.35d * b.utility4(playerColor) + 0.35d * b.utility5(playerColor) + 0.25d * b.utility2(playerColor)
                            + 0.05d * b.utility(playerColor),
                    null);
        }
        depth--;
        double minValue = Double.MAX_VALUE;
        Coordinate bestChild = null;
        for (Coordinate c : children) {
            Board newBoard = b.playMoveGetNewBoard(c.first, c.second, homework.opponentColor(playerColor));
            UtilityObject result = maxValue(newBoard, playerColor, depth, alpha, beta);
            if (result.utility < minValue) {
                minValue = result.utility;
                bestChild = c;
            }
            if (minValue <= alpha) {
                return new UtilityObject(minValue, bestChild);
            }
            beta = Math.min(beta, minValue);
        }
        return new UtilityObject(minValue, bestChild);
    }
}

class UtilityObject {
    double utility;
    Coordinate action;

    public UtilityObject(double u, Coordinate a) {
        utility = u;
        action = a;
    }
}

class Board {
    byte[][] board;

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
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error Reading Input inside Board class" + e.toString());
        }
    }

    public Board playMoveGetNewBoard(byte i, byte j, byte playerColor) {
        long start = System.nanoTime();
        Board b = new Board();
        b.board = homework.copy2darray(this.board);
        b.board[i][j] = playerColor;
        // From this position traverse in all 8 directions to update all the hit pieces
        // of the opponent

        // Up
        byte k = (byte) (i - 1);
        byte hitI = i;
        byte hitJ = j;
        while (k >= 0 && board[k][j] != 0) {
            if (board[k][j] == playerColor) {
                hitI = k;
                hitJ = j;
                break;
            }
            k -= 1;
        }

        // Up Update all opponent pieces
        while (hitI <= i) {
            board[hitI][hitJ] = playerColor;
            hitI++;
        }

        // Down
        k = (byte) (i + 1);
        hitI = i;
        hitJ = j;
        while (k < 12 && board[k][j] != 0) {
            if (board[k][j] == playerColor) {
                hitI = k;
                hitJ = j;
                break;
            }
            k += 1;
        }

        // Down Update all opponent pieces
        while (hitI >= i) {
            board[hitI][hitJ] = playerColor;
            hitI--;
        }

        // Left
        byte l = (byte) (j - 1);
        hitI = i;
        hitJ = j;
        while (l >= 0 && board[i][l] != 0) {
            if (board[i][l] == playerColor) {
                hitI = i;
                hitJ = l;
                break;
            }
            l -= 1;
        }

        // Left Update all opponent pieces
        while (hitJ <= j) {
            board[hitI][hitJ] = playerColor;
            hitJ++;
        }

        // Right
        l = (byte) (j + 1);
        hitI = i;
        hitJ = j;
        while (l < 12 && board[i][l] != 0) {
            if (board[i][l] == playerColor) {
                hitI = i;
                hitJ = l;
                break;
            }
            l += 1;
        }

        // Right Update all opponent pieces
        while (hitJ >= j) {
            board[hitI][hitJ] = playerColor;
            hitJ--;
        }

        // Top right diagonal
        k = (byte) (i - 1);
        l = (byte) (j + 1);
        hitI = i;
        hitJ = j;
        while (k >= 0 && l < 12 && board[k][l] != 0) {
            if (board[k][l] == playerColor) {
                hitI = k;
                hitJ = l;
                break;
            }
            k -= 1;
            l += 1;
        }

        // Top Right Update all opponent pieces
        while (hitJ >= j) {
            board[hitI][hitJ] = playerColor;
            hitI++;
            hitJ--;
        }

        // Bottom right diagonal
        k = (byte) (i + 1);
        l = (byte) (j + 1);
        hitI = i;
        hitJ = j;
        while (k < 12 && l < 12 && board[k][l] != 0) {
            if (board[k][l] == playerColor) {
                hitI = k;
                hitJ = l;
                break;
            }
            k += 1;
            l += 1;
        }

        // Bottom Right Update all opponent pieces
        while (hitJ >= j) {
            board[hitI][hitJ] = playerColor;
            hitI--;
            hitJ--;
        }

        // Bottom left diagonal
        k = (byte) (i + 1);
        l = (byte) (j - 1);
        hitI = i;
        hitJ = j;
        while (k < 12 && l >= 0 && board[k][l] != 0) {
            if (board[k][l] == playerColor) {
                hitI = k;
                hitJ = l;
                break;
            }
            k += 1;
            l -= 1;
        }

        // Bottom Left Update all opponent pieces
        while (hitJ <= j) {
            board[hitI][hitJ] = playerColor;
            hitI--;
            hitJ++;
        }

        // Top left diagonal
        k = (byte) (i - 1);
        l = (byte) (j - 1);
        hitI = i;
        hitJ = j;
        while (k >= 0 && l >= 0 && board[k][l] != 0) {
            if (board[k][l] == playerColor) {
                hitI = k;
                hitJ = l;
                break;
            }
            k -= 1;
            l -= 1;
        }

        // Top Left Update all opponent pieces
        while (hitJ <= j) {
            board[hitI][hitJ] = playerColor;
            hitI++;
            hitJ++;
        }

        long exectime = System.nanoTime() - start;
        double exectimeD = (double) exectime / 1000000000d;
        // System.out.println("Play Moves Get Exec:" + exectimeD);

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

    // public Set<Coordinate> generateValidMoves(byte playerColor) {
    // long start = System.nanoTime();
    // Set<Coordinate> locations = new HashSet<>();
    // for (byte i = 0; i < 12; i++) {
    // for (byte j = 0; j < 12; j++) {
    // if (board[i][j] == playerColor) {
    // // From this position traverse in all 8 directions to determine all possible
    // // valid moves linked to this piece
    // // Up
    // byte k = (byte) (i - 1);
    // byte countOpponent = 0;
    // while (k >= 0 && board[k][j] != playerColor) {
    // if (board[k][j] == 0 && countOpponent == 0) {
    // break;
    // } else if (board[k][j] != 0) {
    // countOpponent++;
    // } else {
    // locations.add(new Coordinate(k, j));
    // break;
    // }
    // k -= 1;
    // }

    // // Down
    // k = (byte) (i + 1);
    // countOpponent = 0;
    // while (k < 12 && board[k][j] != playerColor) {
    // if (board[k][j] == 0 && countOpponent == 0) {
    // break;
    // } else if (board[k][j] != 0) {
    // countOpponent++;
    // } else {
    // locations.add(new Coordinate(k, j));
    // break;
    // }
    // k += 1;
    // }
    // // Left
    // byte l = (byte) (j - 1);
    // countOpponent = 0;
    // while (l >= 0 && board[i][l] != playerColor) {
    // if (board[i][l] == 0 && countOpponent == 0) {
    // break;
    // } else if (board[i][l] != 0) {
    // countOpponent++;
    // } else {
    // locations.add(new Coordinate(i, l));
    // break;
    // }
    // l -= 1;
    // }
    // // Right
    // l = (byte) (j + 1);
    // countOpponent = 0;
    // while (l < 12 && board[i][l] != playerColor) {
    // if (board[i][l] == 0 && countOpponent == 0) {
    // break;
    // } else if (board[i][l] != 0) {
    // countOpponent++;
    // } else {
    // locations.add(new Coordinate(i, l));
    // break;
    // }
    // l += 1;
    // }
    // // Top right diagonal
    // k = (byte) (i - 1);
    // l = (byte) (j + 1);
    // countOpponent = 0;
    // while (k >= 0 && l < 12 && board[k][l] != playerColor) {
    // if (board[k][l] == 0 && countOpponent == 0) {
    // break;
    // } else if (board[k][l] != 0) {
    // countOpponent++;
    // } else {
    // locations.add(new Coordinate(k, l));
    // break;
    // }
    // k -= 1;
    // l += 1;
    // }
    // // Bottom right diagonal
    // k = (byte) (i + 1);
    // l = (byte) (j + 1);
    // countOpponent = 0;
    // while (k < 12 && l < 12 && board[k][l] != playerColor) {
    // if (board[k][l] == 0 && countOpponent == 0) {
    // break;
    // } else if (board[k][l] != 0) {
    // countOpponent++;
    // } else {
    // locations.add(new Coordinate(k, l));
    // break;
    // }
    // k += 1;
    // l += 1;
    // }
    // // Bottom left diagonal
    // k = (byte) (i + 1);
    // l = (byte) (j - 1);
    // countOpponent = 0;
    // while (k < 12 && l >= 0 && board[k][l] != playerColor) {
    // if (board[k][l] == 0 && countOpponent == 0) {
    // break;
    // } else if (board[k][l] != 0) {
    // countOpponent++;
    // } else {
    // locations.add(new Coordinate(k, l));
    // break;
    // }
    // k += 1;
    // l -= 1;
    // }

    // // Top left diagonal
    // k = (byte) (i - 1);
    // l = (byte) (j - 1);
    // countOpponent = 0;
    // while (k >= 0 && l >= 0 && board[k][l] != playerColor) {
    // if (board[k][l] == 0 && countOpponent == 0) {
    // break;
    // } else if (board[k][l] != 0) {
    // countOpponent++;
    // } else {
    // locations.add(new Coordinate(k, l));
    // break;
    // }
    // k -= 1;
    // l -= 1;
    // }
    // }
    // }
    // }

    // long exectime = System.nanoTime() - start;
    // double exectimeD = (double) exectime / 1000000000d;
    // // System.out.println("Generate Valid Moves Exec:" + exectimeD);
    // return locations;
    // }

    public List<Coordinate> generateValidMoves2(byte playerColor) {
        long start = System.nanoTime();
        List<Coordinate> moves = new ArrayList<>();
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
        byte k = (byte) (i - 1);
        byte countOpponent = 0;
        while (k >= 0 && board[k][j] != 0) {
            if (board[k][j] == homework.opponentColor(playerColor)) {
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
            if (board[k][j] == homework.opponentColor(playerColor)) {
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
            if (board[i][l] == homework.opponentColor(playerColor)) {
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
            if (board[i][l] == homework.opponentColor(playerColor)) {
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
            if (board[k][l] == homework.opponentColor(playerColor)) {
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
            if (board[k][l] == homework.opponentColor(playerColor)) {
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
            if (board[k][l] == homework.opponentColor(playerColor)) {
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
            if (board[k][l] == homework.opponentColor(playerColor)) {
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

    public double utility(byte playerColor) {
        double playerCount = countPieces(playerColor);
        double opponentCount = countPieces(homework.opponentColor(playerColor));
        byte buffer = 0;
        if (playerColor == 2) {
            buffer = 1;
        } else {
            buffer = -1;
        }
        return 100 * (playerCount - opponentCount + buffer) / (playerCount + opponentCount + Math.abs(buffer));
    }

    public double countPieces(byte playerColor) {
        long start = System.nanoTime();
        double score = 0d;
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {
                if (board[i][j] == playerColor) {
                    score++;
                }
            }
        }
        long exectime = System.nanoTime() - start;
        double exectimeD = (double) exectime / 1000000000d;
        // System.out.println("Utility Exec:" + exectimeD);
        return score;
    }

    public double utility2(byte playerColor) {
        double playerMobility = (double) generateValidMoves2(playerColor).size();
        double opponentMobility = (double) generateValidMoves2(homework.opponentColor(playerColor)).size();
        return 100 * (playerMobility - opponentMobility) / (playerMobility + opponentMobility);
    }

    public double openCloseEvaluation(byte playerColor, float closeWeight) {
        // Check number of closed for opponent and increment the score based on that

        // Horizontal
        double score = 0;
        final float CORNER_PENALTY = -0.0f;
        float OPPONENT_CLOSE_WEIGHT = closeWeight;
        final float OPPONENT_OPEN_WEIGHT = 0.4f;
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
                        score += OPPONENT_OPEN_WEIGHT;
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

    public double utility4(byte playerColor) {
        double playerScore = openCloseEvaluation(playerColor, 1);
        double opponentScore = openCloseEvaluation(homework.opponentColor(playerColor), 1);
        return 100 * (playerScore - opponentScore) / (playerScore + opponentScore);
    }

    public double utility5(byte playerColor) {
        double playerScore = cornerEvaluation(playerColor);
        double opponentScore = cornerEvaluation(homework.opponentColor(playerColor));
        return 100 * (playerScore - opponentScore) / (playerScore + opponentScore);
    }

    public double cornerEvaluation(byte playerColor) {
        // Score all corners
        double score = 0d;
        if (board[0][0] == playerColor) {
            score += 3;
        }
        if (board[11][11] == playerColor) {
            score += 3;
        }
        if (board[11][0] == playerColor) {
            score += 3;
        }
        if (board[0][11] == playerColor) {
            score += 3;
        }

        byte i = 0;
        byte j = 1;
        while (j <= 11) {
            if (board[i][j] == playerColor) {
                score += 1;
            }
            j++;
        }
        i = 1;
        j = 0;
        while (i <= 11) {
            if (board[i][j] == playerColor) {
                score += 1;
            }
            i++;
        }

        i = 11;
        j = 1;
        while (j <= 11) {
            if (board[i][j] == playerColor) {
                score += 1;
            }
            j++;
        }
        i = 0;
        j = 11;
        while (i <= 11) {
            if (board[i][j] == playerColor) {
                score += 1;
            }
            i++;
        }
        return score;
    }

    public int fastUtility() {
        // Implement this
        return 0;
    }

}

class Coordinate {
    public Byte first, second;

    public Coordinate(Byte first, Byte second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Coordinate)) {
            return false;
        }

        Coordinate other = (Coordinate) obj;
        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }
}

// // @Override
// // public int hashCode() {
// // return Objects.hash(first, second);
// // }
// }

// class Pair<T1, T2> {

// public T1 first;
// public T2 second;

// public Pair(T1 first, T2 second) {
// this.first = first;
// this.second = second;
// }

// @Override
// public boolean equals(Object obj) {

// if (obj == this) {
// return true;
// }

// if (!(obj instanceof Pair)) {
// return false;
// }

// Pair<T1, T2> other = (Pair<T1, T2>) obj;

// return Objects.equals(first, other.first) && Objects.equals(second,
// other.second);
// }

// @Override
// public int hashCode() {
// return Objects.hash(first, second);
// }
// }
