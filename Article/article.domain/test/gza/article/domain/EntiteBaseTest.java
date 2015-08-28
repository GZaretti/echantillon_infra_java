package gza.article.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;


public class EntiteBaseTest {

    private EntiteBase entite1;
    private EntiteBase entite2;
    private EntiteBase entite11;
    private EntiteBase entiteIdNull;

    public EntiteBaseTest() {
    }

    @Before
    public void setup() {
        entite1 = new EntiteBase(1L) {

            @Override
            public void update(Object entite) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        entiteIdNull = new EntiteBase() {

            @Override
            public void update(Object entite) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        entite11 = new EntiteBase(1L) {

            @Override
            public void update(Object entite) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        entite2 = new EntiteBase(2L) {

            @Override
            public void update(Object entite) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

    }

    @Test
    public void testEquals() {
        Assert.assertNotSame(entite1, entite11);
        Assert.assertEquals(entite1, entite11);
    }

    @Test
    public void testNotEquals() {
        Assert.assertFalse(entite1.equals(entiteIdNull));
        Assert.assertFalse(entite1.equals(null));
        Assert.assertFalse(entite1.equals(new Object()));
        Assert.assertFalse(entite1.equals(entite2));
    }

    @Test
    public void testEqualsHashCode() {
        Assert.assertNotSame(entite1, entite11);
        Assert.assertEquals(entite1.hashCode(), entite11.hashCode());
    }

    @Test
    public void testNotEqualsHashCode() {
        Assert.assertFalse(entite1.hashCode() == entiteIdNull.hashCode());
        Assert.assertFalse(entite1.hashCode() == entite2.hashCode());
    }

}
