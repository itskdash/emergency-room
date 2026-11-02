import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage stage) {
        TextField capacityField = new TextField();
        capacityField.setPromptText("Waiting room capacity");

        TextField doctorsField = new TextField();
        doctorsField.setPromptText("Number of doctors");

        TextField patientsField = new TextField();
        patientsField.setPromptText("Number of patients");

        Button startButton = new Button("Start Simulation");
        startButton.setMaxWidth(Double.MAX_VALUE);

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPromptText("Simulation events will appear here...");

        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                write(new byte[] { (byte) b }, 0, 1);
            }

            @Override
            public void write(byte[] b, int off, int len) {
                String text = new String(b, off, len, StandardCharsets.UTF_8);
                Platform.runLater(() -> logArea.appendText(text));
            }
        }, true, StandardCharsets.UTF_8));

        startButton.setOnAction(e -> {
            try {
                int capacity = Integer.parseInt(capacityField.getText().trim());
                int doctors = Integer.parseInt(doctorsField.getText().trim());
                int patients = Integer.parseInt(patientsField.getText().trim());

                if (capacity <= 0 || doctors <= 0 || patients <= 0) {
                    logArea.setText("Please enter positive numbers.");
                    return;
                }

                logArea.clear();
                startButton.setDisable(true);

                Thread simulation = new Thread(() -> {
                    try {
                        new Hospital(capacity, doctors, patients).simulationEvents();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    } finally {
                        Platform.runLater(() -> startButton.setDisable(false));
                    }
                });
                simulation.setDaemon(true);
                simulation.start();

            } catch (NumberFormatException ex) {
                logArea.setText("Please enter valid numbers.");
            }
        });

        VBox root = new VBox(10,
                new Label("Waiting Room Capacity"), capacityField,
                new Label("Number of Doctors"), doctorsField,
                new Label("Number of Patients"), patientsField,
                startButton,
                new Label("Simulation Log"), logArea);
        root.setPadding(new Insets(20));
        VBox.setVgrow(logArea, Priority.ALWAYS);

        stage.setTitle("Hospital ER Simulation");
        stage.setScene(new Scene(root, 600, 650));
        stage.show();
    }
}