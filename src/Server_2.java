import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by ms on 2018. 3. 25..
 */

/**
 * 요청 body 까지 읽을 수 있는 서버
 */
public class Server_2 {
    private ServerSocket serverSocket;
    private static final byte CR = '\r';
    private static final byte LF = '\n';

    public static void main(String[] args) throws IOException {
        Server_2 server = new Server_2();
        server.boot();
    }

    private void boot() throws IOException {
        serverSocket = new ServerSocket(8000);
        Socket socket = serverSocket.accept();
        InputStream in = socket.getInputStream();
        int oneInt = -1;
        byte oldByte = (byte) -1;
        StringBuilder sb = new StringBuilder();
        int lineNumber = 0;
        boolean bodyFlag = false;
        String method = null;
        String requestUrl = null;
        String httpVersion = null;
        int contentLength = -1;
        int bodyRead = 0;
        List<Byte> bodyByteList = null;
        Map<String, String> headerMap = new HashMap<String, String>();
        while (-1 != (oneInt = in.read())) {
            byte thisByte = (byte) oneInt;

            // header 읽기가 마무리되면 contentLength 만큼 읽어서 list에 add한다.
            if (bodyFlag) {
                bodyRead++;
                bodyByteList.add(thisByte);
                if (bodyRead >= contentLength) {
                    break;
                }
            } else {
                if (thisByte == Server_2.LF && oldByte == Server_2.CR) {
                    String oneLine = sb.substring(0, sb.length() - 1);
                    lineNumber++;
                    if (lineNumber == 1) {
                        // 요청의 첫 행, HTTP 메서드, URL, 버전을 알아낸다.
                        int firstBlank = oneLine.indexOf(" ");
                        int secondBlank = oneLine.lastIndexOf(" ");
                        method = oneLine.substring(0, firstBlank);
                        requestUrl = oneLine.substring(firstBlank + 1, secondBlank);
                        httpVersion = oneLine.substring(secondBlank + 1);
                    } else {
                        // CRLF를 만나면 빈 값이므로 헤더가 끝난다.
                        if (oneLine.length() <= 0) {
                            bodyFlag = true;

                            if ("GET".equals(method)) {
                                break;
                            }

                            String contentLengthValue = headerMap.get("Content-Length");
                            if (contentLengthValue != null) {
                                contentLength = Integer.parseInt(contentLengthValue.trim());
                                bodyFlag = true;
                                bodyByteList = new ArrayList<Byte>();
                            }

                            continue;
                        }

                        int indexOfColon = oneLine.indexOf(":");
                        String headerName = oneLine.substring(0, indexOfColon);
                        String headerValue = oneLine.substring(indexOfColon + 1);
                        headerMap.put(headerName, headerValue);
                    }

                    sb.setLength(0);
                } else {
                    sb.append((char) thisByte);
                }
            }
            oldByte = (byte) oneInt;
        }

        in.close();
        socket.close();

        System.out.printf("METHOD: %s REQ: %s HTTP VER. %s\n", method, requestUrl, httpVersion);
        System.out.println("Header list");
        Set<String> keySet = headerMap.keySet();
        Iterator<String> keyIter = keySet.iterator();

        while (keyIter.hasNext()) {
            String headerName = keyIter.next();
            System.out.printf(" key: %s Value: %s\n", headerName, headerMap.get(headerName));
        }

        if (bodyByteList != null) {
            System.out.print("Message Body -->");
            for (byte oneByte : bodyByteList) {
                System.out.print(oneByte);
            }

            System.out.println("<--");
        }

        System.out.println("End of Http Message.");
    }
}
