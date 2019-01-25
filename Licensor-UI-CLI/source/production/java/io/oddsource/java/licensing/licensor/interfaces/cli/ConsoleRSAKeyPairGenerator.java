/*
 * Copyright © 2010-2019 OddSource Code (license@oddsource.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.oddsource.java.licensing.licensor.interfaces.cli;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import io.oddsource.java.licensing.LicensingCharsets;
import io.oddsource.java.licensing.encryption.PasswordProvider;
import io.oddsource.java.licensing.encryption.PrivateKeyDataProvider;
import io.oddsource.java.licensing.encryption.PublicKeyDataProvider;
import io.oddsource.java.licensing.encryption.RSAKeyPairGenerator;
import io.oddsource.java.licensing.encryption.RSAKeyPairGeneratorInterface;
import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;
import io.oddsource.java.licensing.exception.InappropriateKeyException;
import io.oddsource.java.licensing.exception.InappropriateKeySpecificationException;
import io.oddsource.java.licensing.exception.RSA2048NotSupportedException;
import io.oddsource.java.licensing.licensor.interfaces.cli.spi.TextInterfaceDevice;

/**
 * A command-line tool for generating a public/private key pair. Usage is as follows.<br />
 * <br />
 * To view usage and help:<br />
 * {@code java io.oddsource.java.licensing.text ConsoleRSAKeyPairGenerator -help}<br />
 * <br />
 * To use interactive mode:<br />
 * {@code java io.oddsource.java.licensing.text ConsoleRSAKeyPairGenerator -interactive}<br />
 * <br />
 * To specify all options at the command line:<br />
 * <code>java io.oddsource.java.licensing.text ConsoleRSAKeyPairGenerator -password &lt;password&gt; -private
 * &lt;file|class name&gt; -public &lt;file|class name&gt; [-privatePassword &lt;password&gt;] [-classes -passwordClass
 * &lt;class name&gt; -privatePasswordClass &lt;class name&gt; [-privatePackage &lt;package&gt;] [-publicPackage
 * &lt;package&gt;] [-passwordPackage &lt;package&gt;] [-privatePasswordPackage &lt;package&gt;]]</code><br />
 * <br />
 * Be sure to review the documentation for {@link PasswordProvider},
 * {@link PrivateKeyDataProvider} and {@link PublicKeyDataProvider} for
 * further instructions.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("Duplicates")
public class ConsoleRSAKeyPairGenerator
{
    private static final int CLI_WIDTH = 105;

    private static final String LF = System.getProperty("line.separator");

    private static final String USAGE = " ConsoleRSAKeyPairGenerator -help" + LF +
                                        " ConsoleRSAKeyPairGenerator -interactive" + LF +
                                        " ConsoleRSAKeyPairGenerator -password <password> -private <file|class name> " +
                                        "-public <file|class name> [-privatePassword <password>] [-classes " +
                                        "-passwordClass <class name> -privatePasswordClass <class name> " +
                                        "[-privatePackage <package>] [-publicPackage <package>] " +
                                        "[-passwordPackage <package>] [-privatePasswordPackage <package>]]";

    private static final Option HELP = Option.builder("help").desc("Display this help message").hasArg(false).build();

    private static final Option INTERACTIVE = Option.builder("interactive").
        desc("Specify to use interactive mode and ignore command-line options").
        required(false).hasArg(false).build();

    private static final Option GENERATE_CLASSES = Option.builder("classes").
        desc("Specify to generate compilable Java classes instead of key files").
        required(false).hasArg(false).build();

    private static final Option PRIVATE_FILE = Option.builder("private").argName("file|class name").
        desc("The name of the private key file or class to generate (required unless in interactive mode)").
        required(true).hasArg(true).build();

    private static final Option PRIVATE_PACKAGE = Option.builder("privatePackage").argName("package").
        desc("The name of the package to use for the private key class (optional, ignored unless " +
             "generating classes)").
        required(false).hasArg(true).build();

    private static final Option PUBLIC_FILE = Option.builder("public").argName("file|class name").
        desc("The name of the public key file or class to generate (required unless in interactive mode)").
        required(true).hasArg(true).build();

    private static final Option PUBLIC_PACKAGE = Option.builder("publicPackage").argName("package").
        desc("The name of the package to use for the public key class (optional, ignored unless " +
             "generating classes)").
        required(false).hasArg(true).build();

    private static final Option PASSWORD = Option.builder("password").argName("password").
        desc("The password to use to encrypt the public and private keys (required unless in interactive mode)").
        required(true).hasArg(true).build();

    private static final Option PASSWORD_CLASS = Option.builder("passwordClass").argName("class name").
        desc("The name of the password storage class to generate (optional, ignored unless " +
             "generating classes)").
        required(false).hasArg(true).build();

    private static final Option PASSWORD_PACKAGE = Option.builder("passwordPackage").argName("package").
        desc("The name of the package to use for the password storage class (optional, ignored " +
             "unless generating classes)").
        required(false).hasArg(true).build();

    private static final Option PRIVATE_PASSWORD = Option.builder("privatePassword").argName("password").
        desc("A different password to use to encrypt the private key (optional)").
        required(false).hasArg(true).build();

    private static final Option PRIVATE_PASSWORD_CLASS = Option.builder("privatePasswordClass").argName("class name").
        desc("The name of the private key password storage class to generate (optional, ignored " +
             "unless generating classes)").
        required(false).hasArg(true).build();

    private static final Option PRIVATE_PASSWORD_PACKAGE = Option.builder("privatePasswordPackage").argName("package").
        desc("The name of the package to use for the private key password storage class (optional, " +
             "ignored unless generating classes)").
        required(false).hasArg(true).build();

    private static final short ERROR_CODE_FILE_OVERWRITE = 81;

    private static final short ERROR_CODE_RSA_NOT_SUPPORTED = 51;

    private static final short ERROR_CODE_2048_BIT_NOT_SUPPORTED = 52;

    private static final short ERROR_CODE_KEY_ALGORITHM_NOT_SUPPORTED = 41;

    private static final short ERROR_CODE_INVALID_KEY = 42;

    private static final short ERROR_CODE_INVALID_KEY_SPEC = 43;

    private static final short ERROR_CODE_INTERRUPTED = 44;

    private static final short ERROR_CODE_IO = 21;

    private static final short ERROR_CODE_UNKNOWN = -1;

    private static final short PERIODS_DELAY_MILLISECONDS = 25;

    private static final short THREAD_DELAY_MILLISECONDS = 50;

    private static final short MIN_PASSWORD_LENGTH = 6;

    private static final short MAX_PASSWORD_LENGTH = 32;

    private static final short HELP_DISPLAY_LEFT_PAD = 1;

    private static final short HELP_DISPLAY_DESC_PAD = 3;

    private final RSAKeyPairGeneratorInterface generator;

    private final TextInterfaceDevice device;

    private final CommandLineParser cliParser;

    private CommandLine cli;

    private boolean interactive;

    /**
     * Constructor.
     *
     * @param generator The underlying key pair generator
     * @param textInterfaceDevice The text interface device
     * @param cliParser The CLI parser
     */
    protected ConsoleRSAKeyPairGenerator(
        final RSAKeyPairGeneratorInterface generator,
        final TextInterfaceDevice textInterfaceDevice,
        final CommandLineParser cliParser
    )
    {
        this.generator = generator;
        this.device = textInterfaceDevice;
        this.cliParser = cliParser;
    }

    /**
     * Prints a new line.
     *
     * @throws Throwable only if {@code super} does.
     */
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        this.device.printOutLn();
    }

    /**
     * Gets the CLI.
     *
     * @return the CLI.
     */
    protected CommandLine getCli()
    {
        return this.cli;
    }

    /**
     * Sets the CLI (used for testing).
     *
     * @param cli The CLI
     */
    void setCli(final CommandLine cli)
    {
        this.cli = cli;
    }

    /**
     * Checks whether this is an interactive shell.
     *
     * @return {@code true} if it's interactive.
     */
    protected boolean isInteractive()
    {
        return this.interactive;
    }

    /**
     * Sets whether this is an interactive shell (used for testing).
     *
     * @param interactive Whether this is an interactive shell
     */
    @SuppressWarnings("SameParameterValue")
    void setInteractive(final boolean interactive)
    {
        this.interactive = interactive;
    }

    /**
     * Processes the supplied command line arguments.
     *
     * @param arguments The arguments supplied on the command line when invoking the program.
     */
    protected void processCommandLineOptions(final String[] arguments)
    {
        final Options firstParseOptions = new Options();
        firstParseOptions.addOption(HELP).addOption(INTERACTIVE).addOption(GENERATE_CLASSES);

        final Options options = new Options();
        options.addOption(HELP).addOption(INTERACTIVE).addOption(GENERATE_CLASSES).addOption(PRIVATE_FILE).
            addOption(PRIVATE_PACKAGE).addOption(PUBLIC_FILE).addOption(PUBLIC_PACKAGE).addOption(PASSWORD).
            addOption(PASSWORD_CLASS).addOption(PASSWORD_PACKAGE).addOption(PRIVATE_PASSWORD).
            addOption(PRIVATE_PASSWORD_CLASS).addOption(PRIVATE_PASSWORD_PACKAGE);

        try
        {
            this.cli = this.cliParser.parse(firstParseOptions, arguments, true);

            if(this.cli.hasOption("help"))
            {
                final HelpFormatter formatter = new HelpFormatter();
                this.printHelp(formatter, options);

                this.device.exit(0);
            }
            else if(this.cli.hasOption("interactive"))
            {
                this.cli = null;
                this.interactive = true;
            }
            else
            {
                this.cli = this.cliParser.parse(options, arguments);
            }
        }
        catch(final ParseException e)
        {
            this.device.printErrLn(e.getLocalizedMessage());

            final HelpFormatter formatter = new HelpFormatter();
            this.printHelp(formatter, options);

            this.device.exit(1);
        }
    }

    private void printHelp(final HelpFormatter formatter, final Options options)
    {
        final OutputStreamWriter streamWriter = new OutputStreamWriter(this.device.out(), LicensingCharsets.UTF_8);
        final PrintWriter printWriter = new PrintWriter(streamWriter);
        formatter.printHelp(
            printWriter,
            ConsoleRSAKeyPairGenerator.CLI_WIDTH,
            ConsoleRSAKeyPairGenerator.USAGE,
            null,
            options,
            ConsoleRSAKeyPairGenerator.HELP_DISPLAY_LEFT_PAD,
            ConsoleRSAKeyPairGenerator.HELP_DISPLAY_DESC_PAD,
            null,
            false
        );
        printWriter.close();
        try
        {
            streamWriter.close();
        }
        catch(final IOException e)
        {
            e.printStackTrace(this.device.err());
        }
    }

    /**
     * Displays a prompt and gathers a choice selection about generating classes or saving key files.
     *
     * @return {@code true} to generate compilable Java code with embedded keys, {@code false} to save them to files.
     */
    protected boolean promptToGenerateClasses()
    {
        this.device.printOutLn("Would you like to...");
        this.device.printOutLn("    (1) Save the public and private keys to .key files?");
        this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
        final String input = this.device.readLine("Your selection (default 1)? ");
        this.device.printOutLn();

        return input != null && input.trim().equals("2");
    }

    /**
     * Displays a prompt and gathers a choice selection about using the same password or different passwords for each
     * key.
     *
     * @return {@code true} to use different passwords, {@code false} to use just one.
     */
    protected boolean promptToUseDifferentPasswords()
    {
        this.device.printOutLn("Would you like to...");
        this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
        this.device.printOutLn("    (2) Use a different password for each key?");
        final String input = this.device.readLine("Your selection (default 1)? ");
        this.device.printOutLn();

        return input != null && input.trim().equals("2");
    }

    /**
     * Prompts for user input using the specified message and returns the data entered by the user.
     *
     * @param message The prompt message
     *
     * @return The user input
     */
    protected String promptForString(final String message)
    {
        final String input = this.device.readLine(message);
        this.device.printOutLn();

        return input != null && input.trim().length() > 0 ? input.trim() : null;
    }

    /**
     * Checks whether the file exists. If it does, prompts the user whether it should be overwritten.
     *
     * @param fileName The name of the file to check
     * @return {@code true} if the file does not exist or exists but the user agrees to overwrite it, {@code false} if
     *     the file exists and either it cannot be overwritten or the user declines to overwrite it.
     */
    protected boolean checkAndPromptToOverwriteFile(final String fileName)
    {
        final File file = new File(fileName);
        if(file.exists())
        {
            String filePath;
            try
            {
                filePath = file.getCanonicalPath();
            }
            catch(final IOException e)
            {
                // in theory, if we got here, this is impossible
                filePath = file.getAbsolutePath();
            }

            if(!file.canRead() || !file.canWrite())
            {
                this.device.printErrLn("The file " + filePath + " already exists and cannot be overwritten.");
                return false;
            }
            else
            {
                final String answer = this.device.readLine(
                    "The file \"%s\" already exists. Overwrite it (YES/no)? ",
                    filePath
                ).
                    trim();
                return answer.length() == 0 || "y".equalsIgnoreCase(answer) || "yes".equalsIgnoreCase(answer);
            }
        }

        return true;
    }

    private void doInteractivePromptForPasswords(final KeyPairGeneratorInternal internal)
    {
        internal.useDifferentPasswords = this.promptToUseDifferentPasswords();
        internal.password = this.device.promptForValidPassword(
            ConsoleRSAKeyPairGenerator.MIN_PASSWORD_LENGTH, ConsoleRSAKeyPairGenerator.MAX_PASSWORD_LENGTH,
            internal.useDifferentPasswords ? "the public key" : "both keys"
        );
        this.device.printOutLn("Passwords match.");
        if(internal.useDifferentPasswords)
        {
            this.device.printOutLn();
        }
        if(internal.useDifferentPasswords)
        {
            internal.privatePassword = this.device.promptForValidPassword(
                ConsoleRSAKeyPairGenerator.MIN_PASSWORD_LENGTH, ConsoleRSAKeyPairGenerator.MAX_PASSWORD_LENGTH,
                "the private key"
            );
            this.device.printOutLn("Passwords match.");
        }
        else
        {
            internal.privatePassword = null;
        }
        this.device.printOutLn();
    }

    private void doInteractivePromptForStorageMechanism(final KeyPairGeneratorInternal internal)
    {
        while(internal.publicOutputStore == null)
        {
            internal.publicOutputStore = this.promptForString(
                internal.generateClasses ?
                "Please enter the name of a Java class to embed the public key in: " :
                "Please enter the name of a file to store the public key in: "
            );
        }

        internal.publicClassPackage =
            internal.generateClasses ?
            this.promptForString("Enter an optional package name for the public key class: "):
            null;

        while(internal.privateOutputStore == null)
        {
            internal.privateOutputStore = this.promptForString(
                internal.generateClasses ?
                "Please enter the name of a Java class to embed the private key in: " :
                "Please enter the name of a file to store the private key in: "
            );
        }

        internal.privateClassPackage =
            internal.generateClasses ?
            this.promptForString("Enter an optional package name for the private key class: "):
            null;
    }

    /**
     * Manages all the work of prompting for instructions and executing key generation and storage.
     *
     * @throws Exception if basically anything goes wrong.
     */
    protected void doInteractive() throws Exception
    {
        final KeyPairGeneratorInternal internal = new KeyPairGeneratorInternal();

        internal.generateClasses = this.promptToGenerateClasses();

        this.doInteractivePromptForPasswords(internal);

        this.doInteractivePromptForStorageMechanism(internal);

        if(internal.generateClasses)
        {
            final String publicKeyWord = internal.useDifferentPasswords ? "public key" : "key";

            internal.passwordClass = this.promptForString(
                "If you wish to embed the " + publicKeyWord + " password in a Java " +
                "class, enter the class name now: ");

            if(internal.passwordClass != null)
            {
                internal.passwordPackage = this.promptForString("You can optionally enter a package name for the " +
                                                                publicKeyWord + " storage class: ");
            }

            if(internal.useDifferentPasswords)
            {
                internal.privatePasswordClass = this.promptForString(
                    "If you wish to embed the private key password in a Java " +
                    "class, enter the class name now: ");

                if(internal.privatePasswordClass != null)
                {
                    internal.privatePasswordPackage = this.promptForString(
                        "You can optionally enter a package name for the " +
                        "private key storage class: ");
                }
            }
        }

        internal.doGenerateAndSaveKeyPair();
    }

    /**
     * Manages all the work of executing key generation and storage based on flags passed in to the command.
     *
     * @throws Exception if basically anything goes wrong.
     */
    protected void doCommandLine() throws Exception
    {
        if(this.cli == null)
        {
            throw new IllegalStateException("doCommandLine called before processCommandLineOptions!");
        }

        final KeyPairGeneratorInternal internal = new KeyPairGeneratorInternal();

        internal.generateClasses = this.cli.hasOption("classes");
        internal.useDifferentPasswords = this.cli.hasOption("privatePassword");
        internal.password = this.cli.getOptionValue("password").toCharArray();
        internal.privatePassword =
            internal.useDifferentPasswords ? this.cli.getOptionValue("privatePassword").toCharArray() : null;

        internal.publicOutputStore = this.cli.getOptionValue("public");
        internal.publicClassPackage = this.cli.getOptionValue("publicPackage");
        internal.privateOutputStore = this.cli.getOptionValue("private");
        internal.privateClassPackage = this.cli.getOptionValue("privatePackage");

        if(internal.generateClasses)
        {
            internal.passwordClass = this.cli.getOptionValue("passwordClass");
            internal.passwordPackage = this.cli.getOptionValue("passwordPackage");
            if(internal.passwordPackage != null && internal.passwordPackage.trim().length() == 0)
            {
                internal.passwordPackage = null;
            }

            if(internal.useDifferentPasswords)
            {
                internal.privatePasswordClass = this.cli.getOptionValue("privatePasswordClass");
                internal.privatePasswordPackage = this.cli.getOptionValue("privatePasswordPackage");
                if(internal.privatePasswordPackage != null && internal.privatePasswordPackage.trim().length() == 0)
                {
                    internal.privatePasswordPackage = null;
                }
            }
        }

        internal.doGenerateAndSaveKeyPair();
    }

    private void runDoWork(final String[] arguments) throws Exception
    {
        this.processCommandLineOptions(arguments);

        if(this.interactive)
        {
            this.device.printOutLn("Using interactive mode...");
            this.device.printOutLn();
            this.doInteractive();
        }
        else
        {
            this.doCommandLine();
        }
    }

    private void runHandleException(final RSA2048NotSupportedException e)
    {
        this.device.printErrLn(e.getLocalizedMessage());
        if(e.getCause() != null && e.getCause() instanceof NoSuchAlgorithmException)
        {
            this.device.exit(ConsoleRSAKeyPairGenerator.ERROR_CODE_RSA_NOT_SUPPORTED);
        }
        else
        {
            this.device.exit(ConsoleRSAKeyPairGenerator.ERROR_CODE_2048_BIT_NOT_SUPPORTED);
        }
    }

    private void runHandleException(final IOException e)
    {
        this.device.printErrLn(
            "An error occurred writing the key files to the file system. Analyze the error below to determine " +
            "what went wrong and fix it!"
        );
        this.device.printErrLn(e.toString());
        e.printStackTrace();
        this.device.exit(ConsoleRSAKeyPairGenerator.ERROR_CODE_IO);
    }

    /**
     * Processes command line arguments and invokes command-line mode or interactive mode.
     *
     * @param arguments The command line arguments
     */
    public void run(final String[] arguments)
    {
        try
        {
            this.runDoWork(arguments);
        }
        catch(final RSA2048NotSupportedException e)
        {
            this.runHandleException(e);
        }
        catch(final AlgorithmNotSupportedException e)
        {
            this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
            this.device.exit(ConsoleRSAKeyPairGenerator.ERROR_CODE_KEY_ALGORITHM_NOT_SUPPORTED);
        }
        catch(final InappropriateKeyException e)
        {
            this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
            this.device.exit(ConsoleRSAKeyPairGenerator.ERROR_CODE_INVALID_KEY);
        }
        catch(final InappropriateKeySpecificationException e)
        {
            this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
            this.device.exit(ConsoleRSAKeyPairGenerator.ERROR_CODE_INVALID_KEY_SPEC);
        }
        catch(final InterruptedException e)
        {
            // In theory, this error won't actually get reached in this circumstance, but we'll catch it just in case.
            this.device.printErrLn("The system was interrupted while waiting for events to complete.");
            this.device.exit(ConsoleRSAKeyPairGenerator.ERROR_CODE_INTERRUPTED);
        }
        catch(final IOException e)
        {
            this.runHandleException(e);
        }
        catch(final Exception t)
        {
            this.device.printErrLn(t.toString());
            t.printStackTrace();
            this.device.exit(ConsoleRSAKeyPairGenerator.ERROR_CODE_UNKNOWN);
        }

        this.device.exit(0);
    }

    /**
     * The entry point method for this command.
     *
     * @param arguments The command line arguments
     */
    public static void main(final String... arguments)
    {
        final TextInterfaceDevice device = TextInterfaceDevice.CONSOLE;

        ConsoleUtilities.configureInterfaceDevice(device);

        new ConsoleRSAKeyPairGenerator(new RSAKeyPairGenerator(), device, new DefaultParser()).run(arguments);
    }

    /**
     * An internal key pair generator used as a helper class.
     */
    private final class KeyPairGeneratorInternal
    {
        private boolean generateClasses;

        private boolean useDifferentPasswords;

        private char[] password;

        private char[] privatePassword;

        private String privateOutputStore;

        private String privateClassPackage;

        private String publicOutputStore;

        private String publicClassPackage;

        private String passwordClass;

        private String passwordPackage;

        private String privatePasswordClass;

        private String privatePasswordPackage;

        private KeyPairGeneratorInternal()
        {

        }

        /**
         * Generates and saves the key pair, displaying periods as the keys generate to indicate that the program has
         * not frozen (it can take some time to generate keys).
         *
         * @throws InterruptedException if execution is interrupted.
         * @throws IOException if an I/O error occurs.
         */
        public void doGenerateAndSaveKeyPair() throws InterruptedException, IOException
        {
            device.printOut("Generating RSA key pair, 2048-bit long modulus");
            Periods periods = new Periods(ConsoleRSAKeyPairGenerator.PERIODS_DELAY_MILLISECONDS, device.out());
            new Thread(periods).start();
            Thread.sleep(ConsoleRSAKeyPairGenerator.THREAD_DELAY_MILLISECONDS);

            final KeyPair keyPair = generator.generateKeyPair();

            periods.stop();
            device.printOutLn("+++");
            device.printOutLn();

            device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
            periods = new Periods(ConsoleRSAKeyPairGenerator.PERIODS_DELAY_MILLISECONDS, device.out());
            new Thread(periods).start();
            Thread.sleep(ConsoleRSAKeyPairGenerator.THREAD_DELAY_MILLISECONDS);

            if(this.generateClasses)
            {
                this.doGenerateClasses(keyPair, periods);
            }
            else
            {
                this.doGenerateFiles(keyPair, periods);
            }

            Arrays.fill(this.password, '\u0000');
            if(this.useDifferentPasswords)
            {
                Arrays.fill(this.privatePassword, '\u0000');
            }
        }

        private void doGenerateClasses(
            final RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateDescriptor,
            final RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicDescriptor,
            final RSAKeyPairGeneratorInterface.GeneratedClassDescriptor passwordDescriptor,
            final RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privatePasswordDescriptor
        )
        {
            final TextInterfaceDevice device = ConsoleRSAKeyPairGenerator.this.device;
            device.printOutLn("+++");
            device.printOutLn();

            device.printOutLn("Private key provider:");
            device.printOutLn();
            device.printOutLn(privateDescriptor.getJavaFileContents());
            device.printOutLn();

            device.printOutLn("Public key provider:");
            device.printOutLn();
            device.printOutLn(publicDescriptor.getJavaFileContents());

            if(this.passwordClass != null)
            {
                device.printOutLn();
                device.printOutLn(this.useDifferentPasswords ?
                                  "Public key password provider:" :
                                  "Key password provider:");
                device.printOutLn();
                device.printOutLn(passwordDescriptor.getJavaFileContents());
            }

            if(this.useDifferentPasswords && this.privatePasswordClass != null)
            {
                device.printOutLn();
                device.printOutLn("Private key password provider:");
                device.printOutLn();
                device.printOutLn(privatePasswordDescriptor.getJavaFileContents());
            }
        }

        /**
         * Generates classes from the given key pair, displaying periods as the classes generate to indicate that the
         * program has not frozen.
         *
         * @param keyPair The recently-generated key pair
         * @param periods The periods display tool
         */
        public void doGenerateClasses(final KeyPair keyPair, final Periods periods)
        {
            final RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateDescriptor =
                new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor().
                    setClassName(this.privateOutputStore).setPackageName(this.privateClassPackage);

            final RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicDescriptor =
                new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor().
                    setClassName(this.publicOutputStore).setPackageName(this.publicClassPackage);

            if(this.useDifferentPasswords)
            {
                ConsoleRSAKeyPairGenerator.this.generator.saveKeyPairToProviders(
                    keyPair, privateDescriptor, publicDescriptor, this.privatePassword, this.password
                );
            }
            else
            {
                ConsoleRSAKeyPairGenerator.this.generator.saveKeyPairToProviders(
                    keyPair, privateDescriptor, publicDescriptor, this.password
                );
            }

            final RSAKeyPairGeneratorInterface.GeneratedClassDescriptor passwordDescriptor =
                new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor().
                    setClassName(this.passwordClass).setPackageName(this.passwordPackage);

            final RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privatePasswordDescriptor =
                new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor().
                    setClassName(this.privatePasswordClass).setPackageName(this.privatePasswordPackage);

            if(this.passwordClass != null)
            {
                ConsoleRSAKeyPairGenerator.this.generator.savePasswordToProvider(this.password, passwordDescriptor);
            }

            if(this.useDifferentPasswords && this.privatePasswordClass != null)
            {
                ConsoleRSAKeyPairGenerator.this.generator.savePasswordToProvider(
                    this.privatePassword, privatePasswordDescriptor
                );
            }

            periods.stop();

            this.doGenerateClasses(privateDescriptor, publicDescriptor, passwordDescriptor, privatePasswordDescriptor);
        }

        /**
         * Saves the key pair to public and private key files, displaying periods as the files save to indicate that
         * the program has not frozen.
         *
         * @param keyPair The recently-generated key pair
         * @param periods The Periods display tool
         *
         * @throws IOException if an I/O error occurs.
         */
        public void doGenerateFiles(final KeyPair keyPair, final Periods periods) throws IOException
        {
            if(ConsoleRSAKeyPairGenerator.this.interactive)
            {
                if(!ConsoleRSAKeyPairGenerator.this.checkAndPromptToOverwriteFile(this.privateOutputStore) ||
                   !ConsoleRSAKeyPairGenerator.this.checkAndPromptToOverwriteFile(this.publicOutputStore))
                {
                    device.exit(ConsoleRSAKeyPairGenerator.ERROR_CODE_FILE_OVERWRITE);
                    return;
                }
            }

            if(this.useDifferentPasswords)
            {
                generator.saveKeyPairToFiles(
                    keyPair, this.privateOutputStore, this.publicOutputStore, this.privatePassword, this.password
                );
            }
            else
            {
                generator.saveKeyPairToFiles(
                    keyPair, this.privateOutputStore, this.publicOutputStore, this.password
                );
            }

            periods.stop();
            device.printOutLn("+++");
            device.printOutLn();

            device.printOutLn("Private key written to " + this.privateOutputStore);
            device.printOutLn("Public key written to " + this.publicOutputStore);
        }
    }
}
