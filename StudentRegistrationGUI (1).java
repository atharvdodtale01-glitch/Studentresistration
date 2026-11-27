import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Model class: holds the data of one registered student.
 */
class Student {
    private final String name, rollNo, prn, email, branch, semester, gender;

    public Student(String name, String rollNo, String prn,
                   String email, String branch,
                   String semester, String gender) {
        this.name = name;
        this.rollNo = rollNo;
        this.prn = prn;
        this.email = email;
        this.branch = branch;
        this.semester = semester;
        this.gender = gender;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getPrn() {
        return prn;
    }

    /** Returns the values in the same order as the table columns. */
    public Object[] getDetails() {
        return new Object[]{
            name, rollNo, prn, email, branch, semester, gender
        };
    }
}

/**
 * View + controller: Swing window to register, list and delete students.
 */
public class StudentRegistrationGUI extends JFrame {

    private JTextField nameField, rollField, prnField, emailField;
    private JComboBox<String> branchBox, semesterBox;
    private JRadioButton male, female, other;
    private ButtonGroup genderGroup;

    private JTable table;
    private DefaultTableModel model;

    private final ArrayList<Student> students = new ArrayList<>();

    public StudentRegistrationGUI() {

        setTitle("Student Registration System");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        mainPanel.setBackground(new Color(240, 245, 250));

        // ---------- Heading ----------
        JLabel heading = new JLabel("STUDENT REGISTRATION SYSTEM", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(new Color(25, 65, 110));
        mainPanel.add(heading, BorderLayout.NORTH);

        // ---------- Form (7 fields -> 7 rows) ----------
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 12, 12));
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter Student Details"));
        formPanel.setBackground(Color.WHITE);

        nameField = new JTextField();
        rollField = new JTextField();
        prnField = new JTextField();
        emailField = new JTextField();

        branchBox = new JComboBox<>(new String[]{
            "Computer Engineering",
            "Information Technology",
            "Mechanical Engineering",
            "Civil Engineering",
            "Electronics Engineering"
        });

        semesterBox = new JComboBox<>(new String[]{
            "Semester 1", "Semester 2", "Semester 3", "Semester 4",
            "Semester 5", "Semester 6", "Semester 7", "Semester 8"
        });

        male = new JRadioButton("Male");
        female = new JRadioButton("Female");
        other = new JRadioButton("Other");

        genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);
        genderGroup.add(other);

        JPanel genderPanel = new JPanel();
        genderPanel.setBackground(Color.WHITE);
        genderPanel.add(male);
        genderPanel.add(female);
        genderPanel.add(other);

        addField(formPanel, "Student Name:", nameField);
        addField(formPanel, "Roll Number:", rollField);
        addField(formPanel, "PRN Number:", prnField);
        addField(formPanel, "Email ID:", emailField);
        addField(formPanel, "Branch:", branchBox);
        addField(formPanel, "Semester:", semesterBox);
        addField(formPanel, "Gender:", genderPanel);

        // ---------- Buttons ----------
        JButton registerButton = new JButton("Register");
        JButton clearButton = new JButton("Clear");
        JButton deleteButton = new JButton("Delete Selected");

        styleButton(registerButton, new Color(35, 130, 75));
        styleButton(clearButton, new Color(60, 110, 170));
        styleButton(deleteButton, new Color(190, 55, 55));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ---------- Table ----------
        String[] columns = {
            "Name", "Roll No", "PRN", "Email", "Branch", "Semester", "Gender"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registered Students"));
        scrollPane.setPreferredSize(new Dimension(0, 180));

        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        // ---------- Events ----------
        registerButton.addActionListener(e -> registerStudent());
        clearButton.addActionListener(e -> clearFields());
        deleteButton.addActionListener(e -> deleteStudent());

        setVisible(true);
    }

    private void styleButton(JButton button, Color bg) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    private void addField(JPanel panel, String label, Component field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lbl);
        panel.add(field);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void registerStudent() {

        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String prn = prnField.getText().trim();
        String email = emailField.getText().trim();

        String branch = (String) branchBox.getSelectedItem();
        String semester = (String) semesterBox.getSelectedItem();

        String gender = "";
        if (male.isSelected()) gender = "Male";
        else if (female.isSelected()) gender = "Female";
        else if (other.isSelected()) gender = "Other";

        // 1. All fields required
        if (name.isEmpty() || roll.isEmpty() || prn.isEmpty()
                || email.isEmpty() || gender.isEmpty()) {
            showMessage("Please fill all fields!");
            return;
        }

        // 2. Name: letters and spaces only
        if (!name.matches("[a-zA-Z ]+")) {
            showMessage("Enter a valid student name!");
            return;
        }

        // 3. PRN: digits only
        if (!prn.matches("\\d+")) {
            showMessage("PRN must contain digits only!");
            return;
        }

        // 4. Email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showMessage("Enter a valid email address!");
            return;
        }

        // 5. Duplicate roll number / PRN
        for (Student s : students) {
            if (s.getRollNo().equalsIgnoreCase(roll)) {
                showMessage("Roll Number already exists!");
                return;
            }
            if (s.getPrn().equals(prn)) {
                showMessage("PRN Number already exists!");
                return;
            }
        }

        Student student = new Student(name, roll, prn, email, branch, semester, gender);
        students.add(student);
        model.addRow(student.getDetails());

        showMessage("Student Registered Successfully!");
        clearFields();
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        prnField.setText("");
        emailField.setText("");
        branchBox.setSelectedIndex(0);
        semesterBox.setSelectedIndex(0);
        genderGroup.clearSelection();
    }

    private void deleteStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            showMessage("Please select a student to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this student?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            students.remove(selectedRow);
            model.removeRow(selectedRow);
            showMessage("Student Deleted Successfully!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRegistrationGUI::new);
    }
}
