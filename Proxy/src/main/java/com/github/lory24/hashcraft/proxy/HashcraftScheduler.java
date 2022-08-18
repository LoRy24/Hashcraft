package com.github.lory24.hashcraft.proxy;

import com.github.lory24.hashcraft.api.ProxyPlugin;
import com.github.lory24.hashcraft.api.scheduler.Scheduler;
import com.github.lory24.hashcraft.api.scheduler.SchedulerTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashcraftScheduler extends Scheduler {

    /**
     * Scheduler owner update operation enum. Used by the {@link HashcraftScheduler#updateOwnedTasks(ProxyPlugin, int,
     * OwnedTasksUpdateOperation)}
     */
    private enum OwnedTasksUpdateOperation {
        ADD, REMOVE
    }

    /**
     * This hashmap will store all the scheduled tasks
     */
    private final HashMap<Integer, Thread> tasksThreads = new HashMap<>();

    /**
     * This hashmap will store all the plugins and their owned tasks ids
     */
    private final HashMap<ProxyPlugin, List<Integer>> pluginsOwnedTasksIds = new HashMap<>();

    /**
     * Internal tasks register
     */
    private final HashMap<Integer, Thread> internalTasksThreads = new HashMap<>();

    /**
     * Generate a new id for a new task
     */
    @Contract(pure = true)
    private int generateId(@NotNull HashMap<Integer, Thread> register) {
        // Generate the id and return it
        int id = 0;
        while (register.containsKey(id)) id++;
        return id;
    }

    /**
     * Update the owned tasks of a plugin
     */
    @Contract(pure = true)
    private void updateOwnedTasks(final ProxyPlugin owner, final int taskId, @NotNull OwnedTasksUpdateOperation operation) {
        // Switch by operation
        switch (operation) {

            case ADD -> { // Add operation

                // If the hashmap doesn't contain the plugin, initialize it and return
                if (!this.pluginsOwnedTasksIds.containsKey(owner)) {
                    this.pluginsOwnedTasksIds.put(owner, new ArrayList<>() {{ add(taskId); }});
                    break;
                }

                // Update the list
                List<Integer> newTasksIds = this.pluginsOwnedTasksIds.get(owner);
                newTasksIds.add(taskId); // Add the id
                this.pluginsOwnedTasksIds.replace(owner, newTasksIds);
            }

            case REMOVE -> {

                if (!this.pluginsOwnedTasksIds.containsKey(owner)) break; // If the hashmap doesn't contain the owner anymore, break

                // Resolve the index of the id
                List<Integer> newTasksIds = this.pluginsOwnedTasksIds.get(owner);

                // Obtain the value
                int taskIdIndex = 0; while (newTasksIds.get(taskIdIndex) != taskId) taskIdIndex++;

                // Update the list
                newTasksIds.remove(taskIdIndex); // Add the id
                this.pluginsOwnedTasksIds.replace(owner, newTasksIds);
            }
        }
    }

    /**
     * Get the owner of a task that has the id passed in the params
     */
    @Nullable
    private ProxyPlugin getTasksOwner(final int taskId) {
        // Iterate though every plugin
        for (Map.Entry<ProxyPlugin, List<Integer>> ownersEntry: this.pluginsOwnedTasksIds.entrySet())
            if (ownersEntry.getValue().contains(taskId)) return ownersEntry.getKey(); // Return the found owner
        return null; // If the task has no owner
    }

    /**
     * Run a task asyncrously (on a dedicated thread)
     *
     * @param owner The owner of the task
     * @param task  The task that should be runned on the new thread
     * @return The task obj. Contains some infos about the task
     */
    @Override
    public SchedulerTask runTaskAsyncrously(ProxyPlugin owner, Runnable task) {
        // Generate a new task id
        int taskId = this.generateId(this.tasksThreads);

        // Create the task's thread
        Thread taskThread = new Thread(() -> {
            // Run the task
            task.run();

            // Remove the task from everything
            this.tasksThreads.remove(taskId);
            this.updateOwnedTasks(owner, taskId, OwnedTasksUpdateOperation.REMOVE);
        });
        taskThread.setName("HashcraftTask-" + taskId); // Update the thread name

        // Register the task
        this.tasksThreads.put(taskId, taskThread); // Register the thread
        this.updateOwnedTasks(owner, taskId, OwnedTasksUpdateOperation.ADD); // Add the id of the task

        // Start the thread
        taskThread.start();

        // Return a new SchedulerTask obj
        return new SchedulerTask(taskId, task);
    }

    /**
     * Schedule a task that should be runned asyncrously (on a dedicated thread)
     *
     * @param owner The owner of the task
     * @param task  The task that should be scheduled on a new thread
     * @param delay The time that should pass before the task get executed
     * @return The task obj. Contains some infos about the task
     */
    @Override
    public SchedulerTask scheduleTaskAsyncrously(ProxyPlugin owner, Runnable task, long delay) {
        // Use the runTaskAsyncrously to schedule the task
        return this.runTaskAsyncrously(owner, () -> {
            try {
                // Wait and run the task
                Thread.sleep(delay);
                task.run();
            } catch (InterruptedException ignored) {}
        });
    }

    /**
     * Run a repeating task asyncrously (On a new thread)
     *
     * @param owner  The owner of the task
     * @param task   The task that should be scheduled on a new thread
     * @param delay  The time that should travel before the task get executed. Expressed in milliseconds
     * @param millis The time that should travel before re-executing the task. Expressed in milliseconds
     * @return The task obj. Contains some infos about the task
     */
    @SuppressWarnings("BusyWait")
    @Override
    public SchedulerTask scheduleTaskTimerAsyncrously(ProxyPlugin owner, Runnable task, long delay, long millis) {
        // Use the runTaskAsyncrously to schedule the timer task
        return this.runTaskAsyncrously(owner, () -> {
            try {
                Thread.sleep(delay); // Delay before starting the loop
                while (!Thread.interrupted()) {
                    // Run the task and wait
                    task.run();
                    Thread.sleep(millis);
                }
            } catch (InterruptedException ignored) {}
        });
    }

    /**
     * This function will cancel an async task. If the task doesn't exist anymore, it won't send back any error.
     *
     * @param taskId The id of the task that should be cancelled
     */
    @Override
    public void cancelTask(int taskId) {
        this.tasksThreads.get(taskId).interrupt(); // Interrupt the task thread

        // Unregister the task
        this.tasksThreads.remove(taskId);
        ProxyPlugin taskOwner = this.getTasksOwner(taskId);
        if (taskOwner != null) this.updateOwnedTasks(taskOwner, taskId, OwnedTasksUpdateOperation.REMOVE);
    }

    /**
     * With this function the proxy will run asyncrously tasks without defining an owner. The task will be saved in the
     * internal tasks registers.
     *
     * @param task The task that should be run
     * @return a new scheduler task obj
     */
    public SchedulerTask runInternalTaskAsyncrously(Runnable task) {
        // Generate a new task id
        int taskId = this.generateId(this.internalTasksThreads);

        // Create the task's thread
        Thread taskThread = new Thread(() -> {
            // Run the task
            task.run();

            // Remove the task from everything
            this.internalTasksThreads.remove(taskId);
        });
        taskThread.setName("HashcraftInternalTask-" + taskId); // Update the thread name

        // Register the task
        this.internalTasksThreads.put(taskId, taskThread); // Register the thread

        // Start the thread
        taskThread.start();

        // Return a new SchedulerTask obj
        return new SchedulerTask(taskId, task);
    }

    /**
     * Cancel an internal task.
     */
    public void cancelInternalTask(int taskId) {
        this.internalTasksThreads.get(taskId).interrupt(); // Interrupt the task thread

        // Unregister the task from the internal tasks register
        this.internalTasksThreads.remove(taskId);
    }
}
