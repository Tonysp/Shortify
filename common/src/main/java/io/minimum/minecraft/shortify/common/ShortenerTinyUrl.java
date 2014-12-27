package io.minimum.minecraft.shortify.common;

import io.minimum.minecraft.shortify.util.ShortifyUtility;

public class ShortenerTinyUrl implements Shortener
{
    public String getShortenedUrl(String toshort) throws ShortifyException
    {
        return ShortifyUtility.getUrlSimple(
                "http://tinyurl.com/api-create.php?url=" + toshort);
    }
}
