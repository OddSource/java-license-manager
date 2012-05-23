/*
 * ConsoleRSAKeyPairGenerator.java from LicenseManager modified Tuesday, May 22, 2012 19:24:24 CDT (-0500).
 *
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nicholaswilliams.java.licensing.licensor.interfaces.text;

import net.nicholaswilliams.java.licensing.encryption.PublicKeyDataProvider;
import net.nicholaswilliams.java.licensing.encryption.RSAKeyPairGenerator;
import net.nicholaswilliams.java.licensing.encryption.RSAKeyPairGeneratorInterface;
import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import net.nicholaswilliams.java.licensing.exception.RSA2048NotSupportedException;
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.CliOptionsBuilder;
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static net.nicholaswilliams.java.licensing.encryption.RSAKeyPairGeneratorInterface.*;

/**
 * A command-line tool for generating a public/private key pair. Usage is as follows.<br />
 * <br />
 * To view usage and help:<br />
 * <code>java net.nicholaswilliams.java.licensing.text ConsoleRSAKeyPairGenerator -help</code><br />
 * <br />
 * To use interactive mode:<br />
 * <code>java net.nicholaswilliams.java.licensing.text ConsoleRSAKeyPairGenerator -interactive</code><br />
 * <br />
 * To specify all options at the command line:<br />
 * <code>java net.nicholaswilliams.java.licensing.text ConsoleRSAKeyPairGenerator -password &lt;password&gt; -private &lt;file|class name&gt; -public &lt;file|class name&gt; [-privatePassword &lt;password&gt;] [-classes -passwordClass &lt;class name&gt; -privatePasswordClass &lt;class name&gt; [-privatePackage &lt;package&gt;] [-publicPackage &lt;package&gt;] [-passwordPackage &lt;package&gt;] [-privatePasswordPackage &lt;package&gt;]]</code><br />
 * <br />
 * Be sure to review the documentation for {@link net.nicholaswilliams.java.licensing.encryption.PasswordProvider},
 * {@link net.nicholaswilliams.java.licensing.licensor.PrivateKeyDataProvider} and {@link PublicKeyDataProvider} for
 * further instructions.
 *
 * @author Nick Williams
 * @since 1.0.0
 * @version 1.0.0
 */
public class ConsoleRSAKeyPairGenerator
{
	private static final int CLI_WIDTH = 105;

	private static final String USAGE = " ConsoleRSAKeyPairGenerator -help\r\n" +
										" ConsoleRSAKeyPairGenerator -interactive\r\n" +
										" ConsoleRSAKeyPairGenerator -password <password> -private <file|class name> " +
											"-public <file|class name> [-privatePassword <password>] [-classes " +
											"-passwordClass <class name> -privatePasswordClass <class name> " +
											"[-privatePackage <package>] [-publicPackage <package>] " +
											"[-passwordPackage <package>] [-privatePasswordPackage <package>]]";

	private static final Option HELP = CliOptionsBuilder.get().withDescription("Display this help message").
			hasArg(false).create("help");

	private static final Option INTERACTIVE = CliOptionsBuilder.get().
			withDescription("Specify to use interactive mode and ignore command-line options").
			isRequired(false).hasArg(false).create("interactive");

	private static final Option GENERATE_CLASSES = CliOptionsBuilder.get().
			withDescription("Specify to generate compilable Java classes instead of key files").
			isRequired(false).hasArg(false).create("classes");

	private static final Option PRIVATE_FILE = CliOptionsBuilder.get().withArgName("file|class name").
			withDescription("The name of the private key file or class to generate (required unless in " +
							"interactive mode)").
			isRequired(true).hasArg(true).create("private");

	private static final Option PRIVATE_PACKAGE = CliOptionsBuilder.get().withArgName("package").
			withDescription("The name of the package to use for the private key class (optional, ignored unless " +
							"generating classes)").
			isRequired(false).hasArg(true).create("privatePackage");

	private static final Option PUBLIC_FILE = CliOptionsBuilder.get().withArgName("file|class name").
			withDescription("The name of the public key file or class to generate (required unless in " +
							"interactive mode)").
			isRequired(true).hasArg(true).create("public");

	private static final Option PUBLIC_PACKAGE = CliOptionsBuilder.get().withArgName("package").
			withDescription("The name of the package to use for the public key class (optional, ignored unless " +
							"generating classes)").
			isRequired(false).hasArg(true).create("publicPackage");

	private static final Option PASSWORD = CliOptionsBuilder.get().withArgName("password").
			withDescription("The password to use to encrypt the public and private keys (required unless in " +
							"interactive mode)").
			isRequired(true).hasArg(true).create("password");

	private static final Option PASSWORD_CLASS = CliOptionsBuilder.get().withArgName("class name").
			withDescription("The name of the password storage class to generate (optional, ignored unless " +
							"generating classes)").
			isRequired(false).hasArg(true).create("passwordClass");

	private static final Option PASSWORD_PACKAGE = CliOptionsBuilder.get().withArgName("package").
			withDescription("The name of the package to use for the password storage class (optional, ignored " +
							"unless generating classes)").
			isRequired(false).hasArg(true).create("passwordPackage");

	private static final Option PRIVATE_PASSWORD = CliOptionsBuilder.get().withArgName("password").
			withDescription("A different password to use to encrypt the private key (optional)").
			isRequired(false).hasArg(true).create("privatePassword");

	private static final Option PRIVATE_PASSWORD_CLASS = CliOptionsBuilder.get().withArgName("class name").
			withDescription("The name of the private key password storage class to generate (optional, ignored " +
							"unless generating classes)").
			isRequired(false).hasArg(true).create("privatePasswordClass");

	private static final Option PRIVATE_PASSWORD_PACKAGE = CliOptionsBuilder.get().withArgName("package").
			withDescription("The name of the package to use for the private key password storage class (optional, " +
							"ignored unless generating classes)").
			isRequired(false).hasArg(true).create("privatePasswordPackage");

	private final RSAKeyPairGeneratorInterface generator;

	private final TextInterfaceDevice device;

	private final CommandLineParser cliParser;

	protected CommandLine cli = null;

	protected boolean interactive = false;

	protected ConsoleRSAKeyPairGenerator(RSAKeyPairGeneratorInterface generator,
										 TextInterfaceDevice textInterfaceDevice,
										 CommandLineParser cliParser)
	{
		this.generator = generator;
		this.device = textInterfaceDevice;
		this.cliParser = cliParser;
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		this.device.printOutLn();
	}

	protected void processCommandLineOptions(String[] arguments)
	{
		Options firstParseOptions = new Options();
		firstParseOptions.addOption(HELP).addOption(INTERACTIVE).addOption(GENERATE_CLASSES);

		Options options = new Options();
		options.addOption(HELP).addOption(INTERACTIVE).addOption(GENERATE_CLASSES).addOption(PRIVATE_FILE).
				addOption(PRIVATE_PACKAGE).addOption(PUBLIC_FILE).addOption(PUBLIC_PACKAGE).addOption(PASSWORD).
				addOption(PASSWORD_CLASS).addOption(PASSWORD_PACKAGE).addOption(PRIVATE_PASSWORD).
				addOption(PRIVATE_PASSWORD_CLASS).addOption(PRIVATE_PASSWORD_PACKAGE);

		try
		{
			this.cli = this.cliParser.parse(firstParseOptions, arguments, true);

			if(this.cli.hasOption("help"))
			{
				PrintWriter writer = new PrintWriter(this.device.out());
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(writer, CLI_WIDTH, USAGE, null, options, 1, 3, null, false);
				writer.flush();

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
		catch(ParseException e)
		{
			this.device.printErrLn(e.getLocalizedMessage());

			PrintWriter writer = new PrintWriter(this.device.out());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(writer, CLI_WIDTH, USAGE, null, options, 1, 3, null, false);
			writer.flush();

			this.device.exit(1);
		}
	}

	protected boolean promptToGenerateClasses()
	{
		this.device.printOutLn("Would you like to...");
		this.device.printOutLn("    (1) Save the public and private keys to .key files?");
		this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
		String input = this.device.readLine("Your selection (default 1)? ");
		this.device.printOutLn();
		
		return input != null && input.trim().equals("2");
	}

	protected boolean promptToUseDifferentPasswords()
	{
		this.device.printOutLn("Would you like to...");
		this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
		this.device.printOutLn("    (2) Use a different password for each key?");
		String input = this.device.readLine("Your selection (default 1)? ");
		this.device.printOutLn();

		return input != null && input.trim().equals("2");
	}

	protected char[] promptForValidPassword(String which)
	{
		char[] password1 = null, password2 = null;
		boolean passwordVerified = false;

		try
		{
			while(!passwordVerified)
			{
				password1 = this.device.readPassword("Enter pass phrase to encrypt " + which + ": ");

				if(password1.length < 6 || password1.length > 32)
				{
					this.device.printErrLn(
							"The password must be at least six characters and no more than 32 characters long."
					);
					this.device.printErrLn();
					continue;
				}

				password2 = this.device.readPassword("Verifying - Reenter pass phrase to encrypt " + which + ": ");

				passwordVerified = this.generator.passwordsMatch(password1, password2);
				if(!passwordVerified)
				{
					this.device.printErrLn(
							"ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel."
					);
					this.device.printErrLn();
				}
			}

			this.device.printOutLn("Passwords match.");

			return Arrays.copyOf(password1, password1.length);
		}
		finally
		{
			if(password1 != null)
				Arrays.fill(password1, '\u0000');
			if(password2 != null)
				Arrays.fill(password2, '\u0000');
		}
	}

	protected String promptForString(String message)
	{
		String input = this.device.readLine(message);
		this.device.printOutLn();

		return input != null && input.trim().length() > 0 ? input.trim() : null;
	}

	protected boolean checkAndPromptToOverwriteFile(String fileName)
	{
		File file = new File(fileName);
		if(file.exists())
		{
			String filePath;
			try
			{
				filePath = file.getCanonicalPath();
			}
			catch(IOException e)
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
				String answer = this.device.readLine("The file \"%s\" already exists. Overwrite it (YES/no)? ", filePath).
						trim();
				return answer.length() == 0 || answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes");
			}
		}

		return true;
	}

	protected class KeyPairGeneratorInternal
	{
		boolean generateClasses;
		boolean useDifferentPasswords;
		char[] password;
		char[] privatePassword;
		String privateOutputStore, privateClassPackage, publicOutputStore, publicClassPackage;
		String passwordClass, passwordPackage, privatePasswordClass, privatePasswordPackage;

		public void doGenerateAndSaveKeyPair() throws InterruptedException, IOException
		{
			device.printOut("Generating RSA key pair, 2048-bit long modulus");
			Periods periods = new Periods(25, device.out());
			new Thread(periods).start();
			Thread.sleep(50);

			KeyPair keyPair = generator.generateKeyPair();

			periods.stop();
			device.printOutLn("+++");
			device.printOutLn();

			device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
			periods = new Periods(25, device.out());
			new Thread(periods).start();
			Thread.sleep(50);

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
				Arrays.fill(this.privatePassword, '\u0000');
		}

		public void doGenerateClasses(KeyPair keyPair, Periods periods)
		{
			GeneratedClassDescriptor privateDescriptor = new GeneratedClassDescriptor().
						setClassName(this.privateOutputStore).setPackageName(this.privateClassPackage);

			GeneratedClassDescriptor publicDescriptor = new GeneratedClassDescriptor().
						setClassName(this.publicOutputStore).setPackageName(this.publicClassPackage);

			if(this.useDifferentPasswords)
				generator.saveKeyPairToProviders(keyPair, privateDescriptor, publicDescriptor,
												 this.privatePassword, this.password);
			else
				generator.saveKeyPairToProviders(keyPair, privateDescriptor, publicDescriptor, this.password);

			GeneratedClassDescriptor passwordDescriptor = new GeneratedClassDescriptor().
						setClassName(this.passwordClass).setPackageName(this.passwordPackage);

			GeneratedClassDescriptor privatePasswordDescriptor = new GeneratedClassDescriptor().
						setClassName(this.privatePasswordClass).setPackageName(this.privatePasswordPackage);

			if(this.passwordClass != null)
				generator.savePasswordToProvider(this.password, passwordDescriptor);

			if(this.useDifferentPasswords && this.privatePasswordClass != null)
				generator.savePasswordToProvider(this.privatePassword, privatePasswordDescriptor);

			periods.stop();
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
				device.printOutLn(this.useDifferentPasswords ? "Public key password provider:" : "Key password provider:");
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

		public void doGenerateFiles(KeyPair keyPair, Periods periods) throws IOException
		{
			if(ConsoleRSAKeyPairGenerator.this.interactive)
			{
				if(!ConsoleRSAKeyPairGenerator.this.checkAndPromptToOverwriteFile(this.privateOutputStore) ||
				   !ConsoleRSAKeyPairGenerator.this.checkAndPromptToOverwriteFile(this.publicOutputStore))
				{
					device.exit(81);
					return;
				}
			}
			else
			{
				
			}

			if(this.useDifferentPasswords)
				generator.saveKeyPairToFiles(keyPair, this.privateOutputStore, this.publicOutputStore,
											 this.privatePassword, this.password);
			else
				generator.saveKeyPairToFiles(keyPair, this.privateOutputStore, this.publicOutputStore,
											 this.password);

			periods.stop();
			device.printOutLn("+++");
			device.printOutLn();

			device.printOutLn("Private key written to " + this.privateOutputStore);
			device.printOutLn("Public key written to " + this.publicOutputStore);
		}
	}

	protected void doInteractive() throws Exception
	{
		KeyPairGeneratorInternal internal = new KeyPairGeneratorInternal();

		internal.generateClasses = this.promptToGenerateClasses();
		internal.useDifferentPasswords = this.promptToUseDifferentPasswords();
		internal.password = this.promptForValidPassword(internal.useDifferentPasswords ? "the public key" : "both keys");
		if(internal.useDifferentPasswords)
			this.device.printOutLn();
		internal.privatePassword = internal.useDifferentPasswords ? this.promptForValidPassword("the private key") : null;
		this.device.printOutLn();

		while(internal.publicOutputStore == null)
		{
			internal.publicOutputStore = this.promptForString(internal.generateClasses ?
									  "Please enter the name of a Java class to embed the public key in: " :
									  "Please enter the name of a file to store the public key in: ");
		}

		internal.publicClassPackage = internal.generateClasses ?
									 	this.promptForString("Enter an optional package name for the public key class: ") :
										 null;

		while(internal.privateOutputStore == null)
		{
			internal.privateOutputStore = this.promptForString(internal.generateClasses ?
									  "Please enter the name of a Java class to embed the private key in: " :
									  "Please enter the name of a file to store the private key in: ");
		}

		internal.privateClassPackage = internal.generateClasses ?
									 	this.promptForString("Enter an optional package name for the private key class: ") :
										 null;

		if(internal.generateClasses)
		{
			String publicKeyWord = internal.useDifferentPasswords ? "public key" : "key";
			
			internal.passwordClass = this.promptForString("If you wish to embed the " + publicKeyWord + " password in a Java " +
												 	"class, enter the class name now: ");

			if(internal.passwordClass != null)
				internal.passwordPackage = this.promptForString("You can optionally enter a package name for the " +
													   		publicKeyWord + " storage class: ");

			if(internal.useDifferentPasswords)
			{
				internal.privatePasswordClass = this.promptForString("If you wish to embed the private key password in a Java " +
																"class, enter the class name now: ");

				if(internal.privatePasswordClass != null)
					internal.privatePasswordPackage = this.promptForString("You can optionally enter a package name for the " +
																		"private key storage class: ");
			}
		}

		internal.doGenerateAndSaveKeyPair();
	}

	protected void doCommandLine() throws Exception
	{
		KeyPairGeneratorInternal internal = new KeyPairGeneratorInternal();

		internal.generateClasses = this.cli.hasOption("classes");
		internal.useDifferentPasswords = this.cli.hasOption("privatePassword");
		internal.password = this.cli.getOptionValue("password").toCharArray();
		internal.privatePassword = internal.useDifferentPasswords ? this.cli.getOptionValue("privatePassword").toCharArray() : null;

		internal.publicOutputStore = this.cli.getOptionValue("public");
		internal.publicClassPackage = this.cli.getOptionValue("publicPackage");
		internal.privateOutputStore = this.cli.getOptionValue("private");
		internal.privateClassPackage = this.cli.getOptionValue("privatePackage");

		if(internal.generateClasses)
		{
			internal.passwordClass = this.cli.getOptionValue("passwordClass");
			internal.passwordPackage = this.cli.getOptionValue("passwordPackage");
			if(internal.passwordPackage != null && internal.passwordPackage.trim().length() == 0)
				internal.passwordPackage = null;

			if(internal.useDifferentPasswords)
			{
				internal.privatePasswordClass = this.cli.getOptionValue("privatePasswordClass");
				internal.privatePasswordPackage = this.cli.getOptionValue("privatePasswordPackage");
				if(internal.privatePasswordPackage != null && internal.privatePasswordPackage.trim().length() == 0)
					internal.privatePasswordPackage = null;
			}
		}

		internal.doGenerateAndSaveKeyPair();
	}

	public void run(String[] arguments)
	{
		this.processCommandLineOptions(arguments);

		try
		{
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
		catch(RSA2048NotSupportedException e)
		{
			this.device.printErrLn(e.getLocalizedMessage());
			if(e.getCause() != null && e.getCause() instanceof NoSuchAlgorithmException)
				this.device.exit(51);
			else
				this.device.exit(52);
			return;
		}
		catch(AlgorithmNotSupportedException e)
		{
			this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
			this.device.exit(41);
			return;
		}
		catch(InappropriateKeyException e)
		{
			this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
			this.device.exit(42);
			return;
		}
		catch(InappropriateKeySpecificationException e)
		{
			this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
			this.device.exit(43);
			return;
		}
		catch(InterruptedException e)
		{
			// in theory, this error wouldn't actually get reached in this circumstance,
			// but we'll catch it just in case
			this.device.printErrLn("The system was interrupted while waiting for events to complete.");
			this.device.exit(44);
			return;
		}
		catch(IOException e)
		{
			this.device.printErrLn("An error occurred writing the key files to the file system. Analyze the error " +
					"below to determine what went wrong and fix it!");
			this.device.printErrLn(e.toString());
			e.printStackTrace();
			this.device.exit(21);
			return;
		}
		catch(Throwable t)
		{
			this.device.printErrLn(t.toString());
			t.printStackTrace();
			this.device.exit(-1);
			return;
		}

		this.device.exit(0);
	}
	
	public static void main(String[] arguments)
	{
		TextInterfaceDevice device = TextInterfaceDevice.CONSOLE;

		ConsoleUtilities.configureInterfaceDevice(device);

		new ConsoleRSAKeyPairGenerator(new RSAKeyPairGenerator(), device, new GnuParser()).run(arguments);
	}
}
