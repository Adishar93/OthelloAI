public class MyClass {
    public static void main(String args[]) {
     int board[][] = {  {0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,2,2,0,0,0,0,0,0,0,0,0},
                        {1,1,0,0,0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0,0,2,0,1},
                        {0,0,0,0,0,0,1,0,0,0,2,0},
                        {0,0,0,0,0,0,0,2,0,2,0,1},
                        {0,0,0,0,0,0,0,0,2,0,2,0},
                        {0,0,0,0,0,0,0,0,0,0,0,0}};
                        
                         double score = 0;
        final float CORNER_PENALTY = -0.0f;
        final int OPPONENT_CLOSE_WEIGHT = 1;
        final float OPPONENT_OPEN_WEIGHT = 0f;
           // TopRight to Middle diagonal
            for (byte i = 0; i < 12; i++) {
                byte k = i;
                byte l = 11;
                byte startOppk = 0;
                byte startOppl = 0;
                byte endOppk = 0;
                byte endOppl = 0;
                while (k >= 0 && board[k][l] != 2) {
                    k--;
                    l--;
                }
                startOppk = k;
                startOppl = l;
                while (k >= 0 && board[k][l] == 2) {
                    k--;
                    l--;
                }
                endOppk = k;
                endOppl = l;

                // Found opponent piece
                if (startOppk >= 0) {
                    int corner = 0;
                    int playerClose = 0;
                    int emptyClose = 0;
                    if (startOppk == i) {
                        corner++;
                    } else if (board[startOppk + 1][startOppl + 1] == 0) {
                        emptyClose++;
                    } else if (board[startOppk + 1][startOppl + 1] == 1) {
                        playerClose++;
                    }
                    if (endOppk == -1) {
                        corner++;
                    } else if (board[endOppk][endOppl] == 0) {
                        emptyClose++;
                    } else if (board[endOppk][endOppl] == 1) {
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
             // Middle to BottomLeft diagonal
             for (byte j = 10; j >=0; j--) {
                byte k = 11;
                byte l = j;
                byte startOppk = 0;
                byte startOppl = 0;
                byte endOppk = 0;
                byte endOppl = 0;
                while (l >=0 && board[k][l] != 2) {
                    k--;
                    l--;
                }
                startOppk = k;
                startOppl = l;
                while (l >=0 && board[k][l] == 2) {
                    k--;
                    l--;
                }
                endOppk = k;
                endOppl = l;

                // Found opponent piece
                if (startOppl >= 0) {
                    int corner = 0;
                    int playerClose = 0;
                    int emptyClose = 0;
                    if (startOppl == j) {
                        corner++;
                    } else if (board[startOppk + 1][startOppl + 1] == 0) {
                        emptyClose++;
                    } else if (board[startOppk + 1][startOppl + 1] == 1) {
                        playerClose++;
                    }
                    if (endOppl == -1) {
                        corner++;
                    } else if (board[endOppk][endOppl] == 0) {
                        emptyClose++;
                    } else if (board[endOppk][endOppl] == 1) {
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
        System.out.println(score);
}
}