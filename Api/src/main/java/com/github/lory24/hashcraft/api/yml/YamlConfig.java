package com.github.lory24.hashcraft.api.yml;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;

/**
 * Some YAML files are used to create configuration for applications, and with this class you can read data from them. The
 * only limitation is that you canot save comments when saving the data in a file (for now).
 *
 * @author LoRy24
 */
@SuppressWarnings("unused")
public class YamlConfig {

    /**
     * The HashMap that contains all the data of the .yml file. Loaded using the {@code loadConfig()} function.
     */
    private HashMap<String, Object> ymlData = new HashMap<>();

    /**
     * This function will load the content of an .yml file into the ymlData internal hashmap.
     *
     * @param file The file where the YAML data is located
     * @return This YamlConfig object
     * @throws FileNotFoundException If there is an error while creating the FileInputStream. This can occour when the
     *         file doesn't exist
     */
    public YamlConfig loadConfig(final File file) throws FileNotFoundException {
        this.ymlData = new Yaml().load(new FileInputStream(file));
        // Return the current object
        return this;
    }

    /**
     * This function will load the yaml data contained into a string.
     *
     * @param yaml The YAML string that contains the data that should be loaded
     * @return This YamlConfig object
     */
    @SuppressWarnings("UnusedReturnValue")
    public YamlConfig loadConfig(final String yaml) {
        this.ymlData = new Yaml().load(yaml); // Load the YAML data from a string
        // Return the current object
        return this;
    }

    /**
     * This function will return an object obtained from the config. If you want to get an object that is located inside
     * the "settings" section, you should use the path "settings.object". In fact, the last string is the object name, and
     * all the other are the sections.
     *
     * @param path The path where the object is located
     * @return The object obtained from the config or null if it hasn't been found
     */
    @SuppressWarnings("unchecked")
    public Object get(@NotNull final String path) {
        // Split all the path in multiple sections object
        final String[] pathKeys = path.split("\\.");
        HashMap<String, Object> currentHashMap = this.ymlData; // Instance with starter yml data
        // Join in each section and replace the hashmap with the new section data. Repeat this until it reaches the last section.
        for (int i = 0; i < pathKeys.length; i++) if (i != pathKeys.length -1) currentHashMap = (HashMap<String, Object>) currentHashMap.get(pathKeys[i]);
        return currentHashMap.get(pathKeys[pathKeys.length -1]); // Return the obj
    }

    /**
     * Set a value in the config. This function is unsafe and should be used only from the {@code set()} function defined
     * in this class. It works using a recursion-based system. The system is very hard to exaplin so try to figure out
     * yourself how it works.
     *
     * @param path The current path. Used to get to the final object
     * @param valueToApply The value that should be applied to the final object
     * @param copy The HashMap copy. Used in the recursive-system
     * @return The updates ymlData hashmap. This value should replace the ymlData hashmap contained in this class object.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    @Contract("_, _, _ -> param3")
    private Object internalRecursiveSet(@NotNull final String path, final Object valueToApply, final HashMap<String, Object> copy) {
        // If it's the last iteration, replace the value and return the updated copy
        if (path.split("\\.").length == 1) {
            copy.replace(path, valueToApply);
            return copy;
        }

        // Take the current point in the path
        String currentPoint = path.split("\\.")[0];
        // Replace the object in the hasmap with the new hashmap or the object returned by this function. Recursion
        copy.replace(currentPoint, this.internalRecursiveSet(path.substring(currentPoint.length() +1), valueToApply,
                (HashMap<String, Object>) copy.get(currentPoint)));
        return copy; // Return the updated obj
    }

    /**
     * With this function you will be able to update a value from the loaded YML data. It will use an internal function
     * that will use a recursion-based system.
     *
     * @param path The path where the item that should be replaced is located
     * @param value The value that will be applied
     */
    public void set(final String path, final Object value) {
        this.internalRecursiveSet(path, value, this.ymlData); // Use the internal recursive set function
    }

    /**
     * This function will save all the YML data in a file that already exists. REMEMBER THAT WHEN YOU SAVE THE DATA ON A FILE
     * YOU WILL DELETE ALL THE COMMENTS STORED IN IT.
     *
     * @param file The file where to save all the data
     * @throws IOException If there is an error while opening the FileWriter used to save the data. This can occours if
     *         the file doesn't exist
     */
    public void saveConfig(final File file) throws IOException {
        new Yaml().dump(this.ymlData, new FileWriter(file)); // Dump using snakeYaml
    }
}
