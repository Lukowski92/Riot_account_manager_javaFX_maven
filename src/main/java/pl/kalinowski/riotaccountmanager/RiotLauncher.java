package pl.kalinowski.riotaccountmanager;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.Arrays;


public class RiotLauncher {

    public static void login(String username, char[] password) {
        try {
            // 1. Uruchom Riot Client z parametrami
            new ProcessBuilder(
                    "C:\\Riot Games\\Riot Client\\RiotClientServices.exe",
                    "--launch-product=league_of_legends",
                    "--launch-patchline=live"
            ).start();

            System.out.println("Riot Client uruchomiony...");
            Thread.sleep(5000); // Poczekaj na uruchomienie (dopasuj do szybkości PC)

            Robot robot = new Robot();

            // 2. Wklej login
            copyToClipboard(username);
            ctrlV(robot);
            Thread.sleep(500);

            // 3. Przejdź do pola hasła
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            Thread.sleep(300);

            // 4. Wklej hasło
            String passString = new String(password); // tymczasowo jako String
            copyToClipboard(passString);
            ctrlV(robot);
            Thread.sleep(500);

            // 5. Zatwierdź logowanie
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            System.out.println("Login zakończony.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. Wyczyszczenie hasła z pamięci RAM
            Arrays.fill(password, '0');
        }
    }

    private static void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(selection, null);
    }

    private static void ctrlV(Robot robot) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
}

