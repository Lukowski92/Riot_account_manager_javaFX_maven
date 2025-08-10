package pl.kalinowski.riotaccountmanager;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.prefs.Preferences;

public class MainController {

    private final AccountRepository accountRepo = new SQLiteAccountRepository();

    @FXML
    private ListView<Account> accountListView;

    @FXML
    public void initialize() {
        refreshList();

        accountListView.setCellFactory(lv -> {
            ListCell<Account> cell = new ListCell<>() {
                @Override
                protected void updateItem(Account item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getUsername());
                }
            };

            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty() && e.getClickCount() == 2) {
                    Account selected = cell.getItem();
                    onLoginAccount(selected);
                }
            });

            return cell;
        });
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

    //Logowanie przycisk
    @FXML
    public void onLoginAccountButton() {
        Account selected = accountListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String decryptedPassword = CryptoUtil.decrypt(selected.getEncryptedPassword());
            char[] passwordChars = decryptedPassword.toCharArray();  // <-- konwersja na char[]
            String riotClientPath = getRiotClientPath();
            RiotLauncher.login(selected.getUsername(), passwordChars, riotClientPath);

            // Dla bezpieczeństwa od razu wyczyść tablicę po użyciu
            Arrays.fill(passwordChars, '0');
        }
    }

    //lgowanie z listy
    @FXML
    public void onLoginAccount(Account selected) {
        if (selected != null) {
            String decryptedPassword = CryptoUtil.decrypt(selected.getEncryptedPassword());
            char[] passwordChars = decryptedPassword.toCharArray();  // <-- konwersja na char[]
            String riotClientPath = getRiotClientPath();
            RiotLauncher.login(selected.getUsername(), passwordChars, riotClientPath);

            // Dla bezpieczeństwa od razu wyczyść tablicę po użyciu
            Arrays.fill(passwordChars, '0');
        }
    }

    private Preferences prefs = Preferences.userNodeForPackage(MainController.class);

    public void onConfigurePath() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Konfiguracja ścieżki Riot Client");

        TextField pathField = new TextField(getRiotClientPath());
        pathField.setPrefWidth(400);

        Button browseButton = new Button("Wybierz plik...");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz RiotClientServices.exe");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Executable files", "*.exe")
            );
            File selectedFile = fileChooser.showOpenDialog(dialogStage);
            if (selectedFile != null) {
                pathField.setText(selectedFile.getAbsolutePath());
            }
        });

        Button saveButton = new Button("Zapisz");
        saveButton.setOnAction(e -> {
            String newPath = pathField.getText().trim();
            File file = new File(newPath);
            if (file.exists() && file.isFile()) {
                prefs.put("riotClientPath", newPath);
                System.out.println("Zapisano nową ścieżkę: " + newPath);
                dialogStage.close();
            } else {
                System.out.println("Podana ścieżka jest nieprawidłowa lub plik nie istnieje.");
            }
        });
        Button cancelButton = new Button("Anuluj");
        cancelButton.setOnAction(e -> dialogStage.close());

        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox layout = new VBox(10, pathField, browseButton, buttons);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    // Przykład odczytu ścieżki gdziekolwiek w kodzie
    public String getRiotClientPath() {
        return prefs.get("riotClientPath", "C:\\Riot Games\\Riot Client\\RiotClientServices.exe");
    }

}




