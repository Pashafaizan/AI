import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * IntelligentTutoringSystem.java
 *
 * A GUI application combining:
 * 1. Area calculation for learning purposes.
 * 2. An RDF Ontology query execution module.
 */
public class IntelligentTutoringSystem {

    static final String ONTOLOGY_FILE = "C:\\Users\\user\\Documents\\oo\\owl.owl";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ITS().createAndShowGUI());
    }
}

class ITS {

    private JFrame frame;

    public void createAndShowGUI() {
        frame = new JFrame("Intelligent Tutoring System - Math");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Area Calculation Learning System");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Center Panel with Buttons
        JPanel centerPanel = new JPanel();
        JButton calculateAreaButton = new JButton("Learn Area of Shapes");
        JButton queryOntologyButton = new JButton("Query Ontology");

        centerPanel.add(calculateAreaButton);
        centerPanel.add(queryOntologyButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        calculateAreaButton.addActionListener(e -> new AreaCalculation().showCalculator());
        queryOntologyButton.addActionListener(e -> executeOntologyQuery());

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Executes the RDF ontology query using Jena and displays results in a dialog.
     */
    public void executeOntologyQuery() {
        try {
            Model model = ModelFactory.createDefaultModel();
            File ontologyFile = new File(IntelligentTutoringSystem.ONTOLOGY_FILE);

            // Ensure the ontology file exists
            if (!ontologyFile.exists()) {
                JOptionPane.showMessageDialog(null,
                    "Ontology file not found at: " + IntelligentTutoringSystem.ONTOLOGY_FILE,
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            model.read(ontologyFile.toURI().toString());

            String queryString =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "SELECT ?subject ?object " +
                "WHERE { ?subject rdfs:subClassOf ?object }";

            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);

            StringBuilder resultString = new StringBuilder("<html>Ontology Query Results:<br>");
            try (QueryExecution execution = qexec) {
                ResultSet results = execution.execSelect();
                if (!results.hasNext()) {
                    resultString.append("No results found.");
                }

                while (results.hasNext()) {
                    QuerySolution qs = results.next();
                    resultString.append("Subject: ")
                        .append(qs.get("subject").toString())
                        .append(", Object: ")
                        .append(qs.get("object").toString())
                        .append("<br>");
                }
            }

            JOptionPane.showMessageDialog(null, resultString.toString(), 
                "Ontology Query Results", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error in executing ontology query: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class AreaCalculation {

    public void showCalculator() {
        JFrame calcFrame = new JFrame("Area Calculation");
        calcFrame.setSize(400, 400);
        calcFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel calcPanel = new JPanel();
        calcPanel.setLayout(new GridLayout(6, 2));

        JTextField baseField = new JTextField();
        JTextField heightField = new JTextField();
        JTextField sideField = new JTextField();

        JButton calculateTriangleAreaButton = new JButton("Calculate Triangle Area");
        JButton calculateSquareAreaButton = new JButton("Calculate Square Area");

        JLabel resultLabel = new JLabel("Result will appear here");

        calcPanel.add(new JLabel("Base (Triangle Area):"));
        calcPanel.add(baseField);

        calcPanel.add(new JLabel("Height (Triangle Area):"));
        calcPanel.add(heightField);

        calcPanel.add(new JLabel("Side (Square Area):"));
        calcPanel.add(sideField);

        calcPanel.add(calculateTriangleAreaButton);
        calcPanel.add(calculateSquareAreaButton);
        calcPanel.add(resultLabel);

        // Handle Triangle Area Button
        calculateTriangleAreaButton.addActionListener(e -> {
            try {
                double base = Double.parseDouble(baseField.getText());
                double height = Double.parseDouble(heightField.getText());
                double area = 0.5 * base * height;
                resultLabel.setText("<html>Triangle Area: " + area + "</html>");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, 
                    "Please enter valid numbers for Triangle Area Calculation");
            }
        });

        // Handle Square Area Button
        calculateSquareAreaButton.addActionListener(e -> {
            try {
                double side = Double.parseDouble(sideField.getText());
                double area = side * side;
                resultLabel.setText("<html>Square Area: " + area + "</html>");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, 
                    "Please enter valid numbers for Square Area Calculation");
            }
        });

        calcFrame.add(calcPanel);
        calcFrame.setVisible(true);
    }
}
