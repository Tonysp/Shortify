package com.nullblock.vemacs.shortify.common;

import com.nullblock.vemacs.shortify.util.ShortifyUtility;

public class ShortenerIsGd implements Shortener {
    public String getShortenedUrl(String toshort) throws ShortifyException {
        String response = ShortifyUtility.getUrlSimple("http://is.gd/create.php?format=json&url=" + toshort);
        IsGdReply reply = ShortifyUtility.getGson().fromJson(response, IsGdReply.class);

        if (reply.shorturl == null)
        {
            throw new ShortifyException("Error " + reply.errorcode + ": " + reply.errormessage);
        }

        return reply.shorturl;
    }

    private class IsGdReply {
        private String shorturl;
        private int errorcode;
        private String errormessage;
    }
}
