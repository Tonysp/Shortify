package io.minimum.minecraft.shortify.common;

import io.minimum.minecraft.shortify.util.ShortifyUtility;

public class ShortenerSafeMn implements Shortener
{
    public String getShortenedUrl(String toshort) throws ShortifyException
    {
        String response = ShortifyUtility.getUrlSimple("http://safe.mn/api/shorten?url=%s&format=json");
        SafeMnReply reply = ShortifyUtility.getGson().fromJson(response, SafeMnReply.class);

        if (reply.error != null)
        {
            throw new ShortifyException("Unable to shorten URL: " + reply.error);
        }

        return reply.url;
    }

    private class SafeMnReply
    {
        private String url;
        private String error;
    }
}
