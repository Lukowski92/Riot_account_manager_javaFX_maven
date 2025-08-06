package pl.kalinowski.riotaccountmanager;

import java.util.List;

public interface AccountRepository {

    void addAccount(String username, String encryptedPassword);

    List<Account> getAllAccounts();

    void deleteAccount(int id);
}
