package CRUD;

import java.sql.*;
import javax.swing.*;
import java.awt.event.ActionListener;

public class CowSalesSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/2210010499";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // CRUD Operations for Cows
    public static void createCow(String breed, int age, float weight, double price) {
        String query = "INSERT INTO Cows (Breed, Age, Weight, Price) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, breed);
            pstmt.setInt(2, age);
            pstmt.setFloat(3, weight);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readCows() {
        String query = "SELECT * FROM Cows";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("CowID") + ", Breed: " + rs.getString("Breed") + ", Age: " + rs.getInt("Age") + ", Weight: " + rs.getFloat("Weight") + ", Price: " + rs.getDouble("Price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCow(int id, String breed, int age, float weight, double price) {
        String query = "UPDATE Cows SET Breed = ?, Age = ?, Weight = ?, Price = ? WHERE CowID = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, breed);
            pstmt.setInt(2, age);
            pstmt.setFloat(3, weight);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCow(int id) {
        String query = "DELETE FROM Cows WHERE CowID = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Cow getCowById(int id) {
        Cow cow = null;
        String query = "SELECT * FROM Cows WHERE CowID = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String breed = rs.getString("Breed");
                int age = rs.getInt("Age");
                float weight = rs.getFloat("Weight");
                double price = rs.getDouble("Price");
                cow = new Cow(id, breed, age, weight, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cow;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cow Sales System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            JPanel panel = new JPanel();
            frame.add(panel);
            placeComponents(panel);

            frame.setVisible(true);
        });
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel breedLabel = new JLabel("Breed:");
        breedLabel.setBounds(10, 20, 80, 25);
        panel.add(breedLabel);

        JTextField breedText = new JTextField(20);
        breedText.setBounds(100, 20, 165, 25);
        panel.add(breedText);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(10, 50, 80, 25);
        panel.add(ageLabel);

        JTextField ageText = new JTextField(20);
        ageText.setBounds(100, 50, 165, 25);
        panel.add(ageText);

        JLabel weightLabel = new JLabel("Weight:");
        weightLabel.setBounds(10, 80, 80, 25);
        panel.add(weightLabel);

        JTextField weightText = new JTextField(20);
        weightText.setBounds(100, 80, 165, 25);
        panel.add(weightText);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(10, 110, 80, 25);
        panel.add(priceLabel);

        JTextField priceText = new JTextField(20);
        priceText.setBounds(100, 110, 165, 25);
        panel.add(priceText);

        JButton addButton = new JButton("Add Cow");
        addButton.setBounds(10, 140, 120, 25);
        panel.add(addButton);

        JButton updateButton = new JButton("Update Cow");
        updateButton.setBounds(140, 140, 120, 25);
        panel.add(updateButton);

        JButton deleteButton = new JButton("Delete Cow");
        deleteButton.setBounds(270, 140, 120, 25);
        panel.add(deleteButton);

        JButton readButton = new JButton("View Cows");
        readButton.setBounds(400, 140, 120, 25);
        panel.add(readButton);

        JTextArea outputArea = new JTextArea();
        outputArea.setBounds(10, 170, 560, 180);
        panel.add(outputArea);

        addButton.addActionListener(e -> {
            try {
                String breed = breedText.getText();
                int age = Integer.parseInt(ageText.getText().trim());
                float weight = Float.parseFloat(weightText.getText().trim());
                double price = Double.parseDouble(priceText.getText().trim());
                createCow(breed, age, weight, price);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers for Age, Weight, and Price.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Cow ID to update:").trim());
                Cow cow = getCowById(id);
                if (cow != null) {
                    breedText.setText(cow.getBreed());
                    ageText.setText(String.valueOf(cow.getAge()));
                    weightText.setText(String.valueOf(cow.getWeight()));
                    priceText.setText(String.valueOf(cow.getPrice()));

                    // Add an additional action listener to update button to confirm the update
                    updateButton.addActionListener(ev -> {
                        try {
                            String breed = breedText.getText();
                            int age = Integer.parseInt(ageText.getText().trim());
                            float weight = Float.parseFloat(weightText.getText().trim());
                            double price = Double.parseDouble(priceText.getText().trim());
                            updateCow(id, breed, age, weight, price);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid numbers for Age, Weight, and Price.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(panel, "Cow with ID " + id + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter a valid number for ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Cow ID to delete:").trim());
                deleteCow(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter a valid number for ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        readButton.addActionListener(e -> {
            outputArea.setText("");
            String query = "SELECT * FROM Cows";
            try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    outputArea.append("ID: " + rs.getInt("CowID") + ", Breed: " + rs.getString("Breed") + ", Age: " + rs.getInt("Age") + ", Weight: " + rs.getFloat("Weight") + ", Price: " + rs.getDouble("Price") + "\n");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}

class Cow {
    private int id;
    private String breed;
    private int age;
    private float weight;
    private double price;

    public Cow(int id, String breed, int age, float weight, double price) {
        this.id = id;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public float getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }
}
