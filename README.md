# Water Level Monitoring System

This Java program monitors water levels and triggers various actions based on predefined thresholds. The program is designed with a GUI that allows users to set thresholds for alarms and splitters, start and stop the monitoring process, and display the current water level.

## Features

- **Real-Time Water Level Monitoring**: Continuously monitors water levels and updates the status every second.
- **Alarm System**: Activates an alarm when the water level exceeds a specified threshold.
- **Splitter Control**: Activates a splitter when the water level exceeds a higher threshold.
- **Display Panel**: Shows the current water level in a separate window.
- **SMS Notification**: Logs water level updates to a file, simulating SMS notifications.
- **User Control**: GUI allows setting thresholds for the alarm and splitter, and provides buttons to start and stop the monitoring.

## How It Works

- The `ControlRoom` class manages the water level and notifies observers whenever the water level changes.
- Observers (`Alarm`, `Splitter`, `Display`, `SMSSender`) are notified of water level changes and take appropriate actions based on the current water level.
- A simple GUI allows users to set thresholds, start monitoring, and stop monitoring.

## Getting Started

1. **Clone the repository**:
    ```bash
    git clone  https://github.com/KaveeshaUdanii/Water-Level-Observer.git
    cd WaterLevelObserver
    ```

2. **Compile and run the program**:
    ```bash
    javac Demo.java
    java Demo
    ```

## Usage

- **Set Thresholds**: Enter the desired thresholds for the alarm and splitter in the respective fields and click "Set Thresholds".
- **Start Monitoring**: Click "Start Monitoring" to begin observing the water levels.
- **Stop Monitoring**: Click "Stop Monitoring" to end the observation.


