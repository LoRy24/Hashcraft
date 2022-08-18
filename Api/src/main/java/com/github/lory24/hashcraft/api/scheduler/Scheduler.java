package com.github.lory24.hashcraft.api.scheduler;

import com.github.lory24.hashcraft.api.plugin.ProxyPlugin;

/**
 * The scheduler is an util that will be used to run actions syncrously or asyncrously from the server. It can be also
 * very functional when you have to run scheduled tasks or tasks that should be repeated until stopped.
 *
 * @author LoRy24
 * @see SchedulerTask
 */
public abstract class Scheduler {

    /**
     * Run a task asyncrously (on a dedicated thread)
     *
     * @param owner The owner of the task
     * @param task The task that should be runned on the new thread
     * @return The task obj. Contains some infos about the task
     */
    public abstract SchedulerTask runTaskAsyncrously(final ProxyPlugin owner, final Runnable task);

    /**
     * Schedule a task that should be runned asyncrously (on a dedicated thread)
     *
     * @param owner The owner of the task
     * @param task The task that should be scheduled on a new thread
     * @param delay The time that should pass before the task get executed
     * @return The task obj. Contains some infos about the task
     */
    public abstract SchedulerTask scheduleTaskAsyncrously(final ProxyPlugin owner, final Runnable task, final long delay);

    /**
     * Run a repeating task asyncrously (On a new thread)
     *
     * @param owner The owner of the task
     * @param task The task that should be scheduled on a new thread
     * @param delay The time that should travel before the task get executed. Expressed in milliseconds
     * @param millis The time that should travel before re-executing the task. Expressed in milliseconds
     * @return The task obj. Contains some infos about the task
     */
    public abstract SchedulerTask scheduleTaskTimerAsyncrously(final ProxyPlugin owner, final Runnable task, final long delay, final long millis);

    /**
     * This function will cancel an async task. If the task doesn't exist anymore, it won't send back any error.
     *
     * @param taskId The id of the task that should be cancelled
     */
    public abstract void cancelTask(final int taskId);
}
