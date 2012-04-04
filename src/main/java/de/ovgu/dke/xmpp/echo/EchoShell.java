package de.ovgu.dke.xmpp.echo;

import java.io.IOException;

import de.ovgu.dke.mocca.daemon.MoccaShell;

public class EchoShell {

	public static void main(String[] args) throws IOException {
		MoccaShell.start(EchoAgent.class);
	}
}
