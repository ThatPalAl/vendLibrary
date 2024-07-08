package com.example;

import com.example.gui.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("No graphical environment detected. Running in headless mode.");
            return;
        }

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        SwingUtilities.invokeLater(() -> new MainFrame(context));
    }
}
