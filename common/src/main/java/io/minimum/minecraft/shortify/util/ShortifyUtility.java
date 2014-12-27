package io.minimum.minecraft.shortify.util;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import io.minimum.minecraft.shortify.common.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortifyUtility
{
    private static final Pattern URL_PATTERN = Pattern
            .compile("((mailto\\:|(news|(ht|f)tp(s?))\\://){1}\\S+)");
    private static final Gson gson = new Gson();

    public static Gson getGson()
    {
        return gson;
    }

    public static ShortenerManager setupShorteners()
    {
        ShortenerManager sm = new ShortenerManager();
        sm.registerShortener("isgd", new ShortenerIsGd());
        sm.registerShortener("niggr", new ShortenerNigGr());
        sm.registerShortener("safemn", new ShortenerSafeMn());
        sm.registerShortener("tinyurl", new ShortenerTinyUrl());
        sm.registerShortener("yu8me", new ShortenerYu8Me());
        return sm;
    }

    public static void reloadConfigShorteners(ShortenerManager sm, CommonConfiguration c)
    {
        sm.unregisterShortener("bitly");
        sm.registerShortener("bitly", new ShortenerBitLy(
                c.getString("bitlyUSER"), c.getString("bitlyAPI")));
        sm.unregisterShortener("yourls");
        sm.registerShortener("yourls", new ShortenerYourls(
                c.getString("yourlsURI"), c.getString("yourlsUSER"),
                c.getString("yourlsPASS")));
        sm.unregisterShortener("googl");
        sm.registerShortener("googl", new ShortenerGooGl(
                c.getString("googAPI")));
    }

    public static String getUrlSimple(String uri)
            throws ShortifyException
    {
        try (InputStream is = new URL(uri).openStream())
        {
            return new String(ByteStreams.toByteArray(is));
        }
        catch (IOException ex)
        {
            throw new ShortifyException("Unable to fetch URL", ex);
        }
    }

    /**
     * Shorten all URLs in a String.
     *
     * @throws ShortifyException
     */
    public static String shortenAll(String txt, int minln, Shortener shortener) throws ShortifyException
    {
        // From Daring Fireball
        Matcher m = URL_PATTERN.matcher(txt);

        // TODO Replace this with StringBuilder
        StringBuffer sb = new StringBuffer();
        while (m.find())
        {
            final String urlTmp = m.group(1);
            String modified = urlTmp;
            if (urlTmp.length() >= minln)
            {
                try
                {
                    modified = shortener.getShortenedUrl(java.net.URLEncoder
                            .encode(urlTmp, "UTF-8"));

                    if (modified == null)
                        throw new ShortifyException("Shortener returned null for " + urlTmp);

                    // might as well put the encoder in the listener to
                    // prevent possible injections
                }
                catch (UnsupportedEncodingException e1)
                {
                    // do absolutely nothing
                }
            }
            m.appendReplacement(sb, "");
            sb.append(modified);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static void verifyConfiguration(CommonConfiguration c, Logger l)
    {
        if (c.getString("shortener").equals("bitly")
                && (c.getString("bitlyUSER").equals("none") || c.getString(
                "bitlyAPI").equals("none")))
        {
            l.info("bit.ly is not properly configured in config.yml.");
            l.info("Reverting to default shortener is.gd.");
            c.set("shortener", "isgd");
        }
        if (c.getString("shortener").equals("yourls")
                && (c.getString("yourlsUSER").equals("none")
                || c.getString("yourlsURI").equals("none") || c
                .getString("yourlsPASS").equals("none")))
        {
            l.info("YOURLS is not properly configured in config.yml.");
            l.info("Reverting to default shortener is.gd.");
            c.set("shortener", "isgd");
        }
        if (c.getString("shortener").equals("googl")
                && c.getString("googAPI").equals("none"))
        {
            l.info("goo.gl is not properly configured in config.yml.");
            l.info("Reverting to default shortener is.gd.");
            c.set("shortener", "isgd");
        }
    }

    public static CommonConfiguration loadCfg(File pl)
    {
        CommonConfiguration c = new CommonConfiguration();
        c.addDefault("mode", "replace");
        c.addDefault("shortener", "isgd");
        c.addDefault("prefix", "&n");
        c.addDefault("minlength", "20");
        c.addDefault("googAPI", "none");
        c.addDefault("bitlyUSER", "none");
        c.addDefault("bitlyAPI", "none");
        c.addDefault("yourlsURI", "none");
        c.addDefault("yourlsUSER", "none");
        c.addDefault("yourlsPASS", "none");

        File dataDir = new File(pl.getParent() + "/Shortify");
        File cfg = new File(dataDir, "config.yml");
        try
        {
            dataDir.mkdirs();
            if (!cfg.exists())
            {
                cfg.createNewFile();
                c.dumpYaml(cfg);
            } else
            {
                c.loadYaml(cfg);
                c.mergeDefaults();
                c.dumpYaml(cfg);
            }
        }
        catch (IOException ignored)
        {
        }
        return c;
    }

    public static void dumpData(File pl, CommonConfiguration c)
    {
        File dataDir = new File(pl.getParent() + "/Shortify");
        try
        {
            new File(dataDir, "config.yml").createNewFile();
            c.dumpYaml(new File(dataDir, "config.yml"));
        }
        catch (IOException e1)
        {
            // Ignore
        }
    }
}
