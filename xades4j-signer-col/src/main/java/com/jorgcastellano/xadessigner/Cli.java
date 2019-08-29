package com.jorgcastellano.xadessigner;

import org.xml.sax.SAXException;
import xades4j.XAdES4jException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.security.KeyStoreException;

public class Cli {

	public static void main(String[] args) throws SAXException, XAdES4jException, TransformerException, KeyStoreException, ParserConfigurationException, IOException {

		if (args.length == 4) {
			Signer signer = new Signer();
			signer.sign(args[0], args[1], args[2], args[3] );
			System.exit(0);
		} else {
			showUsage();
			System.exit(-1);
		}
	}
	public static void showUsage() {
		System.out.println("Usage:");
		System.out.println("java -jar xades-signer-col <keyPath> <keyPassword> <xmlInPath> <xmlOutPath>");
	}
}
