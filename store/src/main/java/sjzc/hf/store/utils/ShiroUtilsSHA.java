package sjzc.hf.store.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
/**
 * Shiro工具类
 * 
 * @author asiainfo
 *  
 * @date 2016年11月12日 上午9:49:19
 */
public class ShiroUtilsSHA {
	/**  加密算法 */
	public final static String hashAlgorithmName = "SHA-256";
	/**  循环次数 */
	public final static int hashIterations = 16;

	public static String sha256(String password, String salt) {
		return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
	}
}
