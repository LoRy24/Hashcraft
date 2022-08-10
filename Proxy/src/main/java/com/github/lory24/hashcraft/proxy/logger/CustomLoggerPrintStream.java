package com.github.lory24.hashcraft.proxy.logger;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.util.IllegalFormatException;
import java.util.Locale;

/**
 * A custom PrintStream used to capture all the Errors and save them in a new file
 */
public class CustomLoggerPrintStream extends PrintStream {

    /**
     * New line character
     */
    private static final String newLine = System.getProperty("line.separator");

    /**
     * The logs buffer
     */
    @Getter
    private final StringBuffer buffer = new StringBuffer();


    /**
     * The default printstream
     */
    private final PrintStream printStream;

    /**
     * The constructor for the custom logger. This will inject the printStream object.
     * @param printStream Default printStream value
     */
    public CustomLoggerPrintStream(final PrintStream printStream) {
        super(System.out);
        this.printStream = printStream;
    }

    /**
     * Prints a boolean value.  The string produced by {@link
     * String#valueOf(boolean)} is translated into bytes
     * according to the default charset, and these bytes
     * are written in exactly the manner of the
     * {@link #write(int)} method.
     *
     * @param b The {@code boolean} to be printed
     */
    @Override
    public void print(boolean b) {
        this.buffer.append(b); // Append the value to the buffer
        printStream.print(b);
    }

    /**
     * Prints a character.  The character is translated into one or more bytes
     * according to the character encoding given to the constructor, or the
     * default charset if none specified. These bytes
     * are written in exactly the manner of the {@link #write(int)} method.
     *
     * @param c The {@code char} to be printed
     */
    @Override
    public void print(char c) {
        this.buffer.append(c); // Append the value to the buffer
        printStream.print(c);
    }

    /**
     * Prints an integer.  The string produced by {@link
     * String#valueOf(int)} is translated into bytes
     * according to the default charset, and these bytes
     * are written in exactly the manner of the
     * {@link #write(int)} method.
     *
     * @param i The {@code int} to be printed
     * @see Integer#toString(int)
     */
    @Override
    public void print(int i) {
        this.buffer.append(i); // Append the value to the buffer
        printStream.print(i);
    }

    /**
     * Prints a long integer.  The string produced by {@link
     * String#valueOf(long)} is translated into bytes
     * according to the default charset, and these bytes
     * are written in exactly the manner of the
     * {@link #write(int)} method.
     *
     * @param l The {@code long} to be printed
     * @see Long#toString(long)
     */
    @Override
    public void print(long l) {
        this.buffer.append(l); // Append the value to the buffer
        printStream.print(l);
    }

    /**
     * Prints a floating-point number.  The string produced by {@link
     * String#valueOf(float)} is translated into bytes
     * according to the default charset, and these bytes
     * are written in exactly the manner of the
     * {@link #write(int)} method.
     *
     * @param f The {@code float} to be printed
     * @see Float#toString(float)
     */
    @Override
    public void print(float f) {
        this.buffer.append(f); // Append the value to the buffer
        printStream.print(f);
    }

    /**
     * Prints a double-precision floating-point number.  The string produced by
     * {@link String#valueOf(double)} is translated into
     * bytes according to the default charset, and these
     * bytes are written in exactly the manner of the {@link
     * #write(int)} method.
     *
     * @param d The {@code double} to be printed
     * @see Double#toString(double)
     */
    @Override
    public void print(double d) {
        this.buffer.append(d); // Append the value to the buffer
        printStream.print(d);
    }

    /**
     * Prints an array of characters.  The characters are converted into bytes
     * according to the character encoding given to the constructor, or the
     * default charset if none specified. These bytes
     * are written in exactly the manner of the {@link #write(int)} method.
     *
     * @param s The array of chars to be printed
     * @throws NullPointerException If {@code s} is {@code null}
     */
    @Override
    public void print(@NotNull char[] s) {
        this.buffer.append(s); // Append the value to the buffer
        printStream.print(s);
    }

    /**
     * Prints a string.  If the argument is {@code null} then the string
     * {@code "null"} is printed.  Otherwise, the string's characters are
     * converted into bytes according to the character encoding given to the
     * constructor, or the default charset if none
     * specified. These bytes are written in exactly the manner of the
     * {@link #write(int)} method.
     *
     * @param s The {@code String} to be printed
     */
    @Override
    public void print(@Nullable String s) {
        this.buffer.append(s); // Append the value to the buffer
        printStream.print(s);
    }

    /**
     * Prints an object.  The string produced by the {@link
     * String#valueOf(Object)} method is translated into bytes
     * according to the default charset, and these bytes
     * are written in exactly the manner of the
     * {@link #write(int)} method.
     *
     * @param obj The {@code Object} to be printed
     * @see Object#toString()
     */
    @Override
    public void print(@Nullable Object obj) {
        this.buffer.append(obj); // Append the value to the buffer
        printStream.print(obj);
    }

    /**
     * Terminates the current line by writing the line separator string.  The
     * line separator string is defined by the system property
     * {@code line.separator}, and is not necessarily a single newline
     * character ({@code '\n'}).
     */
    @Override
    public void println() {
        this.buffer.append(newLine); // Append the value to the buffer with a newline character
        printStream.println();
    }

    /**
     * Prints a boolean and then terminates the line.  This method behaves as
     * though it invokes {@link #print(boolean)} and then
     * {@link #println()}.
     *
     * @param x The {@code boolean} to be printed
     */
    @Override
    public void println(boolean x) {
        this.buffer.append(x).append(newLine); // Append the value to the buffer with a newline character
        printStream.println(x);
    }

    /**
     * Prints a character and then terminates the line.  This method behaves as
     * though it invokes {@link #print(char)} and then
     * {@link #println()}.
     *
     * @param x The {@code char} to be printed.
     */
    @Override
    public void println(char x) {
        this.buffer.append(x).append(newLine); // Append the value to the buffer with a newline character
        printStream.println(x);
    }

    /**
     * Prints an integer and then terminates the line.  This method behaves as
     * though it invokes {@link #print(int)} and then
     * {@link #println()}.
     *
     * @param x The {@code int} to be printed.
     */
    @Override
    public void println(int x) {
        this.buffer.append(x).append(newLine); // Append the value to the buffer with a newline character
        printStream.println(x);
    }

    /**
     * Prints a long and then terminates the line.  This method behaves as
     * though it invokes {@link #print(long)} and then
     * {@link #println()}.
     *
     * @param x a The {@code long} to be printed.
     */
    @Override
    public void println(long x) {
        this.buffer.append(x).append(newLine); // Append the value to the buffer with a newline character
        printStream.println(x);
    }

    /**
     * Prints a float and then terminates the line.  This method behaves as
     * though it invokes {@link #print(float)} and then
     * {@link #println()}.
     *
     * @param x The {@code float} to be printed.
     */
    @Override
    public void println(float x) {
        this.buffer.append(x).append(newLine); // Append the value to the buffer with a newline character
        printStream.println(x);
    }

    /**
     * Prints a double and then terminates the line.  This method behaves as
     * though it invokes {@link #print(double)} and then
     * {@link #println()}.
     *
     * @param x The {@code double} to be printed.
     */
    @Override
    public void println(double x) {
        this.buffer.append(x).append(newLine); // Append the value to the buffer with a newline character
        printStream.println(x);
    }

    /**
     * Prints an array of characters and then terminates the line.  This method
     * behaves as though it invokes {@link #print(char[])} and
     * then {@link #println()}.
     *
     * @param x an array of chars to print.
     */
    @Override
    public void println(@NotNull char[] x) {
        this.buffer.append(x).append(newLine); // Append the value to the buffer with a newline character
        printStream.println(x);
    }

    /**
     * Prints a String and then terminates the line.  This method behaves as
     * though it invokes {@link #print(String)} and then
     * {@link #println()}.
     *
     * @param x The {@code String} to be printed.
     */
    @Override
    public void println(@Nullable String x) {
        this.buffer.append(x).append(newLine); // Append the value to the buffer with a newline character
        printStream.println(x);
    }

    /**
     * Prints an Object and then terminates the line.  This method calls
     * at first String.valueOf(x) to get the printed object's string value,
     * then behaves as
     * though it invokes {@link #print(String)} and then
     * {@link #println()}.
     *
     * @param x The {@code Object} to be printed.
     */
    @Override
    public void println(@Nullable Object x) {
        this.buffer.append(x).append(newLine); // Append the value to the buffer with a newline character
        printStream.println(x);
    }

    /**
     * A convenience method to write a formatted string to this output stream
     * using the specified format string and arguments.
     *
     * <p> An invocation of this method of the form
     * {@code out.printf(format, args)} behaves
     * in exactly the same way as the invocation
     *
     * <pre>{@code
     *     out.format(format, args)
     * }</pre>
     *
     * @param format A format string as described in <a
     *               href="../util/Formatter.html#syntax">Format string syntax</a>
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.  If there are more arguments than format specifiers, the
     *               extra arguments are ignored.  The number of arguments is
     *               variable and may be zero.  The maximum number of arguments is
     *               limited by the maximum dimension of a Java array as defined by
     *               <cite>The Java Virtual Machine Specification</cite>.
     *               The behaviour on a
     *               {@code null} argument depends on the <a
     *               href="../util/Formatter.html#syntax">conversion</a>.
     * @return This output stream
     * @throws IllegalFormatException If a format string contains an illegal syntax, a format
     *                                          specifier that is incompatible with the given arguments,
     *                                          insufficient arguments given the format string, or other
     *                                          illegal conditions.  For specification of all possible
     *                                          formatting errors, see the <a
     *                                          href="../util/Formatter.html#detail">Details</a> section of the
     *                                          formatter class specification.
     * @throws NullPointerException             If the {@code format} is {@code null}
     * @since 1.5
     */
    @Override
    public PrintStream printf(@NotNull String format, Object... args) {
        this.buffer.append(String.format(format, args)); // Append the value to the buffer with format
        return printStream.printf(format, args);
    }

    /**
     * A convenience method to write a formatted string to this output stream
     * using the specified format string and arguments.
     *
     * <p> An invocation of this method of the form
     * {@code out.printf(l, format, args)} behaves
     * in exactly the same way as the invocation
     *
     * <pre>{@code
     *     out.format(l, format, args)
     * }</pre>
     *
     * @param l      The {@linkplain Locale locale} to apply during
     *               formatting.  If {@code l} is {@code null} then no localization
     *               is applied.
     * @param format A format string as described in <a
     *               href="../util/Formatter.html#syntax">Format string syntax</a>
     * @param args   Arguments referenced by the format specifiers in the format
     *               string.  If there are more arguments than format specifiers, the
     *               extra arguments are ignored.  The number of arguments is
     *               variable and may be zero.  The maximum number of arguments is
     *               limited by the maximum dimension of a Java array as defined by
     *               <cite>The Java Virtual Machine Specification</cite>.
     *               The behaviour on a
     *               {@code null} argument depends on the <a
     *               href="../util/Formatter.html#syntax">conversion</a>.
     * @return This output stream
     * @throws IllegalFormatException If a format string contains an illegal syntax, a format
     *                                          specifier that is incompatible with the given arguments,
     *                                          insufficient arguments given the format string, or other
     *                                          illegal conditions.  For specification of all possible
     *                                          formatting errors, see the <a
     *                                          href="../util/Formatter.html#detail">Details</a> section of the
     *                                          formatter class specification.
     * @throws NullPointerException             If the {@code format} is {@code null}
     * @since 1.5
     */
    @Override
    public PrintStream printf(Locale l, @NotNull String format, Object... args) {
        this.buffer.append(String.format(l, format, args)); // Append the value to the buffer with format
        return printStream.printf(l, format, args);
    }
}
