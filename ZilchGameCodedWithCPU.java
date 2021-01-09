import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


/*
 * This class plays the game of Zilch. The class randomly chooses the dice values, calculates the
 * score, and displays the roll's outcome to the user. There is an integrated COMP player
 * and the game is very fun to play!
 *
 * by: Michael Kalpouzos
 */

public class ZilchGameCodedWithCPU {

    // The global attributes of the class.
    public static Random generator = new Random(System.currentTimeMillis());
    public static final int GAME_LIMIT = 10000;
    private static int numToRoll = 6;
    private static boolean freeRoll = false;

    /*
     * The rollDice method takes in the amount of dice left and
     * creates a new array with the length of the dice being rolled.
     * The values of the dice are put into the array and it sis returned.
     */
    private static int[] rollDice(int rollAmounts) {

        int[] die_values = new int[rollAmounts];

        // This for loop places the dice values into the array.
        for (int i = 0; i< rollAmounts; i++) {
            die_values[i] = generator.nextInt(6) + 1;
        }

        return die_values;

    } // end rollDice

    // This method returns the value of a singular dice being rolled.
    private static int rollOneDice(){

        return generator.nextInt(6) + 1;

    } // end rollOneDice


    // This method sets the amount of dice left to roll to the right amount.
    private static void setNumToRoll(int[] dice) {

        int[] dupCountArray = new int[6];
        int newNumToRoll = 0;

        for (int die : dice) {
            dupCountArray[die - 1]++;
        }

        for (int i = 0; i < 6; i++) {

            if (dupCountArray[i] < 3 && (i != 0) && (i != 4)) {

                newNumToRoll += dupCountArray[i];
                numToRoll = newNumToRoll;
            }
        }
    }

    
    // This method takes in the array of dice and properly scores the roll.
    private static int scoreThrow(int[] dice){

        int[] duplicatedCount = new int[6];
        int countPairs = 0;
        int[] straightRoll = new int[]{1,1,1,1,1,1};
        int throwScore = 0;

        // This creates an array with the amount of times the dice value appeared in the roll.
        for (int die : dice) {
            duplicatedCount[die - 1]++;
        }

        // This checks to see if the player has rolled a straight, it scores it
        // and gives the player their free roll.
        if (Arrays.equals(duplicatedCount, straightRoll)) {
            throwScore += 1500;
            System.out.print(" *straight*");
            freeRoll = true;
        }

        // This sets the number of dice left to roll to the correct amount.
        setNumToRoll(dice);

        // This checks to see if there are any 3,4 or 5 of
        // a kind and it counts the amount of pair in the roll.
        for (int i = 0; i < 6; i++){

            if (duplicatedCount[i] >= 3){

                if (i == 0) {
                    throwScore += 1000 * (i + 1) * Math.pow(2, (duplicatedCount[i] - 3));
                }

                else {
                    throwScore += 100 * (i + 1) * Math.pow(2, (duplicatedCount[i] - 3));
                }

                System.out.print(" *" + getNumberName(duplicatedCount[i]));
                System.out.print(" of a kind*");
            }

            // This counts the amount of pairs in the roll.
            else if (duplicatedCount[i] == 2){
                countPairs ++;
            }
        }

        // This checks to see if the player has rolled three pairs, it scores it
        // and gives the player their free roll.
        if (countPairs == 3 && numToRoll == 6) {
            throwScore += 1500;
            System.out.print(" *three pairs*");
            freeRoll = true;
        }

        // This checks to see if the player has rolled no scoring dice, it scores it
        // and gives the player their free roll.
        if (throwScore == 0 && numToRoll == 6) {
            throwScore += 500;
            System.out.print(" *no scoring dice*");
            freeRoll = true;
        }

        else {

            // This counts the amount of ones in the roll and scores them
            // if there aren't zero or more than two ones.
            if (duplicatedCount[0] < 3 && duplicatedCount[0] != 0) {

                throwScore += 100 * duplicatedCount[0];
                System.out.print(" *" + getNumberName(duplicatedCount[0]));

                if (duplicatedCount[0] == 2) {
                    System.out.print(" ones*");
                }
                else {
                    System.out.print(" one*");
                }
            }

            // This counts the amount of fives in the roll and scores them
            // if there aren't zero or more than two ones.
            if (duplicatedCount[4] < 3 && duplicatedCount[4] != 0) {

                throwScore += 50 * duplicatedCount[4];
                System.out.print(" *" + getNumberName(duplicatedCount[4]));

                if (duplicatedCount[4] == 2) {
                    System.out.print(" fives*");
                }
                else {
                    System.out.print(" five*");
                }
            }
        }
        
        return throwScore;

    } // end scoreThrow

    // Returns the name of the dice roll as a word.
    private static String getNumberName(int roll) {
        String[] names = {"one", "two", "three", "four", "five", "six"};
        return names[roll - 1];
    } // end getNumberName

    // Obtains and returns a single character as provided by the user. If the user enters
    // more than one character the extra characters are ignored. If he or she does not
    // provide any characters then the null character is returned.
    private static char getChar() {
        byte[] buffer = new byte[100];
        int numRead = 0;
        try {
            numRead = System.in.read(buffer);
        } catch (IOException ignored) {
        }
        if (numRead > 0)
            return (char)buffer[0];
        return '\0';
    } // end getChar

    // Returns true if the supplied target is in the supplied array, false otherwise.
    private static boolean inArray(char[] array, char target) {
        for (char elem : array)
            if (elem == target)
                return true;
        return false;
    } // end inArray

    // Prompts for, obtains and returns a single character from the user. If the
    // character is not legal, the user is prompted again.
    private static char playerPrompt(String prompt) {
        char response = '?';
        char[] legalResponses = {'r', 'R', 'b', 'B', 'q', 'Q'};
        while (true) {
            System.out.print(prompt);
            response = getChar();
            if (inArray(legalResponses, response))
                return response;
            else
                System.out.print("Illegal entry, please try again. ");
        }
    } // end playerPrompt

    // Displays instructions to the user.
    public static void displayIntro() {
        // A Text block would be useful here!!!!!
        String out = "This training program will help you practice the game of Zilch learning when to roll again and when it is too risky.";
        out += "\nYou will roll against an AI player who banks or rolls at random.";
        out += "\nYou should be able to win easily if you are making better choices!";
        out += "\n\nPossible responses at a prompt are \"r\" to roll again, \"b\" to";
        out += "\nbank your points, just <enter> and \"q\" to quit the trainer. Otherwise";
        out += "\nthe session will run until one player wins.\n";
        System.out.println(out);
    } // end displayIntro

    // Displays the players dice roll to the console.
    public static void displayRoll(int rollCount, int[] dice) {
        StringBuilder out = new StringBuilder("Roll " + rollCount + ": ");
        for (int die : dice)
            out.append("*").append(getNumberName(die)).append("*");
        out.append(" Scoring:");
        System.out.print(out);
    } // end displayRoll

    // Plays the game.
    public static void runTrainer() {
        boolean playRandom = false;
        int throwSum = 0;
        int turnSum = 0;
        int randomTurnSum = 0;
        int humanTurnSum = 0;
        int bankedSum = 0;
        int randomBankedSum = 0;
        int zilchCount = 0;
        int randomZilchCount = 0;
        int rollCount = 0;
        int randomRollCount = 0;
        int[] dice;
        char input;
        String prompt;
        System.out.print("Press <enter> to start trainer: ");
        input = getChar();
        // Loops until the human has had enough or one player has won.
        while (!(input == 'q' || input == 'Q') && bankedSum < GAME_LIMIT && randomBankedSum < GAME_LIMIT) {
            if (playRandom) {
                System.out.print("\n***RANDOM PLAYER Playing*** ");
                randomTurnSum = turnSum;
                humanTurnSum = 0;
            } else {
                System.out.print("\n***YOU Playing!*** ");
                humanTurnSum = turnSum;
                randomTurnSum = 0;
            }
            // Display current values for both players.
            System.out.println(numToRoll + " dice left.");
            System.out.print("RANDOM PLAYER Stats: ");
            System.out.print("\tTurn Sum: " + randomTurnSum + " \tBank: " + randomBankedSum +
                    " \tZilch count: " + randomZilchCount + "\n");
            System.out.print("YOUR Stats: ");
            System.out.println("\t\tTurn Sum: " + humanTurnSum + " \tBank: " + bankedSum +
                    " \tZilch count: " + zilchCount + "\n");
            // Essentially pauses output so a human can read it..
            if (!playRandom) {
                System.out.print("Press <enter> to roll: ");
                input = getChar();
                if (input == 'q' || input == 'Q')
                    break;
            }
            // Roll the dice and display the resulting values.
            dice = rollDice(numToRoll);
            if (playRandom) {
                randomRollCount++;
                System.out.print("Random ");
                displayRoll(randomRollCount, dice);
            } else {
                rollCount++;
                System.out.print("Your ");
                displayRoll(rollCount, dice);
            }
            // Score the roll and show the score and the free roll notice if obtained.
            throwSum = scoreThrow(dice);

            System.out.print(" " + throwSum + " points.");
            if (freeRoll || numToRoll == 0) {
                System.out.println(" You get a free roll!");
                freeRoll = false;
                numToRoll = 6;
            }
            // Check for a zilch.
            if (throwSum == 0) {
                System.out.print(" A zilch!!\n");
                if (playRandom)
                    randomZilchCount++;
                else {
                    zilchCount++;
                    System.out.print("Press <enter> to continue: ");
                    input = getChar();
                    if (input == 'q' || input == 'Q')
                        break;
                }
                turnSum = 0;
                // Check zilch count.  If at three, penalize banked points and reset
                // zilch count.
                if (zilchCount == 3) {
                    bankedSum -= 500;
                    zilchCount = 0;
                }
                if (randomZilchCount == 3) {
                    randomBankedSum -= 500;
                    randomZilchCount = 0;
                }
                // Turn is over.
                playRandom = !playRandom;
                numToRoll = 6;
            }
            else {
                // Add throw score to turn sum and report.
                turnSum += throwSum;
                System.out.println("\nTurn Sum: " + turnSum + " and " + numToRoll + " dice left to roll.");
                // If turn sum is greater than 300 you can choose to bank the turn
                // sum or roll again.
                if (turnSum >= 300 && numToRoll > 0) {
                    if (playRandom) {
                        // The AI player chooses.
                        if (numToRoll == 6)
                            System.out.println("Rolling again.");
                        else if (rollOneDice() > numToRoll) {
                            // Bank turn sum.
                            numToRoll = 6;
                            System.out.println("Random choice to bank.");
                            randomZilchCount = 0;
                            freeRoll = false;
                            randomBankedSum += turnSum;
                            turnSum = 0;
                            // Turn is over after banking.
                            playRandom = false;
                        }
                        else
                            System.out.println("Random choice to roll.");
                    } else {
                        // The human chooses.
                        prompt = "Do you want to (r)oll or (b)ank your turn sum? ";
                        input = playerPrompt(prompt);
                        if (input == 'q' || input == 'Q')
                            break;
                        if (input == 'b' || input == 'B') {
                            // Bank turn sum.
                            numToRoll = 6;
                            zilchCount = 0;
                            freeRoll = false;
                            bankedSum += turnSum;
                            turnSum = 0;
                            // Turn is over after banking.
                            playRandom = true;
                        }
                    }
                }
                else if (turnSum < 300 && numToRoll > 0)
                    System.out.println("Less than 300 still, you must roll again.");
            }
        }
        // Game is over.  Report results.
        System.out.println("\nYour score: " + bankedSum + " in " + rollCount + " rolls.");
        System.out.println("Random choices score: " + randomBankedSum + " in " + randomRollCount + " rolls.");
        if (bankedSum > randomBankedSum)
            System.out.println("You won! You are making good choices.");
        else
            System.out.println("You lost! What? You are not making good choices!");
    } // end runTrainer

    // Displays the instructions and starts the game.
    public static void main(String[] args) {
        displayIntro();
        runTrainer();
    } // end main

} // end ZilchTrainer
