import java.io.*;
import java.util.*;

class AssassinManagerRevenge{

    private AssassinNode frontOfKillRing;
    private AssassinNode frontOfGraveyard;
    private int graveSize = 0;
    private boolean listModified = false;

    /*AssassinManager(List<String> names){
        System.out.println("Welcome to Nerf Assassin Manager.\n");

        String[] inputNames = getInput();
        for(int i = 0; i < inputNames.length; i++){
            String name = inputNames[i];
            AssassinNode assassin = new AssassinNode(name);

            if(frontOfKillRing == null){
                frontOfKillRing = assassin;
            } else {
                AssassinNode current = frontOfKillRing;
                while(current.next != null){
                    current = current.next;
                }
                current.next = assassin;
            }
        }
        int num = inputNames.length;
        while (!gameOver()) {
            System.out.println();
            toStringKillRing(num);
            toStringGraveyard();
            System.out.println("------------------------------------------\n");
            kill(getKill());
            num--;
        }
        System.out.println("\n" + winner() + " is the winner!\n");
        toStringGraveyard();
        System.out.println("Thank you for using Assassin Manager.");
    }*/

    AssassinManagerRevenge(List<String> names) {
        System.out.println("Welcome to Nerf Assassin Manager.\n");
        System.out.println("Enter the current game's filename.");
        System.out.println("Enter a filename if you are starting a new game.");
        System.out.print("File: ");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        String directory = System.getProperty("user.home");
        String absolutePath = directory + File.separator + fileName;

        int gameSize = readFile(absolutePath);

        char letter = toStringPreGameMenu();

        while (letter != 'b' && letter != 'B') {
            if (letter == 'r' || letter == 'R') {
                gameSize = registerNewPlayers().length;
            } else if (letter == 's' || letter == 'S') {
                saveFile(absolutePath);
            } else if (letter == 'l' || letter == 'L') {
                listAllPlayers();
            } else if (letter == 'q' || letter == 'Q'){
                quit(absolutePath);
                break;
            }
            letter = toStringPreGameMenu();
        }
        if (letter != 'q' && letter != 'Q') {
            char beginGame = beginTheGame();
            if (beginGame == 'y' || beginGame == 'Y') {
                while (!gameOver()) {
                    letter = toStringActiveGameMenu();
                    if (letter == 'a' || letter == 'A') {
                        addNewElimination();
                        gameSize--;
                    } else if (letter == 'p' || letter == 'P') {
                        printGameStatus(gameSize);
                    } else if (letter == 'i' || letter == 'I') {
                        individualStatus();
                    } else if (letter == 's' || letter == 'S') {
                        saveFile(absolutePath);
                    } else if (letter == 'q' || letter == 'Q') {
                        quit(absolutePath);
                        break;
                    }
                }
                if (gameSize == 1) {
                    System.out.println("\n" + winner() + " is the winner!\n");
                    toStringGraveyard();
                    System.out.println("Thank you for using Assassin Manager.");
                }
            }
        }
    }

    private void saveFile(String string) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(string))){
            AssassinNode current = frontOfKillRing;
            while(current != null) {
                bufferedWriter.write(current.getName());
                bufferedWriter.newLine();
                current = current.next;
            }
            System.out.println("Game saved successfully in " + string);
        } catch (IOException ignored) { }
        listModified = false;
    }

    private int readFile(String string) {
        int count = 0;
        int gameSize = 0;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(string))){
            ArrayList<String> namesList = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null){
                namesList.add(line);
                line = bufferedReader.readLine();
                count++;
                gameSize++;
            }
            if (count > 0){
                System.out.println("\nUsing existing game.");
            } else {
                System.out.println("\nStarting a new game of Nerf Assassin.");
            }
            String[] inputNames = new String[namesList.size()];
            for (int j = 0; j < namesList.size(); j++) {
                inputNames[j] = namesList.get(j);
            }
            for(int i = 0; i < inputNames.length; i++){
                String name = inputNames[i];
                AssassinNode assassin = new AssassinNode(name);

                if(frontOfKillRing == null){
                    frontOfKillRing = assassin;
                } else {
                    AssassinNode current = frontOfKillRing;
                    while(current.next != null){
                        current = current.next;
                    }
                    current.next = assassin;
                }
            }
            return gameSize;
        } catch (IOException ignored) { }
        return gameSize;
    }

    private String[] registerNewPlayers(){
        String[] inputNames = getInput();
        for(int i = 0; i < inputNames.length; i++){
            String name = inputNames[i];
            AssassinNode assassin = new AssassinNode(name);

            if(frontOfKillRing == null){
                frontOfKillRing = assassin;
            } else {
                AssassinNode current = frontOfKillRing;
                while(current.next != null){
                    current = current.next;
                }
                current.next = assassin;
            }
        }
        listModified = true;
        return inputNames;
    }

    private void listAllPlayers(){
        AssassinNode current = frontOfKillRing;
        while(current != null){
            System.out.println(current.getName());
            current = current.next;
        }
    }

    private char beginTheGame(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("** Once you begin the game, you will no longer be able to register players.");
        System.out.print("Are you sure you want to begin? (y/n) ");
        return scanner.next().charAt(0);
    }

    private void quit(String string) {
        String saveLetter;
        if (listModified) {
            System.out.println("**** You have modified the game since the last save. ****");
            System.out.print("Save game? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            saveLetter = scanner.nextLine();
        } else {
            saveLetter = "y";
        }

        if (saveLetter.equalsIgnoreCase("y")){
            saveFile(string);
        } else {
            try(OutputStreamWriter flusher = new OutputStreamWriter(new FileOutputStream(string))) {
                AssassinNode current = frontOfKillRing;
                while (current != null) {
                    flusher.flush();
                    current = current.next;
                }
            } catch (IOException ignored) { }
        }
        System.out.println();
    }

    private void addNewElimination(){
        System.out.print("Next player eliminated? ");
        Scanner scanner = new Scanner(System.in);
        String newElimination = scanner.nextLine();
        kill(newElimination);
        listModified = true;
    }

    private void printGameStatus(int num){
        toStringKillRing(num);
        toStringGraveyard();
    }

    private void individualStatus(){
        System.out.print("What player's status would you like to see? ");
        Scanner scanner = new Scanner(System.in);
        String playerStatus = scanner.nextLine();

        AssassinNode current = frontOfGraveyard;
        int num = 0;
        while(current != null){
            if(playerStatus.equals(current.getKiller())){
                num++;
            }
            current = current.next;
        }
        if (killRingContains(playerStatus)) {
            System.out.println(playerStatus + " is still in the game and has " + num + " kill(s).");
        } else if(graveyardContains(playerStatus)) {
            System.out.println(playerStatus + " is out of the game and had " + num + " kills.");
        }else {
            System.out.println("There is no player named " + playerStatus);
        }
    }

    private char toStringPreGameMenu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n** Pre-game Menu **");
        System.out.println("(R)egister new players");
        System.out.println("(L)ist all players");
        System.out.println("(B)egin the game");
        System.out.println("(S)ave the game");
        System.out.println("(Q)uit Nerf Assassin Manager");
        return scanner.next().charAt(0);
    }

    private char toStringActiveGameMenu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n** Active-game Menu **");
        System.out.println("(A)dd new elimination");
        System.out.println("(P)rint game status");
        System.out.println("(I)ndividual status");
        System.out.println("(S)ave the game");
        System.out.println("(Q)uit Nerf Assassin Manager");
        return scanner.next().charAt(0);
    }

    ///////////////////////////////////////////////////////////////////////////

    private String[] getInput(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the names of the players, one per line.");
        System.out.println("(Enter the name DONE to quit.)");
        ArrayList<String> namesList = new ArrayList<String>();
        String name = null;
        while (!(name = scanner.nextLine()).isEmpty())  {
            if(!name.equalsIgnoreCase("DONE")) {
                namesList.add(name);
            }else{
                break;
            }
        }
        String[] namesArray = new String[namesList.size()];
        for(int j =0;j<namesList.size();j++){
            namesArray[j] = namesList.get(j);
        }
        return namesArray;
    }

    private String getKill(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Next player eliminated? ");
        return scanner.nextLine();
    }

    private void toStringKillRing(int gameSize){
        AssassinNode current = frontOfKillRing;
        System.out.println("Current kill ring ("+ gameSize + " players remaining):" );
        while(current != null){
            if(current.next == null){
                System.out.println(current.getName() + " is stalking " + frontOfKillRing.getName());
            } else {
                System.out.println(current.getName() + " is stalking " + current.next.getName());
            }
            current = current.next;
        }
        System.out.println();
    }

    private void toStringGraveyard(){
        if(!gameOver()){
            System.out.println("Current graveyard (" + graveSize + " players eliminated):");
        } else {
            System.out.println("The final graveyard is as follows:");
        }
        if(graveSize == 0){
            System.out.print("Empty");
        }

        AssassinNode current = frontOfGraveyard;
        int num = 1;
        while(current != null){
            if(gameOver()){
                System.out.print(num + ". ");
                num++;
            }
            if(current.next == null){
                System.out.println(current.getName() + " was killed by " + current.getKiller());
            } else {
                System.out.println(current.getName() + " was killed by " + current.getKiller());
            }
            current = current.next;
        }
    }

    private boolean killRingContains(String name){
        AssassinNode current = frontOfKillRing;
        while(current != null){
            if(current.getName().equals(name)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private boolean graveyardContains(String name){
        AssassinNode current = frontOfGraveyard;
        while(current != null){
            if(current.getName().equals(name)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private boolean gameOver(){
        return (frontOfKillRing.next == null);
    }

    private String winner(){
        return frontOfKillRing.getName();
    }

    private void kill(String name){
        if(gameOver()){
            throw new IllegalStateException();
        }
        if(!killRingContains(name)){
            throw new IllegalArgumentException("Player does not exist in Kill Ring!");
        } else {
            AssassinNode assassinated = null;

            if(frontOfKillRing.getName().equals(name)){
                String assassin = null;
                AssassinNode current = frontOfKillRing;
                while(current != null){
                    if(current.next == null){
                        assassin = current.getName();
                    }
                    current = current.next;
                }
                assassinated = frontOfKillRing;
                assassinated.setKiller(assassin);
                frontOfKillRing = frontOfKillRing.next;
            } else {
                AssassinNode current = frontOfKillRing;
                while(current.next != null){
                    if(current.next.getName().equals(name)){
                        String assassin = current.getName();
                        assassinated = current.next;
                        assassinated.setKiller(assassin);
                        if(current.next.next != null){
                            current.next = current.next.next;
                            break;
                        } else {
                            current.next = null;
                            break;
                        }
                    }
                    current = current.next;
                }
            }
            if(frontOfGraveyard != null){
                assassinated.next = frontOfGraveyard;
            } else {
                assassinated.next = null;
            }
            frontOfGraveyard = assassinated;
        }
        graveSize++;
    }
}