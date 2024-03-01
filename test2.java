public class MyClass {
    public static void main(String args[]) {
        int board[][] ={{0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,0,2,2,0,2,0,0,2,0,0,0},
                        {0,0,0,1,0,1,0,1,0,0,0,0},
                        {0,0,0,0,1,1,1,0,0,0,0,0},
                        {0,0,2,1,1,0,1,1,2,0,0,0},
                        {0,0,0,0,1,1,1,0,0,0,0,0},
                        {0,0,0,1,0,1,0,1,0,0,0,0},
                        {0,0,2,0,0,2,0,0,2,0,0,0},
                        {0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0,0,0,0,0}};
        int playerColor =2;
        byte i=5;
        byte j=3;
       board[i][j] = playerColor;
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
    for(int m=0;m<12;m++) {
        for(int n=0;n<12;n++) {
            System.out.print(board[m][n]+" ");
        }
        System.out.println();
    }
}
}