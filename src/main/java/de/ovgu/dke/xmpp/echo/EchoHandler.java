package de.ovgu.dke.xmpp.echo;

import java.util.Properties;

import de.ovgu.dke.cet.xmpp.XMPPCommandMessage;
import de.ovgu.dke.cet.xmpp.XMPPConnector;
import de.ovgu.dke.cet.xmpp.process.XMPPCommandCallback;
import de.ovgu.dke.cet.xmpp.process.XMPPProcess;
import de.ovgu.dke.cet.xmpp.process.XMPPProcessManager;

/*
 * Example XMPP command callback handler
 * 
 * Author: Stefan Haun (stefan.haun@ovgu.de)
 * Created on Feb 3, 2011
 * 
 */

/**
 * Get a message from the ECHO command and send it back.
 */
public class EchoHandler implements XMPPCommandCallback {

    /**
     * Example handler for the ECHO command, which simply responds with ECHO and
     * the data section. If not data had been provided, the process will fail.
     * Do not set against each other!
     */
    @Override
    public boolean handleCommand(XMPPProcess process, XMPPCommandMessage kmsg) {
        if (!"ECHO".equals(kmsg.getCommand()))
            throw new IllegalArgumentException(
                    "Callback handler does not support this command!");

        // Get the data from the command properties
        final String data = kmsg.getProperty("data");

        // Check parameters
        if (data == null)
            // Send an error message
            sendError(process, "InvalidArgument",
                    "Missing data parameter in ECHO command call!");

        // Normally something sensible would happen here

        // Construct new parameters
        final Properties props = new Properties();

        // Put data in response
        props.put("data", data);

        // Send the reply
        XMPPConnector.getInstance().sendCommandMessage(process, "ECHO", props);

        return XMPPCommandCallback.FINISHED;
    }

    /**
     * Send an error PROGRESS message.
     * 
     * @param process
     *            The current process
     * @param cause
     *            The cause keyword for this error
     * @param msg
     *            A human-readable message
     */
    // TODO this will be moved to the XMPP library someday
    public void sendError(XMPPProcess process, String cause, String msg) {
        // Create the properties object
        final Properties props = new Properties();

        // Put the state (FAILED)
        props.put("state", "FAILED");

        // Put cause and message if available
        props.put("cause", cause);
        if (msg != null)
            props.put("data", msg);

        // Send the message
        XMPPConnector.getInstance().sendCommandMessage(process, "PROGRESS",
                props);

        // Set process to FAILED and finish it
        process.setState(XMPPProcess.State.FAILED);
        XMPPProcessManager.getInstance().killProcess(process.getId());
    }

    /**
     * Send a finished PROGRESS message.
     * 
     * @param process
     *            The current process
     */
    // TODO this will be moved to the XMPP library someday
    public void sendFinished(XMPPProcess process) {
        // Create the properties object
        final Properties props = new Properties();

        // Put the state (FINISHED)
        props.put("state", "FINISHED");

        // Send the message
        XMPPConnector.getInstance().sendCommandMessage(process, "PROGRESS",
                props);

        // Set process to FINISHED and finish it
        process.setState(XMPPProcess.State.FINISHED);
        XMPPProcessManager.getInstance().killProcess(process.getId());
    }

}
