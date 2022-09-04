package com.github.lory24.hashcraft.proxy.features;

import com.github.lory24.hashcraft.proxy.Hashcraft;
import com.github.lory24.hashcraft.proxy.HashcraftScheduler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Hey you! Stop spamming. This class will be used to make connections be in timeout for TOT time after they have
 * connected to the server. This will prevent connections from spamming requests.
 *
 * @author LoRy24
 */
@RequiredArgsConstructor
public class ConnectionThrottleSystem {

    /**
     * The addresses that are currently in throttle
     */
    @Getter
    private final List<InetAddress> throttledAddresses = new ArrayList<>();

    /**
     * The proxy reference
     */
    @Getter
    private final Hashcraft hashcraft;


    /**
     * This function will process the connection throttle of an address. If the address wasn't already in cooldown,
     * it will put it in it, and it will return fase, else it will just return true.
     *
     * @param address The address to check
     * @return True if the ip is throttled, else return false
     */
    public boolean makeThrottleConnection(InetAddress address) {
        // If the connection is in throttle, return true
        if (this.throttledAddresses.contains(address)) return true;

        // Else, put it in throttle
        this.throttledAddresses.add(address);

        // Register the task
        ((HashcraftScheduler) this.hashcraft.getScheduler()).runInternalTaskAsyncrously(() -> {
            try {
                // Sleep for 2 seconds and remove the address from the list
                Thread.sleep(2000);
                this.throttledAddresses.remove(address);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Return false: The address wasn't in throttle
        return false;
    }
}
