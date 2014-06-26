package server.com.webHandler.pages.helpers;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.validator.routines.InetAddressValidator;
import server.htmlBuilder.IHTMLFile;

public class GradePageManager {

    private static final Logger LOG = Logger.getLogger(GradePageManager.class.getName());

    private static final Duration lifetime;
    private static final Map<String, PageHolder> pageMap;

    static {
        pageMap = Collections.synchronizedMap(new HashMap<>(10));
        lifetime = Duration.ofMinutes(1);
        ScheduledExecutorService sec = Executors.newSingleThreadScheduledExecutor();
        //ScheduledThreadPoolExecutor sec = new ScheduledThreadPoolExecutor(1);
        sec.scheduleAtFixedRate(new ManagerMonitor(), 60, 60, TimeUnit.SECONDS);
    }

    public static String add(IHTMLFile page, String ip) {
        Objects.requireNonNull(page, "The added grade page cannot be null.");
        String uuid = UUID.randomUUID().toString();
        //System.out.println(uuid + ", " + ip);
        synchronized (pageMap) {
            pageMap.put(uuid, new PageHolder(page, ip));
        }
        LOG.log(Level.INFO, "Grading page with UUID ''{0}'' has been added.", uuid);
        return uuid;
    }

    public static Optional<IHTMLFile> get(String key) {
        Objects.requireNonNull(key, "Grade page UUID cannot be null.");
        PageHolder holder;
        synchronized (pageMap) {
            holder = pageMap.get(key);
            if (holder == null) {
                return Optional.empty();
            }
            return Optional.of(holder.file);
        }
    }

    public static Optional<String> getIP(String key) {
        Objects.requireNonNull(key, "Grade page UUID cannot be null.");
        synchronized (pageMap) {
            PageHolder pageHolder = pageMap.get(key);
            if (pageHolder != null) {
                return pageHolder.ip;
            } else {
                return Optional.empty();
            }
        }
    }

    public static Optional<Instant> getTimestamp(String key) {
        PageHolder holder;
        synchronized (pageMap) {
            holder = pageMap.get(key);
            if (holder == null) {
                return Optional.empty();
            }
            return Optional.of(holder.instant);
        }

    }

    public static boolean purge() {
        synchronized (pageMap) {
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

    public static boolean refresh(String key) {
        Objects.requireNonNull(key, "Grade page UUID cannot be null.");
        synchronized (pageMap) {
            PageHolder pageHolder = pageMap.get(key);
            if (pageHolder != null) {
                pageHolder.instant = Instant.now();
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean update(String key, IHTMLFile page) {
        Objects.requireNonNull(key, "Grade page UUID cannot be null.");
        Objects.requireNonNull(page, "The updated grade page cannot be null.");
        synchronized (pageMap) {
            PageHolder pageHolder = pageMap.get(key);
            if (pageHolder != null) {
                pageHolder.file = page;
                return true;
            } else {
                return false;
            }
        }
    }

    private GradePageManager() {
    }

    private static class ManagerMonitor implements Runnable {

        @Override
        public void run() {
            final Instant now = Instant.now();
            synchronized (pageMap) {
                Iterator<Entry<String, PageHolder>> pageIter = pageMap.entrySet().iterator();
                while (pageIter.hasNext()) {
                    Entry<String, PageHolder> entry = pageIter.next();
                    final Instant start = entry.getValue().instant;
                    if (start.plus(lifetime).isBefore(now)) {
                        pageIter.remove();
                        LOG.log(Level.INFO, "Grading page with UUID ''{0}'' has expired.", entry.getKey());
                    }
                }
            }
        }
    }

    private static class PageHolder {

        IHTMLFile file;
        Instant instant;
        final Optional<String> ip;

        PageHolder(IHTMLFile file, String ip) {
            this(file, ip, Instant.now());
        }

        PageHolder(IHTMLFile file, String ip, Instant instant) {
            this.file = file;
            this.instant = instant;
            if (InetAddressValidator.getInstance().isValid(ip)) {
                this.ip = Optional.empty();
            } else {
                this.ip = Optional.of(ip);
            }
        }
    }

}
