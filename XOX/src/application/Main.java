package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.control.ChoiceDialog;

import java.util.Optional;
import java.util.Random;
import java.util.ArrayList;
public class Main extends Application {
	private Stage primaryStage;
    private char playerToken; // Player's chosen token ('X' or 'O')
    private char computerToken; // Computer's token
    private char whoseTurn; // Indicate which player has a turn, initially it is the player
    private Cell[][] cell = new Cell[3][3];
    private Label lblStatus = new Label(); // Updated dynamically based on the game state

   // Override the start method in the Application class
    @Override
    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
        // Show a ChoiceDialog to let the player choose 'X' or 'O'
        choosePlayerToken();

        // Set the initial turn
        whoseTurn = (playerToken == 'X') ? 'X' : 'O';

        // Create the game board
        GridPane pane = new GridPane();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                pane.add(cell[i][j] = new Cell(), j, i);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        borderPane.setBottom(lblStatus);

        // Create a reset button
        Button resetButton = new Button("Reset Game");
        resetButton.setOnAction(e -> resetGame());

        // Add the reset button to the bottom of the BorderPane
        BorderPane.setAlignment(resetButton, Pos.CENTER);
        borderPane.setBottom(resetButton);
        

        // Create a scene and place it in the stage
        Scene scene = new Scene(borderPane, 450, 200);
        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(scene);
        primaryStage.show();

        if (playerToken == 'O') {
            makeComputerMove();
        }

        // Display initial turn
        lblStatus.setText(whoseTurn + "'s turn");
    }

    // Reset the game
 // Reset the game
 // Reset the game
    private void resetGame() {
    	 primaryStage.close();
    	// Create a new stage
        Stage newStage = new Stage();

        // Create a new instance of the Main class (your application)
        Main newGame = new Main();

        // Start the new game in the new stage
        newGame.start(newStage);
    }



    
    private void choosePlayerToken() {
    	ArrayList<String> choices = new ArrayList<>();
        choices.add("X");
        choices.add("O");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("X", choices);
        dialog.setTitle("Choose Your Token");
        dialog.setHeaderText("Choose 'X' or 'O'");
        dialog.setContentText("Choose your side:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(token -> {
            playerToken = token.charAt(0);
            computerToken = (playerToken == 'X') ? 'O' : 'X';
        });
        
    }

    
    /** Determine if the cell are all occupied */
    public boolean isFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (cell[i][j].getToken() == ' ')
                    return false;
        return true;
    }

    /** Determine if the player with the specified token wins */
    public boolean isWon(char token) {
        for (int i = 0; i < 3; i++)
            if (cell[i][0].getToken() == token
                    && cell[i][1].getToken() == token
                    && cell[i][2].getToken() == token) {
                return true;
            }
        for (int j = 0; j < 3; j++)
            if (cell[0][j].getToken() == token
                    && cell[1][j].getToken() == token
                    && cell[2][j].getToken() == token) {
                return true;
            }
        if (cell[0][0].getToken() == token
                && cell[1][1].getToken() == token
                && cell[2][2].getToken() == token) {
            return true;
        }
        if (cell[0][2].getToken() == token
                && cell[1][1].getToken() == token
                && cell[2][0].getToken() == token) {
            return true;
        }
        return false;
    }

    // An inner class for a cell
    public class Cell extends Pane {
        // Token used for this cell
        private char token = ' ';

        public Cell() {
            setStyle("-fx-border-color: black");
            this.setPrefSize(2000, 2000);
            this.setOnMouseClicked(e -> handleMouseClick());
        }

        /** Return token */
        public char getToken() {
            return token;
        }

        /** Set a new token */
        public void setToken(char c) {
            token = c;
            if (token == 'X') {
                Line line1 = new Line(10, 10,
                        this.getWidth() - 10, this.getHeight() - 10);
                line1.endXProperty().bind(this.widthProperty().subtract(10));
                line1.endYProperty().bind(this.heightProperty().subtract(10));
                Line line2 = new Line(10, this.getHeight() - 10,
                        this.getWidth() - 10, 10);
                line2.startYProperty().bind(
                        this.heightProperty().subtract(10));
                line2.endXProperty().bind(this.widthProperty().subtract(10));
                // Add the lines to the pane
                this.getChildren().addAll(line1, line2);
            } else if (token == 'O') {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        this.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        this.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        this.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        this.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.WHITE);
                getChildren().add(ellipse); // Add the ellipse to the pane
            }
        }

        /* Handle a mouse click event */
        private void handleMouseClick() {
        	// Check if it's the player's turn
            if (whoseTurn == playerToken) {
            	
                // If cell is empty and game is not over
                if (token == ' ' && whoseTurn != ' ') {
                    setToken(whoseTurn); // Set token in the cell
                    // Check game status
                    if (isWon(whoseTurn)) {
                        lblStatus.setText(whoseTurn + " won! The game is over");
                        whoseTurn = ' '; // Game is over
                    } else if (isFull()) {
                        lblStatus.setText("Draw! The game is over");
                        whoseTurn = ' '; // Game is over
                    } else {
                        // Change the turn
                        whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                        // Display whose turn
                        lblStatus.setText(whoseTurn + "'s turn");

                        // If it's the computer's turn, make a move
                        if (whoseTurn == computerToken) {
                            makeComputerMove();
                        }
                    }
                }
            }
        }

        
    }
    private void makeComputerMove() {
    	// Computer logic for making a move
        // This example uses a random move, you can replace it with more intelligent logic
        Random rand = new Random();
        int row, col;

        // Keep generating random moves until an empty cell is found
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (cell[row][col].getToken() != ' ');

        // Make the computer move
        cell[row][col].setToken(computerToken);

        // Check game status
        if (isWon(computerToken)) {
            lblStatus.setText("Computer won! The game is over");
            whoseTurn = ' '; // Game is over
        } else if (isFull()) {
            lblStatus.setText("Draw! The game is over");
            whoseTurn = ' '; // Game is over
        } else {
            // Change the turn back to the player
            whoseTurn = playerToken;
            // Display player's turn
            lblStatus.setText(whoseTurn + "'s turn");
        }
    }

    /**
     * The main method is only needed for the IDE with limited JavaFX support. Not
     * needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
