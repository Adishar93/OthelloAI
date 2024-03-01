public class MyClass {
    public static void main(String args[]) {
     int board[][] = {{0,0,0,0,0,2,2,2,1,1,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0},
                        {0,0,0,0,1,2,2,2,2,0,0,0}};
                        
                         double score = 0;
        final float CORNER_PENALTY = -0.0f;
        final int OPPONENT_CLOSE_WEIGHT = 1;
        final float OPPONENT_OPEN_WEIGHT = 0f;
        for (byte i = 0; i < 12; i++) {
            byte l = 0;
            byte startOpp = 0;
            byte endOpp = 0;
            while (l < 12 && board[i][l] !=2) {
                l++;
            }
            startOpp = l;
            while (l < 12 && board[i][l] == 2) {
                l++;
            }
            endOpp = l;

            // Found opponent piece
            if (startOpp < 12) {
                int corner = 0;
                int playerClose = 0;
                int emptyClose = 0;
                if (startOpp == 0) {
                    corner++;
                } else if (board[i][startOpp - 1] == 0) {
                    emptyClose++;
                } else if (board[i][startOpp - 1] == 1) {
                    playerClose++;
                }
                if (endOpp == 12) {
                    corner++;
                } else if (board[i][endOpp] == 0) {
                    emptyClose++;
                } else if (board[i][endOpp] == 1) {
                    playerClose++;
                }
                score += CORNER_PENALTY * corner * (endOpp - startOpp);
                if (playerClose == 1 && emptyClose == 1) {
                    score += OPPONENT_CLOSE_WEIGHT * (endOpp - startOpp);
                }
                if (emptyClose == 2) {
                    score += OPPONENT_OPEN_WEIGHT;
                }
            }
        }
        System.out.println(score);
}
}