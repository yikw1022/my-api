package open.api.exception;

/**
 * ClassName: GlobalExceptionHandler
 * Package: org.common.exception
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/3/10 - 11:59
 * @Version: v1.0
 */

import lombok.extern.slf4j.Slf4j;
import open.api.response.ResponseCodeEnum;
import open.api.response.ResultData;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResultData systemExceptionHandler(Exception e){
        log.info("运行时异常：",e);
        return ResultData.fail(ResponseCodeEnum.NEED_LOGIN.getCode(),e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public ResultData systemExceptionHandler(SystemException e){
        log.info("系统异常：",e);
        return ResultData.fail(e.getCode(),e.getMessage());
    }

    /**
     * 参数未传异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultData validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e){
        log.info("参数未传异常：",e);
        printLog(request,e);
        return ResultData.fail(ResponseCodeEnum.SERVER_ERROR.getCode(),e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 捕获SQL异常
     */
    @ExceptionHandler(SQLException.class)
    public ResultData sqlException(HttpServletRequest request, SQLException e){
        printLog(request,e);
        log.error("报SQL异常：{}",e.getMessage());
        return ResultData.fail(ResponseCodeEnum.FAIL.getCode(),e.getMessage());
    }

    /**
     * 打印日志信息
     * @param request
     * @param exception
     */
    private static void printLog(HttpServletRequest request, Exception exception) {
        //换行符
        String lineSeparatorStr = System.getProperty("line.separator");

        StringBuilder exStr = new StringBuilder();
        StackTraceElement[] trace = exception.getStackTrace();
        // 获取堆栈信息并输出为打印的形式
        for (StackTraceElement s : trace) {
            exStr.append("\tat " + s + "\r\n");
        }
        //打印error级别的堆栈日志
        log.error("访问地址：" + request.getRequestURL() + ",请求方法：" + request.getMethod() +
                ",远程地址：" + request.getRemoteAddr() + lineSeparatorStr +
                "错误堆栈信息如下:" + exception.toString() + lineSeparatorStr + exStr);
    }
}
