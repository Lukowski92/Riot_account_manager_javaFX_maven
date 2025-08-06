package pl.kalinowski.riotaccountmanager;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.util.Arrays;
import java.util.Optional;

public class MainController {

    private final AccountRepository accountRepo = new SQLiteAccountRepository();

    @FXML
    private ListView<Account> accountListView;

    @FXML
    public void initialize() {
        refreshList();
    }

    @FXML
    public void refreshList() {
        accountListView.getItems().setAll(accountRepo.getAllAccounts());
    }

    @FXML
    public void onAddAccount() {
        TextInputDialog dialogUser = new TextInputDialog();
        dialogUser.setHeaderText("Podaj login:");
        Optional<String> usernameOpt = dialogUser.showAndWait();
        if (usernameOpt.isEmpty()) return;

        TextInputDialog dialogPass = new TextInputDialog();
        dialogPass.setHeaderText("Podaj hasło:");
        Optional<String> passwordOpt = dialogPass.showAndWait();
        if (passwordOpt.isEmpty()) return;

        accountRepo.addAccount(usernameOpt.get(), CryptoUtil.encrypt(passwordOpt.get()));
        refreshList();
    }

    @FXML
    public void onRemoveAccount() {
        Account selected = accountListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("Brak zaznaczonego konta.");
            return;
        }
        System.out.println("Usuwanie konta ID: " + selected.getId());
        accountRepo.deleteAccount(selected.getId());
        refreshList();
    }

    //Logowanie
    @FXML
    public void onLoginAccount() {
        Account selected = accountListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String decryptedPassword = CryptoUtil.decrypt(selected.getEncryptedPassword());
            char[] passwordChars = decryptedPassword.toCharArray();  // <-- konwersja na char[]
            RiotLauncher.login(selected.getUsername(), passwordChars);

            // Dla bezpieczeństwa od razu wyczyść tablicę po użyciu (opcjonalnie):
            Arrays.fill(passwordChars, '0');
        }
    }

}

