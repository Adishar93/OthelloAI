
import java.io.*;
import java.util.*;

public class OthelloAI {
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
            OthelloAI.globalPlayerColor = playerColor;
            String time = reader.readLine();
            String[] times = time.split(" ");
            float myTime = Float.parseFloat(times[0]);
            float opponentTime = Float.parseFloat(times[1]);
            Board b = new Board(reader);

            // Check if board is a specific format
            if (myTime > 199 && OthelloAI.globalPlayerColor == 1 && b.startBoard()) {
                result = new Coordinate((byte) 1, (byte) 2);
            } else {
                if (myTime > 200) {
                result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
                Integer.MAX_VALUE, (byte) 9);
                } else if (myTime > 150) {
                result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
                Integer.MAX_VALUE, (byte) 8);
                } else if (myTime > 110) {
                result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
                Integer.MAX_VALUE, (byte) 7);
                } else if (myTime > 60) {
                result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
                Integer.MAX_VALUE, (byte) 6);
                } else if (myTime > 5) {
                result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
                Integer.MAX_VALUE, (byte) 5);
                } else if (myTime > 2) {
                result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
                Integer.MAX_VALUE, (byte) 3);
                } else if (myTime > 0.5f) {
                result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
                Integer.MAX_VALUE, (byte) 1);
                } else {
                result = b.generateValidMoves(playerColor).get(0);
                }
                // result = MM.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
                //         Integer.MAX_VALUE, (byte) 15);
                // System.out.println("Nodes visited hw: " + MM.nodesVisited);
            }
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
        byte[][] copy = new byte[input.length][];
        for (byte i = 0; i < input.length; i++) {
            byte[] inputRow = input[i];
            int rowLen = inputRow.length;
            copy[i] = new byte[rowLen];
            System.arraycopy(inputRow, 0, copy[i], 0, rowLen);
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
    public static long nodesVisited = 0;
    public static boolean sort = false;

    public static Coordinate alphaBetaSearch(Board b, byte playerColor, int alpha, int beta, byte depth) {
        // if (depth > 12) {
        // sort = true;
        // }
        List<Coordinate> children = b.generateValidMoves(playerColor);
        Coordinate bestChild = null;
        int bestChildUtility = Integer.MIN_VALUE;
        // if (sort) {
        // for (Coordinate c : children) {
        // Board nb = b.playMoveGetNewBoard(c.first, c.second, playerColor);
        // c.heuristic += nb.utilityCornerCloseness(OthelloAI.globalPlayerColor);
        // c.heuristic += nb.utilityCornerEvaluation(OthelloAI.globalPlayerColor);
        // }
        // Collections.sort(children, (x, y) -> {
        // return y.heuristic - x.heuristic;
        // });
        // }
        for (Coordinate c : children) {
            Board newBoard = b.playMoveGetNewBoard(c.first, c.second, playerColor);
            int childUtility = miniMax(newBoard, OthelloAI.opponentColor(playerColor), alpha, beta,
                    (byte) (depth - 1), false);

            if (childUtility > bestChildUtility) {
                bestChildUtility = childUtility;
                bestChild = c;
                alpha = Math.max(alpha, childUtility);
            }
            nodesVisited++;

            if (beta <= alpha) {
                break;
            }
        }
        return bestChild;
    }

    public static int miniMax(Board b, byte playerColor, int alpha, int beta, byte depth,
            boolean maximizingPlayer) {
        if (depth == 0) {
            return b.utilityCornerEvaluation(OthelloAI.globalPlayerColor)
                    + b.utilityFrontierDiscs(OthelloAI.globalPlayerColor)
                    + b.utilityCornerCloseness(OthelloAI.globalPlayerColor);

        }
        List<Coordinate> children = b.generateValidMoves(playerColor);
        if (children.size() == 0) {
            if (b.generateValidMoves(OthelloAI.opponentColor(playerColor)).size() == 0) {
                // Terminal state reached before depth end, so change heuristic to count
                return b.utilityCountPieces(OthelloAI.globalPlayerColor);
            }
            return b.utilityCornerEvaluation(OthelloAI.globalPlayerColor)
                    + b.utilityFrontierDiscs(OthelloAI.globalPlayerColor)
                    + b.utilityCornerCloseness(OthelloAI.globalPlayerColor);
        }

        // if (sort) {
        // for (Coordinate c : children) {
        // Board nb = b.playMoveGetNewBoard(c.first, c.second, playerColor);
        // c.heuristic += nb.utilityCornerCloseness(OthelloAI.globalPlayerColor);
        // c.heuristic += nb.utilityCornerEvaluation(OthelloAI.globalPlayerColor);
        // }
        // if (maximizingPlayer) {
        // Collections.sort(children, (x, y) -> {
        // return y.heuristic - x.heuristic;
        // });
        // } else {
        // Collections.sort(children, (x, y) -> {
        // return x.heuristic - y.heuristic;
        // });
        // }
        // }
        int bestChildUtility = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Coordinate c : children) {
            Board newBoard = b.playMoveGetNewBoard(c.first, c.second, playerColor);
            int childUtility = miniMax(newBoard, OthelloAI.opponentColor(playerColor), alpha, beta,
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
            nodesVisited++;
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
