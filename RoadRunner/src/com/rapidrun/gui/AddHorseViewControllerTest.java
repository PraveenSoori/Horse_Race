package com.rapidrun.gui;

import com.rapidrun.model.Horse;
import com.rapidrun.model.HorseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddHorseViewControllerTest {
    private HorseManager horseManager;

    @BeforeEach
    void setUp() {
        horseManager = new HorseManager();
    }

    @Test
    void addHorseSuccessfully() {
        Horse horse = new Horse("1", "TestHorse", "TestJockey", 5, "Thoroughbred", "0/0", "A", "path/to/image");
        horseManager.addHorse(horse);
        assertFalse(horseManager.getHorses().isEmpty(), "The horse list should not be empty after adding a horse.");
        assertEquals(1, horseManager.getHorses().size(), "The horse list size should be 1 after adding a horse.");
        assertEquals(horse, horseManager.getHorseById("1"), "The added horse should be retrievable by its ID.");
    }
}