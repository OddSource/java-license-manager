/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nicholaswilliams.java.licensing.licensor.interfaces.text;

import net.nicholaswilliams.java.licensing.encryption.KeyPasswordProvider;
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

/**
 * A command-line tool for generating a public/private key pair. To use:<br/>
 * <br/>
 * To view usage help:<br/>
 * <code>java net.nicholaswilliams.java.licensing.text ConsoleRSAKeyPairGenerator -help</code><br/>
 * <br/>
 * <code>java net.nicholaswilliams.java.licensing.text ConsoleRSAKeyPairGenerator -private &lt;file name&gt; -public &lt;file name&gt;</code><br/>
 * <br/>
 * Be sure to review the documentation for {@link KeyPasswordProvider},
 * {@link net.nicholaswilliams.java.licensing.licensor.PrivateKeyDataProvider} and {@link PublicKeyDataProvider} for
 * further instructions.
 *
 * @author Nick Williams
 * @since 1.0.0
 * @version 1.0.0
 */
public class ConsoleRSAKeyPairGenerator
{
	private final RSAKeyPairGeneratorInterface generator;

	private final TextInterfaceDevice device;

	private final CommandLineParser cliParser;

	protected ConsoleRSAKeyPairGenerator(RSAKeyPairGeneratorInterface generator,
										 TextInterfaceDevice textInterfaceDevice,
										 CommandLineParser cliParser)
	{
		this.generator = generator;
		this.device = textInterfaceDevice;
		this.cliParser = cliParser;
	}

	@Override
	public void finalize() throws Throwable
	{
		super.finalize();
		this.device.out().println();
	}

	@SuppressWarnings({"static-access"})
	protected CommandLine processCommandLineOptions(String[] arguments)
	{
		Option help = CliOptionsBuilder.get().withDescription("Display this help message").hasArg(false).
				create("help");
		Option privateKeyFile = CliOptionsBuilder.get().withArgName("key file").
				withDescription("The name of the private key file to generate").isRequired(true).hasArg(true).
				create("private");
		Option publicKeyFile = CliOptionsBuilder.get().withArgName("key file").
				withDescription("The name of the public key file to generate").isRequired(true).hasArg(true).
				create("public");

		Options firstParseOptions = new Options();
		firstParseOptions.addOption(help);

		Options options = new Options();
		options.addOption(help).addOption(privateKeyFile).addOption(publicKeyFile);

		CommandLine cli;
		try
		{
			cli = this.cliParser.parse(firstParseOptions, arguments, true);

			if(cli.hasOption("help"))
			{
				PrintWriter writer = new PrintWriter(this.device.out());
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(writer, 74, "ConsoleRSAKeyPairGenerator", null, options, 1, 3, null, false);
				writer.flush();

				this.device.exit(0);
				return null;
			}

			cli = this.cliParser.parse(options, arguments);
		}
		catch(ParseException e)
		{
			this.device.err().println(e.getLocalizedMessage());

			PrintWriter writer = new PrintWriter(this.device.out());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(writer, 74, "ConsoleRSAKeyPairGenerator", null, options, 1, 3, null, false);
			writer.flush();

			this.device.exit(1);
			return null;
		}

		return cli;
	}

	protected char[] getValidPassword()
	{
		char[] password1 = null, password2 = null;
		boolean passwordVerified = false;

		while(!passwordVerified)
		{
			password1 = this.device.readPassword("Enter pass phrase for the key encryption: ");

			if(password1.length < 6 || password1.length > 32)
			{
				this.device.err().println(
						"The password must be at least six characters and no more than 32 characters long."
				);
				this.device.err().println();
				continue;
			}

			password2 = this.device.readPassword("Verifying - Enter pass phrase for the key encryption: ");

			passwordVerified = this.generator.passwordsMatch(password1, password2);
			if(!passwordVerified)
			{
				this.device.err().println(
						"ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel."
				);
				this.device.err().println();
			}
		}

		Arrays.fill(password2, '\u0000');

		return password1;
	}

	protected boolean checkAndPromptToOverwriteFile(String fileName)
	{
		File file = new File(fileName);
		if(file.exists())
		{
			String filePath;
			try {
				filePath = file.getCanonicalPath();
			} catch(IOException e) {
				// in theory, if we got here, this is impossible
				filePath = file.getAbsolutePath();
			}

			if(!file.canRead() || !file.canWrite())
			{
				this.device.err().println("The file " + filePath + " already exists and cannot be overwritten.");
				return false;
			}
			else
			{
				String answer = this.device.readLine("The file %s already exists. Overwrite it (YES/no)? ", filePath).
						trim();
				return answer.length() == 0 || answer.equalsIgnoreCase("y") ||
						answer.equalsIgnoreCase("yes");
			}
		}

		return true;
	}

	protected void generateAndSaveKeyPair(char[] password, String privateOutputFileName, String publicOutputFileName)
			throws InterruptedException, IOException
	{
		this.device.out().print("Passwords match. Generating RSA key pair, 2048-bit long modulus");
		Periods periods = new Periods(25, this.device.out());
		new Thread(periods).start();
		Thread.sleep(50);

		KeyPair keyPair = this.generator.generateKeyPair();

		periods.stop();
		this.device.out().println("+++");

		this.device.out().print("Key pair generated. Encrypting keys with 256-bit AES security");
		periods = new Periods(25, this.device.out());
		new Thread(periods).start();
		Thread.sleep(50);

		this.generator.saveKeyPairToFiles(
				keyPair,
				privateOutputFileName,
				publicOutputFileName,
				password
		);

		periods.stop();
		this.device.out().println("+++");

		this.device.out().println("Private key written to " + privateOutputFileName);
		this.device.out().println("Public key written to " + publicOutputFileName);
	}

	public void run(String[] arguments)
	{
		CommandLine cli = this.processCommandLineOptions(arguments);

		char[] password;

		String privateOutputFileName = cli.getOptionValue("private");
		String publicOutputFileName = cli.getOptionValue("public");

		if(!this.checkAndPromptToOverwriteFile(publicOutputFileName) ||
		   !this.checkAndPromptToOverwriteFile(privateOutputFileName))
		{
			this.device.exit(81);
			return;
		}

		password = this.getValidPassword();

		try
		{
			this.generateAndSaveKeyPair(password, privateOutputFileName, publicOutputFileName);

			Arrays.fill(password, '\u0000');
		}
		catch(RSA2048NotSupportedException e)
		{
			this.device.err().println(e.getLocalizedMessage());
			if(e.getCause() != null && e.getCause() instanceof NoSuchAlgorithmException)
				this.device.exit(51);
			else
				this.device.exit(52);
			return;
		}
		catch(AlgorithmNotSupportedException e)
		{
			this.device.err().println(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
			this.device.exit(41);
			return;
		}
		catch(InappropriateKeyException e)
		{
			this.device.err().println(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
			this.device.exit(42);
			return;
		}
		catch(InappropriateKeySpecificationException e)
		{
			this.device.err().println(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
			this.device.exit(43);
			return;
		}
		catch(InterruptedException e)
		{
			// in theory, this error wouldn't actually get reached in this circumstance,
			// but we'll catch it just in case
			this.device.err().println("The system was interrupted while waiting for events to complete.");
			this.device.exit(44);
			return;
		}
		catch(IOException e)
		{
			this.device.err().println("An error occurred writing the key files to the file system. Analyze the error " +
					"below to determine what went wrong and fix it!");
			this.device.err().println(e.toString());
			e.printStackTrace();
			this.device.exit(21);
			return;
		}
		catch(Throwable t)
		{
			this.device.err().println(t.toString());
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
