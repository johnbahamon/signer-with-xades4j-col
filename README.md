# Firmador JAVA Facturación electrónica - DIAN Colombia

Este proyecto realiza el firmado criptografico utilizando la API [xades4j](https://github.com/luisgoncalves/xades4j) para los archivos XMLs de facturacion electrónica adaptados para la DIAN Colombia.

```
Usage:

java -jar firmador.jar <certicatePath> <certificatePassword> <xmlInPath> <xmlOutPath>
```

\<certicatePath\> Ruta del certificado (archivo .p12)

\<certificatePassword\> Clave de la llave (archivo .p12)
  
\<xmlInPath\> Ruta de archivo XML
  
\<xmlOutPath\> Ruta de salida del archivo firmado.

Le doy las gracias a [tim1991](https://github.com/tim1991) por su deseo de compartir su proyecto para firmar documentos electrónicos para Costa Rica, me sirvió como base para adaptarlo para la DIAN - Colombia, ademas de leer mucho los comentarios y [aportes de la comunidad de la API xades4j](https://github.com/luisgoncalves/xades4j/issues/134)
