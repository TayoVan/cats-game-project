public class Main {
    public static void main(String[] args) {
        ConfigManager.loadConfig();
        while (true) {
            GameLogic.showMenu();
            int choice = GameLogic.getUserInput();

            switch (choice) {
                case 1 -> GameLogic.playGame();
                case 2 -> GameLogic.configureSettings();
                case 3 -> GameLogic.showRules();
                case 4 -> ConfigManager.showStats();
                case 5 -> {
                    System.out.println("Вихід.");
                    return;
                }
                default -> System.out.println("Опаньки, літер у нашій грі нема, зробіть вибір цифрою)");
            }
        }
    }
}