package com.vaultcore.atm;

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

/**
 * VaultCore ATM Banking Application
 * Premium Splash Screen + Login UI
 *
 * Architecture  : Single-file JavaFX application (no external dependencies)
 * Theme         : Midnight Banking — dark, emerald-accented, glassmorphism
 * Java Version  : 11+ (JavaFX 11+)
 *
 * Compile:
 *   javac --module-path <javafx-sdk>/lib --add-modules javafx.controls,javafx.graphics \
 *         -d out src/com/vaultcore/atm/VaultCoreApp.java
 *
 * Run:
 *   java --module-path <javafx-sdk>/lib --add-modules javafx.controls,javafx.graphics \
 *        -cp out com.vaultcore.atm.VaultCoreApp
 */
public class VaultCoreApp extends Application {

    // ─── Palette ────────────────────────────────────────────────────────────────
    private static final Color BG_DEEP        = Color.web("#0F172A");
    private static final Color BG_PANEL       = Color.web("#1E293B");
    private static final Color ACCENT_PRIMARY = Color.web("#10B981");
    private static final Color ACCENT_HOVER   = Color.web("#059669");
    private static final Color TEXT_PRIMARY   = Color.web("#F8FAFC");
    private static final Color TEXT_SECONDARY = Color.web("#94A3B8");
    private static final Color BORDER_SUBTLE  = Color.web("#334155");

    // Inline CSS equivalents (JavaFX inline style strings)
    private static final String STYLE_BG_DEEP    = "-fx-background-color: #0F172A;";
    private static final String STYLE_BG_PANEL   = "-fx-background-color: #1E293B;";
    private static final String STYLE_ACCENT     = "#10B981";
    private static final String STYLE_TEXT_PRI   = "-fx-text-fill: #F8FAFC;";
    private static final String STYLE_TEXT_SEC   = "-fx-text-fill: #94A3B8;";
    private static final String STYLE_BORDER_SUB = "-fx-border-color: #334155;";

    private Stage primaryStage;

    // ─── Entry ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("VaultCore — Secure Banking");
        stage.setWidth(1100);
        stage.setHeight(700);
        stage.setResizable(false);
        stage.centerOnScreen();

        showSplashScreen();
        stage.show();
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  SPLASH SCREEN
    // ════════════════════════════════════════════════════════════════════════════

    private void showSplashScreen() {
        // Root — deep navy canvas
        StackPane root = new StackPane();
        root.setStyle(STYLE_BG_DEEP);

        // Subtle grid-dot background pattern
        Pane gridLayer = buildGridBackground();
        root.getChildren().add(gridLayer);

        // Central glass card
        VBox card = buildSplashCard();
        StackPane.setAlignment(card, Pos.CENTER);
        root.getChildren().add(card);

        // Corner branding strip (bottom-left)
        Label corner = new Label("© 2025 VaultCore Financial Systems Ltd.");
        corner.setStyle(STYLE_TEXT_SEC + " -fx-font-size: 11px; -fx-font-family: 'Monospace';");
        StackPane.setAlignment(corner, Pos.BOTTOM_LEFT);
        StackPane.setMargin(corner, new Insets(0, 0, 18, 24));
        root.getChildren().add(corner);

        // Version badge (bottom-right)
        Label version = new Label("v4.2.1  •  Build 20250515");
        version.setStyle(STYLE_TEXT_SEC + " -fx-font-size: 11px; -fx-font-family: 'Monospace';");
        StackPane.setAlignment(version, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(version, new Insets(0, 24, 18, 0));
        root.getChildren().add(version);

        Scene splashScene = new Scene(root, 1100, 700);
        primaryStage.setScene(splashScene);

        // Run entrance animation sequence
        animateSplashEntrance(card, root);
    }

    /** Dotted grid — communicates precision / data */
    private Pane buildGridBackground() {
        Pane pane = new Pane();
        pane.setPrefSize(1100, 700);
        int dotSpacing = 40;
        for (int x = 0; x < 1100; x += dotSpacing) {
            for (int y = 0; y < 700; y += dotSpacing) {
                Circle dot = new Circle(x, y, 1);
                dot.setFill(Color.web("#1E293B"));
                pane.getChildren().add(dot);
            }
        }
        return pane;
    }

    /** Central glassmorphism card containing logo + loader */
    private VBox buildSplashCard() {
        VBox card = new VBox(28);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(52, 64, 48, 64));
        card.setMaxWidth(480);
        card.setMaxHeight(420);
        card.setStyle(
            "-fx-background-color: rgba(30, 41, 59, 0.85);" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: #334155;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 20;"
        );

        // Glass reflection top-edge line
        Rectangle topEdge = new Rectangle(380, 1);
        topEdge.setFill(Color.web("#4B5563", 0.5));
        topEdge.setArcWidth(2);
        topEdge.setArcHeight(2);

        // Vault icon
        StackPane icon = buildVaultIcon();

        // Logo text
        VBox logoText = buildLogoText();

        // Tagline
        Label tagline = new Label("ENTERPRISE BANKING INFRASTRUCTURE");
        tagline.setStyle(
            "-fx-text-fill: #94A3B8;" +
            "-fx-font-size: 10.5px;" +
            "-fx-font-family: 'Monospace';" +
            "-fx-letter-spacing: 3px;"
        );

        // Separator
        Rectangle sep = new Rectangle(280, 1);
        sep.setFill(Color.web("#334155"));

        // Loading section
        VBox loadingSection = buildLoadingSection();

        card.getChildren().addAll(topEdge, icon, logoText, tagline, sep, loadingSection);

        // Drop shadow for depth
        DropShadow cardShadow = new DropShadow();
        cardShadow.setColor(Color.web("#000000", 0.6));
        cardShadow.setRadius(60);
        cardShadow.setOffsetY(20);
        card.setEffect(cardShadow);

        return card;
    }

    /** Geometric vault door icon — SVG-inspired, pure shapes */
    private StackPane buildVaultIcon() {
        StackPane iconStack = new StackPane();
        iconStack.setPrefSize(72, 72);

        // Outer ring
        Circle outerRing = new Circle(36);
        outerRing.setFill(Color.TRANSPARENT);
        outerRing.setStroke(ACCENT_PRIMARY);
        outerRing.setStrokeWidth(2.2);

        // Inner filled circle (vault face)
        Circle innerFill = new Circle(28);
        innerFill.setFill(Color.web("#0F172A"));
        innerFill.setStroke(Color.web("#10B981", 0.4));
        innerFill.setStrokeWidth(1);

        // Cross spokes (vault wheel)
        Line spoke1 = spoke(36, 36, 0);
        Line spoke2 = spoke(36, 36, 45);
        Line spoke3 = spoke(36, 36, 90);
        Line spoke4 = spoke(36, 36, 135);

        // Center bolt
        Circle bolt = new Circle(5);
        bolt.setFill(ACCENT_PRIMARY);

        // Shield overlay (security)
        Polygon shield = new Polygon(
            0.0, -10.0,
            8.0, -6.0,
            8.0,  4.0,
            0.0,  9.0,
           -8.0,  4.0,
           -8.0, -6.0
        );
        shield.setFill(Color.TRANSPARENT);
        shield.setStroke(Color.web("#10B981", 0.6));
        shield.setStrokeWidth(1.2);

        iconStack.getChildren().addAll(outerRing, innerFill, spoke1, spoke2, spoke3, spoke4, bolt, shield);

        // Emerald glow
        Glow glow = new Glow(0.4);
        DropShadow emeraldShadow = new DropShadow();
        emeraldShadow.setColor(Color.web("#10B981", 0.7));
        emeraldShadow.setRadius(20);
        emeraldShadow.setInput(glow);
        outerRing.setEffect(emeraldShadow);
        bolt.setEffect(new DropShadow(8, Color.web("#10B981", 0.9)));

        return iconStack;
    }

    private Line spoke(double cx, double cy, double angleDeg) {
        double rad = Math.toRadians(angleDeg);
        double len = 18;
        Line l = new Line(
            cx - Math.cos(rad) * len, cy - Math.sin(rad) * len,
            cx + Math.cos(rad) * len, cy + Math.sin(rad) * len
        );
        l.setStroke(Color.web("#10B981", 0.5));
        l.setStrokeWidth(1.5);
        return l;
    }

    /** "VaultCore" styled logo text */
    private VBox buildLogoText() {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);

        // Merge on one line via HBox
        HBox logoRow = new HBox(0);
        logoRow.setAlignment(Pos.CENTER);

        Text vaultSpaced = new Text("VAULT");
        vaultSpaced.setFont(Font.font("System", FontWeight.BOLD, 38));
        vaultSpaced.setFill(TEXT_PRIMARY);

        Text coreSpaced = new Text("CORE");
        coreSpaced.setFont(Font.font("System", FontWeight.BOLD, 38));
        coreSpaced.setFill(ACCENT_PRIMARY);

        // Subtle emerald text glow on CORE
        DropShadow textGlow = new DropShadow();
        textGlow.setColor(Color.web("#10B981", 0.5));
        textGlow.setRadius(12);
        coreSpaced.setEffect(textGlow);

        logoRow.getChildren().addAll(vaultSpaced, coreSpaced);
        box.getChildren().add(logoRow);
        return box;
    }

    /** Progress bar + status text */
    private VBox buildLoadingSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);

        Label statusLabel = new Label("Initializing secure environment...");
        statusLabel.setId("statusLabel");
        statusLabel.setStyle(STYLE_TEXT_SEC + " -fx-font-size: 12px; -fx-font-family: 'Monospace';");

        // Track
        StackPane trackPane = new StackPane();
        trackPane.setAlignment(Pos.CENTER_LEFT);
        trackPane.setMaxWidth(340);
        trackPane.setPrefWidth(340);

        Rectangle track = new Rectangle(340, 4);
        track.setFill(Color.web("#334155"));
        track.setArcWidth(4);
        track.setArcHeight(4);

        Rectangle fill = new Rectangle(0, 4);
        fill.setId("progressFill");
        fill.setFill(ACCENT_PRIMARY);
        fill.setArcWidth(4);
        fill.setArcHeight(4);
        fill.setEffect(new Glow(0.6));

        trackPane.getChildren().addAll(track, fill);

        // Percentage label
        Label pctLabel = new Label("0%");
        pctLabel.setId("pctLabel");
        pctLabel.setStyle("-fx-text-fill: #10B981; -fx-font-size: 11px; -fx-font-family: 'Monospace';");

        section.getChildren().addAll(statusLabel, trackPane, pctLabel);
        return section;
    }

    // ─── Splash Animation Sequence ───────────────────────────────────────────────

    private void animateSplashEntrance(VBox card, StackPane root) {
        // 1. Root fade-in
        FadeTransition rootFade = new FadeTransition(Duration.millis(600), root);
        rootFade.setFromValue(0);
        rootFade.setToValue(1);

        // 2. Card scale-in from 0.88 → 1.0
        card.setOpacity(0);
        card.setScaleX(0.88);
        card.setScaleY(0.88);

        FadeTransition cardFade = new FadeTransition(Duration.millis(700), card);
        cardFade.setFromValue(0);
        cardFade.setToValue(1);

        ScaleTransition cardScale = new ScaleTransition(Duration.millis(700), card);
        cardScale.setFromX(0.88);
        cardScale.setFromY(0.88);
        cardScale.setToX(1.0);
        cardScale.setToY(1.0);
        cardScale.setInterpolator(Interpolator.SPLINE(0.16, 1.0, 0.3, 1.0)); // ease-out spring

        ParallelTransition cardEntrance = new ParallelTransition(cardFade, cardScale);

        SequentialTransition entrance = new SequentialTransition(rootFade, cardEntrance);
        entrance.setOnFinished(e -> startProgressAnimation(card));
        entrance.play();

        // Emerald glow pulse on icon (runs independently)
        scheduleGlowPulse(card);
    }

    /** Subtle 0.4s glow pulse on the icon every 2.5s */
    private void scheduleGlowPulse(VBox card) {
        card.getChildren().stream()
            .filter(n -> n instanceof StackPane)
            .findFirst()
            .ifPresent(icon -> {
                Timeline glowTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO,         new KeyValue(icon.opacityProperty(), 1.0)),
                    new KeyFrame(Duration.millis(1200), new KeyValue(icon.opacityProperty(), 0.75, Interpolator.EASE_BOTH)),
                    new KeyFrame(Duration.millis(2500), new KeyValue(icon.opacityProperty(), 1.0,  Interpolator.EASE_BOTH))
                );
                glowTimeline.setCycleCount(Animation.INDEFINITE);
                glowTimeline.play();
            });
    }

    /**
     * Animated progress bar + rotating status messages.
     * Simulates 4 phases of a real banking application boot sequence.
     */
    private void startProgressAnimation(VBox card) {
        String[] statuses = {
            "Verifying cryptographic certificates...",
            "Establishing HSM connection...",
            "Loading transaction engine...",
            "Securing banking environment...",
            "Ready."
        };
        double[] milestones = {0, 25, 55, 80, 100};

        // Locate UI nodes
        Rectangle progressFill = (Rectangle) card.lookup("#progressFill");
        Label statusLabel      = (Label)     card.lookup("#statusLabel");
        Label pctLabel         = (Label)     card.lookup("#pctLabel");

        if (progressFill == null || statusLabel == null) {
            transitionToLogin();
            return;
        }

        SequentialTransition seq = new SequentialTransition();

        for (int i = 0; i < statuses.length; i++) {
            final String msg       = statuses[i];
            final double targetPct = milestones[i];
            final double prevPct   = (i == 0) ? 0 : milestones[i - 1];
            double durationMs      = (i == 0) ? 400 : (600 + i * 200);

            // Animate fill width proportional to %
            Timeline segTimeline = new Timeline();
            double fromW = prevPct / 100.0 * 340;
            double toW   = targetPct / 100.0 * 340;

            segTimeline.getKeyFrames().add(new KeyFrame(Duration.ZERO,
                new KeyValue(progressFill.widthProperty(), fromW)
            ));
            segTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(durationMs),
                new KeyValue(progressFill.widthProperty(), toW, Interpolator.EASE_BOTH)
            ));

            // Update labels at start of each segment
            PauseTransition labelUpdate = new PauseTransition(Duration.millis(1));
            labelUpdate.setOnFinished(e -> {
                statusLabel.setText(msg);
                long pct = Math.round(targetPct);
                pctLabel.setText(pct + "%");
            });

            seq.getChildren().addAll(labelUpdate, segTimeline);

            if (i < statuses.length - 1) {
                seq.getChildren().add(new PauseTransition(Duration.millis(260)));
            }
        }

        seq.getChildren().add(new PauseTransition(Duration.millis(800)));
        seq.setOnFinished(e -> transitionToLogin());
        seq.play();
    }

    // ─── Scene Transition ────────────────────────────────────────────────────────

    private void transitionToLogin() {
        Scene currentScene = primaryStage.getScene();
        Node currentRoot   = currentScene.getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(650), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setInterpolator(Interpolator.EASE_IN);
        fadeOut.setOnFinished(e -> showLoginScreen());
        fadeOut.play();
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  LOGIN SCREEN
    // ════════════════════════════════════════════════════════════════════════════

    private void showLoginScreen() {
        HBox root = new HBox();
        root.setStyle(STYLE_BG_DEEP);
        root.setOpacity(0);

        // Left branding panel (40%)
        VBox brandPanel = buildBrandPanel();
        HBox.setHgrow(brandPanel, Priority.NEVER);
        brandPanel.setPrefWidth(420);

        // Right login form panel (60%)
        StackPane formPanel = buildFormPanel();
        HBox.setHgrow(formPanel, Priority.ALWAYS);

        root.getChildren().addAll(brandPanel, formPanel);

        Scene loginScene = new Scene(root, 1100, 700);
        primaryStage.setScene(loginScene);

        // Fade in + slide-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(700), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Slide brand panel from left
        brandPanel.setTranslateX(-40);
        KeyValue kv1 = new KeyValue(brandPanel.translateXProperty(), 0, Interpolator.SPLINE(0.16, 1, 0.3, 1));
        KeyFrame kf1 = new KeyFrame(Duration.millis(700), kv1);
        Timeline slideLeft = new Timeline(kf1);

        // Slide form from right
        formPanel.setTranslateX(40);
        KeyValue kv2 = new KeyValue(formPanel.translateXProperty(), 0, Interpolator.SPLINE(0.16, 1, 0.3, 1));
        KeyFrame kf2 = new KeyFrame(Duration.millis(700), kv2);
        Timeline slideRight = new Timeline(kf2);

        ParallelTransition loginEntrance = new ParallelTransition(fadeIn, slideLeft, slideRight);
        loginEntrance.play();
    }

    /** Left branding sidebar */
    private VBox buildBrandPanel() {
        VBox panel = new VBox();
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(48));
        panel.setSpacing(0);
        panel.setStyle(
            "-fx-background-color: #1E293B;" +
            "-fx-border-color: #334155;" +
            "-fx-border-width: 0 1 0 0;"
        );

        // Top area: logo
        VBox topSection = new VBox(20);
        topSection.setAlignment(Pos.CENTER);

        StackPane icon = buildVaultIcon();
        icon.setScaleX(1.3);
        icon.setScaleY(1.3);

        HBox logoRow = new HBox(0);
        logoRow.setAlignment(Pos.CENTER);
        Text v = new Text("VAULT");
        v.setFont(Font.font("System", FontWeight.BOLD, 32));
        v.setFill(TEXT_PRIMARY);
        Text c = new Text("CORE");
        c.setFont(Font.font("System", FontWeight.BOLD, 32));
        c.setFill(ACCENT_PRIMARY);
        DropShadow cGlow = new DropShadow();
        cGlow.setColor(Color.web("#10B981", 0.45));
        cGlow.setRadius(10);
        c.setEffect(cGlow);
        logoRow.getChildren().addAll(v, c);

        Label sub = new Label("ENTERPRISE BANKING");
        sub.setStyle(STYLE_TEXT_SEC + " -fx-font-size: 10px; -fx-font-family: 'Monospace'; -fx-letter-spacing: 2px;");

        topSection.getChildren().addAll(icon, logoRow, sub);

        // Middle: feature pills
        VBox features = buildFeaturePills();

        // Bottom: security badge
        HBox secBadge = buildSecurityBadge();

        // Spacers
        Region spacer1 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        panel.getChildren().addAll(topSection, spacer1, features, spacer2, secBadge);
        return panel;
    }

    private VBox buildFeaturePills() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(0, 16, 0, 16));

        String[][] pills = {
            {"⬡", "256-bit AES Encryption"},
            {"⬡", "Multi-factor Authentication"},
        };

        for (String[] pill : pills) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 16, 10, 16));
            row.setStyle(
                "-fx-background-color: rgba(16, 185, 129, 0.08);" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: rgba(16, 185, 129, 0.2);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;"
            );
            row.setMaxWidth(300);

            Label dot = new Label("●");
            dot.setStyle("-fx-text-fill: #10B981; -fx-font-size: 8px;");

            Label txt = new Label(pill[1]);
            txt.setStyle(STYLE_TEXT_PRI + " -fx-font-size: 12.5px;");

            row.getChildren().addAll(dot, txt);
            box.getChildren().add(row);
        }
        return box;
    }

    private HBox buildSecurityBadge() {
        HBox badge = new HBox(10);
        badge.setAlignment(Pos.CENTER);
        badge.setPadding(new Insets(10, 20, 10, 20));
        badge.setStyle(
            "-fx-background-color: rgba(15, 23, 42, 0.8);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #334155;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;"
        );

        Label shieldIcon = new Label("🔒");
        shieldIcon.setStyle("-fx-font-size: 14px;");

        Label txt = new Label("Secured by VaultCore Security Suite");
        txt.setStyle(STYLE_TEXT_SEC + " -fx-font-size: 11px; -fx-font-family: 'Monospace';");

        badge.getChildren().addAll(shieldIcon, txt);
        return badge;
    }

    /** Right login form */
    private StackPane buildFormPanel() {
        StackPane panel = new StackPane();
        panel.setStyle(STYLE_BG_DEEP);

        VBox card = new VBox(22);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(48, 52, 48, 52));
        card.setMaxWidth(420);
        card.setStyle(
            "-fx-background-color: rgba(30, 41, 59, 0.9);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: #334155;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 18;"
        );
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#000000", 0.5));
        shadow.setRadius(50);
        shadow.setOffsetY(12);
        card.setEffect(shadow);

        // Heading
        VBox heading = new VBox(6);
        heading.setAlignment(Pos.CENTER);
        Label welcome = new Label("Welcome Back");
        welcome.setStyle(STYLE_TEXT_PRI + " -fx-font-size: 24px; -fx-font-weight: bold;");
        Label subtitle = new Label("Sign in to your VaultCore account");
        subtitle.setStyle(STYLE_TEXT_SEC + " -fx-font-size: 13px;");
        heading.getChildren().addAll(welcome, subtitle);

        // Separator line
        Rectangle sep = new Rectangle(320, 1);
        sep.setFill(Color.web("#334155"));

        // Form fields
        VBox userField = buildInputField("Account ID / Username", "Enter your account ID", false);
        VBox passField = buildInputField("PIN / Password",         "Enter your PIN",        true);

        // Forgot PIN
        HBox forgotRow = new HBox();
        forgotRow.setAlignment(Pos.CENTER_RIGHT);
        Label forgot = new Label("Forgot PIN?");
        forgot.setStyle("-fx-text-fill: #10B981; -fx-font-size: 12px; -fx-cursor: hand;");
        addHoverEffect(forgot, "#10B981", "#059669");
        forgotRow.getChildren().add(forgot);

        // Login button
        Button loginBtn = buildPrimaryButton("SIGN IN");
        loginBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Login Successful!");
            alert.showAndWait();
        });

        // Divider
        HBox divider = buildDivider("OR");

        // Register button
        Button registerBtn = buildSecondaryButton("CREATE NEW ACCOUNT");
        registerBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Register");
            alert.setHeaderText(null);
            alert.setContentText("Registration page coming soon!");
            alert.showAndWait();
        });

        // ATM Card visual hint
        HBox atmHint = new HBox(8);
        atmHint.setAlignment(Pos.CENTER);
        Label atmIcon = new Label("💳");
        atmIcon.setStyle("-fx-font-size: 13px;");
        Label atmTxt = new Label("Insert physical card or use digital credentials");
        atmTxt.setStyle(STYLE_TEXT_SEC + " -fx-font-size: 11px;");
        atmHint.getChildren().addAll(atmIcon, atmTxt);

        card.getChildren().addAll(heading, sep, userField, passField, forgotRow, loginBtn, divider, registerBtn, atmHint);
        panel.getChildren().add(card);
        return panel;
    }

    /** Styled input field with floating label effect */
    private VBox buildInputField(String labelText, String placeholder, boolean isPassword) {
        VBox fieldBox = new VBox(6);

        Label lbl = new Label(labelText);
        lbl.setStyle(STYLE_TEXT_SEC + " -fx-font-size: 12px; -fx-font-family: 'Monospace';");

        TextField field;
        if (isPassword) {
            PasswordField pf = new PasswordField();
            pf.setPromptText(placeholder);
            field = pf;
        } else {
            field = new TextField();
            field.setPromptText(placeholder);
        }

        String baseStyle =
            "-fx-background-color: #0F172A;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #334155;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-text-fill: #F8FAFC;" +
            "-fx-prompt-text-fill: #475569;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 12 16 12 16;";
        field.setStyle(baseStyle);

        // Focus ring — accent border on focus
        field.focusedProperty().addListener((obs, oldVal, focused) -> {
            if (focused) {
                field.setStyle(baseStyle.replace("#334155", "#10B981"));
                DropShadow focusGlow = new DropShadow();
                focusGlow.setColor(Color.web("#10B981", 0.3));
                focusGlow.setRadius(8);
                field.setEffect(focusGlow);
            } else {
                field.setStyle(baseStyle);
                field.setEffect(null);
            }
        });

        field.setPrefWidth(320);
        fieldBox.getChildren().addAll(lbl, field);
        return fieldBox;
    }

    private Button buildPrimaryButton(String text) {
        Button btn = new Button(text);
        String base =
            "-fx-background-color: #10B981;" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: #0F172A;" +
            "-fx-font-size: 13.5px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Monospace';" +
            "-fx-padding: 14 0 14 0;" +
            "-fx-cursor: hand;" +
            "-fx-pref-width: 320;";
        btn.setStyle(base);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(base.replace("#10B981", "#059669"));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(base);
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        DropShadow btnGlow = new DropShadow();
        btnGlow.setColor(Color.web("#10B981", 0.4));
        btnGlow.setRadius(16);
        btnGlow.setOffsetY(4);
        btn.setEffect(btnGlow);

        return btn;
    }

    private Button buildSecondaryButton(String text) {
        Button btn = new Button(text);
        String base =
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #334155;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-text-fill: #94A3B8;" +
            "-fx-font-size: 12.5px;" +
            "-fx-font-family: 'Monospace';" +
            "-fx-padding: 12 0 12 0;" +
            "-fx-cursor: hand;" +
            "-fx-pref-width: 320;";
        btn.setStyle(base);

        btn.setOnMouseEntered(e -> btn.setStyle(base
            .replace("#334155", "#10B981")
            .replace("#94A3B8", "#10B981")
        ));
        btn.setOnMouseExited(e -> btn.setStyle(base));
        return btn;
    }

    private HBox buildDivider(String label) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER);
        row.setMaxWidth(320);

        Rectangle l = new Rectangle(130, 1);
        l.setFill(Color.web("#334155"));
        Label lbl = new Label(label);
        lbl.setStyle(STYLE_TEXT_SEC + " -fx-font-size: 11px;");
        Rectangle r = new Rectangle(130, 1);
        r.setFill(Color.web("#334155"));

        row.getChildren().addAll(l, lbl, r);
        return row;
    }

    /** Generic label hover color transition */
    private void addHoverEffect(Label label, String normal, String hover) {
        label.setOnMouseEntered(e -> label.setStyle(label.getStyle().replace(normal, hover)));
        label.setOnMouseExited(e -> label.setStyle(label.getStyle().replace(hover, normal)));
    }
}
