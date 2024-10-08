class Vehicle {
  private String make;
  private String model;
  private Int year;
  private String color;
  private String vin;
  private String licensePlate;

  public Vehicle(String make, String model, int year, String color, String vin, String licensePlate) {
    this.make = make;
    this.model = model;
    this.year = year;
    this.color = color;
    this.vin = vin;
    this.licensePlate = licensePlate;
  }

  public String getDetails() {
    return "Vehicle Details:\n" + "Make: " + make + "\n" + "Model: " + model + "\n" + "Year: " + year + "\n" + "Color: " + color + "\n" + "VIN: " + vin + "\n" + "License Plate: " + licensePlate;
  }
}

public class VehicleRegistration {
  private JFrame frame;

  public VehicleRegistration() {
    frame = new JFrame("Vehicle Registration");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400,400);

  JPanel panel = new JPanel();
  panel.setLayout(new GridLayout(7,2));

  JLabel lblMake = new JLabel("Make:");
  JTextField txtMake = new JTextField(20);

  JLabel lblModel = new JLabel("Model:");
  JTextField txtModel = new JTextField(20);

  JLabel lblYear = new JLabel("Year:");
  JTextField txtYear = new JTextField(20);

  JLabel lblColor = new JLabel("Color:");
  JTextField txtColor = new JTextField(20);

  JLabel lblVIN = new JLabel("VIN:");
  JTextField txt = new JTextField(20);

  JLabel lblLicensePlate = new JLabel("License Plate:");
  JTextField txt = new JTextField(20);

  JButton btnRegister = new JButton("Register");

  btnRegister.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      String make = txtMake.getText();
      String model = txtModel.getText();
      String yearStr = txtYear.getText();
      String color = txtColor.getText();
      String vin = textVIN.getText();
      String licensePlate = txtLicensePlate.getText();

    if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || color.isEmpty || vin.isEmpty || licensePlate.isEmpty()) {
      JOptionPane.showMessageDialog(frame, "All fields must be filled out");
      return;
    }

    Vehicle vehicle = new Vehicle(make, model, year, color. vin, licensePlate);

    JOptionPane.showMessageDialog(frame, vehicle.getDetails(), "Vehicle Registered", JOptionPane.INFORMATION_MESSAGE);

    txtMake.setText("");
    txtModel.setText("");
    txtYear.setText("");
    txtColor.setText("");
    txtVIN.setText("");
    txtLicensePlate.setText("");
    }
  });

  panel.add(lblMake);
  panel.add(txtMake);
  panel.add(lblModel);
  panel.add(txtModel);
  panel.add(lblYear);
  panel.add(txtYear);
  panel.add(lblColor);
  panel.add(txtColor);
  panel.add(lblVIN);
  panel.add(txtVIN);
  panel.add(lblLicensePlate);
  panel.add(txtLicensePlate);
  panel.add(new JLabel(""));
  panel.add(btnRegister);

  frame.add(panel);
  frame.setVisible(true);
  }
}
