package de.ovgu.dke.xmpp.echo;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import de.ovgu.dke.glue.api.transport.Packet;
import de.ovgu.dke.glue.api.transport.PacketHandler;
import de.ovgu.dke.glue.api.transport.PacketHandlerFactory;
import de.ovgu.dke.glue.api.transport.PacketThread;
import de.ovgu.dke.glue.api.transport.Transport;
import de.ovgu.dke.glue.api.transport.TransportException;
import de.ovgu.dke.glue.api.transport.TransportRegistry;
import de.ovgu.dke.glue.util.serialization.SingleSerializerProvider;
import de.ovgu.dke.glue.util.transport.ClosedPacketHandler;
import de.ovgu.dke.mocca.MoccaException;
import de.ovgu.dke.mocca.command.Command;
import de.ovgu.dke.mocca.command.DefaultCommandFactory;
import de.ovgu.dke.mocca.command.TextCommandSerializer;

public class EchoSender {
	public static void main(String[] args) throws TransportException,
			MoccaException, IOException {
		// initialize and register transport factory
		TransportRegistry.getInstance().loadTransportFactory(
				"de.ovgu.dke.glue.xmpp.transport.XMPPTransportFactory",
				new ClosedPacketHandlerFactory(),
				new SingleSerializerProvider(new TextCommandSerializer()),
				TransportRegistry.AS_DEFAULT, TransportRegistry.DEFAULT_KEY);

		// create a command
		final Properties props = new Properties();
		props.put("text", "Hallo Welt!");
		final Command cmd = new DefaultCommandFactory()
				.createCommand(URI
						.create("http://dke.ovgu.de/mocca/test/command/echo"),
						props);

		// get a transport
		final Transport xmpp = TransportRegistry.getDefaultTransportFactory()
				.createTransport(
						URI.create("xmpp:shaun@bison.cs.uni-magdeburg.de"));

		// create a packet thread
		final PacketThread thread = xmpp
				.createThread(PacketThread.DEFAULT_HANDLER);

		// send the command
		thread.send(cmd, Packet.Priority.DEFAULT);

		System.in.read();

		// finish thread
		thread.dispose();

		// dispose the transport factory
		TransportRegistry.getInstance().disposeAll();

	}
}

class ClosedPacketHandlerFactory implements PacketHandlerFactory {
	private static PacketHandler closedHandler = null;

	@Override
	public synchronized PacketHandler createPacketHandler()
			throws InstantiationException {
		if (closedHandler == null)
			closedHandler = ClosedPacketHandler.instance();

		return closedHandler;
	}
}
