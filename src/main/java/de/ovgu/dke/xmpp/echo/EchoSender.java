package de.ovgu.dke.xmpp.echo;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import de.ovgu.dke.glue.api.serialization.SerializationProvider;
import de.ovgu.dke.glue.api.transport.TransportException;
import de.ovgu.dke.glue.api.transport.TransportRegistry;
import de.ovgu.dke.mocca.MoccaException;
import de.ovgu.dke.mocca.command.Command;
import de.ovgu.dke.mocca.command.DefaultCommandFactory;
import de.ovgu.dke.mocca.context.Context;
import de.ovgu.dke.mocca.context.ContextImpl;
import de.ovgu.dke.mocca.control.CommandHandler;
import de.ovgu.dke.mocca.glue.CommandPacketHandlerFactory;
import de.ovgu.dke.mocca.glue.CommandSerializationProvider;

public class EchoSender {
	public static void main(String[] args) throws TransportException,
			MoccaException, IOException {
		// initialize and register transport factory
		final SerializationProvider serializers = CommandSerializationProvider
				.getInstance();
		TransportRegistry.getInstance().loadTransportFactory(
				"de.ovgu.dke.glue.xmpp.transport.XMPPTransportFactory",
				new CommandPacketHandlerFactory(new EchoCommandHandler()),
				serializers, TransportRegistry.AS_DEFAULT,
				TransportRegistry.DEFAULT_KEY);

		// create a context
		final Context ctx = new ContextImpl(
				TransportRegistry.getDefaultTransportFactory());
		ctx.connect(URI.create("xmpp:shaun@bison.cs.uni-magdeburg.de"));

		// create a command
		final Properties props = new Properties();
		props.put("text", "Hallo Welt!");
		final Command cmd = new DefaultCommandFactory()
				.createCommand(URI
						.create("http://dke.ovgu.de/mocca/test/command/echo"),
						props);

		// send the command
		ctx.sendCommand(cmd);

		System.in.read();

		// disconnect the context
		ctx.disconnect();

		// dispose the transport factory
		TransportRegistry.getInstance().disposeAll();

	}
}

class EchoCommandHandler implements CommandHandler {

	@Override
	public void handleCommand(Command cmd, Context ctx) throws MoccaException {
		System.out.println("Received command: " + cmd.getCommand());
	}
}