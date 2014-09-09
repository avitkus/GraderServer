package edu.unc.cs.graderServer.webHandler.pages;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.unc.cs.graderServer.webHandler.pages.helpers.GradePageManager;
import edu.unc.cs.htmlBuilder.HTMLFile;
import edu.unc.cs.htmlBuilder.IHTMLFile;
import edu.unc.cs.graderServer.utils.ConfigReader;
import edu.unc.cs.graderServer.utils.IConfigReader;

public class GradingResultPage extends HTMLFile implements IGradingResultPage {

    private static final Logger LOG = Logger.getLogger(GradingInProgressPage.class.getName());
    private String id;
    private String ip;

    @Override
    public boolean exists() {
        Optional<IHTMLFile> file = GradePageManager.get(id);
        return file.isPresent();
    }

    @Override
    public void setArgs(String args) {
        //System.out.println(args);
        String[] temp = args.split("\\s");
        args = temp[temp.length - 1];
        args = args.trim();
        //System.out.println("Args: " + args);
        String[] argList = args.split("&");
        //Arrays.stream(argList).forEach((str) -> System.out.println(str.length() + ": " + str));
        for (String arg : argList) {
            if (arg.startsWith("id=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setID(argSplit[1]);
                }
            } else if (arg.startsWith("ip=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setIP(argSplit[1]);
                }
            }
        }
    }

    @Override
    public String getHTML() {
        if (isAllowed()) {
            Optional<IHTMLFile> file = GradePageManager.get(id);
            if (file.isPresent()) {
                return file.get().getHTML();
            }
        }
        return "";
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public void setIP(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean isAllowed() {
        if (id != null && !id.isEmpty()) {
            try {
                IConfigReader config = new ConfigReader("config/config.properties");
                if (config.getBoolean("grader.ip_regulate", false).get()) {
                    Optional<String> pageIp = GradePageManager.getIP(id);
                    if (pageIp.orElse("").equals(ip)) {
                        return true;
                    }
                } else {
                    return true;
                }
            } catch (IOException ex) {
                //ex.printStackTrace();
                Logger.getLogger(GraderPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
