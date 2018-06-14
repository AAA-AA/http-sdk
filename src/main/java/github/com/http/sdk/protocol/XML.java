/**
 */
package github.com.http.sdk.protocol;



import github.com.http.sdk.utils.Clean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * If less checked exception. you can try {@link Clean#u
 *
 * @author ducer
 */
public final class XML {

    public static String toString(Object bean) throws Exception {
        byte[] bts = marshal(bean);
        return Clean.toString(bts, "UTF-8");
    }

    public static byte[] marshal(Object bean) throws Exception {
        JAXBContext context = JAXBCache.instance.getJAXBContext(bean.getClass());
        Marshaller marshaller = context.createMarshaller();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.marshal(bean, bos);
        byte[] result = bos.toByteArray();
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(byte[] data, Class<T> clazz) throws Exception {
        JAXBContext context = JAXBCache.instance.getJAXBContext(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Object bean = unmarshaller.unmarshal(bis);
        return (T) bean;
    }

    public static <T> T unmarshal(InputStream input, Class<T> clazz) throws Exception {
        byte[] data = Clean.toByteArray(input);
        return unmarshal(data, clazz);
    }

    private enum JAXBCache {
        instance;
        private final ConcurrentMap<String, JAXBContext> cache = new ConcurrentHashMap<>();

        JAXBContext getJAXBContext(Class<?> clazz) throws JAXBException {
            JAXBContext context = cache.get(clazz.getName());
            if (null == context) {
                context = JAXBContext.newInstance(clazz);
                cache.putIfAbsent(clazz.getName(), context);
            }
            return context;
        }
    }
}
