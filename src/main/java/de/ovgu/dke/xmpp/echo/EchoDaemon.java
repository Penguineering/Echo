package de.ovgu.dke.xmpp.echo;

import de.ovgu.dke.cet.xmpp.XMPPCLI;
import de.ovgu.dke.cet.xmpp.XMPPConnector;
import de.ovgu.dke.cet.xmpp.XMPPDaemon;

/*
 * Example XMPP client.
 * 
 * Author: Stefan Haun (stefan.haun@ovgu.de)
 * Created on Feb 3, 2011
 * 
 */

/**
 * Daemon class for the example XMPP echo client. Registers the handler for an
 * ECHO command.
 */
public class EchoDaemon extends XMPPDaemon {
    /**
     * Convinience main method: just start the XMPP daemon via CLI class.
     * 
     * @param args
     *            Command line argments as provided in main method.
     */
    public static void main(String[] args) {
        XMPPCLI.main(args);
    }

    /**
     * Put the service name here, this will be used in logging messages.
     */
    @Override
    public String getName() {
        return "Echo Service";
    }

    /**
     * Register global callback handlers here.
     */
    @Override
    public void initCallbackHandlers() {
        XMPPConnector.getInstance().registerGlobalCommandCallback("ECHO", new EchoHandler());
    }
}