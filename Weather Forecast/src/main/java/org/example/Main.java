import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class Main extends JFrame {
    private JTextField cityTextField;
    private JTextArea resultTextArea;
    private JLabel iconLabel, cityLabel;

    public Main() {
        setTitle("Weather Information App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 340));
        setResizable(false);
        // Dark mode gradient background
        getContentPane().setBackground(new Color(32, 32, 32));

        // Text Label for enter city
        cityLabel = new JLabel("Enter City : ");
        cityLabel.setBackground(new Color(32, 32, 32));
        cityLabel.setForeground(new Color(240, 240, 240));
        cityLabel.setFont(new Font("Californian FB", Font.PLAIN, 16));

        // Text field for entering city
        cityTextField = new JTextField(20);
        cityTextField.setBackground(new Color(48, 48, 48));
        cityTextField.setForeground(new Color(240, 240, 240));
        cityTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(64, 64, 64), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        cityTextField.setFont(new Font("Californian FB", Font.PLAIN, 16));

        cityTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeather();
            }
        });

        // Text area for displaying weather information
        resultTextArea = new JTextArea(7, 30);
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setBackground(new Color(32, 32, 32));
        resultTextArea.setForeground(new Color(240,240,240));
        resultTextArea.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
        resultTextArea.setFont(new Font("Californian FB", Font.PLAIN, 16));

        // Button for fetching weather
        JButton submitButton = new JButton("Get Weather");
        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 153, 204), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeather();
            }
        });

        // Initialize iconLabel
        iconLabel = new JLabel();
        iconLabel.setForeground(new Color(240, 240, 240));

        // Panel for input components
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(32, 32, 32));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(cityLabel, BorderLayout.WEST);
        inputPanel.add(cityTextField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);

        // Scroll pane for result text area
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JToggleButton themeToggler = new JToggleButton("Toggle Theme");

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(32, 32, 32));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(resultScrollPane, BorderLayout.CENTER);
        mainPanel.add(iconLabel, BorderLayout.WEST);
        mainPanel.add(themeToggler, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Theme toggler
        themeToggler.setForeground(Color.WHITE);
        themeToggler.setBackground(new Color(0, 153, 204));
        themeToggler.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        themeToggler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (themeToggler.isSelected()) {
                    // Light mode
                    inputPanel.setBackground(new Color(255, 255, 190));
                    mainPanel.setBackground(new Color(255, 255, 190));
                    resultTextArea.setForeground(new Color(48, 48, 48));
                    resultTextArea.setBackground(new Color(255, 255, 185));
                    cityTextField.setForeground(new Color(48, 48, 48));
                    cityTextField.setBackground(new Color(255, 255, 185));
                    cityLabel.setForeground(new Color(32, 32, 32));
                    cityLabel.setBackground(new Color(255, 255, 190));
                    themeToggler.setForeground(Color.DARK_GRAY);
                    themeToggler.setBackground(new Color(255, 220, 100));
                } else {
                    // Dark mode
                    inputPanel.setBackground(new Color(32, 32, 32));
                    mainPanel.setBackground(new Color(32, 32, 32));
                    resultTextArea.setBackground(new Color(32, 32, 32));
                    resultTextArea.setForeground(new Color(240, 240, 240));
                    cityTextField.setBackground(new Color(48, 48, 48));
                    cityTextField.setForeground(new Color(240, 240, 240));
                    cityLabel.setBackground(new Color(32, 32, 32));
                    cityLabel.setForeground(new Color(240, 240, 240));
                    themeToggler.setForeground(Color.WHITE);
                    themeToggler.setBackground(new Color(0, 153, 204));
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void speakTemperature(String city, double temperature) {
        // Show a dialog with the temperature
        JOptionPane.showMessageDialog(this,
                "Temperature: " + String.format("%.1f", temperature) + "°C",
                "Temperature for " + city,
                JOptionPane.INFORMATION_MESSAGE);

        // Try to speak using system commands
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command;

            if (os.contains("win")) {
                // Windows
                command = "powershell -Command \"Add-Type -AssemblyName System.Speech; "
                        + "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; "
                        + "$speak.Speak('Temperature in " + city + " is " + String.format("%.1f", temperature) + " degrees Celsius');\"";
                Runtime.getRuntime().exec(command);
            } else if (os.contains("mac")) {
                // macOS
                command = "say \"Temperature in " + city + " is " + String.format("%.1f", temperature) + " degrees Celsius\"";
                Runtime.getRuntime().exec(command);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                // Linux
                command = "spd-say \"Temperature in " + city + " is " + String.format("%.1f", temperature) + " degrees Celsius\"";
                Runtime.getRuntime().exec(command);
            }
        } catch (IOException e) {
            System.err.println("Text-to-speech not available: " + e.getMessage());
            // Play a confirmation sound instead
            playConfirmSound();
        }
    }

    private void playConfirmSound() {
        try {
            // Play a simple confirmation beep
            Toolkit.getDefaultToolkit().beep();
            Thread.sleep(100);
            Toolkit.getDefaultToolkit().beep();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    private void fetchWeather() {
        String city = cityTextField.getText().trim();
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String apiKey = "32ecb285ca54c7666cb9bf7177dc3feb"; // API key

        resultTextArea.setText("Fetching weather data for " + city + "...");

        // Use a separate thread for network operations
        new Thread(() -> {
            try {
                String encodedCity = URLEncoder.encode(city, "UTF-8");
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiKey;
                URL url = new URL(urlString);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String jsonResponse = response.toString();
                    String weatherDescription = parseWeatherDescription(jsonResponse);
                    double temperature = parseTemperature(jsonResponse);
                    int humidity = parseHumidity(jsonResponse);
                    double windSpeed = parseWindSpeed(jsonResponse);
                    String icon = parseIconName(jsonResponse);

                    // Update UI on the Event Dispatch Thread
                    SwingUtilities.invokeLater(() -> {
                        resultTextArea.setText("Weather in " + city + ":\n" +
                                "Description: " + weatherDescription + "\n" +
                                "Temperature: " + String.format("%.1f", temperature) + "°C\n" +
                                "Humidity: " + humidity + "%\n" +
                                "Wind Speed: " + windSpeed + " m/s");

                        displayWeatherIcon(icon);

                        // Speak the temperature
                        speakTemperature(city, temperature);
                    });

                } else {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(Main.this,
                                "Failed to fetch weather data. Please check the city name and try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
                connection.disconnect();
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(Main.this,
                            "Error: " + e.getMessage() + "\nPlease check your internet connection.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    resultTextArea.setText("Error: " + e.getMessage());
                });
            }
        }).start();
    }

    private String parseWeatherDescription(String jsonResponse) {
        try {
            int startIndex = jsonResponse.indexOf("\"description\":\"") + "\"description\":\"".length();
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            return jsonResponse.substring(startIndex, endIndex);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private double parseTemperature(String jsonResponse) {
        try {
            int startIndex = jsonResponse.indexOf("\"temp\":") + "\"temp\":".length();
            int endIndex = jsonResponse.indexOf(",", startIndex);
            String tempString = jsonResponse.substring(startIndex, endIndex);
            return Double.parseDouble(tempString) - 273.15;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private int parseHumidity(String jsonResponse) {
        try {
            int startIndex = jsonResponse.indexOf("\"humidity\":") + "\"humidity\":".length();
            int endIndex = jsonResponse.indexOf(",", startIndex);
            String humidityString = jsonResponse.substring(startIndex, endIndex).replaceAll("[^0-9]", "");
            return Integer.parseInt(humidityString);
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseWindSpeed(String jsonResponse) {
        try {
            int startIndex = jsonResponse.indexOf("\"speed\":") + "\"speed\":".length();
            int endIndex = jsonResponse.indexOf(",", startIndex);
            String windSpeedString = jsonResponse.substring(startIndex, endIndex);
            return Double.parseDouble(windSpeedString);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private String parseIconName(String jsonResponse) {
        try {
            int startIndex = jsonResponse.indexOf("\"icon\":\"") + "\"icon\":\"".length();
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            return jsonResponse.substring(startIndex, endIndex);
        } catch (Exception e) {
            return "01d"; // Default icon code
        }
    }

    private void displayWeatherIcon(String iconCode) {
        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";

        new Thread(() -> {
            try {
                URL url = new URL(iconUrl);
                BufferedImage img = ImageIO.read(url);
                ImageIcon icon = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));

                SwingUtilities.invokeLater(() -> {
                    iconLabel.setIcon(icon);
                });
            } catch (IOException e) {
                System.err.println("Error loading weather icon: " + e.getMessage());
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}