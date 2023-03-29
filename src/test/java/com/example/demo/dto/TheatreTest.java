package com.example.demo.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TheatreTest {
    private Theatre theatre;

    @Mock
    Map<String,Show> shows;

    @BeforeEach
    public void setUp() {
        theatre = new Theatre(new HashMap<>());
    }
    @Test
    public void testAddShow() {
        theatre.addShow("1","2","3","4");
        assertTrue(theatre.getShows().containsKey("1"));
        assertEquals(theatre.getShows().get("1").getNumRows(),"2");
        assertEquals(theatre.getShows().get("1").getNumSeatsPerRow(),"3");
        assertEquals(theatre.getShows().get("1").getCancellationWindowInMinutes(),"4");
    }
}
