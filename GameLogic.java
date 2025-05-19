import java.util.Scanner;

public class GameLogic {
    static int fieldSize = 3;
    static boolean[][] occupied;
    static char[][] board;
    static char currentPlayer = 'X';
    static Scanner scanner = new Scanner(System.in);
    static String playerXName = "Гравець X";
    static String playerOName = "Гравець O";

    public static void showMenu() {
        System.out.println("Котики збираються грати! готуємо лапки!");
        System.out.println("\n1.{ Котики хочуть грати }");
        System.out.println("2. { Котики налаштують вашу гру! }");
        System.out.println("3. { Котики розкажуть казочку про гру }");
        System.out.println("4. { Котики сформували статистику }");
        System.out.println("5. { Покидаєте котиків?( }");
        System.out.print(" { Тиць на циферки }: ");
    }

    public static int getUserInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Опаньки, літер у нашій грі нема, зробіть вибір цифрою)");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static void playGame() {
        occupied = new boolean[fieldSize][fieldSize];
        board = new char[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                board[i][j] = ' ';
            }
        }

        currentPlayer = 'X';

        while (true) {
            printBoard();
            String currentPlayerName = (currentPlayer == 'X') ? playerXName : playerOName;
            System.out.println("Котик " + currentPlayerName + " (" + currentPlayer + "), ваш хід. Введіть рядок і стовпець (1-" + fieldSize + "): ");
            int row = getUserInput() - 1;
            int col = getUserInput() - 1;

            if (row < 0 || row >= fieldSize || col < 0 || col >= fieldSize || occupied[row][col]) {
                System.out.println("Неправильний хід, спробуйте ще раз.");
                continue;
            }

            occupied[row][col] = true;
            board[row][col] = currentPlayer;

            if (checkWin(row, col)) {
                printBoard();
                System.out.println("Котик " + currentPlayerName + " (" + currentPlayer + ") виграв!");
                ConfigManager.saveStat(currentPlayerName, currentPlayer);
                break;
            }

            if (isBoardFull()) {
                printBoard();
                System.out.println("Опаньки, схоже нічия! Ви повертаєтеся на головне меню");
                ConfigManager.saveStat("Нічия", currentPlayer);
                break;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
    }

    private static boolean isBoardFull() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (!occupied[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkWin(int row, int col) {
        return checkLine(row, 0, 0, 1)
            || checkLine(0, col, 1, 0)
            || (row == col && checkLine(0, 0, 1, 1))
            || (row + col == fieldSize - 1 && checkLine(0, fieldSize - 1, 1, -1));
    }

    private static boolean checkLine(int startRow, int startCol, int deltaRow, int deltaCol) {
        char first = board[startRow][startCol];
        if (first == ' ') return false;
        for (int i = 1; i < fieldSize; i++) {
            int row = startRow + i * deltaRow;
            int col = startCol + i * deltaCol;
            if (board[row][col] != first) {
                return false;
            }
        }
        return true;
    }

    private static void printBoard() {
        System.out.println();
        System.out.print("  ");
        for (int j = 0; j < fieldSize; j++) {
            System.out.print(" " + (j + 1) + "   ");
        }
        System.out.println();
        for (int i = 0; i < fieldSize; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < fieldSize; j++) {
                System.out.print("| " + board[i][j] + " ");
            }
            System.out.println("|");
            if (i < fieldSize - 1) {
                System.out.print("  ");
                for (int j = 0; j < fieldSize; j++) {
                    System.out.print("----");
                }
                System.out.println("-");
            }
        }
    }

    public static void configureSettings() {
        while (true) {
            System.out.println("{ \nНалаштування }");
            System.out.println("1. { Котики змінять розмір поля }");
            System.out.println("2. { Змінити ім'я котика X }");
            System.out.println("3. { Змінити ім'я котика O }");
            System.out.println("0. { Повернути котика до меню }");
            System.out.print("{ Tиць на циферки }: ");

            int choice = getUserInput();
            scanner.nextLine();

            switch (choice) {
                case 1 -> selectFieldSize();
                case 2 -> {
                    System.out.print("Введіть нове ім'я для котика X: ");
                    playerXName = scanner.nextLine();
                    ConfigManager.saveConfig();
                }
                case 3 -> {
                    System.out.print("Введіть нове ім'я для котика O: ");
                    playerOName = scanner.nextLine();
                    ConfigManager.saveConfig();
                }
                case 0 -> { return; }
                default -> System.out.println("Опаньки, такого пункту немає.");
            }
        }
    }

    private static void selectFieldSize() {
        System.out.println(" { Оберіть розмір поля }:");
        System.out.println("1. { 3x3 }");
        System.out.println("2. { 5x5 }");
        System.out.println("3. { 7x7 }");
        System.out.println("4. { 9x9 }");
        System.out.print(" { Ваш вибір }: ");

        int fieldChoice = getUserInput();
        fieldSize = switch (fieldChoice) {
            case 1 -> 3;
            case 2 -> 5;
            case 3 -> 7;
            case 4 -> 9;
            default -> {
                System.out.println("Опаньки, такого розміру немає. Встановлено стандартний розмір 3x3.");
                yield 3;
            }
        };
        ConfigManager.saveConfig();
    }

    public static void showRules() {
        System.out.println("{ \nПравила гри }:");
        System.out.println("1. { Два гравці по черзі ставлять на вільні клітинки поля свої знаки (один завжди хрестик, другий - нулик). }");
        System.out.println("2. { Першим завжди ходить гравець, що ставить хрестик. }");
        System.out.println("3. { Мета гри - першим вибудувати в ряд по горизонталі, вертикалі або діагоналі три своїх фігури. }");
        System.out.println("4. { Якщо все поле заповнене, а ніхто не виграв, оголошується нічия.} ");
        System.out.println("Натисніть Enter щоб повернутися в меню.");
        scanner.nextLine();
        scanner.nextLine();
    }
}