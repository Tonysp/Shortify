package com.nullblock.vemacs.shortify;

import com.nullblock.vemacs.shortify.common.Shortener;
import com.nullblock.vemacs.shortify.common.ShortifyException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PassThroughShortener implements Shortener
{
    @Override
    public String getShortenedUrl(String toshort) throws ShortifyException
    {
        // This is a very simple handler where we decode the URL back as we
        // are not testing the shorteners.
        try
        {
            return URLDecoder.decode(toshort, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new AssertionError(e);
        }
    }
}
