package de.ovgu.dke.xmpp.echo;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import de.ovgu.dke.mocca.api.MoccaException;
import de.ovgu.dke.mocca.api.MoccaRuntime;
import de.ovgu.dke.mocca.api.command.CommandHandler;
import de.ovgu.dke.mocca.daemon.MoccaAgent;
import de.ovgu.dke.mocca.daemon.MoccaShell;

public class EchoAgent implements MoccaAgent {

	public static void main(String[] args) throws IOException {
		MoccaShell.start(EchoAgent.class);
	}

	@Override
	public String getName() {
		return "Echo";
	}

	@Override
	public Collection<CommandHandler> getCommandHandlers() {
		return Collections
				.singletonList((CommandHandler) new EchoCommandHandler());
	}

	@Override
	public void init(MoccaRuntime runtime) throws MoccaException {
	}

	@Override
	public void destroy() {
	}
}
