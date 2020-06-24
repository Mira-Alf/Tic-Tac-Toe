package tictactoe;
import java.util.Scanner;
public class Main {

    //List of characters used in the board
    public static final char CHARACTER_X = 'X';
    public static final char CHARACTER_O = 'O';
    public static final char CHARACTER_EMPTY = '_';
    public static final char CHARACTER_TOP_BOUNDARY = '-';
    public static final char CHARACTER_SIDE_BOUNDARY = '|';

    //Static variables to denote the board and the statistics of the board
    public static char[][] cellsMatrix = new char[3][3];
    public static int EMPTY_SPACES = 0;
    public static int COUNT_OF_X = 0;
    public static int COUNT_OF_O = 0;
    public static int COUNT_WINNINGS_X = 0;
    public static int COUNT_WINNINGS_O = 0;


    public static boolean PROCEED = false;
    //0 for incomplete, 1 for complete and win, 2 for complete and draw and -1 for impossible
    public static int GAME_STATUS;

    private static boolean isValidCharacter(char ch) {
        return ch == CHARACTER_EMPTY || ch == CHARACTER_O || ch == CHARACTER_X;
    }

    public static void initBoard() {
        for( int i = 0; i < 3; i++ ) {
            for( int j = 0; j < 3; j++ ) {
                cellsMatrix[i][j] = CHARACTER_EMPTY;
                updateBoardStatistics(CHARACTER_EMPTY);
            }
        }
        GAME_STATUS = 0;
    }

    public static void printFieldBoundary() {
        for (int i = 0; i < 9; i++) {
            System.out.printf("%c", CHARACTER_TOP_BOUNDARY);
        }
        System.out.println();
    }

    public static void printBoard() {
        printFieldBoundary();
        for (char[] row : cellsMatrix) {
            System.out.printf("%c ", CHARACTER_SIDE_BOUNDARY);
            for (char element : row) {
                System.out.printf("%c ", element);
            }
            System.out.printf("%c%n", CHARACTER_SIDE_BOUNDARY);
        }
        printFieldBoundary();
    }

    public static void updateBoardStatistics( char ch ) {
        switch( ch ) {
            case CHARACTER_EMPTY:
                EMPTY_SPACES++;
                break;
            case CHARACTER_O:
                COUNT_OF_O++;
                EMPTY_SPACES--;
                break;
            case CHARACTER_X:
                COUNT_OF_X++;
                EMPTY_SPACES--;
                break;
            default:

        }
    }

    public static boolean updateBoard(String cellsString) {
        char[] charArray = cellsString.toCharArray();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char ch = charArray[3 * i + j];
                boolean isValidChar = isValidCharacter(ch);
                if (isValidCharacter(ch)) {
                    cellsMatrix[i][j] = charArray[3 * i + j];
                    updateBoardStatistics(ch);
                }
                else {
                    System.out.println("The input string contains an invalid character " + ch);
                    return isValidChar;
                }
            }
        }
        return true;
    }

    public static boolean updateBoard( int[] coordinates, char ch ) {
        int colIndex = coordinates[0] - 1;
        int rowIndex = 3 - coordinates[1];
        if( cellsMatrix[rowIndex][colIndex] != CHARACTER_EMPTY ) {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        } else if( !isValidCharacter(ch) ) {
            System.out.println("An invalid character "+ch);
            return false;
        } else {
            cellsMatrix[rowIndex][colIndex] = ch;
            updateBoardStatistics(ch);
        }
        return true;
    }



    private static void checkWinningShots( char[][] cells, boolean rowWise ) {
        for( int i = 0; i < cells.length; i++ ) {
            char element = rowWise ? cells[i][0] : cells[0][i];
            boolean isCharConsecutive = true;
            for( int j = 1; j < cells.length; j++ ) {
                if( (rowWise && cells[i][j] != element)  ||
                        ( !rowWise && cells[j][i] != element ) ) {
                    isCharConsecutive = false;
                    break;
                }
            }
            if( isCharConsecutive && element == 'X' )
                COUNT_WINNINGS_X++;
            if( isCharConsecutive && element == 'O' )
                COUNT_WINNINGS_O++;
        }
    }

    private static void checkDiagonalWinningShots( char[][] cells, boolean moveTopToBottom ) {
        int lastIndex = cells.length-1;
        char element = moveTopToBottom ? cells[0][0] : cells[lastIndex][0];
        boolean isCharConsecutive = true;
        for( int j = 1; j < cells.length; j++ ) {
            if( (moveTopToBottom && cells[j][j] != element) || (!moveTopToBottom && cells[lastIndex-j][j] != element ) ) {
                isCharConsecutive = false;
                break;
            }
        }
        if( isCharConsecutive && element == 'X' )
            COUNT_WINNINGS_X++;
        if( isCharConsecutive && element == 'O' )
            COUNT_WINNINGS_O++;
    }


    public static void checkWinningShots( ) {
        checkWinningShots(cellsMatrix,true);
        checkWinningShots(cellsMatrix, false);
        checkDiagonalWinningShots(cellsMatrix, true);
        checkDiagonalWinningShots(cellsMatrix, false);
    }


    public static void analyseGameAndDisplayResult( ) {
        if( Math.abs(COUNT_OF_O-COUNT_OF_X) >= 2 ) {
            System.out.println("Impossible");
            GAME_STATUS = -1;
        }
        else {
            checkWinningShots();
            if (COUNT_WINNINGS_X == 0 && COUNT_WINNINGS_O == 0) {
                if (EMPTY_SPACES == 0) {
                    System.out.println("Draw");
                    GAME_STATUS = 2;
                }
                else {
                    //System.out.println("Game not finished");
                    GAME_STATUS = 0;
                }
            } else if ((COUNT_WINNINGS_X == 1 && COUNT_WINNINGS_O == 0) ||
                    (COUNT_WINNINGS_X == 0 && COUNT_WINNINGS_O == 1)) {
                System.out.println(COUNT_WINNINGS_X == 1 ? "X wins" : "O wins");
                GAME_STATUS = 1;
            } else {
                System.out.println("Impossible");
                GAME_STATUS = -1;
            }
        }
    }

    public static int[] getNextMoveFromUser( String inputLine ) {
        String[] inputTokens = inputLine.split("\\s+");
        if( inputLine.length() > 3 || inputTokens.length > 2 ) {
            System.out.println("You should enter numbers!");
            return null;
        }
        int counter = 0;
        int[] coordinates = new int[2];
        for( String token : inputTokens ) {
            if( token.length() != 1 ) {
                System.out.println("You should enter numbers!");
                return null;
            } else if( token.length() == 1 && !Character.isDigit( token.charAt(0) ) ) {
                System.out.println("You should enter numbers!");
                return null;
            } else if( token.length() == 1 && Character.isDigit(token.charAt(0) ) ) {
                int coordinate = Integer.valueOf( token );
                if( coordinate < 1 || coordinate > 3 ) {
                    System.out.println("Coordinates should be from 1 to 3!");
                    return null;
                } else {
                    coordinates[counter] = coordinate;
                }
            }
            counter++;
        }
        return coordinates;
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initBoard();
        printBoard();
        boolean shouldContinueWithInput = GAME_STATUS == 0;
        char ch = CHARACTER_X;
        while( shouldContinueWithInput ) {
            System.out.print("Enter the coordinates: ");
            int[] coordinates = getNextMoveFromUser(scanner.nextLine());
            if( coordinates == null )
                shouldContinueWithInput = true;
            else {
                shouldContinueWithInput = !updateBoard(coordinates, ch);
                if( !shouldContinueWithInput ) {
                    printBoard();
                    analyseGameAndDisplayResult();
                    ch = ch == CHARACTER_X ? CHARACTER_O : CHARACTER_X;
                    shouldContinueWithInput = GAME_STATUS == 0;
                }
            }
        }
    }
}