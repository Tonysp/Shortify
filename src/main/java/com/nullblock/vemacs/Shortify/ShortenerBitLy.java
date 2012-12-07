package com.nullblock.vemacs.Shortify;

public class ShortenerBitLy implements Shortener {

	private String u, a = "";

	public ShortenerBitLy(String user, String apikey) {
		u = user;
		a = apikey;
	}

	public String getShortenedUrl(String toshort) throws ShortifyException {
		return URLReader.getUrlSimple("http://api.bit.ly/v3/shorten?login=" + u
					+ "&apiKey=" + a + "&longUrl=" + toshort + "&format=txt", "bit.ly");
	}

}