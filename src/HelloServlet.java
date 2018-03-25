import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ms on 2018. 3. 25..
 */
public class HelloServlet extends HttpServlet {
    @Override
    public void init() {
        String servletName = getServletName();
        System.out.println(servletName + " 이 초기화 되었습니다\n");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        out.println("Hello World\n");
    }

    @Override
    public void destroy() {
        System.out.println(getServletName() + " 이 종료됩니다. \nbye~");
    }

    //@Override
    //public String getServletName() {
      //  return "HelloServlet!";
    //}
}