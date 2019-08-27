# Firmador JAVA Factura electrónica - DIAN Colombia

Este proyecto realiza el firmado digital de los archivos XMLs de factura electrónica para Colombia.

```
Usage:

java -jar xades-signer-cr sign_factura <keyPath> <keyPassword> <xmlInPath> <xmlOutPath>
java -jar xades-signer-cr sign_nota_debito <keyPath> <keyPassword> <xmlInPath> <xmlOutPath>
java -jar xades-signer-cr sign_nota_credito <keyPath> <keyPassword> <xmlInPath> <xmlOutPath>
```

**sign** firma el xml.

**<keypath>** Ruta de la llave (archivo .p12).

**<keyPassword>** Clave de la llave (archivo .p12).


Le doy las gracias a [tim1991](https://github.com/tim1991) que gracias a su deseo de compartir su proyecto para firmar documentos electrónicos para Costa Rica, me sirvió como base para adaptarlo para la DIAN - Colombia.
