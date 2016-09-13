package example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OneContextWithTwoTransactionManagerBugTest {

    static FeatureDao featureDao;

    static ClassPathXmlApplicationContext applicationContext;

    @BeforeClass
    public static void setUp() {
       applicationContext = new ClassPathXmlApplicationContext("/applicationContext-test1.xml");
        featureDao = (FeatureDao) applicationContext.getBean("featureDao");
    }

    @AfterClass
    public static void destroy() {
        applicationContext.destroy();
    }
    
    private String testStr;

    @Test
    public void testPull1661() {
       // 
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        testStr="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        System.out.println("try fill memory first"); 
        while (runtime.totalMemory() - runtime.freeMemory()>100){
            try{
            System.out.println("freeMemory:"+runtime.freeMemory()+" totalMemory:"+totalMemory);
            testStr += testStr;
            }catch(java.lang.OutOfMemoryError ex){
                ex.printStackTrace();
                break;
            }
        }
        System.out.println("after OutOfMemoryError then gc"); 
        System.gc();
       featureDao.getOk();
    }
}