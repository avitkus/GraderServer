package server.com.webHandler.pages.helpers;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.htmlBuilder.IHTMLFile;


public class GradePageManager {
    private static final Logger LOG = Logger.getLogger(GradePageManager.class.getName());
    
    private static final HashMap<String, PageHolder> pageMap;
    private static final Duration lifetime;
    
    static {
        pageMap = new HashMap<>(10);
        lifetime = Duration.ofMinutes(30);
        ScheduledThreadPoolExecutor sec = new ScheduledThreadPoolExecutor(1);
        sec.scheduleAtFixedRate(new ManagerMonitor(), 60, 60, TimeUnit.SECONDS);
    }
    
    public static boolean purge() {
        synchronized(pageMap) {
            LOG.log(Level.INFO, "Purging grading page register.");
            if (pageMap.isEmpty()) {
                LOG.log(Level.INFO, "Register already empty.");
                return false;
            } else {
                LOG.log(Level.INFO, "{0} pages removed.", pageMap.size());
                pageMap.clear();
                return true;
            }
        }
    }

    public static String add(IHTMLFile page, String ip) {
        String uuid = UUID.randomUUID().toString();
        synchronized(pageMap) {
            pageMap.put(uuid, new PageHolder(page, ip));
        }
        LOG.log(Level.INFO, "Grading page with UUID ''{0}'' has been added.", uuid);
        return uuid;
    }

    public static Optional<Instant> getTimestamp(String key) {
        PageHolder holder;
        synchronized(pageMap) {
            holder = pageMap.get(key);
        }
        if (holder == null) {
            return Optional.empty();
        }
        return Optional.of(holder.instant);
        
    }

    public static Optional<IHTMLFile> get(String key) {
        PageHolder holder;
        synchronized(pageMap) {
            holder = pageMap.get(key);
        }
        if (holder == null) {
            return Optional.empty();
        }
        return Optional.of(holder.file);
    }
    
    public static boolean refresh(String key) {
        synchronized(pageMap) {
            if (pageMap.containsKey(key)) {
                pageMap.get(key).instant = Instant.now();
                return true;
            } else {
                return false;
            }
        }
    }
    
    public static boolean update(String key, IHTMLFile page) {
        synchronized(pageMap) {
            if (pageMap.containsKey(key)) {
                pageMap.get(key).file = page;
                return true;
            } else {
                return false;
            }
        }
    }

    private GradePageManager() {
    }
    
    private static class PageHolder {
        IHTMLFile file;
        final String ip;
        Instant instant;
        
        PageHolder(IHTMLFile file, String ip) {
            this(file, ip, Instant.now());
        }
        
        PageHolder(IHTMLFile file, String ip, Instant instant) {
            this.file = file;
            this.instant = instant;
            this.ip = ip;
        }
    }
    
    private static class ManagerMonitor implements Runnable {

        @Override
        public void run() {
            Instant now = Instant.now();
            synchronized(pageMap) {
                Set<Entry<String, PageHolder>> pageSet = pageMap.entrySet();
                pageSet.forEach((entry) -> {
                    Instant start = entry.getValue().instant;
                    if (start.plus(lifetime).isBefore(now)) {
                        pageSet.remove(entry);
                        LOG.log(Level.INFO, "Grading page with UUID ''{0}'' has expired.", entry.getKey());
                    }
                });
            }
        }
    }

}
