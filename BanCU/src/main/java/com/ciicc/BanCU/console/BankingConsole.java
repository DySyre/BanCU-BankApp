
package com.ciicc.BanCU.console;

import com.ciicc.BanCU.dto.AccountResponse;
import com.ciicc.BanCU.dto.BalanceResponse;
import com.ciicc.BanCU.dto.CreateAccountRequest;
import com.ciicc.BanCU.dto.TransactionResponse;
import com.ciicc.BanCU.service.AccountService;
import com.ciicc.BanCU.service.BalanceService;
import com.ciicc.BanCU.service.DepositService;
import com.ciicc.BanCU.service.TransactionHistoryService;
import com.ciicc.BanCU.service.TransferService;
import com.ciicc.BanCU.service.WithdrawalService;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "bancu.console.enabled",
        havingValue = "true"
)
public class BankingConsole implements CommandLineRunner {

    private final AccountService accountService;
    private final BalanceService balanceService;
    private final DepositService depositService;
    private final WithdrawalService withdrawalService;
    private final TransferService transferService;
    private final TransactionHistoryService historyService;

    @Override
    public void run(String... args) {
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("\n================================");
            System.out.println("       Welcome to BanCU");
            System.out.println("       Banking Console");
            System.out.println("================================");

            while (true) {
                printMenu();

                System.out.print("Select an option: ");
                String choice = scanner.nextLine().trim();

                if ("0".equals(choice)) {
                    System.out.println("Thank you for using BanCU!");
                    break;
                }

                try {
                    switch (choice) {
                        case "1" -> createAccount(scanner);
                        case "2" -> checkBalance(scanner);
                        case "3" -> deposit(scanner);
                        case "4" -> withdraw(scanner);
                        case "5" -> transfer(scanner);
                        case "6" -> transactionHistory(scanner);
                        case "7" -> listAllAccounts();

                        default -> System.out.println(
                                "Invalid option. Please try again."
                        );
                    }

                } catch (Exception ex) {
                    System.out.println(
                            "Operation failed: " + ex.getMessage()
                    );
                }

                System.out.println();
            }
        }
    }

    private void printMenu() {
        System.out.println("""

                -------- BanCU MENU --------
                1. Create Account
                2. Check Balance
                3. Deposit Money
                4. Withdraw Money
                5. Transfer Money
                6. Transaction History
                7. List All Accounts
                0. Exit
                ----------------------------
                """);
    }

    // 1. Create a new account
    private void createAccount(Scanner scanner) {
        CreateAccountRequest request = new CreateAccountRequest();

        System.out.print("Account holder name: ");
        request.setAccountHolder(scanner.nextLine().trim());

        System.out.print("Initial deposit (enter 0 if none): ");
        String initialDeposit = scanner.nextLine().trim();

        request.setInitialDeposit(parseAmount(initialDeposit));

        AccountResponse account =
                accountService.createAccount(request);

        System.out.println("\nAccount created successfully!");
        System.out.println("Account number: " + account.getAccountNumber());
        System.out.println("Account holder: " + account.getAccountHolder());
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Status: " + account.getStatus());
    }

    // 2. Check account balance
    private void checkBalance(Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        checkBalanceFor(accountNumber);
    }

    // 3. Deposit money
    private void deposit(Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        System.out.print("Amount to deposit: ");
        BigDecimal amount = parseAmount(scanner.nextLine());

        depositService.deposit(accountNumber, amount);

        System.out.println("Deposit successful.");
        checkBalanceFor(accountNumber);
    }

    // 4. Withdraw money
    private void withdraw(Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        System.out.print("Amount to withdraw: ");
        BigDecimal amount = parseAmount(scanner.nextLine());

        withdrawalService.withdraw(accountNumber, amount);

        System.out.println("Withdrawal successful.");
        checkBalanceFor(accountNumber);
    }

    // 5. Transfer money between accounts
    private void transfer(Scanner scanner) {
        System.out.print("Source account number: ");
        String source = scanner.nextLine().trim();

        System.out.print("Destination account number: ");
        String destination = scanner.nextLine().trim();

        System.out.print("Amount to transfer: ");
        BigDecimal amount = parseAmount(scanner.nextLine());

        transferService.transfer(source, destination, amount);

        System.out.println("Transfer successful.");

        checkBalanceFor(source);
        checkBalanceFor(destination);
    }

    // 6. Display paginated transaction history
    private void transactionHistory(Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        System.out.print("Page number (starting at 0): ");
        int page = Integer.parseInt(scanner.nextLine().trim());

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative."
            );
        }

        Page<TransactionResponse> result =
                historyService.getHistory(accountNumber, page, 10);

        if (result.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n----- Transaction History -----");

        for (TransactionResponse tx : result.getContent()) {
            System.out.println("-------------------------------");
            System.out.println("Reference: " + tx.getReferenceNumber());
            System.out.println("Type: " + tx.getType());
            System.out.println("Amount: " + tx.getAmount());
            System.out.println("Balance after: " + tx.getBalanceAfter());
            System.out.println(
                    "Related account: " + tx.getRelatedAccountNumber()
            );
            System.out.println("Date: " + tx.getCreatedAt());
        }

        System.out.printf(
                "Page %d of %d | Total transactions: %d%n",
                result.getNumber() + 1,
                result.getTotalPages(),
                result.getTotalElements()
        );
    }

    // 7. Display all accounts
    private void listAllAccounts() {
        List<AccountResponse> accounts =
                accountService.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("\nNo accounts found.");
            return;
        }

        System.out.println("\n================ ALL ACCOUNTS ================");

        System.out.printf(
                "%-18s %-25s %-15s %-12s%n",
                "ACCOUNT NUMBER",
                "ACCOUNT HOLDER",
                "BALANCE",
                "STATUS"
        );

        System.out.println(
                "---------------------------------------------------------------"
        );

        for (AccountResponse account : accounts) {
            System.out.printf(
                    "%-18s %-25s %-15s %-12s%n",
                    account.getAccountNumber(),
                    account.getAccountHolder(),
                    account.getBalance(),
                    account.getStatus()
            );
        }

        System.out.println(
                "---------------------------------------------------------------"
        );

        System.out.println("Total accounts: " + accounts.size());
    }

    // Display the current balance for an account
    private void checkBalanceFor(String accountNumber) {
        BalanceResponse balance =
                balanceService.getBalance(accountNumber);

        System.out.println(
                "Account: " + balance.getAccountNumber()
        );

        System.out.println(
                "Current balance: " + balance.getBalance()
        );
    }

    // Convert user input to BigDecimal
    private BigDecimal parseAmount(String input) {
        try {
            return new BigDecimal(input.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Enter a valid numeric amount, such as 100 or 100.50."
            );
        }
    }
}
