import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/** Khởi động Tomcat 9 nhúng tại http://localhost:8080/ (chỉ dùng để chạy thử). */
public class Launcher {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("port", "8080"));
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(new File("target/tomcat").getAbsolutePath());
        tomcat.setPort(port);
        tomcat.getConnector().setURIEncoding("UTF-8");

        Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        // chạy qua "mvn exec:java": cho ứng dụng web thấy các thư viện (Jasper, MySQL driver) của classpath hiện tại
        ctx.setParentClassLoader(Launcher.class.getClassLoader());
        // cho Tomcat quét @WebServlet trong target/classes
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                new File("target/classes").getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();
        System.out.println("QLST đang chạy tại http://localhost:" + port + "/");
        tomcat.getServer().await();
    }
}
