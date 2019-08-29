package com.jorgcastellano.xadessigner;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.net.URL;
import java.net.MalformedURLException;
import java.security.KeyStoreException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import xades4j.XAdES4jException;
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
import xades4j.utils.XadesProfileResolutionException;

public class Signer {

    private XadesSigner signer;
    private String policyUrl = "https://facturaelectronica.dian.gov.co/politicadefirma/v2/politicadefirmav2.pdf";

    private void initialize( String keyPath, String password ) throws KeyStoreException, XadesProfileResolutionException {

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
        signer = p.newSigner();
    }

    private Document loadDocument( String xmlInPath ) throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(xmlInPath));
    }

    private Node selectNode( Document doc ){
        NodeList tag = doc.getElementsByTagName("ext:ExtensionContent");
        return tag.item(1);
    }

    private DataObjectDesc createDataObjectToSign(){
        return new DataObjectReference("").withTransform(new EnvelopedSignatureTransform());
    }

    private void sign( DataObjectDesc dataObjRef, Node elemToSign ) throws XAdES4jException {

        signer.sign(new SignedDataObjects( dataObjRef ), elemToSign, SignatureAppendingStrategies.AsFirstChild);
    }

    private void saveDocument( String xmlOutPath, Document doc ) throws TransformerException {

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(xmlOutPath);
        Source input = new DOMSource(doc);

        transformer.transform(input, output);
    }

	public void sign(String keyPath, String password, String xmlInPath, String xmlOutPath) throws KeyStoreException, ParserConfigurationException, SAXException, IOException, TransformerException, XAdES4jException {

        initialize( keyPath, password );
        Document doc = loadDocument( xmlInPath );
        Node elemToSign = selectNode( doc );
        DataObjectDesc DataObjectRef =  createDataObjectToSign();
        sign( DataObjectRef, elemToSign );
        saveDocument( xmlOutPath, doc );
	}
}



