/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package waterlevel.observer;

/**
 *
 * @author USER
 */

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

abstract class WaterLevelObserver extends JFrame {
    public abstract void update(int waterLevel);
}

class Alarm extends WaterLevelObserver {
    private int threshold = 50;

    @Override
    public void update(int waterLevel) {
        System.out.println(waterLevel >= threshold ? "Alarm ON" : "Alarm OFF");
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}

class Splitter extends WaterLevelObserver {
    private int threshold = 75;

    @Override
    public void update(int waterLevel) {
        System.out.println(waterLevel >= threshold ? "Splitter ON" : "Splitter OFF");
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}

class Display extends WaterLevelObserver {
    private JLabel waterLevelLabel;

    public Display() {
        setTitle("Water Level Display");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        waterLevelLabel = new JLabel("Water Level: 0", SwingConstants.CENTER);
        add(waterLevelLabel);
        setVisible(true);
    }

    @Override
    public void update(int waterLevel) {
        waterLevelLabel.setText("Water Level: " + waterLevel);
        System.out.println("Water Level: " + waterLevel);
    }
}

class SMSSender extends WaterLevelObserver {
    @Override
    public void update(int waterLevel) {
        System.out.println("Sending SMS: Water Level is " + waterLevel);
        logToFile("Sending SMS: Water Level is " + waterLevel);
    }

    private void logToFile(String message) {
        try (FileWriter fw = new FileWriter("sms_log.txt", true)) {
            fw.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ControlRoom {
    private int waterLevel;
    private final java.util.List<WaterLevelObserver> observerList = new ArrayList<>();

    public synchronized void addWaterLevelObserver(WaterLevelObserver ob) {
        observerList.add(ob);
    }

    public synchronized void setWaterLevel(int waterLevel) {
        if (this.waterLevel != waterLevel) {
            this.waterLevel = waterLevel;
            notifyObservers();
        }
    }

    private synchronized void notifyObservers() {
        for (WaterLevelObserver ob : observerList) {
            ob.update(waterLevel);
        }
    }
}

public class Demo {
    private static volatile boolean running = false;

    public static void main(String[] args) {
        ControlRoom controlRoom = new ControlRoom();

        Alarm alarm = new Alarm();
        Splitter splitter = new Splitter();
        Display display = new Display();
        SMSSender smsSender = new SMSSender();

        controlRoom.addWaterLevelObserver(alarm);
        controlRoom.addWaterLevelObserver(display);
        controlRoom.addWaterLevelObserver(smsSender);
        controlRoom.addWaterLevelObserver(splitter);

        // GUI to control thresholds and stop the program
        JFrame controlFrame = new JFrame("Control Room");
        controlFrame.setSize(400, 200);
        controlFrame.setLayout(new GridLayout(4, 2));

        JLabel alarmLabel = new JLabel("Alarm Threshold:");
        JTextField alarmField = new JTextField("50");
        JLabel splitterLabel = new JLabel("Splitter Threshold:");
        JTextField splitterField = new JTextField("75");
        JButton setButton = new JButton("Set Thresholds");
        JButton startButton = new JButton("Start Monitoring");
        JButton stopButton = new JButton("Stop Monitoring");

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int alarmThreshold = Integer.parseInt(alarmField.getText());
                int splitterThreshold = Integer.parseInt(splitterField.getText());
                alarm.setThreshold(alarmThreshold);
                splitter.setThreshold(splitterThreshold);
                System.out.println("Thresholds set: Alarm=" + alarmThreshold + ", Splitter=" + splitterThreshold);
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = true;
                new Thread(() -> {
                    Random random = new Random();
                    while (running) {
                        int waterLevel = random.nextInt(101);
                        controlRoom.setWaterLevel(waterLevel);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
                System.out.println("Monitoring started.");
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = false;
                System.out.println("Monitoring stopped.");
            }
        });

        controlFrame.add(alarmLabel);
        controlFrame.add(alarmField);
        controlFrame.add(splitterLabel);
        controlFrame.add(splitterField);
        controlFrame.add(setButton);
        controlFrame.add(startButton);
        controlFrame.add(stopButton);
        controlFrame.setVisible(true);
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
