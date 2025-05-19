import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfigManager {
    private static final String CONFIG_FILE = "catsconfig.txt";
    private static final String STATS_FILE = "catsstats.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("fieldSize=")) {
                    GameLogic.fieldSize = Integer.parseInt(line.substring("fieldSize=".length()));
                } else if (line.startsWith("playerXName=")) {
                    GameLogic.playerXName = line.substring("playerXName=".length());
                } else if (line.startsWith("playerOName=")) {
                    GameLogic.playerOName = line.substring("playerOName=".length());
                }
            }
        } catch (IOException e) {
            // конфігурація відсутня — використовуються стандартні значення
        }
    }

    public static void saveConfig() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write("fieldSize=" + GameLogic.fieldSize);
            writer.newLine();
            writer.write("playerXName=" + GameLogic.playerXName);
            writer.newLine();
            writer.write("playerOName=" + GameLogic.playerOName);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Помилочка при збереженні конфігурації.");
        }
    }

    public static void saveStat(String winnerName, char winnerSymbol) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATS_FILE, true))) {
            LocalDateTime now = LocalDateTime.now();
            String dateTime = now.format(FORMATTER);
            String line = winnerName.equals("Нічия")
                ? "Дата та час: " + dateTime + ", Розмір поля: " + GameLogic.fieldSize + "x" + GameLogic.fieldSize + ", Результат: Нічия"
                : "Дата та час: " + dateTime + ", Розмір поля: " + GameLogic.fieldSize + "x" + GameLogic.fieldSize + ", Переможець: " + winnerName + " (" + winnerSymbol + ")";
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Помилочка при збереженні статистики.");
        }
    }

    public static void showStats() {
        System.out.println("{ \nСтатистика ігор котиків } :");
        try (BufferedReader reader = new BufferedReader(new FileReader(STATS_FILE))) {
            String line;
            boolean hasStats = false;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                hasStats = true;
            }
            if (!hasStats) {
                System.out.println("Статистика ігор котиків відсутня.");
            }
        } catch (IOException e) {
            System.out.println("Помилочка при читанні файлу статистики котиків.");
        }
        System.out.println("Тисніть Enter щоб повернутися в меню.");
        GameLogic.scanner.nextLine();
        GameLogic.scanner.nextLine();
    }
}