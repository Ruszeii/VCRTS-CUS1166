package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import main.VehicleOwner;

public class VehicleOwnerDashboard {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private VehicleOwner vehicleOwner;

    public VehicleOwnerDashboard(VehicleOwner vehicleOwner) {
        if (vehicleOwner == null) {
            throw new IllegalArgumentException("VehicleOwner cannot be null.");
        }
        this.vehicleOwner = vehicleOwner;

        
        frame = new JFrame("Vehicle Owner Dashboard");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        
        int xPosition = screenWidth - frame.getWidth();  
        int yPosition = (screenHeight - frame.getHeight()) / 2;  
        frame.setLocation(xPosition, yPosition);

        
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = ImageIO.read(new File("clientdashboard.jpg"));
                } catch (IOException e) {
                    System.out.println("Background image not found: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Initialize table model and add columns
        tableModel = new DefaultTableModel(new Object[]{
                "Vehicle ID", "Make", "Model", "Color", "Year", "License Plate", "Residency"
        }, 0);

        // Create table and add to a scroll pane
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setOpaque(false);
        table.setBackground(new Color(0, 0, 0, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        // Load vehicles from file
        loadVehicles(vehicleOwner.getID());

        // Add components to the background panel
        backgroundPanel.add(new JLabel(vehicleOwner.getDetails(), SwingConstants.CENTER), BorderLayout.NORTH);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Add background panel to the frame
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Create a separate button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // "Register New Vehicle" button
        JButton btnRegisterVehicle = new JButton("Add a Vehicle");
        styleButton(btnRegisterVehicle);
        btnRegisterVehicle.setIcon(new ImageIcon("plus.png"));
        btnRegisterVehicle.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnRegisterVehicle.addActionListener(e -> {
            new VehicleRegistration(vehicleOwner);
            frame.dispose();
        });

        // "Access Vehicle Acknowledgments" button
        JButton btnVehicleAcknowledgments = new JButton("Notifications");
        styleButton(btnVehicleAcknowledgments);
        btnVehicleAcknowledgments.setIcon(new ImageIcon("Bell.png"));
        btnVehicleAcknowledgments.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnVehicleAcknowledgments.addActionListener(e -> {
            new vehicleAcknowledgment();
            reloadVehicles();
        });

        // "Logout" button
        JButton btnReturn = new JButton("Logout");
        styleButton(btnReturn);
        btnReturn.setIcon(new ImageIcon("powerButton.png"));
        btnReturn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnReturn.addActionListener(e -> {
            new GUIWindow();
            frame.dispose();
        });

        // Add buttons to the button panel
        buttonPanel.add(btnReturn, BorderLayout.WEST);
        buttonPanel.add(btnRegisterVehicle, BorderLayout.CENTER);
        buttonPanel.add(btnVehicleAcknowledgments, BorderLayout.EAST);

        // Add button panel to the frame (below the background)
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Make the frame visible
        frame.setVisible(true);
        
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 50));
        button.setFont(new Font("Inter", Font.BOLD, 14));
        button.setBackground(new Color(217, 217, 217)); // Light gray
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
    }

    private void loadVehicles(int ownerId) {
        String fileName = "VehicleRegistrations.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String vehicleId = null, make = null, model = null, licensePlate = null, color = null;
            int year = 0;
            double residency = 0.0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("VIN:")) {
                    vehicleId = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Make:")) {
                    make = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Model:")) {
                    model = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Color:")) {
                    color = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Year:")) {
                    try {
                        year = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    } catch (NumberFormatException e) {
                        year = 0;
                    }
                } else if (line.startsWith("License Plate:")) {
                    licensePlate = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Time Available:")) {
                    residency = Double.parseDouble(line.substring(line.indexOf(":") + 1).trim());
                }

                if (vehicleId != null && make != null && model != null && color != null && licensePlate != null && year != 0 && residency != 0) {
                    Object[] row = new Object[]{vehicleId, make, model, color, year, licensePlate, residency};
                    tableModel.addRow(row);

                    vehicleId = make = model = color = licensePlate = null;
                    year = 0;
                    residency = 0.0;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading vehicle data", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    private void reloadVehicles() {
        // Clear the existing rows from the table
        tableModel.setRowCount(0);
        
        // Reload jobs from the file
        loadVehicles(vehicleOwner.getID());
    }
}
