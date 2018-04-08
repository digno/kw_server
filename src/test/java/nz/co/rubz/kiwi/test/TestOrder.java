package nz.co.rubz.kiwi.test;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestOrder {
    private static final Logger LOG = Logger.getLogger(TestOrder.class);
    @Test
    public void test1First() throws Exception {
        LOG.info("------1--------");
    }

    @Test
    public void test3Second() throws Exception {
        LOG.info("------2--------");

    }

    @Test
    public void test2Third() throws Exception {
        LOG.info("------3--------");
    }
    
    @Test
    public void test(){
    	int a  = 0;
    	Integer b = new Integer(0);
    	System.out.println(a==b);
    	System.out.println(b.equals(a));
    }
    
}
/*
output:
2014-03-31 16:04:15,984 0    [main] INFO  - ------1--------
2014-03-31 16:04:15,986 2    [main] INFO  - ------3--------
2014-03-31 16:04:15,987 3    [main] INFO  - ------2--------
*/