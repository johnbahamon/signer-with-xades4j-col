package com.jorgcastellano.xadessigner;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.net.URL;
import java.net.MalformedURLException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xades4j.algorithms.EnvelopedSignatureTransform;
import xades4j.production.DataObjectReference;
import xades4j.production.XadesEpesSigningProfile;
import xades4j.production.SignatureAppendingStrategies;
import xades4j.production.XadesSigner;
import xades4j.production.SignedDataObjects;
import xades4j.production.XadesSigningProfile;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.ObjectIdentifier;
import xades4j.properties.SignaturePolicyBase;
import xades4j.properties.SignaturePolicyImpliedProperty;
import xades4j.properties.SignaturePolicyIdentifierProperty;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.SignaturePolicyInfoProvider;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;

public class Signer {

	public void sign(String keyPath, String password, String xmlInPath, String xmlOutPath, final String policyUrl) {

        try {
            
            SignaturePolicyInfoProvider policyInfoProvider = new SignaturePolicyInfoProvider() {
                public SignaturePolicyBase getSignaturePolicy() {
                    try {
                        return new SignaturePolicyIdentifierProperty( new ObjectIdentifier( policyUrl ), new URL( policyUrl ).openStream() );
                    } catch (MalformedURLException ex) {
                        return new SignaturePolicyImpliedProperty();
                    } catch (IOException ex) {
                        return new SignaturePolicyImpliedProperty();
                    }
                }
            };

            KeyingDataProvider kp = new FileSystemKeyStoreKeyingDataProvider(
                    "pkcs12",
                    keyPath,
                    new FirstCertificateSelector(),
                    new DirectPasswordProvider(password),
                    new DirectPasswordProvider(password),
                    false
            );

            XadesSigningProfile p = new XadesEpesSigningProfile(kp, policyInfoProvider);

            // open file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new File(xmlInPath));

            // Se establece el punto donde se requiere la firma (segundo elemento ext:ExtensionContent del XML)
            NodeList tag = doc.getElementsByTagName("ext:ExtensionContent");
            Node elemToSign = tag.item(1); // encuentra el nodo en la lista anterior

            // Crea un DataObject del xml para firmar
            DataObjectDesc dataObjRef = new DataObjectReference("").withTransform(new EnvelopedSignatureTransform());

            // Firmo
            XadesSigner signer = p.newSigner();
            signer.sign(new SignedDataObjects( dataObjRef ), elemToSign, SignatureAppendingStrategies.AsFirstChild);

            // Transformo, creo archivo destino con la firma
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(xmlOutPath);
            Source input = new DOMSource(doc);
            
            transformer.transform(input, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

//	public void sign_nota_credito(String keyPath, String password, String xmlInPath, String xmlOutPath) {
//		KeyingDataProvider kp;
//        try {
//
//            SignaturePolicyInfoProvider policyInfoProvider = new SignaturePolicyInfoProvider() {
//                public SignaturePolicyBase getSignaturePolicy() {
//                    return new SignaturePolicyIdentifierProperty(
//                            new ObjectIdentifier("https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica"),
//                            new ByteArrayInputStream("Politica de Factura Digital".getBytes()));
//                }
//            };
//
//            kp = new FileSystemKeyStoreKeyingDataProvider(
//                       "pkcs12",
//                       keyPath,
//                       new FirstCertificateSelector(),
//                       new DirectPasswordProvider(password),
//                       new DirectPasswordProvider(password),
//                       false);
//
//            // SignaturePolicyInfoProvider spi = new
//            XadesSigningProfile p = new XadesEpesSigningProfile(kp, policyInfoProvider);
//            //p.withBasicSignatureOptionsProvider(new SignatureOptionsProvider());
//
//            // open file
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            factory.setNamespaceAware(true);
//            DocumentBuilder builder = null;
//            builder = factory.newDocumentBuilder();
//            Document doc1 = builder.parse(new File(xmlInPath));
//            Element elemToSign = doc1.getDocumentElement();
//
//
//            XadesSigner signer = p.newSigner();
//
//            new Enveloped(signer).sign(elemToSign);
//
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            Result output = new StreamResult(xmlOutPath);
//            Source input = new DOMSource(doc1);
//
//            transformer.transform(input, output);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//	}
//

//	public void sign_nota_debito(String keyPath, String password, String xmlInPath, String xmlOutPath) {
//		KeyingDataProvider kp;
//        try {
//
//            SignaturePolicyInfoProvider policyInfoProvider = new SignaturePolicyInfoProvider() {
//                public SignaturePolicyBase getSignaturePolicy() {
//                    return new SignaturePolicyIdentifierProperty(
//                            new ObjectIdentifier("https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaDebitoElectronica"),
//                            new ByteArrayInputStream("Politica de Factura Digital".getBytes()));
//                }
//            };
//
//            kp = new FileSystemKeyStoreKeyingDataProvider(
//                       "pkcs12",
//                       keyPath,
//                       new FirstCertificateSelector(),
//                       new DirectPasswordProvider(password),
//                       new DirectPasswordProvider(password),
//                       false);
//
//            // SignaturePolicyInfoProvider spi = new
//            XadesSigningProfile p = new XadesEpesSigningProfile(kp, policyInfoProvider);
//            //p.withBasicSignatureOptionsProvider(new SignatureOptionsProvider());
//
//            // open file
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            factory.setNamespaceAware(true);
//            DocumentBuilder builder = null;
//            builder = factory.newDocumentBuilder();
//            Document doc1 = builder.parse(new File(xmlInPath));
//            Element elemToSign = doc1.getDocumentElement();
//
//
//            XadesSigner signer = p.newSigner();
//
//            new Enveloped(signer).sign(elemToSign);
//
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            Result output = new StreamResult(xmlOutPath);
//            Source input = new DOMSource(doc1);
//
//            transformer.transform(input, output);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//	}
}



