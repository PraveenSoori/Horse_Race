package com.rapidrun.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;



public class HorseManager {
    private static final ObservableList<Horse> horses = FXCollections.observableArrayList();
    private static final Map<String, Horse> selectedRaceHorses = new HashMap<>(); // For selected race horses


    private static final String HORSE_FILE_PATH = "horses.txt";


    public static Map<String, Horse> getSelectedRaceHorses() { return selectedRaceHorses; }
    public static ObservableList<Horse> getHorses() {
        return horses;
    }


    // Adds a horse to the list and saves changes to the file
    public static void addHorse(Horse horse) {
        horses.add(horse);
        saveHorsesToFile();
    }

    // Retrieves a horse by ID from the list
    public static Horse getHorseById(String id) {
        for (Horse horse : horses) {
            if (horse.getId().equals(id)) {
                return horse;
            }
        }
        return null;
    }


    // SORT HORSES ==========================================================
    public static ObservableList<Horse> sortHorsesById() {
        // Create a temporary list to sort
        List<Horse> tempHorses = new ArrayList<>(horses);

        // selection sort with integer comparison
        for (int i = 0; i < tempHorses.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < tempHorses.size(); j++) {
                int idCurrent = Integer.parseInt(tempHorses.get(j).getId());
                int idMin = Integer.parseInt(tempHorses.get(minIndex).getId());
                if (idCurrent < idMin) {
                    minIndex = j;
                }
            }
            Horse temp = tempHorses.get(minIndex);
            tempHorses.set(minIndex, tempHorses.get(i));
            tempHorses.set(i, temp);
        }

        return FXCollections.observableArrayList(tempHorses);
    }
    // END SORT ==============================================================



    // Updates a horse in the list and saves changes to the file
    public static void updateHorse(Horse updatedHorse) {
        int index = horses.indexOf(getHorseById(updatedHorse.getId()));
        if (index != -1) {
            horses.set(index, updatedHorse);
            saveHorsesToFile();
        }
    }

    // Deletes a horse from the list and saves changes to the file
    public static boolean deleteHorse(String id) {
        boolean removed = horses.removeIf(horse -> horse.getId().equals(id));
        if (removed) {
            saveHorsesToFile();
        }
        return removed;
    }


    public static boolean isIdUnique(String id) {
        return horses.stream().noneMatch(horse -> horse.idProperty().get().equals(id));
    }


    public static void saveHorsesToFile() {
        File file = new File(HORSE_FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Group horses by the 'group' attribute
            Map<String, List<Horse>> groupedHorses = horses.stream()
                    .collect(Collectors.groupingBy(Horse::getGroup));

            // Write each group and its horses to the file
            for (Map.Entry<String, List<Horse>> entry : groupedHorses.entrySet()) {
                String group = entry.getKey();
                List<Horse> groupHorses = entry.getValue();

                writer.write("Group " + group);
                writer.newLine();

                for (Horse horse : groupHorses) {
                    writer.write(horse.toFileString());
                    writer.newLine();
                }

                writer.newLine(); // Add a newline for separation between groups
            }
        } catch (IOException e) {
            System.err.println("Error saving horses to file: " + e.getMessage());
        }
    }


    public static void loadHorsesFromFile() {
        File file = new File(HORSE_FILE_PATH);
        if (!file.exists()) {
            System.out.println("File does not exist: " + HORSE_FILE_PATH);
            return; // File doesn't exist, no horses to load.
        }

        try (Stream<String> stream = Files.lines(Paths.get(HORSE_FILE_PATH))) {
            stream.filter(line -> line.contains(",") && !line.trim().startsWith("Group"))
                    .forEach(line -> {
                        try {
                            String[] details = line.split(",");
                            if (details.length >= 8) {
                                Horse horse = new Horse(
                                        details[0].trim(), // ID
                                        details[1].trim(), // Name
                                        details[2].trim(), // Jockey Name
                                        Integer.parseInt(details[3].trim()), // Age
                                        details[4].trim(), // Breed
                                        details[5].trim(), // Race Record
                                        details[6].trim(), // Group
                                        details[7].trim()  // Image Path
                                );
                                horses.add(horse);
                            }
                        } catch (Exception e) {
                            System.err.println("Failed to parse horse from line: " + line);
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            System.err.println("Failed to read file: " + HORSE_FILE_PATH);
            e.printStackTrace();
        }
    }


//=====================================================================================================

    public static void selectTopHorsesRandomly() {
        horses.clear(); // Clear existing horse list
        selectedRaceHorses.clear(); // Clear selected horses list
        loadHorsesFromFile(); // Load fresh data from the file

        Map<String, List<Horse>> groupedHorses = horses.stream()
                .collect(Collectors.groupingBy(Horse::getGroup));

        for (Map.Entry<String, List<Horse>> entry : groupedHorses.entrySet()) {
            List<Horse> horsesInGroup = entry.getValue();

            // Calculate total win rate sum for weighted probability
            double totalWinRateSum = horsesInGroup.stream()
                    .mapToDouble(horse -> parseWinRate(horse.getRaceRecord()))
                    .sum();

            double r = new Random().nextDouble() * totalWinRateSum;
            double cumulativeSum = 0;

            for (Horse horse : horsesInGroup) {
                cumulativeSum += parseWinRate(horse.getRaceRecord());
                if (cumulativeSum >= r) {
                    selectedRaceHorses.put(entry.getKey(), horse);
                    break;
                }
            }
        }
    }

    private static double parseWinRate(String raceRecord) {
        if (raceRecord == null || !raceRecord.contains("/")) {
            System.err.println("Invalid race record format: " + raceRecord);
            return 0;
        }

        String[] parts = raceRecord.split("/");
        if (parts.length < 2) {
            System.err.println("Incomplete race record data: " + raceRecord);
            return 0;
        }

        try {
            int wins = Integer.parseInt(parts[0]);
            int totalRaces = Integer.parseInt(parts[1]);
            return (wins / (double) totalRaces) * 100; // Win rate as a percentage
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numbers from race record: " + raceRecord);
            return 0;
        }
    }




    //===========================================================================================
    //after race is run
    // Simulates a race by assigning a random race time to each horse
    public static void simulateRaceForSelectedHorses() {
        Random random = new Random();
        selectedRaceHorses.values().forEach(horse -> {
            double raceTime = random.nextDouble() * 90; // Simulate race time between 0 and 90 seconds
            horse.setRaceTime(raceTime);
        });
    }


//===========================================================================================

    public static boolean hasMinimumSelectedHorsesPerGroup() {
        Set<String> foundGroups = new HashSet<>();

        for (Horse horse : horses) {
            foundGroups.add(horse.getGroup());
        }

        if(foundGroups.size()<4){
            return false;
        }

        return true;
    }

    // GET TOP 3 Horses ==================================================================================

    public static List<Horse> getTopThreeHorses() {
        return getSelectedRaceHorses().values().stream()
                .sorted(Comparator.comparingDouble(Horse::getRaceTime))
                .limit(3) // Only get the top three horses
                .collect(Collectors.toList());
    }




    //TESTING PARTS===================================================================
    public static void clearHorses() {
        horses.clear();
    }

}



