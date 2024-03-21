
public class HeuristicEvaluators {

    public static final int[][] direction = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 1 }, { 1, 1 }, { 1, -1 },
            { -1, -1 } };
    
    // public static final int[][] EVALUATION_TABLE = {
    // { 30, -4, -15, 6, 7, 5, 5, 7, 6, -15, -5, 30 },
    // { -4, -17, -17, 1, 3, 2, 2, 3, 1, -17, -17, -4 },
    // { -15, -17, -12, 0, 1, 1, 1, 1, 0, -12, -17, -15 },
    // { 6, 1, 0, 4, 3, 1, 1, 3, 4, 0, 1, 6 },
    // { 7, 3, 1, 3, 2, 1, 1, 2, 3, 1, 3, 7 },
    // { 5, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 5 },
    // { 5, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 5 },
    // { 7, 3, 1, 3, 2, 1, 1, 2, 3, 1, 3, 7 },
    // { 6, 1, 0, 4, 3, 1, 1, 3, 4, 0, 1, 6 },
    // { -15, -17, -12, 0, 1, 1, 1, 1, 0, -12, -17, -15 },
    // { -5, -17, -17, 1, 3, 2, 2, 3, 1, -17, -17, -5 },
    // { 30, -4, -15, 6, 7, 5, 5, 7, 6, -15, -5, 30 }
    // };

    public static int utilityCornerCloseness(Board b, byte playerColor) {
        int playerScore = 0;
        int opponentScore = 0;
        byte opponentColor = Utility.opponentColor(playerColor);
        if (b.board[0][0] == 0) {
            // Top left corner
            if (b.board[0][1] == playerColor)
                playerScore--;
            else if (b.board[0][1] == opponentColor)
                opponentScore--;
            if (b.board[1][1] == playerColor)
                playerScore--;
            else if (b.board[1][1] == opponentColor)
                opponentScore--;
            if (b.board[1][0] == playerColor)
                playerScore--;
            else if (b.board[1][0] == opponentColor)
                opponentScore--;
        }

        if (b.board[11][0] == 0) {
            // Bottom left corner
            if (b.board[11][1] == playerColor)
                playerScore--;
            else if (b.board[11][1] == opponentColor)
                opponentScore--;
            if (b.board[10][1] == playerColor)
                playerScore--;
            else if (b.board[10][1] == opponentColor)
                opponentScore--;
            if (b.board[10][0] == playerColor)
                playerScore--;
            else if (b.board[10][0] == opponentColor)
                opponentScore--;
        }
        if (b.board[0][11] == 0) {
            // Top Right Corner
            if (b.board[0][10] == playerColor)
                playerScore--;
            else if (b.board[0][10] == opponentColor)
                opponentScore--;
            if (b.board[1][10] == playerColor)
                playerScore--;
            else if (b.board[1][10] == opponentColor)
                opponentScore--;
            if (b.board[1][11] == playerColor)
                playerScore--;
            else if (b.board[1][11] == opponentColor)
                opponentScore--;
        }

        if (b.board[11][11] == 0) {
            // Bottom Right Corner
            if (b.board[11][10] == playerColor)
                playerScore--;
            else if (b.board[11][10] == opponentColor)
                opponentScore--;
            if (b.board[10][10] == playerColor)
                playerScore--;
            else if (b.board[10][10] == opponentColor)
                opponentScore--;
            if (b.board[10][11] == playerColor)
                playerScore--;
            else if (b.board[10][11] == opponentColor)
                opponentScore--;
        }
        return 30 * (playerScore - opponentScore);
    }

    public static int utilityCountPieces(Board b, byte playerColor) {
        int playerCount = 0;
        int opponentCount = 0;
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {
                if (b.board[i][j] == playerColor) {
                    playerCount++;
                } else if (b.board[i][j] == Utility.opponentColor(playerColor)) {
                    opponentCount++;
                }
            }
        }
        int result = (playerCount - opponentCount);
        if (playerColor == 2) {
            result += 1;
        } else {
            result -= 1;
        }
        return 5000 * result;
    }

    public static int utilityPlayerMobility(Board b, byte playerColor) {
        int playerMobility = b.generateValidMoves(playerColor).size();
        int opponentMobility = b.generateValidMoves(Utility.opponentColor(playerColor)).size();
        return 5 * (playerMobility - opponentMobility);

    }

    public static int utilityCornerEvaluation(Board b, byte playerColor) {
        int playerScore1 = 0;
        int opponentScore1 = 0;
        byte opponentColor = Utility.opponentColor(playerColor);

        if (b.board[0][0] == playerColor) {
            playerScore1 += 1;
        } else if (b.board[0][0] == opponentColor) {
            opponentScore1 += 1;
        }
        if (b.board[11][11] == playerColor) {
            playerScore1 += 1;
        } else if (b.board[11][11] == opponentColor) {
            opponentScore1 += 1;
        }
        if (b.board[11][0] == playerColor) {
            playerScore1 += 1;
        } else if (b.board[11][0] == opponentColor) {
            opponentScore1 += 1;
        }
        if (b.board[0][11] == playerColor) {
            playerScore1 += 1;
        } else if (b.board[0][11] == opponentColor) {
            opponentScore1 += 1;
        }

        int playerScore2 = 0;
        int opponentScore2 = 0;

        byte i = 0;
        byte j = 1;
        while (j < 11) {
            if (b.board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (b.board[i][j] == opponentColor) {
                opponentScore2 += 1;
            }
            j++;
        }
        i = 1;
        j = 0;
        while (i < 11) {
            if (b.board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (b.board[i][j] == opponentColor) {
                opponentScore2 += 1;
            }
            i++;
        }

        i = 11;
        j = 1;
        while (j < 11) {
            if (b.board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (b.board[i][j] == opponentColor) {
                opponentScore2 += 1;
            }
            j++;
        }
        i = 1;
        j = 11;
        while (i < 11) {
            if (b.board[i][j] == playerColor) {
                playerScore2 += 1;
            } else if (b.board[i][j] == opponentColor) {
                opponentScore2 += 1;
            }
            i++;
        }
        int result = 0;
        result += 50 * (playerScore1 - opponentScore1);
        result += 10 * (playerScore2 - opponentScore2);
        return result;
    }

    public static int utilityFrontierDiscs(Board b, byte playerColor) {
        int playerScore = 0;
        int opponentScore = 0;
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {

                if (b.board[i][j] != 0) {
                    for (int[] dir : direction) {
                        int x = i + dir[0], y = j + dir[1];
                        if (x < 12 && x >= 0 && y < 12 && y >= 0 && b.board[x][y] == 0) {
                            if (b.board[i][j] == playerColor)
                                playerScore--;
                            else
                                opponentScore--;

                            break;
                        }
                    }
                }
            }
        }
        return 5 * (playerScore - opponentScore);
    }

    // public static int utilityOpenCloseEvaluation(Board b, byte playerColor) {
    // int playerScore = openCloseEvaluation(playerColor);
    // int opponentScore = openCloseEvaluation(Utility.opponentColor(playerColor));
    // return 5 * (playerScore - opponentScore);
    // }

    // public static int utilityUseEvaluationTable(Board board, byte playerColor) {
    // int score = 0;
    // for (int i = 0; i < 12; i++) {
    // for (int j = 0; j < 12; j++) {
    // if (b.board[i][j] == playerColor) {
    // score += EVALUATION_TABLE[i][j];
    // } else if (b.board[i][j] == Utility.opponentColor(playerColor)) {
    // score -= EVALUATION_TABLE[i][j];
    // }
    // }
    // }
    // // if (countTotalPieces() < 40) {
    // // return -result;
    // // } else {
    // // return result;
    // // }
    // return score;
    // }

    // public static int fastFrontierDiscUtility(Board b, byte i, byte j, byte
    // playerColor) {
    // // int playerScore = 0;
    // // int opponentScore = 0;
    // // for (byte i = 1; i < 11; i++) {
    // // for (byte j = 1; j < 11; j++) {
    // // if (b.board[i][j] != 0) {
    // // if (b.board[i - 1][j] == 0 || b.board[i][j - 1] == 0 || b.board[i - 1][j -
    // 1] ==
    // 0
    // // || b.board[i + 1][j] == 0
    // // || b.board[i][j + 1] == 0 || b.board[i + 1][j + 1] == 0 || b.board[i +
    // 1][j - 1]
    // ==
    // // 0
    // // || b.board[i - 1][j + 1] == 0) {
    // // if (b.board[i][j] == playerColor)
    // // playerScore--;
    // // else
    // // opponentScore--;
    // // }
    // // }
    // // }
    // // }
    // // return 5 * (playerScore - opponentScore);
    // int score = 0;
    // // Check if the move is a frontier disc or not
    // if (i != 0 && j != 0 && i != 11 && j != 11) {
    // if (b.board[i - 1][j] == 0 || b.board[i][j - 1] == 0 || b.board[i - 1][j - 1]
    // == 0
    // || b.board[i + 1][j] == 0
    // || b.board[i][j + 1] == 0 || b.board[i + 1][j + 1] == 0 || b.board[i + 1][j -
    // 1] ==
    // 0
    // || b.board[i - 1][j + 1] == 0) {
    // score--;
    // }
    // }
    // for (int[] dir : direction) {
    // int x1 = i + dir[0];
    // int y1 = j + dir[1];
    // if (x1 < 12 && x1 >= 0 && y1 < 12 && y1 >= 0 && b.board[x1][y1] != 0) {
    // for (int[] dirn : direction) {
    // int x2 = x1 + dirn[0], y2 = y1 + dirn[1];
    // if (x2 < 12 && x2 >= 0 && y2 < 12 && y2 >= 0 && b.board[x2][y2] == 0) {
    // if (b.board[x1][y1] == playerColor)
    // score--;
    // else
    // score++;

    // break;
    // }
    // }
    // }
    // }

    // for (int m = 0; m < direction.length; m++) {
    // byte k = (byte) (i + direction[m][0]);
    // byte l = (byte) (j + direction[m][1]);
    // // Move till there is opponent color piece found
    // while (k >= 0 && k < 12 && l >= 0 && l < 12 && b.board[k][l] ==
    // Utility.opponentColor(playerColor)) {
    // k += direction[m][0];
    // l += direction[m][1];
    // }
    // if (k >= 0 && k < 12 && l >= 0 && l < 12 && b.board[k][l] == playerColor) {
    // // Found a black on border, need to flip opponent pieces till that position
    // k = (byte) (i + direction[m][0]);
    // l = (byte) (j + direction[m][1]);
    // while (b.board[k][l] == Utility.opponentColor(playerColor)) {
    // if (k != 0 && l != 0 && k != 11 && l != 11) {
    // if (b.board[k - 1][l] == 0 || b.board[k][l - 1] == 0 || b.board[k - 1][l - 1]
    // == 0
    // || b.board[k + 1][l] == 0
    // || b.board[k][l + 1] == 0 || b.board[k + 1][l + 1] == 0 || b.board[k + 1][l -
    // 1] ==
    // 0
    // || b.board[k - 1][l + 1] == 0) {
    // score -= 2;
    // }
    // }
    // k += direction[m][0];
    // l += direction[m][1];
    // }
    // }
    // }

    // return 5 * score;
    // }

    // public static int openCloseEvaluation(Board board, byte playerColor) {
    // // Check number of closed for opponent and increment the score based on that

    // // Horizontal
    // int score = 0;
    // final int CORNER_PENALTY = -1;
    // final int OPPONENT_CLOSE_WEIGHT = 0;
    // final int OPPONENT_OPEN_WEIGHT = 0;
    // for (byte i = 0; i < 12; i++) {
    // byte startl = 0;
    // while (startl < 12) {
    // byte l = startl;
    // byte startOpp = 0;
    // byte endOpp = 0;
    // while (l < 12 && b.board[i][l] != Utility.opponentColor(playerColor)) {
    // l++;
    // }
    // startOpp = l;
    // while (l < 12 && b.board[i][l] == Utility.opponentColor(playerColor)) {
    // l++;
    // }
    // endOpp = l;
    // startl = l;

    // // Found opponent piece
    // if (startOpp < 12) {
    // int corner = 0;
    // int playerClose = 0;
    // int emptyClose = 0;
    // if (startOpp == 0) {
    // corner++;
    // } else if (b.board[i][startOpp - 1] == 0) {
    // emptyClose++;
    // } else if (b.board[i][startOpp - 1] == playerColor) {
    // playerClose++;
    // }
    // if (endOpp == 12) {
    // corner++;
    // } else if (b.board[i][endOpp] == 0) {
    // emptyClose++;
    // } else if (b.board[i][endOpp] == playerColor) {
    // playerClose++;
    // }
    // score += CORNER_PENALTY * corner;
    // if (playerClose == 1 && emptyClose == 1) {
    // score += OPPONENT_CLOSE_WEIGHT * (endOpp - startOpp);
    // }
    // if (emptyClose == 2) {
    // score += 1;
    // }
    // }
    // }
    // }

    // // Vertical
    // for (byte j = 0; j < 12; j++) {
    // byte startk = 0;
    // while (startk < 12) {
    // byte k = startk;
    // byte startOpp = 0;
    // byte endOpp = 0;
    // while (k < 12 && b.board[k][j] != Utility.opponentColor(playerColor)) {
    // k++;
    // }
    // startOpp = k;
    // while (k < 12 && b.board[k][j] == Utility.opponentColor(playerColor)) {
    // k++;
    // }
    // endOpp = k;
    // startk = k;

    // // Found opponent piece
    // if (startOpp < 12) {
    // int corner = 0;
    // int playerClose = 0;
    // int emptyClose = 0;
    // if (startOpp == 0) {
    // corner++;
    // } else if (b.board[startOpp - 1][j] == 0) {
    // emptyClose++;
    // } else if (b.board[startOpp - 1][j] == playerColor) {
    // playerClose++;
    // }
    // if (endOpp == 12) {
    // corner++;
    // } else if (b.board[endOpp][j] == 0) {
    // emptyClose++;
    // } else if (b.board[endOpp][j] == playerColor) {
    // playerClose++;
    // }
    // score += CORNER_PENALTY * corner;
    // if (playerClose == 1 && emptyClose == 1) {
    // score += OPPONENT_CLOSE_WEIGHT * (endOpp - startOpp);
    // }
    // if (emptyClose == 2) {
    // score += OPPONENT_OPEN_WEIGHT;
    // }
    // }
    // }
    // }

    // // TopLeft to Middle diagonal
    // for (byte i = 0; i < 12; i++) {
    // byte currentk = i;
    // byte currentl = 0;
    // while (currentk >= 0) {
    // byte k = currentk;
    // byte l = currentl;
    // byte startOppk = 0;
    // byte startOppl = 0;
    // byte endOppk = 0;
    // byte endOppl = 0;
    // while (k >= 0 && b.board[k][l] != Utility.opponentColor(playerColor)) {
    // k--;
    // l++;
    // }
    // startOppk = k;
    // startOppl = l;
    // while (k >= 0 && b.board[k][l] == Utility.opponentColor(playerColor)) {
    // k--;
    // l++;
    // }
    // endOppk = k;
    // endOppl = l;
    // currentk = k;
    // currentl = l;

    // // Found opponent piece
    // if (startOppk >= 0) {
    // int corner = 0;
    // int playerClose = 0;
    // int emptyClose = 0;
    // if (startOppk == i) {
    // corner++;
    // } else if (b.board[startOppk + 1][startOppl - 1] == 0) {
    // emptyClose++;
    // } else if (b.board[startOppk + 1][startOppl - 1] == playerColor) {
    // playerClose++;
    // }
    // if (endOppk == -1) {
    // corner++;
    // } else if (b.board[endOppk][endOppl] == 0) {
    // emptyClose++;
    // } else if (b.board[endOppk][endOppl] == playerColor) {
    // playerClose++;
    // }
    // score += CORNER_PENALTY * corner;
    // if (playerClose == 1 && emptyClose == 1) {
    // score += OPPONENT_CLOSE_WEIGHT * (endOppl - startOppl);
    // }
    // if (emptyClose == 2) {
    // score += OPPONENT_OPEN_WEIGHT;
    // }
    // }
    // }
    // }

    // // Middle to BottomRight diagonal
    // for (byte j = 1; j < 12; j++) {
    // byte currentk = 11;
    // byte currentl = j;
    // while (currentl < 12) {
    // byte k = currentk;
    // byte l = currentl;
    // byte startOppk = 0;
    // byte startOppl = 0;
    // byte endOppk = 0;
    // byte endOppl = 0;
    // while (l < 12 && b.board[k][l] != Utility.opponentColor(playerColor)) {
    // k--;
    // l++;
    // }
    // startOppk = k;
    // startOppl = l;
    // while (l < 12 && b.board[k][l] == Utility.opponentColor(playerColor)) {
    // k--;
    // l++;
    // }
    // endOppk = k;
    // endOppl = l;
    // currentk = k;
    // currentl = l;

    // // Found opponent piece
    // if (startOppl < 12) {
    // int corner = 0;
    // int playerClose = 0;
    // int emptyClose = 0;
    // if (startOppl == j) {
    // corner++;
    // } else if (b.board[startOppk + 1][startOppl - 1] == 0) {
    // emptyClose++;
    // } else if (b.board[startOppk + 1][startOppl - 1] == playerColor) {
    // playerClose++;
    // }
    // if (endOppl == 12) {
    // corner++;
    // } else if (b.board[endOppk][endOppl] == 0) {
    // emptyClose++;
    // } else if (b.board[endOppk][endOppl] == playerColor) {
    // playerClose++;
    // }
    // score += CORNER_PENALTY * corner;
    // if (playerClose == 1 && emptyClose == 1) {
    // score += OPPONENT_CLOSE_WEIGHT * (endOppl - startOppl);
    // }
    // if (emptyClose == 2) {
    // score += OPPONENT_OPEN_WEIGHT;
    // }
    // }
    // }
    // }
    // // TopRight to Middle diagonal
    // for (byte i = 0; i < 12; i++) {
    // byte currentk = i;
    // byte currentl = 11;
    // while (currentk >= 0) {
    // byte k = currentk;
    // byte l = currentl;
    // byte startOppk = 0;
    // byte startOppl = 0;
    // byte endOppk = 0;
    // byte endOppl = 0;
    // while (k >= 0 && b.board[k][l] != Utility.opponentColor(playerColor)) {
    // k--;
    // l--;
    // }
    // startOppk = k;
    // startOppl = l;
    // while (k >= 0 && b.board[k][l] == Utility.opponentColor(playerColor)) {
    // k--;
    // l--;
    // }
    // endOppk = k;
    // endOppl = l;
    // currentk = k;
    // currentl = l;

    // // Found opponent piece
    // if (startOppk >= 0) {
    // int corner = 0;
    // int playerClose = 0;
    // int emptyClose = 0;
    // if (startOppk == i) {
    // corner++;
    // } else if (b.board[startOppk + 1][startOppl + 1] == 0) {
    // emptyClose++;
    // } else if (b.board[startOppk + 1][startOppl + 1] == playerColor) {
    // playerClose++;
    // }
    // if (endOppk == -1) {
    // corner++;
    // } else if (b.board[endOppk][endOppl] == 0) {
    // emptyClose++;
    // } else if (b.board[endOppk][endOppl] == playerColor) {
    // playerClose++;
    // }
    // score += CORNER_PENALTY * corner;
    // if (playerClose == 1 && emptyClose == 1) {
    // score += OPPONENT_CLOSE_WEIGHT * (startOppl - endOppl);
    // }
    // if (emptyClose == 2) {
    // score += OPPONENT_OPEN_WEIGHT;
    // }
    // }
    // }
    // }
    // // Middle to BottomLeft diagonal
    // for (byte j = 10; j >= 0; j--) {
    // byte currentk = 11;
    // byte currentl = j;
    // while (currentl >= 0) {
    // byte k = currentk;
    // byte l = currentl;
    // byte startOppk = 0;
    // byte startOppl = 0;
    // byte endOppk = 0;
    // byte endOppl = 0;
    // while (l >= 0 && b.board[k][l] != Utility.opponentColor(playerColor)) {
    // k--;
    // l--;
    // }
    // startOppk = k;
    // startOppl = l;
    // while (l >= 0 && b.board[k][l] == Utility.opponentColor(playerColor)) {
    // k--;
    // l--;
    // }
    // endOppk = k;
    // endOppl = l;
    // currentk = k;
    // currentl = l;

    // // Found opponent piece
    // if (startOppl >= 0) {
    // int corner = 0;
    // int playerClose = 0;
    // int emptyClose = 0;
    // if (startOppl == j) {
    // corner++;
    // } else if (b.board[startOppk + 1][startOppl + 1] == 0) {
    // emptyClose++;
    // } else if (b.board[startOppk + 1][startOppl + 1] == playerColor) {
    // playerClose++;
    // }
    // if (endOppl == -1) {
    // corner++;
    // } else if (b.board[endOppk][endOppl] == 0) {
    // emptyClose++;
    // } else if (b.board[endOppk][endOppl] == playerColor) {
    // playerClose++;
    // }
    // score += CORNER_PENALTY * corner;
    // if (playerClose == 1 && emptyClose == 1) {
    // score += OPPONENT_CLOSE_WEIGHT * (startOppl - endOppl);
    // }
    // if (emptyClose == 2) {
    // score += OPPONENT_OPEN_WEIGHT;
    // }
    // }
    // }
    // }
    // return score;
    // }

}