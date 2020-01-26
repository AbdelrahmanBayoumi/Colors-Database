package colorsdatabase.utilities;

import static colorsdatabase.utilities.Utility.getProgramPath;
import static colorsdatabase.utilities.Utility.createDirectory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Used to log Exceptions and Information
 *
 * @author Abdelrahman Bayoumi
 */
public class Logger {

    public static PrintWriter printWriter;

    public enum Level {
        ERROR,
        INFO
    }

    //========= Helper Objects =========
    private final static String CLASS_NAME = Logger.class.getName();

    /**
     * initialize printWriter object to log data in file
     *
     * @throws IOException
     */
    public static void init() throws IOException {
        createDirectory("logs");
        printWriter = new PrintWriter(new FileWriter(getProgramPath() + "/logs/logs_" + Utility.getDateString(new Date()) + ".txt", true));
    }

    public static void log(Level level, String msg) {
        String DataAndTime = Utility.formatDateTimeString(new Date());
        try {
            if (level == Level.ERROR) {
                System.err.println(msg);
                printWriter.println(DataAndTime + " => " + msg);
            } else if (level == Level.INFO) {
                System.out.println(msg);
                printWriter.println(DataAndTime + " => " + msg);
            }
            printWriter.flush();
        } catch (Exception ex) {
            System.err.println(DataAndTime + "Exception [" + ex + "] in =>\n" + CLASS_NAME
                    + ".log(String msg:" + msg + ")");
        }
    }
}
