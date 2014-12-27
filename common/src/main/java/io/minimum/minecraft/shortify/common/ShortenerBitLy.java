package io.minimum.minecraft.shortify.common;

import io.minimum.minecraft.shortify.util.ShortifyUtility;

public class ShortenerBitLy implements Shortener
{

    private String username, key = "";

    public ShortenerBitLy(String user, String apikey)
    {
        username = user;
        key = apikey;
    }

    public String getShortenedUrl(String toshort) throws ShortifyException
    {
        if (username.equals("none") || key.equals("none"))
        {
            throw new ShortifyException("No API username/key");
        }
        return ShortifyUtility.getUrlSimple(
                "http://api.bit.ly/v3/shorten?login=" + username + "&apiKey=" + key
                        + "&longUrl=" + toshort + "&format=txt");
    }

}
