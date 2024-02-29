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
            c = MM.minimaxDecision(b, playerColor, (byte)3);
            long exectime = System.nanoTime() - start;
            double exectimeD = (double)exectime/1000000000d;
            System.out.println("Minmax Exec:" + exectimeD);
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
    public static Coordinate minimaxDecision(Board b, byte playerColor, byte depth) {
        Set<Coordinate> children = b.generateValidMoves(playerColor);
        
        if (children.size() == 1) {
            return children.iterator().next();
        }
        int maxValue = Integer.MIN_VALUE;
        Coordinate bestChild = null;
        for (Coordinate c : children) {
            Board newBoard = b.playMoveGetNewBoard(c.first, c.second, playerColor);
            int result = minValue(newBoard, playerColor, depth);
            if (result > maxValue) {
                maxValue = result;
                bestChild = c;
            }
        }
        return bestChild;
    }

    public static int maxValue(Board b, byte playerColor, byte depth) {
        Set<Coordinate> children = b.generateValidMoves(playerColor);
        if (children.size() == 0 || depth <= 0) {
            return b.utility(playerColor);
        }
        depth--;
        int maxValue = Integer.MIN_VALUE;
        for (Coordinate c : children) {
            Board newBoard = b.playMoveGetNewBoard(c.first, c.second, playerColor);
            Math.max(maxValue, minValue(newBoard, playerColor, depth));
        }
        return maxValue;
    }

    public static int minValue(Board b, byte playerColor, byte depth) {
        Set<Coordinate> children = b.generateValidMoves(homework.opponentColor(playerColor));
        if (children.size() == 0 || depth <= 0) {
            return b.utility(playerColor);
        }
        depth--;
        int minValue = Integer.MAX_VALUE;
        for (Coordinate c : children) {
            Board newBoard = b.playMoveGetNewBoard(c.first, c.second, homework.opponentColor(playerColor));
            Math.min(minValue, maxValue(newBoard,
                    playerColor, depth));
        }
        return minValue;
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
        double exectimeD = (double)exectime/1000000000d;
        System.out.println("Play Moves Get Exec:" + exectimeD);

        return b;
    }

    public Set<Coordinate> generateValidMoves(byte playerColor) {
        long start = System.nanoTime();
        Set<Coordinate> locations = new HashSet<>();
        for (byte i = 0; i < 12; i++) {
            for (byte j = 0; j < 12; j++) {
                if (board[i][j] == playerColor) {
                    // From this position traverse in all 8 directions to determine all possible
                    // valid moves linked to this piece
                    // Up
                    byte k = (byte) (i - 1);
                    byte countOpponent = 0;
                    while (k >= 0 && board[k][j] != playerColor) {
                        if (board[k][j] == 0 && countOpponent == 0) {
                            break;
                        } else if (board[k][j] != 0 && board[k][j] != playerColor) {
                            countOpponent++;
                        } else if (board[k][j] == 0 && countOpponent > 0) {
                            locations.add(new Coordinate(k, j));
                            break;
                        }
                        k -= 1;
                    }

                    // Down
                    k = (byte) (i + 1);
                    countOpponent = 0;
                    while (k < 12 && board[k][j] != playerColor) {
                        if (board[k][j] == 0 && countOpponent == 0) {
                            break;
                        } else if (board[k][j] != 0 && board[k][j] != playerColor) {
                            countOpponent++;
                        } else if (board[k][j] == 0 && countOpponent > 0) {
                            locations.add(new Coordinate(k, j));
                            break;
                        }
                        k += 1;
                    }
                    // Left
                    byte l = (byte) (j - 1);
                    countOpponent = 0;
                    while (l >= 0 && board[i][l] != playerColor) {
                        if (board[i][l] == 0 && countOpponent == 0) {
                            break;
                        } else if (board[i][l] != 0 && board[i][l] != playerColor) {
                            countOpponent++;
                        } else if (board[i][l] == 0 && countOpponent > 0) {
                            locations.add(new Coordinate(i, l));
                            break;
                        }
                        l -= 1;
                    }
                    // Right
                    l = (byte) (j + 1);
                    countOpponent = 0;
                    while (l < 12 && board[i][l] != playerColor) {
                        if (board[i][l] == 0 && countOpponent == 0) {
                            break;
                        } else if (board[i][l] != 0 && board[i][l] != playerColor) {
                            countOpponent++;
                        } else if (board[i][l] == 0 && countOpponent > 0) {
                            locations.add(new Coordinate(i, l));
                            break;
                        }
                        l += 1;
                    }
                    // Top right diagonal
                    k = (byte) (i - 1);
                    l = (byte) (j + 1);
                    countOpponent = 0;
                    while (k >= 0 && l < 12 && board[k][l] != playerColor) {
                        if (board[k][l] == 0 && countOpponent == 0) {
                            break;
                        } else if (board[k][l] != 0 && board[k][l] != playerColor) {
                            countOpponent++;
                        } else if (board[k][l] == 0 && countOpponent > 0) {
                            locations.add(new Coordinate(k, l));
                            break;
                        }
                        k -= 1;
                        l += 1;
                    }
                    // Bottom right diagonal
                    k = (byte) (i + 1);
                    l = (byte) (j + 1);
                    countOpponent = 0;
                    while (k < 12 && l < 12 && board[k][l] != playerColor) {
                        if (board[k][l] == 0 && countOpponent == 0) {
                            break;
                        } else if (board[k][l] != 0 && board[k][l] != playerColor) {
                            countOpponent++;
                        } else if (board[k][l] == 0 && countOpponent > 0) {
                            locations.add(new Coordinate(k, l));
                            break;
                        }
                        k += 1;
                        l += 1;
                    }
                    // Bottom left diagonal
                    k = (byte) (i + 1);
                    l = (byte) (j - 1);
                    countOpponent = 0;
                    while (k < 12 && l >= 0 && board[k][l] != playerColor) {
                        if (board[k][l] == 0 && countOpponent == 0) {
                            break;
                        } else if (board[k][l] != 0 && board[k][l] != playerColor) {
                            countOpponent++;
                        } else if (board[k][l] == 0 && countOpponent > 0) {
                            locations.add(new Coordinate(k, l));
                            break;
                        }
                        k += 1;
                        l -= 1;
                    }

                    // Top left diagonal
                    k = (byte) (i - 1);
                    l = (byte) (j - 1);
                    countOpponent = 0;
                    while (k >= 0 && l >= 0 && board[k][l] != playerColor) {
                        if (board[k][l] == 0 && countOpponent == 0) {
                            break;
                        } else if (board[k][l] != 0 && board[k][l] != playerColor) {
                            countOpponent++;
                        } else if (board[k][l] == 0 && countOpponent > 0) {
                            locations.add(new Coordinate(k, l));
                            break;
                        }
                        k -= 1;
                        l -= 1;
                    }
                }
            }
        }

        long exectime = System.nanoTime() - start;
        double exectimeD = (double)exectime/1000000000d;
        System.out.println("Generate Valid Moves Exec:" + exectimeD);
        return locations;
    }

    public List<Coordinate> generateValidMoves2(byte playerColor) {
        
    }

    public int utility(byte playerColor) {
        long start = System.nanoTime();
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == playerColor) {
                    score++;
                }
            }
        }
        long exectime = System.nanoTime() - start;
        double exectimeD = (double)exectime/1000000000d;
        System.out.println("Utility Exec:" + exectimeD);
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
