package fr.xephi.authme.command.executable.converter;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.converter.Converter;
import fr.xephi.authme.converter.CrazyLoginConverter;
import fr.xephi.authme.converter.FlatToSql;
import fr.xephi.authme.converter.FlatToSqlite;
import fr.xephi.authme.converter.RakamakConverter;
import fr.xephi.authme.converter.RoyalAuthConverter;
import fr.xephi.authme.converter.SqlToFlat;
import fr.xephi.authme.converter.SqliteToSql;
import fr.xephi.authme.converter.vAuthConverter;
import fr.xephi.authme.converter.xAuthConverter;
import fr.xephi.authme.output.MessageKey;
import fr.xephi.authme.output.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ConverterCommand extends ExecutableCommand {

    @Override
    public void executeCommand(CommandSender sender, List<String> arguments) {
        // AuthMe plugin instance
        final AuthMe plugin = AuthMe.getInstance();

        // Messages instance
        final Messages m = plugin.getMessages();

        // Get the conversion job
        String job = arguments.get(0);

        // Determine the job type
        ConvertType jobType = ConvertType.fromName(job);
        if (jobType == null) {
            m.send(sender, MessageKey.ERROR);
            return;
        }

        // Get the proper converter instance
        Converter converter = null;
        switch (jobType) {
            case FTSQL:
                converter = new FlatToSql();
                break;
            case FTSQLITE:
                converter = new FlatToSqlite(sender);
                break;
            case XAUTH:
                converter = new xAuthConverter(plugin, sender);
                break;
            case CRAZYLOGIN:
                converter = new CrazyLoginConverter(plugin, sender);
                break;
            case RAKAMAK:
                converter = new RakamakConverter(plugin, sender);
                break;
            case ROYALAUTH:
                converter = new RoyalAuthConverter(plugin);
                break;
            case VAUTH:
                converter = new vAuthConverter(plugin, sender);
                break;
            case SQLTOFLAT:
                converter = new SqlToFlat(plugin, sender);
                break;
            case SQLITETOSQL:
            	converter = new SqliteToSql(plugin, sender);
            	break;
            default:
                break;
        }

        // Run the convert job
        Bukkit.getScheduler().runTaskAsynchronously(plugin, converter);

        // Show a status message
        sender.sendMessage("[AuthMe] Successfully converted from " + jobType.getName());
    }

    public enum ConvertType {
        FTSQL("flattosql"),
        FTSQLITE("flattosqlite"),
        XAUTH("xauth"),
        CRAZYLOGIN("crazylogin"),
        RAKAMAK("rakamak"),
        ROYALAUTH("royalauth"),
        VAUTH("vauth"),
        SQLTOFLAT("sqltoflat"),
        SQLITETOSQL("sqlitetosql");

        final String name;

        ConvertType(String name) {
            this.name = name;
        }

        public static ConvertType fromName(String name) {
            for (ConvertType type : ConvertType.values()) {
                if (type.getName().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return null;
        }

        String getName() {
            return this.name;
        }
    }
}
