package org.example;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class TaskApp extends JFrame {
    private CardLayout mainMenu;
    private JPanel mainPanel;
    private TasksList tasksList = new TasksList();

    public TaskApp() {
        setTitle("ToDo List");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainMenu = new CardLayout();
        mainPanel = new JPanel(mainMenu);

        // Intro Page
        JPanel mainPage = new JPanel();
        mainPage.add(new JLabel("Home Page"));
        mainPage.add(new JTextArea("Use the 'options' in the top left to navigate through the app.\n\n" +
            "Manage Tasks: Add tasks, Delete tasks, Mark tasks as done and Update existing tasks\n\n" +
            "Completed Tasks: Displays completed tasks only."
        ));

        // Display Completed Tasks
        JPanel completedTasksPanel = new JPanel();
        completedTasksPanel.add(new JLabel("Completed Tasks."));

        // Manage Tasks
        JPanel manageTasksPanel = new JPanel(new BorderLayout());
        DefaultListModel<Task> taskListModel = new DefaultListModel<>();

        // Display tasks
        JList<Task> taskJList = new JList<>(taskListModel);
        taskJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        manageTasksPanel.add(new JScrollPane(taskJList), BorderLayout.WEST);
        // Display completed tasks
        DefaultListModel<Task> completedTaskListModel = new DefaultListModel<>();
        JList<Task> completedTasksJList = new JList<>(completedTaskListModel);
        completedTasksPanel.add(new JScrollPane(completedTasksJList), BorderLayout.CENTER);

        Runnable refreshList = () -> {      // to refresh list
            taskListModel.clear();
            for (Task t : tasksList.getTasks()) {
                taskListModel.addElement(t);
            }
        };
        refreshList.run();

        // Edit and add tasks
        JPanel editPanel = new JPanel(new GridLayout(6, 1));
        JLabel nameLabel = new JLabel("Task Name: ");
        JTextField nameField = new JTextField();
        JLabel infoLabel = new JLabel("Enter Task Description: ");
        JTextArea infoArea = new JTextArea(4,15);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        JScrollPane infoScrollPane = new JScrollPane(infoArea);
        infoScrollPane.setPreferredSize(new Dimension(250,100));
        JButton saveButton = new JButton("Save Changes");
        JButton markDoneButton = new JButton("Mark Completed");
        JButton delButton = new JButton("Delete Task");
        editPanel.add(nameLabel);
        editPanel.add(nameField);
        editPanel.add(infoLabel);
        editPanel.add(infoScrollPane);
        editPanel.add(saveButton);
        editPanel.add(markDoneButton);
        editPanel.add(delButton);
        manageTasksPanel.add(editPanel, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Task");
        manageTasksPanel.add(addButton, BorderLayout.SOUTH);
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String info = infoArea.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task must have a name!");
                return;
            }

            tasksList.addTask(new Task(name, info));
            refreshList.run();
            
            // Write to file
            tasksList.writeTask("tasks.txt");

            JOptionPane.showMessageDialog(this, "Task Added.");

            // Clear fields
            nameField.setText("");
            infoArea.setText("");
        });

        // When a task is selected
        taskJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Task selected = taskJList.getSelectedValue();
                if (selected != null) {
                    nameField.setText(selected.getTaskName());
                    infoArea.setText(selected.getTaskInfo());
                }
            }
        });

        // To save changes
        saveButton.addActionListener(e -> {
            Task selected = taskJList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a task first!");
                return;
            }
            selected.setTaskName(nameField.getText().trim());
            selected.setTaskInfo(infoArea.getText().trim());

            refreshList.run();

            tasksList.writeTask("tasks.txt");
            JOptionPane.showMessageDialog(this, "Task Updated");
        });

        // Mark a task as complete
        markDoneButton.addActionListener(e -> {
            Task selected = taskJList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a task first");
                return;
            }
            selected.setComplete(true);

            refreshList.run();
            tasksList.writeTask("tasks.txt");
            JOptionPane.showMessageDialog(this, "Task marked as complete");
        });

        // Delete a task
        delButton.addActionListener(e -> {
            Task selected = taskJList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a task first");
                return;
            }
            int op = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to delete the task?", 
                "Confirm", 
                JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                tasksList.getTasks().remove(selected);
            }
            refreshList.run();
            tasksList.writeTask("tasks.txt");

            JOptionPane.showMessageDialog(this, "Task was deleted");

        });
        


       /* manageTasksPanel.setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(4,1));
        JLabel nameLabel = new JLabel("Task Name: ");
        JTextField nameField = new JTextField();
        JLabel infoLabel = new JLabel("Enter Task Description: ");
        JTextArea infoArea = new JTextArea(5,20);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(infoLabel);
        inputPanel.add(new JScrollPane(infoArea));
        manageTasksPanel.add(inputPanel, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Task");
        manageTasksPanel.add(addButton, BorderLayout.SOUTH);
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String info = infoArea.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task must have a name!");
                return;
            }

            tasksList.addTask(new Task(name, info));
            
            // Write to file
            tasksList.writeTask("tasks.txt");

            JOptionPane.showMessageDialog(this, "Task Added.");

            // Clear fields
            nameField.setText("");
            infoArea.setText("");
        }); */

        // Add to main panel
        mainPanel.add(mainPage, "Home Page");
        mainPanel.add(completedTasksPanel, "Completed Tasks");
        mainPanel.add(manageTasksPanel, "Manage Tasks");

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem tasks = new JMenuItem("Home Page");
        JMenuItem completedTasks = new JMenuItem("Completed Tasks");
        JMenuItem manageTasks = new JMenuItem("Manage Tasks");

        menu.add(tasks);
        menu.add(completedTasks);
        menu.add(manageTasks);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        tasks.addActionListener(e -> mainMenu.show(mainPanel, "Home Page"));
        completedTasks.addActionListener(e -> {
            mainMenu.show(mainPanel, "Completed Tasks");
            completedTaskListModel.clear();
            for (Task t : tasksList.getCompletedTasks()) {
                completedTaskListModel.addElement(t);
            }
    });
        manageTasks.addActionListener(e -> mainMenu.show(mainPanel, "Manage Tasks"));

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskApp().setVisible(true));
    }
}


