package cn.wjdiankong.demo;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;  

public class Main {
	
	/**
	 * 设备的salt值，可以利用反射获取，也可以去/data/system/locksettings.db数据库中查看
	 * 需要注意的是数据库中保存的是long类型，这里需要进行hex转化
	 */
	private final static String SALT = Long.toHexString(-9167742676506383495l);

	public static void main(String[] args) {  
		
		//手势密码模拟
		List<LockPatternView.Cell> pattern = new ArrayList<LockPatternView.Cell>();
		LockPatternView.Cell cell1 = new LockPatternView.Cell(0, 0);
		pattern.add(cell1);
		LockPatternView.Cell cell2 = new LockPatternView.Cell(0, 3);
		pattern.add(cell2);
		LockPatternView.Cell cell3 = new LockPatternView.Cell(0, 6);
		pattern.add(cell3);
		LockPatternView.Cell cell4 = new LockPatternView.Cell(0, 7);
		pattern.add(cell4);
		LockPatternView.Cell cell5 = new LockPatternView.Cell(0, 8);
		pattern.add(cell5);
		
		System.out.println("pattern:"+toHex(patternToHash(pattern)));
		
		System.out.println("passwordinfo:"+new String(passwordToHash("3721")));
		
		System.out.println("salt:"+SALT);
		
	}  
	
	/**
	 * 手势密码加密算法
	 * @param pattern
	 * @return
	 */
	public static byte[] patternToHash(List<LockPatternView.Cell> pattern) {
        if (pattern == null) {
            return null;
        }

        int patternSize = pattern.size();
        byte[] res = new byte[patternSize];
        for (int i = 0; i < patternSize; i++) {
            LockPatternView.Cell cell = pattern.get(i);
            res[i] = (byte) (cell.row * 3 + cell.column);
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(res);
            return hash;
        } catch (Exception nsa) {
        }
        return null;
    }
	
	/**
	 * 输入密码加密算法
	 * @param password
	 * @return
	 */
	public static byte[] passwordToHash(String password) {
        if (password == null) {
            return null;
        }
        byte[] hashed = null;
        try {
            byte[] saltedPassword = (password + SALT).getBytes();
            byte[] sha1 = MessageDigest.getInstance("SHA-1").digest(saltedPassword);
            byte[] md5 = MessageDigest.getInstance("MD5").digest(saltedPassword);
            hashed = (toHex(sha1) + toHex(md5)).getBytes();
        } catch (Exception e) {
        }
        return hashed;
    }
	
	private static String toHex(byte[] ary) {
        final String hex = "0123456789ABCDEF";
        String ret = "";
        for (int i = 0; i < ary.length; i++) {
            ret += hex.charAt((ary[i] >> 4) & 0xf);
            ret += hex.charAt(ary[i] & 0xf);
        }
        return ret;
    }

}
