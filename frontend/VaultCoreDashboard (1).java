import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * VaultCore – Futuristic ATM Dashboard
 * Pure JavaFX | setStyle() only | No CSS / FXML / DB
 * All buttons functional | User creation | No NEFT/POS entries
 */
public class VaultCoreDashboard extends Application {

    // ── Palette ────────────────────────────────────────────────
    private static final String BG_DARK      = "#050d1a";
    private static final String BG_CARD      = "#0a1628";
    private static final String BG_SIDEBAR   = "#060e1c";
    private static final String NEON_GREEN   = "#00ff88";
    private static final String NEON_DIM     = "#00cc6a";
    private static final String TEXT_PRIMARY = "#e0ffe8";
    private static final String TEXT_MUTED   = "#5a8a6a";
    private static final String ACCENT_BLUE  = "#0077ff";
    private static final String ACCENT_GOLD  = "#ffd700";
    private static final String ACCENT_CYAN  = "#00ffcc";
    private static final String ACCENT_RED   = "#ff4444";

    // ── User model ─────────────────────────────────────────────
    static class BankUser {
        String name, initials, acct;
        double balance, savings;
        int score;
        BankUser(String name, String initials, String acct, double balance, double savings, int score) {
            this.name = name; this.initials = initials; this.acct = acct;
            this.balance = balance; this.savings = savings; this.score = score;
        }
    }

    // ── Transaction model ──────────────────────────────────────
    static class Transaction {
        String icon, type, date, amount, status, color;
        Transaction(String icon, String type, String date, String amount, String status, String color) {
            this.icon = icon; this.type = type; this.date = date;
            this.amount = amount; this.status = status; this.color = color;
        }
    }

    // ── State ──────────────────────────────────────────────────
    private List<BankUser> users = new ArrayList<>();
    private BankUser activeUser;
    private List<Transaction> transactions = new ArrayList<>();
    private Button activeSidebarBtn = null;
    private StackPane contentArea;

    // Live labels updated on balance change
    private Label headerWelcome;
    private Label sidebarName;
    private Label sidebarAcct;
    private Label sidebarInitials;

    @Override
    public void start(Stage stage) {
        // Seed data
        users.add(new BankUser("Arjun Sharma", "AS", "••••  ••••  4521", 125000, 480000, 812));
        activeUser = users.get(0);
        transactions.add(new Transaction("💸", "ATM Withdrawal",  "16 May 2026", "- ₹5,000",  "COMPLETED", NEON_GREEN));
        transactions.add(new Transaction("📲", "UPI Transfer",     "15 May 2026", "- ₹2,300",  "COMPLETED", NEON_GREEN));
        transactions.add(new Transaction("💰", "Cash Deposit",     "14 May 2026", "+ ₹10,000", "PROCESSED", ACCENT_BLUE));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_DARK + ";");

        HBox header    = buildHeader(stage);
        VBox sidebar   = buildSidebar(stage);
        contentArea    = new StackPane();
        contentArea.getChildren().add(buildDashboardCenter());
        HBox statusBar = buildStatusBar();

        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(contentArea);
        root.setBottom(statusBar);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(900), root);
        fadeIn.setFromValue(0); fadeIn.setToValue(1); fadeIn.play();

        Scene scene = new Scene(root, 1280, 780);
        stage.setTitle("VaultCore — Cyber Banking Interface");
        stage.setScene(scene);
        stage.setMinWidth(1100);
        stage.setMinHeight(680);
        stage.show();
    }

    // ═══════════════════════════════════════════════════════════
    //  HEADER
    // ═══════════════════════════════════════════════════════════
    private HBox buildHeader(Stage stage) {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 28, 14, 28));
        header.setStyle(
            "-fx-background-color: " + BG_CARD + ";" +
            "-fx-border-color: " + NEON_GREEN + ";" +
            "-fx-border-width: 0 0 2 0;"
        );
        DropShadow glow = new DropShadow(18, Color.web(NEON_GREEN));
        glow.setSpread(0.15);
        header.setEffect(glow);

        Label logo = new Label("⬡ VaultCore");
        logo.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + NEON_GREEN + ";");
        applyGlowLabel(logo, NEON_GREEN, 14);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerWelcome = new Label("Welcome,  " + activeUser.name);
        headerWelcome.setStyle("-fx-font-size: 14px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + ";");

        Label clock = new Label();
        clock.setStyle(
            "-fx-font-size: 14px; -fx-font-family: 'Courier New'; -fx-text-fill: " + NEON_GREEN + ";" +
            "-fx-padding: 4 12 4 12; -fx-background-color: #001a0a;" +
            "-fx-border-color: " + NEON_DIM + "; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6;"
        );
        startClock(clock);

        Label notif = new Label("🔔");
        notif.setStyle("-fx-font-size: 20px; -fx-cursor: hand;");
        addPulseEffect(notif);
        Tooltip.install(notif, new Tooltip("No new notifications"));

        Button logoutBtn = styledOutlineButton("  ⏻  LOGOUT", NEON_GREEN);
        logoutBtn.setOnAction(e -> showLogoutDialog(stage));

        header.getChildren().addAll(logo, spacer, headerWelcome, clock, notif, logoutBtn);
        return header;
    }

    // ═══════════════════════════════════════════════════════════
    //  SIDEBAR
    // ═══════════════════════════════════════════════════════════
    private VBox buildSidebar(Stage stage) {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(210);
        sidebar.setPadding(new Insets(24, 12, 24, 12));
        sidebar.setStyle(
            "-fx-background-color: " + BG_SIDEBAR + ";" +
            "-fx-border-color: " + NEON_GREEN + ";" +
            "-fx-border-width: 0 2 0 0;"
        );

        // Avatar
        StackPane avatarPane = new StackPane();
        Circle circle = new Circle(34);
        circle.setStyle("-fx-fill: #001a0a; -fx-stroke: " + NEON_GREEN + "; -fx-stroke-width: 2;");
        sidebarInitials = new Label(activeUser.initials);
        sidebarInitials.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + NEON_GREEN + ";");
        avatarPane.getChildren().addAll(circle, sidebarInitials);
        applyGlowEffect(avatarPane, NEON_GREEN, 12);

        sidebarName = new Label(activeUser.name);
        sidebarName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + ";");
        sidebarAcct = new Label("A/C: " + activeUser.acct);
        sidebarAcct.setStyle("-fx-font-size: 10px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + ";");

        VBox avatarBlock = new VBox(6);
        avatarBlock.setAlignment(Pos.CENTER);
        avatarBlock.setPadding(new Insets(10, 0, 16, 0));
        avatarBlock.getChildren().addAll(avatarPane, sidebarName, sidebarAcct);
        sidebar.getChildren().add(avatarBlock);
        sidebar.getChildren().add(buildDivider());

        String[][] navItems = {
            {"🏠", "Dashboard"},
            {"💸", "Withdraw"},
            {"💰", "Deposit"},
            {"📊", "Balance Inquiry"},
            {"📋", "Transactions"},
            {"🔄", "Transfer Money"},
            {"👤", "Create User"},
            {"⚙",  "Settings"},
            {"⏻",  "Logout"},
        };

        for (String[] item : navItems) {
            Button btn = createSidebarButton(item[0], item[1]);
            sidebar.getChildren().add(btn);
            if (item[1].equals("Dashboard")) {
                setActiveButton(btn);
            }
            btn.setOnAction(e -> {
                if (item[1].equals("Logout")) {
                    showLogoutDialog(stage);
                    return;
                }
                setActiveButton(btn);
                handleNavigation(item[1]);
            });
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Label version = new Label("v2.4.1 · Secure Mode");
        version.setStyle("-fx-font-size: 10px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + ";");
        sidebar.getChildren().addAll(spacer, version);
        return sidebar;
    }

    // ═══════════════════════════════════════════════════════════
    //  NAVIGATION
    // ═══════════════════════════════════════════════════════════
    private void handleNavigation(String section) {
        Node newContent = switch (section) {
            case "Dashboard"       -> buildDashboardCenter();
            case "Withdraw"        -> buildWithdrawPanel();
            case "Deposit"         -> buildDepositPanel();
            case "Balance Inquiry" -> buildBalancePanel();
            case "Transactions"    -> buildTransactionsPanel();
            case "Transfer Money"  -> buildTransferPanel();
            case "Create User"     -> buildCreateUserPanel();
            case "Settings"        -> buildSettingsPanel();
            default                -> buildDashboardCenter();
        };
        FadeTransition fade = new FadeTransition(Duration.millis(280), newContent);
        fade.setFromValue(0); fade.setToValue(1);
        contentArea.getChildren().setAll(newContent);
        fade.play();
    }

    // ═══════════════════════════════════════════════════════════
    //  DASHBOARD CENTER
    // ═══════════════════════════════════════════════════════════
    private ScrollPane buildDashboardCenter() {
        VBox center = new VBox(24);
        center.setPadding(new Insets(28, 32, 28, 32));
        center.setStyle("-fx-background-color: " + BG_DARK + ";");

        Label title = new Label("⬡  DASHBOARD OVERVIEW");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + NEON_GREEN + ";");
        applyGlowLabel(title, NEON_GREEN, 10);

        // Bank cards
        GridPane cards = new GridPane();
        cards.setHgap(18); cards.setVgap(18);
        String[][] cardData = {
            {"💳", "Current Balance",  fmtRupee(activeUser.balance), NEON_GREEN,  "+2.4% this month"},
            {"🏦", "Savings Balance",  fmtRupee(activeUser.savings), ACCENT_BLUE, "High-yield account"},
            {"⭐", "Credit Score",     activeUser.score + " / 900",  ACCENT_GOLD, "Excellent standing"},
            {"🔐", "ATM Card Status",  "● ACTIVE",                   ACCENT_CYAN, "Visa Debit — " + activeUser.acct.substring(activeUser.acct.length() - 4)},
        };
        for (int i = 0; i < cardData.length; i++) {
            VBox card = createBankCard(cardData[i][0], cardData[i][1], cardData[i][2], cardData[i][3], cardData[i][4]);
            cards.add(card, i % 2, i / 2);
            GridPane.setHgrow(card, Priority.ALWAYS);
        }

        // Quick actions
        Label qaTitle = new Label("⚡  QUICK ACTIONS");
        qaTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + ";");
        HBox qaButtons = new HBox(14);
        qaButtons.setAlignment(Pos.CENTER_LEFT);

        Button wdBtn  = buildActionButton("💸", "Withdraw",       NEON_GREEN);
        Button depBtn = buildActionButton("💰", "Deposit",        ACCENT_BLUE);
        Button trBtn  = buildActionButton("🔄", "Send Money",     ACCENT_GOLD);
        Button msBtn  = buildActionButton("📋", "Mini Statement", ACCENT_CYAN);

        wdBtn.setOnAction(e  -> navigateTo("Withdraw"));
        depBtn.setOnAction(e -> navigateTo("Deposit"));
        trBtn.setOnAction(e  -> navigateTo("Transfer Money"));
        msBtn.setOnAction(e  -> navigateTo("Transactions"));
        qaButtons.getChildren().addAll(wdBtn, depBtn, trBtn, msBtn);

        VBox quickActions = new VBox(14);
        quickActions.getChildren().addAll(qaTitle, qaButtons);

        // Recent transactions (3 only on dashboard)
        VBox txnPanel = buildRecentTransactionsBlock();

        center.getChildren().addAll(title, cards, quickActions, txnPanel);

        ScrollPane sp = wrapInScrollPane(center);
        return sp;
    }

    private void navigateTo(String section) {
        // Also update sidebar active button
        handleNavigation(section);
    }

    private VBox buildRecentTransactionsBlock() {
        VBox panel = new VBox(14);
        Label title = new Label("🕑  RECENT TRANSACTIONS");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + ";");
        panel.getChildren().add(title);
        int count = Math.min(3, transactions.size());
        for (int i = 0; i < count; i++) {
            Transaction t = transactions.get(i);
            panel.getChildren().add(createTransactionCard(t.icon, t.type, t.date, t.amount, t.status, t.color));
        }
        if (transactions.isEmpty()) {
            Label empty = new Label("No transactions yet.");
            empty.setStyle("-fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-size: 13px;");
            panel.getChildren().add(empty);
        }
        return panel;
    }

    // ═══════════════════════════════════════════════════════════
    //  WITHDRAW PANEL
    // ═══════════════════════════════════════════════════════════
    private ScrollPane buildWithdrawPanel() {
        VBox panel = panelBase("💸 Withdraw Funds", "Enter amount to withdraw from your account.", NEON_GREEN);

        Label balLbl = new Label("Available Balance: " + fmtRupee(activeUser.balance));
        balLbl.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New'; -fx-text-fill: " + NEON_GREEN + ";");

        TextField amtField = styledField("Enter amount (₹)");
        PasswordField pinField = styledPinField("Enter 4-digit PIN");

        Label msgLabel = alertLabel("");

        Button btn = buildActionButton("💸", "Withdraw Now", NEON_GREEN);
        btn.setOnAction(e -> {
            msgLabel.setText(""); msgLabel.setStyle(alertStyle(NEON_GREEN));
            String amtStr = amtField.getText().trim();
            String pin    = pinField.getText().trim();
            if (amtStr.isEmpty()) { setAlert(msgLabel, "⚠ Please enter an amount.", ACCENT_GOLD); return; }
            double amt;
            try { amt = Double.parseDouble(amtStr); } catch (Exception ex) { setAlert(msgLabel, "⚠ Invalid amount.", ACCENT_GOLD); return; }
            if (amt <= 0)              { setAlert(msgLabel, "⚠ Amount must be greater than ₹0.", ACCENT_GOLD); return; }
            if (!pin.equals("1234"))   { setAlert(msgLabel, "⛔ Incorrect PIN. (Hint: 1234)", ACCENT_RED); return; }
            if (amt > activeUser.balance) { setAlert(msgLabel, "⛔ Insufficient balance.", ACCENT_RED); return; }
            activeUser.balance -= amt;
            updateUserInList();
            addTransaction("💸", "ATM Withdrawal", "- " + fmtRupee(amt), "COMPLETED", NEON_GREEN);
            setAlert(msgLabel, "✅ " + fmtRupee(amt) + " withdrawn successfully!", NEON_GREEN);
            balLbl.setText("Available Balance: " + fmtRupee(activeUser.balance));
            amtField.clear(); pinField.clear();
            refreshHeaderSidebar();
        });

        panel.getChildren().addAll(balLbl, fieldLabel("AMOUNT (₹)"), amtField, fieldLabel("ATM PIN"), pinField, btn, msgLabel);
        return wrapInScrollPane(panel);
    }

    // ═══════════════════════════════════════════════════════════
    //  DEPOSIT PANEL
    // ═══════════════════════════════════════════════════════════
    private ScrollPane buildDepositPanel() {
        VBox panel = panelBase("💰 Deposit Funds", "Deposit cash or cheque into your account.", ACCENT_BLUE);

        Label balLbl = new Label("Current Balance: " + fmtRupee(activeUser.balance));
        balLbl.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New'; -fx-text-fill: " + ACCENT_BLUE + ";");

        TextField amtField = styledField("Enter amount (₹)");
        Label msgLabel = alertLabel("");

        Button btn = buildActionButton("💰", "Deposit Now", ACCENT_BLUE);
        btn.setOnAction(e -> {
            String amtStr = amtField.getText().trim();
            if (amtStr.isEmpty()) { setAlert(msgLabel, "⚠ Please enter an amount.", ACCENT_GOLD); return; }
            double amt;
            try { amt = Double.parseDouble(amtStr); } catch (Exception ex) { setAlert(msgLabel, "⚠ Invalid amount.", ACCENT_GOLD); return; }
            if (amt <= 0) { setAlert(msgLabel, "⚠ Amount must be greater than ₹0.", ACCENT_GOLD); return; }
            activeUser.balance += amt;
            updateUserInList();
            addTransaction("💰", "Cash Deposit", "+ " + fmtRupee(amt), "PROCESSED", ACCENT_BLUE);
            setAlert(msgLabel, "✅ " + fmtRupee(amt) + " deposited successfully!", ACCENT_BLUE);
            balLbl.setText("Current Balance: " + fmtRupee(activeUser.balance));
            amtField.clear();
            refreshHeaderSidebar();
        });

        panel.getChildren().addAll(balLbl, fieldLabel("AMOUNT (₹)"), amtField, btn, msgLabel);
        return wrapInScrollPane(panel);
    }

    // ═══════════════════════════════════════════════════════════
    //  BALANCE INQUIRY PANEL
    // ═══════════════════════════════════════════════════════════
    private ScrollPane buildBalancePanel() {
        VBox panel = panelBase("📊 Balance Inquiry", "Your current & savings balance at a glance.", ACCENT_GOLD);

        String[][] rows = {
            {"Current Account",  fmtRupee(activeUser.balance),                              NEON_GREEN},
            {"Savings Account",  fmtRupee(activeUser.savings),                              ACCENT_BLUE},
            {"Total Wealth",     fmtRupee(activeUser.balance + activeUser.savings),         ACCENT_GOLD},
            {"Credit Score",     activeUser.score + " / 900",                               ACCENT_CYAN},
            {"Account Status",   "● ACTIVE",                                                NEON_GREEN},
        };

        for (String[] row : rows) {
            HBox card = new HBox();
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(16, 22, 16, 22));
            card.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                "-fx-border-color: " + row[2] + "33;" +
                "-fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12;"
            );
            DropShadow sh = new DropShadow(14, Color.web(row[2])); sh.setSpread(0.05); card.setEffect(sh);

            Label lbl = new Label(row[0]);
            lbl.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + ";");
            Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
            Label val = new Label(row[1]);
            val.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + row[2] + ";");
            card.getChildren().addAll(lbl, sp, val);
            panel.getChildren().add(card);
        }
        return wrapInScrollPane(panel);
    }

    // ═══════════════════════════════════════════════════════════
    //  FULL TRANSACTIONS PANEL
    // ═══════════════════════════════════════════════════════════
    private ScrollPane buildTransactionsPanel() {
        VBox panel = panelBase("📋 Transaction History", "Full history of all your recent transactions.", ACCENT_CYAN);

        if (transactions.isEmpty()) {
            Label empty = new Label("No transactions found.");
            empty.setStyle("-fx-font-size: 14px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + ";");
            panel.getChildren().add(empty);
        } else {
            for (Transaction t : transactions) {
                panel.getChildren().add(createTransactionCard(t.icon, t.type, t.date, t.amount, t.status, t.color));
            }
        }
        return wrapInScrollPane(panel);
    }

    // ═══════════════════════════════════════════════════════════
    //  TRANSFER MONEY PANEL
    // ═══════════════════════════════════════════════════════════
    private ScrollPane buildTransferPanel() {
        VBox panel = panelBase("🔄 Transfer Money", "Instantly transfer funds via NEFT / IMPS / UPI.", ACCENT_GOLD);

        Label balLbl = new Label("Available Balance: " + fmtRupee(activeUser.balance));
        balLbl.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New'; -fx-text-fill: " + ACCENT_GOLD + ";");

        TextField nameField = styledField("Beneficiary name");
        TextField acctField = styledField("Account number / UPI ID");
        TextField amtField  = styledField("Amount (₹)");
        PasswordField pinField = styledPinField("4-digit PIN");
        Label msgLabel = alertLabel("");

        Button btn = buildActionButton("🔄", "Transfer Now", ACCENT_GOLD);
        btn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String acct = acctField.getText().trim();
            String amtStr = amtField.getText().trim();
            String pin    = pinField.getText().trim();
            if (name.isEmpty()) { setAlert(msgLabel, "⚠ Enter beneficiary name.", ACCENT_GOLD); return; }
            if (acct.isEmpty()) { setAlert(msgLabel, "⚠ Enter account / UPI ID.", ACCENT_GOLD); return; }
            if (amtStr.isEmpty()) { setAlert(msgLabel, "⚠ Enter amount.", ACCENT_GOLD); return; }
            double amt;
            try { amt = Double.parseDouble(amtStr); } catch (Exception ex) { setAlert(msgLabel, "⚠ Invalid amount.", ACCENT_GOLD); return; }
            if (amt <= 0)                 { setAlert(msgLabel, "⚠ Amount must be greater than ₹0.", ACCENT_GOLD); return; }
            if (!pin.equals("1234"))      { setAlert(msgLabel, "⛔ Incorrect PIN. (Hint: 1234)", ACCENT_RED); return; }
            if (amt > activeUser.balance) { setAlert(msgLabel, "⛔ Insufficient balance.", ACCENT_RED); return; }
            activeUser.balance -= amt;
            updateUserInList();
            addTransaction("🔄", "UPI to " + name, "- " + fmtRupee(amt), "COMPLETED", ACCENT_GOLD);
            setAlert(msgLabel, "✅ " + fmtRupee(amt) + " sent to " + name + " successfully!", NEON_GREEN);
            balLbl.setText("Available Balance: " + fmtRupee(activeUser.balance));
            nameField.clear(); acctField.clear(); amtField.clear(); pinField.clear();
            refreshHeaderSidebar();
        });

        panel.getChildren().addAll(
            balLbl,
            fieldLabel("BENEFICIARY NAME"), nameField,
            fieldLabel("ACCOUNT / UPI ID"), acctField,
            fieldLabel("AMOUNT (₹)"),       amtField,
            fieldLabel("ATM PIN"),           pinField,
            btn, msgLabel
        );
        return wrapInScrollPane(panel);
    }

    // ═══════════════════════════════════════════════════════════
    //  CREATE USER PANEL
    // ═══════════════════════════════════════════════════════════
    private ScrollPane buildCreateUserPanel() {
        VBox panel = panelBase("👤 Create New User", "Register a new banking account holder.", NEON_GREEN);

        TextField nameField    = styledField("Full name (e.g. Priya Mehta)");
        TextField acctField    = styledField("Last 4 digits of account number");
        TextField balField     = styledField("Initial current balance (₹)");
        TextField savField     = styledField("Initial savings balance (₹)");
        Label msgLabel = alertLabel("");

        Button createBtn = buildActionButton("👤", "Create User", NEON_GREEN);
        createBtn.setOnAction(e -> {
            String name    = nameField.getText().trim();
            String acctNum = acctField.getText().trim();
            String balStr  = balField.getText().trim();
            String savStr  = savField.getText().trim();

            if (name.isEmpty())         { setAlert(msgLabel, "⚠ Enter full name.", ACCENT_GOLD); return; }
            if (acctNum.length() < 1)   { setAlert(msgLabel, "⚠ Enter account last 4 digits.", ACCENT_GOLD); return; }
            if (balStr.isEmpty())       { setAlert(msgLabel, "⚠ Enter initial balance.", ACCENT_GOLD); return; }
            double bal, sav = 0;
            try { bal = Double.parseDouble(balStr); } catch (Exception ex) { setAlert(msgLabel, "⚠ Invalid balance.", ACCENT_GOLD); return; }
            try { if (!savStr.isEmpty()) sav = Double.parseDouble(savStr); } catch (Exception ex) { sav = 0; }

            String[] parts = name.split("\\s+");
            StringBuilder initSb = new StringBuilder();
            for (String p : parts) { if (!p.isEmpty()) initSb.append(Character.toUpperCase(p.charAt(0))); }
            String initials = initSb.length() >= 2 ? initSb.substring(0, 2) : initSb.toString();
            String last4 = acctNum.length() >= 4 ? acctNum.substring(acctNum.length() - 4) : acctNum;

            BankUser newUser = new BankUser(name, initials, "••••  ••••  " + last4, bal, sav, 750);
            users.add(newUser);
            setAlert(msgLabel, "✅ User \"" + name + "\" created successfully!", NEON_GREEN);
            nameField.clear(); acctField.clear(); balField.clear(); savField.clear();

            // Refresh list below
            handleNavigation("Create User");
        });

        panel.getChildren().addAll(
            fieldLabel("FULL NAME"),                  nameField,
            fieldLabel("ACCOUNT NO. (LAST 4 DIGITS)"), acctField,
            fieldLabel("INITIAL CURRENT BALANCE (₹)"), balField,
            fieldLabel("INITIAL SAVINGS BALANCE (₹)"), savField,
            createBtn, msgLabel
        );

        // User list
        if (!users.isEmpty()) {
            Label listTitle = new Label("REGISTERED USERS");
            listTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-padding: 20 0 8 0;");
            panel.getChildren().add(listTitle);

            for (BankUser u : users) {
                HBox card = new HBox(14);
                card.setAlignment(Pos.CENTER_LEFT);
                card.setPadding(new Insets(12, 16, 12, 16));
                boolean isActive = u == activeUser;
                card.setStyle(
                    "-fx-background-color: " + (isActive ? "#00ff8812" : BG_CARD) + ";" +
                    "-fx-border-color: " + (isActive ? NEON_GREEN : "#ffffff12") + ";" +
                    "-fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand;"
                );

                StackPane av = new StackPane();
                Circle c = new Circle(22);
                c.setStyle("-fx-fill: #001a0a; -fx-stroke: " + NEON_GREEN + "; -fx-stroke-width: 2;");
                Label ini = new Label(u.initials);
                ini.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + NEON_GREEN + ";");
                av.getChildren().addAll(c, ini);

                VBox info = new VBox(3);
                Label nm = new Label(u.name);
                nm.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + ";");
                Label ac = new Label("A/C: " + u.acct + "  ·  Bal: " + fmtRupee(u.balance));
                ac.setStyle("-fx-font-size: 10px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + ";");
                info.getChildren().addAll(nm, ac);

                Region sp2 = new Region(); HBox.setHgrow(sp2, Priority.ALWAYS);

                card.getChildren().addAll(av, info, sp2);
                if (isActive) {
                    Label tag = new Label("ACTIVE");
                    tag.setStyle("-fx-font-size: 10px; -fx-font-family: 'Courier New'; -fx-text-fill: " + NEON_GREEN + "; -fx-padding: 2 8 2 8; -fx-border-color: " + NEON_GREEN + "44; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4;");
                    card.getChildren().add(tag);
                } else {
                    Button switchBtn = styledOutlineButton("Switch", ACCENT_BLUE);
                    switchBtn.setOnAction(ev -> {
                        activeUser = u;
                        refreshHeaderSidebar();
                        handleNavigation("Create User");
                    });
                    card.getChildren().add(switchBtn);
                }

                // Hover
                card.setOnMouseEntered(ev -> card.setStyle(
                    "-fx-background-color: #00ff8818; -fx-border-color: " + NEON_GREEN + "44;" +
                    "-fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand;"
                ));
                card.setOnMouseExited(ev -> card.setStyle(
                    "-fx-background-color: " + (isActive ? "#00ff8812" : BG_CARD) + ";" +
                    "-fx-border-color: " + (isActive ? NEON_GREEN : "#ffffff12") + ";" +
                    "-fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand;"
                ));
                panel.getChildren().add(card);
            }
        }

        return wrapInScrollPane(panel);
    }

    // ═══════════════════════════════════════════════════════════
    //  SETTINGS PANEL
    // ═══════════════════════════════════════════════════════════
    private ScrollPane buildSettingsPanel() {
        VBox panel = panelBase("⚙ Account Settings", "Manage PIN, card limits, and profile details.", TEXT_MUTED);

        // PIN change card
        VBox pinCard = cardBox(NEON_GREEN);
        Label pinTitle = new Label("🔑  Change PIN");
        pinTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + ";");
        PasswordField curPin = styledPinField("Current PIN");
        PasswordField newPin = styledPinField("New 4-digit PIN");
        PasswordField cfmPin = styledPinField("Confirm new PIN");
        Label pinMsg = alertLabel("");
        Button pinBtn = buildActionButton("🔑", "Update PIN", NEON_GREEN);
        pinBtn.setOnAction(e -> {
            if (!curPin.getText().equals("1234"))              { setAlert(pinMsg, "⛔ Current PIN incorrect. (Hint: 1234)", ACCENT_RED); return; }
            if (newPin.getText().length() < 4)                 { setAlert(pinMsg, "⚠ New PIN must be 4 digits.", ACCENT_GOLD); return; }
            if (!newPin.getText().equals(cfmPin.getText()))    { setAlert(pinMsg, "⚠ PINs do not match.", ACCENT_GOLD); return; }
            setAlert(pinMsg, "✅ PIN updated successfully!", NEON_GREEN);
            curPin.clear(); newPin.clear(); cfmPin.clear();
        });
        pinCard.getChildren().addAll(pinTitle, fieldLabel("CURRENT PIN"), curPin, fieldLabel("NEW PIN"), newPin, fieldLabel("CONFIRM NEW PIN"), cfmPin, pinBtn, pinMsg);

        // Profile details card
        VBox profileCard = cardBox(ACCENT_BLUE);
        Label profTitle = new Label("📇  Profile Details");
        profTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + ";");
        profileCard.getChildren().add(profTitle);

        String[][] rows = {
            {"Account Holder", activeUser.name},
            {"Account Number", activeUser.acct},
            {"Card Type",      "Visa Debit"},
            {"Card Status",    "● ACTIVE"},
            {"2FA",            "ENABLED"},
            {"Encryption",     "AES-256"},
        };
        for (String[] row : rows) {
            HBox rowBox = new HBox();
            Label lbl = new Label(row[0]);
            lbl.setStyle("-fx-font-size: 12px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + ";");
            Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
            Label val = new Label(row[1]);
            val.setStyle("-fx-font-size: 12px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + ";");
            rowBox.getChildren().addAll(lbl, sp, val);
            rowBox.setPadding(new Insets(6, 0, 6, 0));
            rowBox.setStyle("-fx-border-color: #ffffff08; -fx-border-width: 0 0 1 0;");
            profileCard.getChildren().add(rowBox);
        }

        panel.getChildren().addAll(pinCard, profileCard);
        return wrapInScrollPane(panel);
    }

    // ═══════════════════════════════════════════════════════════
    //  LOGOUT DIALOG
    // ═══════════════════════════════════════════════════════════
    private void showLogoutDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Logout");

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(36, 40, 36, 40));
        box.setStyle(
            "-fx-background-color: " + BG_CARD + ";" +
            "-fx-border-color: " + ACCENT_RED + "44;" +
            "-fx-border-width: 2; -fx-border-radius: 16; -fx-background-radius: 16;"
        );
        DropShadow sh = new DropShadow(40, Color.web(ACCENT_RED)); sh.setSpread(0.08); box.setEffect(sh);

        Label title = new Label("⏻  Confirm Logout");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + ACCENT_RED + ";");
        applyGlowLabel(title, ACCENT_RED, 10);

        Label msg = new Label("Are you sure you want to end your\nsession securely?");
        msg.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + "; -fx-text-alignment: center; -fx-alignment: center;");
        msg.setTextAlignment(TextAlignment.CENTER);

        HBox btns = new HBox(16);
        btns.setAlignment(Pos.CENTER);
        Button yes = buildActionButton("⏻", "Yes, Logout", ACCENT_RED);
        Button no  = styledOutlineButton("Cancel", TEXT_MUTED);
        yes.setOnAction(e -> owner.close());
        no.setOnAction(e  -> dialog.close());
        btns.getChildren().addAll(yes, no);

        box.getChildren().addAll(title, msg, btns);
        Scene sc = new Scene(box);
        sc.setFill(Color.TRANSPARENT);
        dialog.setScene(sc);
        dialog.sizeToScene();
        dialog.centerOnScreen();
        dialog.show();
    }

    // ═══════════════════════════════════════════════════════════
    //  STATUS BAR
    // ═══════════════════════════════════════════════════════════
    private HBox buildStatusBar() {
        HBox bar = new HBox(0);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(8, 24, 8, 24));
        bar.setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + NEON_GREEN + "; -fx-border-width: 2 0 0 0;");

        String[] statuses = {
            "🔒  Secure Server: CONNECTED",
            "🔐  Encryption: AES-256 ENABLED",
            "⚡  System Status: ACTIVE",
            "📡  Network: STABLE",
        };
        for (int i = 0; i < statuses.length; i++) {
            Label lbl = new Label(statuses[i]);
            lbl.setStyle("-fx-font-size: 10px; -fx-font-family: 'Courier New'; -fx-text-fill: " + NEON_GREEN + "; -fx-padding: 2 14 2 0;");
            bar.getChildren().add(lbl);
            if (i < statuses.length - 1) {
                Label sep = new Label("|");
                sep.setStyle("-fx-text-fill: " + TEXT_MUTED + "; -fx-font-family: 'Courier New'; -fx-font-size: 10px; -fx-padding: 0 14 0 0;");
                bar.getChildren().add(sep);
            }
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label ticker = new Label("NIFTY 50  ▲ 24,185  |  SENSEX  ▲ 79,802  |  USD/INR  ▼ 83.42  |  GOLD  ▲ ₹72,400/10g");
        ticker.setStyle("-fx-font-size: 10px; -fx-font-family: 'Courier New'; -fx-text-fill: " + ACCENT_GOLD + ";");
        TranslateTransition ta = new TranslateTransition(Duration.seconds(18), ticker);
        ta.setFromX(300); ta.setToX(-700); ta.setCycleCount(Animation.INDEFINITE); ta.play();

        bar.getChildren().addAll(spacer, ticker);
        return bar;
    }

    // ═══════════════════════════════════════════════════════════
    //  COMPONENT FACTORIES
    // ═══════════════════════════════════════════════════════════

    private VBox panelBase(String titleStr, String subtitleStr, String color) {
        VBox panel = new VBox(14);
        panel.setPadding(new Insets(48, 48, 48, 48));
        panel.setStyle("-fx-background-color: " + BG_DARK + ";");

        Label title = new Label(titleStr);
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + color + ";");
        applyGlowLabel(title, color, 14);

        Label sub = new Label(subtitleStr);
        sub.setStyle("-fx-font-size: 14px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + "; -fx-padding: 0 0 14 0;");

        panel.getChildren().addAll(title, sub);
        return panel;
    }

    private VBox cardBox(String color) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20, 22, 20, 22));
        box.setStyle(
            "-fx-background-color: " + BG_CARD + ";" +
            "-fx-border-color: " + color + "33; -fx-border-width: 1; -fx-border-radius: 14; -fx-background-radius: 14;"
        );
        DropShadow sh = new DropShadow(16, Color.web(color)); sh.setSpread(0.05); box.setEffect(sh);
        return box;
    }

    private VBox createBankCard(String icon, String title, String value, String color, String subtitle) {
        VBox card = new VBox(10);
        card.setPrefWidth(260);
        card.setPadding(new Insets(22));
        card.setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: " + color + "33; -fx-border-width: 1; -fx-border-radius: 16; -fx-background-radius: 16;");
        DropShadow shadow = new DropShadow(22, Color.web(color)); shadow.setSpread(0.08); card.setEffect(shadow);

        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label iconL = new Label(icon); iconL.setStyle("-fx-font-size: 22px;");
        Label titleL = new Label(title.toUpperCase());
        titleL.setStyle("-fx-font-size: 11px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + ";");
        topRow.getChildren().addAll(iconL, titleL);

        Label valueL = new Label(value);
        valueL.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + color + ";");
        addPulseEffect(valueL);

        Rectangle div = new Rectangle(); div.setHeight(1.5); div.setFill(Color.web(color + "55")); div.widthProperty().bind(card.widthProperty().subtract(44));
        Label sub = new Label(subtitle); sub.setStyle("-fx-font-size: 11px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + ";");
        card.getChildren().addAll(topRow, valueL, div, sub);

        card.setOnMouseEntered(e -> { ScaleTransition st = new ScaleTransition(Duration.millis(180), card); st.setToX(1.04); st.setToY(1.04); st.play(); DropShadow h = new DropShadow(34, Color.web(color)); h.setSpread(0.18); card.setEffect(h); });
        card.setOnMouseExited(e  -> { ScaleTransition st = new ScaleTransition(Duration.millis(180), card); st.setToX(1.0); st.setToY(1.0); st.play(); card.setEffect(shadow); });
        return card;
    }

    private HBox createTransactionCard(String icon, String type, String date, String amount, String status, String color) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14, 20, 14, 20));
        card.setStyle("-fx-background-color: " + BG_CARD + "; -fx-border-color: #ffffff12; -fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12;");
        DropShadow shadow = new DropShadow(10, Color.web(color)); shadow.setSpread(0.04); card.setEffect(shadow);

        StackPane iconBg = new StackPane();
        Circle bg = new Circle(22); bg.setStyle("-fx-fill: " + color + "18;");
        Label iconL = new Label(icon); iconL.setStyle("-fx-font-size: 18px;");
        iconBg.getChildren().addAll(bg, iconL);

        VBox info = new VBox(4);
        Label typeL = new Label(type); typeL.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_PRIMARY + ";");
        Label dateL = new Label(date); dateL.setStyle("-fx-font-size: 10px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + ";");
        info.getChildren().addAll(typeL, dateL);

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox right = new VBox(4); right.setAlignment(Pos.CENTER_RIGHT);
        Label amtL = new Label(amount); amtL.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-text-fill: " + color + ";");
        Label statusL = new Label(status); statusL.setStyle("-fx-font-size: 9px; -fx-font-family: 'Courier New'; -fx-text-fill: " + color + "; -fx-padding: 2 8 2 8; -fx-background-color: " + color + "18; -fx-border-color: " + color + "44; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4;");
        right.getChildren().addAll(amtL, statusL);

        card.getChildren().addAll(iconBg, info, spacer, right);
        card.setOnMouseEntered(e -> { ScaleTransition st = new ScaleTransition(Duration.millis(140), card); st.setToX(1.015); st.setToY(1.015); st.play(); DropShadow b = new DropShadow(18, Color.web(color)); b.setSpread(0.1); card.setEffect(b); });
        card.setOnMouseExited(e  -> { ScaleTransition st = new ScaleTransition(Duration.millis(140), card); st.setToX(1.0); st.setToY(1.0); st.play(); card.setEffect(shadow); });
        return card;
    }

    private Button buildActionButton(String icon, String label, String color) {
        Button btn = new Button(icon + "  " + label);
        btn.setStyle(btnStyle(color, false));
        DropShadow glow = new DropShadow(12, Color.web(color)); glow.setSpread(0.05); btn.setEffect(glow);
        btn.setOnMouseEntered(e -> { ScaleTransition st = new ScaleTransition(Duration.millis(150), btn); st.setToX(1.07); st.setToY(1.07); st.play(); btn.setStyle(btnStyle(color, true)); DropShadow b = new DropShadow(22, Color.web(color)); b.setSpread(0.2); btn.setEffect(b); });
        btn.setOnMouseExited(e  -> { ScaleTransition st = new ScaleTransition(Duration.millis(150), btn); st.setToX(1.0); st.setToY(1.0); st.play(); btn.setStyle(btnStyle(color, false)); btn.setEffect(glow); });
        btn.setOnMousePressed(e  -> { ScaleTransition st = new ScaleTransition(Duration.millis(80), btn); st.setToX(0.95); st.setToY(0.95); st.play(); });
        btn.setOnMouseReleased(e -> { ScaleTransition st = new ScaleTransition(Duration.millis(80), btn); st.setToX(1.0); st.setToY(1.0); st.play(); });
        return btn;
    }

    private String btnStyle(String color, boolean hovered) {
        return "-fx-background-color: " + (hovered ? color + "22" : BG_CARD) + ";" +
               "-fx-text-fill: " + color + "; -fx-border-color: " + color + "; -fx-border-width: 1.5;" +
               "-fx-border-radius: 10; -fx-background-radius: 10; -fx-font-family: 'Courier New';" +
               "-fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 22 10 22;";
    }

    private Button styledOutlineButton(String label, String color) {
        Button btn = new Button(label);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-border-color: " + color + "; -fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 6 16 6 16;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + color + "22; -fx-text-fill: " + color + "; -fx-border-color: " + color + "; -fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 6 16 6 16;"));
        btn.setOnMouseExited(e  -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-border-color: " + color + "; -fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 6 16 6 16;"));
        return btn;
    }

    private Button createSidebarButton(String icon, String label) {
        Button btn = new Button(icon + "  " + label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 16, 10, 16));
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> { if (btn != activeSidebarBtn) { ScaleTransition st = new ScaleTransition(Duration.millis(150), btn); st.setToX(1.03); st.setToY(1.03); st.play(); btn.setStyle("-fx-background-color: #00ff8815; -fx-text-fill: " + NEON_GREEN + "; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;"); }});
        btn.setOnMouseExited(e  -> { ScaleTransition st = new ScaleTransition(Duration.millis(150), btn); st.setToX(1.0); st.setToY(1.0); st.play(); if (btn != activeSidebarBtn) btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;"); });
        return btn;
    }

    private void setActiveButton(Button btn) {
        if (activeSidebarBtn != null) {
            activeSidebarBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            activeSidebarBtn.setEffect(null);
        }
        btn.setStyle("-fx-background-color: #00ff8820; -fx-text-fill: " + NEON_GREEN + "; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-border-color: " + NEON_GREEN + "; -fx-border-width: 0 0 0 3; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        applyGlowEffect(btn, NEON_GREEN, 8);
        activeSidebarBtn = btn;
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: " + BG_DARK + "; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-border-color: " + NEON_DIM + "44; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-padding: 10 14 10 14; -fx-prompt-text-fill: " + TEXT_MUTED + ";");
        return tf;
    }

    private PasswordField styledPinField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle("-fx-background-color: " + BG_DARK + "; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-border-color: " + NEON_DIM + "44; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-padding: 10 14 10 14; -fx-prompt-text-fill: " + TEXT_MUTED + ";");
        return pf;
    }

    private Label fieldLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 11px; -fx-font-family: 'Courier New'; -fx-text-fill: " + TEXT_MUTED + "; -fx-letter-spacing: 1; -fx-padding: 10 0 2 0;");
        return lbl;
    }

    private Label alertLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle(alertStyle(NEON_GREEN));
        lbl.setWrapText(true);
        return lbl;
    }

    private String alertStyle(String color) {
        return "-fx-font-size: 13px; -fx-font-family: 'Courier New'; -fx-text-fill: " + color + "; -fx-background-color: " + color + "18; -fx-border-color: " + color + "44; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 14 10 14;";
    }

    private void setAlert(Label lbl, String msg, String color) {
        lbl.setText(msg);
        lbl.setStyle(alertStyle(color));
        lbl.setVisible(true);
        // Auto-clear after 4 seconds
        Timeline t = new Timeline(new KeyFrame(Duration.seconds(4), e -> { lbl.setText(""); lbl.setVisible(false); }));
        t.play();
    }

    private ScrollPane wrapInScrollPane(VBox content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background-color: transparent; -fx-background: " + BG_DARK + "; -fx-border-color: transparent;");
        return sp;
    }

    private Rectangle buildDivider() {
        Rectangle div = new Rectangle(186, 1);
        div.setFill(Color.web(NEON_GREEN + "44"));
        return div;
    }

    // ── Helpers ────────────────────────────────────────────────
    private void addTransaction(String icon, String type, String amount, String status, String color) {
        java.time.LocalDate now = java.time.LocalDate.now();
        String date = String.format("%02d %s %d", now.getDayOfMonth(),
            now.getMonth().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH), now.getYear());
        transactions.add(0, new Transaction(icon, type, date, amount, status, color));
    }

    private void updateUserInList() {
        // activeUser reference is mutable — list already reflects changes
    }

    private void refreshHeaderSidebar() {
        if (headerWelcome != null) headerWelcome.setText("Welcome,  " + activeUser.name);
        if (sidebarName   != null) sidebarName.setText(activeUser.name);
        if (sidebarAcct   != null) sidebarAcct.setText("A/C: " + activeUser.acct);
        if (sidebarInitials != null) sidebarInitials.setText(activeUser.initials);
    }

    private String fmtRupee(double amount) {
        return "₹" + String.format("%,.0f", amount);
    }

    // ── Animation helpers ──────────────────────────────────────
    private void addPulseEffect(Node node) {
        ScaleTransition pulse = new ScaleTransition(Duration.millis(1600), node);
        pulse.setFromX(1.0); pulse.setFromY(1.0);
        pulse.setToX(1.04);  pulse.setToY(1.04);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();
    }

    private void startClock(Label clock) {
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            java.time.LocalTime now = java.time.LocalTime.now();
            clock.setText(String.format("⏱  %02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
        }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();
        java.time.LocalTime now = java.time.LocalTime.now();
        clock.setText(String.format("⏱  %02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
    }

    private void applyGlowEffect(Node node, String hexColor, double radius) {
        DropShadow glow = new DropShadow(radius, Color.web(hexColor));
        glow.setSpread(0.3);
        node.setEffect(glow);
    }

    private void applyGlowLabel(Label label, String hexColor, double radius) {
        DropShadow glow = new DropShadow(radius, Color.web(hexColor));
        glow.setSpread(0.4);
        label.setEffect(glow);
    }

    // ═══════════════════════════════════════════════════════════
    //  MAIN
    // ═══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        launch(args);
    }
}
