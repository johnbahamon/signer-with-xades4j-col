package com.jorgcastellano.xadessigner;

public class Cli {

	public static void main(String[] args) {

		// sign
		String keyPath = null;
		String keyPassword = null;
		String xmlInPath = null;
		String xmlOutPath = null;
		String politicyUrl = null;

		if ("sign_factura".equals(args[0])) {
			keyPath = args[1];
			keyPassword = args[2];
			xmlInPath = args[3];
			xmlOutPath = args[4];
			politicyUrl = "https://facturaelectronica.dian.gov.co/politicadefirma/v2/politicadefirmav2.pdf";
			Signer signer = new Signer();
			signer.sign(keyPath, keyPassword, xmlInPath, xmlOutPath, politicyUrl);
			System.exit(0);
//		} else if ("sign_nota_credito".equals(action)) {
//			keyPath = args[1];
//			keyPassword = args[2];
//			xmlInPath = args[3];
//			xmlOutPath = args[4];
//			Signer signer = new Signer();
//			signer.sign_nota_credito(keyPath, keyPassword, xmlInPath, xmlOutPath);
//			System.exit(0);
//		} else if ("sign_nota_debito".equals(action)) {
//			keyPath = args[1];
//			keyPassword = args[2];
//			xmlInPath = args[3];
//			xmlOutPath = args[4];
//			Signer signer = new Signer();
//			signer.sign_nota_debito(keyPath, keyPassword, xmlInPath, xmlOutPath);
//			System.exit(0);
		} else {
			showUsage();
			System.exit(-1);
		}
	}
	public static void showUsage() {
		System.out.println("Usage:");
		System.out.println("java -jar xades-signer-col sign_factura <keyPath> <keyPassword> <xmlInPath> <xmlOutPath>");
//		System.out.println("java -jar xades-signer-col sign_nota_debito <keyPath> <keyPassword> <xmlInPath> <xmlOutPath>");
//		System.out.println("java -jar xades-signer-col sign_nota_credito <keyPath> <keyPassword> <xmlInPath> <xmlOutPath>");
	}
}
