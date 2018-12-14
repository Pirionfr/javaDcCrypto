
[![Build Status](https://travis-ci.org/Pirionfr/goDcCrypto.svg?branch=master)](https://travis-ci.org/Pirionfr/goDcCrypto)

goDcCrypto
==========

encrypt/decrypt lookatch agent message

Build
-----

You can also download the source code and install it manually:

    mvn package


Usage
-----
encrypt your message

    java -jar crypto-1.0-jar-with-dependencies.jar -e -k <master key> -m <message>
    
decrypt your message

    java -jar crypto-1.0-jar-with-dependencies.jar -d -k <master key> -m <message>
