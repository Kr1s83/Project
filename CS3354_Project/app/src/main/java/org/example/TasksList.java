package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TasksList {
    private ArrayList<Task> tasks;

    public TasksList() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(int i) {
        if(i >= 0 && i < tasks.size())
            tasks.remove(i);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Task> getCompletedTasks() {
        ArrayList<Task> completed = new ArrayList<>();
        for(Task t : tasks) {
            if (t.isComplete())
                completed.add(t);
        }
        return completed;
    }

        public ArrayList<Task> getIncompleteTasks() {
        ArrayList<Task> incomplete = new ArrayList<>();
        for(Task t : tasks) {
            if (!t.isComplete())
                incomplete.add(t);
        }
        return incomplete;
    }

    public void writeTask(String filename) {
        try (FileWriter writer = new FileWriter("src/main/resources/" + filename, false)) {
            for (Task t : tasks) {
                writer.write(t.getTaskName() + "\n");
                writer.write(t.getTaskInfo() + "\n");
                writer.write(t.isComplete() + "\n");
                writer.write("--------\n");

            }
        } catch (IOException e) {
            System.out.println("Error saving task: " + e.getMessage());
        }

    }
}
