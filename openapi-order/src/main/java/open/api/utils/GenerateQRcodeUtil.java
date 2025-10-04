package open.api.utils;

/**
 * ClassName: GenerateQRcodeUtil
 * Package: com.mall.util
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/6/18 - 16:49
 * @Version: v1.0
 */

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 生成二维码工具类
 */
@Slf4j
public class GenerateQRcodeUtil {

    /**
     * 生成SVG矢量图二维码
     * @param qrCode 支付宝二维码内容
     * @return
     */
    public static String generateQRCode(String qrCode) throws IOException {
        QrConfig qrConfig = QrConfig.create()
                .setHeight(180)
                .setWidth(180)
                .setRatio(8)
                .setErrorCorrection(ErrorCorrectionLevel.H)
                .setMargin(1);
        //转成base64
        BufferedImage image = QrCodeUtil.generate(qrCode, qrConfig);

        //处理二维码
        return handlerQrCode(image);
    }

    /**
     * 处理二维码
     * @param image
     */
    private static String handlerQrCode(BufferedImage image) {
        //输出流
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            return new String("data:image/jpeg;base64," + Base64.encode(stream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成二维码失败");
        }
        return null;
    }

    /**
     * 生成SVG矢量图二维码
     * @param qrCode 支付宝二维码内容
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static String generateQRCode(String qrCode, int width, int height) {
        QrConfig qrConfig = QrConfig.create()
                .setHeight(height)
                .setWidth(width)
                .setRatio(8)
                .setErrorCorrection(ErrorCorrectionLevel.H)
                .setMargin(1);
        //转成base64
        BufferedImage image = QrCodeUtil.generate(qrCode, qrConfig);

        //处理二维码
        return handlerQrCode(image);
    }

    /**
     * 生成SVG矢量图二维码，带Logo
     * @param qrCode 支付宝二维码内容
     * @param filePath logo图片路径
     * @return
     */
    public static String generateQRCode(String qrCode, String filePath) {
        QrConfig qrConfig = QrConfig.create()
                .setImg(filePath)
                .setHeight(180)
                .setWidth(180)
                .setRatio(8)
                .setErrorCorrection(ErrorCorrectionLevel.H)
                .setMargin(1);
        //转成base64
        BufferedImage image = QrCodeUtil.generate(qrCode, qrConfig);

        //处理二维码
        return handlerQrCode(image);
    }

    /**
     * 生成SVG矢量图二维码，带Logo
     * @param qrCode 支付宝二维码内容
     * @param filePath logo图片路径
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static String generateQRCode(String qrCode, String filePath, int width, int height) {
        QrConfig qrConfig = QrConfig.create()
                .setImg(filePath)
                .setHeight(height)
                .setWidth(width)
                .setRatio(8)
                .setErrorCorrection(ErrorCorrectionLevel.H)
                .setMargin(1);
        //转成base64
        BufferedImage image = QrCodeUtil.generate(qrCode, qrConfig);

        //处理二维码
        return handlerQrCode(image);
    }
}
