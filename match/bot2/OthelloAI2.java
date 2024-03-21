
import java.io.*;
import java.util.*;

public class OthelloAI2 {
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
            // if (myTime > 250) {
            // result = MM2.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 10);
            // } else if (myTime > 200) {
            // result = MM2.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 9);
            // } else if (myTime > 100) {
            // result = MM2.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 7);
            // } else if (myTime > 80) {
            // result = MM2.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 6);
            // } else if (myTime > 5) {
            // result = MM2.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 5);
            // } else if (myTime > 2) {
            // result = MM2.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 3);
            // } else if (myTime > 0.1f) {
            // result = MM2.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE,
            // Integer.MAX_VALUE, (byte) 1);
            // } else {
            // result = b.generateValidMoves(playerColor).get(0);
            // }
            result = MM2.alphaBetaSearch(b, playerColor, Integer.MIN_VALUE, Integer.MAX_VALUE, (byte) 9);
            System.out.println("Nodes visited bot2: " + MM2.nodesVisited);

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
}

class MM2 {
    public static long nodesVisited = 0;
    public static boolean sort = false;
    public static final int[][] direction = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 1 }, { 1, 1 }, { 1, -1 },
            { -1, -1 } };

    public static Coordinate alphaBetaSearch(Board b, byte playerColor, int alpha, int beta, byte depth) {
        if (depth > 12) {
            sort = true;
        }
        List<Coordinate> children = b.generateValidMoves(playerColor);
        Coordinate bestChild = null;
        int bestChildUtility = Integer.MIN_VALUE;

        if (sort) {
            for (Coordinate c : children) {
                Board nb = b.playMoveGetNewBoard(c.first, c.second, playerColor);
                c.heuristic += HeuristicEvaluators.utilityCornerCloseness(nb, OthelloAI2.globalPlayerColor);
                c.heuristic += HeuristicEvaluators.utilityCornerEvaluation(nb, OthelloAI2.globalPlayerColor);
            }
            Collections.sort(children, (x, y) -> {
                return y.heuristic - x.heuristic;
            });
        }

        for (Coordinate c : children) {
            Board newBoard = playMoveGetNewBoard(b.board, c.first, c.second, playerColor);
            int childUtility = miniMax(newBoard, Utility.opponentColor(playerColor), alpha, beta,
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
        // //Check if board is a specific format
        // if(b.checkEdgeCaseBoard()) {
        // if(OthelloAI2.globalPlayerColor == 2) {
        // return Integer.MAX_VALUE-1;
        // } else {
        // return Integer.MIN_VALUE+1;
        // }
        // }
        if (depth == 0) {
            return HeuristicEvaluators.utilityCornerEvaluation(b, OthelloAI2.globalPlayerColor)
                    + HeuristicEvaluators.utilityFrontierDiscs(b, OthelloAI2.globalPlayerColor)
                    + HeuristicEvaluators.utilityCornerCloseness(b, OthelloAI2.globalPlayerColor);
        }
        List<Coordinate> children = b.generateValidMoves(playerColor);
        if (children.size() == 0) {
            if (b.generateValidMoves(Utility.opponentColor(playerColor)).size() == 0) {
                // Terminal state reached before depth end, so change heuristic to count
                return HeuristicEvaluators.utilityCountPieces(b, OthelloAI2.globalPlayerColor);
            }
            return miniMax(b, Utility.opponentColor(playerColor), alpha, beta,
                    (byte) (depth - 1), !maximizingPlayer);
        }
        if (sort) {
            for (Coordinate c : children) {
                Board nb = b.playMoveGetNewBoard(c.first, c.second, playerColor);
                c.heuristic += HeuristicEvaluators.utilityCornerCloseness(nb, OthelloAI2.globalPlayerColor);
                c.heuristic += HeuristicEvaluators.utilityCornerEvaluation(nb, OthelloAI2.globalPlayerColor);
            }
            if (maximizingPlayer) {
                Collections.sort(children, (x, y) -> {
                    return y.heuristic - x.heuristic;
                });
            } else {
                Collections.sort(children, (x, y) -> {
                    return x.heuristic - y.heuristic;
                });
            }
        }

        int bestChildUtility = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Coordinate c : children) {
            Board newBoard = playMoveGetNewBoard(b.board, c.first, c.second, playerColor);
            int childUtility = miniMax(newBoard, Utility.opponentColor(playerColor), alpha, beta,
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

    public static Board playMoveGetNewBoard(byte[][] board, byte i, byte j, byte playerColor) {
        Board b = new Board();
        b.board = Utility.copy2darray(board);
        b.board[i][j] = playerColor;
        // From this position traverse in all 8 directions to update all the hit pieces
        // of the opponent
        for (int m = 0; m < direction.length; m++) {
            byte k = (byte) (i + direction[m][0]);
            byte l = (byte) (j + direction[m][1]);
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

    public static List<Coordinate> generateValidMoves(byte[][] board, byte playerColor) {
        List<Coordinate> moves = new ArrayList<>(10);
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {
                if (isValidMove(board, i, j, playerColor)) {
                    moves.add(new Coordinate(i, j));
                }
            }
        }
        return moves;
    }

    public static boolean isValidMove(byte[][] board, byte i, byte j, byte playerColor) {
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

    public static int utilityCountPieces(byte[][] board, byte playerColor) {
        int playerCount = countPieces(board, playerColor);
        int opponentCount = countPieces(board, Utility.opponentColor(playerColor));
        int result = (playerCount - opponentCount);
        if (playerColor == 2) {
            result += 1;
        } else {
            result -= 1;
        }
        return 5000 * result;
    }

    public static int countPieces(byte[][] board, byte playerColor) {
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

    // public static int utilityCornerEvaluation(Board b, byte playerColor) {
    // int playerScore1 = cornerEvaluation(b.board, playerColor);
    // int opponentScore1 = cornerEvaluation(b.board,
    // Utility.opponentColor(playerColor));
    // int playerScore2 = b.edgeEvaluation(playerColor);
    // int opponentScore2 = b.edgeEvaluation(Utility.opponentColor(playerColor));
    // int result = 0;
    // result += 50 * (playerScore1 - opponentScore1);
    // result += 10 * (playerScore2 - opponentScore2);
    // return result;
    // }

    // public static int cornerEvaluation(byte[][] board, byte playerColor) {
    // // Score all corners
    // int score = 0;
    // boolean topLeft = false;
    // boolean topRight = false;
    // boolean bottomLeft = false;
    // boolean bottomRight = false;
    // byte countCorners = 0;
    // if (board[0][0] == playerColor) {
    // score += 1;
    // topLeft = true;
    // countCorners++;
    // }
    // if (board[11][0] == playerColor) {
    // score += 1;
    // bottomLeft = true;
    // countCorners++;
    // }
    // if (board[0][11] == playerColor) {
    // score += 1;
    // topRight = true;
    // countCorners++;
    // }
    // if (board[11][11] == playerColor) {
    // score += 1;
    // bottomRight = true;
    // countCorners++;
    // }
    // if (bottomLeft && topRight && !topLeft && !bottomRight) {
    // score -= 1;
    // }
    // if (!bottomLeft && !topRight && topLeft && bottomRight) {
    // score -= 1;
    // }
    // if (countCorners >= 3) {
    // return score+=2;
    // }
    // return score;
    // }

    // public static int utilityCornerCloseness(byte[][] board, byte playerColor) {
    // int playerScore = cornerCloseness(board, playerColor);
    // int opponentScore = cornerCloseness(board,
    // Utility.opponentColor(playerColor));
    // return 30 * (playerScore - opponentScore);
    // }

    // public static int cornerCloseness(byte[][] board, byte playerColor) {
    // int score = 0;
    // if (board[0][0] == 0) {
    // // Top left corner
    // if (board[0][1] == playerColor)
    // score--;
    // if (board[1][1] == playerColor)
    // score-=2;
    // if (board[1][0] == playerColor)
    // score--;
    // }

    // if (board[11][0] == 0) {
    // // Bottom left corner
    // if (board[11][1] == playerColor)
    // score--;
    // if (board[10][1] == playerColor)
    // score-=2;
    // if (board[10][0] == playerColor)
    // score--;
    // }
    // if (board[0][11] == 0) {
    // // Top Right Corner
    // if (board[0][10] == playerColor)
    // score--;
    // if (board[1][10] == playerColor)
    // score-=2;
    // if (board[1][11] == playerColor)
    // score--;
    // }

    // if (board[11][11] == 0) {
    // // Bottom Right Corner
    // if (board[11][10] == playerColor)
    // score--;
    // if (board[10][10] == playerColor)
    // score-=2;
    // if (board[10][11] == playerColor)
    // score--;
    // }
    // return score;
    // }

    public static int utilityCornerEvaluation(byte[][] board, byte playerColor) {

        int playerScore1 = 0;
        int opponentScore1 = 0;

        if (board[0][0] == playerColor) {
            playerScore1 += 1;
        } else if (board[0][0] == Utility.opponentColor(playerColor)) {
            opponentScore1 += 1;
        }
        if (board[11][11] == playerColor) {
            playerScore1 += 1;
        } else if (board[11][11] == Utility.opponentColor(playerColor)) {
            opponentScore1 += 1;
        }
        if (board[11][0] == playerColor) {
            playerScore1 += 1;
        } else if (board[11][0] == Utility.opponentColor(playerColor)) {
            opponentScore1 += 1;
        }
        if (board[0][11] == playerColor) {
            playerScore1 += 1;
        } else if (board[0][11] == Utility.opponentColor(playerColor)) {
            opponentScore1 += 1;
        }

        int playerScore2 = 0;
        int opponentScore2 = 0;

        byte i = 0;
        byte j = 1;
        while (j < 11) {
            if (board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (board[i][j] == Utility.opponentColor(playerColor)) {
                opponentScore2 += 1;
            }
            j++;
        }
        i = 1;
        j = 0;
        while (i < 11) {
            if (board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (board[i][j] == Utility.opponentColor(playerColor)) {
                opponentScore2 += 1;
            }
            i++;
        }

        i = 11;
        j = 1;
        while (j < 11) {
            if (board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (board[i][j] == Utility.opponentColor(playerColor)) {
                opponentScore2 += 1;
            }
            j++;
        }
        i = 1;
        j = 11;
        while (i < 11) {
            if (board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (board[i][j] == Utility.opponentColor(playerColor)) {
                opponentScore2 += 1;
            }
            i++;
        }
        int result = 0;
        result += 50 * (playerScore1 - opponentScore1);
        result += 10 * (playerScore2 - opponentScore2);
        return result;
    }

    public static int utilityFrontierDiscs(byte[][] board, byte playerColor) {
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

    public static int utilityCornerCloseness(byte[][] board, byte playerColor) {
        int playerScore = 0;
        int opponentScore = 0;
        byte opponentColor = Utility.opponentColor(playerColor);
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
}
