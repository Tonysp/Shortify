package io.minimum.minecraft.shortify;

import io.minimum.minecraft.shortify.util.ShortifyUtility;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class EventHandlerTest
{

    @Test
    public void messageTest()
    {
        String link = "http://google.com/search?q=shortify+bukkit+plugin";
        assertTrue(ShortifyUtility.shortenAll(link, 0, new PassThroughShortener()).equals(link));

        String linkWithMessage = "http://google.com/search?q=shortify+bukkit+plugin check it out";
        assertTrue(ShortifyUtility.shortenAll(linkWithMessage, 0, new PassThroughShortener()).equals(linkWithMessage));
    }

}
