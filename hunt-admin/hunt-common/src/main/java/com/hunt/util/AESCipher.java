package com.hunt.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.shiro.SecurityUtils;
import org.junit.Test;

/**
 * AES Cipher
 *
 * Created by jiangning on 2016/12/4.
 */

public class AESCipher {
    private static final String TAG = "ZiCOO.AESCipher";

    private static final String ENT_HEADER = "IR2ENTCONTACTS01";
    private static final String charset = "UTF-8";
    private static final String IV_STRING = "A-16-Byte-String";
//    private static final String IV_STRING = "A-32-Byte-String";


    /**
     * 企业通讯录文件加密
     * @param srcf      企业通讯录原文件
     * @param aesf      加密结果文件
     * @param passwd    密码
     * @return
     * @throws IOException
     */
    public static int encryptPadding(String srcf, String aesf, String passwd)
            throws IOException {
        InputStream in = new FileInputStream(srcf);
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(aesf), false));
        byte buffer[] = new byte[6*256-16];//bytes=6*256 => base64=8*256

        try {
        	
            String key = getMD5(passwd,false);
//            String key = StringUtil.createPassword(passwd, null, 0);//getMD5(passwd,false);
            System.out.println("key-->"+key);
//            String key = passwd;
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(charset), "AES");

            byte[] initParam = IV_STRING.getBytes(charset);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            //byte[] encb = cipher.doFinal(srcf.getBytes(charset));
            //String rets = Base64.encode(encb);
            //Util.e(TAG, "KeyMD5="+key+", Cipher result="+rets);
            bw.write(ENT_HEADER);

            int rsiz, retw=16;
            while ((rsiz=in.read(buffer))>0) {
                byte[] result;
                if (rsiz<buffer.length) {
                    int fz = rsiz & 0xf;
                    if (fz>0) fz = 16-fz;
                    byte[] rbuf = new byte[rsiz+fz];
                    System.arraycopy(buffer, 0, rbuf, 0, rsiz);
                    if (fz>0) {
                        System.arraycopy(buffer, rsiz, ",,,,,,,,,,,,,,,".getBytes(), 0, fz);
                        //Util.e(TAG, "Block size: "+rsiz+"+"+fz+"="+rbuf.length);
                    }
                    result = cipher.doFinal(rbuf);
                }
                else result = cipher.doFinal(buffer);
//                String data = Base64.encode(result);
                String data = base64Encode(result);
                bw.write(data);
                retw += data.length();
                //Util.d(TAG, "aesCipher "+rsiz+" >> "+result.length+"/"+data.length()+", total "+retw);
            }
            bw.flush();
            return retw;
        }
  
        catch (Exception e) { 
        	e.printStackTrace();
        }
        finally {
            in.close();  bw.close();
        }
        return 0;
    }

    /**
     * 企业通讯录文件解密
     * @param aesf      加密的文件名
     * @param passwd    密码
     * @return
     * @throws IOException
     */
    public static byte[] decryptPadding(String aesf, String passwd) throws IOException {
        byte buffer[] = new byte[8*256];//Base64=8*256 => bytes=6*256
        InputStream in = new FileInputStream(aesf);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            String key =getMD5(passwd,false);
//            String key =passwd;
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(charset), "AES");

            byte[] initParam = IV_STRING.getBytes(charset);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            byte[] hdr = new byte[16];
            in.read(hdr, 0, 16);
            if (!ENT_HEADER.equals(new String(hdr))) {
                //Util.e(TAG, "Error Header: "+(new String(hdr)));
                return null;
            }

            int rsiz;//, retw=0;
            while ((rsiz=in.read(buffer))>0) {
                byte[] data, result;
                if (rsiz<buffer.length) {
                    byte[] rbuf = new byte[rsiz];
                    System.arraycopy(buffer, 0, rbuf, 0, rsiz);
//                    data = Base64.decode(rbuf);
                    data = base64Decode(rbuf);
                }
                else
//                    data = Base64.decode(buffer);
                data = base64Decode(buffer);

                //Util.d(TAG, "cipher.doFinal: data.size="+data.length+", read.size="+rsiz);
                result = cipher.doFinal(data);
                out.write(result, 0, result.length);
                //retw += result.length;
                //Util.d(TAG, "aesDecipher "+rsiz+" >> "+result.length+", total "+retw);
            }
            out.flush();
            return out.toByteArray();
        }
       
        catch (Exception e) { 
//        	e.printStackTrace();
        	return null;
        }
        finally {
        	if(in!=null) {
        		in.close(); 
        	}
        	if(out!=null) {
        		out.close();
        	}
            
        }

    }

    @Test
    public void testEncy() {
    	File file=new File("C:\\out\\20190930172022341.csv");
    	if(file.exists()) {
    		try {
				int encryptPadding = encryptPadding(file.getAbsolutePath(),"C:\\out\\abc.csv","123456");
				
				System.out.println("textEncy-->"+encryptPadding);
				File f2=new File("C:\\out\\abc.csv");
				if(f2.exists()) {
					f2.renameTo(new File("C:\\out\\重命名.csv"));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    }
    /**
     * 加密文件并删除旧文件
     * @param srcPath
     * @param tagFN  加密后目标文件名
     * @param password
     * @return
     */
    public static File escFile(String srcPath,String tagFN, String password) {
    	File file = new File(srcPath);
    	if(file.exists()) {
    		try {
    			
    			String suString=srcPath.substring(0,srcPath.lastIndexOf("\\")+1);
    			String nFileP=suString+tagFN;
				int encryptPadding = encryptPadding(file.getAbsolutePath(),nFileP,password);
				File nFile = new File(nFileP);
				if(nFile.exists()&&encryptPadding>1) {
					file.delete();
					return nFile;
				}else {
					nFile.delete();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    	}
    	return null;
    }
    
    /**
     * 解密文件
     * @param srcPath
     * @param password
     * @return
     */
    public static File deFile(String srcPath,String password) {
    	File file = new File(srcPath);
    	if(file.exists()) {
    		FileOutputStream out=null;
    		try {
				byte[] decryptPadding = decryptPadding(file.getAbsolutePath(),password);
				if(decryptPadding==null) {
					return null;
				}
				String nFp=srcPath.substring(0,srcPath.lastIndexOf("\\")+1);
				nFp=nFp+"temp"+file.getName();
				File deFile = new File(nFp);
				 out= new FileOutputStream(deFile);
				out.write(decryptPadding);
				return deFile;
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(out!=null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
    	}
    	return null;
    }
    
    
    
    @Test
    public void testStr() {
    	File escFile = escFile("C:\\out\\20190930172022341 - 副本.csv","组织部_20190909_20190909152754.csv","123456");
    	System.out.println("--->"+escFile.getAbsolutePath());
    }
    
    @Test
    public void testNStr() {
//    	File deFile = deFile("C:\\out\\组织部_20190909_20190909152754.csv","123456");
    	File deFile = deFile("G:\\web\\picSrc\\胜凯采油管理版本4_20180810_2019101218471771.csv","1111");
    	System.out.println("deFile-->"+deFile.getAbsolutePath());
    }
    
    public static void main(String[] args) {
    	String string="asfsaf\\safsd\\af.csv";
    	String suString=string.substring(0,string.lastIndexOf("\\")+1);
    	System.out.println("suS-->"+suString);
//    	Log.l(AESCipher.class).debug("testStr-->"+suString);
	}
    
    @Test
    public void testDecy() {
    	File file=new File("C:\\out\\重命名.csv");
    	File deFile=new File("C:\\out\\deFile.csv");
    	
    	if(file.exists()) {
    		FileOutputStream out=null;
    		try {
				byte[] decryptPadding = decryptPadding(file.getAbsolutePath(),"123456");
				if(decryptPadding==null) {
					System.out.println("密码错误");
					return;
				}
				 out= new FileOutputStream(deFile);
				out.write(decryptPadding);
				System.out.println("testDecy-->"+decryptPadding.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(out!=null) {
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
    		
    	}
    }
    
    // AES文件加密
//    public static int encrypt128(String srcf, String aesf, String passwd) throws IOException {
//        byte buffer[] = new byte[4*1024];
//        InputStream in = new FileInputStream(srcf);
//        OutputStream out = new FileOutputStream(aesf);
//
//        try {
//            /*KeyGenerator kgen = KeyGenerator.getInstance("AES");
//            kgen.init(128, new SecureRandom(passwd.getBytes()));
//            SecretKey secretKey = kgen.generateKey();
//            byte[] enCodeFormat = secretKey.getEncoded();*/
//
//            byte[] enCodeFormat = Util.MD5(passwd.getBytes());
//            //Util.e(TAG, "Encrypt Key: "+Util.byte2HexString(enCodeFormat, 0, enCodeFormat.length, false));
//
//            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
//            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
//            //	cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
//
//            int rsiz, retw=0;
//            while ((rsiz=in.read(buffer))>0) {
//                byte[] result;
//                if (rsiz<buffer.length) {
//                    int fz = rsiz & 0xf;
//                    if (fz>0) fz = 16-fz;
//                    byte[] rbuf = new byte[rsiz+fz];
//                    System.arraycopy(buffer, 0, rbuf, 0, rsiz);
//                    if (fz>0) {
//                        System.arraycopy(buffer, rsiz, ",,,,,,,,,,,,,,,".getBytes(), 0, fz);
//                        //Util.e(TAG, "Block size: "+rsiz+"+"+fz+"="+rbuf.length);
//                    }
//                    result = cipher.doFinal(rbuf);
//                }
//                else result = cipher.doFinal(buffer);
//                out.write(result, 0, result.length);
//                retw += result.length;
//                //Util.e(TAG, "aesCipher "+rsiz+" >> "+result.length+", total "+retw);
//            }
//            out.flush();
//            return retw;
//        }
//       
//        catch (Exception e) {
//        	e.printStackTrace();
//        }
//        finally {
//            in.close();  out.close();
//        }
//
//        return -1;
//    }

    // AES文件解密
//    public static byte[] decrypt128(String aesf, String passwd) throws IOException {
//        byte buffer[] = new byte[4*1024+16];
//        InputStream in = new FileInputStream(aesf);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            /*KeyGenerator kgen = KeyGenerator.getInstance("AES");
//            kgen.init(128, new SecureRandom(passwd.getBytes()));
//            SecretKey secretKey = kgen.generateKey();
//            byte[] enCodeFormat = secretKey.getEncoded();*/
//            byte[] enCodeFormat = Util.MD5(passwd.getBytes());
//            //Util.e(TAG, "Decrypt Key: "+passwd+" > "+
//            //        Util.byte2HexString(enCodeFormat, 0, enCodeFormat.length, false));
//
//            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
//            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
//
//            int rsiz;//, retw=0;
//            while ((rsiz=in.read(buffer))>0) {
//                byte[] result;
//                if (rsiz<buffer.length) {
//                    byte[] rbuf = new byte[rsiz];
//                    System.arraycopy(buffer, 0, rbuf, 0, rsiz);
//                    result = cipher.doFinal(rbuf);
//                }
//                else result = cipher.doFinal(buffer);
//                out.write(result, 0, result.length);
//                //retw += result.length;
//                //Util.e(TAG, "aesDecipher "+rsiz+" >> "+result.length+", total "+retw);
//            }
//            out.flush();
//            return out.toByteArray();
//            /*byte[] odat = out.toByteArray();
//            int sz = odat.length;
//            for (int i=0; i<(sz+15)/16; i++) {
//                int len = 16;
//                if (sz-i*16<16) len = sz-i*16;
//                Util.e(TAG, Util.byte2HexString(odat, i*16, len, true));
//            }
//            //Util.e(TAG, "odat.size="+odat.length);
//            return odat;*/
//        }
//      
//        catch (Exception e) { 
//        	e.printStackTrace();
//        }
//        finally {
//            in.close();  out.close();
//        }
//
//        return null;
//    }

    /** AES加密/解密
     * @param content	需要加密的内容
     * @param passwd	加密密码
     * @param encrypt	true:加密, false:解密
     * @return
     */
    public static byte[] aesCipher(byte[] content, String passwd, boolean encrypt) {
        try {
            String key = getMD5(passwd,false);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(charset), "AES");

            byte[] initParam = IV_STRING.getBytes(charset);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            if (encrypt) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
                byte[] result = cipher.doFinal(content);
                /*if ((content.length & 0x0f)!=0) {
                    int fz = (16-content.length%0x0f);
                    byte[] aesi = new byte[content.length + fz];
                    System.arraycopy(content, 0, aesi, 0, content.length);
                    System.arraycopy("~~~~~~~~~~~~~~~".getBytes(), 0, aesi, content.length, fz);
                    result = cipher.doFinal(aesi);
                }
                else result = cipher.doFinal(content);*/
                //Util.e(TAG, "result: "+result.length);
//                return Base64.encode(result).getBytes();
                return base64Encode(result).getBytes();
            }
            else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
//                byte[] base64 = Base64.decode(content);
                byte[] base64 = base64Decode(content);
                //Util.e(TAG, "base64: "+base64.length);
                return cipher.doFinal(base64);
            }
        }
        catch (Exception e) { e.printStackTrace(); }

        return null;
    }
    
    public static String getMD5(String str,boolean bite_16) {
        if (str==null) return null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            for (byte b : md5.digest(str.getBytes("UTF-8"))) {
                result.append(Integer.toHexString((b&0xf0)>>>4).toUpperCase());
                result.append(Integer.toHexString(b&0x0f).toUpperCase());
            }
            if(bite_16){
            	return result.toString().substring(8,24);
            }
           
            return result.toString();
        }
        catch (Exception e) { return null; }
        // catch (UnsupportedEncodingException e) { return ""; }
    }
    
    public static byte[] base64Decode(byte[] bs) {
    	Decoder decoder = Base64.getDecoder();
    	return decoder.decode(bs);
//    	byte[] decode = 
//    	return new String(decode);
    }
    public static String base64Encode(byte[]bs) {
    	Encoder encoder = Base64.getEncoder();
//    	return encoder.encode(bs);
    	byte[] encode = encoder.encode(bs);
    	return new String(encode);
    }
    
    
	/*public static byte[] aesCipher(byte[] content, String password, boolean encrypt) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			if (encrypt) cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			else cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		}
		catch (NoSuchAlgorithmException e) { Util.e(TAG, e); }
		catch (NoSuchPaddingException e) { Util.e(TAG, e); }
		catch (InvalidKeyException e) { Util.e(TAG, e); }
		catch (IllegalBlockSizeException e) { Util.e(TAG, e); }
		catch (BadPaddingException e) { Util.e(TAG, e); }
		return null;
	}*/

}
