package com.github.lory24.hashcraft.api.scheduler;

/**
 * The scheduler task is an object that will be used by the scheduler to store some infos, such as the id or the task's
 * runnable.
 *
 *
 * @param taskId    The id of the task. -1 for syncronized tasks
 * @param task      The task's runnable
 * @author          LoRy24
 */
public record SchedulerTask(int taskId, Runnable task) {

}
