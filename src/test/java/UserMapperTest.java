import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import xsy.smbms.mapper.user.UserMapper;

/**
 * Created by Administrator on 2020/6/29.
 */
public class  UserMapperTest{

    @Test
    public void test2() throws Exception {
        ApplicationContext context =
                new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        UserMapper userMapper = context.getBean("userMapper", UserMapper.class);
        int count = userMapper.getUserCount("李明", 2);
        System.out.println(count);
    }

}