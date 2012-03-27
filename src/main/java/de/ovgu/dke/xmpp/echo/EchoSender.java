package de.ovgu.dke.xmpp.echo;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import de.ovgu.dke.mocca.api.MoccaException;
import de.ovgu.dke.mocca.api.MoccaRuntime;
import de.ovgu.dke.mocca.api.command.Command;
import de.ovgu.dke.mocca.api.command.CommandHandler;
import de.ovgu.dke.mocca.api.context.Context;
import de.ovgu.dke.mocca.util.MoccaHelper;

public class EchoSender {
    public static final URI echoCmd = URI
            .create("http://dke.ovgu.de/mocca/test/command/echo");

    public static void main(String[] args) throws MoccaException, IOException {

        final MoccaRuntime mocca = MoccaHelper.getDefaultRuntime();

        mocca.init();
        
        mocca.getCommandHandlerRegistry().registerCommandHandler(echoCmd,
                new EchoCommandHandler());

        // create a context
        final Context ctx = mocca.createContext();
        ctx.connect(URI.create("xmpp:shaun@bison.cs.uni-magdeburg.de"));

        // create a command
        final Properties props = new Properties();
        props.put("text", "Hallo Welt!");
        final Command cmd = mocca.createCommand(echoCmd, props);

        // send the command
        ctx.sendCommand(cmd);

        System.in.read();

        // disconnect the context
        ctx.disconnect();

        mocca.dispose();
    }
}

class EchoCommandHandler implements CommandHandler {

    @Override
    public void handleCommand(Command cmd, Context ctx) throws MoccaException {
        final String ctx_id = (String) ctx.getAttribute(Context.ATTR_ID);
        System.out.println("Received command on context " + ctx_id + ": "
                + cmd.getCommand());
    }
}