public class Utility {
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
