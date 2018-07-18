

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 08:20
 */
public class HttpTest {

    public static void main(String[] args) {

        Method[] methods = HttpTest.class.getMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                System.out.println(parameter.getName());
            }
        }

    }


}
