package com.example.demo.commandListener;

import com.example.demo.service.impl.AdminShowServiceImplementation;
import com.example.demo.service.impl.BuyerServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CommandLineServiceTest {
    @InjectMocks
    private CommandLineService commandLineService;
    @Mock
    private AdminShowServiceImplementation adminShowServiceImplementation;
    @Mock
    private BuyerServiceImplementation buyerServiceImplementation;

    @Test
    public void testAdminRoleCommands() {
        String input = "Admin\nSetup 1 2 3 4\nView 1\nAdd 2 5 6\nLeave\nexit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        commandLineService.run();

        verify(adminShowServiceImplementation, times(1)).setupShow("1", "2", "3", "4");
        verify(adminShowServiceImplementation, times(1)).view("1");
        verify(adminShowServiceImplementation, times(1)).add("2", "5", "6");
    }

    @Test
    public void testBuyerRoleCommands() {
        String input = "Buyer\nAvailability 1\nBook 1 3 A1\nCancel ABC-123 1\nLeave\nexit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        commandLineService.run();

        verify(buyerServiceImplementation, times(1)).availability("1");
        verify(buyerServiceImplementation, times(1)).book("1", "3", "A1");
        verify(buyerServiceImplementation, times(1)).cancel("ABC-123", "1");
    }

    @Test
    public void testWrongRoleCommands() {
        String input = "CEO\nexit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        commandLineService.run();

        verify(adminShowServiceImplementation, times(0)).setupShow(anyString(), anyString(), anyString(), anyString());
        verify(adminShowServiceImplementation, times(0)).view(anyString());
        verify(adminShowServiceImplementation, times(0)).add(anyString(), anyString(),anyString());
        verify(buyerServiceImplementation, times(0)).availability(anyString());
        verify(buyerServiceImplementation, times(0)).book(anyString(), anyString(), anyString());
        verify(buyerServiceImplementation, times(0)).cancel(anyString(), anyString());

    }

    @Test
    public void testWrongAdminCommands() {
        String input = "Admin\nCloseTheatre 1\nSetupp 1 2 3 4\nLeave\nexit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        commandLineService.run();
        verify(adminShowServiceImplementation, times(0)).setupShow(anyString(), anyString(), anyString(), anyString());
        verify(adminShowServiceImplementation, times(0)).view(anyString());
        verify(adminShowServiceImplementation, times(0)).add(anyString(), anyString(),anyString());
    }

    @Test
    public void testWrongBuyerCommands() {
        String input = "Buyer\nBuyTheatre 1\nAvailabilityy 1\nLeave\nexit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        commandLineService.run();
        verify(buyerServiceImplementation, times(0)).availability(anyString());
        verify(buyerServiceImplementation, times(0)).book(anyString(), anyString(), anyString());
        verify(buyerServiceImplementation, times(0)).cancel(anyString(), anyString());
    }
}
