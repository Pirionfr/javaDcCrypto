package org.dc.crypto;


import org.dc.exception.CryptoException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by pirion on 27/06/17.
 */
public class CryptoTest {


    @Test
    public void cryptString() throws CryptoException {
        Crypto crypto = new Crypto("Boo");
        crypto.init();
        Assert.assertEquals("Foo",
            crypto.decryptString(crypto.encryptString("Foo"))
        );
    }

    @Test
    public void cryptStringEmptyKey() throws CryptoException {
        Crypto crypto = new Crypto("");
        crypto.init();
        Assert.assertEquals("Foo",
            crypto.decryptString(crypto.encryptString("Foo"))
        );
    }

    @Test
    public void cryptStringLongText() throws CryptoException {
        Crypto crypto = new Crypto("Bar");
        crypto.init();
        Assert.assertEquals("Long input with more than 16 characters",
            crypto.decryptString(crypto.encryptString("Long input with more than 16 characters"))
        );
    }



    @Test(expected = CryptoException.class)
    public void decryptStringbase64Detection() throws CryptoException {
        Crypto crypto = new Crypto("Bar");
        crypto.init();
        crypto.decryptString("Long input with more than 16 characters");
    }

    @Test
    public void isBase64EncodedCheck()
    {
        Assert.assertTrue(Crypto.isBase64Encoded("A3xNe7sEB9HixkmBhVrYaF6f_Q=="));
        Assert.assertFalse(Crypto.isBase64Encoded("Long input with more than 16 characters"));
    }

}
