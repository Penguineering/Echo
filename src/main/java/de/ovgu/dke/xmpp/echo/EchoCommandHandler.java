package de.ovgu.dke.xmpp.echo;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import de.ovgu.dke.mocca.api.MoccaException;
import de.ovgu.dke.mocca.api.command.Command;
import de.ovgu.dke.mocca.api.command.CommandHandler;
import de.ovgu.dke.mocca.api.context.Context;

public class EchoCommandHandler implements CommandHandler {
	public static final URI CMD_ECHO = URI
			.create("http://dke.ovgu.de/mocca/test/command/echo");

	@Override
	public Collection<URI> getAvailableCommands() {
		return Collections.singletonList(CMD_ECHO);
	}

	@Override
	public void handleCommand(Command cmd, Context ctx) throws MoccaException {
		final String ctx_id = (String) ctx.getAttribute(Context.ATTR_ID);
		System.out.println("Received command " + cmd.getCommand()
				+ " on context " + ctx_id);

		final String text = cmd.getParameter("text");
		System.out.println("Text is: " + text);

		Integer iteration = (Integer) ctx.getAttribute("iteration");
		if (iteration == null)
			iteration = new Integer(0);
		++iteration;

		ctx.putAttribute("iteration", iteration);

		final Properties props = new Properties();
		props.put("text", text);
		props.put("iteration", Integer.toString(iteration));
		final Command response = ctx.getRuntime().createCommand(
				EchoSender.echoCmd, props);

		ctx.sendCommand(response);
	}
}