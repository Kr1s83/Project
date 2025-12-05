package org.example;

public class Task {
    private String taskName;
    private String taskInfo;
    private boolean complete;

    public Task(String taskName, String taskInfo) {
        this.taskName = taskName;
        this.taskInfo = taskInfo;
        this.complete = false;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return (complete ? "[√] " : "[ ] ") + taskName;
    }
}